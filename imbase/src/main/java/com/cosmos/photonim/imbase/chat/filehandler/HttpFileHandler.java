package com.cosmos.photonim.imbase.chat.filehandler;

import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadFile;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

public class HttpFileHandler implements IFileHandler {

    @Override
    public void uploadFile(ChatData chatData, IChatModel.OnFileUploadListener onFileUploadListener) {
        switch (chatData.getMsgType()) {
            case PhotonIMMessage.IMAGE:
            case PhotonIMMessage.AUDIO:
                uploadFileInner(chatData, onFileUploadListener);
                break;
            default:
                ToastUtils.showText("不支持");
                if (onFileUploadListener != null) {
                    onFileUploadListener.onFileUpload(false, chatData, null);
                }
        }
    }

    @Override
    public void downloadFile(ChatData data, String savePath, IChatModel.OnGetFileListener onGetFileListener) {
        switch (data.getMsgType()) {
            case PhotonIMMessage.AUDIO:
                getVoiceFile(data, savePath, onGetFileListener);
                break;
            default:
                ToastUtils.showText("不支持");
                if (onGetFileListener != null) {
                    onGetFileListener.onGetFile(null);
                }
        }
    }

    public void uploadFileInner(ChatData chatData, IChatModel.OnFileUploadListener onFileUploadListener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMMessage message = chatData.convertToIMMessage();
            PhotonIMDatabase.getInstance().saveMessage(message);
            if (chatData.getMsgType() == PhotonIMMessage.IMAGE) {
                return ImBaseBridge.getInstance().sendPic(chatData.getLocalFile());
            } else {
                return ImBaseBridge.getInstance().sendVoiceFile(chatData.getLocalFile());
            }
        }, result -> {
            JsonUploadFile jsonUploadImage = (JsonUploadFile) ((JsonResult) result).get();
            String url = jsonUploadImage.getData().getUrl();
            chatData.setFileUrl(url);
            if (onFileUploadListener != null) {
                onFileUploadListener.onFileUpload(jsonUploadImage.success(), chatData, null);
            }
        });
    }

    public void getVoiceFile(ChatData data, String savePath, IChatModel.OnGetFileListener onGetFileListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getVoiceFileInner(data, data.getFileUrl(), savePath, new IChatModel.OnGetFileListener() {
            @Override
            public void onGetFile(String path) {
                PhotonIMDatabase.getInstance().updateMessageLocalFile(data.getChatType(), data.getChatWith(), data.getMsgId(), savePath);

                CustomRunnable customRunnable = new CustomRunnable.Builder()
                        .runnable(new Runnable() {
                            @Override
                            public void run() {
                                if (onGetFileListener != null) {
                                    onGetFileListener.onGetFile(path);
                                }
                            }
                        }).build();
                MainLooperExecuteUtil.getInstance().post(customRunnable);
            }

            @Override
            public void onProgress(ChatData chatData, int progress) {

            }
        }));
    }

    private Object getVoiceFileInner(ChatData data, String fileUrl, String savePath, IChatModel.OnGetFileListener onGetFileListener) {
        HttpUtils.getInstance().getFile(fileUrl, savePath, onGetFileListener);
        return null;
    }
}
