package com.momo.demo.main.groupinfo.igroupinfo;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IGroupInfoModel implements IModel {
    public abstract void changeTopStatus(int chatType, String id, boolean top, IChatSetModel.OnChangeStatusListener listener);

    public abstract void getTopStatus(int chatType, String id, IChatSetModel.OnChangeStatusListener chatSetPresenter);

    public abstract void getGroupInfo(String sessionId, String userId, String gid, OnGetGroupInfoListener onGetGroupInfoListener);

    public abstract void getGroupIgnoreStatus(String sessionId, String userId, String gid, IChatSetModel.OnChangeStatusListener listener);

    public abstract void changeGroupIgnoreStatus(String sessionId, String userId, String gid, int switchX, IChatSetModel.OnChangeStatusListener listener);

    public interface OnGetGroupInfoListener {
        void onGetGroupInfoResult(JsonResult jsonResult);
    }
}
