package com.momo.demo.main.roomInfo.igroupinfo;


import androidx.recyclerview.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IRoomInfoPresenter<V extends IRoomInfoView, M extends IRoomInfoModel> extends IPresenter<V, M> {

    public IRoomInfoPresenter(V iView) {
        super(iView);
    }


    @Override
    public V getEmptyView() {
        return (V) new IRoomInfoView() {
            @Override
            public void onGetGroupMemberResult(List<GroupMembersData> jsonResult) {

            }

            @Override
            public IRoomInfoPresenter getIPresenter() {
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

    public abstract void getGroupMembers(String gid);
}
