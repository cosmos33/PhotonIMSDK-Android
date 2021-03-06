package com.momo.demo.main.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListener;
import com.momo.demo.R;
import com.momo.demo.main.contacts.group.GroupActivity;
import com.momo.demo.main.contacts.single.OnLineUserActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends RvBaseFragment {
    private RecyclerView recyclerView;

    private List<ContactsData> baseDataList;
    private ContactsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (adapter == null) {
            baseDataList = new ArrayList<>();
            baseDataList.add(new ContactsData("", "附近在线的人"));
            baseDataList.add(new ContactsData("", "附近的群组"));
            adapter = new ContactsAdapter(baseDataList);
            adapter.setRvListener(new RvListener() {
                @Override
                public void onClick(View view, Object data, int position) {
                    switch (view.getId()) {
                        case R.id.item_contact_llRoot:
                            if (position == 0) {
                                Intent intent = new Intent(getActivity(), OnLineUserActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), GroupActivity.class);
                                startActivity(intent);
                            }
                            break;
                    }
                }

                @Override
                public void onLongClick(View view, Object data, int position) {

                }
            });
        }
        return adapter;
    }
}
