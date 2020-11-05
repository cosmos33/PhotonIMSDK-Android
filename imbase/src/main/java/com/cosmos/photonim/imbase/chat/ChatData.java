package com.cosmos.photonim.imbase.chat;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.PhotonIMSession;
import com.cosmos.photon.im.messagebody.PhotonIMAudioBody;
import com.cosmos.photon.im.messagebody.PhotonIMBaseBody;
import com.cosmos.photon.im.messagebody.PhotonIMCustomBody;
import com.cosmos.photon.im.messagebody.PhotonIMFileBody;
import com.cosmos.photon.im.messagebody.PhotonIMImageBody;
import com.cosmos.photon.im.messagebody.PhotonIMLocationBody;
import com.cosmos.photon.im.messagebody.PhotonIMTextBody;
import com.cosmos.photon.im.messagebody.PhotonIMVideoBody;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.emoji.EmojiUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.FileUtils;
import com.cosmos.photonim.imbase.utils.StringUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class ChatData implements ItemData, Parcelable, Cloneable {
    public static final int CHAT_STATUS_SENTED = 1;
    public static final int CHAT_STATUS_READ = 2;
    public static final int CHAT_STATUS_FAILED = 3;
    public static final int FILE_MAX_SIZE = 100 * 1024 * 1024;

    private String msgId;
    private String icon;
    private String content;
    private SpannableString contentShow;
    private long mediaTime;
    //    private String mediaTimeShow;
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
    private boolean testSend;
    private Location location;
    private String fileName;
    private String fileSize;
    private long fileSizeL;
    private int hashAlg;
    private String hash;
    private long videoTimeL;
    private String videoTime;
    private String videoCover;
    private int progress;
    private double videowhRatio;
    private boolean showProgress;

    private String thumbnailUrl;
    private boolean isChecked;
    private byte[] data;

    private int customArg1;
    private int customArg2;
    private byte[] customData;
    private int customDataSize;
//    private MsgExtra extra;


    private ChatData(Builder builder) {
        msgId = builder.msgId;
//        icon = builder.icon;
        content = builder.content;
        contentShow = EmojiUtils.generateEmojiSpan(content);
        mediaTime = builder.voiceDuration;
        time = builder.time;
//        fileSize = builder.fileSize;
//        extra = builder.extra;
        msgStatus = builder.msgStatus;
//        setIllegal(builder.illegal);
        chatWith = builder.chatWith;
        from = builder.from;
        fromName = builder.fromName;
        to = builder.to;
        icon = builder.icon;
        fileUrl = builder.fileUrl;
        localFile = builder.localFile;
        chatType = builder.chatType;
        notic = builder.notic;
        msgType = builder.msgType;
//        setListPostion(builder.listPostion);
        itemType = builder.itemType;
        timeContent = builder.timeContent;
        atType = builder.atType;
        msgAtList = builder.msgAtList;
        remainHistory = builder.remainHistory;
        testSend = builder.testSend;
        location = builder.location;
        fileName = builder.fileName;
        fileSizeL = builder.fileSizeL;
        fileSize = builder.fileSize == null ? SizeUtils.getSize(builder.fileSizeL) : builder.fileSize;
        hashAlg = builder.hashAlg;
        hash = builder.hash;
        videoTimeL = builder.videoTimeL;
        videoTime = Utils.videoTime(videoTimeL);
        videoCover = builder.videoCover;
        thumbnailUrl = builder.thumbnailUrl;

        customArg1 = builder.customArg1;
        customArg2 = builder.customArg2;
        customData = builder.customData;
        customDataSize = builder.customDataSize;


    }

//    public static ChatData getForwardChatData(ChatData chatData) {
//        ChatData result = null;
//        try {
//            result = (ChatData) chatData.clone();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    public int getCustomArg1(){
        return customArg1;
    }
    public int getCustomArg2(){
        return customArg2;
    }

    public byte[] getCustomData(){
        return customData;
    }

    public int getCustomDataSize(){
        return customDataSize;
    }

    public int getHashAlg(){
        return hashAlg;
    }

    public String getHash(){
        return hash;
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

    public void setContent(String content) {
        this.content = content;
        contentShow = EmojiUtils.generateEmojiSpan(content);
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

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

    public boolean isLocalFileToLarge() {
        if (fileSizeL >= FILE_MAX_SIZE)
            return true;
        if (TextUtils.isEmpty(localFile)) {
            return false;
        }
        File file = new File(localFile);
        if (!file.exists()) {
            return false;
        }
        return file.length() >= FILE_MAX_SIZE;
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

    public boolean isTestSend() {
        return testSend;
    }

    public Location getLocation() {
        return location;
    }

    //    public String getMediaTimeShow() {
    //        return mediaTimeShow;
    //    }

    public String getVideoTime() {
        return videoTime;
    }

    public long getVideoTimeL() {
        return videoTimeL;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int downloadProgress) {
        this.progress = downloadProgress;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public byte[] getData() {
        return customData;
    }

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
        switch (msgType) {
            case PhotonIMMessage.UNKNOW:
                if (msgStatus == PhotonIMMessage.RECALL){
                    notic = photonIMMessage.notic;
                }
                break;
            case PhotonIMMessage.RAW:
                PhotonIMCustomBody customBody = new PhotonIMCustomBody();
                customBody.srcDescription = "这是一个自定义消息呀，弟弟";
                customBody.arg1 = customArg1;
                customBody.arg2 = customArg2;
                customBody.data = customData;
                customBody.size = customDataSize;
                photonIMMessage.body = customBody;
                break;
            case PhotonIMMessage.TEXT:
                PhotonIMTextBody textBody = new PhotonIMTextBody();
                textBody.content = content;
                photonIMMessage.body = textBody;
                break;
            case PhotonIMMessage.IMAGE:
                PhotonIMImageBody imageBody = new PhotonIMImageBody();
                imageBody.srcDescription = "这是一个图片消息呀，弟弟";
                imageBody.url = StringUtils.getTextContent(fileUrl);
                imageBody.localFile = localFile;
                imageBody.thumbUrl = thumbnailUrl;
//                imageBody.hashAlg = hashAlg;
//                imageBody.hash = hash;
                photonIMMessage.body = imageBody;
                break;
            case PhotonIMMessage.AUDIO:
                PhotonIMAudioBody audioBody = new PhotonIMAudioBody();
                audioBody.srcDescription = "这是一个语音消息呀，弟弟";
                audioBody.url = StringUtils.getTextContent(fileUrl);
                audioBody.audioTime = mediaTime;
                audioBody.localFile = localFile;
//                audioBody.hashAlg = hashAlg;
//                audioBody.hash = hash;
                photonIMMessage.body = audioBody;
                break;
            case PhotonIMMessage.VIDEO:
                PhotonIMVideoBody videoBody = new PhotonIMVideoBody();
                videoBody.srcDescription = "这是一个视频消息呀，弟弟";
                videoBody.videoTime = videoTimeL;
                videoBody.url = fileUrl;
                videoBody.localFile = localFile;
                videoBody.coverUrl = videoCover;
//                videoBody.hashAlg = hashAlg;
//                videoBody.hash = hash;
                photonIMMessage.body = videoBody;
                break;
            case PhotonIMMessage.FILE:
                PhotonIMFileBody fileBody = new PhotonIMFileBody();
//                fileBody.fileName = fileName;
                fileBody.srcDescription = "这是一个文件消息呀，弟弟";
                fileBody.localFile = localFile;
                fileBody.url = fileUrl;
                fileBody.size = fileSizeL;
                fileBody.hashAlg = hashAlg;
                fileBody.hash = hash;
                photonIMMessage.body = fileBody;
                break;
            case PhotonIMMessage.LOCATION:
                PhotonIMLocationBody locationBody = new PhotonIMLocationBody();
                locationBody.srcDescription = "这是一个位置消息呀，弟弟";
                if (location != null) {
                    locationBody.lat = location.lat;
                    locationBody.lng = location.lng;
                    locationBody.address = location.address;
                    locationBody.detailedAddress = location.detailedAddress;
                }
                photonIMMessage.body = locationBody;
                break;
        }
        photonIMMessage.msgAtList = msgAtList;
        photonIMMessage.atType = atType;
//        getMsgAtStatus(photonIMMessage);
//        photonIMMessage.extra = getExtra();
        return photonIMMessage;
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

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public double getVideowhRatio() {
        return videowhRatio;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getFileSizeL() {
        return fileSizeL;
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
        private boolean testSend;
        private Location location;
        private PhotonIMBaseBody body;
        private String fileName;
        private long fileSizeL;
        private String fileSize;
        private int hashAlg;
        private String hash;
        private long videoTimeL;
        private String videoCover;
        private double videowhRatio;

        private String thumbnailUrl;

        private int customArg1;
        private int customArg2;
        private byte[] customData;
        private int customDataSize;

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

        public Builder fileName(String val) {
            fileName = val;
            return this;
        }

        public Builder fileSizeL(long val) {
            fileSizeL = val;
            return this;
        }

        public Builder fileSize(String val) {
            fileSize = val;
            return this;
        }

        public Builder hashAlg(int val){
            hashAlg = val;
            return this;
        }

        public Builder hash(String val){
            hash = val;
            return this;
        }

        public Builder videoTime(long var) {
            videoTimeL = var;
            return this;
        }

        public Builder videoCover(String var) {
            videoCover = var;
            return this;
        }

        public Builder videowhRatio(double var) {
            videowhRatio = var;
            return this;
        }

        public Builder thumbnailUrl(String var) {
            thumbnailUrl = var;
            return this;
        }

        public ChatData build() {
            if (body != null) {
                switch (msgType) {
                    case PhotonIMMessage.UNKNOW:
                        break;
                    case PhotonIMMessage.RAW:
                        PhotonIMCustomBody customBody = (PhotonIMCustomBody)body;
                        this.customArg1 = customBody.arg1;
                        this.customArg2 = customBody.arg2;
                        this.customData = customBody.data;
                        this.customDataSize = customBody.size;
                        break;
                    case PhotonIMMessage.TEXT:
                        PhotonIMTextBody textBody = (PhotonIMTextBody) body;
                        this.content = textBody.content;
                        break;
                    case PhotonIMMessage.IMAGE:
                        PhotonIMImageBody imageBody = (PhotonIMImageBody) body;
                        this.fileUrl = imageBody.url;
                        this.localFile = imageBody.localFile;
                        this.thumbnailUrl = imageBody.thumbUrl;
//                        this.hashAlg = imageBody.hashAlg;
//                        this.hash = imageBody.hash;
                        break;
                    case PhotonIMMessage.AUDIO:
                        PhotonIMAudioBody audioBody = (PhotonIMAudioBody) body;
                        this.voiceDuration = audioBody.audioTime;
                        this.localFile = audioBody.localFile;
                        this.fileUrl = audioBody.url;
//                        this.hashAlg = audioBody.hashAlg;
//                        this.hash = audioBody.hash;
                        break;
                    case PhotonIMMessage.VIDEO:
                        PhotonIMVideoBody videoBody = (PhotonIMVideoBody) body;
                        this.videoTimeL = videoBody.videoTime;
                        this.localFile = videoBody.localFile;
                        this.fileUrl = videoBody.url;
                        this.videoCover = videoBody.coverUrl;
//                        this.hashAlg = videoBody.hashAlg;
//                        this.hash = videoBody.hash;
                        break;
                    case PhotonIMMessage.FILE:
                        PhotonIMFileBody fileBody = (PhotonIMFileBody) body;
                        this.localFile = fileBody.localFile;
                        this.fileUrl = fileBody.url;
                        this.fileSizeL = fileBody.size;
                        this.fileName = FileUtils.getFileName(fileBody.localFile);
                        this.hashAlg = fileBody.hashAlg;
                        this.hash = fileBody.hash;
                        break;
                    case PhotonIMMessage.LOCATION:
                        if (this.location == null) {
                            location = new Location();
                        }
                        PhotonIMLocationBody locationBody = (PhotonIMLocationBody) body;
                        this.location.lat = locationBody.lat;
                        this.location.lng = locationBody.lng;
                        this.location.address = locationBody.address;
                        this.location.detailedAddress = locationBody.detailedAddress;
                        break;
                }
            }
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

        public Builder testSend(boolean testSend) {
            this.testSend = testSend;
            return this;
        }

        public Builder lat(double lat) {
            if (this.location == null) {
                location = new Location();
            }
            location.lat = lat;
            return this;
        }

        public Builder lng(double lng) {
            if (this.location == null) {
                location = new Location();
            }
            location.lng = lng;
            return this;
        }

        public Builder address(String address) {
            if (this.location == null) {
                location = new Location();
            }
            location.address = address;
            return this;
        }

        public Builder detailAddress(String detailAddress) {
            if (this.location == null) {
                location = new Location();
            }
            location.detailedAddress = detailAddress;
            return this;
        }


        public Builder customArg1(int arg1){
            customArg1 = arg1;
            return this;
        }

        public Builder customArg2(int arg2){
            customArg2 = arg2;
            return this;
        }

        public Builder customData(byte[] data){
            customData = data;
            return this;
        }

        public Builder customDataSize(int size){
            customDataSize = size;
            return this;
        }



        public Builder msgBody(PhotonIMBaseBody body) {
            this.body = body;
            return this;
        }
    }

    public static class Location implements Parcelable {
        public int coordinateSystem;  //坐标系
        public double lng;   // 经度
        public double lat;   // 纬度
        public String address = ""; // 坐标地址名称
        public String detailedAddress = ""; //坐标详细地址名称


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.coordinateSystem);
            dest.writeDouble(this.lng);
            dest.writeDouble(this.lat);
            dest.writeString(this.address);
            dest.writeString(this.detailedAddress);
        }

        public Location() {
        }

        protected Location(Parcel in) {
            this.coordinateSystem = in.readInt();
            this.lng = in.readDouble();
            this.lat = in.readDouble();
            this.address = in.readString();
            this.detailedAddress = in.readString();
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel source) {
                return new Location(source);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };
    }

    @Override
    protected ChatData clone() throws CloneNotSupportedException {
        return (new Builder()
                .msgStatus(PhotonIMMessage.SENDING)
                .itemType(Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT)
                .icon(ImBaseBridge.getInstance().getMyIcon())
                .voiceDuration(mediaTime)
                .msgType(msgType)
                .chatType(chatType)
                .chatWith(chatWith)
                .content(content)
                .from(from)
                .to(to)
                .fileUrl(fileUrl)
                .time(time)
                .localFile(localFile)
                .msgId(msgId)
//                .fileSize(fileSize)
                .fileName(fileName)
                .detailAddress(location == null ? null : location.detailedAddress)
                .address(location == null ? null : location.address)
                .lat(location == null ? 0 : location.lat)
                .lng(location == null ? 0 : location.lng)
                .customArg1(customArg1)
                .customArg2(customArg2)
                .customData(customData)
                .customDataSize(customDataSize)
                .build());
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
        dest.writeString(this.notic);
        dest.writeInt(this.atType);
        dest.writeStringList(this.msgAtList);
        dest.writeByte(this.remainHistory ? (byte) 1 : (byte) 0);
        dest.writeByte(this.testSend ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.fileName);
        dest.writeString(this.fileSize);
        dest.writeLong(this.fileSizeL);
        dest.writeLong(this.videoTimeL);
        dest.writeString(this.videoTime);
        dest.writeString(this.videoCover);
        dest.writeInt(this.progress);
        dest.writeDouble(this.videowhRatio);
        dest.writeByte(this.showProgress ? (byte) 1 : (byte) 0);
        dest.writeString(this.thumbnailUrl);
        dest.writeInt(this.hashAlg);
        dest.writeString(this.hash);
    }

    protected ChatData(Parcel in) {
        this.msgId = in.readString();
        this.icon = in.readString();
        this.content = in.readString();
//        this.contentShow = in.readParcelable(SpannableString.class.getClassLoader());
        this.mediaTime = in.readLong();
        this.time = in.readLong();
        this.msgStatus = in.readInt();
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
        this.notic = in.readString();
        this.atType = in.readInt();
        this.msgAtList = in.createStringArrayList();
        this.remainHistory = in.readByte() != 0;
        this.testSend = in.readByte() != 0;
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.fileName = in.readString();
        this.fileSize = in.readString();
        this.fileSizeL = in.readLong();
        this.videoTimeL = in.readLong();
        this.videoTime = in.readString();
        this.videoCover = in.readString();
        this.progress = in.readInt();
        this.videowhRatio = in.readDouble();
        this.showProgress = in.readByte() != 0;
        this.thumbnailUrl = in.readString();
        this.hashAlg = in.readInt();
        this.hash = in.readString();
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

    @Override
    public String toString() {
        return "ChatData{" +
                "msgId='" + msgId + '\'' +
                ", icon='" + icon + '\'' +
                ", content='" + content + '\'' +
                ", contentShow=" + contentShow +
                ", mediaTime=" + mediaTime +
                ", time=" + time +
                ", msgStatus=" + msgStatus +
                ", chatWith='" + chatWith + '\'' +
                ", from='" + from + '\'' +
                ", fromName='" + fromName + '\'' +
                ", to='" + to + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", localFile='" + localFile + '\'' +
                ", chatType=" + chatType +
                ", msgType=" + msgType +
                ", listPostion=" + listPostion +
                ", itemType=" + itemType +
                ", timeContent='" + timeContent + '\'' +
                ", notic='" + notic + '\'' +
                ", atType=" + atType +
                ", msgAtList=" + msgAtList +
                ", remainHistory=" + remainHistory +
                ", testSend=" + testSend +
                ", location=" + location +
                ", fileName='" + fileName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileSizeL=" + fileSizeL +
                ", videoTimeL=" + videoTimeL +
                ", videoTime='" + videoTime + '\'' +
                ", videoCover='" + videoCover + '\'' +
                ", progress=" + progress +
                ", videowhRatio=" + videowhRatio +
                ", showProgress=" + showProgress +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
