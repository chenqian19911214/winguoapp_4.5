package com.winguo.mine.collect.goods.bean;

import com.winguo.mine.collect.shop.bean.Logo;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class GoodsCollectItem implements Serializable {
    public int gid;
    public String name;
    public int t_freight_temp_id;
    public double price;
    public Logo pic;
    public int m_item_counts;
    public String city_name;
    public int sale_qty;
    public int stock;

    @Override
    public String toString() {
        return "GoodsCollectItem{" +
                "gid=" + gid +
                ", name='" + name + '\'' +
                ", t_freight_temp_id=" + t_freight_temp_id +
                ", price=" + price +
                ", pic=" + pic +
                ", m_item_counts=" + m_item_counts +
                ", city_name='" + city_name + '\'' +
                ", sale_qty=" + sale_qty +
                ", stock=" + stock +
                '}';
    }
}
