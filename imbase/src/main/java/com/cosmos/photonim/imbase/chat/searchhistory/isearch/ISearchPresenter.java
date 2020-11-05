package com.cosmos.photonim.imbase.chat.searchhistory.isearch;


import androidx.recyclerview.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SearchData;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.ArrayList;

public abstract class ISearchPresenter<V extends ISearchView, M extends ISearchHistoryModel> extends IPresenter<V, M> {

    public ISearchPresenter(V iView) {
        super(iView);
    }

    public abstract void search(String content, int chatType, String chatWith);

    abstract public void cancel();

    @Override
    public V getEmptyView() {
        return (V) new ISearchView() {
            @Override
            public void onSearchResult(ArrayList<SearchData> searchData) {

            }

            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }

            @Override
            public ISearchPresenter getIPresenter() {
                return null;
            }
        };
    }

}
