package com.winguo.productList.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 */

public class FiltersItemBean implements Serializable{
    public String code;
    public ValuesEntity values;
    public String name;


    @Override
    public String toString() {
        return "FiltersItemBean{" +
                "code='" + code + '\'' +
                ", values=" + values +
                ", name='" + name + '\'' +
                '}';
    }
}
