package com.momo.demo.main.groupmemberselected;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class GroupMembersData implements ItemData {
    private boolean selected;
    private String icon;
    private String name;
    private String id;
    private boolean showCb = true;
    private int itemType = Constants.ITEM_TYPE_GROUP_MEMBER_SELECTED;
    private int position;

    public GroupMembersData(String icon, String name, String id) {
        this.icon = icon;
        this.name = name;
        this.id = id;
    }

    public GroupMembersData(String icon, String name, String id, int itemType) {
        this.icon = icon;
        this.name = name;
        this.id = id;
        this.itemType = itemType;
    }

    public GroupMembersData(String icon, String name, String id, boolean showCb, int itemType) {
        this.icon = icon;
        this.name = name;
        this.id = id;
        this.showCb = showCb;
        this.itemType = itemType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isShowCb() {
        return showCb;
    }

    public void setShowCb(boolean showCb) {
        this.showCb = showCb;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
