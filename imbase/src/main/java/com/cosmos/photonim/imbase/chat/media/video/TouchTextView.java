package com.cosmos.photonim.imbase.chat.media.video;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean isPressing;
    private OnTouchListener onTouchListener;

    public TouchTextView(Context context) {
        super(context);
    }

    public TouchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressing = true;
                if (onTouchListener != null) {
                    onTouchListener.onTouch(true);
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (onTouchListener != null && isPressing) {
                    onTouchListener.onTouch(false);
                }
                isPressing = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    public interface OnTouchListener {
        void onTouch(boolean start);
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}
