package com.momo.demo.regist.iregist;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IRegistModel implements IModel {
    abstract public void regist(String userName, String pwd, IRegistListener iRegistListener);

    public interface IRegistListener {
        void onRegistResult(JsonResult result);
    }

}
