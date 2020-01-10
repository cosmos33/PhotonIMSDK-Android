package com.cosmos.photonim.imbase.session.isession;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.cosmos.photonim.imbase.base.IRvBaseFragmentView;
import com.cosmos.photonim.imbase.base.mvp.base.IView;
import com.cosmos.photonim.imbase.session.adapter.SessionData;

import java.util.List;

public abstract class ISessionView extends IRvBaseFragmentView<ISessionPresenter> implements IView {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
    }

    abstract public void onLoadHistory(List<SessionData> sessionData);

    public abstract void onDeleteSession(SessionData data);

    public abstract void onClearSession(SessionData data);

    public abstract void onNewSession(SessionData sessionData);

    public abstract void onGetAllUnReadCount(int result);

    public abstract void onLoadHistoryFromRemote(List<SessionData> sessionData);
}
