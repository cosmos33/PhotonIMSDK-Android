package com.cosmos.photonim.imbase.utils.task;

import android.os.Process;

import java.util.concurrent.Callable;

public class TaskExecutor {
    private static final TaskExecutor ourInstance = new TaskExecutor();

    public static TaskExecutor getInstance() {
        return ourInstance;
    }

    private TaskExecutor() {
    }

    public void createAsycTask(Callable runnable) {
        createAsycTask(runnable, null);
    }

    public void createAsycTask(Callable runnable, AsycTaskUtil.OnTaskListener listener) {
        AsycTaskUtil.getInstance().createAsycTask(runnable, listener, ExecutorUtil.getDefaultExecutor(), Process.THREAD_PRIORITY_FOREGROUND);
    }

    public void createAsycTaskChat(Callable runnable) {
        AsycTaskUtil.getInstance().createAsycTask(runnable, null, ExecutorUtil.getChatExecutor(), Process.THREAD_PRIORITY_BACKGROUND);
    }
}
