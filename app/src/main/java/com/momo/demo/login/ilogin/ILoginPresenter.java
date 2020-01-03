package com.momo.demo.login.ilogin;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonAuth;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonLogin;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public abstract class ILoginPresenter<V extends ILoginView, M extends ILoginModel> extends IPresenter<V, M> {
    public ILoginPresenter(V iView) {
        super(iView);
    }

    abstract public void onLoginClick(String userName, String pwd);

    public abstract void getAuth(String sessionId, String userId);

    @Override
    public V getEmptyView() {
        return (V) new ILoginView() {
            @Override
            public void showDialog() {

            }

            @Override
            public void hideDialog() {

            }

            @Override
            public void onLoginResult(JsonLogin requestResult) {

            }

            @Override
            public void onAuthResult(JsonAuth jsonAuth) {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }

    public abstract void startIm();
}
