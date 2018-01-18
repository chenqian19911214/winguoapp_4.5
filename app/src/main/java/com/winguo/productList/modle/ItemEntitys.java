package com.winguo.productList.modle;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/16.
 */

public class ItemEntitys implements Serializable{

    public IconEntity icon;
    public int shop_id;
    public int sale_qty;
    public PriceEntity price;
    public String cityname;
    public int entity_id;
    public String name;
    public double original_min_price;//使用原始最低价
    public double original_price;//原始价

    @Override
    public String toString() {
        return "ItemEntitys{" +
                "icon=" + icon +
                ", sale_qty=" + sale_qty +
                ", price=" + price +
                ", cityname='" + cityname + '\'' +
                ", entity_id=" + entity_id +
                ", name='" + name + '\'' +
                ", original_min_price='" + original_min_price + '\'' +
                ", original_price='" + original_price + '\'' +
                '}';
    }




}
