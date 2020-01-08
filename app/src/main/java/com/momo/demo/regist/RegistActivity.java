package com.momo.demo.regist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.cosmos.photonim.imbase.base.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.momo.demo.R;
import com.momo.demo.login.LoginActivity;
import com.momo.demo.regist.iregist.IRegistView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends IRegistView {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvRegist)
    TextView tvRegist;
    private boolean isShowPwd = false;
    private ProcessDialogFragment processDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2019-08-03 已登录状态记录以及直接跳转
        setContentView(R.layout.activity_regist);
        initView();
        initEvent();
    }

    private void initView() {
        tvRegist.setEnabled(false);
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
            tvRegist.setEnabled(true);
        } else {
            tvRegist.setEnabled(false);
        }
    }

    @Override
    public void showDialog() {
        processDialogFragment = ProcessDialogFragment.getInstance();
    }

    @Override
    public void hideDialog() {
        if (processDialogFragment != null) {
            processDialogFragment.dismiss();
            processDialogFragment = null;
        }
    }

    @OnClick(R.id.tvRegist)
    public void onRegistClick() {
        presenter.onRegistClick(etUserName.getText().toString().trim(), etPwd.getText().toString().trim(), this);
    }

    @OnClick(R.id.tvToLogin)
    public void onToLogin() {
        finish();
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
    public IPresenter getIPresenter() {
        return new RegistPresenter(this);
    }

    @Override
    public void onRegistResult(JsonResult result) {
        ToastUtils.showText(this, String.format("注册:%s", result.success() ? "成功" : "失败"));

        if (result.success()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
