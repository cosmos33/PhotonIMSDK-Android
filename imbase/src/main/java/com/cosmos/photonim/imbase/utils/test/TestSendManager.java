package com.cosmos.photonim.imbase.utils.test;

import android.os.HandlerThread;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photonim.imbase.chat.ChatPresenter;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.event.IMStatus;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class TestSendManager {
    private static final String TAG = "TestSendManager";
    private HashMap<String, TestSendBean> testMap;
    private HandlerThread handlerThread;
    private ChatPresenter chatPresenter;
    private OnSendChatResultListener onSendChatResultListener;//聊天界面
    private OnAuthResultListener onAuthResultListener;//session界面
    private TestSendBean bindBean;
    private int authSuccess;
    private int authFailed;

    public void clearAuth() {
        authSuccess = 0;
        authFailed = 0;
    }

    public void addTestBean(TestSendBean bean) {
        if (bean == null) {
            return;
        }
        init();
        testMap.put(bean.getChatWith(), bean);
    }

    static class TestManagerHolder {
        static TestSendManager testSendManager = new TestSendManager();
    }

    private TestSendManager() {

    }

    public static TestSendManager getInstance() {
        return TestManagerHolder.testSendManager;
    }

    public void startTest(TestSendBean bean) {
        init();
        TestSendBean testSendBean = testMap.get(bean.getChatWith());
        if (testSendBean != null) {
            LogUtils.log(TAG, String.format("test:%s已经存在", bean.getChatWith()));
//            testMap.remove(testSendBean.getChatWith());
        } else {
            testMap.put(bean.getChatWith(), bean);
            testSendBean = bean;
        }
        testSendBean.setStatus(TestSendBean.STATUS_SENDING);
        testSendBean.setStartTime(System.currentTimeMillis());
        testSendBean.setSendNum(0);
        testSendBean.setSendSuccessNum(0);


        CustomRunnable customRunnable = new CustomRunnable();
        testSendBean.setCustomRunnable(customRunnable);

        customRunnable.setRunnable(new SendRunnable(testSendBean));
        customRunnable.setDelayTime(testSendBean.getInterval());
        customRunnable.setRepeated(true);
        MainLooperExecuteUtil.getInstance().post(customRunnable);
    }

    private void init() {
        if (testMap == null) {
            testMap = new HashMap<>();
            handlerThread = new HandlerThread("send msg HandlerThread");
            chatPresenter = new ChatPresenter(null);
            EventBus.getDefault().register(this);
        }
    }

    public void startTestWithBind(TestSendBean bean) {
        bindBean = bean;
        startTest(bean);
    }

    public void stopTest(TestSendBean testSendBean) {
        if (testMap == null || testSendBean == null) {
            return;
        }

        TestSendBean temp = testMap.get(testSendBean.getChatWith());
        if (temp == null) {
            return;
        }
        temp.setStatus(TestSendBean.STATUS_STOPED);
    }

    class SendRunnable implements Runnable {
        public SendRunnable(TestSendBean testSendBean) {
            this.testSendBean = testSendBean;
        }

        private TestSendBean testSendBean;

        @Override
        public void run() {
            if (testSendBean.getSendNum() == testSendBean.getTestTotal()
                    || testSendBean.getStatus() != TestSendBean.STATUS_SENDING) {
                testSendBean.getCustomRunnable().setRepeated(false);
                return;
            }
            testSendBean.setSendNum(testSendBean.getSendNum() + 1);
            testSendBean.getChatBuilder().content(String.format("%s%d", testSendBean.getTestContent(), testSendBean.getSendNum()));
            testSendBean.getChatBuilder().testSend(true);
            chatPresenter.sendMsg(testSendBean.getChatBuilder());


            CustomRunnable customRunnable = new CustomRunnable();
            customRunnable.setRunnable(() -> {
                if (onSendChatResultListener != null && onSendChatResultListener.getChatWith().equals(testSendBean.getChatWith())) {
                    onSendChatResultListener.onSendChatResult(testSendBean);
                }
            });
            MainLooperExecuteUtil.getInstance().post(customRunnable);
        }
    }

    public TestSendBean bind(String chatId, OnSendChatResultListener onSendChatResultListener) {
        this.onSendChatResultListener = onSendChatResultListener;
        if (testMap == null) {
            return null;
        }
        return bindBean = testMap.get(chatId);
    }

    public void addAuthResultListener(OnAuthResultListener onAllSendChatResultListener) {
        this.onAuthResultListener = onAllSendChatResultListener;
    }

    public void unBind() {
        this.onSendChatResultListener = null;
        bindBean = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendChatData(ChatDataWrapper chatDataWrapper) {
        if (chatDataWrapper.chatData != null && !chatDataWrapper.chatData.isTestSend()) {
            return;
        }
        if (chatDataWrapper.code != 0) {
            return;
        }
        TestSendBean testSendBean = testMap.get(chatDataWrapper.chatData.getChatWith());
        if (testSendBean == null) {
            return;
        }
        testSendBean.setSendSuccessNum(testSendBean.getSendSuccessNum() + 1);
        testSendBean.setLastMsgTime(System.currentTimeMillis());
        if (bindBean != null
                && testSendBean.getChatWith().equals(bindBean.getChatWith())
                && onSendChatResultListener != null
                && onSendChatResultListener.getChatWith().equals(testSendBean.getChatWith())) {
            onSendChatResultListener.onSendChatResult(testSendBean);
        }
    }

    public interface OnSendChatResultListener {
        void onSendChatResult(TestSendBean testSendBean);

        String getChatWith();
    }

    public interface OnAuthResultListener {
        void onAuthResult(int success, int authFailed);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthSuccess(IMStatus imStatus) {
        switch (imStatus.status) {
            case PhotonIMClient.IM_STATE_AUTH_SUCCESS:
                authSuccess++;
                if (onAuthResultListener != null) {
                    onAuthResultListener.onAuthResult(authSuccess, authFailed);
                }
                break;
//            case PhotonIMClient.IM_AUTH:
//                authFailed++;
//                if (onAuthResultListener != null) {
//                    onAuthResultListener.onAuthResult(authSuccess, authFailed);
//                }
//                break;
        }
    }

    public int getAuthSuccess() {
        return authSuccess;
    }

    public int getAuthFailed() {
        return authFailed;
    }

    public HashMap<String, TestSendBean> getTestMap() {
        return testMap;
    }
}
