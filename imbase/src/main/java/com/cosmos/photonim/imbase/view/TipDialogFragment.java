package com.cosmos.photonim.imbase.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TipDialogFragment extends DialogFragment {
    @BindView(R2.id.tvTitle)
    TextView tvTitle;
    @BindView(R2.id.tvContent)
    TextView tvContent;
    @BindView(R2.id.tvCancel)
    TextView tvCancel;
    @BindView(R2.id.tvConfirm)
    TextView tvConfirm;
    private String title;
    private String content;
    private String cancel = "取消";
    private String confirm = "确定";
    private OnDialogClickListener onDialogClickListener;

    public static TipDialogFragment getInstance(String title, String content, String cancel, String confirm,
                                                OnDialogClickListener onDialogClickListener) {
        TipDialogFragment tipFragment = new TipDialogFragment();
        tipFragment.title = title;
        tipFragment.cancel = cancel;
        tipFragment.confirm = confirm;
        tipFragment.content = content;
        tipFragment.onDialogClickListener = onDialogClickListener;
        return tipFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tips, container);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        ButterKnife.bind(this, view);
        tvTitle.setText(title);
        tvConfirm.setText(confirm);
        tvContent.setText(content);
        tvCancel.setText(cancel);
        return view;
    }

    @OnClick(R2.id.tvConfirm)
    public void onConfirmClick() {
        if (onDialogClickListener != null) {
            onDialogClickListener.onConfirmClick();
        }
    }

    @OnClick(R2.id.tvCancel)
    public void onCancelClick() {
        if (onDialogClickListener != null) {
            onDialogClickListener.onCancelClick();
        }
    }

    public interface OnDialogClickListener {
        void onConfirmClick();

        void onCancelClick();
    }

}
