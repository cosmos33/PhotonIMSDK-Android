package com.momo.demo.main.groupmember.igroupmember;

import com.cosmos.photonim.imbase.utils.mvpbase.IModel;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupMemberModel implements IModel {
    public abstract void getGroupMembers(String gid, OnGetGroupMemberListener onGetGroupMemberListener);


    public interface OnGetGroupMemberListener {
        void onGetGroupMembers(List<GroupMembersData> result);
    }
}
