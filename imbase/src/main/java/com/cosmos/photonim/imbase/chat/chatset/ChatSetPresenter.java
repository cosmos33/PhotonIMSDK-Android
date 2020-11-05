package com.cosmos.photonim.imbase.chat.chatset;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetModel;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetPresenter;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetView;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.ToastUtils;
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
    public void setPushAndSaveStatus(int chatType,String userId,String chatWith,boolean save,boolean push,boolean history){
        getiModel().setPushAndSaveStatus(chatType, userId,chatWith,save,push,history);
    }

    @Override
    public boolean getSaveStatus(int chatType,String userId,String chatWith){
        return getiModel().getSaveStatus(chatType,userId, chatWith);
    }

    @Override
    public boolean getPushStatus(int chatType,String userId,String chatWith){
        return getiModel().getPushStatus(chatType,userId,chatWith);

    }
    @Override
    public boolean getHistoryStatus(int chatType,String userId,String chatWith){
        return getiModel().getHistoryStatus(chatType,userId,chatWith);
    }

    @Override
    public boolean getFocusStatus(int chatType, String userId, String chatWith) {
        return getiModel().getFocusUserStatus(chatType,userId,chatWith);
    }

    @Override
    public void setSendTimeoutStatus(boolean sendTimeout, int chatType, String userId, String chatWith) {
        getiModel().setSendTimeoutStatus(sendTimeout,chatType,userId,chatWith);
    }

    @Override
    public boolean getSendTimeoutStatus(int chatType, String userId, String chatWith) {
        return getiModel().getSendTimeoutStatus(chatType,userId,chatWith);
    }

    @Override
    public void setIncreaseUnreadStatus(boolean increaseUnread, int chatType, String userId, String chatWith) {
        getiModel().setIncreaseUnreadStatus(increaseUnread,chatType,userId,chatWith);
    }

    @Override
    public boolean getIncreaseUnreadStatus(int chatType, String userId, String chatWith) {
        return getiModel().getIncreaseUnreadStatus(chatType,userId,chatWith);
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

    @Override
    public void focusUserStatus(boolean focus,String userId,String chatWith){
        getiModel().focusUserStatus(focus,userId,chatWith,((ec, em) -> {
            if(ec == -1){
                ToastUtils.showText(focus?"关注失败，超时":"取消关注失败，超时");
            }else if(ec == 0){
                ToastUtils.showText(focus?"关注成功":"取消关注成功");
            }else{
                ToastUtils.showText(em);
            }
        }));
    }
}
