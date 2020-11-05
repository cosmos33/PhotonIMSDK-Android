package com.momo.demo.main.contacts.room.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class RoomAdapter extends RvBaseAdapter {
    public RoomAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new RoomItem());
    }

}
