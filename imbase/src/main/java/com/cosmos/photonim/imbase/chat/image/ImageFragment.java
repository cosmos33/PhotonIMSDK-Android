package com.cosmos.photonim.imbase.chat.image;

import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;


public class ImageFragment extends BaseFragment {
    private String imageUrl;

    public static ImageFragment getInstance(String imageUrl) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.imageUrl = imageUrl;
        return imageFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_image;
    }

    @Override
    protected void initView(View view) {
        ImageLoaderUtils.getInstance().loadImage(getContext(), imageUrl, R.drawable.chat_placeholder, view.findViewById(R.id.ivImage));
    }
}
