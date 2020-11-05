package com.momo.demo.main.roomInfo.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class RoomMemberAdapter extends RvBaseAdapter {
    public RoomMemberAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new RoomInfoMemberItem());
    }
}
