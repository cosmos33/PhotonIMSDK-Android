package com.momo.demo.main.me;

import com.cosmos.photon.push.PhotonPushManager;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.me.ime.IMeModel;

public class MeModel extends IMeModel {
    @Override
    public void logOut() {
        PhotonPushManager.getInstance().unRegister();
        ImBaseBridge.getInstance().logout();
    }

    @Override
    public void changeNickName(String nickName, OnChangeNickNameListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().changeNickName(nickName,
                LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId()), result -> {
            JsonResult jsonResult = (JsonResult) result;
            if (listener != null) {
                listener.onChangeNickName((JsonSetNickName) jsonResult.get());
            }
        });

    }

    @Override
    public void getMyInfo(onGetMyInfoListener listener) {
        TaskExecutor.getInstance().createAsycTask(() ->
                HttpUtils.getInstance().getMyInfo(LoginInfo.getInstance().getSessionId(),
                        LoginInfo.getInstance().getUserId()), result -> {
            JsonResult jsonResult = (JsonResult) result;
            if (listener != null) {
                listener.onGetMyInfo((JsonMyInfo) jsonResult.get());
            }
        });
    }
}
