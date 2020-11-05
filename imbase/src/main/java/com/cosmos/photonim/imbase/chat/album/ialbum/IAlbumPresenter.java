package com.cosmos.photonim.imbase.chat.album.ialbum;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.ArrayList;

public abstract class IAlbumPresenter extends IPresenter<IAlbumView, IAlbumModel> {
    public IAlbumPresenter(IAlbumView iView) {
        super(iView);
    }

    public abstract ArrayList<CategoryFile> initData();

    public abstract void getAlbum();

    @Override
    public IAlbumView getEmptyView() {
        return new IAlbumView() {
            @Override
            public void notifyDataSetChanged() {

            }

            @Override
            public void notifyItemChanged(int position) {

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

    public abstract ArrayList<CategoryFile> getCheckPics();

    public abstract ArrayList<CategoryFile> getData();

    public abstract void changeCheckStatus(CategoryFile data);

    public abstract void updateData(ArrayList<CategoryFile> requestCode);
}
