package com.momo.demo.main.groupinfo;

import com.cosmos.photonim.imbase.utils.Constants;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoModel;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoPresenter;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoView;
import com.momo.demo.main.groupmemberselected.GroupMemberSelectModel;

public class GroupInfoPresenter extends IGroupInfoPresenter<IGroupInfoView, IGroupInfoModel> {
    private GroupMemberSelectModel groupMemberSelectModel;

    public GroupInfoPresenter(IGroupInfoView iView) {
        super(iView);
        groupMemberSelectModel = new GroupMemberSelectModel();
    }

    @Override
    public void getGroupInfo(String gid) {
        getiModel().getGroupInfo(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId(), gid,
                jsonResult -> getIView().onGetGroupInfoResult(jsonResult));
    }

    @Override
    public void getGroupMembers(String gid) {
        groupMemberSelectModel.getGroupMembers(Constants.ITEM_TYPE_GROUP_MEMBER_INFO, gid, result -> getIView().onGetGroupMemberResult(result));
    }

    @Override
    public void getGroupIgnoreStatus(String gid) {
        getiModel().getGroupIgnoreStatus(LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId(), gid,
                jsonResult -> getIView().onGetGroupIgnoreStatusResult(jsonResult));
    }

    @Override
    public void changeGroupIgnoreStatus(String gid, boolean igonre) {
        // 0（开启勿扰）1(关闭勿扰）
        int switchX = igonre ? 0 : 1;
        getiModel().changeGroupIgnoreStatus(LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId(), gid, switchX,
                jsonResult -> getIView().onChangeGroupIgnoreStatusResult(jsonResult));
    }

    @Override
    public void clearChatContent() {

    }

    @Override
    public IGroupInfoModel generateIModel() {
        return new GroupInfoModel();
    }
}
