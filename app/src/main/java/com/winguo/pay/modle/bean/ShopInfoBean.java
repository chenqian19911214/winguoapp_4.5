package com.winguo.pay.modle.bean;


import java.io.Serializable;

/**
 * Created by Admin on 2017/1/11.
 */

public  class ShopInfoBean implements Serializable{
    /**
     * goodsItems : {"data":{"icon":{"content":"http://g1.img.winguo.com/group1/M00/4E/79/wKgAi1hrOGWAGjiRAANPH9VxDQA31..jpg","modifyTime":1483421797},"largess_qty":0,"cost_price":24,"color_name":"","distributor_id":0,"color_alias":"","skuid":89767,"discount":"0.0%","size":"蓝","num":1,"stock":1000,"price":24,"color":"","discount_type":"不打折","item_id":87129,"name":"新款小米移动电源手机通用型 10400Amh","size_alias":"尺码","share_rate":"0.00%"},"temp_id":1688}
     */

    public TempItemsBean tempItems;
    public String shopName;
    public int shopId;
    public int is_repaired;
    public int is_returned;
    public int goodsNum;
    public int has_invoice;
    public double totalPrice;
    public ExpressMethodBean expressMethodBean;
    public String userMsg;


    @Override
    public String toString() {
        return "ShopInfoBean{" +
                "tempItems=" + tempItems +
                ", shopName='" + shopName + '\'' +
                ", shopId=" + shopId +
                ", is_repaired=" + is_repaired +
                ", is_returned=" + is_returned +
                ", goodsNum=" + goodsNum +
                ", has_invoice=" + has_invoice +
                ", totalPrice=" + totalPrice +
                ", expressMethodBean=" + expressMethodBean +
                ", userMsg=" + userMsg +
                '}';
    }
}
