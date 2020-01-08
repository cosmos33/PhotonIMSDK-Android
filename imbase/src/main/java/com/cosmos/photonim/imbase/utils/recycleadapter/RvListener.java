package com.cosmos.photonim.imbase.utils.recycleadapter;

import android.view.View;

/**
 * Created by fanqiang on 2019/4/16.
 */
interface RvListener<T> {
    void onClick(View view, T data, int position);

    void onLongClick(View view, T data, int position);
}
