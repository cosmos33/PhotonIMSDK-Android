package com.immomo.media_cosmos;

import android.widget.ImageView;

public interface IPlayer {
    void playVideo(String path);

    boolean isPlaying();

    void pause();

    void resume();

    void seekTo(long time);

    long getDuration();

    long getCurrentPosition();

    void releaseVideo();

    ImageView getCoverView();
}
