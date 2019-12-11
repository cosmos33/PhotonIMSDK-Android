package com.momo.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cosmos.photon.push.PhotonPushManager;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.LocalRestoreUtils;
import com.momo.demo.login.LoginActivity;
import com.momo.demo.main.MainActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotonPushManager.getInstance().logPushClick(getIntent());
        if (!isTaskRoot()) {
            finish();
            return;
        }
        String[] auth = LocalRestoreUtils.getAuth();
        Intent intent;
        if (auth.length != 0 && !TextUtils.isEmpty(auth[0]) && !TextUtils.isEmpty(auth[1])) {
            intent = new Intent(this, MainActivity.class);
            ImBaseBridge.getInstance().setLoginInfo(auth[2], auth[1]);
            PhotonPushManager.getInstance().registerWithAlias(auth[1]);
            ImBaseBridge.getInstance().setTokenId(auth[0]);
            ImBaseBridge.getInstance().startIm();
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
