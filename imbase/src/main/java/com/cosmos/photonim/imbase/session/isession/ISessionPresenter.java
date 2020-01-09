package com.cosmos.photonim.imbase.session.isession;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.base.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.session.adapter.SessionData;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public abstract class ISessionPresenter<V extends ISessionView, M extends ISessionModel> extends IPresenter<V, M> {
    public ISessionPresenter(V iView) {
        super(iView);
    }

    public abstract void loadHistoryData();

    public abstract void loadHistoryFromRemote();

    public abstract void saveSession(SessionData sessionData);

    public abstract void upDateSessions();

    public abstract void deleteSession(SessionData data);

    public abstract void clearSession(SessionData data);

    public abstract void updateUnRead(String chatWith);

    public abstract void getNewSession(int chatType, String chatWith);

    public abstract void getSessionUnRead(int chatType, String chatWith);

    public abstract void getAllUnReadCount();

    public abstract void clearSesionUnReadCount(int chatType, String chatWith);

    @Override
    public V getEmptyView() {
        return (V) new ISessionView() {
            @Override
            public int getLayoutId() {
                return 0;
            }

            @Override
            protected void initView(View view) {

            }

            @Override
            public void onLoadHistory(List<SessionData> sessionData) {

            }

            @Override
            public void onDeleteSession(SessionData data) {

            }

            @Override
            public void onClearSession(SessionData data) {

            }

            @Override
            public void onNewSession(SessionData sessionData) {

            }

            @Override
            public void onGetAllUnReadCount(int result) {

            }

            @Override
            public void onLoadHistoryFromRemote(List<SessionData> sessionData) {

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

    public abstract void updateSessionAtType(SessionData sessionData);
}
