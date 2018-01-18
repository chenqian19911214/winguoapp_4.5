package com.winguo.mine.collect.shop.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class ShopCollectRootBean implements Serializable {
    public ShopCollectItemsBean items;
    public ShopCollectOtherInfoBean otherinfo;

    @Override
    public String toString() {
        return "ShopCollectRootBean{" +
                "items=" + items +
                ", otherinfo=" + otherinfo +
                '}';
    }
}
