package com.winguo.productList.modle.city;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/16.
 */

public  class CityItemBean implements Serializable {
    public int code;
    public String name;

    @Override
    public String toString() {
        return "CityItemBean{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
