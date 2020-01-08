package com.momo.demo.main.contacts.group.igroup;


import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.contacts.group.GroupData;

import java.util.List;

public abstract class IGroupPresenter<V extends IGroupView, M extends IGroupModel> extends IPresenter<V, M> {
    public IGroupPresenter(V iView) {
        super(iView);
    }

    abstract public void loadGroups();

    @Override
    public V getEmptyView() {
        return (V) new IGroupView() {
            @Override
            public void loadNearbyGroupResult(List<GroupData> onlineUserData) {

            }

            @Override
            public void showNearbbyGroupEmptyView() {

            }

            @Override
            public void onJoinGroupResult(GroupData groupData, boolean result) {

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

    public abstract void joinGroup(GroupData groupData);
}
