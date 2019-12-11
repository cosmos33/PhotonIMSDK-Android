package com.cosmos.photonim.imbase.utils.http.jsons;

import java.util.List;

public class JsonMyInfo implements JsonRequestResult {

    /**
     * ec : 0
     * em : success
     * errcode : 0
     * errmsg : success
     * timesec : 1568782195
     * data : {"profile":{"username":"hah","nickname":"游客65398","regTime":1563881825,"avatar":"https://img.momocdn.com/banner/28/94/2894D45F-4DB0-B829-917C-51D4377C4DFE20190722.jpg","userId":"x10028"},"joinedGids":["1000001"]}
     */

    private int ec;
    private String em;
    private int errcode;
    private String errmsg;
    private int timesec;
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

    public int getTimesec() {
        return timesec;
    }

    public void setTimesec(int timesec) {
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
        return errcode == 0;
    }

    public static class DataBean {
        /**
         * profile : {"username":"hah","nickname":"游客65398","regTime":1563881825,"avatar":"https://img.momocdn.com/banner/28/94/2894D45F-4DB0-B829-917C-51D4377C4DFE20190722.jpg","userId":"x10028"}
         * joinedGids : ["1000001"]
         */

        private ProfileBean profile;
        private List<String> joinedGids;

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public List<String> getJoinedGids() {
            return joinedGids;
        }

        public void setJoinedGids(List<String> joinedGids) {
            this.joinedGids = joinedGids;
        }

        public static class ProfileBean {
            /**
             * username : hah
             * nickname : 游客65398
             * regTime : 1563881825
             * avatar : https://img.momocdn.com/banner/28/94/2894D45F-4DB0-B829-917C-51D4377C4DFE20190722.jpg
             * userId : x10028
             */

            private String username;
            private String nickname;
            private int regTime;
            private String avatar;
            private String userId;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getRegTime() {
                return regTime;
            }

            public void setRegTime(int regTime) {
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
                        "username='" + username + '\'' +
                        ", nickname='" + nickname + '\'' +
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
                    ", joinedGids=" + joinedGids +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonMyInfo{" +
                "ec=" + ec +
                ", em='" + em + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", timesec=" + timesec +
                ", data=" + data +
                '}';
    }
}
