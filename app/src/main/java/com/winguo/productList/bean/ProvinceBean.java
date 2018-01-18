package com.winguo.productList.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/1/16.
 */

public  class ProvinceBean implements Serializable {
    /**
     * code : 1
     * name : 北京
     * cities : {"item":[{"code":72,"name":"朝阳区"},{"code":2800,"name":"海淀区"},{"code":2801,"name":"西城区"},{"code":2802,"name":"东城区"},{"code":2803,"name":"崇文区"},{"code":2804,"name":"宣武区"},{"code":2805,"name":"丰台区"},{"code":2806,"name":"石景山区"},{"code":2807,"name":"门头沟"},{"code":2808,"name":"房山区"},{"code":2809,"name":"通州区"},{"code":2810,"name":"大兴区"},{"code":2812,"name":"顺义区"},{"code":2814,"name":"怀柔区"},{"code":2816,"name":"密云区"},{"code":2901,"name":"昌平区"},{"code":2953,"name":"平谷区"},{"code":3065,"name":"延庆县"}]}
     */

    public List<ProvinceItemBean> item;


    @Override
    public String toString() {
        return "ProvinceBean{" +
                "item=" + item +
                '}';
    }
}