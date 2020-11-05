package com.cosmos.photonim.imbase.chat.emoji;


import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class EmojiBean implements ItemData {
    public int emojiId;
    public String emojiContent;

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_EMOJI;
    }
}
