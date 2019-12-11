package com.momo.demo.login;

import android.os.Process;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.ExecutorUtil;
import com.momo.demo.login.ilogin.ILoginModel;
import com.momo.demo.login.ilogin.ILoginPresenter;
import com.momo.demo.login.ilogin.ILoginView;

import java.util.concurrent.Callable;

public class LoginPresenter extends ILoginPresenter<ILoginView, ILoginModel> {
    private static final String TAG = "LoginPresenter";

    public LoginPresenter(ILoginView iView) {
        super(iView);
    }

    @Override
    public ILoginModel generateIModel() {
        return new LoginModel();
    }

    @Override
    public void onLoginClick(String userName, String pwd) {
        getIView().showDialog();
        getiModel().login(userName, pwd, (result) -> {
            if (result == null || !result.success()) {
                getIView().hideDialog();
            }
            getIView().onLoginResult(result);
        });
    }

    @Override
    public void getAuth(String sessionId, String userId) {
        getiModel().getAuth(sessionId, userId, result -> {
            getIView().onAuthResult(result);
        });
    }

    @Override
    public void startIm() {
        AsycTaskUtil.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                LogUtils.log("startim", "start im");
                ImBaseBridge.getInstance().startIm();
                LogUtils.log("startim", "end im");
                return null;
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                getIView().hideDialog();
            }
        }, ExecutorUtil.getDefaultExecutor(), Process.THREAD_PRIORITY_FOREGROUND);
    }

}
