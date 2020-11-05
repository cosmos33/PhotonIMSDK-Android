package com.momo.demo.main.sessiontest;

import com.momo.demo.event.SessionTestEvent;
import com.momo.demo.main.sessiontest.isessiontest.ISessionTestModel;
import com.momo.demo.main.sessiontest.isessiontest.ISessionTestPresenter;
import com.momo.demo.main.sessiontest.isessiontest.ISessionTestView;

import java.util.HashSet;
import java.util.Set;

public class SessionTestPresenter extends ISessionTestPresenter<ISessionTestView, ISessionTestModel> {
    private Set<String> othersInfoSet;

    public SessionTestPresenter(ISessionTestView iView) {
        super(iView);
    }

    @Override
    public void loadHistoryData(String sessionId, String userId) {
        getiModel().loadLocalHostoryMsg(sessionId, userId, messageData -> {
            getIView().onLoadHistory(messageData);
        });
    }

    @Override
    public void getOthersInfo(SessionTestData sessionTestData) {
        if (othersInfoSet == null) {
            othersInfoSet = new HashSet<>();
        }
        if (othersInfoSet.contains(sessionTestData.getChatWith())) {
            return;
        }
        othersInfoSet.add(sessionTestData.getChatWith());
        getiModel().getOtherInfo(sessionTestData, result -> {
            othersInfoSet.remove(sessionTestData.getChatWith());
            getIView().onGetOtherInfoResult(result, sessionTestData);
        });
    }

    @Override
    public void saveSession(SessionTestData sessionTestData) {
        getiModel().saveSession(sessionTestData);
    }

    @Override
    public void upDateSessions(String sessionId, String userId) {
        getiModel().loadLocalHostoryMsg(sessionId, userId, messageData -> {
            getIView().onLoadHistory(messageData);
        });
    }

    @Override
    public void deleteSession(SessionTestData data) {
        getiModel().deleteSession(data, () -> {
            getIView().onDeleteSession(data);
        });
    }

    @Override
    public boolean getCurrentIMLoginStatus() {
        return getiModel().getCurrentIMLoginStatus();
    }

    @Override
    public void resendSendingStatusMsgs() {
        getiModel().resendSendingStatusMsgs();
    }

    @Override
    public void updateSessionAtType(SessionTestData sessionTestData) {
        getiModel().updateSessionAtType(sessionTestData);
    }

    @Override
    public void addSessioTest(SessionTestEvent event) {
        getiModel().addSessioTest(event, new ISessionTestModel.OnAddSessionTestResult() {
            @Override
            public void onAddSeesionTest() {
                getIView().onAddSessionResult();
            }
        });
    }

    @Override
    public ISessionTestModel generateIModel() {
        return new SessionTestModel();
    }
}
