package com.cosmos.photonim.imbase.chat.media.video;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.media.OnReturnFragmentListener;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RecordResultFragment extends BaseFragment {
    public static final String BUNDLE_VIDEO_PATH = "BUNDLE_VIDEO_PATH";
    public static final String BUNDLE_VIDEO_COVER_PATH = "BUNDLE_VIDEO_COVER_PATH";

    @BindView(R2.id.ivPhoto)
    ImageView ivPhoto;
    private OnReturnFragmentListener onChangeFragmentListener;
    private String videoPath;
    private String videoCoverPath;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_recordresult;
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            ToastUtils.showText("photo path maybe null");
            return;
        }
        videoPath = arguments.getString(BUNDLE_VIDEO_PATH);
        videoCoverPath = arguments.getString(BUNDLE_VIDEO_COVER_PATH);
        ImageLoaderUtils.getInstance().loadImage(getContext(), videoCoverPath, R.drawable.chat_placeholder, ivPhoto);
    }

    @OnClick(R2.id.ivPlay)
    public void onPlayClick() {

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
            onChangeFragmentListener.onDoneClick(videoPath);
        }
    }

    public void setOnChangeFragmentListener(OnReturnFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }
}
