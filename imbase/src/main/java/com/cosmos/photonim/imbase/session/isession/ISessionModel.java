package com.cosmos.photonim.imbase.session.isession;

import com.cosmos.photonim.imbase.session.SessionData;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

import java.util.List;

public abstract class ISessionModel implements IModel {
    abstract public void loadLocalHostoryMsg(OnLoadHistoryListener onLoadHistoryListener);

    public abstract void getOtherInfo(SessionData sessionData, OnGetOtherInfoListener onGetOtherInfoListener);

    public abstract void saveSession(SessionData sessionData);

    public abstract void deleteSession(SessionData data, OnDeleteSessionListener onDeleteSessionListener);

    public abstract void clearSession(SessionData data, OnClearSessionListener onClearSessionListener);

    public abstract void getNewSession(int chatType, String chatWith, OnGetSessionListener onGetSessionListener);

    public abstract void getAllUnReadCount(OnGetAllUnReadCount onGetAllUnReadCount);

    public abstract void updateSessionUnreadCount(int chatType, String chatWith, int unReadCount);

    public abstract void loadHistoryFromRemote(OnLoadHistoryFromRemoteListener onLoadHistoryFromRemoteListener);

    public abstract void resendSendingStatusMsgs();

    public abstract void updateSessionAtType(SessionData sessionData);


    public interface OnLoadHistoryListener {
        void onLoadHistory(List<SessionData> sessionData);
    }

    public interface OnLoadHistoryFromRemoteListener {
        void onLoadHistoryFromRemote(List<SessionData> sessionData);
    }

    public interface OnGetOtherInfoListener {
        void onGetOtherInfo(JsonResult result);
    }

    public interface OnDeleteSessionListener {
        void onDeleteSession();
    }

    public interface OnClearSessionListener {
        void onClearSession();
    }


    public interface OnGetSessionListener {
        void onGetSession(SessionData sessionData);
    }

    public interface OnGetAllUnReadCount {
        void onGetAllUnReadCount(int result);
    }


}
