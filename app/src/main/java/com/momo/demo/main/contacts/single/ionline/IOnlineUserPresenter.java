package com.momo.demo.main.contacts.single.ionline;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.contacts.single.OnlineUserData;

import java.util.List;

public abstract class IOnlineUserPresenter<V extends IOnlineUserView, M extends IOnlineUserModel> extends IPresenter<V, M> {
    public IOnlineUserPresenter(V iView) {
        super(iView);
    }
    abstract public void loadContacts();

    @Override
    public V getEmptyView() {
        return (V) new IOnlineUserView() {
            @Override
            public int getLayoutId() {
                return 0;
            }

            @Override
            protected void initView(View view) {

            }

            @Override
            public void loadContacts(List<OnlineUserData> onlineUserData) {

            }

            @Override
            public void showContactsEmptyView() {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }

            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }
        };
    }
}
