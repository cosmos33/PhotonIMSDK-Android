package com.cosmos.photonim.imbase.chat.ichat;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;

import java.util.List;
import java.util.Map;

public abstract class IChatView extends RvBaseActivity implements IView {
    protected IChatPresenter chatPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatPresenter = (IChatPresenter) getIPresenter();
        if (chatPresenter == null) {
            throw new IllegalStateException("chatPresenter is null");
        }

    }

    public abstract void onloadHistoryResult(List<ChatData> chatData, Map<String, ChatData> chatDataMap);

    public abstract void onRevertResult(ChatData data1, int error, String msg);

    public abstract void onGetChatVoiceFileResult(ChatData data, String path);

    public abstract void onRecordFinish(long duration);

    public abstract void updateUnreadStatus(ChatData data);

    public abstract void onGetIcon(ChatData chatData, String iconUrl, String name);

    public abstract void onRecordFailed();

    public abstract void onDeleteMsgResult(ChatData chatData);
}
