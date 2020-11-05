package com.momo.demo.main.contacts.single;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class OnlineUserData implements ItemData {
    private String userId;
    private String icon;
    private String nickName;
    private int type;//(1 单人, 2 群)
    private int itemType;
    private boolean selected;
    private int listPosition;

    private OnlineUserData(Builder builder) {
        userId = builder.userId;
        icon = builder.icon;
        nickName = builder.nickName;
        type = builder.type;
        itemType = builder.itemType;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUserId() {
        return userId;
    }

    public String getIcon() {
        return icon;
    }

    public String getNickName() {
        return nickName;
    }


    public int getItemType() {
        return itemType;
    }

    public int getType() {
        return type;
    }

    public static final class Builder {
        private String userId;
        private String icon;
        private String nickName;
        private int type;
        private int itemType = Constants.ITEM_TYPE_ONLINEUSER;

        public Builder() {
        }

        public Builder msgId(String val) {
            userId = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder nickName(String val) {
            nickName = val;
            return this;
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public OnlineUserData build() {
            return new OnlineUserData(this);
        }

        public Builder itemType(int val) {
            itemType = val;
            return this;
        }
    }
}
