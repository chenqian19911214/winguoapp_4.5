package com.winguo.productList.modle.classify;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class FiltersBean implements Serializable{
    public List<FiltersItemBean> item;


    @Override
    public String toString() {
        return "FiltersBean{" +
                "item=" + item +
                '}';
    }
}
