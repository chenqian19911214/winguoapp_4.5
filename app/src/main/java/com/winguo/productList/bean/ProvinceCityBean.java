package com.winguo.productList.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/16.
 */

public class ProvinceCityBean implements Serializable{

    public ProvinceBean province;


    @Override
    public String toString() {
        return "ProvinceCityBean{" +
                "province=" + province +
                '}';
    }
}
