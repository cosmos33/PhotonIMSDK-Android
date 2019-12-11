package com.cosmos.photonim.imbase.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.utils.recycleadapter.CreateRvHelper;
import com.cosmos.photonim.imbase.utils.recycleadapter.ICreateRv;


public abstract class RvBaseFragment extends BaseFragment implements ICreateRv {
    private CreateRvHelper createRvHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initRv() {
        createRvHelper = new CreateRvHelper.Builder(this).build();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
    }
}
