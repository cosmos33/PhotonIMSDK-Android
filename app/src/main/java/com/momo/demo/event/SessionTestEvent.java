package com.momo.demo.event;

import java.util.ArrayList;
import java.util.List;

public class SessionTestEvent {
    public List<Integer> chatTypeList;
    public List<String> chatWithList;

    public void addTest(int chatType, String userId) {
        if (chatTypeList == null) {
            chatTypeList = new ArrayList<>();
        }
        chatTypeList.add(chatType);
        if (chatWithList == null) {
            chatWithList = new ArrayList<>();
        }
        chatWithList.add(userId);
    }
}
