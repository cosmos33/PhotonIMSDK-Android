package com.cosmos.photonim.imbase.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.base.mvp.base.IView;
import com.cosmos.photonim.imbase.utils.ToastUtils;

public abstract class IRVBaseFragmentView<P> extends RvBaseFragment implements IView {
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (P) getIPresenter();
    }

    @Override
    public void toast(String content) {
        ToastUtils.showText(content);
    }
}
