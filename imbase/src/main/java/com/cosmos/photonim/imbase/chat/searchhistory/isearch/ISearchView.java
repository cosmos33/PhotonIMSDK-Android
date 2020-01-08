package com.cosmos.photonim.imbase.chat.searchhistory.isearch;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;

public abstract class ISearchView extends RvBaseActivity implements IView {
    protected ISearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (ISearchPresenter) getIPresenter();
        if (presenter == null) {
            throw new IllegalStateException("contactPresenter is null");
        }
    }
}
