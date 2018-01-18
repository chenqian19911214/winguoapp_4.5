package com.winguo.mine.collect.shop;

import com.winguo.mine.collect.shop.bean.Logo;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class ShopCollectItemBean {
    @Override
    public String toString() {
        return "ShopCollectItemBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile_url='" + mobile_url + '\'' +
                ", adwords='" + adwords + '\'' +
                ", logo='" + logo + '\'' +
                ", item_counts=" + item_counts +
                ", shop_counts=" + shop_counts +
                '}';
    }

    private int id;
    private String name;
    private String mobile_url;
    private String adwords;
    private Logo logo;
    private int item_counts;
    private int shop_counts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getAdwords() {
        return adwords;
    }

    public void setAdwords(String adwords) {
        this.adwords = adwords;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public int getItem_counts() {
        return item_counts;
    }

    public void setItem_counts(int item_counts) {
        this.item_counts = item_counts;
    }

    public int getShop_counts() {
        return shop_counts;
    }

    public void setShop_counts(int shop_counts) {
        this.shop_counts = shop_counts;
    }
}
