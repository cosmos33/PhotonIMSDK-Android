package com.cosmos.photonim.imbase.utils.http.jsons;

public class JsonRegist implements JsonRequestResult {

    private int ec;
    private String em;
    private int errcode;
    private String errmsg;
    private long timesec;
    private DataBean data;

    @Override
    public boolean success() {
        return ec == 0;
    }

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

    public static class DataBean {

        private String sessionId;
        private String userId;
        private String username;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "sessionId='" + sessionId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonRegist{" +
                "ec=" + ec +
                ", em='" + em + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", timesec=" + timesec +
                ", data=" + data +
                '}';
    }
}
