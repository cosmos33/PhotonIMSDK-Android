package com.cosmos.photonim.imbase.chat.album;

import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.cosmos.photonim.imbase.chat.album.ialbum.IAlbumModel;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class AlbumModel extends IAlbumModel {
    @Override
    public void getAlbum(OnGetAlbumListener onGetAlbumListener) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return AlbumPicUtils.queryCategoryFilesSync();
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (onGetAlbumListener != null) {
                    onGetAlbumListener.onGetAlbum((ArrayList<CategoryFile>) result);
                }
            }
        });
    }
}
