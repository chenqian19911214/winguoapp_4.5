package com.winguo.pay.modle.bean;

import com.winguo.pay.modle.store.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/1/9.
 */

public  class UserInfosBean implements Serializable{
    /**
     * addressId : 1804
     * areaCode : 3633
     * areaName : 天河区
     * cityCode : 1601
     * cityName : 广州市
     * dinfo_address : 体育东路羊城国贸西塔
     * dinfo_mobile : 18402010281
     * dinfo_received_name : 张三
     * dinfo_tel :
     * dinfo_zip : 516400
     * isDefaultAddress : 1
     * provinceCode : 19
     * provinceName : 广东
     * townCode : 35747
     * townName : 暨南大学
     */

    public List<com.winguo.pay.modle.store.ItemBean> item;


    @Override
    public String toString() {
        return "UserInfosBean{" +
                "item=" + item +
                '}';
    }
}
