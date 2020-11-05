package com.momo.demo.main.groupinfo;

import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoModel;

public class GroupInfoModel extends IGroupInfoModel {
    @Override
    public void changeTopStatus(int chatType, String id, boolean top, IChatSetModel.OnChangeStatusListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> {
            PhotonIMDatabase.getInstance().updateSessionSticky(chatType, id, top);
            return null;
        }, result -> {
            if (listener != null) {
                listener.onChangeTopStatus();
            }
        });
    }

    @Override
    public void getTopStatus(int chatType, String id, IChatSetModel.OnChangeStatusListener chatSetPresenter) {
        TaskExecutor.getInstance().createAsycTask(() -> PhotonIMDatabase.getInstance().isSessionSticky(chatType, id), result -> {
            if (chatSetPresenter != null) {
                chatSetPresenter.onGetTopStatus((boolean) result);
            }
        });
    }

    @Override
    public void getGroupInfo(String sessionId, String userId, String gid, OnGetGroupInfoListener onGetGroupInfoListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().getGroupProfile(sessionId, userId, gid), result -> {
            if (onGetGroupInfoListener != null) {
                onGetGroupInfoListener.onGetGroupInfoResult((JsonResult) result);
            }
        });
    }

    @Override
    public void getGroupIgnoreStatus(String sessionId, String userId, String gid, IChatSetModel.OnChangeStatusListener onGetGroupIgnoreStatusListener) {
        TaskExecutor.getInstance().createAsycTask(() -> HttpUtils.getInstance().getGroupIgnoreStatus(sessionId, userId, gid), result -> {
            if (onGetGroupIgnoreStatusListener != null) {
                onGetGroupIgnoreStatusListener.onGetIgnoreStatus((JsonResult) result);
            }
        });
    }

    @Override
    public void changeGroupIgnoreStatus(String sessionId, String userId, String gid, int switchX, IChatSetModel.OnChangeStatusListener onChangeGroupIgnoreStatusListener) {
        TaskExecutor.getInstance().createAsycTask(() -> changeGroupIgnoreStatusInner(sessionId, userId, gid, switchX), result -> {
            if (onChangeGroupIgnoreStatusListener != null) {
                onChangeGroupIgnoreStatusListener.onChangeIgnoreStatus((JsonResult) result);
            }
        });
    }

    private Object changeGroupIgnoreStatusInner(String sessionId, String userId, String gid, int switchX) {
        JsonResult jsonResult = HttpUtils.getInstance().setGroupIgnoreStatus(sessionId, userId, gid, switchX);
        if (jsonResult.success()) {
            PhotonIMDatabase.getInstance().updateSessionIgnoreAlert(PhotonIMMessage.GROUP, gid, switchX == 0);
        }
        return jsonResult;
    }
}
