package com.cosmos.photonim.imbase.utils.http.jsons;

public class JsonSetNickName implements JsonRequestResult {

    private int ec;
    private String em;
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
    }

    @Override
    public String toString() {
        return "JsonSetNickName{" +
                "ec=" + ec +
                ", em='" + em + '\'' +
                ", timesec=" + timesec +
                ", data=" + data +
                '}';
    }
}
