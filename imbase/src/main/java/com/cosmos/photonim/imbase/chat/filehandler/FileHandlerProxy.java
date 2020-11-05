package com.cosmos.photonim.imbase.chat.filehandler;

import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;

public class FileHandlerProxy implements IFileHandler {
    private static IFileHandler iFileHandler;

    private FileHandlerProxy() {

    }

    public static IFileHandler getiFileHandler() {
        checkHandler();
        return iFileHandler;
    }

    public static void initFileHander(IFileHandler fileHandler) {
        iFileHandler = fileHandler;
        if (iFileHandler == null) {
            throw new IllegalArgumentException("initFileHander !!!!! error");
        }
    }

    @Override
    public void uploadFile(ChatData chatData, IChatModel.OnFileUploadListener onFileUploadListener) {
        checkHandler();
        iFileHandler.uploadFile(chatData, onFileUploadListener);
    }


    @Override
    public void downloadFile(ChatData data, String savePath, IChatModel.OnGetFileListener onGetFileListener) {
        checkHandler();
        iFileHandler.downloadFile(data, savePath, onGetFileListener);
    }

    private static void checkHandler() {
        if (iFileHandler == null) {
            iFileHandler = new HttpFileHandler();
        }
    }
}
