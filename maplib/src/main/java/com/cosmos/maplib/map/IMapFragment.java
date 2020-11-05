package com.cosmos.maplib.map;

import com.cosmos.maplib.GeocodeHelper;

public interface IMapFragment {
    void setMapInfo(MapInfo mapInfo);

    void getLocationLatLng(GeocodeHelper.GeocodeListener geocodeListener);
}
