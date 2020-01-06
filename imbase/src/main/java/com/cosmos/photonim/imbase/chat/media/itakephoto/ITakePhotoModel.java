package com.cosmos.photonim.imbase.chat.media.itakephoto;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

public abstract class ITakePhotoModel implements IModel {
    public abstract void changeTopStatus(OnChangeStatusListener listener);

    public abstract void changeIgnoreStatus(int chatType, String remoteId, boolean open, OnChangeStatusListener listener);

    public interface OnChangeStatusListener {
        void onChangeTopStatus(JsonSaveIgnoreInfo result);

        void onChangeIgnoreStatus(JsonResult success);
    }

}
