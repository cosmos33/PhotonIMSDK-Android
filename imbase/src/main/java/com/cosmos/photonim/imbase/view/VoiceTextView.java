package com.cosmos.photonim.imbase.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;

public class VoiceTextView extends AppCompatTextView {
    private static final int WAHAT_EVENT_DOWN = 925;
    private static final String TAG = "VoiceTextView";
    private static final int FRQUENT_DURATION = 1000;
    private int[] outLocation;
    private OnEventUpListener onEventUpListener;
    private int timeOut;
    private CustomRunnable customRunnable;
    private long lastDownTime;
    private Handler handler;

    public VoiceTextView(Context context) {
        super(context);
        init();
    }

    public VoiceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoiceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler = new VoiceHandler();
        customRunnable = new CustomRunnable.Builder().runnable(new Runnable() {
            @Override
            public void run() {
                setText("按住说话");
                if (onEventUpListener != null) {
                    onEventUpListener.onTimeout();
                }
            }
        }).build();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX;
        float rawY;
        LogUtils.log(TAG, "onTouchEvent start");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (System.currentTimeMillis() - lastDownTime < FRQUENT_DURATION) {
                    ToastUtils.showText(getContext(), "操作太频繁");
                    return false;
                }
                lastDownTime = System.currentTimeMillis();
                if (onEventUpListener != null) {
                    if (!onEventUpListener.canHandle()) {
                        return super.onTouchEvent(event);
                    }
                }
                setText("松开发送");
                if (customRunnable.getDelayTime() != 0) {
                    MainLooperExecuteUtil.getInstance().post(customRunnable);
                }
                Message message = Message.obtain();
                message.obj = onEventUpListener;
                message.what = WAHAT_EVENT_DOWN;
                handler.sendMessage(message);
                break;
            case MotionEvent.ACTION_MOVE:
                rawX = event.getRawX();
                rawY = event.getRawY();
                outLocation = new int[2];
                getLocationInWindow(outLocation);
                if (rawX > outLocation[0] + getWidth() || rawX < outLocation[0]
                        || rawY < outLocation[1] || rawY > outLocation[1] + getHeight()) {
                    setText("松开取消");
                } else {
                    setText("松开发送");
                }
                LogUtils.log(TAG, "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MainLooperExecuteUtil.getInstance().cancelRunnable(customRunnable);
                rawX = event.getRawX();
                rawY = event.getRawY();
                if (outLocation != null && (rawX > outLocation[0] + getWidth() || rawX < outLocation[0]
                        || rawY < outLocation[1] || rawY > outLocation[1] + getHeight())) {
                    if (onEventUpListener != null) {
                        onEventUpListener.onEventCancel();
                    }
                } else {
                    if (onEventUpListener != null) {
                        onEventUpListener.onEventUp();
                    }
                }
                setText("按住说话");
                LogUtils.log(TAG, "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                MainLooperExecuteUtil.getInstance().cancelRunnable(customRunnable);
                if (onEventUpListener != null) {
                    onEventUpListener.onEventCancel();
                }
                setText("按住说话");
                LogUtils.log(TAG, "ACTION_CANCEL");
                break;
        }
        LogUtils.log(TAG, "onTouchEvent end");
        return true;
    }

    public void setTimeOut(int timeOut) {
        customRunnable.setDelayTime(timeOut);
    }

    public interface OnEventUpListener {
        boolean canHandle();

        void onEventDown();

        void onEventCancel();

        void onEventUp();

        void onTimeout();
    }

    public static class VoiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WAHAT_EVENT_DOWN:
                    if (msg.obj != null) {
                        ((OnEventUpListener) msg.obj).onEventDown();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }


    public void setOnEventUpListener(OnEventUpListener onEventUpListener) {
        this.onEventUpListener = onEventUpListener;
    }
}
