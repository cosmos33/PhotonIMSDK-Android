package com.cosmos.photonim.imbase.chat.chatroom;

import android.os.Bundle;
import androidx.annotation.Nullable;


import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.chatroom.iroom.IChatRoomView;
import com.cosmos.photonim.imbase.utils.event.IMStatus;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;

public class ChatRoomActivity extends IChatRoomView<ChatRoomPresenter> {
    private ProcessDialogFragment processDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.sendJoinText();
    }

    @Override
    protected void onInfoClick() {
        ImBaseBridge.getInstance().onRoomInfoClick(this, chatWith, name);
    }

    @Override
    public String getChatIcon(PhotonIMMessage msg) {
        return null;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    protected boolean loadDataFromDB() {
        return false;
    }

    @Override
    public ChatRoomPresenter getIPresenter() {
        return new ChatRoomPresenter(this);
    }

    @Override
    public void showDialog() {
        processDialogFragment = ProcessDialogFragment.getInstance();
        processDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void dismissDialog() {
        if (processDialogFragment != null) {
            processDialogFragment.dismiss();
        }
    }

    @Override
    public void back() {
        this.finish();
    }

    @Override
    protected void onArrowLeftClick() {
        presenter.leaveRoom(chatWith);
    }

    @Override
    public void onChatBackPressed() {
        presenter.leaveRoom(chatWith);
    }

    @Override
    public void onBaseImStatusChange(IMStatus status) {
        super.onBaseImStatusChange(status);
        if (status.status == PhotonIMClient.IM_STATE_AUTH_SUCCESS) {
            presenter.joinRoom(chatWith);
        }
    }
}
