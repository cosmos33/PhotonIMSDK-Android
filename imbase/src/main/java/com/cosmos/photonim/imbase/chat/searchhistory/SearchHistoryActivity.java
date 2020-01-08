package com.cosmos.photonim.imbase.chat.searchhistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.chat.ChatBaseActivity;
import com.cosmos.photonim.imbase.chat.searchhistory.adapter.SessionSearchAdapter;
import com.cosmos.photonim.imbase.chat.searchhistory.isearch.ISearchView;
import com.cosmos.photonim.imbase.session.SessionData;
import com.cosmos.photonim.imbase.session.SessionUpdateOtherInfoImpl;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchHistoryActivity extends ISearchView {
    @BindView(R2.id.etSearch)
    EditText etSearch;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    private SessionSearchAdapter sessionAdapter;
    private List<SessionData> baseDataList;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SearchHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchhistory);

        initView();
    }


    private void initView() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
    }

    private void search() {
        String searchContent = etSearch.getText().toString();
        if (TextUtils.isEmpty(searchContent)) {
            presenter.cancel();
            clearListResult();
            return;
        }
        presenter.search(searchContent);
    }

    private void clearListResult() {
        baseDataList.clear();
        sessionAdapter.notifyDataSetChanged();
    }

    @OnClick(R2.id.tvCancel)
    public void onCancelClick() {
        presenter.cancel();
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.cancel();
        super.onDestroy();
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (sessionAdapter == null) {
            baseDataList = new ArrayList<>();
            sessionAdapter = new SessionSearchAdapter(baseDataList, new SessionUpdateOtherInfoImpl(new SessionUpdateOtherInfoImpl.OnSessionUpdateCallback() {
                @Override
                public void onSessionUpdate(SessionData sessionData) {
                    sessionAdapter.notifyItemChanged(sessionData.getItemPosition());
                }
            }));
            sessionAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    SessionData sessionData = (SessionData) data;
                    ChatBaseActivity.startActivity(SearchHistoryActivity.this, sessionData.getChatType(),
                            sessionData.getChatWith(), null, sessionData.getNickName(), sessionData.getIcon(), false);
                }
            });
        }
        return sessionAdapter;
    }

    @Override
    public IPresenter getIPresenter() {
        return new SearchPresenter(this);
    }
}
