package com.cosmos.photonim.imbase.utils.http.jsons;

import com.google.gson.annotations.SerializedName;

public class JsonGetGroupIgnoreInfo implements JsonRequestResult {
    @Override
    public boolean success() {
        return ec == 0;
    }


    private int ec;
    private String em;
    private int errcode;
    private String errmsg;
    private long timesec;
    private DataBean data;

    public void setEc(int ec) { this.ec = ec; }

    public int getEc(){
        return ec;
    }

    public void setEm(String em) { this.em = em; }

    public String getEm(){ return em; }

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


    public static class DataBean{

        @SerializedName("switch")
        private int switchX;

        public int getSwitchX() {
            return switchX;
        }

        public void setSwitchX(int switchX) {
            this.switchX = switchX;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "switchX=" + switchX +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonGetGroupIgnoreInfo{" +
                "ec=" + ec +
                ", em='" + em + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", timesec=" + timesec +
                ", data=" + data +
                '}';
    }

}
