package com.immomo.media_cosmos;

import android.app.Activity;
import android.view.SurfaceHolder;

import com.core.glcore.config.MRConfig;
import com.core.glcore.config.Size;
import com.immomo.moment.config.MRecorderActions;
import com.mm.mediasdk.IMultiRecorder;
import com.mm.mediasdk.MoMediaManager;

public class Recorder implements IRecorder {
    private IMultiRecorder recorder;

    public Recorder() {
        recorder = MoMediaManager.createRecorder();
    }

    @Override
    public void prepare(Activity context, MediaConfig mediaConfig) {
        MRConfig mrConfig = MRConfig.obtain();
        mrConfig.setDefaultCamera(mediaConfig.getCameraId());
        Size size = new Size(mediaConfig.getEncodeSize().getWidth(), mediaConfig.getEncodeSize().getHeight());
        mrConfig.setEncodeSize(size);
        // 设置camera 的采集分辨率
        mrConfig.setTargetVideoSize(size);
        recorder.prepare(context, mrConfig);
    }

    @Override
    public void setVisualSize(int width, int height) {
        recorder.setVisualSize(width, height);
    }

    @Override
    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        recorder.setPreviewDisplay(surfaceHolder);
    }

    @Override
    public void startPreview() {
        recorder.startPreview();
    }

    @Override
    public void switchCamera() {
        recorder.switchCamera();
    }

    @Override
    public boolean setFlashStatus(boolean on) {
        if (!recorder.supportFlash()) {
            return false;
        }
        recorder.setFlashMode(on ? IMultiRecorder.FLASH_MODE_ON : IMultiRecorder.FLASH_MODE_OFF);
        return true;
    }

    @Override
    public void stopPreview() {
        recorder.stopPreview();
    }

    @Override
    public void release() {
        recorder.release();
    }

    @Override
    public void takePhoto(String photoSavePath, final ITakePhotoCallBack iTakePhotoCallBack) {
        recorder.takePhoto(photoSavePath, new MRecorderActions.OnTakePhotoListener() {
            @Override
            public void onTakePhotoComplete(int error, Exception e) {
                if (iTakePhotoCallBack != null) {
                    iTakePhotoCallBack.onTakePhotoResult(error);
                }
            }
        });
    }
}
