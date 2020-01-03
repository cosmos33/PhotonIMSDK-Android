package com.cosmos.photonim.imbase.utils.event;

public class IMStatus {
    public int status;
    public String statusMsg;

    public IMStatus(int state, String statusMsg) {
        this.status = state;
        this.statusMsg = statusMsg;
    }
}
