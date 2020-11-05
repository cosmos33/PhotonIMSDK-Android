package com.momo.demo.main.groupmemberselected;

import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.momo.demo.main.groupmemberselected.igroupmember.IGroupMemberModel;
import com.momo.demo.main.groupmemberselected.igroupmember.IGroupMemberPresenter;
import com.momo.demo.main.groupmemberselected.igroupmember.IGroupMemeberView;

public class GroupMemberSelectPresenter extends IGroupMemberPresenter<IGroupMemeberView, IGroupMemberModel> {
    public GroupMemberSelectPresenter(IGroupMemeberView iView) {
        super(iView);
    }

    @Override
    public void getGroupMembers(String gid) {
        getGroupMembers(gid, false, true);
    }

    @Override
    public void getGroupMembers(String gid, boolean containSelf, boolean showCb) {
        getiModel().getGroupMembers(gid, containSelf, showCb, result -> {
            if (CollectionUtils.isEmpty(result)) {
                getIView().showMembersEmptyView();
            } else {
                getIView().onGetGroupMembersResult(result);
            }
        });
    }

    @Override
    public IGroupMemberModel generateIModel() {
        return new GroupMemberSelectModel();
    }
}
