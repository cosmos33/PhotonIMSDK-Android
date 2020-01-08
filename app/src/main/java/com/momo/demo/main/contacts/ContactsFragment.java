package com.momo.demo.main.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.momo.demo.R;
import com.momo.demo.main.contacts.group.GroupActivity;
import com.momo.demo.main.contacts.single.OnLineUserActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends RvBaseFragment {
    private RecyclerView recyclerView;

    private List<ContactsData> baseDataList;
    private ContactsAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
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
            adapter.setRvListener(new RvListenerImpl() {
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
            });
        }
        return adapter;
    }
}
