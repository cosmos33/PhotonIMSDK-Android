package com.cosmos.photonim.imbase.chat.emoji;

import android.widget.ImageView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class EmojiReycycleItem extends ItemTypeAbstract {
    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_EMOJI;
    }

    @Override
    public int getLayout() {
        return R.layout.item_emoji;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        EmojiBean bean = (EmojiBean) data;
        ImageView view = (ImageView) rvViewHolder.getView(R.id.ivEmoji);
        ImageLoaderUtils.getInstance().loadResImage(view.getContext(), bean.emojiId, view);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.ivEmoji};
    }
}
