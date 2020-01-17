package com.cosmos.photonim.imbase.chat.album.ialbum;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;

import java.util.ArrayList;

public abstract class IAlbumModel implements IModel {
    public abstract void getAlbum(OnGetAlbumListener onGetAlbumListener);

    public interface OnGetAlbumListener {
        void onGetAlbum(ArrayList<CategoryFile> files);
    }

}
