package com.cosmos.photonim.imbase.chat.media.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.chat.media.OnChangeToResultFragmentListener;
import com.cosmos.photonim.imbase.chat.media.OnReturnFragmentListener;
import com.cosmos.photonim.imbase.utils.Constants;

public class TakePhotoActivity extends BaseActivity {

    private TakePhotoFragment takePhotoFragment;
    private TakePhotoResultFragment takePhotoResultFragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TakePhotoActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_takephoto_activity);
        init();
    }

    private void init() {
        takePhotoFragment = (TakePhotoFragment) replaceNewFragment(R.id.flContainer,
                "com.cosmos.photonim.imbase.chat.media.takephoto.TakePhotoFragment");
        takePhotoFragment.setOnChangeFragmentListener(new OnChangeToResultFragmentListener() {
            @Override
            public void onChangeToResultFragment(Bundle args) {
                if (takePhotoResultFragment == null) {
                    takePhotoResultFragment = (TakePhotoResultFragment) replaceNewFragment(R.id.flContainer,
                            "com.cosmos.photonim.imbase.chat.media.takephoto.TakePhotoResultFragment", args);
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
                    takePhotoResultFragment.setArguments(args);
                    replaceFragment(R.id.flContainer, takePhotoResultFragment);
                }
            }
        });
    }
}
