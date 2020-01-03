package com.cosmos.photonim.imbase.view.listdialog;

import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class ListDialogItem extends ItemTypeAbstract {

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_LIST_DIALOG;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_list;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        Listitemdata itemData = (Listitemdata) data;
        ((TextView) rvViewHolder.getView(R.id.tvContent)).setText(itemData.getContent());
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.root};
    }
}
