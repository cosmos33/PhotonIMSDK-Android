package com.cosmos.photonim.imbase.chat.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.BaseActivity;

public class TakePhotoActivity extends BaseActivity {
    public static final int REQUEST_CAMERA = 1000;
    private TakePhotoFragment takePhotoFragment;
    private TakePhotoResultFragment takePhotoResultFragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TakePhotoActivity.class);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_takephoto_activity);
        init();
    }

    private void init() {
        takePhotoFragment = (TakePhotoFragment) replaceNewFragment(R.id.flContainer, "com.cosmos.photonim.imbase.chat.media.TakePhotoFragment");
        takePhotoFragment.setOnChangeFragmentListener(new OnChangeToResultFragmentListener() {
            @Override
            public void onChangeToResultFragment(Bundle args) {
                if (takePhotoResultFragment == null) {
                    takePhotoResultFragment = (TakePhotoResultFragment) replaceNewFragment(R.id.flContainer, "com.cosmos.photonim.imbase.chat.media.TakePhotoResultFragment", args);
                    takePhotoResultFragment.setOnChangeFragmentListener(new OnReturnFragmentListener() {

                        @Override
                        public void onDoneClick(String result) {
                            Intent intent = new Intent();
                            intent.putExtra(TakePhotoResultFragment.BUNDLE_PHOTO_PATH, result);
                            setResult(Activity.RESULT_OK, intent);
                            TakePhotoActivity.this.finish();
                        }

                        @Override
                        public void onReturnFragment(Bundle args) {
                            replaceFragment(R.id.flContainer, takePhotoFragment);
                        }
                    });
                } else {
                    replaceFragment(R.id.flContainer, takePhotoResultFragment);
                }
            }
        });
    }

    public interface OnChangeToResultFragmentListener {
        void onChangeToResultFragment(Bundle args);
    }

    public interface OnReturnFragmentListener {
        void onReturnFragment(Bundle args);

        void onDoneClick(String result);
    }
}
