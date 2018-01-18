package com.winguo.mine.collect.shop.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 店铺收藏itemBean
 */
public class ShopCollectDataBean implements Serializable {
    public int id;
    public String name;
    public String mobile_url;
    public String adwords;
    public Logo logo;
    public int item_counts;
    public int shop_counts;

    @Override
    public String toString() {
        return "ShopCollectDataBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile_url='" + mobile_url + '\'' +
                ", adwords='" + adwords + '\'' +
                ", logo=" + logo +
                ", item_counts=" + item_counts +
                ", shop_counts=" + shop_counts +
                '}';
    }
}
