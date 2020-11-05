package com.cosmos.photonim.imbase.chat.searchhistory;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.businessmodel.PersionInfoModel;
import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SearchData;
import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SessionSearchItem;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public class SeachUpdateOtherInfoImpl implements SessionSearchItem.UpdateOtherInfoListener {
    private static final String TAG = "SessionUpdateOtherInfoImpl";
    private PersionInfoModel persionInfoModel;
    private OnSearchUpdateCallback onSessionUpdateCallback;

    public SeachUpdateOtherInfoImpl(OnSearchUpdateCallback onSessionUpdateCallback) {
        this.onSessionUpdateCallback = onSessionUpdateCallback;
    }

    @Override
    public void onUpdateOtherInfo(SearchData sessionData) {
        if (persionInfoModel == null) {
            persionInfoModel = new PersionInfoModel();
        }
        int chatType = PhotonIMMessage.SINGLE;
        String chatWith = sessionData.chatWith;
        persionInfoModel.getOtherInfo(chatType, chatWith, result -> {
            if (result == null || !result.success()) {
                LogUtils.log(TAG, "获取Session item info failed");
                return;
            }
            JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
            if (CollectionUtils.isEmpty(jsonRequestResult.getData().getLists())) {
                return;
            }
            JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonRequestResult.getData().getLists().get(0);

            sessionData.icon = (listsBean.getAvatar());
            sessionData.nickName = (listsBean.getNickname());
            if (onSessionUpdateCallback != null) {
                onSessionUpdateCallback.onSearchUpdate(sessionData);
            }
        });
    }


    public interface OnGetOtherInfoResultListener {
        void onGetOtherInfoResult(JsonResult result);
    }

    public interface OnSearchUpdateCallback {
        void onSearchUpdate(SearchData sessionData);
    }

}
