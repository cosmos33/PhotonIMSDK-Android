package com.cosmos.photonim.imbase.chat.file;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;
import com.cosmos.photonim.imbase.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class FileActivity extends BaseActivity {
    @BindView(R2.id.tvSize)
    TextView tvSize;

    private FileFragment fileFragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FileActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        fileFragment = (FileFragment) getSupportFragmentManager().findFragmentById(R.id.fileFragment);
        fileFragment.setOnFileCheckedListener(new FileFragment.OnFileCheckedListener() {
            @Override
            public void onFileCheckStatusChanged(String size) {
                tvSize.setText(size);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!fileFragment.popDirectory()) {
            super.onBackPressed();
        }
    }

    @OnClick(R2.id.tvSend)
    public void onSendClick() {
        ArrayList<FileItemData> checkedData = fileFragment.getCheckedData();
        if (checkedData.size() == 0) {
            ToastUtils.showText("未选中");
            return;
        }
        // TODO: 2020-01-20 发送
        this.finish();
    }
}