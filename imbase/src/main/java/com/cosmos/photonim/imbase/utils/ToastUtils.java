package com.cosmos.photonim.imbase.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.cosmos.photonim.imbase.ImBaseBridge;

/**
 * Created by fanqiang on 2018/11/28.
 */
public class ToastUtils {
    private static Toast toast;
    private static Handler handler;
    private static ReuseRunnable reuseRunnable;

    public static void showText(final String content) {
        //TODO 是否可以公用一个toast
        if (Looper.myLooper() != null && Looper.myLooper().equals(Looper.getMainLooper())) {
            showTextInner(ContextHolder.getContext(), content);
        } else {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            if (reuseRunnable == null) {
                reuseRunnable = new ReuseRunnable();
            }
            reuseRunnable.setContext(ImBaseBridge.getInstance().getApplication());
            reuseRunnable.setContent(content);
            handler.post(reuseRunnable);
        }
    }

    private static class ReuseRunnable implements Runnable {
        public void setContent(String content) {
            this.content = content;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        private Context context;
        private String content;

        @Override
        public void run() {
            showTextInner(context, content);
        }
    }


    private static void showTextInner(Context context, String content) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
