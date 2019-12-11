package com.momo.demo.main.contacts.single.userinfo.iuserinfo;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfo;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

public abstract class IUserInfoModel implements IModel {
    abstract public void getUserInfo(String id, OnUserInfoListener onUserInfoListener);

    public interface OnUserInfoListener {
        void onGetUserInfo(JsonOtherInfo jsonOtherInfo);
    }

}
