package com.winguo.pay.modle.bean;

import com.winguo.pay.modle.bean.DetailBean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/11.
 */

public  class OrderBean implements Serializable{
    public int max_largess_qty;
    /**
     * shopInfo : {"tempItems":{"goodsItems":{"data":{"icon":{"content":"http://g1.img.winguo.com/group1/M00/4E/79/wKgAi1hrOGWAGjiRAANPH9VxDQA31..jpg","modifyTime":1483421797},"largess_qty":0,"cost_price":24,"color_name":"","distributor_id":0,"color_alias":"","skuid":89767,"discount":"0.0%","size":"蓝","num":1,"stock":1000,"price":24,"color":"","discount_type":"不打折","item_id":87129,"name":"新款小米移动电源手机通用型 10400Amh","size_alias":"尺码","share_rate":"0.00%"},"temp_id":1688}},"shopName":"恒博电子专卖店","shopId":1736,"is_repaired":0,"is_returned":0,"goodsNum":1,"has_invoice":0,"totalPrice":24}
     */

    public DetailBean detail;
    public String pay_method;
    public double totalprice;
    public int goodsNum;


    @Override
    public String toString() {
        return "OrderBean{" +
                "max_largess_qty=" + max_largess_qty +
                ", detail=" + detail +
                ", pay_method='" + pay_method + '\'' +
                ", totalprice=" + totalprice +
                ", goodsNum=" + goodsNum +
                '}';
    }
}