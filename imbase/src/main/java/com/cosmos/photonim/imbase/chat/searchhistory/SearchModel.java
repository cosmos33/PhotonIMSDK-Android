package com.cosmos.photonim.imbase.chat.searchhistory;

import com.cosmos.photon.im.PhotonIMDatabase;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SearchData;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchHistoryModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class SearchModel extends ISearchHistoryModel {
    private static final String MATCH_PREFIX = "[(*..";
    private static final String MATCH_POSTFIX = "..*)]";

    private static final String MATCH_PREFIX_PATTERN = "\\[\\(\\*\\.\\.";
    private static final String MATCH_POSTFIX_PATTERN = "\\.\\.\\*\\)\\]";
    private static final int MATCH_MAXLENGTH = 20;
    private Callable callable;

    @Override
    public void search(String content, int chatType, String chatWith, OnSearchResultCallback onSearchResultCallback) {
        TaskExecutor.getInstance().createAsycTask(callable = new Callable() {
            @Override
            public Object call() throws Exception {
                ArrayList<PhotonIMMessage> photonIMMessages =
                        PhotonIMDatabase.getInstance().searchSessionMessages(chatType,
                                chatWith, MATCH_PREFIX, MATCH_POSTFIX, MATCH_MAXLENGTH, content);
                if (CollectionUtils.isEmpty(photonIMMessages)) {
                    return null;
                }
                ArrayList<SearchData> searchData = new ArrayList<>(photonIMMessages.size());
                for (PhotonIMMessage photonIMMessage : photonIMMessages) {
                    searchData.add(new SearchData(photonIMMessage, MATCH_PREFIX_PATTERN, MATCH_POSTFIX_PATTERN, MATCH_PREFIX, MATCH_POSTFIX));
                }
                return searchData;
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (onSearchResultCallback != null) {
                    onSearchResultCallback.onSearchResultCallBack((ArrayList<SearchData>) result);
                }
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
