package com.cosmos.photonim.imbase.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.cosmos.photonim.imbase.utils.SwipeFinishHelper;

public class SwipeFrameLayout extends FrameLayout {
    private SwipeFinishHelper swipeFinishHelper;

    public SwipeFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
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
