package com.cosmos.photonim.imbase.chat.searchhistory.adapter;

import com.cosmos.photonim.imbase.session.SessionItem;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class SessionSearchAdapter extends RvBaseAdapter {
    public SessionSearchAdapter(List baseDataList, SessionItem.UpdateOtherInfoListener updateOtherInfoListener) {
        super(baseDataList);
        addItemType(new SessionSearchItem(updateOtherInfoListener));
    }
}
