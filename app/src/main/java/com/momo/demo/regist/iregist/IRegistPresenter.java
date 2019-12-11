package com.momo.demo.regist.iregist;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public abstract class IRegistPresenter<V extends IRegistView, M extends IRegistModel> extends IPresenter<V, M> {
    public IRegistPresenter(V iView) {
        super(iView);
    }

    abstract public void onRegistClick(String userName, String pwd, IRegistModel.IRegistListener iRegistListener);

    @Override
    public V getEmptyView() {
        return (V) new IRegistView() {
            @Override
            public void showDialog() {

            }

            @Override
            public void hideDialog() {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }

            @Override
            public void onRegistResult(JsonResult result) {

            }
        };
    }
}
