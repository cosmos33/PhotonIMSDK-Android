package com.immomo.media_cosmos;

import android.app.Activity;
import android.view.SurfaceHolder;

import com.core.glcore.config.MRConfig;
import com.core.glcore.config.Size;
import com.immomo.moment.config.MRecorderActions;
import com.immomo.moment.recorder.MomoRecorder;
import com.mm.mediasdk.IMultiRecorder;
import com.mm.mediasdk.MoMediaManager;

public class CosmosRecorder implements IRecorder {
    private IMultiRecorder recorder;

    public CosmosRecorder() {
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
        mrConfig.setVideoEncodeBitRate(mediaConfig.getVideoEncodeBitRate());
        mrConfig.setAudioChannels(mediaConfig.getAudioChannels());
        mrConfig.setVideoFPS(mediaConfig.getVideoFPS());
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

    @Override
    public void setVideoOutPath(String path) {
        recorder.setMediaOutPath(path);
    }

    @Override
    public void startRecord() {
        recorder.startRecording();
    }

    @Override
    public void cancelRecord() {
        recorder.cancelRecording();
    }

    @Override
    public void pauseRecord() {
        recorder.pauseRecording();
    }

    @Override
    public void finishRecord(final IRecordFinishListener iRecordFinishListener) {
        recorder.finishRecord(new MRecorderActions.OnRecordFinishedListener() {
            @Override
            public void onFinishingProgress(int i) {

            }

            @Override
            public void onRecordFinished() {
                if (iRecordFinishListener != null) {
                    iRecordFinishListener.onRecordFinish(null);
                }
            }

            @Override
            public void onFinishError(String s) {
                if (iRecordFinishListener != null) {
                    iRecordFinishListener.onRecordFinish(s);
                }
            }
        });
    }

    @Override
    public boolean isRecording() {
        return recorder.isRecording();
    }

    @Override
    public void setErrorListener(final IRecorderErrorListener iRecorderErrorListener) {
        recorder.setOnErrorListener(new MRecorderActions.OnErrorListener() {
            @Override
            public void onError(MomoRecorder momoRecorder, int i, int i1) {
                if (iRecorderErrorListener != null) {
                    iRecorderErrorListener.onError(i + "");
                }
            }
        });
    }
}
