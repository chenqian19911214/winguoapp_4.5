package com.winguo.productList.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/1/18.
 */

public class SearchBean implements Serializable {
    public List<Integer> count;
    public List<Integer> has_more_items;
    public FiltersBean filters;

    @Override
    public String toString() {
        return "SearchBean{" +
                "count=" + count +
                ", has_more_items=" + has_more_items +
                ", filters=" + filters +
                '}';
    }
}
