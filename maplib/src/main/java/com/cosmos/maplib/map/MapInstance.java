package com.cosmos.maplib.map;

import android.content.Context;

public class MapInstance implements IMap {
    private IMap iMap;
    private IMapFactory iMapFactory;

    private MapInstance() {
    }

    private static class MapInstanceHold {
        static MapInstance mapInstance = new MapInstance();
    }

    public static MapInstance getInstance() {
        return MapInstanceHold.mapInstance;
    }

    public void setMapFactory(IMapFactory iMapFactory) {
        this.iMapFactory = iMapFactory;
    }

    @Override
    public void init(Context context) {
        checkMap();
        iMap.init(context);
    }

    private void checkMap() {
        if (iMapFactory != null) {
            iMap = iMapFactory.createMap();
        } else {
            throw new IllegalStateException("map factory need");
        }
    }


    @Override
    public void startLocationOnce(OnLocateListener onLocateListener) {
        checkMap();
        checkInit();
        iMap.startLocationOnce(onLocateListener);
    }

    @Override
    public void stopLocatation() {
        checkMap();
        checkInit();
        iMap.stopLocatation();
    }

    private void checkInit() {
        if (iMap == null) {
            throw new IllegalStateException("map need init first");
        }
    }
}
