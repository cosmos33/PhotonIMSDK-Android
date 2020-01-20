package com.cosmos.photonim.imbase.chat.file;

import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;
import com.cosmos.photonim.imbase.chat.file.ifile.IFileModel;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FileModel extends IFileModel {
    @Override
    public void getFileData(String path, OnGetFileListener onGetFileListener) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                File file = new File(path);
                File[] files = file.listFiles();
                ArrayList<FileItemData> result = new ArrayList<>(files.length);
                FileItemData fileItemData;
                for (File childFile : files) {
                    fileItemData = new FileItemData.Builder().file(childFile).build();
                    if (onGetFileListener != null) {
                        onGetFileListener.onProcessing(fileItemData);
                    }
                    result.add(fileItemData);
                }
                return result;
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (onGetFileListener != null) {
                    onGetFileListener.onGetFileListener((ArrayList<FileItemData>) result);
                }
            }
        });
    }

}
