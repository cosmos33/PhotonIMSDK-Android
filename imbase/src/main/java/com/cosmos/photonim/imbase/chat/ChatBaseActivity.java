package com.cosmos.photonim.imbase.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.messagebody.PhotonIMCustomBody;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.chat.adapter.chat.ChatAdapter;
import com.cosmos.photonim.imbase.chat.adapter.chat.ChatItemTypeAbstract;
import com.cosmos.photonim.imbase.chat.chatroom.ChatRoomActivity;
import com.cosmos.photonim.imbase.chat.ichat.IChatPresenter;
import com.cosmos.photonim.imbase.chat.ichat.IChatView;
import com.cosmos.photonim.imbase.chat.map.MapActivity;
import com.cosmos.photonim.imbase.chat.preview.ImageCheckActivity;
import com.cosmos.photonim.imbase.chat.preview.VideoPreviewActivity;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.OpenFileUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.event.AlertEvent;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.event.ClearChatContent;
import com.cosmos.photonim.imbase.utils.event.ClearUnReadStatus;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.ExecutorUtil;
import com.cosmos.photonim.imbase.utils.test.TestSendManager;
import com.cosmos.photonim.imbase.view.ChatPopupWindow;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.cosmos.photonim.imbase.view.TouchRecycleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class ChatBaseActivity<T extends IChatPresenter> extends IChatView<T> {
    private static final String TAG = "ChatActivityTAG";
    private static final String EXTRA_CHATTYPE = "EXTRA_CHATTYPE";
    private static final String EXTRA_CHATWITH = "EXTRA_CHATWITH";
    //    private static final String EXTRA_MyICON = "EXTRA_MyICON";
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
    protected String name;
    private int lastPosition;
    private boolean igoreAlert;
    private TestSendFragment testSendFragment;
    public ChatExtraFragment extraFragment;
    private boolean showTest;

    private int chatType;
    protected String chatWith;
    private String lastDraft;
    private boolean checkStatus;
    private long lastInputContentTime = 0L;
    private boolean sendChannleMsgInRead = false;
    private Lock lock ;
    private Condition condition;

    public static void startActivity(Context from, int chatType, String chatWith, String name) {
        startActivity(from, chatType, chatWith, null, name, null, false, false, null);
    }

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

    public static void startActivity(Context from, int chatType, String chatWith, String myIcon,
                                     String name, String otherIcon, boolean igoreAlert, boolean showTest, String searchMsgId) {
        Intent intent;
        if (chatType == PhotonIMMessage.SINGLE) {
            intent = new Intent(from, ChatSingleActivity.class);
        } else if (chatType == PhotonIMMessage.GROUP) {
            intent = new Intent(from, ChatGroupActivity.class);
        } else {
            intent = new Intent(from, ChatRoomActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CHATTYPE, chatType);
        intent.putExtra(EXTRA_CHATWITH, chatWith);
//        intent.putExtra(EXTRA_MyICON, myIcon);
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

        if (loadDataFromDB()) {
            presenter.loadHistory();
        }
        presenter.getDraft();
        initView();
        lock = new ReentrantLock();
        condition = lock.newCondition();
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
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> onArrowLeftClick());

        titleBar.setRightImageEvent(R.drawable.chat_more, v -> {
            onInfoClick();
        });
        if (loadDataFromDB()) {
            swipeRefreshLayout.setOnRefreshListener(() -> {
                presenter.loadMore();
            });
        } else {
            swipeRefreshLayout.setEnabled(false);
        }

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

        extraFragment.setOnAtCharacterInputListener(new ChatExtraFragment.OnAtCharacterInputListener() {
            @Override
            public void onAtCharacterInput() {
                ChatBaseActivity.this.onAtCharacterInput();
            }
        });

        extraFragment.setOnDeleteMultiClickListener(new ChatExtraFragment.OnDeleteMultiClickListener() {
            @Override
            public void onDeleteMultiClick() {
                presenter.deleteMultiClick();
            }
        });

        extraFragment.setEtInputEventListener(new ChatExtraFragment.OnEtInputEvent() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO 尝试去发送channel类型消息，告知对方正在输入
//                if(chatType == PhotonIMMessage.SINGLE){
//                    synchronized (lock){
//                        long currentTime = System.currentTimeMillis();
//                        if( s!= null  && (currentTime - lastInputContentTime) >2*1000){
//                            sendChannleMsgInRead = true;
//                            lastInputContentTime = currentTime;
//                            presenter.sendChannelMsg(ImBaseBridge.getInstance().getUserId(),chatWith,1,1,"hello".getBytes(),false);
//                        }
//                    }
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO 尝试去发送channel类型消息，告知对方正在输入已经结
//                AsycTaskUtil.getInstance().createAsycTask(new Callable() {
//                    @Override
//                    public Object call() throws Exception {
//                        Thread.sleep(1000);
//                        if(sendChannleMsgInRead){
//                            synchronized (lock){
//                                if(sendChannleMsgInRead){
//                                    presenter.sendChannelMsg(ImBaseBridge.getInstance().getUserId(),chatWith,2,1,"world".getBytes(),false);
//                                    sendChannleMsgInRead = false;
//                                }
//                            }
//                        }
//                        return null;
//                    }
//                }, new AsycTaskUtil.OnTaskListener() {
//                    @Override
//                    public void onTaskFinished(Object result) {
//                    }
//                }, ExecutorUtil.getDefaultExecutor(), Process.THREAD_PRIORITY_FOREGROUND);

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

    protected void onArrowLeftClick() {
        finish();
    }

    protected abstract void onInfoClick();

    protected boolean loadDataFromDB() {
        return true;
    }

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
        ToastUtils.showText("录制失败，请重试");
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
        checkStatus = false;
        presenter.onSendChatData(chatDataWrapper);
        extraFragment.showChatContainer(false);
    }

    @Override
    public void onRevertResult(ChatData data, int error, String msg) {
        if (error == ChatModel.MSG_ERROR_CODE_SUCCESS) {
            data.setMsgStatus(PhotonIMMessage.RECALL);
            data.setContent(msg);
            chatAdapter.notifyItemChanged(data.getListPostion());
            return;
        }
        ToastUtils.showText(String.format("操作失败：%s", msg));
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
    public T getIPresenter() {
        return (T) new ChatPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(presenter.initData(), new ChatItemTypeAbstract.CheckStatusChangeCallback() {
                @Override
                public boolean checkStatus() {
                    return checkStatus;
                }
            }, (chatData) -> {
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
                if (TextUtils.isEmpty(chatData.getLocalFile())) {
                    toast("下载中...");
                    presenter.downLoadFile(chatData);
                } else {
                    presenter.play(ChatBaseActivity.this, ((ChatData) data).getLocalFile());
                }
            }
        } else if (viewId == R.id.ivWarn) {
            presenter.removeData(chatData);
            chatAdapter.notifyDataSetChanged();
//                        chatAdapter.notifyItemRemoved(chatData.getListPostion());
            resendMsg(chatData);
        } else if (viewId == R.id.ivPic) {
            // TODO: 2019-08-19 图片的获取移到其他位置获取
            ArrayList<ChatData> urls = new ArrayList<>();
            int currentPosition = presenter.getImageUrls(chatData, urls);
            ImageCheckActivity.startActivity(ChatBaseActivity.this, urls, currentPosition);
        } else if (viewId == R.id.llLocation) {

            MapActivity.start(ChatBaseActivity.this, chatData);
        } else if (viewId == R.id.flVideo) {
            if (TextUtils.isEmpty(chatData.getLocalFile())) {
                toast("下载中...");
                presenter.downLoadFile(chatData);
            } else {
                VideoPreviewActivity.startActivity(ChatBaseActivity.this, chatData);
            }
        } else if (viewId == R.id.llFileContainer) {
            if (TextUtils.isEmpty(chatData.getLocalFile())) {
                toast("下载中...");
                presenter.downLoadFile(chatData);
            } else {
                OpenFileUtils.openFile(this, chatData.getLocalFile());
            }
        } else if (viewId == R.id.cbCheck) {
            CheckBox checkBox = (CheckBox) view;
            presenter.checkItem(checkBox.isChecked(), chatData);
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
                ImBaseBridge.getInstance().onForwardClick(ChatBaseActivity.this, data);
            }

            @Override
            public void onRevertClick() {
                presenter.revertMsg((ChatData) data);
            }

            @Override
            public void onDeleteClick() {
                presenter.deleteMsg(data);
            }

            @Override
            public void onMultiClick() {
                checkStatus = true;
                chatAdapter.notifyDataSetChanged();
                extraFragment.showChatContainer(true);

            }
        });
        chatPopupWindow.show(((TouchRecycleView) recyclerView).getLastPoint(), recyclerView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(PhotonIMMessage msg) {
        if(msg.chatType == PhotonIMMessage.CUSTOMMSG){
            PhotonIMCustomBody body =(PhotonIMCustomBody) msg.body;
            if(body.arg1 == 1 && msg.from.equals(chatWith)){
                titleBar.setTitle("对方正在输入...");
            }else if(!titleBar.getTitle().equals(name)){
                titleBar.setTitle(name);
            }
        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyItem(ChatData chatData) {
        chatAdapter.notifyItemChanged(chatData.getListPostion());
    }

    @Override
    protected void onDestroy() {
        TestSendManager.getInstance().unBind();
        EventBus.getDefault().post(new ClearUnReadStatus(chatType, chatWith));
        if (!extraFragment.getContent().trim().equals(lastDraft)) {
            presenter.onSaveDraft(extraFragment.getContent().trim());
        }
        presenter.onDestory();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (checkStatus) {
            checkStatus = false;
            extraFragment.showChatContainer(false);
            chatAdapter.notifyDataSetChanged();
            return;
        }
        if (extraFragment.getLLExtraVisibility() == View.VISIBLE) {
            dismissExtraLayout();
        } else {
            onChatBackPressed();
        }
    }

    protected void onChatBackPressed() {
        super.onBackPressed();
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

    public void onGetDraft(String draft) {
        lastDraft = draft;
        if (extraFragment != null) {
            extraFragment.setDraft(draft);
        }
    }
}
