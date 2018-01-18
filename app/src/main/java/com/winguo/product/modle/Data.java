package com.winguo.product.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class Data implements Serializable {
    public double original_price;
    public List<String> value;

    @Override
    public String toString() {
        return "DataBean{" +
                "original_price=" + original_price +
                ", value=" + value +
                '}';
    }
}
