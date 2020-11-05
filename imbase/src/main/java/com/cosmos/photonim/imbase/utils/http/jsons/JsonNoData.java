package com.cosmos.photonim.imbase.utils.http.jsons;

public class JsonNoData implements JsonRequestResult {
    /**
     * ec : 0
     * em : success
     * errcode : 0
     * errmsg : success
     * timesec : 1591342039
     * data : {}
     */

    private int ec;
    private String em;
    private int errcode;
    private String errmsg;
    private int timesec;
    private DataBean data;

    @Override
    public boolean success() {
        return errcode == 0;
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

    public static class DataBean {
    }
}