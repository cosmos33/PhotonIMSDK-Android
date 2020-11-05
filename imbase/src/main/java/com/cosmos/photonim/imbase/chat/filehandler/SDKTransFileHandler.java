package com.cosmos.photonim.imbase.chat.filehandler;

import com.cosmos.photon.im.PhotonIMDatabase;
//import com.cosmos.photon.im.PhotonIMFileManager;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

//import com.cosmos.photon.im.PhotonIMFileManager;

/**
 * sdk 托管上传文件与发送消息
 */
public class SDKTransFileHandler extends SDKFileHandler {
    @Override
    public void uploadFile(ChatData chatData, IChatModel.OnFileUploadListener onFileUploadListener) {
        PhotonIMMessage message = chatData.convertToIMMessage();
        PhotonIMDatabase.getInstance().saveMessage(message);


        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMMessage message1 = chatData.convertToIMMessage();
            PhotonIMDatabase.getInstance().saveMessage(message1);
//            PhotonIMClient.getInstance().sendMessage(message1, (i, s, s1) -> {
//
//            }, new PhotonIMFileManager.FileUpLoadListener() {
//                @Override
//                public void onLoad(int i, String s, PhotonIMMessage photonIMMessage) {
//                    if (i == 200) {
//                        if (onFileUploadListener != null) {
//                            onFileUploadListener.onFileUploadWithSendMsg(true, chatData, photonIMMessage, false);
//                        }
//                    } else if (i != 202) {
//                        if (onFileUploadListener != null) {
//                            onFileUploadListener.onFileUploadWithSendMsg(false, chatData, photonIMMessage, false);
//                        }
//                    }
//                }
//
//                @Override
//                public void onProgress(int i) {
//                    if (onFileUploadListener != null) {
//                        onFileUploadListener.onProgress(chatData, i);
//                    }
//                }
//            }, (i, s, l) -> EventBus.getDefault().post(new ChatDataWrapper(chatData, i, s)));
            return null;
        });
    }
}
