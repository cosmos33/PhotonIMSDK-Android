package com.cosmos.photonim.imbase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class DownLoadProgress extends View {
    private int maxProgress = 100;
    private int currentProgress = 0;
    private Paint paint;
    private RectF rect;

    public DownLoadProgress(Context context) {
        super(context);
    }

    public DownLoadProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DownLoadProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float sweepAngle = getSweepAngle();
        canvas.drawArc(getRect(), 270 - sweepAngle, sweepAngle, true, getPaint());
    }

    private float getSweepAngle() {
        return 360 - currentProgress * 1.0f / maxProgress * 360;
    }

    private RectF getRect() {
        if (rect == null) {
            rect = new RectF(0, 0, getWidth(), getHeight());
        }
        return rect;
    }

    private Paint getPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(0x77FFFFFF);
        }
        return paint;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        if (maxProgress == 0) {
            throw new IllegalArgumentException("maxProgress can't be 0");
        }
    }

    public void setCurrentProgress(int currentProgress) {
        if (this.currentProgress >= currentProgress) {
            return;
        }
        this.currentProgress = currentProgress;
        postInvalidate();
    }
}
