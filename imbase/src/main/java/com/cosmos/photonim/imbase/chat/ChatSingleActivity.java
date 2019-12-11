package com.cosmos.photonim.imbase.chat;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.LoginInfo;
import com.cosmos.photonim.imbase.chat.chatset.ChatSetActivity;

public class ChatSingleActivity extends ChatBaseActivity {

    @Override
    protected void onInfoClick() {
        ChatSetActivity.startActivity(this, chatWith, singleChatUserIcon);
    }

    @Override
    protected String getChatIcon(PhotonIMMessage msg) {
        return msg.from.equals(LoginInfo.getInstance().getUserId()) ? myIcon : singleChatUserIcon;
    }

    @Override
    protected boolean isGroup() {
        return false;
    }
}
