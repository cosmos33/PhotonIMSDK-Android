package com.cosmos.photonim.imbase.chat;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.filehandler.HttpFileHandler;
import com.cosmos.photonim.imbase.chat.filehandler.IFileHandler;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.TimeUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ChatModel extends IChatModel {
    private static final String TAG = "ChatModel";
    public static final int MSG_ERROR_CODE_SUCCESS = 0;//发送成功
    public static final int MSG_ERROR_CODE_SERVER_ERROR = 1000;//服务器内部错误
    public static final int MSG_ERROR_CODE_SERVER_NO_GROUP_MEMBER = 503;//非群成员不能发送
    public static final int MSG_ERROR_CODE_GROUP_CLOSED = 1008;//客户端群消息开关关闭
    public static final int MSG_ERROR_CODE_TEXT_ILLEGAL = 1001;//消息内容包含敏感词
    public static final int MSG_ERROR_CODE_PIC_ILLEGAL = 1002;//图片非法
    public static final int MSG_ERROR_CODE_FREQUENCY = 1003;//消息发送频率过高
    public static final int MSG_ERROR_CODE_CANT_REVOKE = 1004;//消息不可撤回
    public static final int MSG_ERROR_CODE_UPLOAD_FILE_FAILED = 2000;//上传失败  自己定义，非服务器返回
    public static final int MSG_ERROR_CODE_TIME_OUT = -1; // 发送超时，java层定义，非服务器返回
    private IFileHandler iFileHandler;

    public ChatModel() {
        iFileHandler = new HttpFileHandler();
    }

    @Override
    public void loadLocalHistory(int chatType, String chatWith, String anchorMsgId, boolean beforeAuthor, boolean asc, int size, String myId, OnLoadHistoryListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> getLocalHistory(chatType, chatWith, anchorMsgId, beforeAuthor, asc, size, myId),
                result -> {
                    Map map = (Map) result;
                    if (map == null) {
                        listener.onLoadHistory(null, null);
                        return;
                    }
                    listener.onLoadHistory((List<ChatData>) (map.get("list")), (Map<String, ChatData>) (map.get("map")));
                });
    }

    private Object getLocalHistory(int chatType, String chatWith, String anchorMsgId, boolean beforeAuthor, boolean asc, int size, String myId) {
//        ArrayList<PhotonIMMessage> photonIMMessages = PhotonIMDatabase.getInstance().findMessageListByIdRange(chatType, chatWith, anchorMsgId, beforeAuthor, asc, size);
        PhotonIMDatabase.LoadHistoryResult result = PhotonIMDatabase.getInstance().loadHistoryMessage(chatType, chatWith, anchorMsgId, size);
        ArrayList<PhotonIMMessage> photonIMMessages = (ArrayList<PhotonIMMessage>) result.loadMsgList;
        return convertMap(photonIMMessages);
    }

    private Object convertMap(ArrayList<PhotonIMMessage> photonIMMessages) {
        if (photonIMMessages == null) {
            return null;
        }
        Map<String, Object> r = new HashMap<>(2);
        List<ChatData> result = new ArrayList<>(photonIMMessages.size());
        Map<String, ChatData> resultMap = new HashMap<>(photonIMMessages.size());
        ChatData.Builder chatDataBuilder;
        ChatData preData = null;
        Profile profileTemp;
        String iconTemp;
        String nameTemp = null;
        int msgStatus;
        for (PhotonIMMessage photonIMMessage : photonIMMessages) {
            if (photonIMMessage.from.equals(ImBaseBridge.getInstance().getUserId())) {
                iconTemp = ImBaseBridge.getInstance().getMyIcon();
            } else {
                profileTemp = DBHelperUtils.getInstance().findProfile(photonIMMessage.from);
                iconTemp = profileTemp != null ? profileTemp.getIcon() : null;
                nameTemp = profileTemp != null ? profileTemp.getName() : null;
            }

            msgStatus = photonIMMessage.status;
//            if (photonIMMessage.status == PhotonIMMessage.SENDING) {//从数据库读取的消息状态如果是发送中，转换为发送失败
//                msgStatus = PhotonIMMessage.SEND_FAILED;
//            }
            chatDataBuilder = new ChatData.Builder()
                    .msgType(photonIMMessage.messageType)
                    .chatWith(photonIMMessage.chatWith)
                    .from(photonIMMessage.from)
                    .fromName(nameTemp)
                    .chatType(photonIMMessage.chatType)
                    .to(photonIMMessage.to)
                    .time(photonIMMessage.time)
                    .timeContent(TimeUtils.getTimeContent(photonIMMessage.time, preData == null ? 0 : preData.getTime()))
                    .notic(photonIMMessage.notic)
                    .remainHistory(photonIMMessage.remainHistory)
//                    .extra(photonIMMessage.extra)
//                    .icon(photonIMMessage)
                    .icon(iconTemp)
                    .msgId(photonIMMessage.id)
                    .msgStatus(msgStatus)
                    .itemType(getItemType(photonIMMessage, ImBaseBridge.getInstance().getUserId()))
                    .msgBody(photonIMMessage.body);
//            changeBody(photonIMMessage, chatDataBuilder);
            ChatData chatData = chatDataBuilder.build();
            result.add(chatData);
            resultMap.put(photonIMMessage.id, chatData);
            preData = chatData;
        }
        r.put("list", result);
        r.put("map", resultMap);
        return r;
    }

//    private void changeBody(PhotonIMMessage photonIMMessage, ChatData.Builder chatDataBuilder) {
//        switch (photonIMMessage.messageType) {
//            case PhotonIMMessage.LOCATION:
//                PhotonIMLocationBody body = (PhotonIMLocationBody) photonIMMessage.body;
//                chatDataBuilder.lat(body.lat);
//                chatDataBuilder.lng(body.lng);
//                chatDataBuilder.address(body.address);
//                chatDataBuilder.detailAddress(body.address);
//                break;
//            case PhotonIMMessage.IMAGE:
//                PhotonIMImageBody imageBody = (PhotonIMImageBody) photonIMMessage.body;
//                chatDataBuilder.localFile(imageBody.localFile);
//                chatDataBuilder.fileUrl(imageBody.url);
//                break;
//            case PhotonIMMessage.AUDIO:
//                PhotonIMAudioBody audioBody = (PhotonIMAudioBody) photonIMMessage.body;
//                chatDataBuilder.localFile(audioBody.localFile);
//                chatDataBuilder.fileUrl(audioBody.url);
//                chatDataBuilder.voiceDuration(audioBody.audioTime);
//                break;
//            case PhotonIMMessage.FILE:
//                PhotonIMFileBody fileBody = (PhotonIMFileBody) photonIMMessage.body;
//                chatDataBuilder.localFile(fileBody.localFile);
//                chatDataBuilder.fileUrl(fileBody.url);
//                chatDataBuilder.fileSize(SizeUtils.getSize(fileBody.size));
//                chatDataBuilder.fileName(FileUtils.getFileName(fileBody.localFile));
//                break;
//            case PhotonIMMessage.VIDEO:
//                PhotonIMVideoBody videoBody = (PhotonIMVideoBody) photonIMMessage.body;
//                chatDataBuilder.localFile(videoBody.localFile);
//                chatDataBuilder.fileUrl(videoBody.url);
//                chatDataBuilder.v
//                break;
//
//        }
//    }

    @Override
    public void loadAfterSearchMsgId(int chatType, String chatWith, String anchorMsgId, boolean beforeAuthor, boolean asc, int size, OnLoadHistoryListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> getAfterSearchMsgId(chatType, chatWith, anchorMsgId, beforeAuthor, asc, size),
                result -> {
                    Map map = (Map) result;
                    if (map == null) {
                        listener.onLoadHistory(null, null);
                        return;
                    }
                    listener.onLoadHistory((List<ChatData>) (map.get("list")), (Map<String, ChatData>) (map.get("map")));
                });
    }

    private Object getAfterSearchMsgId(int chatType, String chatWith, String anchorMsgId, boolean beforeAuthor, boolean asc, int size) {
        PhotonIMMessage message = PhotonIMDatabase.getInstance().findMessage(chatType, chatWith, anchorMsgId);
        ArrayList<PhotonIMMessage> result = PhotonIMDatabase.getInstance().findMessageListByIdRange(chatType, chatWith, anchorMsgId, beforeAuthor, asc, size);
        result.add(0, message);
        return convertMap(result);
    }

    @Override
    public void loadAllHistory(int chatType, String chatWith, int size, long beginTimeStamp, OnLoadHistoryListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> getAllHistoryFromServer(chatType, chatWith, size, beginTimeStamp),
                result -> {
                    if (listener == null) {
                        return;
                    }
                    Map map = (Map) result;
                    if (map == null) {
                        listener.onLoadHistory(null, null);
                    } else {
                        listener.onLoadHistory((List<ChatData>) (map.get("list")), (Map<String, ChatData>) (map.get("map")));
                    }
                });
    }

    @Override
    public void loadAllHistory(int chatType, String chatWith, String anchor, int size, long beginTimeStamp, long endTimeStamp, OnLoadHistoryListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> getAllHistoryFromServer(chatType, chatWith, anchor, size, beginTimeStamp, endTimeStamp),
                result -> {
                    if (listener == null) {
                        return;
                    }
                    Map map = (Map) result;
                    if (map == null) {
                        listener.onLoadHistory(null, null);
                    } else {
                        listener.onLoadHistory((List<ChatData>) (map.get("list")), (Map<String, ChatData>) (map.get("map")));
                    }
                });
    }

    private Object getAllHistoryFromServer(int chatType, String chatWith, int size, long beginTimeStamp) {
        ArrayList<PhotonIMMessage> resultList = new ArrayList<>();
        PhotonIMDatabase.SyncHistoryResult result = PhotonIMDatabase.getInstance().syncHistoryMessagesFromServer(chatType, chatWith, size, beginTimeStamp);
        if (result == null) {
            return null;
        }
        if (result.ec == 0) {
            resultList.addAll(result.syncMsgList);
        }
        return convertMap(resultList);
    }

    private Object getAllHistoryFromServer(int chatType, String chatWith, String anchor, int size, long beginTimeStamp, long endTimeStamp) {
        ArrayList<PhotonIMMessage> resultList = new ArrayList<>();
        PhotonIMDatabase.SyncHistoryResult result = PhotonIMDatabase.getInstance().syncHistoryMessagesFromServer(chatType, chatWith, anchor, size, beginTimeStamp, endTimeStamp);
        if (result == null) {
            return null;
        }
        if (result.ec == 0) {
            if (!CollectionUtils.isEmpty(result.syncMsgList)) {
                PhotonIMDatabase.getInstance().saveMessageBatch(chatType, chatWith, result.syncMsgList);
                resultList.addAll(result.syncMsgList);
            }
        }
        return convertMap(resultList);
    }

    public static int getItemType(PhotonIMMessage photonIMMessage, String myId) {
        if (photonIMMessage.status == PhotonIMMessage.RECALL) {
            return Constants.ITEM_TYPE_CHAT_SYSINFO;
        }
        return photonIMMessage.from.equals(myId) ? Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT : Constants.ITEM_TYPE_CHAT_NORMAL_LEFT;
    }

    @Override
    public void sendTextMsg(ChatData chatData) {
        sendTextMsgInner(chatData);
    }

    @Override
    public void deleteMsg(ChatData chatData, OnDeleteMsgListener onDeleteMsgListener) {
        ArrayList<ChatData> list = new ArrayList<>(1);
        list.add(chatData);
        deleteMsgs(list, onDeleteMsgListener);
    }

    @Override
    public void deleteMsgs(ArrayList<ChatData> chatData, OnDeleteMsgListener onDeleteMsgListener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
                    if (chatData == null || chatData.size() == 0) {
                        return null;
                    }
                    ArrayList<String> msgs = new ArrayList<>(chatData.size());
                    ChatData temp = chatData.get(0);
                    int chatType = temp.getChatType();
                    String chatWith = temp.getChatWith();
                    for (ChatData chatDatum : chatData) {
                        msgs.add(chatDatum.getMsgId());
                    }
                    PhotonIMClient.getInstance().sendDeleteMessage(chatType, chatWith, msgs,
                            new PhotonIMClient.PhotonIMSendCallback() {
                                @Override
                                public void onSent(int code, String msg, long time) {
                                    if (code == 0) {
                                        for (ChatData chatDatum : chatData) {
                                            PhotonIMDatabase.getInstance().deleteMessage(chatType, chatWith, chatDatum.getMsgId());
                                        }
                                        EventBus.getDefault().post(new ChatDataWrapper(chatData, code, msg, ChatDataWrapper.STATUS_DELETE));
                                    } else {
                                        onDeleteMsgListener.onDeletemsgResult(chatData, msg);
                                    }
                                }
                            });
                    return null;
                },
                result -> {

                });
    }

    private void sendTextMsgInner(ChatData chatData) {
        EventBus.getDefault().post(new ChatDataWrapper(chatData, PhotonIMMessage.SENDING, null));
        TaskExecutor.getInstance().createAsycTaskChat(() -> sendMsgInner(chatData, null));
    }

    @Override
    public void uploadFile(ChatData chatData, OnFileUploadListener onFileUploadListener) {
        iFileHandler.uploadFile(chatData, onFileUploadListener);
    }


    @Override
    public void updateAndsendMsg(ChatData chatData, OnMsgSendListener onMsgSendListener) {
        TaskExecutor.getInstance().createAsycTaskChat(() -> sendMsgInner(true, chatData, onMsgSendListener));
    }

    @Override
    public void sendMsgMulti(List<ChatData> chatDatas, OnMsgSendListener onMsgSendListener) {
        TaskExecutor.getInstance().createAsycTaskChat(() -> {
            if (chatDatas.size() <= 0)
                return null;
            for (ChatData chatDatum : chatDatas) {
                sendMsgInner(chatDatum, null);
            }
            return null;
        });
    }

//    @Override
//    public void sendVoiceFileMsg(ChatData chatData, OnMsgSendListener onMsgSendListener) {
//        TaskExecutor.getInstance().createAsycTaskChat(() -> sendMsgInner(chatData, onMsgSendListener));
//    }

    @Override
    public void revertMsg(ChatData data, OnRevertListener onRevertListener) {
        TaskExecutor.getInstance().createAsycTask(() -> sendRevertMsg(data, onRevertListener));
    }

    @Override
    public void sendReadMsg(ChatData messageData, OnSendReadListener onSendReadListener) {
        TaskExecutor.getInstance().createAsycTask(() -> sendReadMsgInner(messageData, onSendReadListener));
    }

    @Override
    public void getFile(ChatData data, String savePath, OnGetFileListener onGetFileListener) {
        iFileHandler.downloadFile(data, savePath, onGetFileListener);
    }

    @Override
    public void updateExtra(ChatData messageData) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMDatabase.getInstance()
                    .updateMessageExtra(messageData.getChatType(), messageData.getChatWith(),
                            messageData.getMsgId(), null);
            return null;
        });
    }

    @Override
    public void removeChat(int chatType, String chatWith, String id) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMDatabase.getInstance().deleteMessage(chatType, chatWith, id);
            return null;
        });
    }

    @Override
    public void updateStatus(int chatType, String chatWith, String id, int status) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                PhotonIMDatabase.getInstance().updateMessageStatus(chatType, chatWith, id, status);
                return null;
            }
        });
    }
    private Object sendReadMsgInner(ChatData messageData, OnSendReadListener onSendReadListener) {
        List<String> msgList = new ArrayList<>();
        msgList.add(messageData.getMsgId());

        PhotonIMClient.getInstance().sendReadMessage(messageData.getTo(), messageData.getFrom(), msgList, new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int error, String msg, long retTime) {
                LogUtils.log(TAG, String.format("send error:%d", error));
                if (error == ChatModel.MSG_ERROR_CODE_SUCCESS) {
                    PhotonIMDatabase.getInstance().updateMessageStatus(messageData.getChatType(), messageData.getChatWith(), messageData.getMsgId(), PhotonIMMessage.RECV_READ);
                }
                if (onSendReadListener != null) {
                    CustomRunnable customRunnable = new CustomRunnable.Builder()
                            .runnable(new Runnable() {
                                @Override
                                public void run() {
                                    onSendReadListener.onSendRead(messageData, error, msg);
                                }
                            }).build();
                    MainLooperExecuteUtil.getInstance().post(customRunnable);
                }
            }

        });
//
        return null;
    }

    private Object sendRevertMsg(ChatData data, OnRevertListener onRevertListener) {

        PhotonIMClient.getInstance().recallMessage(data.getChatType(), data.getFrom(), data.getTo(), data.getMsgId(), data.getTime(), new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int error, String msg, long retTime) {
                if (onRevertListener != null) {
                    CustomRunnable customRunnable = new CustomRunnable.Builder()
                            .runnable(new Runnable() {
                                @Override
                                public void run() {
                                    onRevertListener.onRevert(data, error, msg);
                                }
                            }).build();
                    MainLooperExecuteUtil.getInstance().post(customRunnable);
                }
            }
        });
        return null;
    }

    private Object sendMsgInner(boolean update, ChatData chatData, OnMsgSendListener onMsgSendListener) {
        PhotonIMMessage message = chatData.convertToIMMessage();
        if (update) {
            PhotonIMDatabase.getInstance().updateMessage(message);
        } else {
            PhotonIMDatabase.getInstance().saveMessage(message);
        }

        PhotonIMClient.getInstance().sendMessage(message, new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int code, String msg, long retTime) {
                EventBus.getDefault().post(new ChatDataWrapper(chatData, code, msg));
            }
        });

        return null;
    }

    private Object sendMsgInner(ChatData chatData, OnMsgSendListener onMsgSendListener) {
        return sendMsgInner(false, chatData, onMsgSendListener);
    }

    @Override
    public void clearChatContent(int chatType, String chatWith, OnClearChatContentListener onClearSessionListener) {
        TaskExecutor.getInstance().createAsycTask(() -> clearSessionInner(chatType, chatWith), result -> {
            if (onClearSessionListener != null) {
                onClearSessionListener.onClearChatContent();
            }
        });
    }

    @Override
    public void saveDraft(int chatType, String chatWith, String trim) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
                                                      @Override
                                                      public Object call() throws Exception {
                                                          PhotonIMDatabase.getInstance().updateSessionDraft(chatType, chatWith, trim);
                                                          return null;
                                                      }
                                                  }
        );
    }

    @Override
    public void getDraft(int chatType, String chatWith, OnGetDraftLitener onGetDraftLitener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
                    return PhotonIMDatabase.getInstance().getSessionDraft(chatType, chatWith);
                }, result -> {
                    if (onGetDraftLitener != null) {
                        onGetDraftLitener.onGetDraft((String) result);
                    }
                }
        );
    }

    private Object clearSessionInner(int chatType, String chatWith) {
        PhotonIMDatabase.getInstance().clearMessage(chatType, chatWith);
        return null;
    }
}
