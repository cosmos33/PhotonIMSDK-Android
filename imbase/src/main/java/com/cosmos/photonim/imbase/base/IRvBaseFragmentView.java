package com.cosmos.photonim.imbase.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosmos.photonim.imbase.base.mvpbase.IBaseFragmentView;
import com.cosmos.photonim.imbase.utils.recycleadapter.CreateRvHelper;
import com.cosmos.photonim.imbase.utils.recycleadapter.ICreateRv;


public abstract class IRvBaseFragmentView<P> extends IBaseFragmentView<P> implements ICreateRv {
    private CreateRvHelper createRvHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initRv();
        return view;
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