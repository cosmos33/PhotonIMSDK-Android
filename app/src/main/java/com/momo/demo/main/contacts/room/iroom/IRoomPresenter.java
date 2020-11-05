package com.momo.demo.main.contacts.room.iroom;


import androidx.recyclerview.widget.RecyclerView;

import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.contacts.room.adapter.RoomData;

import java.util.List;

public abstract class IRoomPresenter<V extends IRoomView, M extends IRoomModel> extends IPresenter<V, M> {
    public IRoomPresenter(V iView) {
        super(iView);
    }

    abstract public void loadRooms();

    public abstract void joinRooms(RoomData roomData);

    @SuppressWarnings("unchecked")
    @Override
    public V getEmptyView() {
        return (V) new IRoomView() {


            @Override
            public void loadRoomResult(List<RoomData> onlineUserData) {

            }

            @Override
            public IRoomPresenter getIPresenter() {
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
