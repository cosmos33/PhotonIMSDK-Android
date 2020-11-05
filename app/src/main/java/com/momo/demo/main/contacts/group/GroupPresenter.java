package com.momo.demo.main.contacts.group;

import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.momo.demo.main.contacts.group.adapter.GroupData;
import com.momo.demo.main.contacts.group.igroup.IGroupModel;
import com.momo.demo.main.contacts.group.igroup.IGroupPresenter;
import com.momo.demo.main.contacts.group.igroup.IGroupView;

public class GroupPresenter extends IGroupPresenter<IGroupView, IGroupModel> {
    public GroupPresenter(IGroupView iView) {
        super(iView);
    }

    @Override
    public void loadGroups() {
        getiModel().loadNearbyGroup(onlineUserData -> {
            if (CollectionUtils.isEmpty(onlineUserData)) {
                getIView().showNearbbyGroupEmptyView();
            } else {
                getIView().loadNearbyGroupResult(onlineUserData);
            }
        });
    }

    @Override
    public void joinGroup(GroupData groupData) {
        getiModel().joinGroup(groupData.getGroupId(), (groupID, result) -> getIView().onJoinGroupResult(groupData, result));
    }

    @Override
    public IGroupModel generateIModel() {
        return new GroupModel();
    }
}
