package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Admin on 2017/2/14.
 */

public class InnerDataBean implements Serializable {
    public String qty;
    public String rate;

    @Override
    public String toString() {
        return "InnerDataBean{" +
                "qty=" + qty +
                ", rate='" + rate + '\'' +
                '}';
    }
}
