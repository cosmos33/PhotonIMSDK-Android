package com.momo.demo.main.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.momo.demo.R;
import com.momo.demo.login.LoginActivity;
import com.momo.demo.main.me.ime.IMeView;
import com.momo.demo.view.ChangeNickNameDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends IMeView {
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChangeNickNameDialog dialog;
    private String nickName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_me, null);
        ButterKnife.bind(this, view);
        registPresenter.getMyInfo();
        initView();
        return view;
    }

    private void initView() {
        tvAccount.setText(ImBaseBridge.getInstance().getUserId());
        swipeRefreshLayout.setOnRefreshListener(() -> registPresenter.getMyInfo());
    }

    @OnClick(R.id.flNickName)
    public void onNickNameClick() {
        dialog = ChangeNickNameDialog.getInstance(new ChangeNickNameDialog.OnNickSetListener() {
            @Override
            public void onCancelClick() {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onComplete(String nickName) {
                if (TextUtils.isEmpty(nickName.trim())) {
                    ToastUtils.showText(getContext(), "昵称不能为空");
                } else if (MeFragment.this.nickName != null && MeFragment.this.nickName.equals(nickName.trim())) {
                    ToastUtils.showText(getContext(), "昵称未改变");
                } else {
                    registPresenter.changeNickName(nickName);
                }
            }
        }, nickName);
        dialog.show(getFragmentManager(), getResources().getString(R.string.fragment_tag_nickname));
    }

    @OnClick(R.id.tvLogout)
    public void onLogoutClick() {
        registPresenter.logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

//    @Override
//    public void onLogout() {
//        ImBaseBridge.getInstance().stopIm(getContext());
//    }

    @Override
    public void onChangeNickName(JsonSetNickName jsonResult) {
        if (jsonResult.success()) {
            registPresenter.getMyInfo();
            ToastUtils.showText(getContext(), "修改成功");
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

        }
    }

    @Override
    public void onGetMyInfo(JsonMyInfo jsonResult) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (jsonResult != null && jsonResult.success()) {
            if (TextUtils.isEmpty(jsonResult.getData().getProfile().getNickname())) {
                tvNickName.setText(getResources().getString(R.string.me_nickname_notset));
            } else {
                nickName = jsonResult.getData().getProfile().getNickname();
                tvNickName.setText(nickName);
            }
            String avatar = jsonResult.getData().getProfile().getAvatar();
            ImBaseBridge.getInstance().setMyIcon(avatar);
            ImBaseBridge.getInstance().setGids(jsonResult.getData().getJoinedGids());
            ImageLoaderUtils.getInstance().loadImage(getContext(), avatar, R.drawable.head_placeholder, ivIcon);
        } else {
            ToastUtils.showText(getContext(), "获取个人信息失败");
        }
    }

    @Override
    public IPresenter getIPresenter() {
        return new MePresenter(this);
    }
}
