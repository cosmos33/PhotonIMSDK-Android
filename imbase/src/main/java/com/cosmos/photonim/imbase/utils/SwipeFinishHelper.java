package com.cosmos.photonim.imbase.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

public class SwipeFinishHelper {
    private static final int DURATION = 400;
    private static float DIFF;
    private int screenWidth;
    private boolean animate;
    private float xLast;
    private float yLast;
    private WeakReference<View> viewWeakReference;
    private boolean toFinish;

    public SwipeFinishHelper(View view) {
        viewWeakReference = new WeakReference<>(view);
        int[] screenSize = Utils.getScreenSize(view.getContext());
        screenWidth = screenSize[0];
        DIFF = screenWidth * 0.02f;

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (animate) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getRawX() <= DIFF) {
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (animate) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xLast = event.getRawX();
                yLast = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                setLocation(event.getRawX() - xLast);
                xLast = event.getRawX();
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (viewWeakReference != null
                        && (viewWeakReference.get() != null)
                        && viewWeakReference.get().getContext() instanceof Activity) {
                    if (event.getRawX() > screenWidth / 2) {
                        animate = true;
                        toFinish = true;
                        AnimatorPath animatorPath = new AnimatorPath();
                        animatorPath.moveTo(xLast, 0);
                        animatorPath.lineTo(screenWidth, 0);
                        animatorPath.startAnimator(viewWeakReference.get(),
                                (int) ((screenWidth - xLast) * 1.f / screenWidth * DURATION), onAnimationEndListener);
                    } else {
                        animate = true;
                        AnimatorPath animatorPath = new AnimatorPath();
                        animatorPath.moveTo(xLast, 0);
                        animatorPath.lineTo(0, 0);
                        animatorPath.startAnimator(viewWeakReference.get(),
                                (int) (xLast * 1.f / screenWidth * DURATION), onAnimationEndListener);
                    }
                }
                break;

        }
        return false;
    }

    private void setLocation(float x) {
        if (viewWeakReference != null) {
            View view = viewWeakReference.get();
            float translate = view.getTranslationX() + x;
            if (translate > 0) {
                view.setTranslationX(view.getTranslationX() + x);
            }
        }
    }

    private AnimatorPath.OnAnimationEndListener onAnimationEndListener = new AnimatorPath.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd() {
            animate = false;
            if (viewWeakReference != null) {
                View view = viewWeakReference.get();
                if (view != null && toFinish && view.getContext() instanceof Activity) {
                    ((Activity) view.getContext()).finish();
                }
            }
        }
    };
}
