package com.cosmos.photonim.imbase.chat.chatset;

import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
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
        TaskExecutor.getInstance().createAsycTask(() -> ImBaseBridge.getInstance().getIgnoreStatus(userId), result -> {
            if (onGetIgnoreStatusListener != null) {
                onGetIgnoreStatusListener.onGetIgnoreStatus((JsonResult) result);
            }
        });
    }

    private Object changeIgnoreStatusInner(int chatType, String remoteId, boolean igoreAlert, OnChangeStatusListener listener) {
        JsonResult jsonResult = ImBaseBridge.getInstance().setIgnoreStatus(remoteId, igoreAlert);
        if (jsonResult.success()) {
            PhotonIMDatabase.getInstance().updateSessionIgnoreAlert(chatType, remoteId, igoreAlert);
        }

        return jsonResult;
    }
}
