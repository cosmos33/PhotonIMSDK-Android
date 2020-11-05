package com.cosmos.photonim.imbase.chat.adapter.chatextra;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;


public class ChatExtraItemData implements ItemData {
    public int resId;
    public String content;

    public ChatExtraItemData(int resId, String content) {
        this.resId = resId;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_CHAT_EXTRA;
    }
}
