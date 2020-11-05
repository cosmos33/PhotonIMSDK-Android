package com.cosmos.photonim.imbase.chat;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.test.TestSendBean;
import com.cosmos.photonim.imbase.utils.test.TestSendManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class TestSendFragment extends BaseFragment {
    @BindView(R2.id.llTestRoot)
    LinearLayout llTestRoot;
    @BindView(R2.id.etInterval)
    EditText etInterval;
    @BindView(R2.id.etSendNum)
    EditText etSendNum;
    @BindView(R2.id.tvSendNum)
    TextView tvSendNum;
    @BindView(R2.id.tvSendSuccessNum)
    TextView tvSendSuccessNum;
    @BindView(R2.id.tvTotalTime)
    TextView tvTotalTime;
    @BindView(R2.id.tvStart)
    TextView tvStart;
    @BindView(R2.id.etCustomContent)
    EditText etCustomContent;
    @BindView(R2.id.tvSet)
    TextView tvSet;

    boolean showTest;
    TestSendBean testSendBean;

    private String chatWith;
    private String myIcon;
    private int chatType;

    @Override
    public int getLayoutId() {
        return R.layout.layout_chat_test;
    }

    @Override
    protected void initView(View view) {
        // TODO: 2019-12-0 fragment？
        testSendBean = TestSendManager.getInstance().bind(chatWith, new TestSendManager.OnSendChatResultListener() {
            @Override
            public void onSendChatResult(TestSendBean testSendBean) {
                changeTestStatus(testSendBean);
            }

            @Override
            public String getChatWith() {
                return chatWith;
            }
        });
        testSendBean.getChatBuilder().icon(myIcon);

        etCustomContent.setText(testSendBean.getTestContent());
        etSendNum.setText(testSendBean.getTestTotal() + "");
        etInterval.setText(testSendBean.getInterval() + "");
        changeTestStatus(testSendBean);
    }

    private void changeTestStatus(TestSendBean testSendBean) {
        tvSendNum.setText(testSendBean.getSendNum() + "");
        tvSendSuccessNum.setText(testSendBean.getSendSuccessNum() + "");
        tvTotalTime.setText(testSendBean.getTotalTime() + "");

        switch (testSendBean.getStatus()) {
            case TestSendBean.STATUS_UNSTART:
            case TestSendBean.STATUS_ENDED:
            case TestSendBean.STATUS_STOPED:
                tvStart.setText("开始");
                break;
            case TestSendBean.STATUS_SENDING:
                tvStart.setText("停止");
                break;
        }
    }

    @OnClick(R2.id.tvSet)
    public void onTvSetClick() {
        if (testSendBean == null) {
            testSendBean = TestSendBean.getDefaultBean(chatType, chatWith);
        }
        int interval;
        int sendNum;

        try {
            interval = Integer.valueOf(etInterval.getText().toString());
            sendNum = Integer.valueOf(etSendNum.getText().toString());
        } catch (NumberFormatException e) {
            ToastUtils.showText("输入有误，请检查");
            return;
        }
        testSendBean.setInterval(interval);
        testSendBean.setTestCount(sendNum);
        testSendBean.setTestContent(etCustomContent.getText().toString());
        TestSendManager.getInstance().addTestBean(testSendBean);
    }

    @OnClick(R2.id.tvStart)
    public void onTvStartClick() {
        if (testSendBean == null
                || testSendBean.getStatus() == TestSendBean.STATUS_ENDED
                || testSendBean.getStatus() == TestSendBean.STATUS_UNSTART
                || testSendBean.getStatus() == TestSendBean.STATUS_STOPED) {
            tvStart.setText("停止");
            testSendStart();
        } else {
            stopTest();
        }
    }

    private void testSendStart() {
        tvSendNum.setText("");
        tvSendSuccessNum.setText("");
        tvTotalTime.setText("0");
        int interval;
        int sendNum;

        try {
            interval = Integer.valueOf(etInterval.getText().toString());
            sendNum = Integer.valueOf(etSendNum.getText().toString());
        } catch (NumberFormatException e) {
            ToastUtils.showText("输入有误，请检查");
            return;
        }
        ChatData.Builder chatDataBuilder = new ChatData.Builder()
                .icon(myIcon)
                .msgType(PhotonIMMessage.TEXT)
                .chatWith(chatWith)
                .chatType(chatType)
                .from(ImBaseBridge.getInstance().getUserId())
                .to(chatWith);

        if (testSendBean == null) {
            testSendBean = new TestSendBean.Builder()
                    .chatWith(chatWith)
                    .chatType(chatType)
                    .interval(interval)
                    .testCount(sendNum)
                    .testContent(etCustomContent.getText().toString())
                    .chatBuilder(chatDataBuilder).build();
        } else {
            testSendBean.setInterval(interval);
            testSendBean.setTestCount(sendNum);
            testSendBean.setTestContent(etCustomContent.getText().toString());
        }
        TestSendManager.getInstance().startTestWithBind(testSendBean);

        EventBus.getDefault().post(testSendBean);
    }

    private void stopTest() {
        tvStart.setText("开始");
        TestSendManager.getInstance().stopTest(testSendBean);
        EventBus.getDefault().post(testSendBean);
    }

    public void setParams(String chatWith, int chatType, String myIcon) {
        this.chatType = chatType;
        this.chatWith = chatWith;
        this.myIcon = myIcon;
    }
}
