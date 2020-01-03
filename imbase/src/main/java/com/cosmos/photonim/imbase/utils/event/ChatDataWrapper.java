package com.cosmos.photonim.imbase.utils.event;

import com.cosmos.photonim.imbase.chat.ChatData;

public class ChatDataWrapper {
    public ChatData chatData;
    public int code;
    public String msg;

    public ChatDataWrapper(ChatData chatData, int code, String msg) {
        this.chatData = chatData;
        this.code = code;
        this.msg = msg;
    }
}
