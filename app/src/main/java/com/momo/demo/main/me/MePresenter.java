package com.momo.demo.main.me;

import com.momo.demo.main.me.ime.IMeModel;
import com.momo.demo.main.me.ime.IMePresenter;
import com.momo.demo.main.me.ime.IMeView;

public class MePresenter extends IMePresenter<IMeView, IMeModel> {
    public MePresenter(IMeView iView) {
        super(iView);
    }

    @Override
    public void logOut() {
        getiModel().logOut();
    }

    @Override
    public void changeNickName(String nickName) {
        getiModel().changeNickName(nickName, jsonResult -> getIView().onChangeNickName(jsonResult));
    }

    @Override
    public void getMyInfo() {
        getiModel().getMyInfo(jsonResult -> {
            getIView().onGetMyInfo(jsonResult);
        });
    }

    @Override
    public IMeModel generateIModel() {
        return new MeModel();
    }
}
