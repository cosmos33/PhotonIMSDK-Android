package com.momo.demo.main.groupinfo.igroupinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupInfoView extends RvBaseActivity implements IView {
    protected IGroupInfoPresenter iGroupInfoPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iGroupInfoPresenter = (IGroupInfoPresenter) getIPresenter();
        if (iGroupInfoPresenter == null) {
            throw new IllegalStateException("chatPresenter is null");
        }

    }

    public abstract void onGetGroupInfoResult(JsonResult jsonResult);

    public abstract void onGetGroupMemberResult(List<GroupMembersData> jsonResult);

    public abstract void onGetGroupIgnoreStatusResult(JsonResult jsonResult);

    public abstract void onChangeGroupIgnoreStatusResult(JsonResult jsonResult);
}
