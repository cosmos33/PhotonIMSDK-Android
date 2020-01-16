package com.cosmos.photonim.imbase.chat.chatset.ichatset;

import com.cosmos.photonim.imbase.base.mvp.IBaseActivityView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IChatSetView extends IBaseActivityView<IChatSetPresenter> {
    public abstract void onIgnoreChangeStatusResult(boolean success);

    public abstract void onGetIgnoreStatus(JsonResult result);

    public abstract void dimissProgressDialog();

    public abstract void changeTopStatus(boolean top);
}
