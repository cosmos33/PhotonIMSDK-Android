package com.momo.demo.main.groupinfo.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

public class GroupInfoMemberItem extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return false;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_GROUP_MEMBER_INFO;
    }

    @Override
    public int getLayout() {
        return R.layout.item_groupinfo_type;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        GroupMembersData groupInfoData = (GroupMembersData) data;
        TextView view = (TextView) rvViewHolder.getView(R.id.tvName);
        view.setText(groupInfoData.getName());
        ImageLoaderUtils.getInstance().loadImage(view.getContext(), groupInfoData.getIcon(),
                R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(com.momo.demo.R.id.ivIcon));
    }

    @Override
    public int[] getOnClickViews() {
        return new int[0];
    }
}
