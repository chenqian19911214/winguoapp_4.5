package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class ItemBean implements Serializable {
    public String value;
    public String label;

    @Override
    public String toString() {
        return "ItemBean{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}