package com.cosmos.photonim.imbase.chat.map.imap;

import com.cosmos.maplib.map.MyLatLng;
import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;

public abstract class IMapModel implements IModel {
    public abstract void sendPosition(MyLatLng myLatLng);

    public interface OnChangeStatusListener {
        void onChangeTopStatus(JsonSaveIgnoreInfo result);

        void onChangeIgnoreStatus(JsonResult success);
    }

}
