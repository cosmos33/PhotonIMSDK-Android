package com.momo.demo.main.forward.iforward;


import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.momo.demo.main.forward.ForwardData;

import java.util.List;
import java.util.Map;


public abstract class IForwardPresenter<V extends IForwardView, M extends IChatModel> extends IPresenter<V, M> {

    public IForwardPresenter(V iView) {
        super(iView);
    }

    public abstract void sendMsgToMulti(ChatData chatData, Map<String, ForwardData> selectedData);

    public abstract void loadContacts();

    @Override
    public V getEmptyView() {
        return (V) new IForwardView() {
            @Override
            public void showContactsEmptyView() {

            }

            @Override
            public void loadContacts(List<ForwardData> contactsData) {

            }

            @Override
            public void updateOtherInfo(JsonResult result, ForwardData forwardData) {

            }

            @Override
            public IPresenter getIPresenter() {
                return null;
            }

            @Override
            public RecyclerView getRecycleView() {
                return null;
            }

            @Override
            public RvBaseAdapter getAdapter() {
                return null;
            }
        };
    }

    public abstract void getOthersInfo(String userId, ForwardData forwardData);
}
