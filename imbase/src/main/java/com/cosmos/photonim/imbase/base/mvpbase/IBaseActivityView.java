package com.cosmos.photonim.imbase.base.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;

public abstract class IBaseActivityView<P> extends BaseActivity implements IView {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (P) getIPresenter();
        if (presenter == null) {
            throw new IllegalStateException("chatPresenter is null");
        }

    }
}
