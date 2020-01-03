package com.cosmos.photonim.imbase.utils.recycleadapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by fanqiang on 2019/4/16.
 */
public class RvViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> sparseArray;
    private View itemView;

    public RvViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        sparseArray = new SparseArray<>();
    }

    public View getView(int viewId) {
        View view = sparseArray.get(viewId);
        if (view == null) {
            View viewById = itemView.findViewById(viewId);
            sparseArray.put(viewId, viewById);
            return viewById;
        }
        return view;
    }

    public View getItemView() {
        return itemView;
    }
}
