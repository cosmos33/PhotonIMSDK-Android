package com.momo.demo.main.contacts.single;

import com.cosmos.photonim.imbase.utils.Constants;
import com.momo.demo.main.contacts.single.ionline.IOnlineUserModel;
import com.momo.demo.main.contacts.single.ionline.IOnlineUserPresenter;
import com.momo.demo.main.contacts.single.ionline.IOnlineUserView;

import java.util.List;

public class OnlineUserPresenter extends IOnlineUserPresenter<IOnlineUserView, IOnlineUserModel> {
    public OnlineUserPresenter(IOnlineUserView iView) {
        super(iView);
    }

    @Override
    public void loadContacts() {
        generateIModel().loadContacts(Constants.ITEM_TYPE_ONLINEUSER, new IOnlineUserModel.OnLoadContactListener() {
            @Override
            public void onLoadContact(List<OnlineUserData> contactsData) {
                if (contactsData == null || contactsData.size() == 0) {
                    getIView().showContactsEmptyView();
                } else {
                    getIView().loadContacts(contactsData);
                }
            }
        });
    }

    @Override
    public IOnlineUserModel generateIModel() {
        return new OnlineUserModel();
    }
}
