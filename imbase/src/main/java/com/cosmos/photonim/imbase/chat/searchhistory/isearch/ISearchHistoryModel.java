package com.cosmos.photonim.imbase.chat.searchhistory.isearch;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SearchData;

import java.util.ArrayList;

public abstract class ISearchHistoryModel implements IModel {
    public abstract void search(String content, int chatType, String chatWith, OnSearchResultCallback onSearchResultCallback);

    public abstract void cancel();

    public interface OnSearchResultCallback {
        void onSearchResultCallBack(ArrayList<SearchData> searchData);
    }
}
