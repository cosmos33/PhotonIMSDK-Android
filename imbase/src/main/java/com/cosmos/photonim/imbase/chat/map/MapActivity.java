package com.cosmos.photonim.imbase.chat.map;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.cosmos.maplib.AMapFragment;
import com.cosmos.maplib.map.MapInfo;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.chat.map.imap.IMapView;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.view.TitleBar;

import butterknife.BindView;

public class MapActivity extends IMapView {
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    private AMapFragment mapFragment;
    private double[] positionArray;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MapActivity.class);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, double lat, double lng) {
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra(AMapFragment.EXTRA_LATLNG, new double[]{lat, lng});
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_map);

        positionArray = getIntent().getDoubleArrayExtra(AMapFragment.EXTRA_LATLNG);
        initView();
    }

    private void initView() {
        titleBar.setTitle(getResources().getString(R.string.map_title));
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> MapActivity.this.finish());

        mapFragment = new AMapFragment();
        if (positionArray != null && positionArray.length == 2) {
            Bundle bundle = new Bundle();
            bundle.putDoubleArray(AMapFragment.EXTRA_LATLNG, positionArray);
            mapFragment.setArguments(bundle);
            titleBar.setRightImageEvent(R.drawable.map_relay, v -> {
                // TODO: 2020-01-07 转发
            });
        } else {
            titleBar.setRightTextEvent("发送", 0xffffffff, R.drawable.drawable_map_send, v -> {
                presenter.sendPosition(mapFragment.getLocationLatLng());
            });
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, mapFragment);
        fragmentTransaction.commit();
        MapInfo mapInfo = new MapInfo.Builder().locationDrawableId(R.drawable.map_location).build();
        mapFragment.setMapInfo(mapInfo);
    }

    @Override
    public IPresenter getIPresenter() {
        return new MapPresenter(this);
    }
}
