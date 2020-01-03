package com.cosmos.photonim.imbase.chat.ichat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class IChatPresenter<V extends IChatView, M extends IChatModel> extends IPresenter<V, M> {

    public IChatPresenter(V iView) {
        super(iView);
    }

    public abstract void loadLocalHistory(int chatType, String chatWith, String anchorMsgId,
                                          boolean beforeAuthor, boolean asc,
                                          int size, String myId);

    public abstract void loadAllHistory(int chatType, String chatWith, int size, long beginTimeStamp);

    public abstract void loadAllHistory(int chatType, String chatWith, int size, long beginTimeStamp, long endTimeStamp);
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

    public abstract void getVoiceFile(ChatData chatData);

    public abstract void updateExtra(ChatData messageData);

    public abstract void removeChat(int chatType, String chatWith, String id);

    public abstract void getInfo(ChatData chatData);

    @Override
    public V getEmptyView() {
        return (V) new IChatView() {
            @Override
            public void onloadHistoryResult(List<ChatData> chatData, Map<String, ChatData> chatDataMap) {

            }


            @Override
            public void onRevertResult(ChatData data1, int error, String msg) {

            }

            @Override
            public void onGetChatVoiceFileResult(ChatData data, String path) {

            }

            @Override
            public void onRecordFinish(long duration) {

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
            public void onDeleteMsgResult(ChatData chatData) {

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

    public abstract ChatData sendMsg(ChatData.Builder chatDataBuilder);

    public abstract void reSendMsg(ChatData chatData);

    public abstract void deleteMsg(ChatData data);
}
