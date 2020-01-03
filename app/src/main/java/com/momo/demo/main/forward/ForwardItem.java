package com.momo.demo.main.forward;

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

public class ForwardItem extends ItemTypeAbstract {
    private String igoreId;
    private final UpdateOtherInfoListener updateOtherInfoListener;

    public ForwardItem(String chatWith, UpdateOtherInfoListener updateOtherInfoListener) {
        super();
        igoreId = chatWith;
        this.updateOtherInfoListener = updateOtherInfoListener;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_FORWARD;
    }

    @Override
    public int getLayout() {
        return R.layout.item_forward;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        ForwardData userData = (ForwardData) data;
        userData.setPosition(position);
        CheckBox cb = (CheckBox) rvViewHolder.getView(R.id.checkbox);
        if (userData.getUserId().equals(LoginInfo.getInstance().getUserId()) || userData.getUserId().equals(igoreId)) {
            cb.setVisibility(View.INVISIBLE);
        } else {
            cb.setVisibility(View.VISIBLE);
            cb.setChecked(userData.isSelected());
        }
        ImageLoaderUtils.getInstance().loadImage(cb.getContext(), userData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));
        ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(userData.getNickName());
        if (userData.isShowTitle()) {
            rvViewHolder.getView(R.id.llRoot).setVisibility(View.VISIBLE);
            if (userData.getType() == ForwardData.ONLINE) {
                rvViewHolder.getView(R.id.divider).setVisibility(View.GONE);
                ((TextView) rvViewHolder.getView(R.id.tvTitle)).setText("附近在线的人");
            } else {
                rvViewHolder.getView(R.id.divider).setVisibility(View.VISIBLE);
                ((TextView) rvViewHolder.getView(R.id.tvTitle)).setText("最近会话");
            }
        } else {
            rvViewHolder.getView(R.id.llRoot).setVisibility(View.GONE);
        }

        if (userData.getNickName() == null && updateOtherInfoListener != null) {
            updateOtherInfoListener.onUpdateOtherInfo(userData);
        }
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.checkbox};
    }

    public interface UpdateOtherInfoListener {
        void onUpdateOtherInfo(ForwardData sessionData);
    }
}
