package com.momo.demo.main.groupinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Switch;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.searchhistory.SearchHistoryActivity;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGetGroupIgnoreInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.main.groupinfo.adapter.GroupInfoMemberAdapter;
import com.momo.demo.main.groupinfo.igroupinfo.IGroupInfoView;
import com.momo.demo.main.groupmember.GroupMemberActivity;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupInfoActivity extends IGroupInfoView {
    private static final String EXTRA_GID = "EXTRA_GID";
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R2.id.tvNotic)
    TextView tvNotic;
    @BindView(R2.id.tvMemberCount)
    TextView tvMemberCount;
    @BindView(R2.id.sIgnore)
    Switch sIgnore;

    private String gid;
    private List<GroupMembersData> groupInfoDataList;
    private GroupInfoMemberAdapter groupInfoMemberAdapter;
    private boolean getGroupInfo;
    private boolean getGroupMemberInfo;
    private boolean getGroupStatusInfo;

    public static void startActivity(Activity activity, String gid) {
        Intent intent = new Intent(activity, GroupInfoActivity.class);
        intent.putExtra(EXTRA_GID, gid);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupinfo);
        gid = getIntent().getStringExtra(EXTRA_GID);

        initView();
        getGroupInfo();
    }

    private void getGroupInfo() {
        presenter.getGroupInfo(gid);
        presenter.getGroupMembers(gid);
        presenter.getGroupIgnoreStatus(gid);
    }

    private void initView() {
        titleBar.setTitle("消息设置");
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> GroupInfoActivity.this.finish());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getGroupInfo = false;
            getGroupMemberInfo = false;
            getGroupStatusInfo = false;
            getGroupInfo();
        });
    }

    @Override
    public IPresenter getIPresenter() {
        return new GroupInfoPresenter(this);
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
            groupInfoMemberAdapter = new GroupInfoMemberAdapter(groupInfoDataList);
        }
        return groupInfoMemberAdapter;
    }

    @Override
    public void onGetGroupInfoResult(JsonResult jsonResult) {
        getGroupInfo = true;
        if (jsonResult.success()) {
            JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) jsonResult.get();
            if (!TextUtils.isEmpty(jsonGroupProfile.getData().getProfile().getName())) {
                tvGroupName.setText(jsonGroupProfile.getData().getProfile().getName());
            }
        } else {
            ToastUtils.showText("获取群信息失败");
        }
        updateSwipeLayoutStatus();
    }

    @Override
    public void onGetGroupMemberResult(List<GroupMembersData> result) {
        getGroupMemberInfo = true;
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

    @Override
    public void onGetGroupIgnoreStatusResult(JsonResult jsonResult) {
        getGroupStatusInfo = true;
        if (jsonResult.success()) {
            JsonGetGroupIgnoreInfo jsonGetGroupIgnoreInfo = (JsonGetGroupIgnoreInfo) jsonResult.get();
            sIgnore.setChecked(jsonGetGroupIgnoreInfo.getData().getSwitchX() == 0);
        } else {
            ToastUtils.showText("获取群消息免打扰状态失败");
            sIgnore.setChecked(!sIgnore.isChecked());
        }
        updateSwipeLayoutStatus();
    }

    @Override
    public void onChangeGroupIgnoreStatusResult(JsonResult jsonResult) {
        if (jsonResult.success()) {
            ToastUtils.showText("设置成功");
        } else {
            ToastUtils.showText("设置失败");
        }
    }

    @OnClick(R2.id.sIgnore)
    public void onIgoreClick() {
        presenter.changeGroupIgnoreStatus(gid, sIgnore.isChecked());
    }

    @OnClick(R2.id.flMemberRoot)
    public void onflMemberRootClick() {
        GroupMemberActivity.start(this, gid);
    }

    @OnClick(R2.id.flSearchContent)
    public void onSearchContentClick() {
        SearchHistoryActivity.start(this, gid, PhotonIMMessage.GROUP);
    }

    @OnClick(R2.id.flClearChatContent)
    public void onClearChatContent() {
        presenter.clearChatContent();
    }

    private void updateSwipeLayoutStatus() {
        if (getGroupInfo && getGroupMemberInfo && getGroupStatusInfo) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
