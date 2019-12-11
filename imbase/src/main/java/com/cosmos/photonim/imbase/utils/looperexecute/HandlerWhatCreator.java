package com.cosmos.photonim.imbase.utils.looperexecute;

/**
 * Created by fanqiang on 2018/12/11.
 */
public class HandlerWhatCreator {
    private static HandlerWhatCreator ourInstance;

    public static HandlerWhatCreator getInstance() {
        if (ourInstance == null) {
            ourInstance = new HandlerWhatCreator();
        }
        return ourInstance;
    }

    private int generateId = 0;

    private HandlerWhatCreator() {
    }

    public int getNextId() {
        ++generateId;
        if (generateId == Integer.MAX_VALUE) {
            generateId = 1;
        }
        return generateId;
    }
}
