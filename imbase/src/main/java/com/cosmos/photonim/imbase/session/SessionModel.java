package com.cosmos.photonim.imbase.session;

import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.PhotonIMSession;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.session.adapter.SessionData;
import com.cosmos.photonim.imbase.session.isession.ISessionModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactRecent;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SessionModel extends ISessionModel {
    public static final String DRAFT = "[草稿]";
    @Override
    public void loadLocalHostoryMsg(OnLoadHistoryListener onLoadHistoryListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getLocalHistoryMsg(),
                result -> onLoadHistoryListener.onLoadHistory((List<SessionData>) result));
    }

    @Override
    public void saveSession(SessionData sessionData) {
        TaskExecutor.getInstance().createAsycTask((Callable) () -> saveSessionInner(sessionData));
    }

    @Override
    public void deleteSession(SessionData data, OnDeleteSessionListener onDeleteSessionListener) {
        TaskExecutor.getInstance().createAsycTask(() -> deleteSessionInner(data), result -> {
            if (onDeleteSessionListener != null) {
                onDeleteSessionListener.onDeleteSession();
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
    public void loadHistoryFromRemote(OnLoadHistoryFromRemoteListener onLoadHistoryFromRemoteListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getRequestJson(), result -> {
            if (onLoadHistoryFromRemoteListener != null) {
                onLoadHistoryFromRemoteListener.onLoadHistoryFromRemote((List<SessionData>) result);
            }
        });
    }

    @Override
    public void resendSendingStatusMsgs() {
        TaskExecutor.getInstance().createAsycTaskChat((Callable) () -> {
            List<PhotonIMMessage> messageList = PhotonIMDatabase.getInstance().findMessageListByStatus(PhotonIMMessage.SENDING);
            if (CollectionUtils.isEmpty(messageList)) {
                return null;
            }
            String iconTemp = ImBaseBridge.getInstance().getMyIcon();
            for (PhotonIMMessage photonIMMessage : messageList) {
                ChatData chatData = new ChatData.Builder()
                        .msgType(photonIMMessage.messageType)
                        .chatWith(photonIMMessage.chatWith)
                        .from(photonIMMessage.from)
                        .chatType(photonIMMessage.chatType)
                        .to(photonIMMessage.to)
                        .time(photonIMMessage.time)
                        .notic(photonIMMessage.notic)
                        .icon(iconTemp)
                        .msgId(photonIMMessage.id)
                        .msgStatus(photonIMMessage.status)
                        .itemType(ChatModel.getItemType(photonIMMessage, ImBaseBridge.getInstance().getUserId()))
                        .msgBody(photonIMMessage.body)
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
    public void updateSessionAtType(SessionData sessionData) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMDatabase.getInstance().updateSessionAtType(sessionData.getChatType(), sessionData.getChatWith(), PhotonIMSession.SESSION_NO_AT);
            return null;
        });
    }

    @Override
    public void setSessionUnRead(SessionData data, OnSetUnreadListener onSetUnreadListener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            if (data.getUnreadCount() == 0) {
                PhotonIMDatabase.getInstance().setSessionUnread(data.getChatType(), data.getChatWith());
            } else {
                PhotonIMDatabase.getInstance().setSessionRead(data.getChatType(), data.getChatWith());
            }
            return null;
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (onSetUnreadListener != null) {
                    onSetUnreadListener.onSetResult();
                }
            }
        });
    }

//    private Object getSessionInner(int chatType, String chatWith) {
//        PhotonIMSession session = PhotonIMDatabase.getInstance().findSession(chatType, chatWith);
//        SessionData sessionData = new SessionData(session);
//        return sessionData;
//    }


    private Object deleteSessionInner(SessionData data) {
        PhotonIMDatabase.getInstance().deleteSession(data.getChatType(), data.getChatWith(), true);
        return null;
    }

    private Object saveSessionInner(SessionData sessionData) {
        PhotonIMSession photonIMSession = sessionData.convertToPhotonIMSession();
        PhotonIMDatabase.getInstance().saveSession(photonIMSession);
        return null;
    }

    public static Object getLocalHistoryMsg() {
        // TODO: 2019-08-09 没有加载更多
//        ArrayList<PhotonIMSession> sessionList = PhotonIMDatabase.getInstance().findSessionList(0, 50, false);
        List<PhotonIMSession> sessionList = PhotonIMDatabase.getInstance().findSessionListPage(null,50);
        if (CollectionUtils.isEmpty(sessionList)) {
            return null;
        }
        ArrayList<SessionData> result = new ArrayList<>(sessionList.size());
        SessionData sessionData;
        ArrayList<SessionData> sticker = new ArrayList<>();
        String tempContent = "";
        boolean showAtMsg;
        String lastMsgFrName;
        String icon;
        String nickName;
        boolean updateFromInfo;
        for (PhotonIMSession photonIMSession : sessionList) {
            lastMsgFrName = "";
            icon = "";
            tempContent = "";
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
                case PhotonIMMessage.LOCATION:
                    if (!TextUtils.isEmpty(photonIMSession.lastMsgContent)) {
                        tempContent = photonIMSession.lastMsgContent;
                    } else {
                        tempContent = "[位置]";
                    }
                    break;
                case PhotonIMMessage.FILE:
                    if (!TextUtils.isEmpty(photonIMSession.lastMsgContent)) {
                        tempContent = photonIMSession.lastMsgContent;
                    } else {
                        tempContent = "[文件]";
                    }
                    break;
                default:
//                    tempContent = "[未知消息]";
            }
            boolean isAtMeMsg = false;
            Profile profile = DBHelperUtils.getInstance().findProfile(photonIMSession.chatWith);
            if (!TextUtils.isEmpty(photonIMSession.lastMsgFr)
                    && !photonIMSession.lastMsgFr.equals(ImBaseBridge.getInstance().getUserId())
                    && photonIMSession.chatType == PhotonIMMessage.GROUP) {
                isAtMeMsg = isAtMeMsg(photonIMSession);
                Profile fromProfile = DBHelperUtils.getInstance().findProfile(photonIMSession.lastMsgFr);
                updateFromInfo = fromProfile == null;
                lastMsgFrName = fromProfile == null ? photonIMSession.lastMsgFr : fromProfile.getName();

                icon = profile == null ? "" : profile.getIcon();
                nickName = profile == null ? "" : profile.getName();
            } else {
                icon = profile == null ? "" : profile.getIcon();
                nickName = profile == null ? "" : profile.getName();
            }
            String sessionDraft = PhotonIMDatabase.getInstance().getSessionDraft(photonIMSession.chatType, photonIMSession.chatWith);
            if (!TextUtils.isEmpty(sessionDraft)) {
                tempContent = String.format("%s%s", DRAFT, sessionDraft);
            }
            sessionData = new SessionData.Builder()
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
                    .sticky(photonIMSession.sticky)
                    .icon(icon)
                    .nickName(nickName)
                    .updateFromInfo(updateFromInfo)
                    .lastMsgStatus(photonIMSession.lastMsgStatus)
                    .lastMsgId(photonIMSession.lastMsgId)
                    .lastMsgTime(photonIMSession.lastMsgTime)
                    .lastMsgTo(photonIMSession.lastMsgTo)
                    .lastMsgType(photonIMSession.lastMsgType)
                    .showAtTip(showAtMsg = (isAtMeMsg && showAtTip(photonIMSession)))
                    .generateAtMsg(showAtMsg ? Utils.generateAtMsg(lastMsgFrName, tempContent) : null)
                    .build();
            if (sessionData.isSticky()) {
                sticker.add(sessionData);
            } else {
                result.add(sessionData);
            }
        }
        if (sticker.size() != 0) {//置顶
            result.addAll(0, sticker);
        }
        return result;
    }

    private static boolean isAtMeMsg(PhotonIMSession photonIMSession) {
        return photonIMSession.atType == PhotonIMSession.SESSION_AT_ME || photonIMSession.atType == PhotonIMSession.SESSION_AT_ALL;
//        return true;
    }

    private static boolean showAtTip(PhotonIMSession photonIMSession) {
        return photonIMSession.unreadCount != 0;
    }

    private Object getRequestJson() {
        JsonContactRecent recentUser = ImBaseBridge.getInstance().getRecentUser();
        if (recentUser.success()) {
            List<JsonContactRecent.DataBean.ListsBean> lists = recentUser.getData().getLists();
            List<SessionData> sessionData = new ArrayList<>(lists.size());
            SessionData msgDataTemp;
            List<PhotonIMSession> photonIMSessions = new ArrayList<>();
            for (JsonContactRecent.DataBean.ListsBean list : lists) {
                if (list == null) {
                    continue;
                }
                msgDataTemp = new SessionData.Builder()
                        .lastMsgContent(PhotonIMDatabase.getInstance().getSessionLastMsgId(list.getType(),
                                list.getType() == PhotonIMMessage.SINGLE ? list.getUserId() : list.getId()))
                        .sticky(list.getIsTop() == 1)
                        .chatWith(list.getType() == PhotonIMMessage.SINGLE ? list.getUserId() : list.getId())
                        .chatType(list.getType())
                        .build();
//                msgDataTemp.setExtra(list.getNickname(), list.getAvatar());
                photonIMSessions.add(msgDataTemp.convertToPhotonIMSession());
                sessionData.add(msgDataTemp);
            }
            PhotonIMDatabase.getInstance().saveSessionBatch(photonIMSessions);
            return sessionData;
        }
        return null;
    }

}
