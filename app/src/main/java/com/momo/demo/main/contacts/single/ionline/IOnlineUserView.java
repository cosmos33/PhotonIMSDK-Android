package com.momo.demo.main.contacts.single.ionline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.utils.mvpbase.IView;
import com.momo.demo.main.contacts.single.OnlineUserData;

import java.util.List;

public abstract class IOnlineUserView extends RvBaseFragment implements IView {
    protected IOnlineUserPresenter contactPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactPresenter = (IOnlineUserPresenter) getIPresenter();
        if (contactPresenter == null) {
            throw new IllegalStateException("contactPresenter is null");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
    }

    public abstract void loadContacts(List<OnlineUserData> onlineUserData);

    public abstract void showContactsEmptyView();
}
