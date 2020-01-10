package com.momo.demo.login.ilogin;

import com.cosmos.photonim.imbase.base.mvp.IBaseActivityView;

public abstract class ILoginView extends IBaseActivityView<ILoginPresenter> {
    abstract public void showDialog();

    abstract public void hideDialog();
}
