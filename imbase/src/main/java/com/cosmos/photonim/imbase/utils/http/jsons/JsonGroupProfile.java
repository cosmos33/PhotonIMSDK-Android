package com.cosmos.photonim.imbase.utils.http.jsons;

public class JsonGroupProfile implements JsonRequestResult {
    /**
     * ec : 0
     * em : success
     * errcode : 0
     * errmsg : success
     * timesec : 1568781980
     * data : {"profile":{"name":"测试群组1000001","gid":"1000001","avatar":"https://img.momocdn.com/banner/87/B4/87B42F89-354B-1F5F-10A6-63C775D9602220190918.jpg"}}
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
         * profile : {"name":"测试群组1000001","gid":"1000001","avatar":"https://img.momocdn.com/banner/87/B4/87B42F89-354B-1F5F-10A6-63C775D9602220190918.jpg"}
         */

        private ProfileBean profile;

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public static class ProfileBean {
            /**
             * name : 测试群组1000001
             * gid : 1000001
             * avatar : https://img.momocdn.com/banner/87/B4/87B42F89-354B-1F5F-10A6-63C775D9602220190918.jpg
             */

            private String name;
            private String gid;
            private String avatar;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
