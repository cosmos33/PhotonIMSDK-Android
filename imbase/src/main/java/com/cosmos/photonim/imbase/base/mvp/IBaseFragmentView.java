package com.cosmos.photonim.imbase.base.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.base.mvp.base.IView;
import com.cosmos.photonim.imbase.utils.ToastUtils;

public abstract class IBaseFragmentView<P> extends BaseFragment implements IView {
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
