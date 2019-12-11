package com.momo.demo.main.forward.iforward;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.forward.ForwardData;

import java.util.List;


public abstract class IForwardView extends RvBaseActivity implements IView {
    protected IForwardPresenter iForwardPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iForwardPresenter = (IForwardPresenter) getIPresenter();
        if (iForwardPresenter == null) {
            throw new IllegalStateException("iForwardPresenter is null");
        }

    }

    public abstract void showContactsEmptyView();

    public abstract void loadContacts(List<ForwardData> contactsData);

    public abstract void updateOtherInfo(JsonResult result, ForwardData forwardData);
}
