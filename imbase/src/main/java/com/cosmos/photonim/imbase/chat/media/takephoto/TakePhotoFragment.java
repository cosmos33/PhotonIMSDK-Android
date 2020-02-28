package com.cosmos.photonim.imbase.chat.media.takephoto;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.core.glcore.util.BitmapPrivateProtocolUtil;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.media.OnChangeToResultFragmentListener;
import com.cosmos.photonim.imbase.utils.FileUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.immomo.media_cosmos.IRecorder;
import com.immomo.media_cosmos.ITakePhotoCallBack;
import com.immomo.media_cosmos.MediaConfig;
import com.immomo.media_cosmos.RecorderFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class TakePhotoFragment extends BaseFragment {
    //    @BindView(R2.id.title)
//    TitleBar title;
    @BindView(R2.id.surfaceView)
    SurfaceView surfaceView;

    private IRecorder recorder;
    private boolean flashStatus;
    private String photoPath;
    private OnChangeToResultFragmentListener onChangeFragmentListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_takephoto;
    }

    @Override
    protected void initView(View view) {
        init();
    }

    private void init() {
//        title.setTitle("");
//        title.setLeftImageEvent(R.drawable.chat_close, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getActivity() != null) {
//                    getActivity().finish();
//                }
//            }
//        });
        recorder = RecorderFactory.createRecorder();
//        recorder.prepare(getActivity(), getConfig());
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (recorder != null) {
                    recorder.prepare(getActivity(), getConfig());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                recorder.setVisualSize(width, height);
                recorder.setPreviewDisplay(holder);
                recorder.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if (recorder != null) {
            recorder.stopPreview();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (recorder != null) {
            recorder.release();
        }
        super.onDestroy();
    }

    private MediaConfig getConfig() {
        return new MediaConfig.Builder().
                cameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .encodeSize(new Size(1280, 720))
                .targetVideoSize(new Size(1280, 720))
                .videoFPS(20)
                .build();
    }

    @OnClick(R2.id.tvSwitch)
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

    @OnClick(R2.id.ivClose)
    public void onCloseClick() {
        Objects.requireNonNull(getActivity()).finish();
    }

    @OnClick(R2.id.tvTakePhoto)
    public void onTakePhoto() {
        String prefix = System.currentTimeMillis() + "";
        File file = new File(FileUtils.getPhotoPath(), String.format("%s.jpg", prefix));
        FileUtils.createFile(file);
        recorder.takePhoto(this.photoPath = file.getAbsolutePath(), new ITakePhotoCallBack() {
            @Override
            public void onTakePhotoResult(int error) {
                if (error != 0) {
                    ToastUtils.showText("出错了囧");
                } else {
                    //获取到的是加密的
                    String temp = photoPath.replace(prefix, String.format("%s_true", prefix));
                    try {
                        new File(temp).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileUtils.saveBitmap(temp,
                            BitmapPrivateProtocolUtil.getBitmap(photoPath));


                    if (onChangeFragmentListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(TakePhotoResultFragment.BUNDLE_PHOTO_PATH, temp);
                        onChangeFragmentListener.onChangeToResultFragment(bundle);
                    }
                    photoPath = temp;
                }
            }
        });
    }

    public void setOnChangeFragmentListener(OnChangeToResultFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }

}
