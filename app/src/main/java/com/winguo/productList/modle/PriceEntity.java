package com.winguo.productList.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/16.
 */

public class PriceEntity implements Serializable{
    /**
     * special :
     * regular : 39
     */
    public String special;
    public String regular;

    @Override
    public String toString() {
        return "PriceEntity{" +
                "special='" + special + '\'' +
                ", regular=" + regular +
                '}';
    }
}
