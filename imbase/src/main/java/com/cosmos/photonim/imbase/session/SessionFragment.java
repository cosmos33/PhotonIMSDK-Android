package com.cosmos.photonim.imbase.session;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.session.isession.ISessionView;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.LocalRestoreUtils;
import com.cosmos.photonim.imbase.utils.event.AllUnReadCount;
import com.cosmos.photonim.imbase.utils.event.ClearUnReadStatus;
import com.cosmos.photonim.imbase.utils.event.OnDBChanged;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.cosmos.photonim.imbase.view.listdialog.ListDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SessionFragment extends ISessionView {
    private static final String TAG = "SessionFragment";
    RecyclerView recyclerView;
    @BindView(R2.id.llNoMsg)
    LinearLayout llNoMsg;

    private List<SessionData> baseDataList;
    //    private Map<String, SessionData> baseDataMap;//key 为chatWith
    private SessionAdapter sessionAdapter;
    private ProcessDialogFragment processDialogFragment;
    private ListDialogFragment sessionDialogFragment;
    private boolean isVisibleToUser;
    private boolean isFirstLoad;
    private List<String> itemContent;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected void initView(View view) {
        isFirstLoad = LocalRestoreUtils.getFirstLoadSession();
        recyclerView = view.findViewById(R.id.recyclerView);
        presenter.loadHistoryData();
        presenter.getAllUnReadCount();
        itemContent = new ArrayList<>();
        itemContent.add("删除会话");
        itemContent.add("清空会话");
        // 删除demo层的重发机制，防止和sdk内部重发逻辑混淆
//        presenter.resendSendingStatusMsgs();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
//        if (isVisibleToUser && presenter != null) {
//            presenter.loadHistoryData(LoginInfo.getInstance().getSessenId(), LoginInfo.getInstance().getUserId());
//        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onReceiveMsg(PhotonIMMessage msg) {
//        if (msg.status == PhotonIMMessage.SENT_READ) {
//            return;
//        }
//        SessionData sessionData = getSessionData(msg.chatWith);
//        String content;
//        switch (msg.messageType) {
//            case PhotonIMMessage.AUDIO:
//                content = "[语音]";
//                break;
//            case PhotonIMMessage.IMAGE:
//                content = "[图片]";
//                break;
//            case PhotonIMMessage.VIDEO:
//                content = "[视频]";
//                break;
//            case PhotonIMMessage.TEXT:
//                content = msg.content;
//                break;
//            default:
//                content = "未知消息";
//        }
//        ChatToastUtils.showText(getContext(), content);
//        if (sessionData == null) {
//            if (llNoMsg.getVisibility() == View.VISIBLE) {
//                llNoMsg.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//            }
//            sessionData = new SessionData(msg);
//            sessionData.setLastMsgContent(content);
//            presenter.saveSession(sessionData);
//            baseDataList.add(sessionData);
////            baseDataMap.put(msg.chatWith, sessionData);
//            sessionAdapter.notifyItemInserted(baseDataList.size() - 1);
//        } else {
//            sessionData.setUnreadCount(sessionData.getUnreadCount() + 1);
//            sessionData.setLastMsgContent(content);
//
//            int itemPosition = getItemPosition(sessionData.getChatWith());
//            if (itemPosition < 0) {
//                LogUtils.log(TAG, "未找到要更新的item");
//                return;
//            }
//            sessionAdapter.notifyItemChanged(itemPosition);
//        }
//    }

    private SessionData getSessionData(String chatWith) {
        for (SessionData sessionData : baseDataList) {
            if (sessionData.getChatWith().equals(chatWith))
                return sessionData;
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOnDBChanged(OnDBChanged onDBChanged) {
//        if (!isVisibleToUser) {
//            return;
//        }
        switch (onDBChanged.event) {
            case 0://新增
            case 1://修改
            case 2://删除
//                presenter.getSessionUnRead(onDBChanged.chatType,onDBChanged.chatWith);
                presenter.loadHistoryData();
                break;
        }
        presenter.getAllUnReadCount();
    }

    @Subscribe
    public void onClearUnReadStatus(ClearUnReadStatus event) {
//        int itemPosition = getItemPosition(event.chatWith);
//        if (itemPosition < 0) {
//            LogUtils.log(TAG, "未找到要更新的item");
//            return;
//        }
//        presenter.updateUnRead(event.chatWith);
//        baseDataList.get(itemPosition).setUnreadCount(0);
//        sessionAdapter.notifyItemChanged(itemPosition);

        presenter.clearSesionUnReadCount(event.chatType, event.chatWith);
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
        if (sessionAdapter == null) {
            baseDataList = new ArrayList<>();
//            baseDataMap = new HashMap<>();
            sessionAdapter = new SessionAdapter(baseDataList, new SessionUpdateOtherInfoImpl(new SessionUpdateOtherInfoImpl.OnSessionUpdateCallback() {
                @Override
                public void onSessionUpdate(SessionData sessionData) {
                    sessionAdapter.notifyItemChanged(sessionData.getItemPosition());
                }
            }));
            sessionAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    SessionData sessionData = (SessionData) data;
                    if (sessionData.isShowAtTip()) {
                        presenter.updateSessionAtType(sessionData);
                    }
                    ChatBaseActivity.startActivity(SessionFragment.this.getActivity(), sessionData.getChatType(),
                            sessionData.getChatWith(), null, sessionData.getNickName(), sessionData.getIcon(), false);
                }

                @Override
                public void onLongClick(View view, Object data, int position) {
                    if (sessionDialogFragment != null) {
                        sessionDialogFragment.dismiss();
                    }
                    sessionDialogFragment = ListDialogFragment.getInstance(new ListDialogFragment.OnHandleListener() {
                        @Override
                        public void onItemClick(int positon) {
                            switch (positon) {
                                case 0:
                                    presenter.deleteSession((SessionData) data);
                                    break;
                                case 1:
                                    presenter.clearSession((SessionData) data);
                                    break;
                            }

                        }

                    }, itemContent);
                    sessionDialogFragment.show(getFragmentManager(), "sessiondialog");
                }
            });
        }
        return sessionAdapter;
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
        return new SessionPresenter(this);
    }

    @Override
    public void onLoadHistory(List<SessionData> sessionData) {
        if (CollectionUtils.isEmpty(sessionData)) {
            if (isFirstLoad) {
                presenter.loadHistoryFromRemote();
                isFirstLoad = false;
            }
            recyclerView.setVisibility(View.GONE);
            llNoMsg.setVisibility(View.VISIBLE);
            return;
        }
        isFirstLoad = false;
        recyclerView.setVisibility(View.VISIBLE);
        llNoMsg.setVisibility(View.GONE);
        baseDataList.clear();
//        baseDataMap.clear();
        if (sessionData != null) {
            baseDataList.addAll(sessionData);
        }
        sessionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadHistoryFromRemote(List<SessionData> sessionData) {
        if (CollectionUtils.isEmpty(sessionData)) {
            llNoMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        llNoMsg.setVisibility(View.GONE);
        baseDataList.clear();
//        baseDataMap.clear();
        baseDataList.addAll(sessionData);
        sessionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteSession(SessionData data) {
        if (sessionDialogFragment != null) {
            sessionDialogFragment.dismiss();
        }
        baseDataList.remove(data);
        sessionAdapter.notifyDataSetChanged();
        if (baseDataList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            llNoMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llNoMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClearSession(SessionData data) {
        if (sessionDialogFragment != null) {
            sessionDialogFragment.dismiss();
        }
    }

    @Override
    public void onNewSession(SessionData sessionData) {
        // TODO: 2019-08-12 置顶
        baseDataList.add(0, sessionData);
        sessionAdapter.notifyItemInserted(0);
    }

    @Override
    public void onGetAllUnReadCount(int result) {
        EventBus.getDefault().post(new AllUnReadCount(result));
    }
}
