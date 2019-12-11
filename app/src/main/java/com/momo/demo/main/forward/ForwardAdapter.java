package com.momo.demo.main.forward;


import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.util.List;

public class ForwardAdapter extends RvBaseAdapter {
    public ForwardAdapter(List baseDataList, String chatWith, ForwardItem.UpdateOtherInfoListener updateOtherInfoListener) {
        super(baseDataList);
        addItemType(new ForwardItem(chatWith, updateOtherInfoListener));
    }
}
