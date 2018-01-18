package com.winguo.product.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class Item implements Serializable {
    /**
     * value : [89461,"","自定义属性1",150,0,1000,"",1000,""]
     * original_price : 150
     */

    public List<Data> data;

    @Override
    public String toString() {
        return "ItemBean{" +
                "data=" + data +
                '}';
    }


}
