package com.cosmos.photonim.imbase.session;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.session.adapter.SessionAdapter;
import com.cosmos.photonim.imbase.session.adapter.SessionData;
import com.cosmos.photonim.imbase.session.isession.ISessionView;
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

    //    private Map<String, SessionData> baseDataMap;//key 为chatWith
    private SessionAdapter sessionAdapter;
    private ProcessDialogFragment processDialogFragment;
    private ListDialogFragment sessionDialogFragment;
    private boolean isVisibleToUser;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        presenter.loadHistoryData();
        presenter.getAllUnReadCount();
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
//            baseDataMap = new HashMap<>();
            sessionAdapter = new SessionAdapter(presenter.initData(), new SessionUpdateOtherInfoImpl(new SessionUpdateOtherInfoImpl.OnSessionUpdateCallback() {
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
                    SessionData sessionData = (SessionData) data;
                    final boolean isTopStatus = sessionData.isSticky();
                    List<String> itemContent;
                    itemContent = new ArrayList<>();
                    itemContent.add("删除会话");
                    if (!isTopStatus) {
                        itemContent.add("会话置顶");
                    } else {
                        itemContent.add("取消置顶");
                    }
                    if (sessionData.getUnreadCount() == 0) {
                        itemContent.add("标为未读");
                    } else {
                        itemContent.add("标为已读");
                    }
                    sessionDialogFragment = ListDialogFragment.getInstance(new ListDialogFragment.OnHandleListener() {
                        @Override
                        public void onItemClick(int positon) {
                            switch (positon) {
                                case 0:
                                    presenter.deleteSession((SessionData) data);
                                    break;
                                case 1:
//                                    presenter.clearSession((SessionData) data);
                                    presenter.changeSessionTopStatus(sessionData.getChatType(), sessionData.getChatWith(), isTopStatus);
                                    break;
                                case 2:
                                    presenter.setSessionUnRead((SessionData) data);
                                    dismissSessionDialog();
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

    @Override
    public IPresenter getIPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public void notifyItemInserted(int position) {
        sessionAdapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemChanged(int position) {
        sessionAdapter.notifyItemChanged(position);
    }

    @Override
    public void notifyDataSetChanged() {
        sessionAdapter.notifyDataSetChanged();
    }

    @Override
    public void setNoMsgViewVisibility(boolean visibility) {
        if (visibility) {
            recyclerView.setVisibility(View.GONE);
            llNoMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llNoMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismissSessionDialog() {
        if (sessionDialogFragment != null) {
            sessionDialogFragment.dismiss();
        }
    }
}
