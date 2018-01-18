package com.winguo.product.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/2/14.
 */

public class PriceIntervalBean implements Serializable {
    public List<InnerDataBean> data;

    @Override
    public String toString() {
        return "PriceIntervalBean{" +
                "data=" + data +
                '}';
    }


}
