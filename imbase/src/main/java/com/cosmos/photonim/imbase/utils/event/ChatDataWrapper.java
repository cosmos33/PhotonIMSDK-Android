package com.cosmos.photonim.imbase.utils.event;

import com.cosmos.photonim.imbase.chat.ChatData;

public class ChatDataWrapper {
    public static final int STATUS_SEND = 462;
    public static final int STATUS_DELETE = 463;
    public ChatData chatData;
    public int code;
    public String msg;
    public int status = STATUS_SEND;

    public ChatDataWrapper(ChatData chatData, int code, String msg) {
        this.chatData = chatData;
        this.code = code;
        this.msg = msg;
    }

    public ChatDataWrapper(ChatData chatData, int code, String msg, int status) {
        this.chatData = chatData;
        this.code = code;
        this.msg = msg;
        this.status = status;
    }
}
