package com.cosmos.photonim.imbase.chat.filehandler;

import com.cosmos.photon.im.PhotonIMDatabase;
//import com.cosmos.photon.im.PhotonIMFileManager;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.concurrent.Callable;

//import com.cosmos.photon.im.PhotonIMFileManager;

/**
 * sdk 托管上传文件
 */
public class SDKFileHandler implements IFileHandler {
    @Override
    public void uploadFile(ChatData chatData, IChatModel.OnFileUploadListener onFileUploadListener) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                PhotonIMMessage message = chatData.convertToIMMessage();
                PhotonIMDatabase.getInstance().saveMessage(message);
//                PhotonIMFileManager.getInstance().uploadFile(message, new PhotonIMFileManager.FileUpLoadListener() {
//
//                    @Override
//                    public void onLoad(int i, String s, PhotonIMMessage photonIMMessage) {
//                        if (i == 200) {
//                            if (onFileUploadListener != null) {
//                                onFileUploadListener.onFileUpload(true, chatData, photonIMMessage);
//                            }
//                        } else if (i != 202) {
//                            if (onFileUploadListener != null) {
//                                onFileUploadListener.onFileUpload(false, chatData, photonIMMessage);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onProgress(int i) {
//                        if (onFileUploadListener != null) {
//                            onFileUploadListener.onProgress(chatData, i);
//                        }
//                    }
//                });
                return null;
            }
        });
    }

    @Override
    public void downloadFile(ChatData data, String savePath, IChatModel.OnGetFileListener onGetFileListener) {
//        PhotonIMFileManager.getInstance().downloadFile( data.convertToIMMessage(), null,0,new PhotonIMFileManager.FileDownloadListener() {
//            @Override
//            public void onLoad(int i, String s, String s1) {
//                if (i == 200) {
//                    PhotonIMDatabase.getInstance().updateMessageLocalFile(data.getChatType(), data.getChatWith(), data.getMsgId(), s1);
//
//                    CustomRunnable customRunnable = new CustomRunnable.Builder()
//                            .runnable(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (onGetFileListener != null) {
//                                        onGetFileListener.onGetFile(s1);
//                                    }
//                                }
//                            }).build();
//                    MainLooperExecuteUtil.getInstance().post(customRunnable);
//                } else {
//                    if (onGetFileListener != null) {
//                        onGetFileListener.onGetFile(null);
//                    }
//                }
//            }
//
//            @Override
//            public void onProgress(int i) {
//                if (onGetFileListener != null) {
//                    onGetFileListener.onProgress(data, i);
//                }
//            }
//        });
    }
}
