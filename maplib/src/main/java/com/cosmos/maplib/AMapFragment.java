package com.cosmos.maplib;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cosmos.maplib.map.IMapFragment;
import com.cosmos.maplib.map.MapInfo;
import com.cosmos.maplib.map.MyLatLng;

public class AMapFragment extends MapFragment implements IMapFragment {
    public static final String EXTRA_LATLNG = "EXTRA_LATLNG";
    private static final String TAG = "AMapFragment";
    private boolean initMarker;
    private AMap.OnMyLocationChangeListener onMyLocationChangeListener;
    //    private Marker myLocationMarker;
    private Marker locationMarker;
    private MapInfo mapInfo;
    private double[] positionArray;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (getArguments() != null) {
            positionArray = getArguments().getDoubleArray(EXTRA_LATLNG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        locateMyPositon();
    }

    private void locateMyPositon() {
        AMap aMap = getMap();
        if (aMap == null || mapInfo == null) {
            return;
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        if (positionArray != null && positionArray.length == 2) {//给定坐标定位
            initMarker(positionArray[0], positionArray[1], false);
        } else {
            aMap.setOnMyLocationChangeListener(getMyLocationListener());
        }
    }

    private AMap.OnMyLocationChangeListener getMyLocationListener() {
        if (onMyLocationChangeListener == null) {
            onMyLocationChangeListener = new AMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if (!initMarker) {
                        MyLocationStyle myLocationStyle = getMap().getMyLocationStyle();
                        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
                        getMap().setMyLocationStyle(myLocationStyle);
                        initMarker(location.getLatitude(), location.getLongitude(), true);
                    }
                    initMarker = true;
                }
            };
        }
        return onMyLocationChangeListener;
    }

    private void initMarker(double latitude, double longitude, boolean draggable) {
//        LatLng latLng = new LatLng(latitude, longitude);
        AMap map = getMap();
        if (map == null) {
            Log.v(TAG, "map == null");
            return;
        }
//        myLocationMarker = map.addMarker(new MarkerOptions().position(latLng).draggable(false).snippet("DefaultMarker"));

        if (mapInfo != null && mapInfo.getLocationDrawableId() != -1) {
            MarkerOptions markerOption = new MarkerOptions();
            LatLng latLng = new LatLng(latitude, longitude);
            markerOption.position(latLng);

            map.animateCamera(CameraUpdateFactory.newLatLng(latLng), 1000, null);
            markerOption.draggable(draggable);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), mapInfo.getLocationDrawableId())));
            markerOption.setFlat(true);//设置marker平贴地图效果
            locationMarker = map.addMarker(markerOption);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (locationMarker != null) {
            locationMarker.destroy();
        }
        initMarker = false;
    }

    @Override
    public void setMapInfo(MapInfo mapInfo) {
        this.mapInfo = mapInfo;
        locateMyPositon();
    }

    @Override
    public MyLatLng getLocationLatLng() {
        if (locationMarker == null) {
            return null;
        }
        return new MyLatLng(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude);
    }
}
