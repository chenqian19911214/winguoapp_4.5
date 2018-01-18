package com.winguo.productList.view;


import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;

/**
 * Created by Administrator on 2016/12/10.
 */

public interface IProductListView {
    //获取数据
    void getProductList(ProductListBean productListBeens);
    //加载更多数据
    void getMoreProductList(ProductListBean productListBeens);
    //省市数据
    void getProvinceCity(ProvinceCityBean provinceCityBean);
    //分类名称数据
    void getClassifyName(ClassifyName classifyName);

}
