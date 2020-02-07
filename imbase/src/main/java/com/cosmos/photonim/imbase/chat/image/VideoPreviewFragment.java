package com.cosmos.photonim.imbase.chat.image;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.media.video.VideoInfo;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.immomo.media_cosmos.CosmosPlayer;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoPreviewFragment extends BaseFragment {
    public static final String BUNDLE_VIDEO = "BUNDLE_VIDEO";

    @BindView(R2.id.player)
    CosmosPlayer player;
    @BindView(R2.id.playerContainer)
    FrameLayout playerContainer;
    @BindView(R2.id.ivPlayIcon)
    ImageView ivPlayIcon;
    @BindView(R2.id.progress)
    ProgressBar progressView;

    private boolean startPlay;
    private CustomRunnable customRunnable;
    private int progress;
    private ChatData chatData;
    private VideoInfo videoInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_video_preview;
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            ToastUtils.showText("photo path maybe null");
            return;
        }
        chatData = (ChatData) arguments.getParcelable(BUNDLE_VIDEO);

        videoInfo = new VideoInfo();
        videoInfo.path = chatData.getLocalFile();
        videoInfo.videoTime = chatData.getVideoTimeL();
        videoInfo.videoCoverPath = chatData.getVideoCover();
        videoInfo.height = 1;
        videoInfo.width = (int) (chatData.getVideowhRatio() * videoInfo.height);

        ViewGroup.LayoutParams layoutParams = playerContainer.getLayoutParams();
        int[] screenSize = Utils.getScreenSize(getContext());
        int layoutWidth = screenSize[0];

        if (videoInfo.height != 0 && videoInfo.width != 0) {
            double coverRatio = videoInfo.width * 1. / videoInfo.height;
            layoutParams.height = (int) (layoutWidth * 1. / coverRatio);
            playerContainer.setLayoutParams(layoutParams);
        }

        ImageLoaderUtils.getInstance().loadImage(getContext(), videoInfo.videoCoverPath, R.drawable.chat_placeholder, player.getCoverView());

        progressView.setMax((int) (videoInfo.videoTime));
    }

    @OnClick(R2.id.ivPlayIcon)
    public void onPlayIcon() {
        if (player.isPlaying()) {
            ivPlayIcon.setVisibility(View.VISIBLE);
            player.pause();
            cancelProgress();
        } else {
            startProgress();
            if (startPlay) {
                player.resume();
            } else {
                ivPlayIcon.setVisibility(View.GONE);
                player.playVideo(videoInfo.path);
                startPlay = true;
            }
        }
    }

    @OnClick(R2.id.ivClose)
    public void onCloseClick() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @OnClick(R2.id.tvForward)
    public void onForwardClick() {
        ImBaseBridge.getInstance().onForwardClick(getActivity(), chatData);
    }

    private void startProgress() {
        customRunnable = new CustomRunnable.Builder()
                .runnable(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setProgress(++progress);
                        if (progress >= videoInfo.videoTime) {
                            progress = 0;
                        }
                    }
                })
                .delayTime(1000)
                .repeated(true)
                .build();
        MainLooperExecuteUtil.getInstance().post(customRunnable);
    }

    private void cancelProgress() {
        MainLooperExecuteUtil.getInstance().cancelRunnable(customRunnable);
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.releaseVideo();
        }
        cancelProgress();
        super.onDestroy();
    }
}
