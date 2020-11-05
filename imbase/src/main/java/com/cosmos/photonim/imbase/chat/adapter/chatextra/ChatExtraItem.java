package com.cosmos.photonim.imbase.chat.adapter.chatextra;

import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class ChatExtraItem extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_CHAT_EXTRA;
    }

    @Override
    public int getLayout() {
        return R.layout.item_chat_extra;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        ChatExtraItemData extraItemData = (ChatExtraItemData) data;
        ((ImageView) rvViewHolder.getView(R.id.ivIcon)).setImageResource(extraItemData.resId);
        ((TextView) rvViewHolder.getView(R.id.tvContent)).setText(extraItemData.content);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.llContainer};
    }
}
