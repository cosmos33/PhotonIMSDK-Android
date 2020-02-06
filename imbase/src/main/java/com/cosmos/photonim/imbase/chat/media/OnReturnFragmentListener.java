package com.cosmos.photonim.imbase.chat.media;

import android.os.Bundle;

import com.cosmos.photonim.imbase.chat.media.video.VideoInfo;

public interface OnReturnFragmentListener {
    void onReturnFragment(Bundle args);

    void onDoneClick(VideoInfo videoInfo);
}
