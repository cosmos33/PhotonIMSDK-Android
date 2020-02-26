package com.cosmos.photonim.imbase.chat.filehandler;

import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;

public interface IFileHandler {
    void uploadFile(ChatData chatData, IChatModel.OnFileUploadListener onFileUploadListener);

    void downloadFile(ChatData data, String savePath, IChatModel.OnGetFileListener onGetFileListener);
}
