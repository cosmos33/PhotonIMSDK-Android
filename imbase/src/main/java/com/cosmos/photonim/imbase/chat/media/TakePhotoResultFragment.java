package com.cosmos.photonim.imbase.chat.media;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TakePhotoResultFragment extends BaseFragment {
    public static final String BUNDLE_PHOTO_PATH = "BUNDLE_PHOTO_PATH";
    @BindView(R2.id.ivPhoto)
    ImageView ivPhoto;

    private String photoPath;
    private TakePhotoActivity.OnReturnFragmentListener onChangeFragmentListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_takephotoresult;
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            ToastUtils.showText("photo path maybe null");
            return;
        }
        photoPath = arguments.getString(BUNDLE_PHOTO_PATH);
        ImageLoaderUtils.getInstance().loadImage(getContext(), photoPath, R.drawable.chat_placeholder, ivPhoto);
    }

    @OnClick(R2.id.ivReturn)
    public void onReturnClick() {
        if (onChangeFragmentListener != null) {
            onChangeFragmentListener.onReturnFragment(null);
        }
    }


    @OnClick(R2.id.ivDone)
    public void onDonwClick() {
        if (onChangeFragmentListener != null) {
            onChangeFragmentListener.onDoneClick(photoPath);
        }
    }

    public void setOnChangeFragmentListener(TakePhotoActivity.OnReturnFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }
}
