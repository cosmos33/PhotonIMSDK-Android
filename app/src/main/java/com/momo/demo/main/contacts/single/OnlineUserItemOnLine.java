package com.momo.demo.main.contacts.single;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;

public class OnlineUserItemOnLine extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_ONLINEUSER;
    }

    @Override
    public int getLayout() {
        return R.layout.item_contact_online;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {

    }

    @Override
    public int[] getOnClickViews() {
        return new int[0];
    }
}
