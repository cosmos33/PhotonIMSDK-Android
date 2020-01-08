package com.momo.demo.main.contacts.group;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.R;
import com.momo.demo.main.contacts.group.igroup.IGroupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupActivity extends IGroupView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llNoMsg)
    LinearLayout llNoMsg;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.titleBar)
    TitleBar titleBar;

    private GroupAdapter adapter;
    private List<GroupData> groupData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_group);
        initView();
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
    }

    private void initView() {
        contactPresenter.loadGroups();
        titleBar.setTitle("附近的群组");
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> GroupActivity.this.finish());
        refreshLayout.setOnRefreshListener(() -> contactPresenter.loadGroups());
    }

    @Override
    public void loadNearbyGroupResult(List<GroupData> onlineUserData) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        groupData.clear();
        groupData.addAll(onlineUserData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showNearbbyGroupEmptyView() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onJoinGroupResult(GroupData groupData, boolean result) {
        if (result) {
            ImBaseBridge.getInstance().addJoindGId(groupData.getGroupId());
            groupData.setInGroup(true);
            adapter.notifyDataSetChanged();
            ToastUtils.showText(this, "加入成功");
            enterGroup(groupData);
        } else {
            ToastUtils.showText(this, "加入失败");
        }
    }

    private void enterGroup(GroupData gData) {
        ChatBaseActivity.startActivity(this, PhotonIMMessage.GROUP, gData.getGroupId(),
                ImBaseBridge.getInstance().getMyIcon(), gData.getName(), null, false);
    }

    @Override
    public IPresenter getIPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (adapter == null) {
            groupData = new ArrayList<>();
            adapter = new GroupAdapter(groupData);
            adapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    switch (view.getId()) {
                        case R.id.tvJoin:
                            contactPresenter.joinGroup((GroupData) data);
                            break;
                        case R.id.item_contact_llRoot:
                            GroupData gData = (GroupData) data;
                            if (gData.isInGroup()) {
                                enterGroup(gData);
                            } else {
                                ToastUtils.showText("请先加入");
                            }
                            break;
                    }
                }

                @Override
                public void onLongClick(View view, Object data, int position) {

                }
            });
        }
        return adapter;
    }
}
