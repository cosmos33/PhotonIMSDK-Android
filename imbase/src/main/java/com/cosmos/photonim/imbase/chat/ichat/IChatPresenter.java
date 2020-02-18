package com.cosmos.photonim.imbase.chat.ichat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class IChatPresenter<V extends IChatView, M extends IChatModel> extends IPresenter<V, M> {

    public IChatPresenter(V iView) {
        super(iView);
    }

    public abstract void loadLocalHistory(String anchorMsgId,
                                          boolean beforeAuthor, boolean asc,
                                          int size, String myId);

    public abstract void loadAfterSearchMsgId(String searchMsgId, boolean beforeAuthor, boolean asc, int size);

    public abstract void loadAllHistory(int size, long beginTimeStamp);

    public abstract void loadAllHistory(int size, long beginTimeStamp, long endTimeStamp);
//    public abstract void loadRemoteHistory();

//    public abstract void sendText(int chatType, String content, String chatWith, String fromId, String toId, String icon);

//    public abstract void reSendText(int chatType, String msgId, String content, String chatWith, String fromId, String toId, String icon);

//    public abstract void sendPic(int chatType, String absolutePath, String chatWith, String fromId, String toId, String icon);

//    public abstract void reSendPic(int chatType, String msgId, String absolutePath, String chatWith, String fromId, String toId, String icon);

//    public abstract void sendVoice(int chatType, String absolutePath, long time, String chatWith, String fromId, String toId, String icon);

//    public abstract void reSendVoice(int chatType, String msgId, String absolutePath, long time, String chatWith, String fromId, String toId, String icon);

    public abstract void revertMsg(ChatData data);

    public abstract void sendReadMsg(ChatData messageData);


    public abstract File startRecord(Context context);

    public abstract void stopRecord();

    public abstract void play(Context context, String fileName);

    public abstract void cancelPlay();


    public abstract void cancelRecord();

    public abstract void downLoadFile(ChatData chatData);

    public abstract void updateExtra(ChatData messageData);

    public abstract void removeChat(int chatType, String chatWith, String id);

    public abstract void getInfo(ChatData chatData);

    public abstract void clearData();

    @Override
    public V getEmptyView() {
        return (V) new IChatView() {


            @Override
            public void onRevertResult(ChatData data1, int error, String msg) {

            }

            @Override
            public void onGetChatVoiceFileResult(ChatData data, String path) {

            }

            @Override
            public void updateUnreadStatus(ChatData data) {

            }

            @Override
            public void onGetIcon(ChatData chatData, String iconUrl, String name) {

            }

            @Override
            public void onRecordFailed() {

            }

            @Override
            public void notifyItemChanged(int listPostion) {

            }

            @Override
            public void notifyItemInserted(int listPostion) {

            }

            @Override
            public String getChatIcon(PhotonIMMessage msg) {
                return null;
            }

            @Override
            public void scrollToPosition(int i) {

            }

            @Override
            public String getVideoFilePath() {
                return null;
            }

            @Override
            public void setRefreshing(boolean b) {

            }

            @Override
            public void notifyDataSetChanged() {

            }

            @Override
            public void notifyItemRangeInserted(int i, int size) {

            }

            @Override
            public void toast(int chat_msg_nomore) {

            }

            @Override
            public boolean isGroup() {
                return false;
            }

            @Override
            public ArrayList<AtEditText.Entity> getAtList() {
                return null;
            }

            @Override
            public void onGetDraft(String draft) {

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

    public abstract void destoryVoiceHelper();

    public abstract void reSendMsg(ChatData chatData);

    public abstract void deleteMsg(ChatData data);

    public abstract void onReceiveMsg(PhotonIMMessage msg);

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void onCreate(int chatType, String chatWith, String myIcon, String searchMsgId);

    public abstract void onDestory();

    public abstract List<ChatData> initData();

    public abstract void loadHistory();

    public abstract void loadMore();

    public abstract void onSendChatData(ChatDataWrapper chatDataWrapper);

    public abstract void removeData(ChatData chatData);

    public abstract int getImageUrls(ChatData chatData, ArrayList<ChatData> urls);

    public abstract void sendText(String content);

    public abstract void onWindowFocusChanged(boolean hasFocus);

    public abstract void onSaveDraft(String trim);

    public abstract void getDraft();

}
