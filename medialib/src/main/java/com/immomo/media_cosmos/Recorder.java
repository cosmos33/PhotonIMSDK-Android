package com.immomo.media_cosmos;

import android.app.Application;
import android.content.Context;

import com.mm.mediasdk.IMultiRecorder;
import com.mm.mediasdk.MoMediaManager;

public class Recorder {
    private IMultiRecorder recorder;

    public void init(Application context, String appid) {
        MoMediaManager.init(context, appid);
        recorder = MoMediaManager.createRecorder();
    }

    private void prepare(Context context, MediaConfig mediaConfig) {
//        recorder.prepare(this, getConfig());
    }


}
