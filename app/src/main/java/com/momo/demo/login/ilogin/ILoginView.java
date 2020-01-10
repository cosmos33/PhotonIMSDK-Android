package com.momo.demo.login.ilogin;

import com.cosmos.photonim.imbase.base.mvp.IBaseActivityView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonAuth;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonLogin;

public abstract class ILoginView extends IBaseActivityView<ILoginPresenter> {
    abstract public void showDialog();

    abstract public void hideDialog();

    abstract public void onLoginResult(JsonLogin requestResult);

    public abstract void onAuthResult(JsonAuth jsonAuth);
}
