package com.winguo.product.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class TitleBean implements Serializable {
    public String id;
    public String color_name;
    public List<String> name;

    @Override
    public String toString() {
        return "TitleBean{" +
                "id='" + id + '\'' +
                ", color_name='" + color_name + '\'' +
                ", name=" + name +
                '}';
    }
}
