package com.cosmos.photonim.imbase.utils.dbhelper.sessiontest;


import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(tableName = "sesstiontest", primaryKeys = {"chatWith", "chatType", "userId"})
public class SessionTest {
    @NonNull
    private String chatWith = "";
    @NonNull
    private int chatType = 0;
    @NonNull
    private String userId = "";

    public SessionTest() {
    }

    @NonNull
    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(@NonNull String chatWith) {
        this.chatWith = chatWith;
    }
}