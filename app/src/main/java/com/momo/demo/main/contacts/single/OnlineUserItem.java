package com.momo.demo.main.contacts.single;

import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;

public class OnlineUserItem extends ItemTypeAbstract {
    private OnlineUserData onlineUserData;

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_ONLINEUSER;
    }

    @Override
    public int getLayout() {
        return R.layout.item_onlineuser;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        onlineUserData = (OnlineUserData) data;
        ImageView imageView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(onlineUserData.getNickName());
        ImageLoaderUtils.getInstance().loadImage(imageView.getContext(), onlineUserData.getIcon(), R.drawable.head_placeholder, imageView);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_contact_llRoot};
    }
}
