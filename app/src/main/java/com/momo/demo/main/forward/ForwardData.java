package com.momo.demo.main.forward;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class ForwardData implements ItemData {
    public static final int ONLINE = 201;
    public static final int SESSION = 202;
    private String nickName;
    private String icon;
    private boolean selected;
    private String userId;
    private int position;
    private int type;//1为最近会话；2为通讯录
    private boolean showTitle;
    private int chatType;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getIcon() {
        return icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void showTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_FORWARD;
    }
}
