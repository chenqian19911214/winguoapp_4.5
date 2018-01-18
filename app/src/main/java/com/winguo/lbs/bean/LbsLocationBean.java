package com.winguo.lbs.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 定位相关信息
 */

public class LbsLocationBean implements Serializable {
    //返回码
    private int locType;
    //城市
    private String city;
    //经度
    private double longitude;
    //纬度
    private double latitude;
    //详细位置
    private String describe;

    @Override
    public String toString() {
        return "LbsLocationBean{" +
                "locType=" + locType +
                ", city='" + city + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", describe='" + describe + '\'' +
                '}';
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
