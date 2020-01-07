package com.immomo.media_cosmos;

import android.app.Application;

import com.mm.mediasdk.MoMediaManager;

public class RecorderFactory {
    public static void init(Application context, String appId) {
        MoMediaManager.init(context, appId);
    }

    public static IRecorder createRecorder() {
        return new CosmosRecorder();
    }
}
