package com.winguo.pay.modle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/1/11.
 */

public  class ExpressageBean implements Serializable{
    /**
     * price : 0
     * name : 卖家承担运费
     * code : 0
     */

    public List<ItemBean> item;



    @Override
    public String toString() {
        return "ExpressageBean{" +
                "item=" + item +

                '}';
    }
}
