package com.cosmos.photonim.imbase.chat.chatset.ichatset;

import com.cosmos.photonim.imbase.base.mvpbase.IBaseActivityView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

public abstract class IChatSetView extends IBaseActivityView<IChatSetPresenter> {
    public abstract void onTopChangeStatusResult(boolean success);

    public abstract void onIgnoreChangeStatusResult(boolean success);

    public abstract void onGetIgnoreStatus(JsonResult result);
}
