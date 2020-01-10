package com.momo.demo.main.groupmemberselected;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.ChatGroupActivity;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.R;
import com.momo.demo.main.groupmemberselected.apater.GroupMemberAdapter;
import com.momo.demo.main.groupmemberselected.igroupmember.IGroupMemeberView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberSelectActivity extends IGroupMemeberView {
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
    @BindView(R.id.tvSelectCount)
    TextView tvSelectCount;

    private String gId;
    private Map<String, GroupMembersData> selectedData;
    private List<GroupMembersData> groupMemberData;
    private GroupMemberAdapter groupMemberAdapter;

    static public void start(Activity activity, String gId) {
        Intent intent = new Intent(activity, GroupMemberSelectActivity.class);
        intent.putExtra(EXTRA_GID, gId);
        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmember_selected);
        initView();
    }

    private void initView() {
        tvSelectCount.setText(getString(R.string.forward_selectcount, 0));
        gId = getIntent().getStringExtra(EXTRA_GID);
        titleBar.setLeftTextEvent("取消", view -> GroupMemberSelectActivity.this.finish());
        titleBar.setTitle("选择提醒的人");

        if (!TextUtils.isEmpty(gId)) {
            presenter.getGroupMembers(gId);
        } else {
            LogUtils.log(TAG, "gid is null");
        }
        refreshLayout.setOnRefreshListener(() -> {
            tvSelectCount.setText(getString(R.string.forward_selectcount, 0));
            selectedData.clear();
            presenter.getGroupMembers(gId);
        });
    }

    @Override
    public void onGetGroupMembersResult(List<GroupMembersData> result) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        groupMemberData.clear();
        GroupMembersData allMemData = new GroupMembersData(null, String.format("所有人（%d）", result.size()), null);
        allMemData.setItemType(Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED_ALL);
        groupMemberData.add(allMemData);
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

    @OnClick(R.id.tvConfirm)
    public void onConfirmClick() {
        Iterator<String> iterator = selectedData.keySet().iterator();
        ArrayList<String> resultsNames = new ArrayList<>(selectedData.size());
        ArrayList<String> resultsIds = new ArrayList<>(selectedData.size());
        GroupMembersData groupMembersData;
        while (iterator.hasNext()) {
            groupMembersData = selectedData.get(iterator.next());
            resultsNames.add(groupMembersData.getName());
            resultsIds.add(groupMembersData.getId());
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ChatGroupActivity.EXTRA_RESULT_NAME, resultsNames);
        intent.putStringArrayListExtra(ChatGroupActivity.EXTRA_RESULT_ID, resultsIds);
        intent.putExtra(ChatGroupActivity.EXTRA_ALL, resultsNames.size() == groupMemberData.size() - 1);
        setResult(ChatGroupActivity.RESULT_AT, intent);
        this.finish();
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (groupMemberAdapter == null) {
            selectedData = new HashMap<>();
            groupMemberData = new ArrayList<>();
            groupMemberAdapter = new GroupMemberAdapter(groupMemberData);
            groupMemberAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    switch (view.getId()) {
                        case R.id.checkbox:
                            onMemberStatusChanged(view, data);
                            break;
                        case R.id.llRoot:
                            onAllMemberStatusChanged();
                            break;
                    }
                }

                private void onAllMemberStatusChanged() {
                    for (GroupMembersData groupMemberDatum : groupMemberData) {
                        if (groupMemberDatum.getItemType() != Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED) {
                            continue;
                        }
                        groupMemberDatum.setSelected(true);
                        selectedData.put(groupMemberDatum.getId(), groupMemberDatum);
                    }
                    tvSelectCount.setText(getString(R.string.forward_selectcount, selectedData.size()));
                    groupMemberAdapter.notifyDataSetChanged();
                }

                private void onMemberStatusChanged(View view, Object data) {
                    GroupMembersData userData = (GroupMembersData) data;
                    userData.setSelected(!userData.isSelected());
                    if (((CheckBox) view).isChecked()) {
                        LogUtils.log("转发选择联系人：" + userData.getId());
                        selectedData.put(userData.getId(), userData);
                    } else {
                        selectedData.remove(userData.getId());
                    }
                    tvSelectCount.setText(getString(R.string.forward_selectcount, selectedData.size()));
                    groupMemberAdapter.notifyItemChanged(userData.getPosition());
                }

                @Override
                public void onLongClick(View view, Object data, int position) {

                }
            });
        }
        return groupMemberAdapter;
    }
}
