package com.momo.demo.main.groupmemberselected.apater;

import android.view.View;
import android.widget.TextView;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

public class GroupMemberAllItem extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED_ALL;
    }

    @Override
    public int getLayout() {
        return R.layout.item_group_member_selected_all;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        GroupMembersData groupMembersData = (GroupMembersData) data;
        groupMembersData.setPosition(position);
        View view = rvViewHolder.getView(R.id.ivIcon);
        ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(groupMembersData.getName());
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.llRoot};
    }
}
