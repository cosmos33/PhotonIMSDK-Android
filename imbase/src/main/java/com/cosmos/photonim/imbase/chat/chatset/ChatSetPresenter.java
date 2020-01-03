package com.cosmos.photonim.imbase.chat.chatset;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetPresenter;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;

public class ChatSetPresenter extends IChatSetPresenter<IChatSetView, IChatSetModel> implements ChatSetModel.OnChangeStatusListener {
    public ChatSetPresenter(IChatSetView iView) {
        super(iView);
    }

    @Override
    public IChatSetModel generateIModel() {
        return new ChatSetModel();
    }

    @Override
    public void changeTopStatus() {
        getiModel().changeTopStatus(this);
    }

    @Override
    public void changeIgnoreStatus(String remoteId, boolean open) {
        getiModel().changeIgnoreStatus(PhotonIMMessage.SINGLE, remoteId, open, this);
    }

    @Override
    public void getIgnoreStatus(String remoteId) {
        getiModel().getIgnoreStatus(remoteId, result -> getIView().onGetIgnoreStatus(result));
    }

    @Override
    public void onChangeTopStatus(JsonSaveIgnoreInfo result) {
        getIView().onTopChangeStatusResult(result != null && result.success());
    }

    @Override
    public void onChangeIgnoreStatus(JsonResult jsonResult) {
        getIView().onIgnoreChangeStatusResult(jsonResult.success());
    }
}
