package com.momo.demo.main.groupmemberselected.apater;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class GroupMemberAdapter extends RvBaseAdapter {

    public GroupMemberAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new GroupMemberItem());
        addItemType(new GroupMemberAllItem());
    }
}
