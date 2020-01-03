package com.cosmos.photonim.imbase.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosmos.photonim.imbase.R;

public class ProcessDialogFragment extends DialogFragment {

    public static ProcessDialogFragment getInstance() {
        ProcessDialogFragment processDialogFragment = new ProcessDialogFragment();
        return processDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_process, container);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        return view;
    }
}
