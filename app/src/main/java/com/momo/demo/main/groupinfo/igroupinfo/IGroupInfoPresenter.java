package com.momo.demo.main.groupinfo.igroupinfo;


import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.groupmemberselected.GroupMembersData;

import java.util.List;

public abstract class IGroupInfoPresenter<V extends IGroupInfoView, M extends IGroupInfoModel> extends IPresenter<V, M> {

    public IGroupInfoPresenter(V iView) {
        super(iView);
    }


    @Override
    public V getEmptyView() {
        return (V) new IGroupInfoView() {

            @Override
            public void onGetGroupInfoResult(JsonResult jsonResult) {

            }

            @Override
            public void onGetGroupMemberResult(List<GroupMembersData> jsonResult) {

            }

            @Override
            public void onGetGroupIgnoreStatusResult(JsonResult jsonResult) {

            }

            @Override
            public void onChangeGroupIgnoreStatusResult(JsonResult jsonResult) {

            }

            @Override
            public void dimissProgressDialog() {

            }

            @Override
            public void changeTopStatus(boolean top) {

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

    public abstract void getGroupInfo(String gid);

    public abstract void getGroupMembers(String gid);

    public abstract void getGroupIgnoreStatus(String gid);

    public abstract void changeGroupIgnoreStatus(String gid, boolean igonre);

    public abstract void clearChatContent(int chatType, String chatWith);

    public abstract void changeTopStatus(int chatType, String id);

    public abstract void getTopStatus(int chatType, String id);
}
