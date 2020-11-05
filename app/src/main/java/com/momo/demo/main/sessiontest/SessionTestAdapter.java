package com.momo.demo.main.sessiontest;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class SessionTestAdapter extends RvBaseAdapter<SessionTestData> {
    public SessionTestAdapter(List<SessionTestData> baseDataList,
                              SessionTestItem.UpdateOtherInfoListener updateOtherInfoListener) {
        super(baseDataList);
        addItemType(new SessionTestItem(updateOtherInfoListener));
    }
}
