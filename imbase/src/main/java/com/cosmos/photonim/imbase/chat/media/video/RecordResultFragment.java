package com.cosmos.photonim.imbase.chat.media.video;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    public static final String BUNDLE_VIDEO = "BUNDLE_VIDEO";

    @BindView(R2.id.player)
    CosmosPlayer player;
    @BindView(R2.id.playerContainer)
    FrameLayout playerContainer;
    @BindView(R2.id.ivPlayIcon)
    ImageView ivPlayIcon;
    @BindView(R2.id.tvTime)
    TextView tvTime;

    private OnReturnFragmentListener onChangeFragmentListener;

    private boolean startPlay;
    private VideoInfo videoInfo;

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
        videoInfo = (VideoInfo) arguments.getSerializable(BUNDLE_VIDEO);
        tvTime.setText(Utils.videoTime(videoInfo.videoTime));

        ViewGroup.LayoutParams layoutParams = playerContainer.getLayoutParams();
        int[] screenSize = Utils.getScreenSize(getContext());
        int layoutWidth = screenSize[0];
        int layoutHeight = screenSize[1];

        double coverRatio = videoInfo.width * 1. / videoInfo.height;
        double layoutRatio = layoutWidth * 1. / layoutHeight;
        if (coverRatio >= layoutRatio) {
            if (layoutWidth > videoInfo.width) {
                layoutWidth = videoInfo.width;
            }
            layoutParams.height = (int) (layoutWidth * 1. / coverRatio);
        } else {
            if (layoutHeight > videoInfo.height) {
                layoutHeight = videoInfo.height;
            }
            layoutParams.width = (int) (layoutHeight * coverRatio);
        }
        playerContainer.setLayoutParams(layoutParams);

        ImageLoaderUtils.getInstance().loadImage(getContext(), videoInfo.videoCoverPath, R.drawable.chat_placeholder, player.getCoverView());
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
                player.playVideo(videoInfo.path);
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
            onChangeFragmentListener.onDoneClick(videoInfo);
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
