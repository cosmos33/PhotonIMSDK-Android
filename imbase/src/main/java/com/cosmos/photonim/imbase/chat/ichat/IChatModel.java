package com.cosmos.photonim.imbase.chat.ichat;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class IChatModel implements IModel {
    public abstract void loadLocalHistory(int chatType, String chatWith, String anchorMsgId,
                                          boolean beforeAuthor, boolean asc,
                                          int size, String myId, OnLoadHistoryListener listener);

    public abstract void loadAfterSearchMsgId(int chatType, String chatWith, String anchorMsgId, boolean beforeAuthor, boolean asc, int size, OnLoadHistoryListener listener);
    public abstract void loadAllHistory(int chatType, String chatWith, int size, long beginTimeStamp, OnLoadHistoryListener listener);

    public abstract void loadAllHistory(int chatType, String chatWith, String anchor, int size, long beginTimeStamp, long endTimeStamp, OnLoadHistoryListener listener);
//    public abstract void loadRemoteHistorey(OnLoadHistoryListener listener);

//    public abstract void sendTextMsg(int chatType, String messagId, String content, String chatWith, String fromId, String toId,
//                                     String icon, OnMsgSendListener onMsgSendListener);
//
//    public abstract void sendTextMsg(int chatType, String content, String chatWith, String fromId, String toId,
//                                     String icon, OnMsgSendListener onMsgSendListener);
//
//    public abstract void uploadPic(int chatType, String absolutePath, String icon, String chatWith, String from,
//                                   String to, OnMsgSendListener onMsgSendListener, OnPicUploadListener onPicUploadListener);
//
//    public abstract void uploadPic(int chatType, String messageId, String absolutePath, String icon, String chatWith, String from,
//                                   String to, OnMsgSendListener onMsgSendListener, OnPicUploadListener onPicUploadListener);

//    public abstract void uploadPic(ChatData chatData, OnPicUploadListener onPicUploadListener);

//    public abstract void uploadVoiceFile(int chatType, String absolutePath, long time, String icon, String chatWith,
//                                         String from, String to, OnMsgSendListener onMsgSendListener, OnVoiceUploadListener onVoiceUploadListener);
//
//    public abstract void uploadVoiceFile(int chatType, String msgId, String absolutePath, long time, String icon, String chatWith,
//                                         String from, String to, OnMsgSendListener onMsgSendListener, OnVoiceUploadListener onVoiceUploadListener);

//    public abstract void uploadVoiceFile(ChatData chatData, OnVoiceUploadListener onVoiceUploadListener);

    public abstract void uploadFile(ChatData chatData, OnFileUploadListener onFileUploadListener);

    public abstract void sendTextMsg(ChatData chatData);

    public abstract void deleteMsg(ChatData chatData, OnDeleteMsgListener onDeleteMsgListener);

    public abstract void deleteMsgs(ArrayList<ChatData> chatData, OnDeleteMsgListener onDeleteMsgListener);

    public abstract void updateAndsendMsg(ChatData chatData, OnMsgSendListener onMsgSendListener);

    public abstract void sendMsgMulti(List<ChatData> chatDatas, OnMsgSendListener onMsgSendListener);

//    public abstract void sendVoiceFileMsg(ChatData chatData, OnMsgSendListener onMsgSendListener);

    public abstract void revertMsg(ChatData data, OnRevertListener onRevertListener);

    public abstract void sendReadMsg(ChatData messageData, OnSendReadListener onSendReadListener);

    public abstract void getFile(ChatData chatData, String savePath, OnGetFileListener onGetFileListener);

    public abstract void updateExtra(ChatData messageData);

    public abstract void removeChat(int chatType, String chatWith, String id);

    public abstract void updateStatus(int chatType, String chatWith, String id, int status);

    public abstract void clearChatContent(int chatType, String chatWith, OnClearChatContentListener onClearSessionListener);

    public abstract void saveDraft(int chatType, String chatWith, String trim);

    public abstract void getDraft(int chatType, String chatWith, OnGetDraftLitener onGetDraftLitener);

    public abstract void sendChannelMsg(String from,String to,int arg1,int arg2,byte[] data,boolean sync);

    public interface OnLoadHistoryListener {
        void onLoadHistory(List<ChatData> chatData, Map<String, ChatData> chatDataMap);
    }

    public interface OnMsgSendListener {
        void onMsgSend(int code, String codeMsg, ChatData chatData);
    }

    public interface OnPicUploadListener {
        void onPicUpload(ChatData chatData, JsonResult result);
    }

    public interface OnVoiceUploadListener {
        void onVoiceFileUpload(ChatData chatData, JsonResult result);
    }

    public interface OnFileUploadListener {
        void onFileUpload(boolean success, ChatData chatData, PhotonIMMessage photonIMMessage);

        void onFileUploadWithSendMsg(boolean success, ChatData chatData, PhotonIMMessage photonIMMessage, boolean sendMsg);

        void onProgress(ChatData chatData, int progress);
    }

    public interface OnRevertListener {
        void onRevert(ChatData data, int error, String msg);
    }

    public interface OnSendReadListener {
        void onSendRead(ChatData data, int error, String msg);
    }

    public interface OnGetFileListener {
        void onGetFile(String path);

        void onProgress(ChatData chatData, int progress);
    }

    public interface OnDeleteMsgListener {
        void onDeletemsgResult(ArrayList<ChatData> data, String error);
    }

    public interface OnClearChatContentListener {
        void onClearChatContent();
    }

    public interface OnGetDraftLitener {
        void onGetDraft(String draft);
    }

}
