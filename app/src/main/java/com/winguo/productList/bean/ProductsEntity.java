package com.winguo.productList.bean;

import com.winguo.productList.modle.ItemEntitys;

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

