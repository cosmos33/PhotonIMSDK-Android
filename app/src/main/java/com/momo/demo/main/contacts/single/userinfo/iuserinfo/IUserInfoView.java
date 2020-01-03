package com.momo.demo.main.contacts.single.userinfo.iuserinfo;

import android.os.Bundle;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class IUserInfoView extends BaseActivity implements IView {
    protected IUserInfoPresenter infoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoPresenter = (IUserInfoPresenter) getIPresenter();
        if (infoPresenter == null) {
            throw new IllegalStateException("IUserInfoPresenter is null");
        }
    }

//    public abstract void onGetUserInfo(JsonOtherInfo jsonOtherInfo);
}
