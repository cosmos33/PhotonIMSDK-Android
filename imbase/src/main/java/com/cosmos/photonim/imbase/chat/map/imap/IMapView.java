package com.cosmos.photonim.imbase.chat.map.imap;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class IMapView extends BaseActivity implements IView {
    protected IMapPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (IMapPresenter) getIPresenter();
        if (presenter == null) {
            throw new IllegalStateException("contactPresenter is null");
        }
    }
}
