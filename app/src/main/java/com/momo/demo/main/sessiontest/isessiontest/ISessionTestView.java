package com.momo.demo.main.sessiontest.isessiontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.momo.demo.main.sessiontest.SessionTestData;

import java.util.List;

public abstract class ISessionTestView extends RvBaseFragment implements IView {
    protected ISessionTestPresenter iSessionPresenterTest;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iSessionPresenterTest = (ISessionTestPresenter) getIPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
    }

    abstract public void onLoadHistory(List<SessionTestData> sessionData);

    public abstract void onGetOtherInfoResult(JsonResult result, SessionTestData sessionData);

    public abstract void onDeleteSession(SessionTestData data);

    public abstract void onClearSession(SessionTestData data);

    public abstract void onNewSession(SessionTestData sessionData);

    public abstract void onAddSessionResult();
}
