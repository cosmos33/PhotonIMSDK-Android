package com.momo.demo.regist;

import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.regist.iregist.IRegistModel;

public class RegistModel extends IRegistModel {
    @Override
    public void regist(String userName, String pwd, IRegistListener iRegistListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().regist(userName, pwd), result -> {
            if (iRegistListener != null) {
                iRegistListener.onRegistResult((JsonResult) result);
            }
        });
    }
}
