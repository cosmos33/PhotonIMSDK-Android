package com.momo.demo.main.me.ime;

import android.view.View;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;

public abstract class IMePresenter<V extends IMeViewView, M extends IMeModel> extends IPresenter<V, M> {
    public IMePresenter(V iView) {
        super(iView);
    }

    public abstract void logOut();

    public abstract void changeNickName(String nickName);

    public abstract void getMyInfo();

    @Override
    public V getEmptyView() {
        return (V) new IMeViewView() {

            @Override
            public int getLayoutId() {
                return 0;
            }

            @Override
            protected void initView(View view) {

            }

            @Override
            public void onChangeNickName(JsonSetNickName jsonResult) {

            }

            @Override
            public void onGetMyInfo(JsonMyInfo jsonResult) {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }

}
