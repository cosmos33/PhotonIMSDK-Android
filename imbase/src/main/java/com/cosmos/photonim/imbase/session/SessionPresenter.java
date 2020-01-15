package com.cosmos.photonim.imbase.session;

import com.cosmos.photonim.imbase.businessmodel.PersionInfoModel;
import com.cosmos.photonim.imbase.session.adapter.SessionData;
import com.cosmos.photonim.imbase.session.isession.ISessionModel;
import com.cosmos.photonim.imbase.session.isession.ISessionPresenter;
import com.cosmos.photonim.imbase.session.isession.ISessionView;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.LocalRestoreUtils;
import com.cosmos.photonim.imbase.utils.event.AllUnReadCount;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SessionPresenter extends ISessionPresenter<ISessionView, ISessionModel> {
    private PersionInfoModel persionInfoModel;
    private boolean isFirstLoad;
    private List<SessionData> baseDataList;

    public SessionPresenter(ISessionView iView) {
        super(iView);
        isFirstLoad = LocalRestoreUtils.getFirstLoadSession();
    }

    @Override
    public void loadHistoryData() {
        getiModel().loadLocalHostoryMsg(messageData -> {
            onLoadHistory(messageData);
        });
    }

    @Override
    public void saveSession(SessionData sessionData) {
        getiModel().saveSession(sessionData);
    }

    @Override
    public void upDateSessions() {
        getiModel().loadLocalHostoryMsg(messageData -> {
            onLoadHistory(messageData);
        });
    }

    @Override
    public void deleteSession(SessionData data) {
        getiModel().deleteSession(data, () -> {
            onDeleteSession(data);
        });
    }

    @Override
    public void updateUnRead(String chatWith) {
//        iSessionModel.updateUnRead(chatWith);
    }

    @Override
    public void getNewSession(int chatType, String chatWith) {
        getiModel().getNewSession(chatType, chatWith, new ISessionModel.OnGetSessionListener() {
            @Override
            public void onGetSession(SessionData sessionData) {
                onNewSession(sessionData);
            }
        });
    }

    @Override
    public void getSessionUnRead(int chatType, String chatWith) {
//        iSessionModel.getUnReadAndAllUnRead();
    }

    @Override
    public void getAllUnReadCount() {
        getiModel().getAllUnReadCount(new ISessionModel.OnGetAllUnReadCount() {
            @Override
            public void onGetAllUnReadCount(int result) {
                EventBus.getDefault().post(new AllUnReadCount(result));
            }
        });
    }

    @Override
    public void clearSesionUnReadCount(int chatType, String chatWith) {
        getiModel().updateSessionUnreadCount(chatType, chatWith, 0);
    }

    @Override
    public void resendSendingStatusMsgs() {
        getiModel().resendSendingStatusMsgs();
    }

    @Override
    public void updateSessionAtType(SessionData sessionData) {
        getiModel().updateSessionAtType(sessionData);
    }

    public void onLoadHistory(List<SessionData> sessionData) {
        if (CollectionUtils.isEmpty(sessionData)) {
            if (isFirstLoad) {
                loadHistoryFromRemote();
                isFirstLoad = false;
            }
            getIView().setNoMsgViewVisibility(true);
            return;
        }
        isFirstLoad = false;
        getIView().setNoMsgViewVisibility(false);
        baseDataList.clear();
//        baseDataMap.clear();
        if (sessionData != null) {
            baseDataList.addAll(sessionData);
        }
        getIView().notifyDataSetChanged();
    }

    public void onLoadHistoryFromRemote(List<SessionData> sessionData) {
        if (CollectionUtils.isEmpty(sessionData)) {
            getIView().setNoMsgViewVisibility(true);
            return;
        }
        getIView().setNoMsgViewVisibility(false);
        baseDataList.clear();
//        baseDataMap.clear();
        baseDataList.addAll(sessionData);
        getIView().notifyDataSetChanged();
    }

    public void onDeleteSession(SessionData data) {
        getIView().dismissSessionDialog();
        baseDataList.remove(data);
        getIView().notifyDataSetChanged();
        if (baseDataList.size() == 0) {
            getIView().setNoMsgViewVisibility(true);
        } else {
            getIView().setNoMsgViewVisibility(false);
        }
    }

    public void onNewSession(SessionData sessionData) {
        // TODO: 2019-08-12 置顶
        baseDataList.add(0, sessionData);
        getIView().notifyItemInserted(0);
    }

    @Override
    public ArrayList<SessionData> initData() {
        baseDataList = new ArrayList<>();
        return (ArrayList<SessionData>) baseDataList;
    }

    @Override
    public void loadHistoryFromRemote() {
        getiModel().loadHistoryFromRemote(new ISessionModel.OnLoadHistoryFromRemoteListener() {
            @Override
            public void onLoadHistoryFromRemote(List<SessionData> sessionData) {
                SessionPresenter.this.onLoadHistoryFromRemote(sessionData);
            }
        });
    }

    @Override
    public ISessionModel generateIModel() {
        return new SessionModel();
    }
}
