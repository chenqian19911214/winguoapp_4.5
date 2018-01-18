package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class PriceBean implements Serializable{
    public String regular;
    public String special;

    @Override
    public String toString() {
        return "PriceBean{" +
                "regular=" + regular +
                ", special='" + special + '\'' +
                '}';
    }
}
