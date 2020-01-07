package com.cosmos.photonim.imbase.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.chat.adapter.chat.ChatAdapter;
import com.cosmos.photonim.imbase.chat.ichat.IChatView;
import com.cosmos.photonim.imbase.chat.image.ImageCheckActivity;
import com.cosmos.photonim.imbase.chat.media.TakePhotoActivity;
import com.cosmos.photonim.imbase.chat.media.TakePhotoResultFragment;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.TimeUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.event.AlertEvent;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.event.ClearUnReadStatus;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.utils.test.TestSendManager;
import com.cosmos.photonim.imbase.view.ChatPopupWindow;
import com.cosmos.photonim.imbase.view.ChatToastUtils;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.cosmos.photonim.imbase.view.TouchRecycleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class ChatBaseActivity extends IChatView {
    private static final int LIMIT_LOADREMOTE = 200;
    protected static final String AT_ALL_CONTENT = "所有人 ";
    private static final int IMAGE_MAX_SIZE = 10 * 1024 * 1024;
    public static final int REQUEST_IMAGE_CODE = 1001;
    private static final String TAG = "ChatActivityTAG";
    private static final String EXTRA_CHATTYPE = "EXTRA_CHATTYPE";
    private static final String EXTRA_CHATWITH = "EXTRA_CHATWITH";
    private static final String EXTRA_MyICON = "EXTRA_MyICON";
    private static final String EXTRA_IGOREALERT = "EXTRA_IGOREALERT";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_OTHERICON = "EXTRA_OTHERICON";
    private static final String EXTRA_SHOWTEST = "EXTRA_SHOWTEST";
    private static final int PAGE_ONE = 50;

    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChatPopupWindow chatPopupWindow;

    private ChatAdapter chatAdapter;
    private List<ChatData> chatMsg;
    private HashMap<String, ChatData> chatMsgMap;//key:msgId

    private int chatType;
    protected String chatWith;
    private boolean firstLoad = true;
    protected String myIcon;
    protected String singleChatUserIcon;
    private String name;
    private int lastPosition;
    private boolean igoreAlert;
    private boolean lastLoadHistoryFromRemote = false;
    private TestSendFragment testSendFragment;
    public ChatExtraFragment extraFragment;
    private boolean showTest;
    private String loginUserId;


    public static void startActivity(Activity from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert) {
        startActivity(from, chatType, chatWith, myIcon, name, otherIcon, igoreAlert, false);
    }

    public static void startActivity(Activity from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert, boolean showTest) {
        Intent intent;
        if (chatType == PhotonIMMessage.SINGLE) {
            intent = new Intent(from, ChatSingleActivity.class);
        } else {
            intent = new Intent(from, ChatGroupActivity.class);
        }
        intent.putExtra(EXTRA_CHATTYPE, chatType);
        intent.putExtra(EXTRA_CHATWITH, chatWith);
        intent.putExtra(EXTRA_MyICON, myIcon);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_OTHERICON, otherIcon);
        intent.putExtra(EXTRA_IGOREALERT, igoreAlert);
        intent.putExtra(EXTRA_SHOWTEST, showTest);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatType = getIntent().getIntExtra(EXTRA_CHATTYPE, 0);
        chatWith = getIntent().getStringExtra(EXTRA_CHATWITH);
        myIcon = ImBaseBridge.getInstance().getMyIcon();
        singleChatUserIcon = getIntent().getStringExtra(EXTRA_OTHERICON);
        name = getIntent().getStringExtra(EXTRA_NAME);
        igoreAlert = getIntent().getBooleanExtra(EXTRA_IGOREALERT, false);
        showTest = getIntent().getBooleanExtra(EXTRA_SHOWTEST, false);

        loginUserId = ImBaseBridge.getInstance().getUserId();

        getHistory(false, 0L, "");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onPause() {
//        // TODO: 2019-08-13 记录位置
////        lastPosition = recyclerView.get
//        EventBus.getDefault().post(new ClearUnReadStatus(chatType, chatWith));
//        super.onPause();
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            EventBus.getDefault().post(new ClearUnReadStatus(chatType, chatWith));
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void getHistory(boolean loadFromRemote, long endTimeStamp, String anchorMsgId) {
        if (loadFromRemote) {
            lastLoadHistoryFromRemote = true;
//            chatPresenter.loadAllHistory(chatType, chatWith, PAGE_ONE, beginTimeStamp);
            chatPresenter.loadAllHistory(chatType, chatWith, PAGE_ONE, 0, endTimeStamp);
        } else {
            lastLoadHistoryFromRemote = false;
            chatPresenter.loadLocalHistory(chatType, chatWith, anchorMsgId, true, false,
                    PAGE_ONE, loginUserId);
        }
    }

    private void initView() {
        titleBar.setTitle(TextUtils.isEmpty(name) ? "" : name, null);
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> ChatBaseActivity.this.finish());

        titleBar.setRightImageEvent(R.drawable.chat_more, v -> {
            onInfoClick();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (chatMsg.size() > 0) {
                ChatData chatData = chatMsg.get(0);
                if (!isGroup() && chatMsg.size() >= LIMIT_LOADREMOTE) {//单人消息从本地拉取LIMIT_LOADREMOTE条之后，从服务器拉取
                    getHistory(true, chatData.getTime(), chatData.getMsgId());
                } else {
                    getHistory(chatData.isRemainHistory(), chatData.getTime(), chatData.getMsgId());
                }
            } else {
                getHistory(false, 0L, "");
            }
        });

        extraFragment = (ChatExtraFragment) getSupportFragmentManager().findFragmentById(R.id.extraFragment);
        extraFragment.setOnVoiceEventListener(new ChatExtraFragment.OnVoiceEventListener() {
            @Override
            public File onVoiceStart() {
                return chatPresenter.startRecord((ChatBaseActivity.this));
            }

            @Override
            public void onVoiceCancel() {
                chatPresenter.cancelRecord();
            }

            @Override
            public void onVoiceStop() {
                chatPresenter.stopRecord();
            }
        });

        ((TouchRecycleView) recyclerView).setOnRecycleViewClickListener(() -> {
            Utils.keyBoard(this, extraFragment.getInput(), false);
        });

        if (showTest) {
            testSendFragment = new TestSendFragment();
            testSendFragment.setParams(chatWith, chatType, myIcon);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutTest, testSendFragment, "TestSendFragment");
            fragmentTransaction.commit();
        }
    }

    protected abstract void onInfoClick();

    protected abstract String getChatIcon(PhotonIMMessage msg);

    protected void onAtCharacterInput() {

    }

    protected abstract boolean isGroup();

    @Override
    public void onRecordFinish(long duration) {
        if (duration < 1000) {
            ChatToastUtils.showChatTimeWarn();
            chatPresenter.cancelRecord();
            return;
        }
        ChatData.Builder chatDataBuilder = new ChatData.Builder()
                .msgStatus(PhotonIMMessage.SENDING)
                .icon(myIcon)
                .localFile(extraFragment.getVideoFilePath())
                .msgType(PhotonIMMessage.AUDIO)
                .chatType(chatType)
                .voiceDuration(duration / 1000)
                .chatWith(chatWith)
                .from(loginUserId)
                .to(chatWith);

        chatPresenter.sendMsg(chatDataBuilder);
    }

    @Override
    public void onRecordFailed() {
        ToastUtils.showText(this, "录制失败，请重试");
        chatPresenter.stopRecord();
    }

    @Override
    public void onDeleteMsgResult(ChatData chatData) {
        chatMsg.remove(chatData.getListPostion());
        chatMsgMap.remove(chatData.getMsgId());
        chatAdapter.notifyDataSetChanged();//全部更新一下，要不然chatData里边的listposition就不对了
    }

    @Override
    public void updateUnreadStatus(ChatData data) {
        data.setMsgStatus(PhotonIMMessage.RECV_READ);
        chatAdapter.notifyItemChanged(data.getListPostion());
    }

    @Override
    public void onGetIcon(ChatData chatData, String url, String name) {
        chatData.setIcon(url);
        chatData.setFromName(name);
        chatAdapter.notifyItemChanged(chatData.getListPostion());
    }

    @Override
    public void onloadHistoryResult(List<ChatData> chatData, Map<String, ChatData> chatDataMap) {
        if (lastLoadHistoryFromRemote) {//服务器没有历史消息了需要加载本地
            if (chatMsg.size() > 0) {
                ChatData chatDataTemp = chatMsg.get(0);
                getHistory(false, 0L, chatDataTemp.getMsgId());
            } else {
                getHistory(false, 0L, "");
            }
            return;
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (CollectionUtils.isEmpty(chatData)) {
//            chatPresenter.loadRemoteHistory();
            com.cosmos.photonim.imbase.utils.ToastUtils.showText(this, getResources().getString(R.string.chat_msg_nomore));
        } else {
            chatMsg.addAll(0, chatData);
            chatMsgMap = (HashMap<String, ChatData>) chatDataMap;
            chatAdapter.notifyItemRangeInserted(0, chatData.size());
            if (firstLoad) {
                firstLoad = false;
                recyclerView.scrollToPosition(chatData.size() - 1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendChatData(ChatDataWrapper chatDataWrapper) {
        if (!chatDataWrapper.chatData.getChatWith().equals(chatWith)) {
            return;
        }

        switch (chatDataWrapper.code) {
            case PhotonIMMessage.SENDING:
                addNewMsg(chatDataWrapper.chatData);
                break;
            case ChatModel.MSG_ERROR_CODE_CANT_REVOKE:
                ToastUtils.showText(this, getResources().getString(R.string.chat_revoke_failed));
                break;
            default:
                changeDataStatus(chatDataWrapper.chatData, chatDataWrapper.code, chatDataWrapper.msg);
        }

    }

    @Override
    public void onRevertResult(ChatData data, int error, String msg) {
        if (error == ChatModel.MSG_ERROR_CODE_SUCCESS) {
            data.setMsgStatus(PhotonIMMessage.RECALL);
            data.setContent(msg);
            chatAdapter.notifyItemChanged(data.getListPostion());
            return;
        }
        ToastUtils.showText(this, String.format("操作失败：%s", msg));
    }

    @Override
    public void onGetChatVoiceFileResult(ChatData data, String path) {
        data.setLocalFile(path);
        chatAdapter.notifyItemChanged(data.getListPostion());
    }

    private void addNewMsg(ChatData chatData) {
        chatData.setTimeContent(getTimeContent(chatData.getTime()));
        chatMsg.add(chatData);
//        chatData.setListPostion(chatMsg.size() - 1);
        chatMsgMap.put(chatData.getMsgId(), chatData);
        chatAdapter.notifyItemInserted(chatMsg.size());
        LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        mLayoutManager.scrollToPosition(chatMsg.size() - 1);
    }

    private void changeDataStatus(ChatData chatData, int code, String msg) {
        ChatData temp = chatMsgMap.get(chatData.getMsgId());
        if (temp == null) {
            LogUtils.log(TAG, "chatData is null");
            return;
        }
        switch (code) {
            case ChatModel.MSG_ERROR_CODE_SUCCESS:
                temp.setMsgStatus(PhotonIMMessage.SENT);
                break;
            case ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED:
            case ChatModel.MSG_ERROR_CODE_SERVER_ERROR:
                temp.setMsgStatus(PhotonIMMessage.SEND_FAILED);
                if (!TextUtils.isEmpty(msg)) {
                    temp.setNotic(msg);
                }
                ChatToastUtils.showChatSendFailedWarn();
                break;
            case ChatModel.MSG_ERROR_CODE_TEXT_ILLEGAL:
            case ChatModel.MSG_ERROR_CODE_PIC_ILLEGAL:
            case ChatModel.MSG_ERROR_CODE_SERVER_NO_GROUP_MEMBER:
            case ChatModel.MSG_ERROR_CODE_GROUP_CLOSED:
                temp.setNotic(msg);
                break;
            case ChatModel.MSG_ERROR_CODE_FREQUENCY:
                com.cosmos.photonim.imbase.utils.ToastUtils.showText(this, getResources().getString(R.string.chat_send_failed_frequency));
                // TODO: 2019-08-07 是否删除该条
                break;
            case ChatModel.MSG_ERROR_CODE_TIME_OUT:
                temp.setMsgStatus(PhotonIMMessage.SEND_FAILED);
                break;
            default:
                temp.setNotic(msg);

        }
        //需要从chatMsgMap读取：有可能是退出聊天再次进入
        chatAdapter.notifyItemChanged(temp.getListPostion());
    }

    @OnClick(R2.id.etInput)
    public void onInputClick() {
        extraFragment.setEmojiRootVisibility(View.GONE);
        extraFragment.setLLExtraVisibility(View.GONE);
    }

    @OnClick(R2.id.ivExtra)
    public void onExtraClick() {
        Utils.keyBoard(this, extraFragment.getInput(), false);
        if (extraFragment.getLLExtraVisibility() == View.VISIBLE) {
            dismissExtraLayout();
        } else {
            showExtraLayout();
        }
    }

    private void dismissExtraLayout() {
        extraFragment.setLLExtraVisibility(View.GONE);
    }

    private void showExtraLayout() {
        extraFragment.setLLExtraVisibility(View.VISIBLE);
        extraFragment.setEmojiRootVisibility(View.GONE);
    }


    @OnClick(R2.id.tvSendMsg)
    public void onSendMsgClick() {
        ChatData.Builder chatDataBuilder = new ChatData.Builder()
                .content(extraFragment.getContent())
                .icon(myIcon)
                .msgType(PhotonIMMessage.TEXT)
                .chatWith(chatWith)
                .chatType(chatType)
                .from(loginUserId)
                .to(chatWith);
        getAtStatus(chatDataBuilder);
        chatPresenter.sendMsg(chatDataBuilder);
        extraFragment.clearInput();
    }

    private void getAtStatus(ChatData.Builder chatDataBuilder) {
        ArrayList<AtEditText.Entity> atList = extraFragment.getAtList();
        if (CollectionUtils.isEmpty(atList)) {
            return;
        }
        List<String> atMsgList = new ArrayList<>(atList.size());
        for (AtEditText.Entity entity : atList) {
            atMsgList.add(entity.getId());
            if (entity.getName().equals(AT_ALL_CONTENT)) {
                chatDataBuilder.atType(PhotonIMMessage.MSG_AT_ALL);
                return;
            }
        }
        chatDataBuilder.atType(PhotonIMMessage.MSG_NO_AT_ALL).msgAtList(atMsgList);
    }

    @Override
    public IPresenter getIPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (chatAdapter == null) {
            chatMsg = new ArrayList<>();
            chatMsgMap = new HashMap<>();
            chatAdapter = new ChatAdapter(chatMsg, (chatData) -> {
                // TODO: 2019-08-17 已经下载中的无需下载
                chatPresenter.getVoiceFile(chatData);
            }, data -> chatPresenter.sendReadMsg(data), chatData -> {
                // TODO: 2019-08-17 已经下载中的无需下载
                chatPresenter.getInfo(chatData);
            }, isGroup());
            chatAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    ChatData chatData = (ChatData) data;
                    int viewId = view.getId();
                    if (viewId == R.id.llVoice) {
                        chatPresenter.cancelPlay();
                        if (chatData.getFrom().equals(loginUserId)) {
                            chatPresenter.play(ChatBaseActivity.this, ((ChatData) data).getLocalFile());
                        } else {
                            if (TextUtils.isEmpty(((ChatData) data).getLocalFile())) {
                                ToastUtils.showText(ChatBaseActivity.this, "请稍后");
                                return;
                            }
                            chatPresenter.play(ChatBaseActivity.this, ((ChatData) data).getLocalFile());
                        }
                    } else if (viewId == R.id.ivWarn) {
                        chatMsg.remove(chatData.getListPostion());
                        chatMsgMap.remove(chatData.getMsgId());
                        chatAdapter.notifyDataSetChanged();
//                        chatAdapter.notifyItemRemoved(chatData.getListPostion());
                        resendMsg(chatData);
                    } else if (viewId == R.id.ivPic) {
                        // TODO: 2019-08-19 图片的获取移到其他位置获取
                        ChatData chatDataTemp;
                        int currentPosition = 0;
                        ArrayList<String> urls = new ArrayList<>();
                        for (int i = 0; i < chatMsg.size(); i++) {
                            chatDataTemp = chatMsg.get(i);
                            if (chatDataTemp.getMsgType() != PhotonIMMessage.IMAGE) {
                                continue;
                            }
                            urls.add(TextUtils.isEmpty(chatDataTemp.getLocalFile()) ? chatDataTemp.getFileUrl() : chatDataTemp.getLocalFile());
                            if (chatData.getMsgId().equals(chatDataTemp.getMsgId())) {
                                currentPosition = urls.size() - 1;
                            }
                        }
                        ImageCheckActivity.startActivity(ChatBaseActivity.this, urls, currentPosition);
                    }
                }

                @Override
                public void onLongClick(View view, Object data, int position) {
                    ChatData chatData = (ChatData) data;
                    int i = view.getId();
                    if (i == R.id.tvContent || i == R.id.llVoice || i == R.id.ivPic) {
                        showPopupMenu(chatData, view);
                    }
                }
            });
        }
        return chatAdapter;
    }

    private void resendMsg(ChatData chatData) {
        chatPresenter.removeChat(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId());
        chatPresenter.reSendMsg(chatData);
    }

    private void showPopupMenu(ChatData data, View view) {
        if (chatPopupWindow != null) {
            chatPopupWindow.dismiss();
        }
        boolean showRevert = data.getFrom().equals(loginUserId)
                && System.currentTimeMillis() - data.getTime() < 1000 * 60 * 2
                && data.getMsgStatus() != PhotonIMMessage.SEND_FAILED;
        boolean showCopy = data.getMsgType() == PhotonIMMessage.TEXT;
        chatPopupWindow = new ChatPopupWindow(showCopy, showRevert, ChatBaseActivity.this, new ChatPopupWindow.OnMenuClick() {
            @Override
            public void onCopyClick() {
                // TODO: 2019-09-23 如果有at？
                Utils.copyToClipBoard(ChatBaseActivity.this, ((ChatData) data).getContent());
            }

            @Override
            public void onRelayClick() {
                ImBaseBridge.getInstance().onRelayClick(ChatBaseActivity.this, data);
            }

            @Override
            public void onRevertClick() {
                chatPresenter.revertMsg((ChatData) data);
            }

            @Override
            public void onDeleteClick() {
                chatPresenter.deleteMsg(data);
            }
        });
        chatPopupWindow.show(((TouchRecycleView) recyclerView).getLastPoint(), recyclerView);
    }

    // TODO: 2019-08-18 移到presenter
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnReceiveMsg(PhotonIMMessage msg) {
        if (!msg.chatWith.equals(chatWith)) {
            return;
        }

        if (msg.status == PhotonIMMessage.RECV_READ && msg.msgId != null) {
            for (String msgId : msg.msgId) {
                ChatData chatData = chatMsgMap.get(msgId);
                if (chatData == null) {
                    LogUtils.log(TAG, "update status :chatData is null");
                    continue;
                }
                chatData.setMsgStatus(PhotonIMMessage.SENT_READ);
                chatAdapter.notifyItemChanged(chatData.getListPostion());
            }
            return;
        }
        // 删除demo层消息去重，防止和sdk2.0混淆
//        if (chatMsgMap.get(msg.id) != null) {
//            LogUtils.log(TAG, String.format("消息重复：%s", msg.id));
//            return;
//        }
        if (msg.status == PhotonIMMessage.RECALL) {
            for (String msgId : msg.msgId) {
                ChatData chatData = chatMsgMap.get(msgId);
                if (chatData == null) {
                    LogUtils.log(TAG, "update status :chatData is null");
                    continue;
                }
                chatData.setContent(msg.notic);
                chatData.setMsgStatus(PhotonIMMessage.RECALL);
                chatAdapter.notifyItemChanged(chatData.getListPostion());
            }
            return;
        }
        ChatData messageData;
        messageData = new ChatData.Builder()
                .chatWith(msg.chatWith)
                .content(msg.content)
                .localFile(msg.localFile)
                .to(msg.to)
                .from(msg.from)
                .time(msg.time)
                .itemType(msg.from.equals(loginUserId) ? Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT : Constants.ITEM_TYPE_CHAT_NORMAL_LEFT)
                .fileUrl(msg.fileUrl)
                .msgStatus(msg.status)
                .icon(getChatIcon(msg))
                .msgId(msg.id)
                .timeContent(getTimeContent(msg.time))
                .msgType(msg.messageType)
                .voiceDuration(msg.mediaTime)
                .chatType(msg.chatType)
                .build();
        chatMsg.add(messageData);
        chatMsgMap.put(msg.id, messageData);
        chatAdapter.notifyItemInserted(chatMsg.size() - 1);
        LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        mLayoutManager.scrollToPosition(chatMsg.size() - 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlertEvent(AlertEvent event) {
        igoreAlert = event.igoreAlert;
    }

    @Override
    protected void onDestroy() {
        TestSendManager.getInstance().unBind();
        EventBus.getDefault().post(new ClearUnReadStatus(chatType, chatWith));
        EventBus.getDefault().unregister(this);
        chatPresenter.cancelPlay();
        chatPresenter.destoryVoiceHelper();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (extraFragment.getLLExtraVisibility() == View.VISIBLE) {
            dismissExtraLayout();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            Uri originalUri = data.getData(); // 获得图片的uri
            String filePath = Utils.getFilePath(this, originalUri);
            if (new File(filePath).length() >= IMAGE_MAX_SIZE) {
                ToastUtils.showText(this, "仅支持发送10M以内的图片");
                return;
            }
            ChatData.Builder chatDataBuild = new ChatData.Builder()
                    .icon(myIcon)
                    .localFile(filePath)
                    .msgType(PhotonIMMessage.IMAGE)
                    .chatType(chatType)
                    .chatWith(chatWith)
                    .from(loginUserId)
                    .to(chatWith);

            chatPresenter.sendMsg(chatDataBuild);
        } else if (requestCode == TakePhotoActivity.REQUEST_CAMERA && resultCode == RESULT_OK) {
            String result = data.getStringExtra(TakePhotoResultFragment.BUNDLE_PHOTO_PATH);
            if (result == null) {
                return;
            }
            if (new File(result).length() >= IMAGE_MAX_SIZE) {
                ToastUtils.showText(this, "仅支持发送10M以内的图片");
                return;
            }
            ChatData.Builder chatDataBuild = new ChatData.Builder()
                    .icon(myIcon)
                    .localFile(result)
                    .msgType(PhotonIMMessage.IMAGE)
                    .chatType(chatType)
                    .chatWith(chatWith)
                    .from(loginUserId)
                    .to(chatWith);
            chatPresenter.sendMsg(chatDataBuild);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private String getTimeContent(long curTime) {
        return TimeUtils.getTimeContent(curTime, CollectionUtils.isEmpty(chatMsg) ? 0 : chatMsg.get(chatMsg.size() - 1).getTime());
    }

}
