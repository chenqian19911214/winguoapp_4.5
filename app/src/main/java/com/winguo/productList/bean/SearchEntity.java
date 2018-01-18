package com.winguo.productList.bean;

import com.winguo.productList.bean.ProductsEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class SearchEntity implements Serializable{

    public List<Integer> count;
    public List<Integer> has_more_items;
    public ProductsEntity products;

    @Override
    public String toString() {
        return "SearchEntity{" +
                "count=" + count +
                ", has_more_items=" + has_more_items +
                ", products=" + products +
                '}';
    }





}
