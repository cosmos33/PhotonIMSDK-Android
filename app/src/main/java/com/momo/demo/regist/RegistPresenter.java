package com.momo.demo.regist;

import com.momo.demo.regist.iregist.IRegistModel;
import com.momo.demo.regist.iregist.IRegistPresenter;
import com.momo.demo.regist.iregist.IRegistView;

public class RegistPresenter extends IRegistPresenter<IRegistView, IRegistModel> {

    public RegistPresenter(IRegistView iView) {
        super(iView);
    }

    @Override
    public void onRegistClick(String userName, String pwd, IRegistModel.IRegistListener loginListener) {
        getiModel().regist(userName, pwd, loginListener);
    }

    @Override
    public IRegistModel generateIModel() {
        return new RegistModel();
    }
}
