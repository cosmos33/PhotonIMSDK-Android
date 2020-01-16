package com.cosmos.photonim.imbase.chat.image.preview;

import android.view.View;
import android.widget.ImageView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewFragment extends BaseFragment {
    @BindView(R2.id.ivClose)
    ImageView ivClose;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_image_preview;
    }

    @Override
    protected void initView(View view) {

    }

    @OnClick(R2.id.tvForward)
    public void onForwardClick() {

    }

    @OnClick(R2.id.tvPreviewOrigin)
    public void onPreViewOrigin() {

    }

    @OnClick(R2.id.tvDown)
    public void onDownClick() {

    }
}
