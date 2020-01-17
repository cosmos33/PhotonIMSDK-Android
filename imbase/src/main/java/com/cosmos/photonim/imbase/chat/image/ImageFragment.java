package com.cosmos.photonim.imbase.chat.image;

import android.net.Uri;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;


public class ImageFragment extends BaseFragment {
    @BindView(R2.id.photoView)
    PhotoView photoView;
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
//        ImageLoaderUtils.getInstance().loadImage(getContext(), imageUrl, R.drawable.chat_placeholder, view.findViewById(R.id.ivImage));
        photoView.setImageURI(Uri.parse(imageUrl));
    }
}
