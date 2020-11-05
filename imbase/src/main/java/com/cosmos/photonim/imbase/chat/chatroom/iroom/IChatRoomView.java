package com.cosmos.photonim.imbase.chat.chatroom.iroom;

import com.cosmos.photonim.imbase.chat.ChatBaseActivity;

public abstract class IChatRoomView<T extends IChatRoomPresenter> extends ChatBaseActivity<T> {
    abstract public void showDialog();

    abstract public void dismissDialog();

    public abstract void back();
}
