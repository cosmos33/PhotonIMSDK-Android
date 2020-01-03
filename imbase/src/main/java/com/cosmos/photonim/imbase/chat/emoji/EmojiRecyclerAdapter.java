package com.cosmos.photonim.imbase.chat.emoji;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class EmojiRecyclerAdapter extends RvBaseAdapter {
    public EmojiRecyclerAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new EmojiReycycleItem());
    }
}
