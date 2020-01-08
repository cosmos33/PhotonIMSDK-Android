package com.momo.demo.main.groupinfo.igroupinfo;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IGroupInfoModel implements IModel {
    public abstract void getGroupInfo(String sessionId, String userId, String gid, OnGetGroupInfoListener onGetGroupInfoListener);

    public abstract void getGroupIgnoreStatus(String sessionId, String userId, String gid, OnGetGroupIgnoreStatusListener onGetGroupIgnoreStatusListener);

    public abstract void changeGroupIgnoreStatus(String sessionId, String userId, String gid, int switchX, OnChangeGroupIgnoreStatusListener onChangeGroupIgnoreStatusListener);

    public interface OnGetGroupInfoListener {
        void onGetGroupInfoResult(JsonResult jsonResult);
    }

    public interface OnGetGroupIgnoreStatusListener {
        void onGetGroupIgnoreStatus(JsonResult jsonResult);
    }

    public interface OnChangeGroupIgnoreStatusListener {
        void onChangeGroupIgnoreStatus(JsonResult jsonResult);
    }


}
