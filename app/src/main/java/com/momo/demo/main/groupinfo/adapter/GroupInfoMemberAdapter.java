package com.momo.demo.main.groupinfo.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class GroupInfoMemberAdapter extends RvBaseAdapter {
    public GroupInfoMemberAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new GroupInfoMemberItem());
    }
}
