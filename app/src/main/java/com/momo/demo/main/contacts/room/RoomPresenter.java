package com.momo.demo.main.contacts.room;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.chat.chatroom.ChatRoomActivity;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.momo.demo.main.contacts.room.adapter.RoomData;
import com.momo.demo.main.contacts.room.iroom.IRoomModel;
import com.momo.demo.main.contacts.room.iroom.IRoomPresenter;

import java.util.List;

public class RoomPresenter extends IRoomPresenter<RoomActivity, RoomModel> {
    private static final int JOIN_ROOM_TIMEOUT = 2000;
    private volatile boolean isEnteringRoom;

    public RoomPresenter(RoomActivity iView) {
        super(iView);
        getiModel().setRoomListener(new IRoomModel.OnRoomImpl() {
            @Override
            public void onLoadRoom(boolean success, List<RoomData> onlineUserData) {
                if (success) {
                    if (CollectionUtils.isEmpty(onlineUserData)) {
                        getIView().toast("获取room为空");
                    } else {
                        getIView().loadRoomResult(onlineUserData);
                    }
                } else {
                    getIView().toast("获取room失败");
                }
            }

            @Override
            public void onJoinRoomResult(boolean success, String roomId) {
                if (success) {
                    sdkJoinRoom(roomId);
                } else {
                    getIView().toast("join room failed");
                    isEnteringRoom = false;
                }
            }
        });
    }

    private void sdkJoinRoom(String roomId) {
        PhotonIMClient.getInstance().sendJoinRoom(roomId, JOIN_ROOM_TIMEOUT, new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int code, String msg, long retTime) {
                if (code == 0) {
                    ChatRoomActivity.startActivity(getIView().getApplicationContext(), PhotonIMMessage.ROOM, roomId, String.format("聊天室%s", roomId));
                } else {
                    getIView().toast(String.format("join room failed(sdk:%s)", msg));
                }
                isEnteringRoom = false;
            }
        });
    }

    @Override
    public void loadRooms() {
        getiModel().loadRooms();
    }

    @Override
    public void joinRooms(RoomData roomData) {
        if (isEnteringRoom) {
            return;
        }
        isEnteringRoom = true;
        getiModel().joinRooms(roomData.getGid());
    }

    @Override
    public RoomModel generateIModel() {
        return new RoomModel();
    }
}
