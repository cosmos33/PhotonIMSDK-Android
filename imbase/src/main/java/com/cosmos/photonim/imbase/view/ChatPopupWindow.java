package com.cosmos.photonim.imbase.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.Utils;

public class ChatPopupWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "ChatPopupWindow";
    private OnMenuClick onMenuClick;
    private static int screenHeight;

    public ChatPopupWindow(boolean showCopy, boolean showRevert, Context context, OnMenuClick onMenuClick) {
        this.onMenuClick = onMenuClick;
        View view = View.inflate(context, R.layout.popup_chat_men, null);
        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        TextView tvCopy = view.findViewById(R.id.tvCopy);
        TextView tvRelay = view.findViewById(R.id.tvRelay);
        TextView tvRevert = view.findViewById(R.id.tvRevert);
        TextView tvDelete = view.findViewById(R.id.tvDelete);
        if (!showRevert) {
            view.findViewById(R.id.divider_revert).setVisibility(View.GONE);
            tvRevert.setVisibility(View.GONE);
        }
        if (!showCopy) {
            view.findViewById(R.id.divider_copy).setVisibility(View.GONE);
            tvCopy.setVisibility(View.GONE);
        }

        tvCopy.setOnClickListener(this);
        tvRelay.setOnClickListener(this);
        tvRevert.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onMenuClick != null) {
            int i = v.getId();
            if (i == R.id.tvCopy) {
                onMenuClick.onCopyClick();
            } else if (i == R.id.tvRelay) {
                onMenuClick.onRelayClick();
            } else if (i == R.id.tvRevert) {
                onMenuClick.onRevertClick();
            } else if (i == R.id.tvDelete) {
                onMenuClick.onDeleteClick();
            }
        }
        dismiss();
    }

    public void show(float[] lastPoint, View parent) {
        if (screenHeight == 0) {
            screenHeight = Utils.getScreenSize(parent.getContext())[1];
        }
//        int[] location = new int[2];
//        parent.getLocationInWindow(location);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        getContentView().measure(w, h);
        int height = getContentView().getMeasuredHeight();
        int width = getContentView().getMeasuredWidth();
        int yOff;
        boolean up = lastPoint[1] <= (screenHeight / 2) ? true : false;

        if (up) {
            getContentView().setBackgroundResource(R.drawable.popup_chat_up);
            yOff = 0;
        } else {
            getContentView().setBackgroundResource(R.drawable.popup_chat);
            yOff = -height;
        }
//        int yOff = up ? (location[1] + parent.getHeight() / 2) : (location[1] - parent.getHeight() / 2);
//        int xOff = location[0] + parent.getWidth() / 2 - width / 2;

        LogUtils.log(TAG, String.format("lastx:%f;lasty:%f", lastPoint[0], lastPoint[1]));
        showAtLocation(parent.getRootView(), Gravity.NO_GRAVITY, (int) lastPoint[0] - width / 2, (int) lastPoint[1] + yOff);

        // TODO: 2019-08-21 显示在点击的坐标位置

    }

    public interface OnMenuClick {
        void onCopyClick();

        void onRelayClick();

        void onRevertClick();

        void onDeleteClick();
    }
}
