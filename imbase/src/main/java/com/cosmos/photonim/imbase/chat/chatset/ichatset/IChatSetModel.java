package com.cosmos.photonim.imbase.chat.chatset.ichatset;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IChatSetModel implements IModel {
    public abstract void changeTopStatus(int chatType, String id, boolean top, OnChangeStatusListener listener);

    public abstract void getTopStatus(int chatType, String id, OnChangeStatusListener chatSetPresenter);

    public abstract void changeIgnoreStatus(int chatType, String remoteId, boolean open, OnChangeStatusListener listener);

    public abstract void getIgnoreStatus(String userId, OnChangeStatusListener onGetIgnoreStatusListener);

    public abstract void focusUserStatus(boolean focus,String useId, String chatWith,onChannelMsgListener onChannelMsgListener);

    public abstract boolean getFocusUserStatus(int chatType,String userId,String chatWith);

    public abstract void setPushAndSaveStatus(int chatType,String userId,String chatWith,boolean save,boolean push,boolean history);

    public abstract boolean getSaveStatus(int chatType,String userId,String chatWith);

    public abstract boolean getPushStatus(int chatType,String userId,String chatWith);

    public abstract boolean getHistoryStatus(int chatType,String userId,String chatWith);

    public abstract void setSendTimeoutStatus(boolean sendTimeout,int chatType,String userId,String chatWith);

    public abstract boolean getSendTimeoutStatus(int chatType,String userId,String chatWith);

    public abstract void setIncreaseUnreadStatus(boolean unreadStatus,int chatType,String userId,String chatWith);

    public abstract boolean getIncreaseUnreadStatus(int chatType,String userId,String chatWith);


    public interface OnChangeStatusListener {
        void onGetTopStatus(boolean top);

        void onChangeTopStatus();

        void onChangeIgnoreStatus(JsonResult success);
        void onGetIgnoreStatus(JsonResult result);
    }

    public interface onChannelMsgListener{
        void onChannelMsg(int ec,String em);
    }
}
