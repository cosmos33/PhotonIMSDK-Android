package com.momo.demo.main.forward.iforward;

import com.cosmos.photonim.imbase.base.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.forward.ForwardData;

import java.util.List;


public abstract class IForwardView extends RvBaseActivity<IForwardPresenter> implements IView {
    public abstract void showContactsEmptyView();

    public abstract void loadContacts(List<ForwardData> contactsData);

    public abstract void updateOtherInfo(JsonResult result, ForwardData forwardData);
}
