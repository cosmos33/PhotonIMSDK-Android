package com.cosmos.photonim.imbase.chat.ichat;

import com.cosmos.photonim.imbase.base.mvpbase.IView;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;

import java.util.List;
import java.util.Map;

public abstract class IChatView extends RvBaseActivity<IChatPresenter> implements IView {

    public abstract void onloadHistoryResult(List<ChatData> chatData, Map<String, ChatData> chatDataMap, boolean search);

    public abstract void onRevertResult(ChatData data1, int error, String msg);

    public abstract void onGetChatVoiceFileResult(ChatData data, String path);

    public abstract void onRecordFinish(long duration);

    public abstract void updateUnreadStatus(ChatData data);

    public abstract void onGetIcon(ChatData chatData, String iconUrl, String name);

    public abstract void onRecordFailed();

    public abstract void onDeleteMsgResult(ChatData chatData);
}
