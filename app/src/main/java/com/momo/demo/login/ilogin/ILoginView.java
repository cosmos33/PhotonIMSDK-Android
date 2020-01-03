package com.momo.demo.login.ilogin;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonAuth;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonLogin;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class ILoginView extends BaseActivity implements IView {
    protected ILoginPresenter loginPresenter;

    abstract public void showDialog();

    abstract public void hideDialog();

    abstract public void onLoginResult(JsonLogin requestResult);

    public abstract void onAuthResult(JsonAuth jsonAuth);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = (ILoginPresenter) getIPresenter();
    }

}
