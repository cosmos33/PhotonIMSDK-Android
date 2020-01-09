package com.cosmos.photonim.imbase.chat.searchhistory.adapter;

import android.text.SpannableString;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.emoji.EmojiUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.StringUtils;
import com.cosmos.photonim.imbase.utils.TimeUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class SearchData implements ItemData {
    public int chatType;
    public String chatWith;
    public String id;
    public SpannableString snippetContent;
    public String timeContent;
    public String nickName;
    public String icon;
    public int position;
    public String userId;

    public SearchData(PhotonIMMessage photonIMMessage, String matchPrefixPattern, String matchPostfixPattern,
                      String matchPrefix, String matchPostfix) {
        chatType = photonIMMessage.chatType;
        chatWith = photonIMMessage.chatWith;
        Profile profile = DBHelperUtils.getInstance().findProfile(photonIMMessage.from);
        id = photonIMMessage.id;
        if (photonIMMessage.from.equals(ImBaseBridge.getInstance().getUserId())) {
            nickName = "我";
            icon = ImBaseBridge.getInstance().getMyIcon();
        } else {
            nickName = profile == null ? photonIMMessage.from : profile.getName();
            icon = profile == null ? "" : profile.getIcon();
        }
        SpannableString spannableString = StringUtils.changeColor(photonIMMessage.snippetContent,
                matchPrefixPattern, matchPostfixPattern, matchPrefix, matchPostfix, ImBaseBridge.getInstance().getApplication().getResources().getColor(R.color.btn_normal));
        snippetContent = EmojiUtils.generateEmojiSpan(spannableString);
        timeContent = photonIMMessage.time == 0 ? null : TimeUtils.getTimeFormat(photonIMMessage.time);
        userId = photonIMMessage.from;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_SEARCH;
    }
}
