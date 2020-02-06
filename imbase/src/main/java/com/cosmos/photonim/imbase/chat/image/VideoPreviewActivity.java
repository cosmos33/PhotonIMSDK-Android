package com.cosmos.photonim.imbase.chat.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.chat.media.video.VideoInfo;

public class VideoPreviewActivity extends BaseActivity {
    private static final String EXTRA_VIDEO = "EXTRA_VIDEO";

    public static void startActivity(Activity context, VideoInfo videoInfo) {
        Intent intent = new Intent(context, VideoPreviewActivity.class);
        intent.putExtra(EXTRA_VIDEO, videoInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);

        VideoPreviewFragment videoPreviewFragment = new VideoPreviewFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoPreviewFragment.BUNDLE_VIDEO, getIntent().getSerializableExtra(EXTRA_VIDEO));
        videoPreviewFragment.setArguments(bundle);

        replaceFragment(R.id.llContainer, videoPreviewFragment);
    }
}
