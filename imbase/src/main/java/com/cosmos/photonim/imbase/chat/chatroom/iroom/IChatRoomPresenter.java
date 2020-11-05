package com.cosmos.photonim.imbase.chat.chatroom.iroom;

import com.cosmos.photonim.imbase.chat.ChatPresenter;

public abstract class IChatRoomPresenter<V extends IChatRoomView, M extends IChatRoomModel> extends ChatPresenter<V, M> {
    public IChatRoomPresenter(IChatRoomView iView) {
        super(iView);
    }

    public abstract void leaveRoom(String rId);

    public abstract void joinRoom(String chatWith);

    public abstract void sendJoinText();
}
