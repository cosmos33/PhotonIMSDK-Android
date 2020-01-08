package com.cosmos.photonim.imbase.chat.searchhistory.isearch;

import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SearchData;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;

import java.util.ArrayList;

public abstract class ISearchView extends RvBaseActivity<ISearchPresenter> {
    public abstract void onSearchResult(ArrayList<SearchData> searchData);
}
