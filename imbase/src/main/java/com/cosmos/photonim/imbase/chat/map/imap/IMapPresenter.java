package com.cosmos.photonim.imbase.chat.map.imap;


import com.cosmos.maplib.map.MyLatLng;
import com.cosmos.photonim.imbase.base.mvpbase.IPresenter;

public abstract class IMapPresenter<V extends IMapView, M extends IMapModel> extends IPresenter<V, M> {

    public IMapPresenter(V iView) {
        super(iView);
    }

    public abstract void sendPosition(MyLatLng myLatLng);

    @Override
    public V getEmptyView() {
        return (V) new IMapView() {
            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }
}
