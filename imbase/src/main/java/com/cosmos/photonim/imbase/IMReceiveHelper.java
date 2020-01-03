package com.cosmos.photonim.imbase;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.event.IMStatus;
import com.cosmos.photonim.imbase.utils.event.OnDBChanged;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class IMReceiveHelper {
    private final int FREQUENT_UPDATE_DURATION = 500;
    private final int WHAT_UPDATE_SESSION = 1000;
    private final int WHAT_UPDATE_STATUS = 1001;
    private static final String TAG = "IMReceiveHelper";
    private HandlerThread handlerThread;
    private Handler handler;
    private volatile boolean tag;

    private static final IMReceiveHelper ourInstance = new IMReceiveHelper();

    public static IMReceiveHelper getInstance() {
        return ourInstance;
    }

    private IMReceiveHelper() {
        handlerThread = new HandlerThread("updateSession");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_UPDATE_SESSION:
                        EventBus.getDefault().post(msg.obj);
                        tag = false;
                        break;
                    case WHAT_UPDATE_STATUS:
                        EventBus.getDefault().post(msg.obj);
                        break;
                }
                return true;
            }
        });
    }

    public void start() {
        PhotonIMClient.getInstance().setPhotonIMStateListener((state, errorCode, errorMsg) -> {
            String statusMsg;
            LogUtils.log(TAG, String.format("state:%d,threadId:%d", state, Thread.currentThread().getId()));
            switch (state) {
                case PhotonIMClient.IM_STATE_CONNECTING:
                    statusMsg = "自动重连中";
                    break;
                case PhotonIMClient.IM_STATE_AUTH_SUCCESS:
                    statusMsg = "鉴权成功";
                    break;
                case PhotonIMClient.IM_STATE_AUTH_FAILED:
                    statusMsg = "鉴权失败";
                    break;
                case PhotonIMClient.IM_STATE_KICK:
                    statusMsg = "服务器强制下线";
                    break;
                case PhotonIMClient.IM_STATE_NET_UNAVAILABLE:
                    statusMsg = "网络不可用";
                    break;
                default:
                    statusMsg = "未知状态";
            }
            Message msg = Message.obtain();
            msg.what = WHAT_UPDATE_STATUS;
            msg.obj = new IMStatus(state, statusMsg);
            handler.sendMessage(msg);
        });
        LogUtils.log("startim", "setPhotonIMStateListener");
        PhotonIMClient.getInstance().attachUserId(ImBaseBridge.getInstance().getUserId());
        LogUtils.log("startim", "attachUserId");
        PhotonIMClient.getInstance().setDBMode(PhotonIMClient.DB_SYNC);
        LogUtils.log("startim", "setDBMode");
        PhotonIMClient.getInstance().setPhotonIMReSendCallback((code, msg, retTime, chatType, chatWith, msgId) -> {
            LogUtils.log("pim_demo", "Recv DB PhotonIMReSendCallback " + msgId);
            if (code != -1) {
                code = ChatModel.MSG_ERROR_CODE_SUCCESS;
            }
            ChatData chatData = new ChatData.Builder().msgId(msg).chatType(chatType).chatWith(chatWith).build();
            ChatDataWrapper chatDataWrapper = new ChatDataWrapper(chatData, code, msg);
            EventBus.getDefault().post(chatDataWrapper);
        });
        LogUtils.log("startim", "setPhotonIMReSendCallback");
        PhotonIMClient.getInstance().setPhotonIMMessageReceiver((photonIMMessage, lt, lv) -> {
            LogUtils.log(TAG, String.format("msgStatus:%d", photonIMMessage.status));
            LogUtils.log(TAG, "is receive is main thread :" + (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()));
            EventBus.getDefault().post(photonIMMessage);
        });

        LogUtils.log("startim", "setPhotonIMMessageReceiver");
        PhotonIMClient.getInstance().setPhotonIMSyncEventListener((i -> {
            switch (i) {
                case PhotonIMClient.SYNC_START:
                    LogUtils.log("pim_demo", "SYNC_START");
                    break;
                case PhotonIMClient.SYNC_END:
                    LogUtils.log("pim_demo", "SYNC_END");
                    break;
                case PhotonIMClient.SYNCT_IMEOUT:
                    // 这个不会打印，因为默认sync超时，sdk主动断开重连
                    LogUtils.log("pim_demo", "SYNCT_IMEOUT");
                    break;
            }
        }));
        LogUtils.log("startim", "setPhotonIMSyncEventListener");
        PhotonIMDatabase.getInstance().addSessionDataChangeObserver(new SessionDataChangeObserverImpl());
        LogUtils.log("startim", "addSessionDataChangeObserver");
        PhotonIMClient.getInstance().login(ImBaseBridge.getInstance().getUserId(), ImBaseBridge.getInstance().getTokenId(), new HashMap<>());
        LogUtils.log("startim", "login");
//
//        PhotonIMClient.getInstance().setPhotonIMForbiddenAutoResend(new PhotonIMClient.PhotonIMForbiddenAutoResend() {
//            @Override
//            public int onForbiddenAutoResend() {
//                return PhotonIMClient.IMForbidenAutoResendTypeNO;
//            }
//        });
    }

    public class SessionDataChangeObserverImpl implements PhotonIMDatabase.SessionDataChangeObserver {
        private long lastChangeTime;

        public SessionDataChangeObserverImpl() {

        }

        /**
         * 会话数据变化时回调，数据库中已更新
         *
         * @param event    0 新增，1 修改，2 删除
         * @param chatType
         * @param chatWith
         */
        @Override
        public void onSessionChange(int event, int chatType, String chatWith) {
            LogUtils.log(TAG, "onSessionChange");
            if (System.currentTimeMillis() - lastChangeTime < FREQUENT_UPDATE_DURATION) {
                lastChangeTime = System.currentTimeMillis();
                if (!tag) {
                    tag = true;
                    Message msg = Message.obtain();
                    msg.what = WHAT_UPDATE_SESSION;
                    msg.obj = new OnDBChanged(event, chatType, chatWith);
                    handler.sendMessageDelayed(msg, FREQUENT_UPDATE_DURATION);
                }
            } else {
                lastChangeTime = System.currentTimeMillis();
                EventBus.getDefault().post(new OnDBChanged(event, chatType, chatWith));
            }
        }
    }

    public void stop() {

    }
}
