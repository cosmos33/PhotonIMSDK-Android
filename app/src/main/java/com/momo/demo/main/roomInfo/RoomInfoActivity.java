package com.momo.demo.main.roomInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.main.groupmember.GroupMemberActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;
import com.momo.demo.main.roomInfo.adapter.RoomMemberAdapter;
import com.momo.demo.main.roomInfo.igroupinfo.IRoomInfoPresenter;
import com.momo.demo.main.roomInfo.igroupinfo.IRoomInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomInfoActivity extends IRoomInfoView {
    private static final String EXTRA_GID = "EXTRA_GID";
    private static final String EXTRA_RNAME = "EXTRA_RNAME";
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R2.id.tvMemberCount)
    TextView tvMemberCount;

    private String gid;
    private String roomName;
    private List<GroupMembersData> groupInfoDataList;
    private RoomMemberAdapter groupInfoMemberAdapter;

    public static void startActivity(Activity activity, String gid, String rName) {
        Intent intent = new Intent(activity, RoomInfoActivity.class);
        intent.putExtra(EXTRA_GID, gid);
        intent.putExtra(EXTRA_RNAME, rName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roominfo);
        gid = getIntent().getStringExtra(EXTRA_GID);
        roomName = getIntent().getStringExtra(EXTRA_RNAME);

        initView();
        getMembers();
    }

    private void getMembers() {
        presenter.getGroupMembers(gid);
    }

    private void initView() {
        titleBar.setTitle("聊天室设置");
        tvGroupName.setText(roomName);
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> RoomInfoActivity.this.finish());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getMembers();
        });
    }

    @Override
    public IRoomInfoPresenter getIPresenter() {
        return new RoomInfoPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (groupInfoMemberAdapter == null) {
            groupInfoDataList = new ArrayList<>();
            groupInfoMemberAdapter = new RoomMemberAdapter(groupInfoDataList);
        }
        return groupInfoMemberAdapter;
    }

    @Override
    public void onGetGroupMemberResult(List<GroupMembersData> result) {
        if (CollectionUtils.isEmpty(result)) {
            tvMemberCount.setText("0人");
            ToastUtils.showText("获取群成员失败");
        } else {
            tvMemberCount.setText(String.format("%d人", result.size()));
            groupInfoDataList.clear();
            groupInfoDataList.addAll(result);
            groupInfoMemberAdapter.notifyDataSetChanged();
        }
        updateSwipeLayoutStatus();
    }

    @OnClick(R2.id.flMemberRoot)
    public void onflMemberRootClick() {
        GroupMemberActivity.start(this, gid);
    }

    private void updateSwipeLayoutStatus() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
