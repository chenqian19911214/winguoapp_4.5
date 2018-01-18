package com.winguo.productList.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class ProductsEntity implements Serializable{

    public List<ItemEntitys> item;

    @Override
    public String toString() {
        return "ProductsEntity{" +
                "item=" + item +
                '}';
    }


}

