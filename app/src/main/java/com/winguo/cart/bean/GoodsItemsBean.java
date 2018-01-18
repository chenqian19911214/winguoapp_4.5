package com.winguo.cart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public  class GoodsItemsBean implements Serializable{
    /**
     * skuid : 30959
     * icon : {"modifyTime":1442811022,"content":"http://g1.img.winguo.com/group1/M00/1B/D1/wKgAi1X_jI6AbXaZAAFYFRi8woY126.jpg"}
     * distributor_id : 0
     * share_rate : 0.00%
     * name : OPPO R7 金色 移动4G手机 双卡双待
     * item_id : 30963
     * size : 均码
     * color : FFFFFF
     * color_name : 默认
     * num : 1
     * largess_qty : 0
     * price : 1999
     * cost_price : 1999
     * stock : 100
     * limit_buy :
     * discount : 0.0%
     * discount_type : 不打折
     */

    public List<DataBean> data;


    @Override
    public String toString() {
        return "GoodsItemsBean{" +
                "data=" + data +
                '}';
    }
}