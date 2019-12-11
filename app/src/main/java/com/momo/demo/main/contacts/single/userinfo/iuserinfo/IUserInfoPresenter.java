package com.momo.demo.main.contacts.single.userinfo.iuserinfo;

import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public abstract class IUserInfoPresenter<V extends IUserInfoView, M extends IUserInfoModel> extends IPresenter<V, M> {
    public IUserInfoPresenter(V iView) {
        super(iView);
    }

    public abstract void getUserInfo(String id);

    @Override
    public V getEmptyView() {
        return (V) new IUserInfoView() {
            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }
}
