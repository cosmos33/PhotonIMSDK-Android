package com.cosmos.photonim.imbase.chat.searchhistory;

import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchHistoryModel;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchPresenter;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchView;

public class SearchPresenter extends ISearchPresenter<ISearchView, ISearchHistoryModel> {

    public SearchPresenter(ISearchView iView) {
        super(iView);
    }

    @Override
    public ISearchHistoryModel generateIModel() {
        return new SearchModel();
    }

    @Override
    public void search(String content) {
        getiModel().search(content);
    }

    @Override
    public void cancel() {
        getiModel().cancel();
    }
}
