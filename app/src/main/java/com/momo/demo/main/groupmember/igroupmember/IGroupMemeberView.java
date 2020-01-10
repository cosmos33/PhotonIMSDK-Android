package com.momo.demo.main.groupmember.igroupmember;

import com.cosmos.photonim.imbase.base.mvp.base.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupMemeberView extends RvBaseActivity<IGroupMemberPresenter> implements IView {

    public abstract void onGetGroupMembersResult(List<GroupMembersData> result);

    public abstract void showMembersEmptyView();
}
