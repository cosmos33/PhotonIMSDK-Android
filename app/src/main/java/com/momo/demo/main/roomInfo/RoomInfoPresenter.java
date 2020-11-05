package com.momo.demo.main.roomInfo;

import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.groupmemberselected.GroupMembersData;
import com.momo.demo.main.roomInfo.igroupinfo.IRoomInfoModel;
import com.momo.demo.main.roomInfo.igroupinfo.IRoomInfoPresenter;
import com.momo.demo.main.roomInfo.igroupinfo.IRoomInfoView;

import java.util.List;

public class RoomInfoPresenter extends IRoomInfoPresenter<IRoomInfoView, IRoomInfoModel> {

    public RoomInfoPresenter(IRoomInfoView iView) {
        super(iView);
    }

    @Override
    public void getGroupMembers(String gid) {
        getiModel().getRoomMember(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId(),
                gid, new IRoomInfoModel.OnGetRoomMemberListener() {
                    @Override
                    public void onGetRoomResult(List<GroupMembersData> result) {
                        getIView().onGetGroupMemberResult(result);
                    }
                });
    }

    @Override
    public IRoomInfoModel generateIModel() {
        return new RoomInfoModel();
    }
}
