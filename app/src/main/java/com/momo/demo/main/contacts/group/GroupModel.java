package com.momo.demo.main.contacts.group;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroups;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.group.igroup.IGroupModel;

import java.util.ArrayList;
import java.util.List;

public class GroupModel extends IGroupModel {
    @Override
    public void loadNearbyGroup(OnLoadNearbyGroupListener onLoadNearbyGroup) {
        TaskExecutor.getInstance().createAsycTask(() -> getGroups(),
                result -> {
                    if (onLoadNearbyGroup != null) {
                        onLoadNearbyGroup.onLoadNearbyGroup((List<GroupData>) result);
                    }
                });
    }

    @Override
    public void joinGroup(String groupID, OnJoinGroupListener onJoinGroupListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().joinGroup(
                LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId(),
                groupID),
                result -> {
                    if (onJoinGroupListener != null) {
                        onJoinGroupListener.onJoinGroupResult(groupID, ((JsonResult) result).success());
                    }
                });
    }

    private Object getGroups() {
        JsonResult jsonResult = HttpUtils.getInstance().getGroups(
                LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
        if (jsonResult.success()) {
            JsonGroups jsonGroups = (JsonGroups) jsonResult.get();
            List<GroupData> groupData = new ArrayList<>();
            boolean inGroup;
            for (JsonGroups.DataBean.ListsBean list : jsonGroups.getData().getLists()) {
                inGroup = false;
                if (ImBaseBridge.getInstance().getJoinedGids() != null) {
                    for (String joinedGid : ImBaseBridge.getInstance().getJoinedGids()) {
                        if (list.getGid().equals(joinedGid)) {
                            inGroup = true;
                            break;
                        }
                    }
                }
                groupData.add(new GroupData.Builder()
                        .groupId(list.getGid())
                        .icon(list.getAvatar())
                        .name(list.getName())
                        .inGroup(inGroup)
                        .build());
            }
            return groupData;
        }
        return null;
    }
}
