package com.momo.demo.login;

import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonAuth;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonLogin;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.ilogin.ILoginModel;

public class LoginModel extends ILoginModel {
    @Override
    public void login(String userName, String pwd, OnLoginListener onLoginListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().login(userName, pwd),
                result -> {
                    JsonResult jsonResult = (JsonResult) result;
                    if (onLoginListener != null) {
                        onLoginListener.onLoginResult((JsonLogin) jsonResult.get());
                    }
                });
    }

    @Override
    public void getAuth(String sessionId, String userId, OnGetAuthListener onGetAuthListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().getAuth(sessionId, userId),
                result -> {
                    JsonResult jsonResult = (JsonResult) result;
                    if (onGetAuthListener != null) {
                        onGetAuthListener.onGetAuthResult((JsonAuth) jsonResult.get());
                    }
                });
    }
}
