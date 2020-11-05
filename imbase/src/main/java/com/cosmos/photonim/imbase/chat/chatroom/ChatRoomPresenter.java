package com.cosmos.photonim.imbase.chat.chatroom;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.messagebody.PhotonIMCustomBody;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.chatroom.iroom.IChatRoomModel;
import com.cosmos.photonim.imbase.chat.chatroom.iroom.IChatRoomPresenter;
import com.cosmos.photonim.imbase.chat.chatroom.iroom.IChatRoomView;
import com.cosmos.photonim.imbase.utils.looperexecute.CustomRunnable;
import com.cosmos.photonim.imbase.utils.looperexecute.MainLooperExecuteUtil;

public class ChatRoomPresenter extends IChatRoomPresenter<IChatRoomView, IChatRoomModel> {
    private final int JOIN_ROOM_TIMEOUT = 2000;
    private volatile boolean isQuiting;

    public ChatRoomPresenter(IChatRoomView iView) {
        super(iView);
    }

    @Override
    public void leaveRoom(final String rId) {
        if (isQuiting) {
            return;
        }
        isQuiting = true;
        getiModel().leaveRoom(rId, new IChatRoomModel.ChatRoomListener() {
            @Override
            public void onLeaveResult(boolean result) {
                if (result) {
                    sdkLeave(rId);
                } else {
                    getIView().toast("leave room failed");
                    isQuiting = false;
                }
            }
        });
    }

    @Override
    public void joinRoom(String chatWith) {
        PhotonIMClient.getInstance().sendJoinRoom(chatWith, JOIN_ROOM_TIMEOUT, new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int i, String s, long l) {
                getIView().toast(String.format("join room %s(sdk:%s)", (i == 0) + "", s));
            }
        });
    }

    @Override
    public void sendJoinText() {
        sendTextInner(String.format("%s加入了房间", loginUserId));
    }

    public void sendQuitText() {
        sendTextInner(String.format("%s退出了房间", loginUserId));
    }

    private void sendTextInner(String content) {
        PhotonIMClient.getInstance().sendMessage(getJoinTextMessage(content), JOIN_ROOM_TIMEOUT, new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int i, String s, long l) {

            }
        });
    }

    private PhotonIMMessage getJoinTextMessage(String content) {
        PhotonIMCustomBody customBody = new PhotonIMCustomBody();
        customBody.data = String.format(content).getBytes();
        ChatData chatData = new ChatData.Builder()
                .from(loginUserId)
                .to(chatWith)
                .msgType(PhotonIMMessage.RAW)
                .chatType(PhotonIMMessage.ROOM)
                .msgBody(customBody).build();
        return chatData.convertToIMMessage();
    }

    private void sdkLeave(String rId) {
        sendQuitText();
        PhotonIMClient.getInstance().sendQuitRoom(rId, JOIN_ROOM_TIMEOUT, new PhotonIMClient.PhotonIMSendCallback() {
            @Override
            public void onSent(int code, String msg, long retTime) {
                if (code == 0) {
                    CustomRunnable customRunnable = new CustomRunnable.Builder().runnable(new Runnable() {
                        @Override
                        public void run() {
                            getIView().back();
                        }
                    }).build();
                    MainLooperExecuteUtil.getInstance().post(customRunnable);
                } else {
                    getIView().toast(String.format("leave room failed(sdk:%s)", msg));
                }
                isQuiting = false;
            }
        });
    }

    @Override
    public IChatRoomModel generateIModel() {
        return new ChatRoomModel();
    }
}
