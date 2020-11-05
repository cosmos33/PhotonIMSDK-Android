package com.cosmos.photonim.imbase.chat.album.ialbum;

import com.cosmos.photonim.imbase.base.IRvBaseFragmentView;

public abstract class IAlbumView extends IRvBaseFragmentView<IAlbumPresenter> {
    public abstract void notifyDataSetChanged();

    public abstract void notifyItemChanged(int position);
}
