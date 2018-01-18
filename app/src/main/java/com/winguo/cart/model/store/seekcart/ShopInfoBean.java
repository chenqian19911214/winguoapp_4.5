package com.winguo.cart.model.store.seekcart;

import com.winguo.cart.bean.GoodsItemsBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */

public  class ShopInfoBean implements Serializable {
    public int shopId;
    public String shopName;
    public int goodsNum;
    public double totalPrice;
    public int has_invoice;
    public int is_repaired;
    public int is_returned;
    public GoodsItemsBean goodsItems;
    protected boolean isChoosed;

    @Override
    public String toString() {
        return "ShopInfoBean{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", goodsNum=" + goodsNum +
                ", totalPrice=" + totalPrice +
                ", has_invoice=" + has_invoice +
                ", is_repaired=" + is_repaired +
                ", is_returned=" + is_returned +
                ", goodsItems=" + goodsItems +
                ", isChoosed=" + isChoosed +
                '}';
    }
    public boolean isChoosed()
    {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed)
    {
        this.isChoosed = isChoosed;
    }


}
