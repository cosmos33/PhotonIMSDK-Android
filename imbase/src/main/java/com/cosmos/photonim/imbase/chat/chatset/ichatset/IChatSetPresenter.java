package com.cosmos.photonim.imbase.chat.chatset.ichatset;


import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public abstract class IChatSetPresenter<V extends IChatSetView, M extends IChatSetModel> extends IPresenter<V, M> {

    public IChatSetPresenter(V iView) {
        super(iView);
    }

    public abstract void changeTopStatus();

    public abstract void changeIgnoreStatus(String remoteId, boolean open);

    public abstract void getIgnoreStatus(String remoteId);

    @Override
    public V getEmptyView() {
        return (V) new IChatSetView() {
            @Override
            public void onTopChangeStatusResult(boolean success) {

            }

            @Override
            public void onIgnoreChangeStatusResult(boolean success) {

            }

            @Override
            public void onGetIgnoreStatus(JsonResult result) {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }
}
