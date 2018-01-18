package com.winguo.mine.collect.goods.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class GoodsCollectRootBean implements Serializable {
    public int count;
    public int has_more;
    public GoodsCollectItems items;

    @Override
    public String toString() {
        return "GoodsCollectRootBean{" +
                "count=" + count +
                ", has_more=" + has_more +
                ", items=" + items +
                '}';
    }
}
