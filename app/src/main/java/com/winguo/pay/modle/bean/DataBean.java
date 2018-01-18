package com.winguo.pay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/11.
 */

public  class DataBean implements Serializable {
    /**
     * content : http://g1.img.winguo.com/group1/M00/4E/79/wKgAi1hrOGWAGjiRAANPH9VxDQA31..jpg
     * modifyTime : 1483421797
     */

    public IconBean icon;
    public int largess_qty;
    public double cost_price;
    public String color_name;
    public int distributor_id;
    public String color_alias;
    public int skuid;
    public String discount;
    public String size;
    public int num;
    public int stock;
    public double price;
    public String color;
    public String discount_type;
    public double cash_coupon;
    public int item_id;
    public String name;
    public String size_alias;
    public String share_rate;


    @Override
    public String toString() {
        return "DataBean{" +
                "icon=" + icon +
                ", largess_qty=" + largess_qty +
                ", cost_price=" + cost_price +
                ", color_name='" + color_name + '\'' +
                ", distributor_id=" + distributor_id +
                ", color_alias='" + color_alias + '\'' +
                ", skuid=" + skuid +
                ", discount='" + discount + '\'' +
                ", size='" + size + '\'' +
                ", num=" + num +
                ", stock=" + stock +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", discount_type='" + discount_type + '\'' +
                ", cash_coupon='" + cash_coupon + '\'' +
                ", item_id=" + item_id +
                ", name='" + name + '\'' +
                ", size_alias='" + size_alias + '\'' +
                ", share_rate='" + share_rate + '\'' +
                '}';
    }


    public static class IconBean {
        public String content;
        public String modifyTime;

        @Override
        public String toString() {
            return "IconBean{" +
                    "content='" + content + '\'' +
                    ", modifyTime=" + modifyTime +
                    '}';
        }
    }
}
