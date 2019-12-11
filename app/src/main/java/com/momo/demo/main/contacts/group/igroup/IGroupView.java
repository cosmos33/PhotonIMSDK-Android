package com.momo.demo.main.contacts.group.igroup;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.contacts.group.GroupData;

import java.util.List;

public abstract class IGroupView extends RvBaseActivity implements IView {
    protected IGroupPresenter contactPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactPresenter = (IGroupPresenter) getIPresenter();
        if (contactPresenter == null) {
            throw new IllegalStateException("contactPresenter is null");
        }
    }

    public abstract void loadNearbyGroupResult(List<GroupData> onlineUserData);

    public abstract void showNearbbyGroupEmptyView();

    public abstract void onJoinGroupResult(GroupData groupData, boolean result);
}
