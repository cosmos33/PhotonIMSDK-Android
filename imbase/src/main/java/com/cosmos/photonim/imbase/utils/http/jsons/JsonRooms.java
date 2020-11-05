package com.cosmos.photonim.imbase.utils.http.jsons;

import java.util.List;

public class JsonRooms implements JsonRequestResult {

    /**
     * ec : 0
     * em : success
     * errcode : 0
     * errmsg : success
     * timesec : 1591342165
     * data : {"lists":[{"name":"测试房间1000001","gid":"1000001","avatar":"https://img.momocdn.com/banner/89/2B/892B9CFA-396F-4A1C-CB04-3ACCEB387E9320200605.jpg"},{"name":"测试房间1000002","gid":"1000002","avatar":"https://img.momocdn.com/banner/0A/FB/0AFB9122-AEB3-DC0E-0441-B73DF7E0A92C20200605.jpg"},{"name":"测试房间1000003","gid":"1000003","avatar":"https://img.momocdn.com/banner/C4/77/C477FD7F-ECF6-DFB6-28FD-1BDFFB6158AF20200605.jpg"}]}
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
        private List<ListsBean> lists;

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            /**
             * name : 测试房间1000001
             * gid : 1000001
             * avatar : https://img.momocdn.com/banner/89/2B/892B9CFA-396F-4A1C-CB04-3ACCEB387E9320200605.jpg
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
