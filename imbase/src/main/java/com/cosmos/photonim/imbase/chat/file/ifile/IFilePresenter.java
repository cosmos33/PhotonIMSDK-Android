package com.cosmos.photonim.imbase.chat.file.ifile;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.file.FileFragment;
import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.ArrayList;

public abstract class IFilePresenter extends IPresenter<IFileView, IFileModel> {
    public IFilePresenter(IFileView iView) {
        super(iView);
    }

    public abstract ArrayList<FileItemData> init();

    public abstract void getFileData(String path);

    public abstract ArrayList<FileItemData> getSelectedFiles();

    public abstract boolean checkData(FileItemData file);

    public abstract String getCurrentPath();

    public abstract boolean popFileData();

    @Override
    public IFileView getEmptyView() {
        return new IFileView() {
            @Override
            public void progress(boolean show) {

            }

            @Override
            public void notifyData() {

            }

            @Override
            public int getLayoutId() {
                return 0;
            }

            @Override
            protected void initView(View view) {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }

            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }
        };
    }

    public abstract void setOnFileCheckedListener(FileFragment.OnFileCheckedListener onFileCheckedListener);
}
