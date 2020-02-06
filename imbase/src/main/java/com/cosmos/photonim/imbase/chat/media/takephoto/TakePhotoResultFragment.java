package com.cosmos.photonim.imbase.chat.media.takephoto;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.media.OnReturnPicFragmentListener;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

public class TakePhotoResultFragment extends BaseFragment {
    public static final String BUNDLE_PHOTO_PATH = "BUNDLE_PHOTO_PATH";
    @BindView(R2.id.photoView)
    PhotoView photoView;

    private String photoPath;
    private OnReturnPicFragmentListener onChangeFragmentListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_takephotoresult;
    }

    @Override
    protected void initView(View view) {
        initPhoto();
    }

    private void initPhoto() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            ToastUtils.showText("photo path maybe null");
            return;
        }
        photoPath = arguments.getString(BUNDLE_PHOTO_PATH);
        photoView.setImageURI(Uri.parse(photoPath));
//        ImageLoaderUtils.getInstance().loadImage(getContext(), photoPath, R.drawable.chat_placeholder, ivPhoto);
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

    public void setOnChangeFragmentListener(OnReturnPicFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }
}
