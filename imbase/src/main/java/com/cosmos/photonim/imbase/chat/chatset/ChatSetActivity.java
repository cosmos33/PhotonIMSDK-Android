package com.cosmos.photonim.imbase.chat.chatset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.chatset.ichatset.IChatSetView;
import com.cosmos.photonim.imbase.chat.searchhistory.SearchHistoryActivity;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGetIgnoreInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;
import com.cosmos.photonim.imbase.view.TipDialogFragment;
import com.cosmos.photonim.imbase.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatSetActivity extends IChatSetView {
    private static final String EXTRA_USERID = "EXTRA_USERID";
    private static final String EXTRA_ICON = "EXTRA_ICON";
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.ivIcon)
    ImageView ivIcon;
    @BindView(R2.id.tvNickName)
    TextView tvNickName;
    @BindView(R2.id.sBan)
    Switch sBan;
    @BindView(R2.id.sTop)
    Switch sTop;

    private String userId;
    private String icon;
    private TipDialogFragment tipDialogFragment;
    private ProcessDialogFragment processDialogFragment;

    public static void startActivity(Activity activity, String userId, String icon) {
        Intent intent = new Intent(activity, ChatSetActivity.class);
        intent.putExtra(EXTRA_USERID, userId);
        intent.putExtra(EXTRA_ICON, icon);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatset);
        initView();
    }

    private void initView() {
        titleBar.setTitle(getResources().getString(R.string.chatset_title));
        titleBar.setLeftImageEvent(R.drawable.arrow_left, v -> ChatSetActivity.this.finish());

        userId = getIntent().getStringExtra(EXTRA_USERID);
        icon = getIntent().getStringExtra(EXTRA_ICON);


        ImageLoaderUtils.getInstance().loadImage(this, icon, R.drawable.head_placeholder, ivIcon);
        tvNickName.setText(userId);

        presenter.getIgnoreStatus(userId);
        presenter.getTopStatus(PhotonIMMessage.SINGLE, userId);
    }

    @OnClick(R2.id.sTop)
    public void onTopChange() {
        presenter.changeTopStatus(PhotonIMMessage.SINGLE, userId);
    }

    @OnClick(R2.id.sBan)
    public void onBanChange() {
        presenter.changeIgnoreStatus(userId, sBan.isChecked());
    }

    @OnClick(R2.id.flSearchContent)
    public void onSearchContent() {
        SearchHistoryActivity.start(this, userId, PhotonIMMessage.SINGLE);
    }

    @OnClick(R2.id.flClearChatContent)
    public void onClearChatContentClick() {
        tipDialogFragment = TipDialogFragment.getInstance("删除聊天记录",
                "删除后不可恢复，清谨慎操作",
                "取消",
                "删除", new TipDialogFragment.OnDialogClickListener() {
                    @Override
                    public void onConfirmClick() {
                        tipDialogFragment.dismiss();
                        presenter.clearChatContent(PhotonIMMessage.SINGLE, userId);
                        processDialogFragment = new ProcessDialogFragment();
                        processDialogFragment.show(getSupportFragmentManager(), "");
                    }

                    @Override
                    public void onCancelClick() {
                        tipDialogFragment.dismiss();
                    }
                });
        tipDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onIgnoreChangeStatusResult(boolean success) {
        ToastUtils.showText(this, success ? "成功" : "失败");
        if (!success) {
            sBan.setChecked(!sBan.isChecked());
        }
    }

    @Override
    public void onGetIgnoreStatus(JsonResult result) {
        if (!result.success()) {
            ToastUtils.showText(this, "获取勿扰设置失败");
            return;
        }
        JsonGetIgnoreInfo ignoreInfo = (JsonGetIgnoreInfo) result.get();
        sBan.setChecked(ignoreInfo.getData().getSwitchX() == 0);
    }

    @Override
    public void dimissProgressDialog() {
        if (processDialogFragment != null) {
            processDialogFragment.dismiss();
        }
    }

    @Override
    public void changeTopStatus(boolean top) {
        sTop.setChecked(top);
    }

    @Override
    public IPresenter getIPresenter() {
        return new ChatSetPresenter(this);
    }

    @Override
    protected void onDestroy() {
        dimissProgressDialog();
        if (tipDialogFragment != null) {
            tipDialogFragment.dismiss();
        }
        super.onDestroy();
    }
}
