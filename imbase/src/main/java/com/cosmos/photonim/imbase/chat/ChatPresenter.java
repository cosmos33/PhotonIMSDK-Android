package com.cosmos.photonim.imbase.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.cosmos.maplib.map.MyLocation;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.album.AlbumFragment;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.cosmos.photonim.imbase.chat.file.FileActivity;
import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.chat.ichat.IChatPresenter;
import com.cosmos.photonim.imbase.chat.ichat.IChatView;
import com.cosmos.photonim.imbase.chat.map.MapActivity;
import com.cosmos.photonim.imbase.chat.media.takephoto.TakePhotoResultFragment;
import com.cosmos.photonim.imbase.chat.media.video.RecordResultFragment;
import com.cosmos.photonim.imbase.chat.media.video.VideoInfo;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.FileUtils;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.TimeUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.VoiceHelper;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.event.ClearUnReadStatus;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadImage;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.cosmos.photonim.imbase.view.ChatToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import top.oply.opuslib.OpusRecorder;

public class ChatPresenter extends IChatPresenter<IChatView, IChatModel> {
    private static final int IMAGE_MAX_SIZE = 10 * 1024 * 1024;
    private static final int LIMIT_LOADREMOTE = 200;
    private static final int PAGE_ONE = 50;
    private static final String TAG = "ChatSetPresenter";
    protected static final String AT_ALL_CONTENT = "所有人 ";

    private VoiceHelper voiceHelper;
    private volatile long voiceTimeStart;
    private Set<String> sendReadStatusSet;
    private Set<String> getUserInfoSet;

    private int chatType;
    protected String chatWith;
    protected String myIcon;
    private String loginUserId;
    private String searchMsgId;

    private List<ChatData> chatMsg;
    private HashMap<String, ChatData> chatMsgMap;//key:msgId
    private boolean lastLoadHistoryFromRemote = false;
    private boolean firstLoad = true;
    private HashMap<String, ChatData> downloadData;
    private boolean loadAllHistoryFormServer = true;

    public ChatPresenter(IChatView iView) {
        super(iView);
    }

    @Override
    public void onCreate(int chatType, String chatWith, String myIcon, String searchMsgId) {
        this.chatType = chatType;
        this.chatWith = chatWith;
        this.myIcon = myIcon;
        this.searchMsgId = searchMsgId;
        loginUserId = ImBaseBridge.getInstance().getUserId();
    }

    @Override
    public void loadLocalHistory(String anchorMsgId,
                                 boolean beforeAuthor, boolean asc,
                                 int size, String myId) {
        getiModel().loadLocalHistory(chatType, chatWith, anchorMsgId, beforeAuthor, asc, size, myId,
                (chatData, chatDataMap) -> {
                    onloadHistoryResult(chatData, chatDataMap, false);
                });
    }

    @Override
    public void loadAfterSearchMsgId(String searchMsgId, boolean beforeAuthor, boolean asc, int size) {
        getiModel().loadAfterSearchMsgId(chatType, chatWith, searchMsgId, beforeAuthor, asc, size,
                (chatData, chatDataMap) -> {
                    onloadHistoryResult(chatData, chatDataMap, true);
                });
    }

    @Override
    public void loadAllHistory(int size, long beginTimeStamp) {
        getiModel().loadAllHistory(chatType, chatWith, size, beginTimeStamp,
                (chatData, chatDataMap) -> {
                    onloadHistoryResult(chatData, chatDataMap, false);
                });
    }

    @Override
    public void loadAllHistory(int size, long beginTimeStamp, long endTimeStamp) {
        getiModel().loadAllHistory(chatType, chatWith, size, beginTimeStamp, endTimeStamp,
                (chatData, chatDataMap) -> {
                    onloadHistoryResult(chatData, chatDataMap, false);
                });
    }

    private void onloadHistoryResult(List<ChatData> chatData, Map<String, ChatData> chatDataMap, boolean search) {
//        if(!loadAllHistoryFormServer){
        if (lastLoadHistoryFromRemote) {//服务器没有历史消息了需要加载本地
            if (loadAllHistoryFormServer) {
                getHistory(false, 0L, chatMsg.size() == 0 ? "" : chatMsg.get(0).getMsgId());
            } else {
                if (chatMsg.size() > 0) {
                    ChatData chatDataTemp = chatMsg.get(0);
                    getHistory(false, 0L, chatDataTemp.getMsgId());
                } else {
                    getHistory(false, 0L, "");
                }
            }
            return;
        }
//        }
        getIView().setRefreshing(false);
        if (CollectionUtils.isEmpty(chatData)) {
//            presenter.loadRemoteHistory();
            getIView().toast(R.string.chat_msg_nomore);
        } else {
            chatMsg.addAll(0, chatData);
            chatMsgMap = (HashMap<String, ChatData>) chatDataMap;
            getIView().notifyItemRangeInserted(0, chatData.size());
            if (!search) {
                if (firstLoad) {
                    firstLoad = false;
                    getIView().scrollToPosition(chatData.size() - 1);
                }
            }
        }
    }

    private void getHistory() {
        loadAfterSearchMsgId(searchMsgId, false, false, Integer.MAX_VALUE);
    }

    private void getHistory(boolean loadFromRemote, long endTimeStamp, String anchorMsgId) {
        if (loadFromRemote) {
            lastLoadHistoryFromRemote = true;
//            presenter.loadAllHistory(chatType, chatWith, PAGE_ONE, beginTimeStamp);
            loadAllHistory(PAGE_ONE, 0, endTimeStamp);
        } else {
            lastLoadHistoryFromRemote = false;
            loadLocalHistory(anchorMsgId, true, false,
                    PAGE_ONE, loginUserId);
        }
    }

    //    @Override
//    public void loadRemoteHistory() {
//        getiModel().loadRemoteHistorey(new IChatModel.OnLoadHistoryListener() {
//            @Override
//            public void onLoadHistory(List<ChatData> chatData, Map<String, ChatData> chatDataMap) {
//                getIView().loadRemoteHistorey(chatData, chatDataMap);
//            }
//        });
//    }

//    @Override
//    public void sendText(int chatType, String content, String chatWith, String fromId, String toId, String icon) {
//        sendTextInner(chatType, null, content, chatWith, fromId, toId, icon);
//    }

    public ChatData sendMsg(ChatData.Builder chatDataBuilder) {
        ChatData chatData = chatDataBuilder
                .msgId(getMsgID())
                .time(System.currentTimeMillis())
                .itemType(Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT)
                .msgStatus((PhotonIMMessage.SENDING)).build();
        switch (chatDataBuilder.getMsgType()) {
            case PhotonIMMessage.TEXT:
            case PhotonIMMessage.LOCATION:
                getiModel().sendTextMsg(chatData);
                break;
            case PhotonIMMessage.IMAGE:
                sendPicMsgInner(chatData);
                break;
            case PhotonIMMessage.AUDIO:
            case PhotonIMMessage.FILE:
            case PhotonIMMessage.VIDEO:
                sendFile(chatData);
                break;
        }
        return chatData;
    }

    private void sendFile(ChatData chatData) {
        EventBus.getDefault().post(new ChatDataWrapper(chatData, PhotonIMMessage.SENDING, null));
        if (TextUtils.isEmpty(chatData.getFileUrl())) {// TODO: 2020-02-05 maybe judge file is exist?
            getiModel().uploadFile(chatData, new IChatModel.OnFileUploadListener() {
                @Override
                public void onFileUpload(boolean success, ChatData chatData) {
                    if (success) {
                        getiModel().updateAndsendMsg(chatData, null);
                    } else {
                        getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
                        EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传失败"));
                    }
                }
            });
        } else {
            getiModel().updateAndsendMsg(chatData, null);
        }
    }

    @Override
    public void reSendMsg(ChatData chatData) {
        chatData.setNotic("");
        chatData.setMsgStatus(PhotonIMMessage.SENDING);
        chatData.setTime(System.currentTimeMillis());
        switch (chatData.getMsgType()) {
            case PhotonIMMessage.TEXT:
                getiModel().sendTextMsg(chatData);
                break;
            case PhotonIMMessage.IMAGE:
                sendPicMsgInner(chatData);
                break;
            case PhotonIMMessage.AUDIO:
            case PhotonIMMessage.VIDEO:
            case PhotonIMMessage.FILE:
                sendFile(chatData);
                break;
        }
    }

    @Override
    public void deleteMsg(ChatData data) {
        getiModel().deleteMsg(data, new IChatModel.OnDeleteMsgListener() {
            @Override
            public void onDeletemsgResult(ChatData data, String error) {
                if (!TextUtils.isEmpty(error)) {
                    getIView().toast(error);
                }
            }
        });
    }

    @Override
    public void onReceiveMsg(PhotonIMMessage msg) {
        if (!msg.chatWith.equals(chatWith)) {
            return;
        }

        if (msg.status == PhotonIMMessage.RECV_READ && msg.msgId != null) {
            for (String msgId : msg.msgId) {
                ChatData chatData = chatMsgMap.get(msgId);
                if (chatData == null) {
                    LogUtils.log(TAG, "update status :chatData is null");
                    continue;
                }
                chatData.setMsgStatus(PhotonIMMessage.SENT_READ);
                getIView().notifyItemChanged(chatData.getListPostion());
            }
            return;
        }
        // 删除demo层消息去重，防止和sdk2.0混淆
//        if (chatMsgMap.get(msg.id) != null) {
//            LogUtils.log(TAG, String.format("消息重复：%s", msg.id));
//            return;
//        }
        if (msg.status == PhotonIMMessage.RECALL) {
            for (String msgId : msg.msgId) {
                ChatData chatData = chatMsgMap.get(msgId);
                if (chatData == null) {
                    LogUtils.log(TAG, "update status :chatData is null");
                    continue;
                }
                chatData.setContent(msg.notic);
                chatData.setMsgStatus(PhotonIMMessage.RECALL);
                getIView().notifyItemChanged(chatData.getListPostion());
            }
            return;
        }
        ChatData messageData;
        messageData = new ChatData.Builder()
                .chatWith(msg.chatWith)
                .to(msg.to)
                .from(msg.from)
                .time(msg.time)
                .itemType(msg.from.equals(loginUserId) ? Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT : Constants.ITEM_TYPE_CHAT_NORMAL_LEFT)
                .msgStatus(msg.status)
                .icon(getIView().getChatIcon(msg))
                .msgId(msg.id)
                .timeContent(getTimeContent(msg.time))
                .msgType(msg.messageType)
                .chatType(msg.chatType)
                .msgBody(msg.body)
                .build();
        chatMsg.add(messageData);
        chatMsgMap.put(msg.id, messageData);
        getIView().notifyItemInserted(chatMsg.size() - 1);
        getIView().scrollToPosition(chatMsg.size() - 1);
    }

    private String getTimeContent(long curTime) {
        return TimeUtils.getTimeContent(curTime, CollectionUtils.isEmpty(chatMsg) ? 0 : chatMsg.get(chatMsg.size() - 1).getTime());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            sendImage(data);
        } else if (requestCode == Constants.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            sendPhoto(data);
        } else if (requestCode == Constants.REQUEST_VIDEO && resultCode == Activity.RESULT_OK) {
            sendVideo(data);
        } else if (requestCode == Constants.REQUEST_MAP && resultCode == Activity.RESULT_OK) {
            sendMap(data);
        } else if (requestCode == Constants.REQUEST_FILE && resultCode == Activity.RESULT_OK) {
            sendFile(data);
        }
    }

    @Override
    public void onDestory() {
        cancelPlay();
        destoryVoiceHelper();
    }

    @Override
    public List<ChatData> initData() {
        chatMsg = new ArrayList<>();
        chatMsgMap = new HashMap<>();
        return chatMsg;
    }

    public void clearData() {
        if (chatMsg != null) {
            chatMsg.clear();
        }
        if (chatMsgMap != null) {
            chatMsgMap.clear();
        }
    }

    @Override
    public void loadHistory() {
        if (searchMsgId == null) {
            if (loadAllHistoryFormServer) {
                getHistory(true, System.currentTimeMillis(), "");//全部从服务器获取消息
            } else {
                getHistory(false, 0L, "");
            }
        } else {
            getHistory();
        }
    }

    @Override
    public void loadMore() {
        if (chatMsg.size() > 0) {
            ChatData chatData = chatMsg.get(0);
            if (loadAllHistoryFormServer) {
                getHistory(true, chatData.getTime(), chatData.getMsgId());
            } else {
                if (!getIView().isGroup() && chatMsg.size() >= LIMIT_LOADREMOTE) {//单人消息从本地拉取LIMIT_LOADREMOTE条之后，从服务器拉取
                    getHistory(true, chatData.getTime(), chatData.getMsgId());
                } else {
                    getHistory(chatData.isRemainHistory(), chatData.getTime(), chatData.getMsgId());
                }
            }
        } else {
            if (loadAllHistoryFormServer) {
                getHistory(true, 0L, "");
            } else {
                getHistory(false, 0L, "");
            }
        }
    }

    @Override
    public void onSendChatData(ChatDataWrapper chatDataWrapper) {
        if (!chatDataWrapper.chatData.getChatWith().equals(chatWith)) {
            return;
        }

        switch (chatDataWrapper.code) {
            case PhotonIMMessage.SENDING:
                addNewMsg(chatDataWrapper.chatData);
                break;
            case ChatModel.MSG_ERROR_CODE_CANT_REVOKE:
                getIView().toast(R.string.chat_revoke_failed);
                break;
            default:
                changeDataStatus(chatDataWrapper.chatData, chatDataWrapper.code, chatDataWrapper.msg, chatDataWrapper.status);
        }
    }

    @Override
    public void removeData(ChatData chatData) {
        chatMsg.remove(chatData.getListPostion());
        chatMsgMap.remove(chatData.getMsgId());
    }

    @Override
    public int getImageUrls(ChatData chatData, ArrayList<ChatData> urls) {
        ChatData chatDataTemp;
        int currentPosition = 0;
        for (int i = 0; i < chatMsg.size(); i++) {
            chatDataTemp = chatMsg.get(i);
            if (chatDataTemp.getMsgType() != PhotonIMMessage.IMAGE) {
                continue;
            }
            urls.add(chatDataTemp);
            if (chatData.getMsgId().equals(chatDataTemp.getMsgId())) {
                currentPosition = urls.size() - 1;
            }
        }
        return currentPosition;
    }

    @Override
    public void sendText(String content) {
        ChatData.Builder chatDataBuilder = new ChatData.Builder()
                .content(content)
                .icon(myIcon)
                .msgType(PhotonIMMessage.TEXT)
                .chatWith(chatWith)
                .chatType(chatType)
                .from(loginUserId)
                .to(chatWith);
        getAtStatus(chatDataBuilder);
        sendMsg(chatDataBuilder);
    }

    private void getAtStatus(ChatData.Builder chatDataBuilder) {
        ArrayList<AtEditText.Entity> atList = getIView().getAtList();
        if (CollectionUtils.isEmpty(atList)) {
            return;
        }
        List<String> atMsgList = new ArrayList<>(atList.size());
        for (AtEditText.Entity entity : atList) {
            atMsgList.add(entity.getId());
            if (entity.getName().equals(AT_ALL_CONTENT)) {
                chatDataBuilder.atType(PhotonIMMessage.MSG_AT_ALL);
                return;
            }
        }
        chatDataBuilder.atType(PhotonIMMessage.MSG_NO_AT_ALL).msgAtList(atMsgList);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            EventBus.getDefault().post(new ClearUnReadStatus(chatType, chatWith));
        }
    }

    private void addNewMsg(ChatData chatData) {
        chatData.setTimeContent(getTimeContent(chatData.getTime()));
        chatMsg.add(chatData);
//        chatData.setListPostion(chatMsg.size() - 1);
        chatMsgMap.put(chatData.getMsgId(), chatData);
        getIView().notifyItemInserted(chatMsg.size());
        getIView().scrollToPosition(chatMsg.size() - 1);
    }

    private void changeDataStatus(ChatData chatData, int code, String msg, int status) {
        ChatData temp = chatMsgMap.get(chatData.getMsgId());
        if (temp == null) {
            LogUtils.log(TAG, "chatData is null");
            return;
        }
        switch (code) {
            case ChatModel.MSG_ERROR_CODE_SUCCESS:
                temp.setMsgStatus(PhotonIMMessage.SENT);
                break;
            case ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED:
            case ChatModel.MSG_ERROR_CODE_SERVER_ERROR:
                temp.setMsgStatus(PhotonIMMessage.SEND_FAILED);
                if (!TextUtils.isEmpty(msg)) {
                    temp.setNotic(msg);
                }
                ChatToastUtils.showChatSendFailedWarn();
                break;
            case ChatModel.MSG_ERROR_CODE_TEXT_ILLEGAL:
            case ChatModel.MSG_ERROR_CODE_PIC_ILLEGAL:
            case ChatModel.MSG_ERROR_CODE_SERVER_NO_GROUP_MEMBER:
            case ChatModel.MSG_ERROR_CODE_GROUP_CLOSED:
                temp.setNotic(msg);
                break;
            case ChatModel.MSG_ERROR_CODE_FREQUENCY:
                getIView().toast(R.string.chat_send_failed_frequency);
                // TODO: 2019-08-07 是否删除该条
                break;
            case ChatModel.MSG_ERROR_CODE_TIME_OUT:
                temp.setMsgStatus(PhotonIMMessage.SEND_FAILED);
                break;
            default:
                temp.setNotic(msg);

        }
        if (status == ChatDataWrapper.STATUS_DELETE) {
            onDeleteMsgResult(chatData);
        } else {
            //需要从chatMsgMap读取：有可能是退出聊天再次进入
            getIView().notifyItemChanged(temp.getListPostion());
        }
    }

    private String getMsgID() {
        return UUID.randomUUID().toString();
    }

    private void sendPicMsgInner(ChatData chatDataTemp) {
        getiModel().uploadPic(chatDataTemp, (chatData, result) -> {
            if (result != null && result.success()) {
                JsonUploadImage jsonUploadImage = (JsonUploadImage) result.get();
                String url = jsonUploadImage.getData().getUrl();
                chatData.setFileUrl(url);
                getiModel().updateAndsendMsg(chatData, null);
            } else {
                getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
                EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传图片失败"));
            }
        });
    }



    @Override
    public void revertMsg(ChatData data) {
        getiModel().revertMsg(data, (data1, error, msg) -> getIView().onRevertResult(data1, error, msg));
    }

    @Override
    public void sendReadMsg(ChatData messageData) {
        if (sendReadStatusSet == null) {
            sendReadStatusSet = new HashSet<>();
        }
        if (sendReadStatusSet.contains(messageData.getMsgId())) {
            LogUtils.log(TAG, String.format("msg with id:%s read status sending", messageData.getMsgId()));
            return;
        }
        sendReadStatusSet.add(messageData.getMsgId());
        getiModel().sendReadMsg(messageData, new IChatModel.OnSendReadListener() {
            private int retryCount = 3;
            private int retryNum = 0;

            @Override
            public void onSendRead(ChatData data, int error, String msg) {
                sendReadStatusSet.remove(data.getMsgId());
                if (error != ChatModel.MSG_ERROR_CODE_SUCCESS && retryNum < retryCount) {
                    LogUtils.log(TAG, String.format("sendread times:%d", ++retryNum));
                    getiModel().sendReadMsg(messageData, this);
                } else {
                    getIView().updateUnreadStatus(data);
                }
            }
        });
    }

    @Override
    public File startRecord(Context context) {
        init(context);
        File file = new File(Environment.getExternalStorageDirectory(), FileUtils.VOICE_PATH_SEND + String.format("voice_%d", System.currentTimeMillis()));
        FileUtils.createFile(file);

        voiceHelper.record(file.getAbsolutePath(), new OpusRecorder.OnRecordStatusListener() {
            @Override
            public void onRecordStart() {
                voiceTimeStart = System.currentTimeMillis();
            }
        });
        return file;
    }

    private void init(Context context) {
        if (voiceHelper != null) {
            return;
        }
        voiceHelper = new VoiceHelper(context, new VoiceHelper.OnVoiceListener() {
            @Override
            public void onRecordFinish() {
                long duration = System.currentTimeMillis() - voiceTimeStart;
                if (duration < 1000) {
                    ChatToastUtils.showChatTimeWarn();
                    cancelRecord();
                    return;
                }
                ChatData.Builder chatDataBuilder = new ChatData.Builder()
                        .msgStatus(PhotonIMMessage.SENDING)
                        .icon(myIcon)
                        .localFile(getIView().getVideoFilePath())
                        .msgType(PhotonIMMessage.AUDIO)
                        .chatType(chatType)
                        .voiceDuration(duration / 1000)
                        .chatWith(chatWith)
                        .from(loginUserId)
                        .to(chatWith);

                sendMsg(chatDataBuilder);
            }

            @Override
            public void onRecordFailed() {
                getIView().onRecordFailed();
            }
        });
    }

    @Override
    public void stopRecord() {
        if (voiceHelper != null) {
            voiceHelper.stopRecord();
        }
    }

    @Override
    public void play(Context context, String fileName) {
        init(context);
        voiceHelper.play(fileName);
    }

    @Override
    public void cancelPlay() {
        if (voiceHelper != null) {
            voiceHelper.cancelPlay();
        }
    }

    @Override
    public void cancelRecord() {
        if (voiceHelper != null)
            voiceHelper.cancelRecord();
    }

    @Override
    public void downLoadFile(ChatData chatData) {
        String fileUrlName = getFileUrlName(chatData.getFileUrl());
        if (fileUrlName == null) {
            LogUtils.log(TAG, "fileurl == null");
            return;
        }
        if (downloadData != null && downloadData.get(chatData.getMsgId()) != null) {
            getIView().toast("下载中");
            return;
        }
        if (downloadData == null) {
            downloadData = new HashMap<>();
        }
        downloadData.put(chatData.getMsgId(), chatData);
//        File file = new File(Environment.getExternalStorageDirectory(), FileUtils.VOICE_PATH_RECEIVE + fileUrlName);
//        FileUtils.createFile(file);
        chatData.setDownloading(true);
        getIView().notifyItemChanged(chatData.getListPostion());
        getiModel().getFile(chatData, null, new IChatModel.OnGetFileListener() {
            @Override
            public void onGetFile(String path) {
                downloadData.remove(chatData.getMsgId());
                chatData.setDownloading(false);
                if (TextUtils.isEmpty(path)) {
                    getIView().toast("下载失败");
                    return;
                }
                getIView().onGetChatVoiceFileResult(chatData, path);
            }

            @Override
            public void onProgress(ChatData chatData, int progress) {
                chatData.setDownloadProgress(progress);
                LogUtils.log(String.format("download progress %d", progress));
                CustomRunnable customRunnable = new CustomRunnable.Builder()
                        .runnable(new Runnable() {
                            @Override
                            public void run() {
                                getIView().notifyItemChanged(chatData.getListPostion());
                            }
                        }).build();
                MainLooperExecuteUtil.getInstance().post(customRunnable);
            }
        });
    }

    @Override
    public void updateExtra(ChatData messageData) {
        getiModel().updateExtra(messageData);
    }

    @Override
    public void removeChat(int chatType, String chatWith, String id) {
        // TODO: 2019-08-16 线程安全
        getiModel().removeChat(chatType, chatWith, id);
    }

    @Override
    public void getInfo(ChatData chatData) {
        if (getUserInfoSet == null) {
            getUserInfoSet = new HashSet<>();
        }
        if (getUserInfoSet.contains(chatData.getFrom())) {
            return;
        }
        getUserInfoSet.add(chatData.getFrom());
        ImBaseBridge.getInstance().getUserIcon(chatData.getFrom(), new ImBaseBridge.OnGetUserIconListener() {
            @Override
            public void onGetUserIcon(String iconUrl, String name) {
                getUserInfoSet.remove(chatData.getFrom());
                getIView().onGetIcon(chatData, iconUrl, name);
            }
        });
    }

    @Override
    public void destoryVoiceHelper() {
        if (voiceHelper != null) {
            voiceHelper.destory();
        }
    }

    private String getFileUrlName(String fileUrl) {
        if (fileUrl == null) {
            return null;
        }
        String[] split = fileUrl.split("/");
        return split[split.length - 1];
    }

    @Override
    public IChatModel generateIModel() {
        return new ChatModel();
    }

    private void sendFile(Intent data) {
        ArrayList<FileItemData> result = (ArrayList<FileItemData>) data.getSerializableExtra(FileActivity.INTENT_FILE);
        if (result == null) {
            return;
        }
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        for (FileItemData file : result) {
            ChatData.Builder chatDataBuild = new ChatData.Builder()
                    .icon(myIcon)
                    .localFile(file.getFilePath())
                    .fileName(file.getFileName())
                    .fileSizeL(file.getSizeL())
                    .msgType(PhotonIMMessage.FILE)
                    .chatType(chatType)
                    .chatWith(chatWith)
                    .from(loginUserId)
                    .to(chatWith);

            sendMsg(chatDataBuild);
        }
    }
    private void sendMap(Intent data) {
        MyLocation result = (MyLocation) data.getSerializableExtra(MapActivity.MAP_LOCATION);
        if (result == null) {
            return;
        }
        ChatData.Builder chatDataBuild = new ChatData.Builder()
                .icon(myIcon)
                .msgType(PhotonIMMessage.LOCATION)
                .chatType(chatType)
                .chatWith(chatWith)
                .from(loginUserId)
                .to(chatWith)
                .lat(result.lat)
                .lng(result.lng)
                .address(result.address)
                .detailAddress(result.detailedAddress);
        sendMsg(chatDataBuild);
    }

    private void sendVideo(Intent data) {
        VideoInfo result = (VideoInfo) data.getSerializableExtra(RecordResultFragment.BUNDLE_VIDEO);
        if (result == null) {
            return;
        }
        ChatData.Builder chatDataBuild = new ChatData.Builder()
                .icon(myIcon)
                .localFile(result.path)
                .videoCover(result.videoCoverPath)
                .videoTime(result.videoTime)
                .msgType(PhotonIMMessage.VIDEO)
                .chatType(chatType)
                .chatWith(chatWith)
                .from(loginUserId)
                .to(chatWith);
        sendMsg(chatDataBuild);
    }

    private void sendPhoto(Intent data) {
        String result = data.getStringExtra(TakePhotoResultFragment.BUNDLE_PHOTO_PATH);
        if (result == null) {
            return;
        }
        if (new File(result).length() >= IMAGE_MAX_SIZE) {
            ToastUtils.showText("仅支持发送10M以内的图片");
            return;
        }
        ChatData.Builder chatDataBuild = new ChatData.Builder()
                .icon(myIcon)
                .localFile(result)
                .msgType(PhotonIMMessage.IMAGE)
                .chatType(chatType)
                .chatWith(chatWith)
                .from(loginUserId)
                .to(chatWith);
        sendMsg(chatDataBuild);
    }

    protected void sendImage(Intent data) {
//        Uri originalUri = data.getData(); // 获得图片的uri
//        String filePath = Utils.getFilePath(ImBaseBridge.getInstance().getApplication(), originalUri);
//        if (new File(filePath).length() >= IMAGE_MAX_SIZE) {
//            ToastUtils.showText("仅支持发送10M以内的图片");
//            return;
//        }
        ArrayList<CategoryFile> files = data.getParcelableArrayListExtra(AlbumFragment.ALBUM);

        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (CategoryFile file : files) {
            ChatData.Builder chatDataBuild = new ChatData.Builder()
                    .icon(myIcon)
                    .localFile(file.mPath)
                    .msgType(PhotonIMMessage.IMAGE)
                    .chatType(chatType)
                    .chatWith(chatWith)
                    .from(loginUserId)
                    .to(chatWith);

            sendMsg(chatDataBuild);
        }
    }

    public void onDeleteMsgResult(ChatData chatData) {
        chatMsg.remove(chatData.getListPostion());
        chatMsgMap.remove(chatData.getMsgId());
        getIView().notifyDataSetChanged();//全部更新一下，要不然chatData里边的listposition就不对了
    }
}
