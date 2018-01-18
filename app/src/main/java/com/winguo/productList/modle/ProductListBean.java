package com.winguo.productList.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/12.
 */

public class ProductListBean implements Serializable{


    public SearchEntity search;

    @Override
    public String toString() {
        return "ProductListBean{" +
                "search=" + search +
                '}';
    }


}
