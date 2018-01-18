package com.winguo.product.modle.bean;

import com.winguo.product.modle.ItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class AdditionalAttributesBean implements Serializable{
    /**
     * value : 智高（ZHIGAO）
     * label : 品牌
     */

    public List<ItemBean> item;

    @Override
    public String toString() {
        return "AdditionalAttributesBean{" +
                "item=" + item +
                '}';
    }


}
