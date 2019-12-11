package com.cosmos.photonim.imbase.session;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.session.isession.ISessionView;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.LocalRestoreUtils;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.event.AllUnReadCount;
import com.cosmos.photonim.imbase.utils.event.ClearUnReadStatus;
import com.cosmos.photonim.imbase.utils.event.OnDBChanged;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRequestResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.cosmos.photonim.imbase.view.SessionDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionFragment extends ISessionView implements SessionItem.UpdateOtherInfoListener {
    private static final String TAG = "SessionFragment";
    RecyclerView recyclerView;
    @BindView(R2.id.llNoMsg)
    LinearLayout llNoMsg;

    private List<SessionData> baseDataList;
    //    private Map<String, SessionData> baseDataMap;//key 为chatWith
    private SessionAdapter sessionAdapter;
    private ProcessDialogFragment processDialogFragment;
    private SessionDialogFragment sessionDialogFragment;
    private boolean isVisibleToUser;
    private boolean isFirstLoad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_message, null);
        ButterKnife.bind(this, view);
        isFirstLoad = LocalRestoreUtils.getFirstLoadSession();
        recyclerView = view.findViewById(R.id.recyclerView);
        iSessionPresenter.loadHistoryData();
        iSessionPresenter.getAllUnReadCount();
        // 删除demo层的重发机制，防止和sdk内部重发逻辑混淆
//        iSessionPresenter.resendSendingStatusMsgs();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
//        if (isVisibleToUser && iSessionPresenter != null) {
//            iSessionPresenter.loadHistoryData(LoginInfo.getInstance().getSessenId(), LoginInfo.getInstance().getUserId());
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
//            iSessionPresenter.saveSession(sessionData);
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
//                iSessionPresenter.getSessionUnRead(onDBChanged.chatType,onDBChanged.chatWith);
                iSessionPresenter.loadHistoryData();
                break;
        }
        iSessionPresenter.getAllUnReadCount();
    }

    @Subscribe
    public void onClearUnReadStatus(ClearUnReadStatus event) {
//        int itemPosition = getItemPosition(event.chatWith);
//        if (itemPosition < 0) {
//            LogUtils.log(TAG, "未找到要更新的item");
//            return;
//        }
//        iSessionPresenter.updateUnRead(event.chatWith);
//        baseDataList.get(itemPosition).setUnreadCount(0);
//        sessionAdapter.notifyItemChanged(itemPosition);

        iSessionPresenter.clearSesionUnReadCount(event.chatType, event.chatWith);
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
            sessionAdapter = new SessionAdapter(baseDataList, this);
            sessionAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    SessionData sessionData = (SessionData) data;
                    if (sessionData.isShowAtTip()) {
                        iSessionPresenter.updateSessionAtType(sessionData);
                    }
                    ChatBaseActivity.startActivity(SessionFragment.this.getActivity(), sessionData.getChatType(),
                            sessionData.getChatWith(), null, sessionData.getNickName(), sessionData.getIcon(), false);
                }

                @Override
                public void onLongClick(View view, Object data, int position) {
                    if (sessionDialogFragment != null) {
                        sessionDialogFragment.dismiss();
                    }
                    sessionDialogFragment = SessionDialogFragment.getInstance(new SessionDialogFragment.OnHandleListener() {
                        @Override
                        public void onDelete() {
                            iSessionPresenter.deleteSession((SessionData) data);
                        }

                        @Override
                        public void onClearContent() {
                            iSessionPresenter.clearSession((SessionData) data);
                        }
                    });
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
                iSessionPresenter.loadHistoryFromRemote();
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
    public void onGetOtherInfoResult(JsonResult result, SessionData sessionData) {
        if ( result == null || !result.success()) {
            LogUtils.log(TAG, "获取Session item info failed");
            return;
        }
        if (sessionData.getChatType() == PhotonIMMessage.SINGLE) {
            JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
            if (CollectionUtils.isEmpty(jsonRequestResult.getData().getLists())) {
                return;
            }
            JsonOtherInfoMulti.DataBean.ListsBean listsBean = jsonRequestResult.getData().getLists().get(0);

            sessionData.setIcon(listsBean.getAvatar());
            sessionData.setNickName(listsBean.getNickname());
        } else {
            JsonRequestResult jr = result.get();
            if (jr instanceof JsonOtherInfoMulti) {
                JsonOtherInfoMulti jsonRequestResult = (JsonOtherInfoMulti) result.get();
                if (jsonRequestResult.getData().getLists().size() == 0) {
                    return;
                }
                sessionData.setLastMsgFrName(jsonRequestResult.getData().getLists().get(0).getNickname());
                sessionData.setUpdateFromInfo(false);
            } else {
                JsonGroupProfile jsonGroupProfile = (JsonGroupProfile) result.get();
                JsonGroupProfile.DataBean.ProfileBean profile = jsonGroupProfile.getData().getProfile();
                sessionData.setIcon(profile.getAvatar());
                sessionData.setNickName(profile.getName());
            }
        }

        sessionAdapter.notifyItemChanged(sessionData.getItemPosition());
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

    @Override
    public void onUpdateOtherInfo(SessionData sessionData) {
        iSessionPresenter.getOthersInfo(sessionData);
    }
}
