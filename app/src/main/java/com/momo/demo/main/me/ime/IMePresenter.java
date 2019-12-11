package com.momo.demo.main.me.ime;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public abstract class IMePresenter<V extends IMeView, M extends IMeModel> extends IPresenter<V, M> {
    public IMePresenter(V iView) {
        super(iView);
    }

    public abstract void logOut();

    public abstract void changeNickName(String nickName);

    public abstract void getMyInfo();

    @Override
    public V getEmptyView() {
        return (V) new IMeView() {

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
