package com.cosmos.photonim.imbase.chat.emoji;

import java.util.List;

public class EmojiJson {


    private List<EmojiBean> emojis;

    public List<EmojiBean> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<EmojiBean> emojis) {
        this.emojis = emojis;
    }

    public static class EmojiBean {
        /**
         * credentialName : [龇牙]
         * resId : R.id.emoji_ciya
         */

        private String credentialName;
        private String resId;

        public String getCredentialName() {
            return credentialName;
        }

        public void setCredentialName(String credentialName) {
            this.credentialName = credentialName;
        }

        public String getResId() {
            return resId;
        }

        public void setResId(String resId) {
            this.resId = resId;
        }
    }
}
