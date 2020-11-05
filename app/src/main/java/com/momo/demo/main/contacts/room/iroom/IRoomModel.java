package com.momo.demo.main.contacts.room.iroom;

import com.cosmos.photonim.imbase.base.mvp.base.IModel;
import com.momo.demo.main.contacts.room.adapter.RoomData;

import java.util.List;

public abstract class IRoomModel implements IModel {
    public abstract void setRoomListener(OnRoomListener onLoadNearbyGroup);

    public abstract void loadRooms();

    public abstract void joinRooms(String roomId);

    public abstract void leaveRooms(String roomId);


    public interface OnRoomListener {
        void onLoadRoom(boolean success, List<RoomData> onlineUserData);

        void onJoinRoomResult(boolean success, String roomId);

        void onLeaveRoomResult(boolean success, String roomId);
    }

    public static class OnRoomImpl implements OnRoomListener {

        @Override
        public void onLoadRoom(boolean success, List<RoomData> onlineUserData) {

        }

        @Override
        public void onJoinRoomResult(boolean success, String roomId) {

        }

        @Override
        public void onLeaveRoomResult(boolean success, String roomId) {

        }
    }

}
