package com.momo.demo.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.push.PhotonPushManager;
import com.cosmos.photon.push.msg.MoMessage;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.event.IMStatus;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.momo.demo.MyApplication;
import com.momo.demo.R;
import com.momo.demo.login.ilogin.ILoginView;
import com.momo.demo.main.MainActivity;
import com.momo.demo.regist.RegistActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends ILoginView implements MyApplication.PushTokenObserver {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    private ProcessDialogFragment processDialogFragment;
    private boolean isShowPwd = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        // push
        PhotonPushManager.getInstance().logPushClick(getIntent());

        MyApplication.registerPushTokenObserver(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.RECORD_AUDIO
                    , Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
                    , Manifest.permission.CAMERA}, 101);
        }
    }

    private void initView() {
        tvLogin.setEnabled(false);
    }

    private void initEvent() {
        etUserName.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkNull();
        }
    };

    private void checkNull() {
        if (!TextUtils.isEmpty(etUserName.getText().toString())
                && !TextUtils.isEmpty(etPwd.getText().toString())) {
            tvLogin.setEnabled(true);
        } else {
            tvLogin.setEnabled(false);
        }
    }

    @OnClick(R.id.tvLogin)
    public void onLoginClick() {
        presenter.onLoginClick(etUserName.getText().toString().trim(), etPwd.getText().toString().trim());
    }

    @OnClick(R.id.tvToRegist)
    public void onToRegistClick() {
        startActivity(new Intent(this, RegistActivity.class));
    }

    @OnClick(R.id.ivSeePwd)
    public void onSeePwdClick() {
        isShowPwd = !isShowPwd;
        if (isShowPwd) {
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    @Override
    public void showDialog() {
        processDialogFragment = ProcessDialogFragment.getInstance();
        processDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        if (processDialogFragment != null) {
            processDialogFragment.dismiss();
            processDialogFragment = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthSuccess(IMStatus imStatus) {
        hideDialog();
        switch (imStatus.status) {
            case PhotonIMClient.IM_STATE_AUTH_SUCCESS:
                PhotonPushManager.getInstance().registerWithAlias(LoginInfo.getInstance().getUserId());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case PhotonIMClient.IM_STATE_AUTH_FAILED:
                ToastUtils.showText(this, "鉴权失败");
                break;
        }
    }
    @Override
    public IPresenter getIPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onReceiveToken(String token) {
        ToastUtils.showText(this, "token" + token);
    }

    @Override
    public void onReceiveMessage(MoMessage moMessage) {
        ToastUtils.showText(this, "偷穿消息" + moMessage.text);
    }
}
