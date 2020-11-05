package com.momo.demo.main.contacts.room;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.R;
import com.momo.demo.main.contacts.room.adapter.RoomAdapter;
import com.momo.demo.main.contacts.room.adapter.RoomData;
import com.momo.demo.main.contacts.room.iroom.IRoomView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RoomActivity extends IRoomView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    private RoomAdapter roomAdapter;
    private List<RoomData> roomData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
        getIPresenter().loadRooms();
    }

    private void initView() {
        titleBar.setTitle("聊天室", null);
        titleBar.setLeftImageEvent(com.cosmos.photonim.imbase.R.drawable.arrow_left, v -> RoomActivity.this.finish());
    }

    @Override
    public void loadRoomResult(List<RoomData> onlineUserData) {
        roomData.addAll(onlineUserData);
        roomAdapter.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
    }

    @Override
    public RoomPresenter getIPresenter() {
        return new RoomPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RoomAdapter getAdapter() {
        roomData = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomData);
        roomAdapter.setRvListener(new RvListenerImpl() {
            @Override
            public void onClick(View view, Object data, int position) {
                getIPresenter().joinRooms((RoomData) data);
            }
        });
        return roomAdapter;
    }
}
