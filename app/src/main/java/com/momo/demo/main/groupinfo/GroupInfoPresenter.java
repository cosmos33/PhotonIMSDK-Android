package com.momo.demo.main.groupinfo;

import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.chat.chatset.ChatSetModel;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.event.ClearChatContent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoModel;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoPresenter;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoView;
import com.momo.demo.main.groupmemberselected.GroupMemberSelectModel;

import org.greenrobot.eventbus.EventBus;

public class GroupInfoPresenter extends IGroupInfoPresenter<IGroupInfoView, IGroupInfoModel> implements ChatSetModel.OnChangeStatusListener {
    private GroupMemberSelectModel groupMemberSelectModel;
    private ChatModel chatModel;
    private boolean top;

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
                LoginInfo.getInstance().getUserId(), gid, this);
    }

    @Override
    public void changeGroupIgnoreStatus(String gid, boolean igonre) {
        // 0（开启勿扰）1(关闭勿扰）
        int switchX = igonre ? 0 : 1;
        getiModel().changeGroupIgnoreStatus(LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId(), gid, switchX, this);
    }

    @Override
    public void clearChatContent(int chatType, String chatWith) {
        if (chatModel == null) {
            chatModel = new ChatModel();
        }
        chatModel.clearChatContent(chatType, chatWith, new IChatModel.OnClearChatContentListener() {
            @Override
            public void onClearChatContent() {
                EventBus.getDefault().post(new ClearChatContent());
                getIView().dimissProgressDialog();
            }
        });
    }

    @Override
    public void changeTopStatus(int chatType, String id) {
        getiModel().changeTopStatus(chatType, id, !top, this);
    }

    @Override
    public void getTopStatus(int chatType, String id) {
        getiModel().getTopStatus(chatType, id, this);
    }

    @Override
    public void onGetTopStatus(boolean top) {
        this.top = top;
        getIView().changeTopStatus(top);
    }

    @Override
    public void onChangeTopStatus() {
        top = !top;
        getIView().toast("操作成功");
    }

    @Override
    public void onChangeIgnoreStatus(JsonResult success) {
        getIView().onChangeGroupIgnoreStatusResult(success);
    }

    @Override
    public void onGetIgnoreStatus(JsonResult result) {
        getIView().onGetGroupIgnoreStatusResult(result);
    }

    @Override
    public IGroupInfoModel generateIModel() {
        return new GroupInfoModel();
    }
}
