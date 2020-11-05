package com.momo.demo.main.contacts;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class ContactsData implements ItemData {
    private String icon;
    private String name;

    public ContactsData(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_CONTACT;
    }
}
