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

public class SessionDialogFragment extends DialogFragment {
    @BindView(R2.id.tvDelete)
    TextView tvDelete;
    private OnHandleListener onHandleListener;

    public static SessionDialogFragment getInstance(OnHandleListener onHandleListener) {
        SessionDialogFragment sessionDialogFragment = new SessionDialogFragment();
        sessionDialogFragment.onHandleListener = onHandleListener;
        return sessionDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_session, container);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R2.id.tvDelete)
    public void onDeleteClick() {
        if (onHandleListener != null) {
            onHandleListener.onDelete();
        }
    }

    @OnClick(R2.id.tvDeleteContent)
    public void oClearContentClick() {
        if (onHandleListener != null) {
            onHandleListener.onClearContent();
        }
    }

    public interface OnHandleListener {
        void onDelete();

        void onClearContent();
    }

}
