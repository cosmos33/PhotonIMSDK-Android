package com.momo.demo.main.roomInfo.igroupinfo;

import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IRoomInfoView extends RvBaseActivity<IRoomInfoPresenter> {
    public abstract void onGetGroupMemberResult(List<GroupMembersData> jsonResult);
}
