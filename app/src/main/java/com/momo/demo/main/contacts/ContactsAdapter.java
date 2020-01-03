package com.momo.demo.main.contacts;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class ContactsAdapter extends RvBaseAdapter {
    public ContactsAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new ContactsItem());
    }
}
