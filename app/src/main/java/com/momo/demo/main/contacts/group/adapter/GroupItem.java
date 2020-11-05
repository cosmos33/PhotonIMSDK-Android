package com.momo.demo.main.contacts.group.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;

public class GroupItem extends ItemTypeAbstract {
    private GroupData groupData;

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_GROUP;
    }

    @Override
    public int getLayout() {
        return R.layout.item_group;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        groupData = (GroupData) data;
        ImageView imageView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(groupData.getName());
        ImageLoaderUtils.getInstance().loadImage(imageView.getContext(), groupData.getIcon(), R.drawable.head_placeholder, imageView);
        if (groupData.isInGroup()) {
            rvViewHolder.getView(R.id.tvJoin).setVisibility(View.GONE);
        } else {
            rvViewHolder.getView(R.id.tvJoin).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_contact_llRoot, R.id.tvJoin};
    }
}
