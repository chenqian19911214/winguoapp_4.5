package com.winguo.productList.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/1/16.
 */

public  class CitiesBean implements Serializable{
    /**
     * code : 72
     * name : 朝阳区
     */

    public List<CityItemBean> item;


    @Override
    public String toString() {
        return "CitiesBean{" +
                "item=" + item +
                '}';
    }
}
