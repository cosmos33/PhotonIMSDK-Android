package com.cosmos.photonim.imbase.chat.adapter;

import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.adapter.chat.ChatItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class ChatSysInfoItem extends ChatItemTypeAbstract {
    @Override
    public boolean openClick() {
        return false;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_CHAT_SYSINFO;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_chat_item_sysinfo;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        super.fillContent(rvViewHolder, position, data);
        ((TextView) rvViewHolder.getView(R.id.tvSysInfo)).setText(((ChatData) data).getContentShow());
    }

    @Override
    public int[] getOnClickViews() {
        return new int[0];
    }
}
