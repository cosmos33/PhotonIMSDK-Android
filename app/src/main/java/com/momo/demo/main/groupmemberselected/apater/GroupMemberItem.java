package com.momo.demo.main.groupmemberselected.apater;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

public class GroupMemberItem extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED;
    }

    @Override
    public int getLayout() {
        return R.layout.item_group_member_selected;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        GroupMembersData groupMembersData = (GroupMembersData) data;
        groupMembersData.setPosition(position);

        if (groupMembersData.isShowCb()) {
            CheckBox cb = (CheckBox) rvViewHolder.getView(R.id.checkbox);

            if (groupMembersData.getId().equals(LoginInfo.getInstance().getUserId())
                //                || groupMembersData.getId().equals(igoreId)
            ) {
                cb.setVisibility(View.INVISIBLE);
            } else {
                cb.setVisibility(View.VISIBLE);
                cb.setChecked(groupMembersData.isSelected());
            }
        } else {
            rvViewHolder.getView(R.id.checkbox).setVisibility(View.GONE);
        }
        TextView view = (TextView) rvViewHolder.getView(R.id.tvNickName);
        ImageLoaderUtils.getInstance().loadImage(view.getContext(), groupMembersData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));
        view.setText(groupMembersData.getName());
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.checkbox};
    }
}
