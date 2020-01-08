package com.cosmos.photonim.imbase.chat.map;

import com.cosmos.maplib.map.MyLatLng;
import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.cosmos.photonim.imbase.chat.map.imap.IMapPresenter;
import com.cosmos.photonim.imbase.chat.map.imap.IMapView;

public class MapPresenter extends IMapPresenter {
    public MapPresenter(IMapView iView) {
        super(iView);
    }

    @Override
    public void sendPosition(MyLatLng myLatLng) {

    }

    @Override
    public IModel generateIModel() {
        return new MapModel();
    }
}
