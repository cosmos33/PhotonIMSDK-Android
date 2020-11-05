package com.cosmos.photonim.imbase.chat.album.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class AlbumItemType extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_ALBUM;
    }

    @Override
    public int getLayout() {
        return R.layout.item_album;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        CategoryFile categoryFile = (CategoryFile) data;
        ImageView view = (ImageView) rvViewHolder.getView(R.id.ivPic);
        ImageLoaderUtils.getInstance().loadImage(view.getContext(), categoryFile.mPath, R.drawable.head_placeholder, view);

        ((CheckBox) rvViewHolder.getView(R.id.cbCheck)).setChecked(categoryFile.checked);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.cbCheck, R.id.ivPic};
    }
}
