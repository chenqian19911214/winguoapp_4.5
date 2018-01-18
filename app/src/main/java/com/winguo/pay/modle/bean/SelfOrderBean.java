package com.winguo.pay.modle.bean;

import com.winguo.pay.modle.store.selfbean.SelfShopInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/4/28.
 */

public class SelfOrderBean implements Serializable{
    public List<SelfShopInfo> shopInfos;
    public String pay_method;
    public double totalprice;
    public int goodsNum;
    public int max_largess_qty;

    @Override
    public String toString() {
        return "SelfOrderBean{" +
                "shopInfos=" + shopInfos +
                ", pay_method='" + pay_method + '\'' +
                ", totalprice=" + totalprice +
                ", goodsNum=" + goodsNum +
                ", max_largess_qty=" + max_largess_qty +
                '}';
    }
}
