package com.cosmos.photonim.imbase.chat.searchhistory.isearch;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;

public abstract class ISearchHistoryModel implements IModel {
    public abstract void search(String content);

    public abstract void cancel();

    public interface OnChangeStatusListener {
        void onChangeTopStatus(JsonSaveIgnoreInfo result);
    }
}
