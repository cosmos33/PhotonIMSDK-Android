package com.cosmos.photonim.imbase.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanqiang on 2019/3/28.
 */
public class AnimatorPath {
    private static final String TAG = "AnimatorPath";
    private List<PointType> pointTypeList;
    private WeakReference weakReference;

    public AnimatorPath() {
        this.pointTypeList = new ArrayList<>();
    }

    public void moveTo(float toX, float toY) {
        PointType pointType = PointType.createMoveType();
        pointType.setX(toX);
        pointType.setY(toY);
        pointTypeList.add(pointType);
    }

    public void lineTo(float lineToX, float lineToY) {
        PointType pointType = PointType.createLineType();
        pointType.setX(lineToX);
        pointType.setY(lineToY);
        pointTypeList.add(pointType);
    }

    public void cubicTo(float fristXControl, float fristYControl, float secondXControl,
                        float secondYControl, float endX, float endY) {
        PointType pointType = PointType.createCubicType();
        pointType.setFristXControl(fristXControl);
        pointType.setFristYControl(fristYControl);
        pointType.setSecondXControl(secondXControl);
        pointType.setSecondYControl(secondYControl);
        pointType.setX(endX);
        pointType.setY(endY);
        pointTypeList.add(pointType);
    }

    public void startAnimator(View view, int duration, OnAnimationEndListener onAnimationEndListener) {
        this.weakReference = new WeakReference(view);
        // NOTE: targe的参数是this!!!
        // NOTE: ofObject的最后一个参数如果只有一个值，startValue的值为getHahaha()；
        // NOTE：如果是多余一个值,则startValue为第一个值，endValue为第二个值；startValue为第二个值，endValue为第三个值...
        ObjectAnimator hahahaAnimtor = ObjectAnimator.ofObject(this, "hahaha", new TypeEvaluator<PointType>() {
            @Override
            public PointType evaluate(float fraction, PointType startValue, PointType endValue) {
                Log.v(TAG, String.format("pointType.x:%s,y:%s", startValue, endValue));
                PointType pointType = new PointType();
                if (endValue.getType() == PointType.TYPE_MOVE) {
                    pointType.setX(endValue.getX());
                    pointType.setY(endValue.getY());
                } else if (endValue.getType() == PointType.TYPE_LINE) {
                    pointType.setX(startValue.getX() + (endValue.getX() - startValue.getX()) * fraction);
                    pointType.setY(startValue.getY() + (endValue.getY() - startValue.getY()) * fraction);
                } else if (endValue.getType() == PointType.TYPE_CUBIC) {
                    float temp = 1 - fraction;
                    pointType.setX(startValue.getX() * temp * temp * temp + 3 * endValue.getFristXControl() * fraction * temp * temp
                            + 3 * endValue.getSecondXControl() * fraction * fraction * temp + endValue.getY() * fraction * fraction * fraction);
                    pointType.setY(startValue.getY() * temp * temp * temp + 3 * endValue.getFristYControl() * fraction * temp * temp
                            + 3 * endValue.getSecondYControl() * fraction * fraction * temp + endValue.getY() * fraction * fraction * fraction);
                }
                Log.v(TAG, String.format("=====pointType.x:%s,y:%s", pointType.getX(), pointType.getY()));
                return pointType;
            }
        }, pointTypeList.toArray());
        hahahaAnimtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                AnimatorPath.this.weakReference = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorPath.this.weakReference = null;
                Log.v(TAG, "=========animation end=========");
                if (onAnimationEndListener != null) {
                    onAnimationEndListener.onAnimationEnd();
                }
            }
        });
        hahahaAnimtor.setDuration(duration);
        hahahaAnimtor.start();
    }

    public void setHahaha(PointType pointType) {
        if (weakReference != null) {
            View view = (View) weakReference.get();
            if (view != null) {
                view.setTranslationX(pointType.getX());
                view.setTranslationY(pointType.getY());
            }
        }
    }

    public PointType getHahaha() {
        return new PointType();
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }

}
