package com.cosmos.photonim.imbase.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class TouchRecycleView extends RecyclerView {
    private float downX;
    private float downY;
    private int move;
    private static final String TAG = "TouchRecycleView";
    private OnRecycleViewClickListener onRecycleViewClickListener;
    private float lastPointX;
    private float lastPointY;

    public TouchRecycleView(@NonNull Context context) {
        super(context);
        init();
    }

    public TouchRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        move = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setItemAnimator(null);
        setHasFixedSize(false);
//        getItemAnimator().setAddDuration(0);
//        getItemAnimator().setChangeDuration(0);
//        getItemAnimator().setMoveDuration(0);
//        getItemAnimator().setRemoveDuration(0);
//        ((DefaultItemAnimator)getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        lastPointX = e.getRawX();
        lastPointY = e.getRawY();
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = e.getX();
                downY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(downX - e.getX()) < move
                        && Math.abs(downY - e.getY()) < move) {
                    if (onRecycleViewClickListener != null) {
                        onRecycleViewClickListener.onRecycleViewClick();
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    public void setOnRecycleViewClickListener(OnRecycleViewClickListener onRecycleViewClickListener) {
        this.onRecycleViewClickListener = onRecycleViewClickListener;
    }

    public interface OnRecycleViewClickListener {
        void onRecycleViewClick();
    }

    public float[] getLastPoint() {
        return new float[]{lastPointX, lastPointY};
    }
}
