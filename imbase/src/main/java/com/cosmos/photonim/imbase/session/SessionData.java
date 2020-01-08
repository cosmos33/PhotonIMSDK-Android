package com.cosmos.photonim.imbase.session;

import android.text.SpannableString;

import com.cosmos.photon.im.PhotonIMSession;
import com.cosmos.photonim.imbase.chat.emoji.EmojiUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.TimeUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

import java.util.HashMap;
import java.util.Map;

public class SessionData implements ItemData {
    private String chatWith;
    private int chatType;
    private boolean ignoreAlert;
    private boolean sticky;
    private int unreadCount;
    private int lastMsgType;
    private String lastMsgId;
    private String lastMsgFr;
    private String lastMsgFrName;
    private boolean updateFromInfo;
    private String lastMsgTo;
    private String lastMsgContent;
    private SpannableString lastMsgContentShow;
    private long lastMsgTime;
    private String timeContent;
    private int lastMsgStatus;
    private Map<String, String> extra;
    //    private Map<String, String> sessionExtra;//保存exta里的json信息
    private long orderId;
    private String draft;

    private int itemType;
    private int position;
    private boolean showAtTip;
    private SpannableString atMsg;
    private String nickName;
    private String icon;

    private SpannableString snippetContent;

    public SpannableString getAtMsg() {
        return atMsg;
    }

    public boolean isShowAtTip() {
        return showAtTip;
    }
//    public SessionData(PhotonIMMessage msg) {
//        chatWith = msg.chatWith;
//        chatType = msg.chatType;
//        lastMsgFr = msg.from;
//        lastMsgId = msg.id;
//        lastMsgTime = msg.time;
//        lastMsgTo = msg.to;
//        lastMsgType = msg.messageType;
//        itemType = Constants.ITEM_TYPE_MSG;
//        unreadCount = 1;
//    }

    public SessionData(PhotonIMSession session) {
        chatWith = session.chatWith;
        chatType = session.chatType;
        ignoreAlert = session.ignoreAlert;
        sticky = session.sticky;
        unreadCount = session.unreadCount;
        lastMsgType = session.lastMsgType;
        lastMsgFr = session.lastMsgFr;
        lastMsgTo = session.lastMsgTo;
        lastMsgContent = session.lastMsgContent;
        lastMsgContentShow = EmojiUtils.generateEmojiSpan(lastMsgContent);
        lastMsgTime = session.lastMsgTime;
        timeContent = lastMsgTime == 0 ? null : TimeUtils.getTimeFormat(lastMsgTime);
        lastMsgStatus = session.lastMsgStatus;
        extra = session.extra;
        orderId = session.orderId;
        draft = session.draft;
    }

    private SessionData(Builder builder) {
        setChatWith(builder.chatWith);
        setChatType(builder.chatType);
        setIgnoreAlert(builder.ignoreAlert);
        setSticky(builder.sticky);
        setUnreadCount(builder.unreadCount);
        setLastMsgType(builder.lastMsgType);
        setLastMsgId(builder.lastMsgId);
        setLastMsgFr(builder.lastMsgFr);
        lastMsgFrName = builder.lastMsgFrName;
        updateFromInfo = builder.updateFromInfo;
        setLastMsgTo(builder.lastMsgTo);
        setLastMsgContent(builder.lastMsgContent);
        lastMsgContentShow = EmojiUtils.generateEmojiSpan(lastMsgContent);
        setLastMsgTime(builder.lastMsgTime);
        timeContent = lastMsgTime == 0 ? null : TimeUtils.getTimeFormat(lastMsgTime);
        setLastMsgStatus(builder.lastMsgStatus);
        setExtra(builder.extra);
//        sessionExtra = builder.sessionExtra;
        setOrderId(builder.orderId);
        setDraft(builder.draft);
        setItemType(builder.itemType);
        position = builder.position;
        showAtTip = builder.showAtTip;
        atMsg = builder.generateAtMsg;
        nickName = builder.nickName;
        icon = builder.icon;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public boolean isIgnoreAlert() {
        return ignoreAlert;
    }

    public void setIgnoreAlert(boolean ignoreAlert) {
        this.ignoreAlert = ignoreAlert;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getLastMsgType() {
        return lastMsgType;
    }

    public void setLastMsgType(int lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public String getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(String lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public String getLastMsgFr() {
        return lastMsgFr;
    }

    public void setLastMsgFr(String lastMsgFr) {
        this.lastMsgFr = lastMsgFr;
    }

    public void setLastMsgFrName(String name) {
        this.lastMsgFrName = name;
    }

    public String getLastMsgTo() {
        return lastMsgTo;
    }

    public void setLastMsgTo(String lastMsgTo) {
        this.lastMsgTo = lastMsgTo;
    }

    public String getLastMsgContent() {
        return lastMsgContent;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public int getLastMsgStatus() {
        return lastMsgStatus;
    }

    public void setLastMsgStatus(int lastMsgStatus) {
        this.lastMsgStatus = lastMsgStatus;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public SpannableString getSnippetContent() {
        return snippetContent;
    }

    public String getTimeContent() {
        return timeContent;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

//    public String getIcon() {
//        if (extra == null) {
//            return null;
//        }
////        return sessionExtra.icon;
//        return extra.get("icon");
//    }
//
//    public String getNickName() {
//        if (extra == null) {
//            return null;
//        }
//        return extra.get("nickname");
//    }
//
//    public void setIcon(String avatar) {
//        if (extra == null) {
//            extra = new HashMap<>();
//        }
//
//        extra.put("icon", avatar);
//    }
//
//    public void setNickName(String nickname) {
//        if (extra == null) {
//            extra = new HashMap<>();
//        }
//        extra.put("nickname", nickname);
//    }

    public PhotonIMSession convertToPhotonIMSession() {
        PhotonIMSession photonIMSession = new PhotonIMSession();
        photonIMSession.chatType = chatType;
        photonIMSession.chatWith = chatWith;
        photonIMSession.draft = draft;
//        photonIMSession.extra = extra == null ? new Gson().toJson(sessionExtra) : extra;
        photonIMSession.extra = extra == null ? new HashMap<>() : extra;
        photonIMSession.ignoreAlert = ignoreAlert;
        photonIMSession.lastMsgContent = lastMsgContent;
        photonIMSession.lastMsgFr = lastMsgFr;
        photonIMSession.lastMsgId = lastMsgId;
        photonIMSession.lastMsgStatus = lastMsgStatus;
        photonIMSession.lastMsgTime = lastMsgTime;
        photonIMSession.lastMsgTo = lastMsgTo;
        photonIMSession.lastMsgType = lastMsgType;
        photonIMSession.orderId = orderId;
        photonIMSession.sticky = sticky;
        photonIMSession.unreadCount = unreadCount;
        return photonIMSession;
    }

//    public Map<String, String> getExtra(String nickName, String icon) {
//        Map<String, String> map = new HashMap<>();
//        map.put("nickname", nickName);
//        map.put("icon", icon);
//        return map;
//    }
//
//    public void setExtra(String nickName, String icon) {
//        extra = new HashMap<>();
//        extra.put("nickname", nickName);
//        extra.put("icon", icon);
//        this.extra = extra;
//
//    }

    public int getItemPosition() {
        return position;
    }

    public void setItemPosition(int position) {
        this.position = position;
    }

    public String getLastMsgFrName() {
        return lastMsgFrName;
    }

    public boolean isUpdateFromInfo() {
        return updateFromInfo;
    }

    public void setUpdateFromInfo(boolean updateFromInfo) {
        this.updateFromInfo = updateFromInfo;
    }

    public String getIcon() {
        return icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setIcon(String avatar) {
        this.icon = avatar;
    }

    public void setNickName(String nickname) {
        this.nickName = nickname;
    }

    public static final class Builder {
        private String chatWith;
        private int chatType;
        private boolean ignoreAlert;
        private boolean sticky;
        private int unreadCount;
        private int lastMsgType;
        private String lastMsgId;
        private String lastMsgFr;
        private String lastMsgFrName;
        private boolean updateFromInfo;
        private String lastMsgTo;
        private String lastMsgContent;
        private SpannableString lastMsgContentShow;
        private long lastMsgTime;
        private String timeContent;
        private int lastMsgStatus;

        private Map<String, String> extra;
        //        private Map<String, String> sessionExtra;
        private String icon;
        private String nickName;

        private long orderId;
        private String draft;
        private int itemType = Constants.ITEM_TYPE_MSG;
        private int position;
        private boolean showAtTip;
        private SpannableString atMsg;
        private SpannableString generateAtMsg;

        public Builder() {
        }

        public Builder chatWith(String val) {
            chatWith = val;
            return this;
        }

        public Builder chatType(int val) {
            chatType = val;
            return this;
        }

        public Builder ignoreAlert(boolean val) {
            ignoreAlert = val;
            return this;
        }

        public Builder sticky(boolean val) {
            sticky = val;
            return this;
        }

        public Builder unreadCount(int val) {
            unreadCount = val;
            return this;
        }

        public Builder lastMsgType(int val) {
            lastMsgType = val;
            return this;
        }

        public Builder lastMsgId(String val) {
            lastMsgId = val;
            return this;
        }

        public Builder lastMsgFr(String val) {
            lastMsgFr = val;
            return this;
        }

        public Builder lastMsgFrName(String val) {
            lastMsgFrName = val;
            return this;
        }

        public Builder updateFromInfo(boolean val) {
            updateFromInfo = val;
            return this;
        }

        public Builder lastMsgTo(String val) {
            lastMsgTo = val;
            return this;
        }

        public Builder lastMsgContent(String val) {
            lastMsgContent = val;
            return this;
        }

        public Builder lastMsgContentShow(SpannableString val) {
            lastMsgContentShow = val;
            return this;
        }

        public Builder lastMsgTime(long val) {
            lastMsgTime = val;
            return this;
        }

        public Builder timeContent(String val) {
            timeContent = val;
            return this;
        }

        public Builder lastMsgStatus(int val) {
            lastMsgStatus = val;
            return this;
        }

        public Builder extra(Map<String, String> val) {
            extra = val;
            return this;
        }

//        public Builder sessionExtra(Map<String, String> val) {
//            sessionExtra = val;
//            return this;
//        }

        public Builder orderId(long val) {
            orderId = val;
            return this;
        }

        public Builder draft(String val) {
            draft = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder itemType(int val) {
            itemType = val;
            return this;
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public Builder showAtTip(boolean val) {
            showAtTip = val;
            return this;
        }

        public Builder atMsg(SpannableString val) {
            atMsg = val;
            return this;
        }

        public Builder generateAtMsg(SpannableString val) {
            generateAtMsg = val;
            return this;
        }

        public SessionData build() {
            return new SessionData(this);
        }

    }

    public class SessionExtra {

        /**
         * icon :
         * nickname :
         */

        public String icon;
        public String nickname;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }


}
