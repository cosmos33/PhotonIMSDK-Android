package com.cosmos.photonim.imbase.utils.task;

import com.cosmos.photonim.imbase.utils.LogUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by fanqiang on 2018/12/7.
 */
public class ExecutorUtil {
    private static final String TAG = "ExecutorUtil";
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int maximumPoolSize = CPU_COUNT * 2 + 1;
    private static int keepAliveTime = 30;
    private static Executor defaultExecutor;
    private static Executor chatExecutor;
    private static BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque();
    private static BlockingQueue<Runnable> chatBlockingQueue = new LinkedBlockingDeque();
    private static ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
//            thread.setPriority();
            return thread;
        }
    };

    private static RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            LogUtils.log(TAG, "rejectedExecution runnable:" + r.hashCode());
            super.rejectedExecution(r, e);
        }
    };

    static public Executor getDefaultExecutor() {
        if (defaultExecutor == null) {
            defaultExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                    keepAliveTime, TimeUnit.SECONDS, blockingQueue, threadFactory, rejectedExecutionHandler);
        }
        return defaultExecutor;
    }

    static public Executor getChatExecutor() {
        if (chatExecutor == null) {
            chatExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                    keepAliveTime, TimeUnit.SECONDS, chatBlockingQueue, threadFactory, rejectedExecutionHandler);
        }
        return chatExecutor;
    }
}
