package com.cosmos.photonim.imbase.chat.image;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_image, null);
        ImageLoaderUtils.getInstance().loadImage(getContext(), imageUrl, R.drawable.chat_placeholder, view.findViewById(R.id.ivImage));
        return view;
    }
}
