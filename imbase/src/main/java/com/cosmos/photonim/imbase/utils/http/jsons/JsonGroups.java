package com.cosmos.photonim.imbase.utils.http.jsons;

import java.util.List;

public class JsonGroups implements JsonRequestResult {

    /**
     * ec : 0
     * em : success
     * errcode : 0
     * errmsg : success
     * timesec : 1568781731
     * data : {"lists":[{"nickName":"测试群组1000001","gid":"1000001","avatar":"https://img.momocdn.com/banner/87/B4/87B42F89-354B-1F5F-10A6-63C775D9602220190918.jpg"},{"nickName":"测试群组1000002","gid":"1000002","avatar":"https://img.momocdn.com/banner/D9/BE/D9BE4041-D931-D88C-DA98-0EBA1E220C5720190918.jpg"},{"nickName":"测试群组1000003","gid":"1000003","avatar":"https://img.momocdn.com/banner/B5/0F/B50F8436-1C38-6F8F-714F-F705DDCDFF5320190918.jpg"}]}
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
             * nickName : 测试群组1000001
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

            @Override
            public String toString() {
                return "ListsBean{" +
                        "nickName='" + name + '\'' +
                        ", gid='" + gid + '\'' +
                        ", avatar='" + avatar + '\'' +
                        '}';
            }
        }

    }

    @Override
    public String toString() {
        return "JsonGroups{" +
                "ec=" + ec +
                ", em='" + em + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", timesec=" + timesec +
                ", data=" + data +
                '}';
    }
}
