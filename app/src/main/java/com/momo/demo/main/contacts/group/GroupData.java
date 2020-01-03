package com.momo.demo.main.contacts.group;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class GroupData implements ItemData {
    private String name;
    private String icon;
    private String groupId;
    private boolean inGroup;

    private GroupData(Builder builder) {
        name = builder.name;
        icon = builder.icon;
        groupId = builder.groupId;
        inGroup = builder.inGroup;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isInGroup() {
        return inGroup;
    }

    public void setInGroup(boolean inGroup) {
        this.inGroup = inGroup;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_GROUP;
    }

    public static final class Builder {
        private String name;
        private String icon;
        private String groupId;
        private boolean inGroup;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder groupId(String val) {
            groupId = val;
            return this;
        }

        public Builder inGroup(boolean val) {
            inGroup = val;
            return this;
        }

        public GroupData build() {
            return new GroupData(this);
        }
    }
}
