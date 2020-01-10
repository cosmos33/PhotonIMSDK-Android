package com.momo.demo.main.contacts.single.userinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.R;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.single.userinfo.iuserinfo.IUserInfoView;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends IUserInfoView {
    public static final String ICON_URL = "ICON_URL";
    public static final String USER_ID = "USER_ID";
    public static final String NICK_NAME = "NICK_NAME";
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvAcount)
    TextView tvAcount;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tvSendMsg)
    TextView tvSendMsg;

    private String userId;
    private String userIcon;

    public static void startActivity(Activity from, String id, String icon, String nickName) {
        Intent intent = new Intent(from, UserInfoActivity.class);
        intent.putExtra(ICON_URL, icon);
        intent.putExtra(USER_ID, id);
        intent.putExtra(NICK_NAME, nickName);

        from.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);


        initView();
//        infoPresenter.getUserInfo(getIntent().getStringExtra(USER_ID));
    }

//    @Override
//    public void onGetUserInfo(JsonOtherInfo jsonOtherInfo) {
//        if (jsonOtherInfo != null && jsonOtherInfo.success()) {
//            tvAcount.setText(String.format(getResources().getString(R.string.userinfo_account), jsonOtherInfo.getData().getProfile().getUserId()));
//        } else {
//            ChatToastUtils.showText(this, getResources().getString(R.string.userinfo_get_failed));
//        }
//    }

    private void initView() {
        userId = getIntent().getStringExtra(USER_ID);
        if (userId.equals(LoginInfo.getInstance().getUserId())) {
            tvSendMsg.setVisibility(View.GONE);
        }
        titleBar.setTitle("");
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> UserInfoActivity.this.finish());
        tvAcount.setText(String.format(getResources().getString(R.string.userinfo_account), userId));
        userIcon = getIntent().getStringExtra(ICON_URL);
        ImageLoaderUtils.getInstance().loadImage(this, userIcon, R.drawable.head_placeholder, ivIcon);
        tvNickName.setText(String.format(getResources().getString(R.string.userinfo_nickname), getIntent().getStringExtra(NICK_NAME)));
    }

    @OnClick(R.id.tvSendMsg)
    public void onSendMsg() {
        ChatBaseActivity.startActivity(this, PhotonIMMessage.SINGLE, userId, null, userId, userIcon, false);
    }

    @Override
    public IPresenter getIPresenter() {
        return new UserInfoPresenter(this);
    }
}
