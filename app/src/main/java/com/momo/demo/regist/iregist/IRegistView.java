package com.momo.demo.regist.iregist;

import com.cosmos.photonim.imbase.base.mvp.IBaseActivityView;

public abstract class IRegistView extends IBaseActivityView<IRegistPresenter> implements IRegistModel.IRegistListener {

    abstract public void showDialog();

    abstract public void hideDialog();
}
