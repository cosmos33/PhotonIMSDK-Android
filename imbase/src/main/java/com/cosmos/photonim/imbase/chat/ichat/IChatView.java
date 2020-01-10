package com.cosmos.photonim.imbase.chat.ichat;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;

import java.util.ArrayList;

public abstract class IChatView extends RvBaseActivity<IChatPresenter> {

    public abstract void onRevertResult(ChatData data1, int error, String msg);

    public abstract void onGetChatVoiceFileResult(ChatData data, String path);

    public abstract void updateUnreadStatus(ChatData data);

    public abstract void onGetIcon(ChatData chatData, String iconUrl, String name);

    public abstract void onRecordFailed();

    public abstract void notifyItemChanged(int listPostion);

    public abstract void notifyItemInserted(int listPostion);

    public abstract String getChatIcon(PhotonIMMessage msg);

    public abstract void scrollToPosition(int i);

    public abstract String getVideoFilePath();

    public abstract void setRefreshing(boolean b);

    public abstract void notifyDataSetChanged();

    public abstract void notifyItemRangeInserted(int i, int size);

    public abstract void toast(int chat_msg_nomore);

    public abstract boolean isGroup();

    public abstract ArrayList<AtEditText.Entity> getAtList();
}
