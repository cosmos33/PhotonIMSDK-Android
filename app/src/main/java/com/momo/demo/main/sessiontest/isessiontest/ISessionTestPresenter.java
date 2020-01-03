package com.momo.demo.main.sessiontest.isessiontest;


import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.event.SessionTestEvent;
import com.momo.demo.main.sessiontest.SessionTestData;

import java.util.List;

public abstract class ISessionTestPresenter<V extends ISessionTestView, M extends ISessionTestModel> extends IPresenter<V, M> {
    public ISessionTestPresenter(V iView) {
        super(iView);
    }

    public abstract void loadHistoryData(String sessionId, String userId);

    public abstract void getOthersInfo(SessionTestData sessionData);

    public abstract void saveSession(SessionTestData sessionData);

    public abstract void upDateSessions(String sessionId, String userId);

    public abstract void deleteSession(SessionTestData data);

    @Override
    public V getEmptyView() {
        return (V) new ISessionTestView() {
            @Override
            public void onLoadHistory(List<SessionTestData> sessionData) {

            }

            @Override
            public void onGetOtherInfoResult(JsonResult result, SessionTestData sessionData) {

            }

            @Override
            public void onDeleteSession(SessionTestData data) {

            }

            @Override
            public void onClearSession(SessionTestData data) {

            }

            @Override
            public void onNewSession(SessionTestData sessionData) {

            }

            @Override
            public void onAddSessionResult() {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }

            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }
        };
    }

    public abstract void resendSendingStatusMsgs();

    public abstract void updateSessionAtType(SessionTestData sessionData);

    public abstract void addSessioTest(SessionTestEvent event);
}
