package com.cosmos.photonim.imbase.chat.chatset;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetPresenter;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetView;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.event.ClearChatContent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

import org.greenrobot.eventbus.EventBus;

public class ChatSetPresenter extends IChatSetPresenter<IChatSetView, IChatSetModel> implements ChatSetModel.OnChangeStatusListener {
    private ChatModel chatModel;
    private boolean top;

    public ChatSetPresenter(IChatSetView iView) {
        super(iView);
    }

    @Override
    public IChatSetModel generateIModel() {
        return new ChatSetModel();
    }

    @Override
    public void changeTopStatus(int chatType, String id) {
        getiModel().changeTopStatus(chatType, id, !top, this);
    }

    @Override
    public void getTopStatus(int chatType, String id) {
        getiModel().getTopStatus(chatType, id, this);
    }

    @Override
    public void changeIgnoreStatus(String remoteId, boolean open) {
        getiModel().changeIgnoreStatus(PhotonIMMessage.SINGLE, remoteId, open, this);
    }

    @Override
    public void getIgnoreStatus(String remoteId) {
        getiModel().getIgnoreStatus(remoteId, this);
    }

    @Override
    public void clearChatContent(int chatType, String chatWith) {
        if (chatModel == null) {
            chatModel = new ChatModel();
        }
        chatModel.clearChatContent(chatType, chatWith, new IChatModel.OnClearChatContentListener() {
            @Override
            public void onClearChatContent() {
                EventBus.getDefault().post(new ClearChatContent());
                getIView().dimissProgressDialog();
            }
        });
    }

    @Override
    public void onGetTopStatus(boolean top) {
        this.top = top;
        getIView().changeTopStatus(top);
    }

    @Override
    public void onChangeTopStatus() {
        top = !top;
        getIView().toast("操作成功");
    }

    @Override
    public void onChangeIgnoreStatus(JsonResult jsonResult) {
        getIView().onIgnoreChangeStatusResult(jsonResult.success());
    }

    @Override
    public void onGetIgnoreStatus(JsonResult result) {
        getIView().onGetIgnoreStatus(result);
    }
}
