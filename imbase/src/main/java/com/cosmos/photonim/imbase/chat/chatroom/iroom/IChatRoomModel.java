package com.cosmos.photonim.imbase.chat.chatroom.iroom;

import com.cosmos.photonim.imbase.chat.ChatModel;

public abstract class IChatRoomModel extends ChatModel {
    public abstract void leaveRoom(String rId, ChatRoomListener listener);

    public interface ChatRoomListener {
        void onLeaveResult(boolean result);
    }

}
