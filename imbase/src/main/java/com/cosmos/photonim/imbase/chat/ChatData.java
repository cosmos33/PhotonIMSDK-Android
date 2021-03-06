package com.cosmos.photonim.imbase.chat;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.PhotonIMSession;
import com.cosmos.photonim.imbase.chat.emoji.EmojiUtils;
import com.cosmos.photonim.imbase.utils.AtSpan;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

import java.io.Serializable;
import java.util.List;

public class ChatData implements ItemData, Parcelable {
    public static final int CHAT_STATUS_SENTED = 1;
    public static final int CHAT_STATUS_READ = 2;
    public static final int CHAT_STATUS_FAILED = 3;

    private String msgId;
    private String icon;
    private String content;
    private SpannableString contentShow;
    private long mediaTime;
    private long time;
    private int msgStatus;
    //    private boolean illegal;
    private String chatWith;
    private String from;
    private String fromName;
    private String to;
    private String fileUrl;
    private String localFile;
    private int chatType;// single group
    private int msgType;// Text image
    private int listPostion;//list中的位置
    private int itemType = Constants.ITEM_TYPE_CHAT_NORMAL_LEFT;
    private String timeContent;
    private String notic;
    private int atType;
    private List<String> msgAtList;
    private boolean remainHistory;
//    private MsgExtra extra;


    private ChatData(Builder builder) {
        msgId = builder.msgId;
//        icon = builder.icon;
        content = builder.content;
        contentShow = EmojiUtils.generateEmojiSpan(content);
        mediaTime = builder.voiceDuration;
        time = builder.time;
//        extra = builder.extra;
        setMsgStatus(builder.msgStatus);
//        setIllegal(builder.illegal);
        setChatWith(builder.chatWith);
        setFrom(builder.from);
        setFromName(builder.fromName);
        setTo(builder.to);
        setIcon(builder.icon);
        setFileUrl(builder.fileUrl);
        setLocalFile(builder.localFile);
        setChatType(builder.chatType);
        setNotic(builder.notic);
        msgType = builder.msgType;
//        setListPostion(builder.listPostion);
        itemType = builder.itemType;
        setTimeContent(builder.timeContent);
        atType = builder.atType;
        this.msgAtList = builder.msgAtList;
        this.remainHistory = builder.remainHistory;
    }

    public void setNotic(String notic) {
        this.notic = notic;
    }

    public String getNotic() {
        return notic;
    }

    public SpannableString getContentShow() {
        return contentShow;
    }

    public String getMsgId() {
        return msgId;
    }

//    public String getInfo() {
//        if (extra == null) {
//            return icon;
//        }
//        return extra.getInfo();
//    }

    public void setContent(String content) {
        this.content = content;
        contentShow = EmojiUtils.generateEmojiSpan(content);
    }

//    public void icon(String icon) {
//        if (extra == null) {
//            extra = new MsgExtra();
//        }
//        extra.icon = icon;
//    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

//    public boolean isIllegal() {
//        return illegal;
//    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

//    public void setIllegal(boolean illegal) {
//        this.illegal = illegal;
//    }


    public boolean isRemainHistory() {
        return remainHistory;
    }

    public String getFrom() {
        return from;
    }

    public String getFromName() {
        return fromName;
    }

    public String getTo() {
        return to;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFromName(String from) {
        this.fromName = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    public int getListPostion() {
        return listPostion;
    }

    public void setListPostion(int listPostion) {
        this.listPostion = listPostion;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public long getMediaTime() {
        return mediaTime;
    }

    public String getTimeContent() {
        return timeContent;
    }

    public void setTimeContent(String timeContent) {
        this.timeContent = timeContent;
    }

//    public String getExtra() {
//        if (extra == null) {
//            return null;
//        }
//        return new Gson().toJson(extra);
//    }

    public PhotonIMMessage convertToIMMessage() {
        PhotonIMMessage photonIMMessage = new PhotonIMMessage();
        photonIMMessage.id = msgId;
        photonIMMessage.chatWith = chatWith;
        photonIMMessage.from = from;
        photonIMMessage.to = to;
        photonIMMessage.time = time;
        photonIMMessage.messageType = msgType;
        photonIMMessage.status = msgStatus;
        photonIMMessage.chatType = chatType;
        photonIMMessage.content = content;
        photonIMMessage.mediaTime = mediaTime;
        photonIMMessage.fileUrl = fileUrl == null ? "" : fileUrl;
        photonIMMessage.thumbUrl = fileUrl == null ? "" : fileUrl;
        photonIMMessage.localFile = localFile;
        photonIMMessage.whRatio = 0;
        photonIMMessage.msgAtList = msgAtList;
        photonIMMessage.atType = atType;
//        getMsgAtStatus(photonIMMessage);
//        photonIMMessage.extra = getExtra();
        return photonIMMessage;
    }

    private void getMsgAtStatus(PhotonIMMessage photonIMMessage) {
        if (TextUtils.isEmpty(photonIMMessage.content)) {
            return;
        }
        if (!photonIMMessage.content.contains("@")) {
            return;
        }
        if (photonIMMessage.content.contains("@ 所有人")) {
            photonIMMessage.atType = PhotonIMMessage.MSG_AT_ALL;
//            photonIMMessage.msgAtList;
        } else {
            SpannableString spannableString = new SpannableString(photonIMMessage.content);
            AtSpan[] spans = spannableString.getSpans(0, photonIMMessage.content.length(), AtSpan.class);
            if (spans.length != 0) {
                for (AtSpan span : spans) {

                }
            }
        }
//        photonIMMessage.atType;
//        photonIMMessage.msgAtList;


    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public PhotonIMSession convertToImSession() {
        PhotonIMSession photonIMSession = new PhotonIMSession();
        photonIMSession.chatType = chatType;
        photonIMSession.chatWith = chatWith;
        photonIMSession.lastMsgContent = content;
        photonIMSession.lastMsgFr = from;
        photonIMSession.lastMsgId = msgId;
        photonIMSession.lastMsgStatus = msgStatus;
        photonIMSession.lastMsgTime = time;
        photonIMSession.lastMsgTo = to;
        photonIMSession.lastMsgType = msgType;
        photonIMSession.orderId = System.currentTimeMillis();
        return photonIMSession;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setTime(long currentTimeMillis) {
        this.time = currentTimeMillis;
    }


    public static final class Builder implements Serializable {
        private String msgId;
        //        private String icon;
        private String content;
        private long time;
        private int msgStatus;
        private boolean illegal = true;
        private int msgType;
        private String chatWith;
        private String from;
        private String fromName;
        private String to;
        private String fileUrl;
        private String localFile;
        private int chatType;
        private int listPostion;
        private int itemType;
        private String timeContent;
        private long voiceDuration;
        private String icon;
        private String notic;
        private int atType;
        private List<String> msgAtList;
        private boolean remainHistory;
//        private MsgExtra extra;

        public Builder() {
        }

        public Builder msgId(String val) {
            msgId = val;
            return this;
        }

//        public Builder icon(String val) {
//            icon = val;
//            return this;
//        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder time(long val) {
            time = val;
            return this;
        }

        public Builder msgStatus(int val) {
            msgStatus = val;
            return this;
        }

        public Builder illegal(boolean val) {
            illegal = val;
            return this;
        }

        public Builder msgType(int val) {
            msgType = val;
            return this;
        }

        public Builder chatWith(String val) {
            chatWith = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder fromName(String val) {
            fromName = val;
            return this;
        }

        public Builder to(String val) {
            to = val;
            return this;
        }

        public Builder fileUrl(String val) {
            fileUrl = val;
            return this;
        }

        public Builder localFile(String val) {
            localFile = val;
            return this;
        }

        public Builder chatType(int val) {
            chatType = val;
            return this;
        }

        public Builder listPostion(int val) {
            listPostion = val;
            return this;
        }

        public Builder itemType(int val) {
            itemType = val;
            return this;
        }

        public Builder timeContent(String val) {
            timeContent = val;
            return this;
        }

        public ChatData build() {
            return new ChatData(this);
        }

        public Builder voiceDuration(long val) {
            voiceDuration = val;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder notic(String notic) {
            this.notic = notic;
            return this;
        }

        public Builder remainHistory(boolean remainHistory) {
            this.remainHistory = remainHistory;
            return this;
        }

        public int getMsgType() {
            return msgType;
        }

        public Builder atType(int atType) {
            this.atType = atType;
            return this;
        }

        public Builder msgAtList(List<String> msgAtList) {
            this.msgAtList = msgAtList;
            return this;
        }

        //        public Builder extra(String extra) {
//            this.extra = new Gson().fromJson(extra, MsgExtra.class);
//            return this;
//        }
//
//
//        public Builder icon(String icon) {
//            if (extra == null) {
//                extra = new MsgExtra();
//            }
//            extra.icon = icon;
//            return this;
//        }
    }

    public static class MsgExtra implements Serializable {

        public String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msgId);
        dest.writeString(this.icon);
        dest.writeString(this.content);
//        dest.writeParcelable(this.contentShow, flags);
        dest.writeLong(this.mediaTime);
        dest.writeLong(this.time);
        dest.writeInt(this.msgStatus);
//        dest.writeByte(this.illegal ? (byte) 1 : (byte) 0);
        dest.writeString(this.notic);
        dest.writeString(this.chatWith);
        dest.writeString(this.from);
        dest.writeString(this.fromName);
        dest.writeString(this.to);
        dest.writeString(this.fileUrl);
        dest.writeString(this.localFile);
        dest.writeInt(this.chatType);
        dest.writeInt(this.msgType);
        dest.writeInt(this.listPostion);
        dest.writeInt(this.itemType);
        dest.writeString(this.timeContent);
    }

    protected ChatData(Parcel in) {
        this.msgId = in.readString();
        this.icon = in.readString();
        this.content = in.readString();
//        this.contentShow = in.readParcelable(SpannableString.class.getClassLoader());
        this.mediaTime = in.readLong();
        this.time = in.readLong();
        this.msgStatus = in.readInt();
//        this.illegal = in.readByte() != 0;
        this.notic = in.readString();
        this.chatWith = in.readString();
        this.from = in.readString();
        this.fromName = in.readString();
        this.to = in.readString();
        this.fileUrl = in.readString();
        this.localFile = in.readString();
        this.chatType = in.readInt();
        this.msgType = in.readInt();
        this.listPostion = in.readInt();
        this.itemType = in.readInt();
        this.timeContent = in.readString();
    }

    public static final Creator<ChatData> CREATOR = new Creator<ChatData>() {
        @Override
        public ChatData createFromParcel(Parcel source) {
            return new ChatData(source);
        }

        @Override
        public ChatData[] newArray(int size) {
            return new ChatData[size];
        }
    };
}
