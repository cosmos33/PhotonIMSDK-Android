package com.cosmos.photonim.imbase.chat.media.video;

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
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.immomo.media_cosmos.IRecordFinishListener;
import com.immomo.media_cosmos.IRecorder;
import com.immomo.media_cosmos.MediaConfig;
import com.immomo.media_cosmos.RecorderFactory;
import com.immomo.moment.mediautils.VideoDataRetrieverBySoft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoRecordFragment extends BaseFragment {
    private static final int RECORD_MAX_TIME = 3 * 60 * 1000;
    @BindView(R2.id.title)
    TitleBar title;
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_record;
    }

    @Override
    protected void initView(View view) {
        title.setTitle("");
        title.setLeftImageEvent(R.drawable.chat_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
                tvTime.setText(String.format("%d:%d(最长录制3分钟)", 0, 0));
                if (start) {
                    setEnable(false);
                    customRunnable = new CustomRunnable.Builder()
                            .runnable(new Runnable() {
                                int count;

                                @Override
                                public void run() {
                                    count += 1;
                                    tvTime.setText(String.format("%d:%d(最长录制3分钟)", count / 60, count % 60));
                                    if (count * 1000 >= RECORD_MAX_TIME) {
                                        customRunnable.setCanceled(true);
                                        stopRecord();
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
                    customRunnable.setCanceled(true);
                    stopRecord();
                }
            }
        });
        tvTime.setText(String.format("%d:%d(最长录制3分钟)", 0, 0));
    }

    private void createRecorder() {
        recorder = RecorderFactory.createRecorder();
        recorder.prepare(getActivity(), getConfig());
    }

    private void setEnable(boolean enable) {
        ivSwitch.setEnabled(enable);
    }

    private void stopRecord() {
        recorder.pauseRecord();
        recorder.finishRecord(new IRecordFinishListener() {
            @Override
            public void onRecordFinish(String error) {
                if (!TextUtils.isEmpty(error)) {
                    ToastUtils.showText(error);
                } else {
                    ToastUtils.showText("保存成功");
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
            public String call() {
                long frames = 2;// TODO: 2020-01-07 获取帧数
                for (int i = 0; i < frames; i++) {
                    VideoDataRetrieverBySoft.Node node = new VideoDataRetrieverBySoft.Node(i * 1000 / 15, 0);//注意此处的参数是微秒
                    videoNodes.add(node);
                }
                videoDataRetrieve.init(videoPath);
                videoDataRetrieve.getImageByList(videoNodes);
                File file = new File(FileUtils.getVideoCoverPath(), videoName);
                FileUtils.createFile(file);
                FileUtils.saveBitmap(file.getAbsolutePath(), videoNodes.get(0).bmp);
                return file.getAbsolutePath();
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (processDialogFragment != null) {
                    processDialogFragment.dismiss();
                }
                recorder.release();
                if (onChangeFragmentListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(RecordResultFragment.BUNDLE_VIDEO_PATH, videoPath);
                    bundle.putString(RecordResultFragment.BUNDLE_VIDEO_COVER_PATH, (String) result);
                    onChangeFragmentListener.onChangeToResultFragment(bundle);
                }
            }
        });
    }

    private MediaConfig getConfig() {
        return new MediaConfig.Builder()
                .cameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .encodeSize(new Size(1280, 720))
                .targetVideoSize(new Size(1280, 720))
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
