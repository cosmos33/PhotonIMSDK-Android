package com.cosmos.photonim.imbase.view.listdialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.utils.recycleadapter.CreateRvHelper;
import com.cosmos.photonim.imbase.utils.recycleadapter.ICreateRv;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListDialogFragment extends DialogFragment implements ICreateRv {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private ListDialogAdapter listDialogAdapter;
    private OnHandleListener onHandleListener;
    private List<Listitemdata> listitemdata;
    private List<String> items;

    public static ListDialogFragment getInstance(OnHandleListener onHandleListener, List<String> items) {
        ListDialogFragment sessionDialogFragment = new ListDialogFragment();
        sessionDialogFragment.onHandleListener = onHandleListener;
        sessionDialogFragment.items = items;
        return sessionDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_session, container);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        ButterKnife.bind(this, view);
        new CreateRvHelper.Builder(this).build();
        return view;
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (listDialogAdapter == null) {
            listitemdata = new ArrayList<>();
            for (String item : items) {
                listitemdata.add(new Listitemdata(item));
            }
            listDialogAdapter = new ListDialogAdapter(listitemdata);
            listDialogAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    if (onHandleListener != null) {
                        onHandleListener.onItemClick(position);
                    }
                }
            });

        }
        return listDialogAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
    }

    public interface OnHandleListener {
        void onItemClick(int positon);
    }

}
