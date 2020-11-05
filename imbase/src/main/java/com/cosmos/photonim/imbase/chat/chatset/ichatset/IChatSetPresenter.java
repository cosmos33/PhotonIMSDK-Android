package com.cosmos.photonim.imbase.chat.chatset.ichatset;


import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IChatSetPresenter<V extends IChatSetView, M extends IChatSetModel> extends IPresenter<V, M> {

    public IChatSetPresenter(V iView) {
        super(iView);
    }

    public abstract void changeTopStatus(int chatType, String id);

    public abstract void getTopStatus(int chatType, String id);

    public abstract void changeIgnoreStatus(String remoteId, boolean open);

    public abstract void getIgnoreStatus(String remoteId);

    public abstract void clearChatContent(int chatType, String chatWith);

    public abstract void focusUserStatus(boolean focus,String userId,String chatWith);

    public abstract void setPushAndSaveStatus(int chatType,String userId,String chatWith,boolean save,boolean push,boolean history);

    public abstract boolean getSaveStatus(int chatType,String userId,String chatWith);

    public abstract boolean getPushStatus(int chatType,String userId,String chatWith);

    public abstract boolean getHistoryStatus(int chatType,String userId,String chatWith);

    public abstract boolean getFocusStatus(int chatType,String userId,String chatWith);

    public abstract void setSendTimeoutStatus(boolean sendTimeout,int chatType,String userId,String chatWith);

    public abstract boolean getSendTimeoutStatus(int chatType,String userId,String chatWith);

    public abstract void setIncreaseUnreadStatus(boolean increaseUnread,int chatType,String userId,String chatWith);

    public abstract boolean getIncreaseUnreadStatus(int chatType,String userId,String chatWith);

    @Override
    public V getEmptyView() {
        return (V) new IChatSetView() {

            @Override
            public void onIgnoreChangeStatusResult(boolean success) {

            }

            @Override
            public void onGetIgnoreStatus(JsonResult result) {

            }

            @Override
            public void dimissProgressDialog() {

            }

            @Override
            public void changeTopStatus(boolean top) {

            }

            @Override
            public IChatSetPresenter getIPresenter() {
                return null;
            }
        };
    }

}
