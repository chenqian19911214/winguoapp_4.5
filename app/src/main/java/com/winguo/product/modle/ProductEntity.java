package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ProductEntity implements Serializable {
    public ProductBean product;

    @Override
    public String toString() {
        return "ProductEntity{" +
                "product=" + product +
                '}';
    }




}
