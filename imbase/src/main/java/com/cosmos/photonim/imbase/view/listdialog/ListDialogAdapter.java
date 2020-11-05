package com.cosmos.photonim.imbase.view.listdialog;

import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class ListDialogAdapter extends RvBaseAdapter {
    public ListDialogAdapter(List baseDataList) {
        super(baseDataList);
        addItemType(new ListDialogItem());
    }
}
