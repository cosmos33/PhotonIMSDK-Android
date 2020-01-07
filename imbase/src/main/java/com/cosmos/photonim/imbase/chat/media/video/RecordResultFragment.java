package com.cosmos.photonim.imbase.chat.media.video;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.media.OnReturnFragmentListener;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.immomo.media_cosmos.CosmosPlayer;

import butterknife.BindView;
import butterknife.OnClick;

public class RecordResultFragment extends BaseFragment {
    public static final String BUNDLE_VIDEO_PATH = "BUNDLE_VIDEO_PATH";
    public static final String BUNDLE_VIDEO_COVER_PATH = "BUNDLE_VIDEO_COVER_PATH";
    public static final String BUNDLE_VIDEO_COVER_WIDTH = "BUNDLE_VIDEO_COVER_WIDTH";
    public static final String BUNDLE_VIDEO_COVER_HEIGHT = "BUNDLE_VIDEO_COVER_HEIGHT";

    @BindView(R2.id.player)
    CosmosPlayer player;
    @BindView(R2.id.playerContainer)
    FrameLayout playerContainer;
    @BindView(R2.id.ivPlayIcon)
    ImageView ivPlayIcon;

    private OnReturnFragmentListener onChangeFragmentListener;
    private String videoPath;
    private String videoCoverPath;

    private boolean startPlay;
    private int width;
    private int height;

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
        width = arguments.getInt(BUNDLE_VIDEO_COVER_WIDTH);
        height = arguments.getInt(BUNDLE_VIDEO_COVER_HEIGHT);

        ViewGroup.LayoutParams layoutParams = playerContainer.getLayoutParams();
        int[] screenSize = Utils.getScreenSize(getContext());
        int layoutWidth = screenSize[0];
        int layoutHeight = screenSize[1];

        double coverRatio = width * 1. / height;
        double layoutRatio = layoutWidth * 1. / layoutHeight;
        if (coverRatio >= layoutRatio) {
            if (layoutWidth > width) {
                layoutWidth = width;
            }
            layoutParams.height = (int) (layoutWidth * 1. / coverRatio);
        } else {
            if (layoutHeight > height) {
                layoutHeight = height;
            }
            layoutParams.width = (int) (layoutHeight * coverRatio);
        }
        playerContainer.setLayoutParams(layoutParams);

        ImageLoaderUtils.getInstance().loadImage(getContext(), videoCoverPath, R.drawable.chat_placeholder, player.getCoverView());
    }

    @OnClick(R2.id.ivPlayIcon)
    public void onPlayIcon() {
        if (player.isPlaying()) {
            ivPlayIcon.setVisibility(View.VISIBLE);
            player.pause();
        } else {
            if (startPlay) {
                player.resume();
            } else {
                ivPlayIcon.setVisibility(View.GONE);
                player.playVideo(videoPath);
                startPlay = true;
            }
        }
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

    @Override
    public void onDestroy() {
        if (player != null) {
            player.releaseVideo();
        }
        super.onDestroy();
    }
}
