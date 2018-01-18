package com.winguo.pay.modle.store.selfbean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/5/2.
 */

public class SelfShopInfo implements Serializable {
    public String shopName;
    public int shopId;
    public int is_repaired;
    public int is_returned;
    public int goodsNum;
    public int has_invoice;
    public double totalPrice;
    public SelfGoodsItem goodsItem;

    @Override
    public String toString() {
        return "SelfShopInfo{" +
                "shopName='" + shopName + '\'' +
                ", shopId=" + shopId +
                ", is_repaired=" + is_repaired +
                ", is_returned=" + is_returned +
                ", goodsNum=" + goodsNum +
                ", has_invoice=" + has_invoice +
                ", totalPrice=" + totalPrice +
                ", goodsItem=" + goodsItem +
                '}';
    }
}
