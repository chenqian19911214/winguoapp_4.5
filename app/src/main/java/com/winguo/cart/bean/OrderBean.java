package com.winguo.cart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */

public  class OrderBean implements Serializable{
    public DetailBean detail;
    public int max_largess_qty;
    public int goodsNum;
    public double totalprice;
    public int pay_method;


    @Override
    public String toString() {
        return "OrderBean{" +
                "detail=" + detail +
                ", max_largess_qty=" + max_largess_qty +
                ", goodsNum=" + goodsNum +
                ", totalprice=" + totalprice +
                ", pay_method=" + pay_method +
                '}';
    }
}
