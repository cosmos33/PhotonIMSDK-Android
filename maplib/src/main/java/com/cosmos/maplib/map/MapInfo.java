package com.cosmos.maplib.map;

public class MapInfo {
    private int locationDrawableId;

    private MapInfo(Builder builder) {
        locationDrawableId = builder.locationDrawableId;
    }

    public int getLocationDrawableId() {
        return locationDrawableId;
    }

    public static final class Builder {
        private int locationDrawableId = -1;

        public Builder() {
        }

        public Builder locationDrawableId(int val) {
            locationDrawableId = val;
            return this;
        }

        public MapInfo build() {
            return new MapInfo(this);
        }
    }
}
