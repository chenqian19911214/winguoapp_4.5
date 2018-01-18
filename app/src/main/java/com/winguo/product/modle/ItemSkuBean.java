package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class ItemSkuBean implements Serializable {
    /**
     * id : sku id
     * color_name : 颜色名称
     * name : ["颜色","尺码","单价","重量","数量","剩余数量","颜色图片","原价"]
     */

    public TitleBean title;
    /**
     * data : {"value":[89461,"","自定义属性1",150,0,1000,"",1000,""],"original_price":150}
     */

    public Item item;

    @Override
    public String toString() {
        return "ItemSkuBean{" +
                "title=" + title +
                ", item=" + item +
                '}';
    }




}

