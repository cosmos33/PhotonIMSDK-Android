package com.momo.demo.main.groupmember.igroupmember;

import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupMemeberView extends RvBaseActivity<IGroupMemberPresenter> {

    public abstract void onGetGroupMembersResult(List<GroupMembersData> result);

    public abstract void showMembersEmptyView();
}
