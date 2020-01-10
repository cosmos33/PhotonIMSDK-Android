package com.momo.demo.main.groupinfo.igroupinfo;

import com.cosmos.photonim.imbase.base.mvp.base.IView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupInfoView extends RvBaseActivity<IGroupInfoPresenter> implements IView {
    public abstract void onGetGroupInfoResult(JsonResult jsonResult);

    public abstract void onGetGroupMemberResult(List<GroupMembersData> jsonResult);

    public abstract void onGetGroupIgnoreStatusResult(JsonResult jsonResult);

    public abstract void onChangeGroupIgnoreStatusResult(JsonResult jsonResult);
}
