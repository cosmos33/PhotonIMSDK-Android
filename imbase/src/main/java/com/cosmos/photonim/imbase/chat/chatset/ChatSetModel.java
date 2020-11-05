package com.cosmos.photonim.imbase.chat.chatset;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.messagebody.PhotonIMCustomBody;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.SharedPrefUtil;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

public class ChatSetModel extends IChatSetModel {
    @Override
    public void changeTopStatus(int chatType, String id, boolean top, OnChangeStatusListener listener) {
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
    public void changeIgnoreStatus(int chatType, String remoteId, boolean open, OnChangeStatusListener listener) {
        TaskExecutor.getInstance().createAsycTask(() -> changeIgnoreStatusInner(chatType, remoteId, open, listener), result -> {
            if (listener != null) {
                listener.onChangeIgnoreStatus((JsonResult) result);
            }
        });
    }

    @Override
    public void getIgnoreStatus(String userId, OnChangeStatusListener onGetIgnoreStatusListener) {
        TaskExecutor.getInstance().createAsycTask(() -> ImBaseBridge.getInstance().getIgnoreStatus(userId), result -> {
            if (onGetIgnoreStatusListener != null) {
                onGetIgnoreStatusListener.onGetIgnoreStatus((JsonResult) result);
            }
        });
    }

    @Override
    public void getTopStatus(int chatType, String id, OnChangeStatusListener chatSetPresenter) {
        TaskExecutor.getInstance().createAsycTask(() -> PhotonIMDatabase.getInstance().isSessionSticky(chatType, id), result -> {
            if (chatSetPresenter != null) {
                chatSetPresenter.onGetTopStatus((boolean) result);
            }
        });
    }

    @Override
    public void focusUserStatus(boolean focus, String useId, String chatWith, onChannelMsgListener onChannelMsgListener) {
        PhotonIMCustomBody photonIMCustomBody = new PhotonIMCustomBody();
        photonIMCustomBody.arg1 = focus?3:4;
        photonIMCustomBody.arg2 = 4;
        photonIMCustomBody.data = "focus".getBytes();
        photonIMCustomBody.size = photonIMCustomBody.data.length;
        PhotonIMClient.getInstance().sendChannelMessage(ImBaseBridge.getInstance().getUserId(),chatWith,true,true,5*1000,photonIMCustomBody,(ec,em,retTime)->{
            if(ec == 0){
                if(photonIMCustomBody.arg1 == 3){
                    SharedPrefUtil.saveFocusStatus(useId,chatWith);
                }else if(photonIMCustomBody.arg1 == 4){
                    SharedPrefUtil.removeFocusStatus(useId,chatWith);
                }
            }
            onChannelMsgListener.onChannelMsg(ec,em);
        });
    }

    @Override
    public boolean getFocusUserStatus(int chatType,String userId,String chatWith){
        return SharedPrefUtil.getFocusStatus(userId,chatWith);
    }


    @Override
    public void setPushAndSaveStatus(int chatType,String userId,String chatWith,boolean save,boolean push,boolean history){
        LogUtils.log("PIM","setPushAndSaveStatus save:"+save+" push:"+push+" history:"+history);
        if(save){
            SharedPrefUtil.removeChatStatusSave(userId,chatWith);
        }else {
            SharedPrefUtil.saveChatStausSave(userId,chatWith);
        }

        if(push){
            SharedPrefUtil.removeChatStatusPush(userId,chatWith);
        }else {
            SharedPrefUtil.saveChatStausPush(userId,chatWith);
        }

        if(history){
            SharedPrefUtil.removeChatStatusHistory(userId,chatWith);
        }else {
            SharedPrefUtil.saveChatStausHistory(userId,chatWith);
        }

    }

    @Override
    public boolean getSaveStatus(int chatType,String userId,String chatWith){
        return !SharedPrefUtil.getChatStausSave(userId,chatWith);
    }

    @Override
    public boolean getPushStatus(int chatType,String userId,String chatWith){
        return !SharedPrefUtil.getChatStausPush(userId,chatWith);
    }
    @Override
    public boolean getHistoryStatus(int chatType,String userId,String chatWith){
        return !SharedPrefUtil.getChatStausHistory(userId,chatWith);
    }

    @Override
    public void setSendTimeoutStatus(boolean sendTimeout,int chatType,String userId,String chatWith){
        if(sendTimeout){
            SharedPrefUtil.saveSendTimeoutStatus(userId,chatWith);
        }else{
            SharedPrefUtil.removeSendTimeoutStatus(userId,chatWith);
        }
    }
    @Override
    public boolean getSendTimeoutStatus(int chatType,String userId,String chatWith){
        return SharedPrefUtil.getSendTimeoutStatus(userId,chatWith);
    }

    @Override
    public void setIncreaseUnreadStatus(boolean unreadStatus, int chatType, String userId, String chatWith) {
        if(unreadStatus){
            SharedPrefUtil.removeIncreaseUnreadStatus(userId,chatWith);
        }else{
            SharedPrefUtil.saveIncreaseUnreadStatus(userId,chatWith);
        }
    }

    @Override
    public boolean getIncreaseUnreadStatus(int chatType, String userId, String chatWith) {
        return !SharedPrefUtil.getIncreaseUnreadStatus(userId,chatWith);
    }

    private Object changeIgnoreStatusInner(int chatType, String remoteId, boolean igoreAlert, OnChangeStatusListener listener) {
        JsonResult jsonResult = ImBaseBridge.getInstance().setIgnoreStatus(remoteId, igoreAlert);
        if (jsonResult.success()) {
            PhotonIMDatabase.getInstance().updateSessionIgnoreAlert(chatType, remoteId, igoreAlert);
        }

        return jsonResult;
    }
}
