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
    public String getChatIcon(PhotonIMMessage msg) {
        return msg.from.equals(ImBaseBridge.getInstance().getUserId()) ? ImBaseBridge.getInstance().getMyIcon() : singleChatUserIcon;
    }

    @Override
    public boolean isGroup() {
        return false;
    }
}
