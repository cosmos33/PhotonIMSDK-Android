package com.cosmos.photonim.imbase.chat.chatset.ichatset;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;

public abstract class IChatSetModel implements IModel {
    public abstract void changeTopStatus(OnChangeStatusListener listener);

    public abstract void changeIgnoreStatus(int chatType, String remoteId, boolean open, OnChangeStatusListener listener);

    public abstract void getIgnoreStatus(String userId, OnGetIgnoreStatusListener onGetIgnoreStatusListener);

    public interface OnChangeStatusListener {
        void onChangeTopStatus(JsonSaveIgnoreInfo result);

        void onChangeIgnoreStatus(JsonResult success);
    }

    public interface OnGetIgnoreStatusListener {
        void onGetIgnoreStatus(JsonResult result);
    }

}
