package com.momo.demo.main.contacts.single;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class OnlineUserAdapter extends RvBaseAdapter {
    public OnlineUserAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new OnlineUserItem());
    }
}
