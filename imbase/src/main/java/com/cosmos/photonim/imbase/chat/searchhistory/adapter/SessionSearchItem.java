package com.cosmos.photonim.imbase.chat.searchhistory.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class SessionSearchItem extends ItemTypeAbstract {
    private final UpdateOtherInfoListener updateOtherInfoListener;
    private ImageView iconView;
    private SearchData searchData;

    public SessionSearchItem(UpdateOtherInfoListener updateOtherInfoListener) {
        this.updateOtherInfoListener = updateOtherInfoListener;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean openLongClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_SEARCH;
    }

    @Override
    public int getLayout() {
        return R.layout.item_msg_search;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        searchData = (SearchData) data;
        searchData.position = position;
        if (!TextUtils.isEmpty(searchData.nickName)) {
            ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(searchData.nickName);
        } else {
            ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText("");
        }

        if (TextUtils.isEmpty(searchData.timeContent)) {
            ((TextView) rvViewHolder.getView(R.id.tvTime)).setText("");
        } else {
            ((TextView) rvViewHolder.getView(R.id.tvTime)).setText(searchData.timeContent);
        }
        ((TextView) rvViewHolder.getView(R.id.tvMsgContent)).setText(searchData.snippetContent);
        iconView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ImageLoaderUtils.getInstance().loadImage(iconView.getContext(), searchData.icon, R.drawable.head_placeholder, iconView);

        if (updateOtherInfoListener != null) {
            if (searchData.nickName == null) {
                updateOtherInfoListener.onUpdateOtherInfo(searchData);
            }
        }

    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_msg_llRoot};
    }

    public interface UpdateOtherInfoListener {
        void onUpdateOtherInfo(SearchData sessionData);
    }
}
