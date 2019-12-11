package com.cosmos.photonim.imbase.chat;

import android.content.Context;
import android.os.Environment;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.chat.ichat.IChatPresenter;
import com.cosmos.photonim.imbase.chat.ichat.IChatView;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.FileUtils;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.VoiceHelper;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadImage;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadVoice;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import top.oply.opuslib.OpusRecorder;

public class ChatPresenter extends IChatPresenter<IChatView, IChatModel> {
    private static final String TAG = "ChatSetPresenter";
    private VoiceHelper voiceHelper;
    private volatile long voiceTimeStart;
    private Set<String> sendReadStatusSet;
    private Set<String> getUserInfoSet;

    public ChatPresenter(IChatView iView) {
        super(iView);
    }

    @Override
    public void loadLocalHistory(int chatType, String chatWith, String anchorMsgId,
                                 boolean beforeAuthor, boolean asc,
                                 int size, String myId) {
        getiModel().loadLocalHistory(chatType, chatWith, anchorMsgId, beforeAuthor, asc, size, myId,
                (chatData, chatDataMap) -> {
                    getIView().onloadHistoryResult(chatData, chatDataMap);
                });
    }

    @Override
    public void loadAllHistory(int chatType, String chatWith, int size, long beginTimeStamp) {
        getiModel().loadAllHistory(chatType, chatWith, size, beginTimeStamp,
                (chatData, chatDataMap) -> {
                    getIView().onloadHistoryResult(chatData, chatDataMap);
                });
    }

    @Override
    public void loadAllHistory(int chatType, String chatWith, int size, long beginTimeStamp, long endTimeStamp) {
        getiModel().loadAllHistory(chatType, chatWith, size, beginTimeStamp, endTimeStamp,
                (chatData, chatDataMap) -> {
                    getIView().onloadHistoryResult(chatData, chatDataMap);
                });
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

    @Override
    public ChatData sendMsg(ChatData.Builder chatDataBuilder) {
        ChatData chatData = chatDataBuilder
                .msgId(getMsgID())
                .time(System.currentTimeMillis())
                .itemType(Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT)
                .msgStatus((PhotonIMMessage.SENDING)).build();
        switch (chatDataBuilder.getMsgType()) {
            case PhotonIMMessage.TEXT:
                getiModel().sendTextMsg(chatData);
                break;
            case PhotonIMMessage.IMAGE:
                sendPicMsgInner(chatData);
                break;
            case PhotonIMMessage.AUDIO:
                sendVoice(chatData);
                break;
        }
        return chatData;
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
                sendVoice(chatData);
                break;
        }
    }

    private String getMsgID() {
        return UUID.randomUUID().toString();
    }

//    @Override
//    public void reSendText(int chatType, String msgId, String content, String chatWith, String fromId, String toId, String icon) {
//        sendTextInner(chatType, msgId, content, chatWith, fromId, toId, icon);
//    }

//    private void sendTextInner(int chatType, String msgId, String content, String chatWith, String fromId, String toId, String icon) {
//        getiModel().sendTextMsg(chatType, msgId, content, chatWith, fromId, toId, icon, null);
//    }

//    @Override
//    public void sendPic(int chatType, String absolutePath, String chatWith, String fromId, String toId, String icon) {
//        sendPicInner(chatType, null, absolutePath, chatWith, fromId, toId, icon);
//    }

//    @Override
//    public void reSendPic(int chatType, String msgId, String absolutePath, String chatWith, String fromId, String toId, String icon) {
//        sendPicInner(chatType, msgId, absolutePath, chatWith, fromId, toId, icon);
//    }

//    private void sendPicInner(int chatType, String msgId, String absolutePath, String chatWith, String fromId, String toId, String icon) {
//        getiModel().uploadPic(chatType, msgId, absolutePath, icon, chatWith, fromId, toId, new IGroupInfoModel.OnMsgSendListener() {
//
//            @Override
//            public void onMsgSend(int code, String codeMsg, ChatData chatData) {
//            }
//        }, (chatData, result) -> {
//            if (result != null && result.success()) {
//                JsonUploadImage jsonUploadImage = (JsonUploadImage) result.get();
//                String url = jsonUploadImage.getData().getUrl();
//                chatData.setFileUrl(url);
//                getiModel().sendPicMsg(chatData, null);
//            } else {
//                getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
//                EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传图片失败"));
//            }
//        });
//    }

    private void sendPicMsgInner(ChatData chatDataTemp) {
        getiModel().uploadPic(chatDataTemp, (chatData, result) -> {
            if (result != null && result.success()) {
                JsonUploadImage jsonUploadImage = (JsonUploadImage) result.get();
                String url = jsonUploadImage.getData().getUrl();
                chatData.setFileUrl(url);
                getiModel().sendPicMsg(chatData, null);
            } else {
                getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
                EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传图片失败"));
            }
        });
    }

//    @Override
//    public void sendVoice(int chatType, String absolutePath, long time, String chatWith, String fromId, String toId, String icon) {
//        sendVoice(chatType, null, absolutePath, time, chatWith, fromId, toId, icon);
//    }

//    @Override
//    public void reSendVoice(int chatType, String msgId, String absolutePath, long time, String chatWith, String fromId, String toId, String icon) {
//        sendVoice(chatType, msgId, absolutePath, time, chatWith, fromId, toId, icon);
//    }

//    private void sendVoice(int chatType, String msgId, String absolutePath, long time, String chatWith, String fromId, String toId, String icon) {
//        getiModel().uploadVoiceFile(chatType, msgId, absolutePath, time, icon, chatWith, fromId, toId, null, (chatData, result) -> {
//            if (result.success()) {
//                JsonUploadVoice jsonUploadImage = (JsonUploadVoice) result.get();
//                String url = jsonUploadImage.getData().getUrl();
//                chatData.setFileUrl(url);
//                getiModel().sendVoiceFileMsg(chatData, null);
//            } else {
//                getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
//                EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传语音失败"));
//            }
//        });
//    }

    private void sendVoice(ChatData chatDataTemp) {
        getiModel().uploadVoiceFile(chatDataTemp, (chatData, result) -> {
            if (result.success()) {
                JsonUploadVoice jsonUploadImage = (JsonUploadVoice) result.get();
                String url = jsonUploadImage.getData().getUrl();
                chatData.setFileUrl(url);
                getiModel().sendVoiceFileMsg(chatData, null);
            } else {
                getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
                EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传语音失败"));
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
                getIView().onRecordFinish(System.currentTimeMillis() - voiceTimeStart);
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
    public void getVoiceFile(ChatData chatData) {
        String fileUrlName = getFileUrlName(chatData.getFileUrl());
        if (fileUrlName == null) {
            LogUtils.log(TAG, "fileurl == null");
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory(), FileUtils.VOICE_PATH_RECEIVE + fileUrlName);
        FileUtils.createFile(file);
        getiModel().getVoiceFile(chatData, file.getAbsolutePath(), new IChatModel.OnGetFileListener() {
            @Override
            public void onGetFile(String path) {
                getIView().onGetChatVoiceFileResult(chatData, path);
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
        ImBaseBridge.IGetUserIconListener iGetUserIcon = ImBaseBridge.getInstance().getiGetUserIconListener();
        if (iGetUserIcon != null) {
            iGetUserIcon.getUserIcon(chatData.getFrom(), new ImBaseBridge.OnGetUserIconListener() {
                @Override
                public void onGetUserIcon(String iconUrl, String name) {
                    getUserInfoSet.remove(chatData.getFrom());
                    getIView().onGetIcon(chatData, iconUrl, name);
                }
            });
        }
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
}
