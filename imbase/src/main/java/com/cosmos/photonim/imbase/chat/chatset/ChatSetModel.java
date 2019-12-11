package com.cosmos.photonim.imbase.chat.chatset;

import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photonim.imbase.LoginInfo;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

public class ChatSetModel extends IChatSetModel {
    @Override
    public void changeTopStatus(OnChangeStatusListener listener) {

    }

    @Override
    public void changeIgnoreStatus(int chatType, String remoteId, boolean open, OnChangeStatusListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> changeIgnoreStatusInner(chatType, remoteId, open, listener), result -> {
            if (listener != null) {
                listener.onChangeIgnoreStatus((JsonResult) result);
            }
        });
    }

    @Override
    public void getIgnoreStatus(String userId, OnGetIgnoreStatusListener onGetIgnoreStatusListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().getIgnoreStatus(LoginInfo.getInstance().getSessenId()
                , LoginInfo.getInstance().getUserId(), userId), result -> {
            if (onGetIgnoreStatusListener != null) {
                onGetIgnoreStatusListener.onGetIgnoreStatus((JsonResult) result);
            }
        });
    }

    private Object changeIgnoreStatusInner(int chatType, String remoteId, boolean igoreAlert, OnChangeStatusListener listener) {
        JsonResult jsonResult = HttpUtils.getInstance().setIgnoreStatus(remoteId, igoreAlert,
                LoginInfo.getInstance().getSessenId(), LoginInfo.getInstance().getUserId());
        if (jsonResult.success()) {
            PhotonIMDatabase.getInstance().updateSessionIgnoreAlert(chatType, remoteId, igoreAlert);
        }

        return jsonResult;
    }
}
