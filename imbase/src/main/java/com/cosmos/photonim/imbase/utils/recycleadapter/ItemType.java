package com.cosmos.photonim.imbase.utils.recycleadapter;

/**
 * Created by fanqiang on 2019/4/16.
 */
interface ItemType<T extends ItemData> {

    boolean openClick();

    boolean openLongClick();

    int getType();

    int getLayout();

    void fillContent(RvViewHolder rvViewHolder, int position, T data);

    boolean isCurrentType(T data, int position);

    int[] getOnClickViews();

    int[] getOnLongClickViews();
}
