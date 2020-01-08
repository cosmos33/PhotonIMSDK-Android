package com.cosmos.photonim.imbase.businessmodel;

import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.session.SessionData;
import com.cosmos.photonim.imbase.session.SessionUpdateOtherInfoImpl;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersionInfoModel {
    private Set<String> othersInfoSet;

    public void getOtherInfo(SessionData sessionData, SessionUpdateOtherInfoImpl.OnGetOtherInfoResultListener onGetOtherInfoListener) {
        if (othersInfoSet == null) {
            othersInfoSet = new HashSet<>();
        }
        if (othersInfoSet.contains(sessionData.getChatWith())) {
            return;
        }
        othersInfoSet.add(sessionData.getChatWith());
        TaskExecutor.getInstance().createAsycTask(() -> getOtherInfoInner(sessionData)
                , result -> {
                    othersInfoSet.remove(sessionData.getChatWith());
                    if (onGetOtherInfoListener != null) {
                        onGetOtherInfoListener.onGetOtherInfoResult((JsonResult) result);
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
        JsonResult othersInfo = ImBaseBridge.getInstance().getGroupProfile(otherId);
        if (othersInfo.success()) {
            JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) othersInfo.get();
            JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
            Map<String, String> extra = sessionData.getExtra(profile.getName(), profile.getAvatar());
            PhotonIMDatabase.getInstance().updateSessionExtra(sessionData.getChatType(), sessionData.getChatWith(), extra);
        }
        return othersInfo;
    }

    private Object getUserinfo(String otherId, SessionData sessionData, boolean saveSessionExtra) {
        JsonResult othersInfo = ImBaseBridge.getInstance().getOthersInfo(new String[]{otherId});
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
}
