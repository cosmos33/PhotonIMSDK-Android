package com.cosmos.photonim.imbase.chat;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.chatset.ChatSetActivity;

public class ChatSingleActivity extends ChatBaseActivity {

    @Override
    protected void onInfoClick() {
        ChatSetActivity.startActivity(this, chatWith, singleChatUserIcon);
    }

    @Override
    protected String getChatIcon(PhotonIMMessage msg) {
        ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
        String loginUserId = "";
        if (businessListener != null) {
            loginUserId = businessListener.getUserId();
        }
        return msg.from.equals(loginUserId) ? myIcon : singleChatUserIcon;
    }

    @Override
    protected boolean isGroup() {
        return false;
    }
}
