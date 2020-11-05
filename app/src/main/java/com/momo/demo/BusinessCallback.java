package com.momo.demo;

import android.app.Activity;
import android.content.Intent;

import com.cosmos.photon.push.PhotonPushManager;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.RoamData;
import com.cosmos.photonim.imbase.chat.chatroom.iroom.IChatRoomModel;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactRecent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.momo.demo.login.LoginActivity;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.room.RoomModel;
import com.momo.demo.main.contacts.room.iroom.IRoomModel;
import com.momo.demo.main.contacts.single.userinfo.UserInfoModel;
import com.momo.demo.main.contacts.single.userinfo.iuserinfo.IUserInfoModel;
import com.momo.demo.main.forward.ForwardActivity;
import com.momo.demo.main.groupinfo.GroupInfoActivity;
import com.momo.demo.main.groupmemberselected.GroupMemberSelectActivity;
import com.momo.demo.main.me.RoamInfo;
import com.momo.demo.main.roomInfo.RoomInfoActivity;

public class BusinessCallback implements ImBaseBridge.BusinessListener {
    private IUserInfoModel iUserInfoModel;
    private IRoomModel iRoomModel;

    @Override
    public void onForwardClick(Activity activity, ChatData chatData) {
        ForwardActivity.startActivity(activity, chatData);
    }

    @Override
    public void getUserIcon(String userId, ImBaseBridge.OnGetUserIconListener onGetUserIconListener) {
        if (iUserInfoModel == null) {
            iUserInfoModel = new UserInfoModel();
        }
        iUserInfoModel.getUserInfo(userId, jsonOtherInfo -> {
            if (jsonOtherInfo != null && jsonOtherInfo.success() && onGetUserIconListener != null) {
                onGetUserIconListener.onGetUserIcon(jsonOtherInfo.getData().getProfile().getAvatar(),
                        jsonOtherInfo.getData().getProfile().getNickname());
            }
        });
    }

    @Override
    public void onAtListener(Activity activity, String gid) {
        GroupMemberSelectActivity.start(activity, gid);
    }

    @Override
    public void onKickUser(Activity activity) {
        PhotonPushManager.getInstance().unRegister();
        ImBaseBridge.getInstance().logout();

        ToastUtils.showText("服务器强制下线");
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onGroupInfoClick(Activity activity, String gId) {
        GroupInfoActivity.startActivity(activity, gId);
    }

    @Override
    public void onRoomInfoClick(Activity chatRoomActivity, String roomId, String rName) {
        RoomInfoActivity.startActivity(chatRoomActivity, roomId, rName);
    }

    @Override
    public void onAddTestClick(Activity activity) {
        ForwardActivity.startForSessionTestActivity(activity);
    }

    @Override
    public JsonResult getOthersInfo(String[] ids) {
        return (JsonResult) HttpUtils.getInstance().getOthersInfo(ids, LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId());
    }

    @Override
    public JsonResult getGroupProfile(String groupId) {
        return HttpUtils.getInstance().getGroupProfile(LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId(), groupId);
    }

    @Override
    public JsonContactRecent getRecentUser() {
        return (JsonContactRecent) HttpUtils.getInstance().getRecentUser(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId()).get();
    }

    @Override
    public JsonResult setIgnoreStatus(String remoteId, boolean igoreAlert) {
        return HttpUtils.getInstance().setIgnoreStatus(remoteId, igoreAlert,
                LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
    }

    @Override
    public JsonResult getIgnoreStatus(String userId) {
        return (JsonResult) HttpUtils.getInstance().getIgnoreStatus(LoginInfo.getInstance().getSessionId()
                , LoginInfo.getInstance().getUserId(), userId);
    }

    @Override
    public JsonResult sendVoiceFile(String localFile) {
        return (JsonResult) HttpUtils.getInstance().sendVoiceFile(localFile,
                LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
    }

    @Override
    public JsonResult sendPic(String localFile) {
        return (JsonResult) HttpUtils.getInstance().sendPic(localFile,
                LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
    }

    @Override
    public String getUserId() {
        return LoginInfo.getInstance().getUserId();
    }

    @Override
    public String getTokenId() {
        return LoginInfo.getInstance().getToken();
    }

    @Override
    public RoamData getRoamData() {
        RoamData roamData = new RoamData();
        roamData.roamOpen = RoamInfo.openRoam();
        roamData.startTime = RoamInfo.getStartTime();
        roamData.endTime = RoamInfo.getEndTime();
        roamData.count = RoamInfo.getCount();
        return roamData;
    }

    @Override
    public void leaveRoom(String rId, IChatRoomModel.ChatRoomListener listener) {
        if (iRoomModel == null) {
            iRoomModel = new RoomModel();
        }
        iRoomModel.setRoomListener(new IRoomModel.OnRoomImpl() {
            @Override
            public void onLeaveRoomResult(boolean success, String roomId) {
                if (listener != null) {
                    listener.onLeaveResult(success);
                }
            }
        });
        iRoomModel.leaveRooms(rId);
    }
}
