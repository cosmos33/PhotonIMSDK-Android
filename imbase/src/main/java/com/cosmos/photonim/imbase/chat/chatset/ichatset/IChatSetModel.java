package com.cosmos.photonim.imbase.chat.chatset.ichatset;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IChatSetModel implements IModel {
    public abstract void changeTopStatus(int chatType, String id, boolean top, OnChangeStatusListener listener);

    public abstract void getTopStatus(int chatType, String id, OnChangeStatusListener chatSetPresenter);

    public abstract void changeIgnoreStatus(int chatType, String remoteId, boolean open, OnChangeStatusListener listener);

    public abstract void getIgnoreStatus(String userId, OnChangeStatusListener onGetIgnoreStatusListener);


    public interface OnChangeStatusListener {
        void onGetTopStatus(boolean top);

        void onChangeTopStatus();

        void onChangeIgnoreStatus(JsonResult success);
        void onGetIgnoreStatus(JsonResult result);
    }
}
