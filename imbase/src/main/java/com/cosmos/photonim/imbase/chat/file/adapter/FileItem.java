package com.cosmos.photonim.imbase.chat.file.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class FileItem extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_FILE;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_file;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        FileItemData file = (FileItemData) data;
        file.setPosition(position);
        ImageView icon = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        icon.setImageResource(file.getResId());
        ((TextView) rvViewHolder.getView(R.id.tvName)).setText(file.getFileName());
        CheckBox checkBox = (CheckBox) rvViewHolder.getView(R.id.cbCheck);
        if (file.isDirectory()) {
            checkBox.setVisibility(View.GONE);
            ((TextView) rvViewHolder.getView(R.id.tvContent)).setText(String.format("文件：%d", file.getChildSize()));
        } else {
            checkBox.setVisibility(View.VISIBLE);
            ((TextView) rvViewHolder.getView(R.id.tvContent)).setText(String.format("%s %s", file.getSize(), file.getTime()));
            checkBox.setChecked(file.isChecked());
        }
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.cbCheck, R.id.llContainer};
    }
}
