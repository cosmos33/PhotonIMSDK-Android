package com.momo.demo.main.me.ime;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;

public abstract class IMeView extends BaseFragment implements IView {
    protected IMePresenter registPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registPresenter = (IMePresenter) getIPresenter();
    }

//    public abstract void onLogout();

    public abstract void onChangeNickName(JsonSetNickName jsonResult);

    public abstract void onGetMyInfo(JsonMyInfo jsonResult);
}
