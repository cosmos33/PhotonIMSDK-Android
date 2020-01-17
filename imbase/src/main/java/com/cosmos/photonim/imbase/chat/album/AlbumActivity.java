package com.cosmos.photonim.imbase.chat.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.view.TitleBar;

import butterknife.BindView;

public class AlbumActivity extends BaseActivity {
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    private AlbumFragment albumFragment;

    public static void start(FragmentActivity activity) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_IMAGE_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initView();
    }

    private void initView() {
        albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentById(R.id.albumFragment);
        titleBar.setTitle("所有照片");
        titleBar.setLeftImageEvent(R.drawable.arrow_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.this.finish();
            }
        });

        titleBar.setRightTextEvent("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        albumFragment.onActivityResult(requestCode, resultCode, data);
    }
}
