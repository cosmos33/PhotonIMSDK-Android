package com.momo.demo.main.contacts.group.igroup;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.momo.demo.main.contacts.group.GroupData;

import java.util.List;

public abstract class IGroupModel implements IModel {
    public abstract void loadNearbyGroup(OnLoadNearbyGroupListener onLoadNearbyGroup);

    public abstract void joinGroup(String groupID, OnJoinGroupListener onJoinGroupListener);

    public interface OnLoadNearbyGroupListener {
        void onLoadNearbyGroup(List<GroupData> onlineUserData);
    }

    public interface OnJoinGroupListener {
        void onJoinGroupResult(String groupId, boolean result);
    }

}
