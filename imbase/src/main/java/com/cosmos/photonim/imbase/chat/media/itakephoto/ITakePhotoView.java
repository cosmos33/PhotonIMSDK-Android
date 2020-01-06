package com.cosmos.photonim.imbase.chat.media.itakephoto;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class ITakePhotoView extends BaseActivity implements IView {
    protected ITakePhotoPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (ITakePhotoPresenter) getIPresenter();
        if (presenter == null) {
            throw new IllegalStateException("contactPresenter is null");
        }
    }
}
