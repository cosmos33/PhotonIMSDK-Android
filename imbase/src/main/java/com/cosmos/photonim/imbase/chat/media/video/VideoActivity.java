package com.cosmos.photonim.imbase.chat.media.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.chat.media.OnChangeToResultFragmentListener;
import com.cosmos.photonim.imbase.chat.media.OnReturnFragmentListener;

public class VideoActivity extends BaseActivity {
    public static final int REQUEST_VIDEO = 1002;
    private VideoRecordFragment videoRecordFragment;
    private RecordResultFragment recordResultFragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, VideoActivity.class);
        activity.startActivityForResult(intent, REQUEST_VIDEO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
    }

    private void initView() {
        videoRecordFragment = (VideoRecordFragment) replaceNewFragment(R.id.flContainer,
                "com.cosmos.photonim.imbase.chat.media.video.VideoRecordFragment");
        videoRecordFragment.setOnChangeFragmentListener(new OnChangeToResultFragmentListener() {
            @Override
            public void onChangeToResultFragment(Bundle args) {
                if (recordResultFragment == null) {
                    recordResultFragment = (RecordResultFragment) replaceNewFragment(R.id.flContainer,
                            "com.cosmos.photonim.imbase.chat.media.video.RecordResultFragment", args);
                    recordResultFragment.setOnChangeFragmentListener(new OnReturnFragmentListener() {

                        @Override
                        public void onDoneClick(String result) {
                            Intent intent = new Intent();
                            intent.putExtra(RecordResultFragment.BUNDLE_VIDEO_PATH, result);
                            setResult(Activity.RESULT_OK, intent);
                            VideoActivity.this.finish();
                        }

                        @Override
                        public void onReturnFragment(Bundle args) {
                            replaceFragment(R.id.flContainer, videoRecordFragment);
                        }
                    });
                } else {
                    recordResultFragment.setArguments(args);
                    replaceFragment(R.id.flContainer, recordResultFragment);
                }
            }
        });
    }
}