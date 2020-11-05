package com.momo.demo.main.contacts.room.iroom;

import com.cosmos.photonim.imbase.utils.recycleadapter.actiivty.RvBaseActivity;
import com.momo.demo.main.contacts.room.adapter.RoomData;

import java.util.List;

public abstract class IRoomView extends RvBaseActivity<IRoomPresenter> {

    public abstract void loadRoomResult(List<RoomData> onlineUserData);
}
