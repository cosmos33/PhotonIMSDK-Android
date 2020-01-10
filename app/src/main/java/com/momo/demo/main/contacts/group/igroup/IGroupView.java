package com.momo.demo.main.contacts.group.igroup;

import com.cosmos.photonim.imbase.base.mvp.base.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.contacts.group.GroupData;

import java.util.List;

public abstract class IGroupView extends RvBaseActivity<IGroupPresenter> implements IView {

    public abstract void loadNearbyGroupResult(List<GroupData> onlineUserData);

    public abstract void showNearbbyGroupEmptyView();

    public abstract void onJoinGroupResult(GroupData groupData, boolean result);
}
