package com.cosmos.photonim.imbase.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.adapter.chat.ChatAdapter;
import com.cosmos.photonim.imbase.chat.ichat.IChatView;
import com.cosmos.photonim.imbase.chat.image.ImageCheckActivity;
import com.cosmos.photonim.imbase.chat.image.VideoPreviewActivity;
import com.cosmos.photonim.imbase.chat.map.MapActivity;
import com.cosmos.photonim.imbase.chat.media.video.VideoInfo;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.OpenFileUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.event.AlertEvent;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.event.ClearChatContent;
import com.cosmos.photonim.imbase.utils.event.ClearUnReadStatus;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.utils.test.TestSendManager;
import com.cosmos.photonim.imbase.view.ChatPopupWindow;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.cosmos.photonim.imbase.view.TouchRecycleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class ChatBaseActivity extends IChatView {
    private static final String TAG = "ChatActivityTAG";
    private static final String EXTRA_CHATTYPE = "EXTRA_CHATTYPE";
    private static final String EXTRA_CHATWITH = "EXTRA_CHATWITH";
    private static final String EXTRA_MyICON = "EXTRA_MyICON";
    private static final String EXTRA_IGOREALERT = "EXTRA_IGOREALERT";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_OTHERICON = "EXTRA_OTHERICON";
    private static final String EXTRA_SHOWTEST = "EXTRA_SHOWTEST";
    private static final String EXTRA_SEARCHID = "EXTRA_SEARCHID";

    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChatPopupWindow chatPopupWindow;

    private ChatAdapter chatAdapter;

    protected String singleChatUserIcon;
    private String name;
    private int lastPosition;
    private boolean igoreAlert;
    private TestSendFragment testSendFragment;
    public ChatExtraFragment extraFragment;
    private boolean showTest;

    private int chatType;
    protected String chatWith;


    public static void startActivity(Activity from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert, String searchId) {
        startActivity(from, chatType, chatWith, myIcon, name, otherIcon, igoreAlert, false, searchId);
    }

    public static void startActivity(Activity from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert) {
        startActivity(from, chatType, chatWith, myIcon, name, otherIcon, igoreAlert, false);
    }

    public static void startActivity(Activity from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert, boolean showTest) {
        startActivity(from, chatType, chatWith, myIcon, name, otherIcon, igoreAlert, showTest, null);
    }

    public static void startActivity(Activity from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert, boolean showTest, String searchMsgId) {
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
        intent.putExtra(EXTRA_SEARCHID, searchMsgId);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        singleChatUserIcon = getIntent().getStringExtra(EXTRA_OTHERICON);
        name = getIntent().getStringExtra(EXTRA_NAME);
        igoreAlert = getIntent().getBooleanExtra(EXTRA_IGOREALERT, false);
        showTest = getIntent().getBooleanExtra(EXTRA_SHOWTEST, false);

        presenter.onCreate(chatType = getIntent().getIntExtra(EXTRA_CHATTYPE, 0),
                chatWith = getIntent().getStringExtra(EXTRA_CHATWITH),
                ImBaseBridge.getInstance().getMyIcon(),
                getIntent().getStringExtra(EXTRA_SEARCHID));

        presenter.loadHistory();
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
        presenter.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    private void initView() {
        titleBar.setTitle(TextUtils.isEmpty(name) ? "" : name, null);
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> ChatBaseActivity.this.finish());

        titleBar.setRightImageEvent(R.drawable.chat_more, v -> {
            onInfoClick();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadMore();
        });

        extraFragment = (ChatExtraFragment) getSupportFragmentManager().findFragmentById(R.id.extraFragment);
        extraFragment.setOnVoiceEventListener(new ChatExtraFragment.OnVoiceEventListener() {
            @Override
            public File onVoiceStart() {
                return presenter.startRecord((ChatBaseActivity.this));
            }

            @Override
            public void onVoiceCancel() {
                presenter.cancelRecord();
            }

            @Override
            public void onVoiceStop() {
                presenter.stopRecord();
            }
        });

        ((TouchRecycleView) recyclerView).setOnRecycleViewClickListener(() -> {
            Utils.keyBoard(this, extraFragment.getInput(), false);
        });

        if (showTest) {
            testSendFragment = new TestSendFragment();
            testSendFragment.setParams(chatWith, chatType, ImBaseBridge.getInstance().getMyIcon());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutTest, testSendFragment, "TestSendFragment");
            fragmentTransaction.commit();
        }
    }

    protected abstract void onInfoClick();

    protected void onAtCharacterInput() {

    }

    public abstract boolean isGroup();

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.white));
        return dividerItemDecoration;
    }

    @Override
    public void onRecordFailed() {
        ToastUtils.showText(this, "录制失败，请重试");
        presenter.stopRecord();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendChatData(ChatDataWrapper chatDataWrapper) {
        presenter.onSendChatData(chatDataWrapper);

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
        toast("下载成功");
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
        presenter.sendText(extraFragment.getContent());
        extraFragment.clearInput();
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
            chatAdapter = new ChatAdapter(presenter.initData(), (chatData) -> {
                // TODO: 2019-08-17 已经下载中的无需下载
                presenter.downLoadFile(chatData);
            }, data -> presenter.sendReadMsg(data), chatData -> {
                // TODO: 2019-08-17 已经下载中的无需下载
                presenter.getInfo(chatData);
            }, isGroup());
            chatAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    onItemClick(view, data);
                }

                @Override
                public void onLongClick(View view, Object data, int position) {
                    ChatData chatData = (ChatData) data;
                    int i = view.getId();
                    if (i == R.id.tvContent
                            || i == R.id.llVoice
                            || i == R.id.ivPic
                            || i == R.id.llLocation
                            || i == R.id.llFileContainer
                            || i == R.id.flVideo) {
                        showPopupMenu(chatData, view);
                    }
                }
            });
        }
        return chatAdapter;
    }

    private void onItemClick(View view, Object data) {
        ChatData chatData = (ChatData) data;
        int viewId = view.getId();
        if (viewId == R.id.llVoice) {
            presenter.cancelPlay();
            if (chatData.getFrom().equals(ImBaseBridge.getInstance().getUserId())) {
                presenter.play(ChatBaseActivity.this, ((ChatData) data).getLocalFile());
            } else {
                if (TextUtils.isEmpty(((ChatData) data).getLocalFile())) {
                    ToastUtils.showText(ChatBaseActivity.this, "请稍后");
                    return;
                }
                presenter.play(ChatBaseActivity.this, ((ChatData) data).getLocalFile());
            }
        } else if (viewId == R.id.ivWarn) {
            presenter.removeData(chatData);
            chatAdapter.notifyDataSetChanged();
//                        chatAdapter.notifyItemRemoved(chatData.getListPostion());
            resendMsg(chatData);
        } else if (viewId == R.id.ivPic) {
            // TODO: 2019-08-19 图片的获取移到其他位置获取
            ArrayList<String> urls = new ArrayList<>();
            int currentPosition = presenter.getImageUrls(chatData, urls);
            ImageCheckActivity.startActivity(ChatBaseActivity.this, urls, currentPosition);
        } else if (viewId == R.id.llLocation) {

            MapActivity.start(ChatBaseActivity.this, chatData);
        } else if (viewId == R.id.flVideo) {
            if (TextUtils.isEmpty(chatData.getLocalFile())) {
                toast("下载中...");
                presenter.downLoadFile(chatData);
            } else {
                VideoInfo videoInfo = new VideoInfo();
                videoInfo.path = chatData.getLocalFile();
                videoInfo.videoTime = chatData.getVideoTimeL();
                videoInfo.videoCoverPath = chatData.getVideoCover();
                videoInfo.height = 1;
                videoInfo.width = (int) (chatData.getVideowhRatio() * videoInfo.height);
                VideoPreviewActivity.startActivity(ChatBaseActivity.this, videoInfo);
            }
        } else if (viewId == R.id.llFileContainer) {
            if (TextUtils.isEmpty(chatData.getLocalFile())) {
                toast("下载中...");
                presenter.downLoadFile(chatData);
            } else {
                OpenFileUtils.openFile(this, chatData.getLocalFile());
            }
        }
    }

    private void resendMsg(ChatData chatData) {
        presenter.removeChat(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId());
        presenter.reSendMsg(chatData);
    }

    private void showPopupMenu(ChatData data, View view) {
        if (chatPopupWindow != null) {
            chatPopupWindow.dismiss();
        }
        boolean showRevert = data.getFrom().equals(ImBaseBridge.getInstance().getUserId())
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
                presenter.revertMsg((ChatData) data);
            }

            @Override
            public void onDeleteClick() {
                presenter.deleteMsg(data);
            }
        });
        chatPopupWindow.show(((TouchRecycleView) recyclerView).getLastPoint(), recyclerView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(PhotonIMMessage msg) {
        presenter.onReceiveMsg(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearChatContent(ClearChatContent content) {
        presenter.clearData();
        chatAdapter.notifyDataSetChanged();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlertEvent(AlertEvent event) {
        igoreAlert = event.igoreAlert;
    }

    @Override
    protected void onDestroy() {
        TestSendManager.getInstance().unBind();
        EventBus.getDefault().post(new ClearUnReadStatus(chatType, chatWith));
        presenter.onDestory();
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
        presenter.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void notifyItemChanged(int listPostion) {
        chatAdapter.notifyItemChanged(listPostion);
    }

    @Override
    public void notifyItemInserted(int listPostion) {
        chatAdapter.notifyItemInserted(listPostion);
    }

    @Override
    public void scrollToPosition(int position) {
        LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        mLayoutManager.scrollToPosition(position);
    }

    @Override
    public String getVideoFilePath() {
        return extraFragment.getVideoFilePath();
    }

    @Override
    public void setRefreshing(boolean b) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemRangeInserted(int i, int size) {
        chatAdapter.notifyItemRangeInserted(i, size);
    }

    @Override
    public void toast(int resId) {
        com.cosmos.photonim.imbase.utils.ToastUtils.showText(getResources().getString(resId));
    }

    @Override
    public ArrayList<AtEditText.Entity> getAtList() {
        return extraFragment.getAtList();
    }
}
