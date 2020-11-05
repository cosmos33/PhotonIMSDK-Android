package com.momo.demo.main.contacts.group.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class GroupAdapter extends RvBaseAdapter {
    public GroupAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new GroupItem());
    }
}
