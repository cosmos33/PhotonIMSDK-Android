package com.cosmos.photonim.imbase.session;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.businessmodel.PersionInfoModel;
import com.cosmos.photonim.imbase.session.adapter.SessionData;
import com.cosmos.photonim.imbase.session.adapter.SessionItem;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRequestResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public class SessionUpdateOtherInfoImpl implements SessionItem.UpdateOtherInfoListener {
    private static final String TAG = "SessionUpdateOtherInfoImpl";
    private PersionInfoModel persionInfoModel;
    private OnSessionUpdateCallback onSessionUpdateCallback;

    public SessionUpdateOtherInfoImpl(OnSessionUpdateCallback onSessionUpdateCallback) {
        this.onSessionUpdateCallback = onSessionUpdateCallback;
    }

    @Override
    public void onUpdateOtherInfo(SessionData sessionData) {
        if (persionInfoModel == null) {
            persionInfoModel = new PersionInfoModel();
        }
        int chatType = sessionData.getChatType();
        String chatWith = sessionData.getChatWith();
        String nickName = sessionData.getNickName();
        boolean updateFromInfo = sessionData.isUpdateFromInfo();
        String lastMsgFrom = sessionData.getLastMsgFr();
        persionInfoModel.getOtherInfo(chatType, chatWith, nickName, updateFromInfo, lastMsgFrom, result -> {
            if (result == null || !result.success()) {
                LogUtils.log(TAG, "获取Session item info failed");
                return;
            }
            if (sessionData.getChatType() == PhotonIMMessage.SINGLE) {
                JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
                if (CollectionUtils.isEmpty(jsonRequestResult.getData().getLists())) {
                    return;
                }
                JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonRequestResult.getData().getLists().get(0);

                sessionData.setIcon(listsBean.getAvatar());
                sessionData.setNickName(listsBean.getNickname());
            } else {
                JsonRequestResult jr = result.get();
                if (jr instanceof JsonOtherInfoMulti) {
                    JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
                    if (jsonRequestResult.getData().getLists().size() == 0) {
                        return;
                    }
                    sessionData.setLastMsgFrName(jsonRequestResult.getData().getLists().get(0).getNickname());
                    sessionData.setUpdateFromInfo(false);
                } else {
                    JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) result.get();
                    JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
                    sessionData.setUpdateFromInfo(false);
                    sessionData.setIcon(profile.getAvatar());
                    sessionData.setNickName(profile.getName());
                }
            }
            if (onSessionUpdateCallback != null) {
                onSessionUpdateCallback.onSessionUpdate(sessionData);
            }
        });
    }


    public interface OnGetOtherInfoResultListener {
        void onGetOtherInfoResult(JsonResult result);
    }

    public interface OnSessionUpdateCallback {
        void onSessionUpdate(SessionData sessionData);
    }

}
