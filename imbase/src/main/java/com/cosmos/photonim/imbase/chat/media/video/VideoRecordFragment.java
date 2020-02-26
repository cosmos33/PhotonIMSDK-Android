package com.cosmos.photonim.imbase.chat.media.video;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.media.OnChangeToResultFragmentListener;
import com.cosmos.photonim.imbase.utils.FileUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.immomo.media_cosmos.IRecordFinishListener;
import com.immomo.media_cosmos.IRecorder;
import com.immomo.media_cosmos.MediaConfig;
import com.immomo.media_cosmos.RecorderFactory;
import com.immomo.moment.mediautils.VideoDataRetrieverBySoft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoRecordFragment extends BaseFragment {
    private static final int RECORD_MAX_TIME = 3 * 60 * 1000;
    private static final int INTERVAL = 1000;
    @BindView(R2.id.tvTime)
    TextView tvTime;
    @BindView(R2.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R2.id.tvTakePhoto)
    TouchTextView touchTextView;
    @BindView(R2.id.ivSwitch)
    ImageView ivSwitch;

    private boolean flashStatus;
    private OnChangeToResultFragmentListener onChangeFragmentListener;
    private IRecorder recorder;
    private CustomRunnable customRunnable;
    private String videoPath;
    private String videoName;
    private ProcessDialogFragment processDialogFragment;
    private String time;
    private int timeCount;
    private long lastPressTime;
    private boolean finishRecord;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_record;
    }

    @Override
    protected void initView(View view) {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                finishRecord = false;
                createRecorder();
                recorder.setVisualSize(width, height);
                recorder.setPreviewDisplay(holder);
                recorder.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        touchTextView.setOnTouchListener(new TouchTextView.OnTouchListener() {
            @Override
            public void onTouch(boolean start) {
                if (start && finishRecord) {
                    return;
                }
                if (start && (System.currentTimeMillis() - lastPressTime < INTERVAL)) {
                    ToastUtils.showText("操作太频繁");
                    return;
                }
                lastPressTime = System.currentTimeMillis();
                tvTime.setText("00:00(最长录制3分钟)");
                if (start) {
                    setEnable(false);
                    timeCount = 0;
                    customRunnable = new CustomRunnable.Builder()
                            .runnable(new Runnable() {

                                @Override
                                public void run() {
                                    timeCount += 1;
                                    tvTime.setText((time = Utils.videoTime(timeCount)) + "(最长录制3分钟)");
                                    if (timeCount * 1000 >= RECORD_MAX_TIME) {
                                        customRunnable.setCanceled(true);
                                        stopRecord();
                                    }
                                    if (customRunnable.isCanceled()) {
                                        tvTime.setText("00:00(最长录制3分钟)");
                                    }
                                }
                            })
                            .delayTime(1000)
                            .repeated(true).build();

                    MainLooperExecuteUtil.getInstance().post(customRunnable);

                    String videoPathTemp = FileUtils.getVideoPath();
                    File file = new File(videoPathTemp, (videoName = System.currentTimeMillis() + "") + ".mp4");
                    FileUtils.createFile(file);
                    recorder.setVideoOutPath(videoPath = file.getAbsolutePath());
                    recorder.startRecord();
                } else {
                    setEnable(true);
                    if (recorder.isRecording()) {
                        customRunnable.setCanceled(true);
                        stopRecord();
                    }
                }
            }
        });
        tvTime.setText("00:00(最长录制3分钟)");
    }

    private void createRecorder() {
        recorder = RecorderFactory.createRecorder();
        recorder.prepare(getActivity(), getConfig());
    }

    private void setEnable(boolean enable) {
        ivSwitch.setEnabled(enable);
    }

    private void stopRecord() {
        if (timeCount == 0) {
            ToastUtils.showText("录制时间太短，请重新录制");
            recorder.cancelRecord();
            return;
        }
        recorder.pauseRecord();
        recorder.finishRecord(new IRecordFinishListener() {
            @Override
            public void onRecordFinish(String error) {
                if (!TextUtils.isEmpty(error)) {
                    ToastUtils.showText(error);
                } else {
                    ToastUtils.showText("保存成功");
                    finishRecord = true;
                    getVideoCover();
                }
            }
        });
    }

    private void getVideoCover() {
        ToastUtils.showText("处理视频中请稍后...");
        processDialogFragment = new ProcessDialogFragment();
        processDialogFragment.show(getFragmentManager(), "");
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            List<VideoDataRetrieverBySoft.Node> videoNodes = new ArrayList<>();
            VideoDataRetrieverBySoft videoDataRetrieve = new VideoDataRetrieverBySoft();

            @Override
            public BmpInfo call() {
                long frames = 2;// TODO: 2020-01-07 获取帧数
                for (int i = 0; i < frames; i++) {
                    VideoDataRetrieverBySoft.Node node = new VideoDataRetrieverBySoft.Node(i * 1000 / 15, 0);//注意此处的参数是微秒
                    videoNodes.add(node);
                }
                videoDataRetrieve.init(videoPath);
                videoDataRetrieve.getImageByList(videoNodes);
                File file = new File(FileUtils.getVideoCoverPath(), videoName);
                FileUtils.createFile(file);
                Bitmap bmp = videoNodes.get(0).bmp;
                FileUtils.saveBitmap(file.getAbsolutePath(), bmp);
                return new BmpInfo(file.getAbsolutePath(), bmp.getWidth(), bmp.getHeight());
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (processDialogFragment != null) {
                    processDialogFragment.dismiss();
                }
                recorder.release();
                if (onChangeFragmentListener != null) {
                    BmpInfo bmpInfo = (BmpInfo) result;
                    if (bmpInfo == null) {
                        ToastUtils.showText("封面生成失败");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.videoTime = timeCount;
                    videoInfo.path = videoPath;
                    videoInfo.width = bmpInfo.width;
                    videoInfo.height = bmpInfo.height;
                    videoInfo.videoCoverPath = bmpInfo.path;
                    bundle.putSerializable(RecordResultFragment.BUNDLE_VIDEO, videoInfo);
//                    if (recorder.getRotateDegree() / 90 % 2 == 0) {
//                    } else {
//                        bundle.putInt(RecordResultFragment.BUNDLE_VIDEO_COVER_WIDTH, bmpInfo.height);
//                        bundle.putInt(RecordResultFragment.BUNDLE_VIDEO_COVER_HEIGHT, bmpInfo.width);
//                    }
                    onChangeFragmentListener.onChangeToResultFragment(bundle);
                }
            }
        });
    }

    class BmpInfo {
        String path;
        int width;
        int height;

        public BmpInfo(String absolutePath, int width, int height) {
            this.path = absolutePath;
            this.width = width;
            this.height = height;
        }
    }


    private MediaConfig getConfig() {
        return new MediaConfig.Builder()
                .cameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .encodeSize(new Size(720, 1280))
                .targetVideoSize(new Size(720, 1280))
                .videoEncodeBitRate(8 << 20)
                .audioChannels(1)
                .videoFPS(20)
                .build();
    }

    @Override
    public void onPause() {
        recorder.stopPreview();
        recorder.cancelRecord();
        super.onPause();
    }

    @OnClick(R2.id.tvClose)
    public void onCloseClick() {
        Objects.requireNonNull(getActivity()).finish();
    }
    @OnClick(R2.id.ivSwitch)
    public void onSwitchCameraClick() {
        recorder.switchCamera();
    }

    @OnClick(R2.id.tvFlashing)
    public void onFlashingClick() {
        flashStatus = !flashStatus;
        boolean result = recorder.setFlashStatus(flashStatus);
        if (!result) {
            ToastUtils.showText("不支持");
        }
    }

    public void setOnChangeFragmentListener(OnChangeToResultFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }

    @Override
    public void onDestroy() {
        if (recorder != null) {
            recorder.cancelRecord();
            recorder.release();
        }
        super.onDestroy();
    }
}
