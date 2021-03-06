package com.momo.demo.main.me.ime;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

public abstract class IMeModel implements IModel {
    public abstract void logOut();

    public abstract void changeNickName(String nickName, OnChangeNickNameListener listener);

    public abstract void getMyInfo(onGetMyInfoListener listener);

    public interface OnChangeNickNameListener {
        void onChangeNickName(JsonSetNickName jsonResult);
    }

    public interface onGetMyInfoListener {
        void onGetMyInfo(JsonMyInfo jsonResult);
    }

}
