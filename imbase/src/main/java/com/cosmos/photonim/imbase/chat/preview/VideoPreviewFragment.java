package com.cosmos.photonim.imbase.chat.preview;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.media.video.VideoInfo;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.immomo.media_cosmos.CosmosPlayer;
import com.mm.player.ICosPlayer;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoPreviewFragment extends BaseFragment {
    private static final String TAG = "VideoPreviewFragment";
    public static final String BUNDLE_VIDEO = "BUNDLE_VIDEO";

    @BindView(R2.id.player)
    CosmosPlayer player;
    @BindView(R2.id.playerContainer)
    FrameLayout playerContainer;
    @BindView(R2.id.ivPlayIcon)
    ImageView ivPlayIcon;
    @BindView(R2.id.progress)
    ProgressBar progressView;
    @BindView(R2.id.ivPlay)
    ImageView ivPlay;
    @BindView(R2.id.tvTime)
    TextView tvTime;
    @BindView(R2.id.tvTotalT)
    TextView tvTotalT;

    private boolean startPlay;
    private CustomRunnable customRunnable;
    //    private int progress;
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
        player.setLoopPlay(false);
        player.setOnStateChangedListener(new ICosPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(int i) {
                LogUtils.log(TAG, i + "");
                if (i == ICosPlayer.STATE_ENDED) {
                    lastProgress();
                    startPlay = false;
                    ivPlayIcon.setVisibility(View.VISIBLE);
                    ivPlay.setImageResource(R.drawable.video_preview_play);
                } else if (i == ICosPlayer.STATE_PREPARED) {
                    startProgress();
                }
            }
        });
    }

    @OnClick(R2.id.ivPlayIcon)
    public void onPlayIcon() {
        onPlayIconInner();
    }

    private boolean onPlayIconInner() {
        if (player.isPlaying()) {
            ivPlayIcon.setVisibility(View.VISIBLE);
            ivPlay.setImageResource(R.drawable.video_preview_play);
            player.pause();
            cancelProgress();
            return false;
        } else {
            if (startPlay) {
                player.resume();
                startProgress();
            } else {
                player.playVideo(videoInfo.path);
                startPlay = true;
            }
            ivPlayIcon.setVisibility(View.GONE);
            ivPlay.setImageResource(R.drawable.video_preview_pause);
            return true;
        }
    }

    @OnClick(R2.id.ivPlay)
    public void onBottomPlayClick() {
        if (onPlayIconInner()) {
//            startProgress();
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

    private void lastProgress(){
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cancelProgress();
        progressView.setProgress(100);
        tvTime.setText(Utils.videoTime((player.getDuration()/1000)));
    }

    private void startProgress() {
        if (customRunnable != null)
            return;
        tvTotalT.setText(Utils.videoTime(player.getDuration()/1000));
        int progress = getCurrentProgress();
        progressView.setProgress(progress);
        tvTime.setText(Utils.videoTime(progress));
        customRunnable = new CustomRunnable.Builder()
                .runnable(new Runnable() {
                    @Override
                    public void run() {
                        int progress = getCurrentProgress();
                        progressView.setProgress(progress);
                        tvTime.setText(Utils.videoTime(progress));
                        if (progress >= (player.getDuration() / 1000)) {
                            customRunnable.setCanceled(true);
                            customRunnable = null;
                        }
                    }
                })
                .delayTime(1000)
                .repeated(true)
                .build();
        MainLooperExecuteUtil.getInstance().post(customRunnable);
    }

    private int getCurrentProgress() {
        return (int) (player.getCurrentPosition() / 1000);
    }

    private void cancelProgress() {
        MainLooperExecuteUtil.getInstance().cancelRunnable(customRunnable);
        customRunnable = null;
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
