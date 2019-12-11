package com.cosmos.photonim.imbase.utils.event;

public class OnDBChanged {
    public int event;
    public int chatType;
    public String chatWith;

    public OnDBChanged(int event, int chatType, String chatWith) {
        this.event = event;
        this.chatType = chatType;
        this.chatWith = chatWith;
    }
}
