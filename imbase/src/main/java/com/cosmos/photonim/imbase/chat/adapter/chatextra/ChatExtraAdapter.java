package com.cosmos.photonim.imbase.chat.adapter.chatextra;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class ChatExtraAdapter extends RvBaseAdapter {
    public ChatExtraAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new ChatExtraItem());
    }
}
