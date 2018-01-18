package com.winguo.mine.collect.shop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class ShopCollectItemsBean implements Serializable {
    public List<ShopCollectDataBean> data;

    @Override
    public String toString() {
        return "ShopCollectItemsBean{" +
                "data=" + data +
                '}';
    }
}
