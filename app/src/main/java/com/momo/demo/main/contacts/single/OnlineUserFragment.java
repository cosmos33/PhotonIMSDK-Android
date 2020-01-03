package com.momo.demo.main.contacts.single;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListener;
import com.momo.demo.R;
import com.momo.demo.main.contacts.single.ionline.IOnlineUserView;
import com.momo.demo.main.contacts.single.userinfo.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlineUserFragment extends IOnlineUserView implements RvListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llNoMsg)
    LinearLayout llNoMsg;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private OnlineUserAdapter adapter;
    private List<OnlineUserData> baseDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onlineusers, null);
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.recyclerView);
        initView();
        return view;
    }

    private void initView() {
        refreshLayout.setOnRefreshListener(() -> contactPresenter.loadContacts());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && contactPresenter != null) {
            contactPresenter.loadContacts();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 2019-08-05 修改加载时机
        contactPresenter.loadContacts();
    }

    @Override
    public void loadContacts(List<OnlineUserData> onlineUserData) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        baseDataList.clear();
        baseDataList.addAll(onlineUserData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showContactsEmptyView() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @OnClick(R.id.llNoMsg)
    public void onOnNoMsgClick() {
        contactPresenter.loadContacts();
    }

    @Override
    public IPresenter getIPresenter() {
        return new OnlineUserPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (adapter == null) {
            baseDataList = new ArrayList<>();
            adapter = new OnlineUserAdapter(baseDataList);
            adapter.setRvListener(this);
        }
        return adapter;
    }

    @Override
    public void onClick(View view, Object data, int position) {
        OnlineUserData onlineUserData = (OnlineUserData) data;
        UserInfoActivity.startActivity(getActivity(), onlineUserData.getUserId(), onlineUserData.getIcon(), onlineUserData.getNickName());
    }

    @Override
    public void onLongClick(View view, Object data, int position) {

    }

}
