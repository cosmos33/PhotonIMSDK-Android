package com.cosmos.photonim.imbase.session.adapter;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class SessionAdapter extends RvBaseAdapter<SessionData> {
    public SessionAdapter(List<SessionData> baseDataList,
                          SessionItem.UpdateOtherInfoListener updateOtherInfoListener) {
        super(baseDataList);
        addItemType(new SessionItem(updateOtherInfoListener));
    }
}
