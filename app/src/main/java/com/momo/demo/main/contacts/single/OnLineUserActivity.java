package com.momo.demo.main.contacts.single;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.R;

import butterknife.BindView;

public class OnLineUserActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlineuser);
        initView();
    }

    private void initView() {
        titleBar.setTitle("附近在线的人");
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> OnLineUserActivity.this.finish());
    }

}
