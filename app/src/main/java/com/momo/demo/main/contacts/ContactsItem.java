package com.momo.demo.main.contacts;

import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;
import com.momo.demo.R;

public class ContactsItem extends ItemTypeAbstract {
    private ContactsData contactsData;

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_CONTACT;
    }

    @Override
    public int getLayout() {
        return R.layout.item_contact;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        contactsData = (ContactsData) data;
        ImageView imageView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(contactsData.getName());
        ImageLoaderUtils.getInstance().loadImage(imageView.getContext(), contactsData.getIcon(), R.drawable.contact, imageView);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_contact_llRoot};
    }
}
