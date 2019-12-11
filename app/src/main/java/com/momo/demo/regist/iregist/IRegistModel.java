package com.momo.demo.regist.iregist;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

public abstract class IRegistModel implements IModel {
    abstract public void regist(String userName, String pwd, IRegistListener iRegistListener);

    public interface IRegistListener {
        void onRegistResult(JsonResult result);
    }

}
