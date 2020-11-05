package com.cosmos.photonim.imbase.view.listdialog;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class Listitemdata implements ItemData {
    private String content;

    public Listitemdata(String item) {
        this.content = item;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_LIST_DIALOG;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
