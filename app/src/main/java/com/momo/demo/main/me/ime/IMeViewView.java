package com.momo.demo.main.me.ime;

import com.cosmos.photonim.imbase.base.mvp.IBaseFragmentView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;

public abstract class IMeViewView extends IBaseFragmentView<IMePresenter> {
//    public abstract void onLogout();

    public abstract void onChangeNickName(JsonSetNickName jsonResult);

    public abstract void onGetMyInfo(JsonMyInfo jsonResult);
}
