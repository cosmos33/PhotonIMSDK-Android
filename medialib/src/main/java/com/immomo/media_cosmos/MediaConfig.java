package com.immomo.media_cosmos;

import android.util.Size;

public class MediaConfig {
    private int cameraId;
    private Size encodeSize;
    private Size targetVideoSize;

    private MediaConfig(Builder builder) {
        cameraId = builder.cameraId;
        encodeSize = builder.encodeSize;
        targetVideoSize = builder.targetVideoSize;
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

    public static final class Builder {
        private int cameraId;
        private Size encodeSize;
        private Size targetVideoSize;

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

        public MediaConfig build() {
            return new MediaConfig(this);
        }
    }
}
