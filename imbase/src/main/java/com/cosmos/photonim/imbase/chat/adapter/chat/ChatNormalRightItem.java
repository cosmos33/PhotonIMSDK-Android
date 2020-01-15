package com.cosmos.photonim.imbase.chat.adapter.chat;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class ChatNormalRightItem extends ChatItemTypeAbstract {
    private boolean group;

    public ChatNormalRightItem(boolean group) {
        this.group = group;
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
        return Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_chat_item_normal_right;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        super.fillContent(rvViewHolder, position, data);
        fillMsgContent(rvViewHolder);
        TextView illegal = (TextView) rvViewHolder.getView(R.id.tvStatus);
        if (!TextUtils.isEmpty(chatData.getNotic())) {
            illegal.setText(chatData.getNotic());
            rvViewHolder.getView(R.id.ivWarn).setVisibility(View.VISIBLE);
        } else {
            rvViewHolder.getView(R.id.ivWarn).setVisibility(View.GONE);
            switch (chatData.getMsgStatus()) {
                case PhotonIMMessage.SENDING:
                    illegal.setVisibility(View.VISIBLE);
                    illegal.setText("发送中");
                    break;
                case PhotonIMMessage.SENT_READ:
                    illegal.setVisibility(View.VISIBLE);
                    illegal.setText("已读");
                    break;
                case PhotonIMMessage.SENT:
                    if (!group) {
                        illegal.setVisibility(View.VISIBLE);
                        illegal.setText("已送达");
                    } else {
                        illegal.setVisibility(View.GONE);
                    }
                    break;
                case PhotonIMMessage.SEND_FAILED:
                    illegal.setVisibility(View.VISIBLE);
                    illegal.setText("发送失败");
                    rvViewHolder.getView(R.id.ivWarn).setVisibility(View.VISIBLE);
                    break;
                case PhotonIMMessage.RECALL:
                    rvViewHolder.getView(R.id.llMsgRoot).setVisibility(View.GONE);
                    rvViewHolder.getView(R.id.tvSysInfo).setVisibility(View.VISIBLE);
                    rvViewHolder.getView(R.id.tvStatus).setVisibility(View.GONE);
                    ((TextView) rvViewHolder.getView(R.id.tvSysInfo)).setText(chatData.getContentShow());
                    break;
                default:
                    illegal.setText("");
            }
        }
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.tvContent, R.id.llVoice, R.id.ivWarn, R.id.ivPic, R.id.llLocation};
    }

    @Override
    public int[] getOnLongClickViews() {
        return new int[]{R.id.tvContent, R.id.llVoice, R.id.ivPic, R.id.llLocation};
    }
}
