package com.cosmos.photonim.imbase.chat.searchhistory.isearch;

import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

public abstract class ISearchHistoryModel implements IModel {
    public abstract void search(String content);

    public abstract void cancel();

    public interface OnChangeStatusListener {
        void onChangeTopStatus(JsonSaveIgnoreInfo result);
    }
}
