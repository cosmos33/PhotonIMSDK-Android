package com.cosmos.photonim.imbase.utils.task;

import android.os.AsyncTask;
import android.os.Process;

import com.cosmos.photonim.imbase.utils.LogUtils;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * Created by fanqiang on 2018/12/7.
 */
public class AsycTaskUtil {
    private static final String TAG = "AsycTaskUtil";
    private static final int THREAD_PRIORITY_HIGHTEST = -20;
    private static AsycTaskUtil instance;
    private HashMap<Callable<Object>, AsyncTaskCustom> runningTaskMap;

    public static AsycTaskUtil getInstance() {
        if (instance == null) {
            instance = new AsycTaskUtil();
        }
        return instance;
    }

    private AsycTaskUtil() {
    }

    public void createAsycTask(Callable runnable, OnTaskListener listener, Executor executor) {
        createAsycTask(runnable, listener, executor, Process.THREAD_PRIORITY_BACKGROUND);
    }

    public void createAsycTask(Callable runnable, OnTaskListener listener, Executor executor, int priority) {
        AsyncTaskCustom asycTask = new AsyncTaskCustom();
        asycTask.setCallable(runnable);
        asycTask.setListener(listener);
        asycTask.setPriority(priority);
        asycTask.executeOnExecutor(executor);
        if (runningTaskMap == null) {
            runningTaskMap = new HashMap<>();
        }
        runningTaskMap.put(runnable, asycTask);
    }

    public void cancelTask(Callable runnable) {
        if (runningTaskMap == null)
            return;
        AsyncTaskCustom taskCustom = runningTaskMap.get(runnable);
        if (taskCustom == null) {
            return;
        }
        taskCustom.cancel(true);
        taskCustom.setCallable(null);
        taskCustom.setListener(null);
        runningTaskMap.remove(runnable);
    }

    private static class AsyncTaskCustom extends AsyncTask {
        private Callable<Object> callable;
        private OnTaskListener listener;

        private int priority = Process.THREAD_PRIORITY_BACKGROUND;

        @Override
        protected Object doInBackground(Object[] objects) {
            Process.setThreadPriority(priority);
            if (callable != null) {
                if (!isCancelled()) {
                    try {
                        LogUtils.log(TAG, callable.toString());
                        return callable.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (listener != null) {
                listener.onTaskFinished(o);
            }
        }

        public void setCallable(Callable callable) {
            this.callable = callable;
        }

        public void setListener(OnTaskListener listener) {
            this.listener = listener;
        }

        public void setPriority(int priority) {
            if (priority > Process.THREAD_PRIORITY_LOWEST) {
                priority = Process.THREAD_PRIORITY_LOWEST;
            }
            if (priority < THREAD_PRIORITY_HIGHTEST) {
                priority = THREAD_PRIORITY_HIGHTEST;
            }
            this.priority = priority;
        }
    }

    public interface OnTaskListener {
        void onTaskFinished(Object result);
    }
}
