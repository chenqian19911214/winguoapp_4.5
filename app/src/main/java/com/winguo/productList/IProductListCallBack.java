package com.winguo.productList;


import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;

/**
 * Created by Administrator on 2016/12/10.
 */

public interface IProductListCallBack {
    //返回商品列表数据
    void onBackProductList(ProductListBean productListBeen);
    //返回商品列表更多数据
    void onBackMoreProductList(ProductListBean productListBeen);
    //返回省市数据
    void onBackProvinceCityData(ProvinceCityBean provinceCityBean);
    //返回分类名称的数据
    void onBackClassifyData(ClassifyName classifyName);
}
