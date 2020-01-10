package com.momo.demo.main.sessiontest.isessiontest;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.momo.demo.event.SessionTestEvent;
import com.momo.demo.main.sessiontest.SessionTestData;

import java.util.List;

public abstract class ISessionTestModel implements IModel {
    abstract public void loadLocalHostoryMsg(String sessionId, String userId, OnLoadHistoryListener onLoadHistoryListener);

    public abstract void getOtherInfo(SessionTestData sessionData, OnGetOtherInfoListener onGetOtherInfoListener);

    public abstract void saveSession(SessionTestData sessionData);

    public abstract void deleteSession(SessionTestData data, OnDeleteSessionListener onDeleteSessionListener);

    public abstract void clearSession(SessionTestData data, OnClearSessionListener onClearSessionListener);

    public abstract void getNewSession(int chatType, String chatWith, OnGetSessionListener onGetSessionListener);

    public abstract void getAllUnReadCount(OnGetAllUnReadCount onGetAllUnReadCount);

    public abstract void updateSessionUnreadCount(int chatType, String chatWith, int unReadCount);

    public abstract void loadHistoryFromRemote(String sessionId, String userId, OnLoadHistoryFromRemoteListener onLoadHistoryFromRemoteListener);

    public abstract void resendSendingStatusMsgs();

    public abstract void updateSessionAtType(SessionTestData sessionData);

    public abstract void addSessioTest(SessionTestEvent event, OnAddSessionTestResult onAddSessionTestResult);


    public interface OnLoadHistoryListener {
        void onLoadHistory(List<SessionTestData> sessionData);
    }

    public interface OnLoadHistoryFromRemoteListener {
        void onLoadHistoryFromRemote(List<SessionTestData> sessionData);
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
        void onGetSession(SessionTestData sessionData);
    }

    public interface OnGetAllUnReadCount {
        void onGetAllUnReadCount(int result);
    }

    public interface OnAddSessionTestResult {
        void onAddSeesionTest();
    }


}
