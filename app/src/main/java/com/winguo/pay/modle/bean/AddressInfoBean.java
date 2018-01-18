package com.winguo.pay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/9.
 */

public class AddressInfoBean implements Serializable{

    /**
     * item : {"addressId":1804,"areaCode":3633,"areaName":"天河区","cityCode":1601,"cityName":"广州市","dinfo_address":"体育东路羊城国贸西塔","dinfo_mobile":18402010281,"dinfo_received_name":"黄成派","dinfo_tel":"","dinfo_zip":516400,"isDefaultAddress":1,"provinceCode":19,"provinceName":"广东","townCode":35747,"townName":"暨南大学"}
     */

    public UserInfosBean userInfos;


    @Override
    public String toString() {
        return "AddressInfoBean{" +
                "userInfos=" + userInfos +
                '}';
    }
}
