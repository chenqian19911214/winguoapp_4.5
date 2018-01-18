package com.winguo.shop.controller;


import android.content.Context;

import com.winguo.productList.IProductListCallBack;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.view.IProductListView;
import com.winguo.shop.modle.ShopRequestNet;

/**
 * Created by Administrator on 2016/12/10.
 */

public class ShopController implements IProductListCallBack {

    private IProductListView iProductView;
    private ShopRequestNet shopRequestNet;
    public ShopController(IProductListView iProductView) {
        this.iProductView = iProductView;
        shopRequestNet = new ShopRequestNet();
    }

    /**
     * 获取网络数据
     * @param index 要搜索的页码数
     * @param sort
     */
    public void getData(Context context, int index, String orders, String sort, String mid) {
        shopRequestNet.getData(context, index, this, orders, sort,mid);
    }

    /**
     * 获取更多网络数据
     * @param index  要搜索的页码数
     * @param orders
     * @param orders
     */
    public void getMoreData(Context context,int index, String orders, String sort,String mid) {
        shopRequestNet.getMoreData(context, index, this, orders, sort,mid);
    }


    @Override
    public void onBackProductList(ProductListBean productListBeen) {
        iProductView.getProductList(productListBeen);
    }

    @Override
    public void onBackMoreProductList(ProductListBean productListBeen) {
        iProductView.getMoreProductList(productListBeen);
    }

    @Override
    public void onBackProvinceCityData(ProvinceCityBean provinceCityBean) {

    }

    @Override
    public void onBackClassifyData(ClassifyName classifyName) {

    }


}
