package com.momo.demo.main.contacts.room.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;

public class RoomItem extends ItemTypeAbstract {
    private RoomData roomData;

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_ROOM;
    }

    @Override
    public int getLayout() {
        return R.layout.item_room;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        roomData = (RoomData) data;
        ImageView imageView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ImageLoaderUtils.getInstance().loadImage(imageView.getContext(), roomData.getAvatar(), R.drawable.head_placeholder, imageView);
        ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(roomData.getName());
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_room_root};
    }
}
