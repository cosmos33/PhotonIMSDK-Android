package com.cosmos.photonim.imbase.chat.album.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class AlbumAdapter extends RvBaseAdapter {
    public AlbumAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new AlbumItemType());
    }
}
