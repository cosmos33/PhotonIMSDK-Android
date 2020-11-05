package com.cosmos.maplib;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cosmos.maplib.map.MyLocation;

public class GeocodeHelper {
    private GeocodeSearch geocoderSearch;
    private GeocodeListener geocodeListener;
    private MyLocation requestLocation;

    private static class GeocodeHelperHolder {
        static GeocodeHelper geocodeHelper = new GeocodeHelper();
    }

    public static GeocodeHelper getInstance() {
        return GeocodeHelperHolder.geocodeHelper;
    }

    private GeocodeHelper() {

    }

    public void init(Context context) {
        geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
                if (geocodeListener != null) {
                    if (code == 1000) {
                        RegeocodeAddress geocodeAddress = regeocodeResult.getRegeocodeAddress();
                        MyLocation myLocation = new MyLocation();
                        myLocation.lat = requestLocation.lat;
                        myLocation.lng = requestLocation.lng;
                        myLocation.address = geocodeAddress.getFormatAddress();
                        myLocation.detailedAddress = geocodeAddress.getFormatAddress();
                        geocodeListener.onGeocodeResult(true, myLocation);
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {

            }
        });
    }

    public void search(MyLocation location, GeocodeListener geocodeListener) {
        this.requestLocation = location;
        this.geocodeListener = geocodeListener;
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.lat, location.lng), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    public interface GeocodeListener {
        void onGeocodeResult(boolean success, MyLocation myLocation);
    }
}
