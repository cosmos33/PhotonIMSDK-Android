package com.cosmos.photonim.imbase.chat.adapter.chat;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class ChatNormalLeftItem extends ChatItemTypeAbstract {
    private OnGetVoiceFileListener onGetVoiceFileListener;
    private OnReceiveReadListener onReceiveReadListener;
    private OnGetIconListener onGetInfoListener;

    public ChatNormalLeftItem(CheckStatusChangeCallback checkStatusChangeCallback,
                              OnGetVoiceFileListener onGetVoiceFileListener,
                              OnReceiveReadListener onReceiveReadListener,
                              OnGetIconListener onGetInfoListener) {
        super(checkStatusChangeCallback);
        this.onGetVoiceFileListener = onGetVoiceFileListener;
        this.onReceiveReadListener = onReceiveReadListener;
        this.onGetInfoListener = onGetInfoListener;
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
        return Constants.ITEM_TYPE_CHAT_NORMAL_LEFT;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_chat_item_normal_left;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        super.fillContent(rvViewHolder, position, data);
        fillMsgContent(rvViewHolder);

        switch (chatData.getMsgStatus()) {
            case PhotonIMMessage.RECALL:
                rvViewHolder.getView(R.id.llMsgRoot).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.tvSysInfo).setVisibility(View.VISIBLE);
                ((TextView) rvViewHolder.getView(R.id.tvSysInfo)).setText(chatData.getContentShow());
                break;
        }
        if (chatData.getChatType() == PhotonIMMessage.SINGLE
                && chatData.getMsgStatus() != PhotonIMMessage.RECV_READ
                && chatData.getMsgStatus() != PhotonIMMessage.RECALL) {
            if (onReceiveReadListener != null) {
                onReceiveReadListener.onReceiveRead((ChatData) data);
            }
        }

        if (chatData.getChatType() == PhotonIMMessage.GROUP) {
            TextView view = (TextView) rvViewHolder.getView(R.id.tvSenderName);
            view.setVisibility(View.VISIBLE);
            view.setText(TextUtils.isEmpty(chatData.getFromName()) ? chatData.getFrom() : chatData.getFromName());
        } else {
            rvViewHolder.getView(R.id.tvSenderName).setVisibility(View.GONE);
        }

        if (chatData.getIcon() == null ||
                (chatData.getChatType() == PhotonIMMessage.GROUP && TextUtils.isEmpty(chatData.getFromName()))) {
            if (onGetInfoListener != null) {
                onGetInfoListener.onGetUserInfo((ChatData) data);
            }
        }
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.tvContent, R.id.llVoice, R.id.ivPic, R.id.llLocation, R.id.flVideo, R.id.llFileContainer, R.id.cbCheck};
    }

    @Override
    public int[] getOnLongClickViews() {
        return new int[]{R.id.tvContent, R.id.llVoice, R.id.ivPic, R.id.llLocation, R.id.flVideo, R.id.llFileContainer};
    }

    public interface OnGetVoiceFileListener {
        void onGetVoice(ChatData data);
    }

    public interface OnReceiveReadListener {
        void onReceiveRead(ChatData data);
    }

    public interface OnGetIconListener {
        void onGetUserInfo(ChatData chatData);
    }


}
