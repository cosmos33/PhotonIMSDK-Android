package com.momo.demo.main.sessiontest.isessiontest;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.cosmos.photonim.imbase.base.IRvBaseFragmentView;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.momo.demo.main.sessiontest.SessionTestData;

import java.util.List;

public abstract class ISessionTestView extends IRvBaseFragmentView<ISessionTestPresenter> {
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
