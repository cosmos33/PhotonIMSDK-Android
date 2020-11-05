package com.cosmos.photonim.imbase.chat.chatroom;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.chatroom.iroom.IChatRoomModel;

public class ChatRoomModel extends IChatRoomModel {

    @Override
    public void leaveRoom(String rId, ChatRoomListener listener) {
        ImBaseBridge.getInstance().leaveRoom(rId, listener);
    }
}
