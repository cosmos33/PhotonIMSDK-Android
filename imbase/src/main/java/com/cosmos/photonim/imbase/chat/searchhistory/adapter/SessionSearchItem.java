package com.cosmos.photonim.imbase.chat.searchhistory.adapter;

import com.cosmos.photonim.imbase.session.SessionData;
import com.cosmos.photonim.imbase.session.SessionItem;

public class SessionSearchItem extends SessionItem {
    public SessionSearchItem(UpdateOtherInfoListener updateOtherInfoListener) {
        super(updateOtherInfoListener);
    }

    @Override
    protected CharSequence getMsgContent(SessionData sessionData) {
        return super.getMsgContent(sessionData);
    }
}
