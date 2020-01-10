package com.momo.demo.regist.iregist;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

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
