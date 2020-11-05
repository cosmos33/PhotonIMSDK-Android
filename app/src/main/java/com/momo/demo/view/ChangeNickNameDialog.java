package com.momo.demo.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.momo.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeNickNameDialog extends DialogFragment {
    private int count;
    private static final int NICKNAME_COUNT = 20;
    @BindView(R.id.etNickName)
    EditText etNickName;
    @BindView(R.id.tvNickCount)
    TextView tvNickCount;
    private OnNickSetListener onNickSetListener;
    private String oldNickName;

    public static ChangeNickNameDialog getInstance(OnNickSetListener onNickSetListener, String oldNickName) {
        ChangeNickNameDialog dialog = new ChangeNickNameDialog();
        dialog.onNickSetListener = onNickSetListener;
        dialog.oldNickName = oldNickName;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_nickname, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        ButterKnife.bind(this, view);
        etNickName.setText(oldNickName);
        etNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NICKNAME_COUNT)});
        setNickNameCount();
        etNickName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setNickNameCount();
            }
        });
        return view;
    }

    private void setNickNameCount() {
        int length = etNickName.getText().toString().length();
        if (length >= NICKNAME_COUNT) {
            count = NICKNAME_COUNT;
//            etNickName.(etNickName.getText().toString().substring(0, NICKNAME_COUNT));
        } else {
            count = length;
        }
        tvNickCount.setText(String.format("%d/20", count));
    }

    @OnClick(R.id.tvCancel)
    public void onCancel() {
        if (onNickSetListener != null) {
            onNickSetListener.onCancelClick();
        }
    }

    @OnClick(R.id.tvComplete)
    public void onComplete() {
        if (onNickSetListener != null) {
            onNickSetListener.onComplete(etNickName.getText().toString().trim());
        }
    }

    public interface OnNickSetListener {
        void onCancelClick();

        void onComplete(String nickName);
    }

}
