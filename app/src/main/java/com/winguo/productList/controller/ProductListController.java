package com.winguo.productList.controller;


import android.content.Context;

import com.winguo.productList.IProductListCallBack;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.modle.ProductListRequestNet;
import com.winguo.productList.modle.city.CityRequestNet;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.modle.classify.ClassifyRequestNet;
import com.winguo.productList.view.IProductListView;

/**
 * Created by Administrator on 2016/12/10.
 */

public class ProductListController implements IProductListCallBack {

    private IProductListView iProductView;
    private ProductListRequestNet productListRequestNet;
    private final CityRequestNet cityRequestNet;
    private final ClassifyRequestNet classifyRequestNet;

    public ProductListController(IProductListView iProductView) {
        this.iProductView = iProductView;
        productListRequestNet = new ProductListRequestNet();
        cityRequestNet = new CityRequestNet();
        classifyRequestNet = new ClassifyRequestNet();
    }

    /**
     * 获取网络数据
     *
     * @param text  要搜索的关键字
     * @param index 要搜索的页码数
     * @param sort
     */
    public void getData(Context context,String text, int index, String orders, String sort, int cateid, String mLowPrice, String mHightPrice,
                        int mProvinceCode, int mCityCode, String mClassifyName) {
        productListRequestNet.getData(context,text, index, this, orders, sort,cateid,mLowPrice,mHightPrice,mProvinceCode,mCityCode,mClassifyName);
    }

    /**
     * 获取更多网络数据
     *
     * @param text   要搜索的关键字
     * @param index  要搜索的页码数
     * @param orders
     * @param orders
     */
    public void getMoreData(Context context,String text, int index, String orders, String sort,String mLowPrice,String mHightPrice,
                            int mProvinceCode,int mCityCode,String mClassifyName,int mCategorieId) {
        productListRequestNet.getMoreData(context,text, index, this, orders, sort,mLowPrice,mHightPrice,mProvinceCode,mCityCode,mClassifyName,mCategorieId);
    }

    /**
     * 获取省市网络数据
     */
    public void getProvinceCityData(){
        cityRequestNet.getData(this);
    }

    /**
     * 获取分类名称数据
     * @param text
     * @param orders
     * @param sort
     * @param cateid
     */
    public void getClassifyNameData(String text, String orders, String sort,int cateid){
        classifyRequestNet.getData(text,orders,sort,cateid,this);
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
       iProductView.getProvinceCity(provinceCityBean);
    }

    @Override
    public void onBackClassifyData(ClassifyName classifyName) {
        iProductView.getClassifyName(classifyName);
    }


}
