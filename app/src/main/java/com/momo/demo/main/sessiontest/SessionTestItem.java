package com.momo.demo.main.sessiontest;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.cosmos.photonim.imbase.utils.test.TestSendBean;

public class SessionTestItem extends ItemTypeAbstract {
    private final UpdateOtherInfoListener updateOtherInfoListener;
    private ImageView iconView;
    private SessionTestData sessionTestData;

    public SessionTestItem(UpdateOtherInfoListener updateOtherInfoListener) {
        this.updateOtherInfoListener = updateOtherInfoListener;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean openLongClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_MSG_TEST;
    }

    @Override
    public int getLayout() {
        return R.layout.item_msg_test;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        sessionTestData = (SessionTestData) data;
        boolean isSendFromMe = false;
        if (sessionTestData.getLastMsgFr() != null) {
            isSendFromMe = sessionTestData.getLastMsgFr().equals(ImBaseBridge.getInstance().getUserId());
        }
        sessionTestData.setItemPosition(position);
        if (!TextUtils.isEmpty(sessionTestData.getNickName())) {
            ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(sessionTestData.getNickName());
        } else {
            ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(sessionTestData.getChatWith());
        }

        if (sessionTestData.isShowAtTip()) {
            ((TextView) rvViewHolder.getView(R.id.tvMsgContent)).setText(sessionTestData.getAtMsg());
        } else {
            if (TextUtils.isEmpty(sessionTestData.getLastMsgFrName())) {
                ((TextView) rvViewHolder.getView(R.id.tvMsgContent)).setText(sessionTestData.getLastMsgContent());
            } else {
                ((TextView) rvViewHolder.getView(R.id.tvMsgContent)).setText(String.format("%s:%s", sessionTestData.getLastMsgFrName(), sessionTestData.getLastMsgContent()));
            }
        }
        iconView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ImageLoaderUtils.getInstance().loadImage(iconView.getContext(), sessionTestData.getIcon(), R.drawable.head_placeholder, iconView);

        if (updateOtherInfoListener != null) {
            if (sessionTestData.getNickName() == null || shouldUpdateFromName(isSendFromMe)) {
                updateOtherInfoListener.onUpdateOtherInfo(sessionTestData);
            }
        }

        // testtool
        TestSendBean testSendBean = sessionTestData.getTestSendBean();
        TextView view = (TextView) rvViewHolder.getView(R.id.tvStartTest);
        if (testSendBean != null) {
            int status = testSendBean.getStatus();
            switch (status) {
                case TestSendBean.STATUS_ENDED:
                case TestSendBean.STATUS_STOPED:
                case TestSendBean.STATUS_UNSTART:
                    view.setText("开始");
                    break;
                case TestSendBean.STATUS_SENDING:
                    view.setText("停止");
                    break;
            }
        } else {
            view.setText("开始");
        }

    }

    private boolean shouldUpdateFromName(boolean isSendFromMe) {
        return (sessionTestData.getChatType() == PhotonIMMessage.GROUP && sessionTestData.isUpdateFromInfo() && !isSendFromMe);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_msg_llRoot, R.id.tvStartTest, R.id.tvItemClear};
    }

    @Override
    public int[] getOnLongClickViews() {
        return new int[]{R.id.item_msg_llRoot};
    }

    public interface UpdateOtherInfoListener {
        void onUpdateOtherInfo(SessionTestData sessionTestData);
    }
}
