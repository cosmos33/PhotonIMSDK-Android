package com.momo.demo.main.contacts.room.adapter;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class RoomData implements ItemData {

    /**
     * name : 测试房间1000001
     * gid : 1000001
     * avatar :
     */

    private String name;
    private String gid;
    private String avatar;

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_ROOM;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
