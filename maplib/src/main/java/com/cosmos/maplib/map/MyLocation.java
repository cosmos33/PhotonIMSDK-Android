package com.cosmos.maplib.map;

import java.io.Serializable;

public class MyLocation implements Serializable {
    public int coordinateSystem;  //坐标系
    public double lng;   // 经度
    public double lat;   // 纬度
    public String address; // 坐标地址名称
    public String detailedAddress;//坐标详细地址名称
}
