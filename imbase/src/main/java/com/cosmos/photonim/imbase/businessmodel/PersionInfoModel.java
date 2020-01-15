package com.cosmos.photonim.imbase.businessmodel;

import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.session.SessionUpdateOtherInfoImpl;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersionInfoModel {
    private Set<String> othersInfoSet;

    public void getOtherInfo(int chatType,
                             String chatWith,
                             SessionUpdateOtherInfoImpl.OnGetOtherInfoResultListener onGetOtherInfoListener) {
        getOtherInfo(chatType, chatWith, null, false, null, onGetOtherInfoListener);
    }

    public void getOtherInfo(int chatType,
                             String chatWith,
                             String nickName,
                             boolean updateFromInfo,
                             String lastMsgFrom,
                             SessionUpdateOtherInfoImpl.OnGetOtherInfoResultListener onGetOtherInfoListener) {

        if (othersInfoSet == null) {
            othersInfoSet = new HashSet<>();
        }
        if (othersInfoSet.contains(chatWith)) {
            return;
        }
        othersInfoSet.add(chatWith);
        TaskExecutor.getInstance().createAsycTask(() -> getOtherInfoInner(chatType, chatWith, nickName, updateFromInfo, lastMsgFrom)
                , result -> {
                    othersInfoSet.remove(chatWith);
                    if (onGetOtherInfoListener != null) {
                        onGetOtherInfoListener.onGetOtherInfoResult((JsonResult) result);
                    }
                });
    }

    private Object getOtherInfoInner(int chatType, String chatWith, String nickName, boolean updateFromInfo, String lastMsgFrom) {
        if (chatType == PhotonIMMessage.GROUP) {
            if (TextUtils.isEmpty(nickName)) {
                return getGroupInfo(chatWith);
            } else if (updateFromInfo) {
                return getUserinfo(lastMsgFrom);
            }
            return null;
        } else {
            return getUserinfo(chatWith);
        }
    }

    private Object getGroupInfo(String otherId) {
        JsonResult othersInfo = ImBaseBridge.getInstance().getGroupProfile(otherId);
        if (othersInfo.success()) {
            JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) othersInfo.get();
            JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
            DBHelperUtils.getInstance().saveProfile(profile.getGid(),
                    profile.getAvatar(), profile.getName());
        }
        return othersInfo;
    }

    private Object getUserinfo(String otherId) {
        JsonResult othersInfo = ImBaseBridge.getInstance().getOthersInfo(new String[]{otherId});
        if (othersInfo.success()) {
            if (((JsonOtherInfoMulti) othersInfo.get()).getData().getLists().size() > 0) {
                List<JsonOtherInfoMulti.DataBean.ListsBean> lists = ((JsonOtherInfoMulti) othersInfo.get()).getData().getLists();
                DBHelperUtils.getInstance().saveProfile(lists.get(0).getUserId(),
                        lists.get(0).getAvatar(), lists.get(0).getNickname());

            }
        }
//        if (saveSessionExtra && othersInfo.success()) {
//            // TODO: 2019-08-09 对服务器返回的数据进行校验
//            JsonOtherInfoMulti jsonOtherInfo = (JsonOtherInfoMulti) othersInfo.get();
//            if (jsonOtherInfo.getData().getLists().size() <= 0) {
//                return othersInfo;
//            }
//            JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonOtherInfo.getData().getLists().get(0);
//            Map<String, String> extra = sessionData.getExtra(listsBean.getNickname(), listsBean.getAvatar());
//            PhotonIMDatabase.getInstance().updateSessionExtra(sessionData.getChatType(), sessionData.getChatWith(), extra);
//
//        }
        return othersInfo;
    }
}
