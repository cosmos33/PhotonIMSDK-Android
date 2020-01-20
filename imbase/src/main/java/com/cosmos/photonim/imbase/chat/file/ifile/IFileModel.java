package com.cosmos.photonim.imbase.chat.file.ifile;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;

import java.util.ArrayList;

public abstract class IFileModel implements IModel {

    public abstract void getFileData(String path, IFileModel.OnGetFileListener onGetFileListener);

    public interface OnGetFileListener {
        void onProcessing(FileItemData fileItemData);

        void onGetFileListener(ArrayList<FileItemData> fileItemData);
    }

}
