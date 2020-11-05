package com.cosmos.photonim.imbase.utils.event;

public class ClearUnReadStatus {
    public int chatType;
    public String chatWith;

    public ClearUnReadStatus(int chatType, String chatWith) {
        this.chatType = chatType;
        this.chatWith = chatWith;
    }
}
