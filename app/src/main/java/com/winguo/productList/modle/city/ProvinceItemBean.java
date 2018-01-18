package com.winguo.productList.modle.city;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/16.
 */

public  class ProvinceItemBean implements Serializable{
    public int code;
    public String name;
    public CitiesBean cities;


    @Override
    public String toString() {
        return "ProvinceItemBean{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", cities=" + cities +
                '}';
    }
}
