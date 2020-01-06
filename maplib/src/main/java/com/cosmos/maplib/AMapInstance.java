package com.cosmos.maplib;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cosmos.maplib.map.IMap;
import com.cosmos.maplib.map.LocationInfo;
import com.cosmos.maplib.map.OnLocateListener;

import java.util.HashSet;

public class AMapInstance implements IMap {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private HashSet<OnLocateListener> onLocateListeners;

    public void init(Context context) {
        if (mLocationClient != null) {
            return;
        }
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        mLocationClient.setLocationListener(mAMapLocationListener);
    }

    public void startLocationOnce(OnLocateListener onLocateListener) {
        if (onLocateListeners == null) {
            onLocateListeners = new HashSet<>();
        }
        onLocateListeners.add(onLocateListener);
        checkInit();
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void stopLocatation() {
        checkInit();
        mLocationClient.stopLocation();
    }

    private void checkInit() {
        if (mLocationClient == null) {
            throw new IllegalStateException("need init first");
        }
    }

    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (onLocateListeners != null) {
                    LocationInfo locationInfo = new LocationInfo.Builder()
                            .locationResult(amapLocation.getErrorCode() == 0)
                            .locationError(amapLocation.getErrorInfo())
                            .latitude(amapLocation.getLatitude())
                            .longitude(amapLocation.getLongitude()).build();
                    for (OnLocateListener onLocateListener : onLocateListeners) {
                        onLocateListener.onLocationChanged(locationInfo);
                    }
                }
            }
        }
    };
}
