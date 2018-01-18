package com.winguo.mine.collect.goods.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class GoodsCollectItems implements Serializable {
    public List<GoodsCollectItem> item;

    @Override
    public String toString() {
        return "GoodsCollectItems{" +
                "item=" + item +
                '}';
    }
}
