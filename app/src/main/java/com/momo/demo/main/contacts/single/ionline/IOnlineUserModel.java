package com.momo.demo.main.contacts.single.ionline;

import com.cosmos.photonim.imbase.base.mvpbase.IModel;
import com.momo.demo.main.contacts.single.OnlineUserData;

import java.util.List;

public abstract class IOnlineUserModel implements IModel {
    public abstract void loadContacts(int itemType, OnLoadContactListener onLoadContactListener);

    public interface OnLoadContactListener {
        void onLoadContact(List<OnlineUserData> onlineUserData);
    }
}
