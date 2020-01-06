package com.cosmos.maplib.map;

public class LocationInfo {
    private boolean locationResult;
    private String locationError;
    private double latitude;
    private double longitude;

    private LocationInfo(Builder builder) {
        locationResult = builder.locationResult;
        locationError = builder.locationError;
        latitude = builder.latitude;
        longitude = builder.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isLocationResult() {
        return locationResult;
    }

    public String getLocationError() {
        return locationError;
    }

    public static final class Builder {
        private double latitude;
        private double longitude;
        private boolean locationResult;
        private String locationError;

        public Builder() {
        }

        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
            return this;
        }

        public LocationInfo build() {
            return new LocationInfo(this);
        }

        public Builder locationResult(boolean val) {
            locationResult = val;
            return this;
        }

        public Builder locationError(String val) {
            locationError = val;
            return this;
        }
    }
}
