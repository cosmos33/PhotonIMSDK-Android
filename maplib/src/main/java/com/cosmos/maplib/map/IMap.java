package com.cosmos.maplib.map;

import android.content.Context;

public interface IMap {
    void init(Context context);

    void startLocationOnce(OnLocateListener onLocateListener);

    void stopLocatation();
}
