package com.momo.demo.main.roomInfo;

import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupMembers;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.main.groupmemberselected.GroupMembersData;
import com.momo.demo.main.roomInfo.igroupinfo.IRoomInfoModel;

import java.util.ArrayList;
import java.util.List;

public class RoomInfoModel extends IRoomInfoModel {

    @Override
    public void getRoomMember(String sessionId, String userId, String gid, OnGetRoomMemberListener onGetGroupInfoListener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            JsonResult jsonResult = HttpUtils.getInstance().roomMember(sessionId, userId, gid);
            if (jsonResult.success()) {
                JsonGroupMembers jsonGroupMembers = (JsonGroupMembers) jsonResult.get();
                List<JsonGroupMembers.DataBean.ListsBean> lists = jsonGroupMembers.getData().getLists();
                if (CollectionUtils.isEmpty(lists)) {
                    return null;
                } else {
                    List<GroupMembersData> result = new ArrayList<>(lists.size());
                    for (JsonGroupMembers.DataBean.ListsBean list : lists) {
//                        if (!containSelf && list.getUserId().equals(LoginInfo.getInstance().getUserId())) {
//                            continue;
//                        }
                        result.add(new GroupMembersData(list.getAvatar(), list.getNickname(), list.getUserId(), false, Constants.ITEM_TYPE_GROUP_MEMBER_INFO));
                    }
                    return result;
                }
            } else {
                return null;
            }
        }, result -> {
            if (onGetGroupInfoListener != null) {
                onGetGroupInfoListener.onGetRoomResult((List<GroupMembersData>) result);
            }
        });
    }
}
