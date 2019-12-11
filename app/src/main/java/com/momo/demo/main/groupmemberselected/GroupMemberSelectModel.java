package com.momo.demo.main.groupmemberselected;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupMembers;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.main.groupmemberselected.igroupmember.IGroupMemberModel;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberSelectModel extends IGroupMemberModel {
    @Override
    public void getGroupMembers(String gid, boolean containSelf, OnGetGroupMemberListener onGetGroupMemberListener) {
        getGroupMembers(Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED, gid, containSelf, onGetGroupMemberListener);
    }

    @Override
    public void getGroupMembers(String gid, boolean containSelf, boolean showCb, OnGetGroupMemberListener onGetGroupMemberListener) {
        getGroupMembers(Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED, gid, containSelf, showCb, onGetGroupMemberListener);
    }

    @Override
    public void getGroupMembers(int itemType, String gid, OnGetGroupMemberListener onGetGroupMemberListener) {
        getGroupMembers(itemType, gid, true, onGetGroupMemberListener);
    }

    @Override
    public void getGroupMembers(int itemType, String gid, boolean containSelf, OnGetGroupMemberListener onGetGroupMemberListener) {
        getGroupMembers(itemType, gid, containSelf, true, onGetGroupMemberListener);
    }

    @Override
    public void getGroupMembers(int itemType, String gid, boolean containSelf, boolean showCb, OnGetGroupMemberListener onGetGroupMemberListener) {
        TaskExecutor.getInstance().createAsycTask(() ->
                getGroupMembersInner(ImBaseBridge.getInstance().getSessenId(), ImBaseBridge.getInstance().getUserId(), gid, itemType, containSelf, showCb), result -> {
            if (onGetGroupMemberListener != null) {
                onGetGroupMemberListener.onGetGroupMembers((List<GroupMembersData>) result);
            }
        });
    }

    public static Object getGroupMembersInner(String sessionId, String userID, String gid, int itemType, boolean containSelf, boolean showCb) {
        JsonResult jsonResult = HttpUtils.getInstance().getGroupMembers(sessionId, userID, gid);
        if (jsonResult.success()) {
            JsonGroupMembers jsonGroupMembers = (JsonGroupMembers) jsonResult.get();
            List<JsonGroupMembers.DataBean.ListsBean> lists = jsonGroupMembers.getData().getLists();
            if (CollectionUtils.isEmpty(lists)) {
                return null;
            } else {
                List<GroupMembersData> result = new ArrayList<>(lists.size());
                for (JsonGroupMembers.DataBean.ListsBean list : lists) {
                    if (!containSelf && list.getUserId().equals(ImBaseBridge.getInstance().getUserId())) {
                        continue;
                    }
                    result.add(new GroupMembersData(list.getAvatar(), list.getNickname(), list.getUserId(), showCb, itemType));
                }
                return result;
            }
        } else {
            return null;
        }
    }
}
