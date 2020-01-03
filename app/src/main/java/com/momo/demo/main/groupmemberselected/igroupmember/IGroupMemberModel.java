package com.momo.demo.main.groupmemberselected.igroupmember;

import com.cosmos.photonim.imbase.utils.mvpbase.IModel;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupMemberModel implements IModel {
    public abstract void getGroupMembers(String gid, boolean containSelf, OnGetGroupMemberListener onGetGroupMemberListener);

    public abstract void getGroupMembers(String gid, boolean containSelf, boolean showCb, OnGetGroupMemberListener onGetGroupMemberListener);

    public abstract void getGroupMembers(int itemType, String gid, OnGetGroupMemberListener onGetGroupMemberListener);

    public abstract void getGroupMembers(int itemType, String gid, boolean containSelf, OnGetGroupMemberListener onGetGroupMemberListener);

    public abstract void getGroupMembers(int itemType, String gid, boolean containSelf, boolean showCb, OnGetGroupMemberListener onGetGroupMemberListener);

    public interface OnGetGroupMemberListener {
        void onGetGroupMembers(List<GroupMembersData> result);
    }
}
