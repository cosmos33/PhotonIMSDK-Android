package com.cosmos.photonim.imbase.chat.searchhistory;

import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SearchData;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchHistoryModel;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchPresenter;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchView;

import java.util.ArrayList;

public class SearchPresenter extends ISearchPresenter<ISearchView, ISearchHistoryModel> {

    public SearchPresenter(ISearchView iView) {
        super(iView);
    }

    @Override
    public ISearchHistoryModel generateIModel() {
        return new SearchModel();
    }

    @Override
    public void search(String content, int chatType, String chatWith) {
        getiModel().search(content, chatType, chatWith, new ISearchHistoryModel.OnSearchResultCallback() {

            @Override
            public void onSearchResultCallBack(ArrayList<SearchData> searchData) {
                getIView().onSearchResult(searchData);
            }
        });
    }

    @Override
    public void cancel() {
        getiModel().cancel();
    }
}
