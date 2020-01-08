package com.momo.demo.login.ilogin;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonAuth;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonLogin;

public abstract class ILoginModel implements IModel {
    abstract public void login(String userName, String pwd, OnLoginListener onLoginListener);

    public abstract void getAuth(String sessionId, String userId, OnGetAuthListener onGetAuthListener);

    public interface OnLoginListener {
        void onLoginResult(JsonLogin login);
    }

    public interface OnGetAuthListener {
        void onGetAuthResult(JsonAuth auth);
    }


}
