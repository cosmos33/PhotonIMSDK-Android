package com.momo.demo.main.groupmember.igroupmember;


import androidx.recyclerview.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupMemberPresenter<V extends IGroupMemeberView, M extends IGroupMemberModel> extends IPresenter<V, M> {
    public IGroupMemberPresenter(V iView) {
        super(iView);
    }

    public abstract void getGroupMembers(String gid);

    @Override
    public V getEmptyView() {
        return (V) new IGroupMemeberView() {

            @Override
            public void onGetGroupMembersResult(List<GroupMembersData> result) {

            }

            @Override
            public void showMembersEmptyView() {

            }

            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }

            @Override
            public IGroupMemberPresenter getIPresenter() {
                return null;
            }

        };
    }
}
