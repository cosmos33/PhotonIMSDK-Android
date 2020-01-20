package com.cosmos.photonim.imbase.chat.file.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class FileAdapter extends RvBaseAdapter {
    public FileAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new FileItem());
    }
}
