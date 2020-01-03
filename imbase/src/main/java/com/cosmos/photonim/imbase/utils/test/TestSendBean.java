package com.cosmos.photonim.imbase.utils.test;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;

public class TestSendBean {
    public static final int STATUS_UNSTART = 1;
    public static final int STATUS_SENDING = 2;
    public static final int STATUS_ENDED = 3;
    public static final int STATUS_STOPED = 4;
    private String chatWith;
    private String testContent;
    private int testTotal;//需要发送消息总数
    private long interval;
    private volatile int sendNum;//已经发送消息总数
    private int sendSuccessNum;
    private ChatData.Builder chatBuilder;
    private volatile int status;
    private CustomRunnable customRunnable;
    private int chatType;
    private long fristTime;
    private long lastMsgTime;


    public static TestSendBean getDefaultBean(int chatType, String chatWith) {
        ChatData.Builder chatDataBuilder = new ChatData.Builder()
                .msgType(PhotonIMMessage.TEXT)
                .chatWith(chatWith)
                .chatType(chatType)
                .icon(ImBaseBridge.getInstance().getMyIcon())
                .from(ImBaseBridge.getInstance().getUserId())
                .to(chatWith);

        TestSendBean testSendBean = new TestSendBean.Builder()
                .chatWith(chatWith)
                .chatType(chatType)
                .interval(1000)
                .testCount(100)
                .testContent("哈哈哈")
                .chatBuilder(chatDataBuilder).build();
        return testSendBean;
    }

    private TestSendBean(Builder builder) {
        chatWith = builder.chatWith;
        testContent = builder.testContent;
        testTotal = builder.testTotal;
        interval = builder.interval;
        setSendNum(builder.sendNum);
        sendSuccessNum = builder.sendSuccessNum;
        chatBuilder = builder.chatBuilder;
        setStatus(builder.status);
        setCustomRunnable(builder.customRunnable);
        chatType = builder.chatType;
    }

    public String getChatWith() {
        return chatWith;
    }

    public String getTestContent() {
        return testContent;
    }

    public int getTestTotal() {
        return testTotal;
    }

    public long getInterval() {
        return interval;
    }

    public long getTotalTime() {
        if (lastMsgTime == 0 && fristTime != 0) {
            return System.currentTimeMillis() - fristTime;
        }
        return lastMsgTime - fristTime;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public int getSendSuccessNum() {
        return sendSuccessNum;
    }

    public void setSendSuccessNum(int sendSuccessNum) {
        this.sendSuccessNum = sendSuccessNum;
    }

    public ChatData.Builder getChatBuilder() {
        return chatBuilder;
    }

    public CustomRunnable getCustomRunnable() {
        return customRunnable;
    }

    public void setCustomRunnable(CustomRunnable customRunnable) {
        this.customRunnable = customRunnable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChatType() {
        return chatType;
    }

    public void setStartTime(long currentTimeMillis) {
        this.fristTime = currentTimeMillis;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setTestCount(int sendNum) {
        this.testTotal = sendNum;
    }

    public void setTestContent(String toString) {
        this.testContent = toString;
    }

    public void clear() {
        sendNum = 0;
        sendSuccessNum = 0;
        fristTime = 0;
        lastMsgTime = 0;
    }

    public static final class Builder {
        private String testContent;
        private int testTotal;
        private long interval;
        private int sendNum;
        private int sendSuccessNum;
        private ChatData.Builder chatBuilder;
        private int status = STATUS_UNSTART;
        private CustomRunnable customRunnable;
        private int chatType;
        private String chatWith;

        public Builder() {
        }

        public Builder testContent(String val) {
            testContent = val;
            return this;
        }

        public Builder testCount(int val) {
            testTotal = val;
            return this;
        }

        public Builder interval(long val) {
            interval = val;
            return this;
        }

        public Builder sendNum(int val) {
            sendNum = val;
            return this;
        }

        public Builder sendSuccessNum(int val) {
            sendSuccessNum = val;
            return this;
        }

        public Builder chatBuilder(ChatData.Builder val) {
            chatBuilder = val;
            return this;
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public Builder customRunnable(CustomRunnable val) {
            customRunnable = val;
            return this;
        }

        public Builder chatType(int val) {
            chatType = val;
            return this;
        }

        public TestSendBean build() {
            return new TestSendBean(this);
        }

        public Builder chatWith(String val) {
            chatWith = val;
            return this;
        }
    }
}
