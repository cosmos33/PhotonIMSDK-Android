package com.cosmos.photonim.imbase.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.cosmos.photonim.imbase.utils.SwipeFinishHelper;

public class SwipeLinearLayout extends LinearLayout {
    private SwipeFinishHelper swipeFinishHelper;

    public SwipeLinearLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        swipeFinishHelper = new SwipeFinishHelper(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = swipeFinishHelper.onInterceptTouchEvent(ev);
        return result ? result : super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = swipeFinishHelper.onTouchEvent(event);
        return result ? result : super.onTouchEvent(event);
    }
}
