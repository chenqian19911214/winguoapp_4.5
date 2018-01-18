package com.winguo.cart.bean;


import com.winguo.cart.model.store.seekcart.IconBean;
import com.winguo.pay.modle.bean.ExpressMethodBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */

public  class DataBean implements Serializable{
    public int skuid;
    /**
     * modifyTime : 1442811022
     * content : http://g1.img.winguo.com/group1/M00/1B/D1/wKgAi1X_jI6AbXaZAAFYFRi8woY126.jpg
     */
    public IconBean icon;
    public int distributor_id;
    public String share_rate;
    public String name;
    public int item_id;
    public String size;
    public String color;
    public String color_name;
    public int num;
    public int largess_qty;
    public double price;
    public double cost_price;
    public int stock;
    public String limit_buy;
    public String discount;
    public String discount_type;
    protected boolean isChoosed;
    public ExpressMethodBean dispatchMethodBean;


    public boolean isChoosed()
    {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed)
    {
        this.isChoosed = isChoosed;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "skuid=" + skuid +
                ", icon=" + icon +
                ", distributor_id=" + distributor_id +
                ", share_rate='" + share_rate + '\'' +
                ", name='" + name + '\'' +
                ", item_id=" + item_id +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", color_name='" + color_name + '\'' +
                ", num=" + num +
                ", largess_qty=" + largess_qty +
                ", price=" + price +
                ", cost_price=" + cost_price +
                ", stock=" + stock +
                ", limit_buy='" + limit_buy + '\'' +
                ", discount='" + discount + '\'' +
                ", discount_type='" + discount_type + '\'' +
                ", isChoosed=" + isChoosed +
                ", dispatchMethodBean=" + dispatchMethodBean +
                '}';
    }
}
