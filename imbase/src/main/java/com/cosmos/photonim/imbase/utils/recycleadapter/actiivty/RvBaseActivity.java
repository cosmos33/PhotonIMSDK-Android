package com.cosmos.photonim.imbase.utils.recycleadapter.actiivty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.recycleadapter.CreateRvHelper;
import com.cosmos.photonim.imbase.utils.recycleadapter.ICreateRv;


/**
 * Created by fanqiang on 2019/4/17.
 */
public abstract class RvBaseActivity extends BaseActivity implements ICreateRv {
    private CreateRvHelper createRvHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        createRvHelper = new CreateRvHelper.Builder(this).build();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
    }
}
