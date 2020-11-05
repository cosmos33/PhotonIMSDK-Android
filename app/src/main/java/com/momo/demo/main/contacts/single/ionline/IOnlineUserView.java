package com.momo.demo.main.contacts.single.ionline;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.cosmos.photonim.imbase.base.IRvBaseFragmentView;
import com.momo.demo.main.contacts.single.OnlineUserData;

import java.util.List;

public abstract class IOnlineUserView extends IRvBaseFragmentView<IOnlineUserPresenter> {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
    }

    public abstract void loadContacts(List<OnlineUserData> onlineUserData);

    public abstract void showContactsEmptyView();
}
