package com.momo.demo.main.sessiontest;

import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.PhotonIMSession;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.dbhelper.sessiontest.SessionTest;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactRecent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.event.SessionTestEvent;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.sessiontest.isessiontest.ISessionTestModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SessionTestModel extends ISessionTestModel {
    @Override
    public void loadLocalHostoryMsg(String sessionId, String userId, OnLoadHistoryListener onLoadHistoryListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getLocalHistoryMsg(),
                result -> onLoadHistoryListener.onLoadHistory((List<SessionTestData>) result));
    }

    @Override
    public void getOtherInfo(SessionTestData sessionTestData, OnGetOtherInfoListener onGetOtherInfoListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getOtherInfoInner(sessionTestData)
                , result -> {
                    if (onGetOtherInfoListener != null) {
                        onGetOtherInfoListener.onGetOtherInfo((JsonResult) result);
                    }
                });
    }

    private Object getOtherInfoInner(SessionTestData sessionTestData) {
        if (sessionTestData.getChatType() == PhotonIMMessage.GROUP) {
            if (sessionTestData.getNickName() == null) {
                return getGroupInfo(sessionTestData.getChatWith(), sessionTestData);
            } else if (sessionTestData.isUpdateFromInfo()) {
                return getUserinfo(sessionTestData.getLastMsgFr(), sessionTestData, false);
            }
            return null;
        } else {
            return getUserinfo(sessionTestData.getChatWith(), sessionTestData, true);
        }
    }

    private Object getGroupInfo(String otherId, SessionTestData sessionTestData) {
        JsonResult othersInfo = HttpUtils.getInstance().getGroupProfile(LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId(), otherId);
        if (othersInfo.success()) {
            JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) othersInfo.get();
            JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
            DBHelperUtils.getInstance().saveProfile(sessionTestData.getChatWith(), profile.getName(), profile.getAvatar());
        }
        return othersInfo;
    }

    private Object getUserinfo(String otherId, SessionTestData sessionTestData, boolean saveSessionExtra) {
        JsonResult othersInfo = (JsonResult) HttpUtils.getInstance().getOthersInfo(new String[]{otherId}, LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId());
        if (othersInfo.success()) {
            if (((JsonOtherInfoMulti) othersInfo.get()).getData().getLists().size() > 0) {
                List<JsonOtherInfoMulti.DataBean.ListsBean> lists = ((JsonOtherInfoMulti) othersInfo.get()).getData().getLists();
                DBHelperUtils.getInstance().saveProfile(lists.get(0).getUserId(),
                        lists.get(0).getAvatar(), lists.get(0).getNickname());

            }
        }
        return othersInfo;
    }

    @Override
    public void saveSession(SessionTestData sessionTestData) {
        TaskExecutor.getInstance().createAsycTask((Callable) () -> saveSessionInner(sessionTestData));
    }

    @Override
    public void deleteSession(SessionTestData data, OnDeleteSessionListener onDeleteSessionListener) {
        TaskExecutor.getInstance().createAsycTask(() -> deleteSessionInner(data), result -> {
            if (onDeleteSessionListener != null) {
                onDeleteSessionListener.onDeleteSession();
            }
        });
    }

    @Override
    public void clearSession(SessionTestData data, OnClearSessionListener onDeleteSessionListener) {
        TaskExecutor.getInstance().createAsycTask(() -> clearSessionInner(data), result -> {
            if (onDeleteSessionListener != null) {
                onDeleteSessionListener.onClearSession();
            }
        });
    }

    @Override
    public void getNewSession(int chatType, String chatWith, OnGetSessionListener onGetSessionListener) {
//        TaskExecutor.getInstance().createAsycTask(()-> getSessionInner(chatType,chatWith), result -> {
//            if (onGetSessionListener != null) {
//                onGetSessionListener.onGetSession(result);
//            }
//        });
    }

    @Override
    public void getAllUnReadCount(OnGetAllUnReadCount onGetAllUnReadCount) {
        TaskExecutor.getInstance().createAsycTask(() -> PhotonIMDatabase.getInstance().getTotalUnreadCount(), result -> {
            if (onGetAllUnReadCount != null) {
                onGetAllUnReadCount.onGetAllUnReadCount((Integer) result);
            }
        });
    }

    @Override
    public void updateSessionUnreadCount(int chatType, String chatWith, int unReadCount) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            if (chatType == PhotonIMMessage.GROUP) {
                PhotonIMDatabase.getInstance().updateSessionAtType(chatType, chatWith, PhotonIMSession.SESSION_NO_AT);
            }
            PhotonIMDatabase.getInstance().updateSessionUnreadCount(chatType, chatWith, unReadCount);
            return null;
        });
    }

    @Override
    public void loadHistoryFromRemote(String sessionId, String userId, OnLoadHistoryFromRemoteListener onLoadHistoryFromRemoteListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getRequestJson(sessionId, userId), result -> {
            if (onLoadHistoryFromRemoteListener != null) {
                onLoadHistoryFromRemoteListener.onLoadHistoryFromRemote((List<SessionTestData>) result);
            }
        });
    }

    @Override
    public void resendSendingStatusMsgs() {
        TaskExecutor.getInstance().createAsycTaskChat((Callable) () -> {
            ArrayList<PhotonIMMessage> messageList = PhotonIMDatabase.getInstance().findMessageListByStatus(PhotonIMMessage.SENDING);
            if (CollectionUtils.isEmpty(messageList)) {
                return null;
            }
            String iconTemp = ImBaseBridge.getInstance().getMyIcon();
            String myId = ImBaseBridge.getInstance().getUserId();
            for (PhotonIMMessage photonIMMessage : messageList) {
                ChatData chatData = new ChatData.Builder()
                        .msgType(photonIMMessage.messageType)
                        .chatWith(photonIMMessage.chatWith)
                        .localFile(photonIMMessage.localFile)
                        .fileUrl(photonIMMessage.fileUrl)
                        .from(photonIMMessage.from)
                        .chatType(photonIMMessage.chatType)
                        .to(photonIMMessage.to)
                        .time(photonIMMessage.time)
                        .notic(photonIMMessage.notic)
                        .content(photonIMMessage.content)
                        .icon(iconTemp)
                        .msgId(photonIMMessage.id)
                        .msgStatus(photonIMMessage.status)
                        .itemType(ChatModel.getItemType(photonIMMessage, myId))
                        .voiceDuration(photonIMMessage.mediaTime)
                        .build();
                PhotonIMClient.getInstance().sendMessage(photonIMMessage, new PhotonIMClient.PhotonIMSendCallback() {
                    @Override
                    public void onSent(int code, String msg, long retTime) {
                        EventBus.getDefault().post(new ChatDataWrapper(chatData, code, msg));
                    }
                });

//                PhotonIMClient.getInstance().sendMessage(photonIMMessage, (code, msg) -> {
//                    EventBus.getDefault().post(new ChatDataWrapper(chatData, code, msg));
//                });
            }
            return null;
        });
    }

    @Override
    public void updateSessionAtType(SessionTestData sessionTestData) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMDatabase.getInstance().updateSessionAtType(sessionTestData.getChatType(), sessionTestData.getChatWith(), PhotonIMSession.SESSION_NO_AT);
            return null;
        });
    }

    @Override
    public void addSessioTest(SessionTestEvent event, OnAddSessionTestResult onAddSessionTestResult) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            if (event.chatTypeList.size() != 0) {
                List<SessionTest> sessionTests = new ArrayList<>();
                for (int i = 0; i < event.chatTypeList.size(); i++) {
                    SessionTest sessionTest = new SessionTest();
                    sessionTest.setUserId(ImBaseBridge.getInstance().getUserId());
                    sessionTest.setChatType(event.chatTypeList.get(i));
                    sessionTest.setChatWith(event.chatWithList.get(i));
                    sessionTests.add(sessionTest);
                }
                DBHelperUtils.getInstance().saveSessionTestList(sessionTests);
            }
            return null;
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (onAddSessionTestResult != null) {
                    onAddSessionTestResult.onAddSeesionTest();
                }
            }
        });
    }

    private Object getSessionInner(int chatType, String chatWith) {
        PhotonIMSession session = PhotonIMDatabase.getInstance().findSession(chatType, chatWith);
        SessionTestData sessionTestData = new SessionTestData(session);
        return sessionTestData;
    }


    private Object clearSessionInner(SessionTestData data) {
        PhotonIMDatabase.getInstance().clearMessage(data.getChatType(), data.getChatWith());
        return null;
    }

    private Object deleteSessionInner(SessionTestData data) {
        DBHelperUtils.getInstance().deleteSessionTestData(data.getChatType(), data.getChatWith());
        return null;
    }

    private Object saveSessionInner(SessionTestData sessionTestData) {
        PhotonIMSession photonIMSession = sessionTestData.convertToPhotonIMSession();
        PhotonIMDatabase.getInstance().saveSession(photonIMSession);
        return null;
    }

    public static Object getLocalHistoryMsg() {
        // TODO: 2019-08-09 没有加载更多
//        ArrayList<PhotonIMSession> sessionList = PhotonIMDatabase.getInstance().findSessionList(0, 50, false);
        List<SessionTest> allSessionTest = DBHelperUtils.getInstance().findAllSessionTest(ImBaseBridge.getInstance().getUserId());
        if (CollectionUtils.isEmpty(allSessionTest)) {
            return null;
        }
        ArrayList<PhotonIMSession> sessionList = new ArrayList<>(allSessionTest.size());
        for (SessionTest sessionTest : allSessionTest) {
            PhotonIMSession session = PhotonIMDatabase.getInstance().findSession(sessionTest.getChatType(), sessionTest.getChatWith());
            if (session == null) {
                session = fakeSession(sessionTest);
            }
            sessionList.add(session);
        }
        ArrayList<SessionTestData> result = new ArrayList<>(allSessionTest.size());
        SessionTestData sessionTestData;
        String tempContent;
        String lastMsgFrName;
        boolean updateFromInfo;
        for (PhotonIMSession photonIMSession : sessionList) {
            lastMsgFrName = "";
            updateFromInfo = false;
            switch (photonIMSession.lastMsgType) {
                case PhotonIMMessage.AUDIO:
                    if (!TextUtils.isEmpty(photonIMSession.lastMsgContent)) {
                        tempContent = photonIMSession.lastMsgContent;
                    } else {
                        tempContent = "[语音]";
                    }
                    break;
                case PhotonIMMessage.IMAGE:
                    if (!TextUtils.isEmpty(photonIMSession.lastMsgContent)) {
                        tempContent = photonIMSession.lastMsgContent;
                    } else {
                        tempContent = "[图片]";
                    }
                    break;
                case PhotonIMMessage.VIDEO:
                    if (!TextUtils.isEmpty(photonIMSession.lastMsgContent)) {
                        tempContent = photonIMSession.lastMsgContent;
                    } else {
                        tempContent = "[视频]";
                    }
                    break;
                case PhotonIMMessage.TEXT:
                    tempContent = photonIMSession.lastMsgContent;
                    break;
                case PhotonIMMessage.RAW:
                    // FIXME 默认自定义消息根据lastMsgContent进行填充，待修改
                    tempContent = "[自定义消息]" + photonIMSession.lastMsgContent;
                    break;
                default:
                    tempContent = "[未知消息]";
            }
            if (photonIMSession.lastMsgFr != null
                    && !photonIMSession.lastMsgFr.equals(ImBaseBridge.getInstance().getUserId())
                    && photonIMSession.chatType == PhotonIMMessage.GROUP) {
                Profile profile = DBHelperUtils.getInstance().findProfile(photonIMSession.lastMsgFr);
                updateFromInfo = profile == null;
                lastMsgFrName = profile == null ? photonIMSession.lastMsgFr : profile.getName();
            }
            sessionTestData = new SessionTestData.Builder()
                    .chatType(photonIMSession.chatType)
                    .chatWith(photonIMSession.chatWith)
                    .draft(photonIMSession.draft)
                    .extra(photonIMSession.extra)
                    .ignoreAlert(photonIMSession.ignoreAlert)
                    .orderId(photonIMSession.orderId)
                    .unreadCount(photonIMSession.unreadCount)
                    .lastMsgContent(tempContent)
                    .lastMsgFr(photonIMSession.lastMsgFr)
                    .lastMsgFrName(lastMsgFrName)
                    .updateFromInfo(updateFromInfo)
                    .lastMsgStatus(photonIMSession.lastMsgStatus)
                    .lastMsgId(photonIMSession.lastMsgId)
                    .lastMsgTime(photonIMSession.lastMsgTime)
                    .lastMsgTo(photonIMSession.lastMsgTo)
                    .lastMsgType(photonIMSession.lastMsgType)
                    .build();
            Profile profile = DBHelperUtils.getInstance().findProfile(photonIMSession.chatWith);
            if (profile != null) {
                sessionTestData.setNickName(profile.getName());
                sessionTestData.setIcon(profile.getIcon());
            }
            result.add(sessionTestData);
        }
        return result;
    }

    private static PhotonIMSession fakeSession(SessionTest sessionTest) {
        PhotonIMSession photonIMSession = new PhotonIMSession();
        photonIMSession.chatWith = sessionTest.getChatWith();
        photonIMSession.chatType = sessionTest.getChatType();
        photonIMSession.lastMsgType = PhotonIMMessage.TEXT;
        return photonIMSession;
    }

    private Object getRequestJson(String sessionId, String userId) {
        JsonContactRecent recentUser = (JsonContactRecent) HttpUtils.getInstance().getRecentUser(sessionId, userId).get();
        if (recentUser.success()) {
            List<JsonContactRecent.DataBean.ListsBean> lists = recentUser.getData().getLists();
            List<SessionTestData> sessionDatumTests = new ArrayList<>(lists.size());
            SessionTestData msgDataTemp;
            List<PhotonIMSession> photonIMSessions = new ArrayList<>();
            for (JsonContactRecent.DataBean.ListsBean list : lists) {
                if (list == null) {
                    continue;
                }
                msgDataTemp = new SessionTestData.Builder()
                        .lastMsgContent(PhotonIMDatabase.getInstance().getSessionLastMsgId(list.getType(),
                                list.getType() == PhotonIMMessage.SINGLE ? list.getUserId() : list.getId()))
//                        .sticky(list.getIsTop() == 0) //不考虑置顶
                        .chatWith(list.getType() == PhotonIMMessage.SINGLE ? list.getUserId() : list.getId())
                        .chatType(list.getType())
                        .build();
                msgDataTemp.setExtra(list.getNickname(), list.getAvatar());
                photonIMSessions.add(msgDataTemp.convertToPhotonIMSession());
                sessionDatumTests.add(msgDataTemp);
            }
            PhotonIMDatabase.getInstance().saveSessionBatch(photonIMSessions);
            return sessionDatumTests;
        }
        return null;
    }

}
