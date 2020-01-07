package com.cosmos.photonim.imbase.chat.adapter.chat;


import com.cosmos.photonim.imbase.chat.adapter.ChatSysInfoItem;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class ChatAdapter extends RvBaseAdapter {
    public ChatAdapter(List baseDataList, ChatNormalLeftItem.OnGetVoiceFileListener onGetVoiceFileListener,
                       ChatNormalLeftItem.OnReceiveReadListener onReceiveReadListener,
                       ChatNormalLeftItem.OnGetIconListener onGetIconListener, boolean group) {
        super(baseDataList);
        if (baseDataList == null) {
            throw new IllegalStateException("chat list is null");
        }
        addItemType(new ChatNormalLeftItem(onGetVoiceFileListener, onReceiveReadListener, onGetIconListener));
        addItemType(new ChatNormalRightItem(group));
        addItemType(new ChatSysInfoItem());
    }
}
