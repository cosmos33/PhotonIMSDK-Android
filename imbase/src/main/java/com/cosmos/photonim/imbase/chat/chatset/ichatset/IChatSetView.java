package com.cosmos.photonim.imbase.chat.chatset.ichatset;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class IChatSetView extends BaseActivity implements IView {
    protected IChatSetPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (IChatSetPresenter) getIPresenter();
        if (presenter == null) {
            throw new IllegalStateException("contactPresenter is null");
        }
    }

    public abstract void onTopChangeStatusResult(boolean success);

    public abstract void onIgnoreChangeStatusResult(boolean success);

    public abstract void onGetIgnoreStatus(JsonResult result);
}
