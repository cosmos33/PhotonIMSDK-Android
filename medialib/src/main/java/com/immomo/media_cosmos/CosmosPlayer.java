package com.immomo.media_cosmos;

import android.content.Context;
import android.util.AttributeSet;

import com.mm.player.VideoView;
import com.mm.player.scale.ScalableType;

public class CosmosPlayer extends VideoView implements IPlayer {
    public CosmosPlayer(Context context) {
        super(context);
        init();
    }


    public CosmosPlayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setScaleType(ScalableType.CENTER_CROP);
    }
}
