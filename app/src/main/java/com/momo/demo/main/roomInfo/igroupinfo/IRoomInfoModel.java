package com.momo.demo.main.roomInfo.igroupinfo;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IRoomInfoModel implements IModel {

    public abstract void getRoomMember(String sessionId, String userId, String gid, OnGetRoomMemberListener onGetGroupInfoListener);

    public interface OnGetRoomMemberListener {
        void onGetRoomResult(List<GroupMembersData> result);
    }
}
