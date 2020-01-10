package com.momo.demo.main.sessiontest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.event.OnDBChanged;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRequestResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.utils.test.TestSendBean;
import com.cosmos.photonim.imbase.utils.test.TestSendManager;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.cosmos.photonim.imbase.view.listdialog.ListDialogFragment;
import com.momo.demo.event.SessionTestEvent;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.sessiontest.isessiontest.ISessionTestView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SessionTestViewFragment extends ISessionTestView implements SessionTestItem.UpdateOtherInfoListener {
    private static final String TAG = "SessionTestViewFragment";
    RecyclerView recyclerView;
    @BindView(R2.id.llNoMsg)
    LinearLayout llNoMsg;
    @BindView(R2.id.tvAuthSuccess)
    TextView tvAuthSuccess;
    @BindView(R2.id.tvAuthFailed)
    TextView tvAuthFailed;
    @BindView(R2.id.tvLog)
    TextView tvLog;

    private List<SessionTestData> baseDataList;
    private SessionTestAdapter sessionTestAdapter;
    private ProcessDialogFragment processDialogFragment;
    private ListDialogFragment sessionDialogFragment;
    private boolean isVisibleToUser;
    private List<String> listItem;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_message_test;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        presenter.loadHistoryData(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
        initTestTool();
    }

    @OnClick(R2.id.tvClear)
    public void onAuthClearClick() {
        TestSendManager.getInstance().clearAuth();
        updateAuthStatus(0, 0);
    }

    public void updateAuthStatus(int authSuccess, int authFailed) {
        tvAuthSuccess.setText("" + authSuccess);
        tvAuthFailed.setText("" + authFailed);
    }

    private void initTestTool() {
        listItem = new ArrayList<>();
        listItem.add("删除");
        TestSendManager.getInstance().addAuthResultListener(new TestSendManager.OnAuthResultListener() {
            @Override
            public void onAuthResult(int success, int authFailed) {
                updateAuthStatus(success, authFailed);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
//        if (isVisibleToUser && presenterTest != null) {
//            presenterTest.loadHistoryData(LoginInfo.getInstance().getSessenId(), LoginInfo.getInstance().getUserId());
//        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOnDBChanged(OnDBChanged onDBChanged) {
//        if (!isVisibleToUser) {
//            return;
//        }
        switch (onDBChanged.event) {
            case 0://新增
                presenter.loadHistoryData(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
                break;
            case 1://修改
            case 2://删除
                if (CollectionUtils.isEmpty(baseDataList)) {
                    return;
                }
                // TODO: 2019-12-02 maybe use map
                for (SessionTestData sessionTestData : baseDataList) {
                    if (onDBChanged.chatWith.equals(sessionTestData.getChatWith()) && onDBChanged.chatType == sessionTestData.getChatType()) {
                        presenter.loadHistoryData(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
                        return;
                    }
                }
                break;
        }
    }

    private int getItemPosition(String chatWith) {
        int size = baseDataList.size();
        for (int i = 0; i < size; i++) {
            if (chatWith.equals(baseDataList.get(i).getChatWith())) {
                return i;
            }

        }
        return -1;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (sessionTestAdapter == null) {
            baseDataList = new ArrayList<>();
//            baseDataMap = new HashMap<>();
            sessionTestAdapter = new SessionTestAdapter(baseDataList, this);
            sessionTestAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    SessionTestData sessionTestData = (SessionTestData) data;
                    if (view.getId() == R.id.tvStartTest) {
                        onStartTestClick(sessionTestData);
                        return;
                    } else if (view.getId() == R.id.tvItemClear) {
                        onClearItemClick(sessionTestData);
                        return;
                    }
                    if (sessionTestData.isShowAtTip()) {
                        presenter.updateSessionAtType(sessionTestData);
                    }
                    ChatBaseActivity.startActivity(SessionTestViewFragment.this.getActivity(), sessionTestData.getChatType(),
                            sessionTestData.getChatWith(), null, sessionTestData.getNickName(), sessionTestData.getIcon(), false, true);
                }

                @Override
                public void onLongClick(View view, Object data, int position) {
                    sessionDialogFragment = ListDialogFragment.getInstance(new ListDialogFragment.OnHandleListener() {
                        @Override
                        public void onItemClick(int p) {
                            presenter.deleteSession(baseDataList.get(position));
                            onDeleteSession(baseDataList.get(position));
                        }
                    }, listItem);
                    sessionDialogFragment.show(getFragmentManager(), "sessionTestdialog");
                }
            });
        }
        return sessionTestAdapter;
    }

    private void onClearItemClick(SessionTestData sessionTestData) {
        TestSendBean testSendBean = sessionTestData.getTestSendBean();
        if (testSendBean != null) {
            if (testSendBean.getStatus() == TestSendBean.STATUS_SENDING) {
                ToastUtils.showText("请先暂停！！！");
                return;
            }
        }
        sessionTestData.clear();
    }

    private void onStartTestClick(SessionTestData sessionTestData) {
        TestSendBean testSendBean = sessionTestData.getTestSendBean();
//        if (testSendBean == null) {
//            testSendBean = TestSendBean.getDefaultBean(sessionTestData.getChatType(),sessionTestData.getChatWith());
//        }
        if (testSendBean.getStatus() == TestSendBean.STATUS_SENDING) {
            TestSendManager.getInstance().stopTest(testSendBean);
        } else {
            TestSendManager.getInstance().startTest(testSendBean);
        }
        onChangeSendStatus(testSendBean);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeSendStatus(TestSendBean testSendBean) {
        int size = baseDataList.size();
        SessionTestData sessionTestData;
        for (int i = 0; i < size; i++) {
            sessionTestData = baseDataList.get(i);
            if (sessionTestData.getChatType() == testSendBean.getChatType()
                    && sessionTestData.getChatWith().equals(testSendBean.getChatWith())) {
                TestSendBean sendBean = sessionTestData.getTestSendBean();
                if (sendBean == null) {
                    sessionTestData.setTestSendBean(testSendBean);
                } else {
                    sendBean.setStatus(testSendBean.getStatus());
                }
                sessionTestAdapter.notifyItemChanged(i);
                return;
            }
        }

    }

//    @Override
//    public void showDialog() {
//        processDialogFragment = ProcessDialogFragment.getInstance();
//        processDialogFragment.show(getFragmentManager(), "");
//    }
//
//    @Override
//    public void hideDialog() {
//        if (processDialogFragment != null) {
//            processDialogFragment.dismiss();
//            processDialogFragment = null;
//        }
//    }

    @Override
    public IPresenter getIPresenter() {
        return new SessionTestPresenter(this);
    }

    @Override
    public void onLoadHistory(List<SessionTestData> sessionDatumTests) {
        if (CollectionUtils.isEmpty(sessionDatumTests)) {
            recyclerView.setVisibility(View.GONE);
            llNoMsg.setVisibility(View.VISIBLE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        llNoMsg.setVisibility(View.GONE);
        baseDataList.clear();
//        baseDataMap.clear();
        if (sessionDatumTests != null) {
            baseDataList.addAll(sessionDatumTests);
            HashMap<String, TestSendBean> testMap = TestSendManager.getInstance().getTestMap();
            if (testMap != null && baseDataList.size() != 0) {
                for (SessionTestData sessionTestData : baseDataList) {
                    TestSendBean testSendBean = testMap.get(sessionTestData.getChatWith());
                    if (testSendBean == null) {
                        testSendBean = TestSendBean.getDefaultBean(sessionTestData.getChatType(), sessionTestData.getChatWith());
                        TestSendManager.getInstance().addTestBean(testSendBean);
                    }
                    sessionTestData.setTestSendBean(testSendBean);
                }
            } else if (testMap == null) {
                if (baseDataList.size() != 0) {
                    for (SessionTestData sessionTestData : baseDataList) {
                        TestSendBean defaultBean = TestSendBean.getDefaultBean(sessionTestData.getChatType(), sessionTestData.getChatWith());
                        sessionTestData.setTestSendBean(defaultBean);
                        TestSendManager.getInstance().addTestBean(defaultBean);
                    }
                }
            }
        }
        sessionTestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetOtherInfoResult(JsonResult result, SessionTestData sessionTestData) {
        if (result == null || !result.success()) {
            LogUtils.log(TAG, "获取Session item info failed");
            return;
        }
        if (sessionTestData.getChatType() == PhotonIMMessage.SINGLE) {
            JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
            if (CollectionUtils.isEmpty(jsonRequestResult.getData().getLists())) {
                return;
            }
            JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonRequestResult.getData().getLists().get(0);

            sessionTestData.setIcon(listsBean.getAvatar());
            sessionTestData.setNickName(listsBean.getNickname());
        } else {
            JsonRequestResult jr = result.get();
            if (jr instanceof JsonOtherInfoMulti) {
                JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
                if (jsonRequestResult.getData().getLists().size() == 0) {
                    return;
                }
                sessionTestData.setLastMsgFrName(jsonRequestResult.getData().getLists().get(0).getNickname());
                sessionTestData.setUpdateFromInfo(false);
            } else {
                JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) result.get();
                JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
                sessionTestData.setIcon(profile.getAvatar());
                sessionTestData.setNickName(profile.getName());
            }
        }

        sessionTestAdapter.notifyItemChanged(sessionTestData.getItemPosition());
    }

    @Override
    public void onDeleteSession(SessionTestData data) {
        if (sessionDialogFragment != null) {
            sessionDialogFragment.dismiss();
        }
        baseDataList.remove(data);
        sessionTestAdapter.notifyDataSetChanged();
        if (baseDataList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            llNoMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llNoMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClearSession(SessionTestData data) {
        if (sessionDialogFragment != null) {
            sessionDialogFragment.dismiss();
        }
    }

    @Override
    public void onNewSession(SessionTestData sessionTestData) {
        // TODO: 2019-08-12 置顶
        baseDataList.add(0, sessionTestData);
        sessionTestAdapter.notifyItemInserted(0);
    }

    @Override
    public void onAddSessionResult() {
        OnDBChanged onDBChanged = new OnDBChanged(0, 0, "");
        onOnDBChanged(onDBChanged);
    }

    @Override
    public void onUpdateOtherInfo(SessionTestData sessionTestData) {
        presenter.getOthersInfo(sessionTestData);
    }

    @OnClick(R2.id.tvAddTest)
    public void onAddTestClick() {
        ImBaseBridge.getInstance().onAddTestClick(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddTestEvent(SessionTestEvent event) {
        presenter.addSessioTest(event);
    }

    @OnClick(R2.id.tvLog)
    public void onLogClick(){ImBaseBridge.getInstance().upLoadMMfileLog();}

}
