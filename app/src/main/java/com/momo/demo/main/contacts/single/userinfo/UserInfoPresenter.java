package com.momo.demo.main.contacts.single.userinfo;

import com.momo.demo.main.contacts.single.userinfo.iuserinfo.IUserInfoModel;
import com.momo.demo.main.contacts.single.userinfo.iuserinfo.IUserInfoPresenter;
import com.momo.demo.main.contacts.single.userinfo.iuserinfo.IUserInfoView;

public class UserInfoPresenter extends IUserInfoPresenter<IUserInfoView, IUserInfoModel> {
    public UserInfoPresenter(IUserInfoView iView) {
        super(iView);
    }

    @Override
    public void getUserInfo(String id) {
        getiModel().getUserInfo(id, jsonOtherInfo -> {
//            iUserInfoView.onGetUserInfo(jsonOtherInfo);
        });
    }

    @Override
    public IUserInfoModel generateIModel() {
        return new UserInfoModel();
    }
}
