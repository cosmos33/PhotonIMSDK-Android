package com.cosmos.photonim.imbase.chat.searchhistory;

import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchHistoryModel;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.concurrent.Callable;

public class SearchModel extends ISearchHistoryModel {
    private Callable callable;

    @Override
    public void search(String content) {
        TaskExecutor.getInstance().createAsycTask(callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {

            }
        });
    }

    @Override
    public void cancel() {
        if (callable != null) {
            TaskExecutor.getInstance().cancel(callable);
            callable = null;
        }
    }
}
