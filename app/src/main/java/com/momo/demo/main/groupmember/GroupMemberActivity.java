package com.momo.demo.main.groupmember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListener;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.R;
import com.momo.demo.main.groupmemberselected.GroupMemberSelectPresenter;
import com.momo.demo.main.groupmemberselected.GroupMembersData;
import com.momo.demo.main.groupmemberselected.apater.GroupMemberAdapter;
import com.momo.demo.main.groupmemberselected.igroupmember.IGroupMemeberView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupMemberActivity extends IGroupMemeberView {
    private static final String EXTRA_GID = "EXTRA_GID";
    private static final int ACTIVITY_REQUEST_CODE = 123;
    private static final String TAG = "GroupMemberSelectActivity";
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llNoMsg)
    LinearLayout llNoMsg;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private String gId;
    private List<GroupMembersData> groupMemberData;
    private GroupMemberAdapter groupMemberAdapter;

    static public void start(Activity activity, String gId) {
        Intent intent = new Intent(activity, GroupMemberActivity.class);
        intent.putExtra(EXTRA_GID, gId);
        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmember);
        initView();
    }

    private void initView() {
        gId = getIntent().getStringExtra(EXTRA_GID);
        titleBar.setLeftImageEvent(R.drawable.arrow_left, view -> GroupMemberActivity.this.finish());
        titleBar.setTitle("组成员");

        if (!TextUtils.isEmpty(gId)) {
            iGroupPresenter.getGroupMembers(gId, true, false);
        } else {
            LogUtils.log(TAG, "gid is null");
        }
        refreshLayout.setOnRefreshListener(() -> {
            iGroupPresenter.getGroupMembers(gId, true, false);
        });
    }

    @Override
    public void onGetGroupMembersResult(List<GroupMembersData> result) {
        titleBar.setTitle(String.format("组成员（%d人）", result.size()));
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        groupMemberData.clear();
        groupMemberData.addAll(result);
        groupMemberAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMembersEmptyView() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public IPresenter getIPresenter() {
        return new GroupMemberSelectPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (groupMemberAdapter == null) {
            groupMemberData = new ArrayList<>();
            groupMemberAdapter = new GroupMemberAdapter(groupMemberData);
            groupMemberAdapter.setRvListener(new RvListener() {
                @Override
                public void onClick(View view, Object data, int position) {
                    switch (view.getId()) {
                    }
                }

                @Override
                public void onLongClick(View view, Object data, int position) {

                }
            });
        }
        return groupMemberAdapter;
    }
}
