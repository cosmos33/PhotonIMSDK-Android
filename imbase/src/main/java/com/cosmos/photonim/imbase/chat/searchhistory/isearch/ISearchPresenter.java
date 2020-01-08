package com.cosmos.photonim.imbase.chat.searchhistory.isearch;


import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

public abstract class ISearchPresenter<V extends ISearchView, M extends ISearchHistoryModel> extends IPresenter<V, M> {

    public ISearchPresenter(V iView) {
        super(iView);
    }

    public abstract void search(String content);

    abstract public void cancel();

    @Override
    public V getEmptyView() {
        return (V) new ISearchView() {
            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }

}
