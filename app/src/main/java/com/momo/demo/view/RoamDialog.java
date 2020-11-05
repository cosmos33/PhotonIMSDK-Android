package com.momo.demo.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.momo.demo.R;
import com.momo.demo.main.me.RoamInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoamDialog extends DialogFragment {
    @BindView(R.id.etStartTime)
    EditText etStartTime;
    @BindView(R.id.etEndTime)
    EditText etEndTime;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.open)
    Switch open;

    public static RoamDialog getInstance() {
        RoamDialog dialog = new RoamDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_roam, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        open.setChecked(RoamInfo.openRoam());
        setEtEnable(open.isChecked());
        etStartTime.setText(String.valueOf(RoamInfo.getStartTime()));
        etEndTime.setText(String.valueOf(RoamInfo.getEndTime()));
        etCount.setText(String.valueOf(RoamInfo.getCount()));
    }

    @OnClick(R.id.tvCancel)
    public void onCancelClick() {
        this.dismiss();
    }

    @OnClick(R.id.tvComplete)
    public void onCompleteClick() {
        RoamInfo.setRoamOpen(open.isChecked());
        if (!checkData()) {
            ToastUtils.showText("输入有误");
            return;
        }
        RoamInfo.setRoamStartTime(Long.valueOf(etStartTime.getText().toString()));
        RoamInfo.setRoamEndTime(Long.valueOf(etEndTime.getText().toString()));
        RoamInfo.setRoamCount(Integer.valueOf(etCount.getText().toString()));
        this.dismiss();
    }

    private boolean checkData() {
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();

        try {
            if (startTime.length() != 13 && Integer.valueOf(startTime) != 0) {
                return false;
            }
            if (endTime.length() != 13 && Integer.valueOf(endTime) != 0) {
                return false;
            }
            Integer.valueOf(etCount.getText().toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @OnClick(R.id.open)
    public void onOpenClick() {
        setEtEnable(open.isChecked());
    }

    private void setEtEnable(boolean enable) {
        etStartTime.setEnabled(enable);
        etEndTime.setEnabled(enable);
        etCount.setEnabled(enable);
    }
}
