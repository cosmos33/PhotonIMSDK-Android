package com.cosmos.photonim.imbase.utils.http.jsons;

public class JsonOtherInfo implements JsonRequestResult {

    private int ec;
    private String em;
    private int errcode;
    private String errmsg;
    private long timesec;
    private DataBean data;

    public int getEc() {
        return ec;
    }

    public void setEc(int ec) {
        this.ec = ec;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public long getTimesec() {
        return timesec;
    }

    public void setTimesec(long timesec) {
        this.timesec = timesec;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public boolean success() {
        return ec == 0;
    }

    public static class DataBean {

        private ProfileBean profile;

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public static class ProfileBean {

            private String nickname;
            private long regTime;
            private String avatar;
            private String userId;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public long getRegTime() {
                return regTime;
            }

            public void setRegTime(long regTime) {
                this.regTime = regTime;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }


            @Override
            public String toString() {
                return "ProfileBean{" +
                        "nickname='" + nickname + '\'' +
                        ", regTime=" + regTime +
                        ", avatar='" + avatar + '\'' +
                        ", userId='" + userId + '\'' +
                        '}';
            }


        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "profile=" + profile +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonOtherInfo{" +
                "ec=" + ec +
                ", em='" + em + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", timesec=" + timesec +
                ", data=" + data +
                '}';
    }
}
