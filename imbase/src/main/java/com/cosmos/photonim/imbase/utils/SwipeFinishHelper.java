package com.cosmos.photonim.imbase.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.cosmos.photonim.imbase.R;

import java.lang.ref.WeakReference;

public class SwipeFinishHelper {
    private static final int DURATION = 1000;
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

        view.post(new Runnable() {
            @Override
            public void run() {
                setViewColor(view);
            }
        });
    }

    private void setViewColor(View view) {
        View decorView = ((Activity) view.getContext()).getWindow().getDecorView();
        decorView.setBackgroundResource(R.drawable.activity_root_bg);
//        if (view == null) {
//            return;
//        }
//        if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent.getParent() == null) {
//                parent.setBackgroundResource(R.drawable.activity_root_bg);
//            } else {
//                parent.setBackgroundColor(Color.TRANSPARENT);
//            }
//            setViewColor(parent);
//        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (animate) {
//            return true;
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (ev.getRawX() <= DIFF) {
//                    return true;
//                }
//                break;
//        }
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
                        animatorPath.moveTo(screenWidth, 0);
                        animatorPath.startAnimator(viewWeakReference.get(), DURATION, onAnimationEndListener);
                    } else {
                        animate = true;
                        AnimatorPath animatorPath = new AnimatorPath();
                        animatorPath.moveTo(0, 0);
                        animatorPath.startAnimator(viewWeakReference.get(), DURATION, onAnimationEndListener);
                    }
                }
                break;

        }
        return false;
    }

    private void setLocation(float x) {
        if (viewWeakReference != null) {
            View view = viewWeakReference.get();
            view.setTranslationX(view.getTranslationX() + x);
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
