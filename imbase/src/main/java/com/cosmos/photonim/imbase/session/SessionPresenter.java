package com.cosmos.photonim.imbase.session;

import com.cosmos.photonim.imbase.session.isession.ISessionModel;
import com.cosmos.photonim.imbase.session.isession.ISessionPresenter;
import com.cosmos.photonim.imbase.session.isession.ISessionView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionPresenter extends ISessionPresenter<ISessionView, ISessionModel> {
    private Set<String> othersInfoSet;
    public SessionPresenter(ISessionView iView) {
        super(iView);
    }

    @Override
    public void loadHistoryData(String sessionId, String userId) {
        getiModel().loadLocalHostoryMsg(sessionId, userId, messageData -> {
            getIView().onLoadHistory(messageData);
        });
    }

    @Override
    public void getOthersInfo(SessionData sessionData) {
        if (othersInfoSet == null) {
            othersInfoSet = new HashSet<>();
        }
        if (othersInfoSet.contains(sessionData.getChatWith())) {
            return;
        }
        othersInfoSet.add(sessionData.getChatWith());
        getiModel().getOtherInfo(sessionData, result -> {
            othersInfoSet.remove(sessionData.getChatWith());
            getIView().onGetOtherInfoResult(result, sessionData);
        });
    }

    @Override
    public void saveSession(SessionData sessionData) {
        getiModel().saveSession(sessionData);
    }

    @Override
    public void upDateSessions(String sessionId, String userId) {
        getiModel().loadLocalHostoryMsg(sessionId, userId, messageData -> {
            getIView().onLoadHistory(messageData);
        });
    }

    @Override
    public void deleteSession(SessionData data) {
        getiModel().deleteSession(data, () -> {
            getIView().onDeleteSession(data);
        });
    }

    @Override
    public void clearSession(SessionData data) {
        getiModel().clearSession(data, () -> {
            getIView().onClearSession(data);
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
                getIView().onNewSession(sessionData);
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
                getIView().onGetAllUnReadCount(result);
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

    @Override
    public void loadHistoryFromRemote(String sessionId, String userId) {
        getiModel().loadHistoryFromRemote(sessionId, userId, new ISessionModel.OnLoadHistoryFromRemoteListener() {
            @Override
            public void onLoadHistoryFromRemote(List<SessionData> sessionData) {
                getIView().onLoadHistoryFromRemote(sessionData);
            }
        });
    }

    @Override
    public ISessionModel generateIModel() {
        return new SessionModel();
    }
}
