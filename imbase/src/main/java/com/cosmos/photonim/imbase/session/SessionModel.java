package com.cosmos.photonim.imbase.session;

import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.PhotonIMSession;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.session.isession.ISessionModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.Profile;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactRecent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class SessionModel extends ISessionModel {
    @Override
    public void loadLocalHostoryMsg(OnLoadHistoryListener onLoadHistoryListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getLocalHistoryMsg(),
                result -> onLoadHistoryListener.onLoadHistory((List<SessionData>) result));
    }

    @Override
    public void getOtherInfo(SessionData sessionData, OnGetOtherInfoListener onGetOtherInfoListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getOtherInfoInner(sessionData)
                , result -> {
                    if (onGetOtherInfoListener != null) {
                        onGetOtherInfoListener.onGetOtherInfo((JsonResult) result);
                    }
                });
    }

    private Object getOtherInfoInner(SessionData sessionData) {
        if (sessionData.getChatType() == PhotonIMMessage.GROUP) {
            if (sessionData.getNickName() == null) {
                return getGroupInfo(sessionData.getChatWith(), sessionData);
            } else if (sessionData.isUpdateFromInfo()) {
                return getUserinfo(sessionData.getLastMsgFr(), sessionData, false);
            }
            return null;
        } else {
            return getUserinfo(sessionData.getChatWith(), sessionData, true);
        }
    }

    private Object getGroupInfo(String otherId, SessionData sessionData) {
        ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
        if (businessListener == null) {
            return null;
        }
        JsonResult othersInfo = businessListener.getGroupProfile(otherId);
        if (othersInfo.success()) {
            JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) othersInfo.get();
            JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
            Map<String, String> extra = sessionData.getExtra(profile.getName(), profile.getAvatar());
            PhotonIMDatabase.getInstance().updateSessionExtra(sessionData.getChatType(), sessionData.getChatWith(), extra);
        }
        return othersInfo;
    }

    private Object getUserinfo(String otherId, SessionData sessionData, boolean saveSessionExtra) {
        ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
        if (businessListener == null) {
            return null;
        }
        JsonResult othersInfo = businessListener.getOthersInfo(new String[]{otherId});
        if (othersInfo.success()) {
            if (((JsonOtherInfoMulti) othersInfo.get()).getData().getLists().size() > 0) {
                List<JsonOtherInfoMulti.DataBean.ListsBean> lists = ((JsonOtherInfoMulti) othersInfo.get()).getData().getLists();
                DBHelperUtils.getInstance().saveProfile(lists.get(0).getUserId(),
                        lists.get(0).getAvatar(), lists.get(0).getNickname());

            }
        }
        if (saveSessionExtra && othersInfo.success()) {
            // TODO: 2019-08-09 对服务器返回的数据进行校验
            JsonOtherInfoMulti jsonOtherInfo = (JsonOtherInfoMulti) othersInfo.get();
            if (jsonOtherInfo.getData().getLists().size() <= 0) {
                return othersInfo;
            }
            JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonOtherInfo.getData().getLists().get(0);
            Map<String, String> extra = sessionData.getExtra(listsBean.getNickname(), listsBean.getAvatar());
            PhotonIMDatabase.getInstance().updateSessionExtra(sessionData.getChatType(), sessionData.getChatWith(), extra);

        }
        return othersInfo;
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
    public void clearSession(SessionData data, OnClearSessionListener onDeleteSessionListener) {
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
                PhotonIMDatabase.getInstance().updateSessionAtType(chatType, chatWith, PhotonIMMessage.SESSION_NO_AT);
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
            ArrayList<PhotonIMMessage> messageList = PhotonIMDatabase.getInstance().findMessageListByStatus(PhotonIMMessage.SENDING);
            if (CollectionUtils.isEmpty(messageList)) {
                return null;
            }
            String iconTemp = ImBaseBridge.getInstance().getMyIcon();
            ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
            String myId = "";
            if (businessListener != null) {
                myId = businessListener.getUserId();
            }
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
                    public void onSent(int code, String msg,long retTime) {
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
            PhotonIMDatabase.getInstance().updateSessionAtType(sessionData.getChatType(), sessionData.getChatWith(), PhotonIMMessage.SESSION_NO_AT);
            return null;
        });
    }

    private Object getSessionInner(int chatType, String chatWith) {
        PhotonIMSession session = PhotonIMDatabase.getInstance().findSession(chatType, chatWith);
        SessionData sessionData = new SessionData(session);
        return sessionData;
    }


    private Object clearSessionInner(SessionData data) {
        PhotonIMDatabase.getInstance().clearMessage(data.getChatType(), data.getChatWith());
        return null;
    }

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
        ArrayList<PhotonIMSession> sessionList = PhotonIMDatabase.getInstance().findSessionList(0, 50, false);
        if (CollectionUtils.isEmpty(sessionList)) {
            return null;
        }
        ArrayList<SessionData> result = new ArrayList<>(sessionList.size());
        SessionData sessionData;
        ArrayList<SessionData> sticker = new ArrayList<>();
        String tempContent;
        boolean showAtMsg;
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
                    tempContent = "[自定义消息]"+photonIMSession.lastMsgContent;
                    break;
                default:
                    tempContent = "[未知消息]";
            }
            boolean isAtMeMsg = false;
            String loginUserId = "";
            ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
            if (businessListener != null) {
                loginUserId = businessListener.getUserId();
            }
            if (!photonIMSession.lastMsgFr.equals(loginUserId)
                    && photonIMSession.chatType == PhotonIMMessage.GROUP) {
                isAtMeMsg = isAtMeMsg(photonIMSession);
                Profile profile = DBHelperUtils.getInstance().findProfile(photonIMSession.lastMsgFr);
                updateFromInfo = profile == null;
                lastMsgFrName = profile == null ? photonIMSession.lastMsgFr : profile.getName();
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
            result.addAll(sticker);
        }
        return result;
    }

    private static boolean isAtMeMsg(PhotonIMSession photonIMSession) {
        return photonIMSession.atType == PhotonIMMessage.SESSION_AT_ME || photonIMSession.atType == PhotonIMMessage.SESSION_AT_ALL;
//        return true;
    }

    private static boolean showAtTip(PhotonIMSession photonIMSession) {
        return photonIMSession.unreadCount != 0;
    }

    private Object getRequestJson() {
        ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
        if (businessListener == null) {
            return null;
        }
        JsonContactRecent recentUser = businessListener.getRecentUser();
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
//                        .sticky(list.getIsTop() == 0) //不考虑置顶
                        .chatWith(list.getType() == PhotonIMMessage.SINGLE ? list.getUserId() : list.getId())
                        .chatType(list.getType())
                        .build();
                msgDataTemp.setExtra(list.getNickname(), list.getAvatar());
                photonIMSessions.add(msgDataTemp.convertToPhotonIMSession());
                sessionData.add(msgDataTemp);
            }
            PhotonIMDatabase.getInstance().saveSessionBatch(photonIMSessions);
            return sessionData;
        }
        return null;
    }

}
