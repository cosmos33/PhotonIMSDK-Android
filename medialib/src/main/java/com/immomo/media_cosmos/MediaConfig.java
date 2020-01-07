package com.immomo.media_cosmos;

import android.hardware.Camera;
import android.util.Size;

public class MediaConfig {
    private int cameraId;
    private Size encodeSize;
    private Size targetVideoSize;
    private int videoEncodeBitRate;
    private int audioChannels;
    private int videoFPS;

    private MediaConfig(Builder builder) {
        cameraId = builder.cameraId;
        encodeSize = builder.encodeSize;
        targetVideoSize = builder.targetVideoSize;
        videoEncodeBitRate = builder.videoEncodeBitRate;
        audioChannels = builder.audioChannels;
        videoFPS = builder.videoFPS;
    }


    public int getCameraId() {
        return cameraId;
    }

    public Size getEncodeSize() {
        return encodeSize;
    }

    public Size getTargetVideoSize() {
        return targetVideoSize;
    }

    public int getVideoEncodeBitRate() {
        return videoEncodeBitRate;
    }

    public int getAudioChannels() {
        return audioChannels;
    }

    public int getVideoFPS() {
        return videoFPS;
    }

    public static final class Builder {
        private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        private Size encodeSize = new Size(1280, 720);
        private Size targetVideoSize;
        private int videoEncodeBitRate;
        private int audioChannels;
        private int videoFPS;

        public Builder() {
        }

        public Builder cameraId(int val) {
            cameraId = val;
            return this;
        }

        public Builder encodeSize(Size val) {
            encodeSize = val;
            return this;
        }

        public Builder targetVideoSize(Size val) {
            targetVideoSize = val;
            return this;
        }

        public Builder videoEncodeBitRate(int val) {
            videoEncodeBitRate = val;
            return this;
        }

        public Builder audioChannels(int val) {
            audioChannels = val;
            return this;
        }

        public Builder videoFPS(int val) {
            videoFPS = val;
            return this;
        }

        public MediaConfig build() {
            return new MediaConfig(this);
        }
    }
}
