package com.immomo.media_cosmos;

import android.app.Activity;
import android.view.SurfaceHolder;

public interface IRecorder {
    void prepare(Activity context, MediaConfig mediaConfig);

    void setVisualSize(int width, int height);

    void setPreviewDisplay(SurfaceHolder surfaceHolder);

    void startPreview();

    void switchCamera();

    boolean setFlashStatus(boolean on);

    void stopPreview();

    void release();

    void takePhoto(String photoSavePath, ITakePhotoCallBack iTakePhotoCallBack);
}
