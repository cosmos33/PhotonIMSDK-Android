package com.momo.demo.regist.iregist;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class IRegistView extends BaseActivity implements IView, IRegistModel.IRegistListener {
    protected IRegistPresenter registPresenter;

    abstract public void showDialog();

    abstract public void hideDialog();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registPresenter = (IRegistPresenter) getIPresenter();
    }
}
