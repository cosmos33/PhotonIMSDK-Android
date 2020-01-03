package com.momo.demo.main.groupmemberselected.igroupmember;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupMemeberView extends RvBaseActivity implements IView {
    protected IGroupMemberPresenter iGroupPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iGroupPresenter = (IGroupMemberPresenter) getIPresenter();
        if (iGroupPresenter == null) {
            throw new IllegalStateException("iForwardPresenter is null");
        }
    }

    public abstract void onGetGroupMembersResult(List<GroupMembersData> result);

    public abstract void showMembersEmptyView();
}
