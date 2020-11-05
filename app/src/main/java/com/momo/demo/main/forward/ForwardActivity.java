package com.momo.demo.main.forward;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.event.SessionTestEvent;
import com.momo.demo.main.forward.iforward.IForwardPresenter;
import com.momo.demo.main.forward.iforward.IForwardView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ForwardActivity extends IForwardView {
    private static final String EXTRA_CHATDATA = "EXTRA_CHATDATA";
    private static final String EXTRA_FOR_SESSIONTEST = "EXTRA_FOR_SESSIONTEST";
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.tvSelectCount)
    TextView tvSelectCount;
    @BindView(R2.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R2.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R2.id.llNoMsg)
    LinearLayout llNoMsg;

    private ForwardAdapter forwardAdapter;
    private List<ForwardData> onlineUserData;
    private Map<String, ForwardData> selectedData;
    private ChatData chatData;
    private boolean forSessionTest;

    public static void startActivity(Activity activity, ChatData chatData) {
        Intent intent = new Intent(activity, ForwardActivity.class);
        intent.putExtra(EXTRA_CHATDATA, chatData);
        activity.startActivity(intent);
    }

    public static void startForSessionTestActivity(Activity activity) {
        Intent intent = new Intent(activity, ForwardActivity.class);
        intent.putExtra(EXTRA_FOR_SESSIONTEST, true);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatData = getIntent().getParcelableExtra(EXTRA_CHATDATA);
        setContentView(R.layout.activity_forward);
        forSessionTest = getIntent().getBooleanExtra(EXTRA_FOR_SESSIONTEST, false);
        presenter.loadContacts();
        initView();
    }

    private void initView() {
        tvSelectCount.setText(getString(R.string.forward_selectcount, 0));
        titleBar.setTitle("选择联系人");
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> ForwardActivity.this.finish());
        refreshLayout.setOnRefreshListener(() -> {
            tvSelectCount.setText(getString(R.string.forward_selectcount, 0));
            selectedData.clear();
            presenter.loadContacts();
        });
    }

    @OnClick(R2.id.tvConfirm)
    public void onConfirmClick() {
        if (selectedData.size() <= 0) {
            ToastUtils.showText("请至少选择一个人");
            return;
        }
        if (forSessionTest) {
            Iterator<String> keyIterator = selectedData.keySet().iterator();
            SessionTestEvent sessionTestEvent = new SessionTestEvent();
            while (keyIterator.hasNext()) {
                String next = keyIterator.next();
                ForwardData forwardData = selectedData.get(next);
                sessionTestEvent.addTest(forwardData.getChatType(), forwardData.getUserId());
            }
            EventBus.getDefault().post(sessionTestEvent);
        } else {
            presenter.sendMsgToMulti(chatData, selectedData);
        }
        this.finish();
    }

    @Override
    public void loadContacts(List<ForwardData> dataList) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        onlineUserData.clear();
        onlineUserData.addAll(dataList);
        forwardAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateOtherInfo(JsonResult result, ForwardData forwardData) {
        if (result.success()) {
            JsonOtherInfoMulti jsonOtherInfo = (JsonOtherInfoMulti) result.get();
            if (jsonOtherInfo.getData().getLists().size() <= 0) {
                return;
            }
            JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonOtherInfo.getData().getLists().get(0);
            forwardData.setNickName(listsBean.getNickname());
            forwardData.setIcon(listsBean.getAvatar());
        }
        forwardAdapter.notifyItemChanged(forwardData.getPosition());
    }

    @Override
    public void showContactsEmptyView() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public IForwardPresenter getIPresenter() {
        return new ForwardPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (forwardAdapter == null) {
            selectedData = new HashMap<>();
            onlineUserData = new ArrayList<>();
            forwardAdapter = new ForwardAdapter(onlineUserData, chatData != null ? chatData.getChatWith() : "", new ForwardItem.UpdateOtherInfoListener() {
                @Override
                public void onUpdateOtherInfo(ForwardData forwardData) {
                    presenter.getOthersInfo(forwardData.getUserId(), forwardData);
                }
            });
            forwardAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    ForwardData userData = (ForwardData) data;
                    userData.setSelected(!userData.isSelected());
                    if (((CheckBox) view).isChecked()) {
                        LogUtils.log("转发选择联系人：" + userData.getUserId());
                        selectedData.put(userData.getUserId(), userData);
                    } else {
                        selectedData.remove(userData.getUserId());
                    }
                    tvSelectCount.setText(getString(R.string.forward_selectcount, selectedData.size()));
                    forwardAdapter.notifyItemChanged(userData.getPosition());
                }
            });
        }
        return forwardAdapter;
    }
}
