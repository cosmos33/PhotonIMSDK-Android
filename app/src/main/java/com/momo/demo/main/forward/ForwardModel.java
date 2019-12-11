package com.momo.demo.main.forward;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.session.SessionData;
import com.cosmos.photonim.imbase.session.SessionModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.single.OnlineUserData;
import com.momo.demo.main.contacts.single.OnlineUserModel;

import java.util.ArrayList;
import java.util.List;

public class ForwardModel implements IModel {
    public void loadContacts(int itemType, OnLoadForwardDataListener onLoadContactListener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
                    List<OnlineUserData> onlineUserData = (List<OnlineUserData>) OnlineUserModel.getOnLineUsers(itemType);
                    ArrayList<SessionData> sessionData = (ArrayList<SessionData>) SessionModel.getLocalHistoryMsg();
                    return convertToOnLine(onlineUserData, sessionData);
                },
                result -> {
                    if (onLoadContactListener != null) {
                        onLoadContactListener.onLoadForwardData((List<ForwardData>) result);
                    }
                });
    }

    private Object convertToOnLine(List<OnlineUserData> onlineUserData, ArrayList<SessionData> sessionData) {
        if (CollectionUtils.isEmpty(onlineUserData) && CollectionUtils.isEmpty(sessionData)) {
            return null;
        }
        List<ForwardData> forwardData = new ArrayList<>();
        ForwardData data;
        boolean first = true;
        if (onlineUserData != null) {
            for (OnlineUserData onlineUserDatum : onlineUserData) {
                data = new ForwardData();
                data.showTitle(first);
                first = false;
                data.setIcon(onlineUserDatum.getIcon());
                data.setNickName(onlineUserDatum.getNickName());
                data.setUserId(onlineUserDatum.getUserId());
                data.setType(ForwardData.ONLINE);
                data.setChatType(PhotonIMMessage.SINGLE);
                forwardData.add(data);
            }
        }
        first = true;
        if (sessionData != null) {
            for (SessionData sessionDatum : sessionData) {
                data = new ForwardData();
                data.showTitle(first);
                first = false;
                data.setIcon(sessionDatum.getIcon());
                data.setNickName(sessionDatum.getNickName());
                data.setUserId(sessionDatum.getChatWith());
                data.setType(ForwardData.SESSION);
                data.setChatType(sessionDatum.getChatType());
                forwardData.add(data);
            }
        }
        return forwardData;
    }

    public void getOtherInfo(String otherId, ForwardData forwardData, OnGetOtherInfoListener onGetOtherInfoListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getOtherInfoInner(otherId),
                result -> {
                    if (onGetOtherInfoListener != null) {
                        onGetOtherInfoListener.onGetOtherInfo((JsonResult) result, forwardData);
                    }
                });
    }

    private JsonResult getOtherInfoInner(String otherId) {
        return (JsonResult) HttpUtils.getInstance().getOthersInfo(new String[]{otherId}, com.momo.demo.login.LoginInfo.getInstance().getSessionId(),
                LoginInfo.getInstance().getUserId());
    }

    public interface OnLoadForwardDataListener {
        void onLoadForwardData(List<ForwardData> result);
    }

    public interface OnGetOtherInfoListener {
        void onGetOtherInfo(JsonResult jsonResult, ForwardData forwardData);
    }


}
