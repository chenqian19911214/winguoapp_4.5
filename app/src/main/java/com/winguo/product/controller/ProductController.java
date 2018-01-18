package com.winguo.product.controller;

import android.app.Activity;
import android.content.Context;

import com.winguo.product.IProductCallBack;
import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.bean.ProductEntity;
import com.winguo.product.modle.ProductRequestNet;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.product.modle.bean.CollectBean;
import com.winguo.product.modle.collect.CollectRequestNet;
import com.winguo.product.view.IProductView;


/**
 * Created by Administrator on 2016/12/22.
 */

public class ProductController implements IProductCallBack {
    private IProductView iProductView;
    private final ProductRequestNet productRequestNet;
    private final CollectRequestNet collectRequestNet;

    public ProductController(IProductView iProductView) {
        this.iProductView = iProductView;
        productRequestNet = new ProductRequestNet();
        collectRequestNet = new CollectRequestNet();
    }

    //调用请求网络的方法
    @Deprecated
    public void getGoodsData(Context context, int gid) {
        productRequestNet.getData(context,gid, this);
    }

    /**
     * 获取商品详情
     * @param context
     * @param gid
     */
    public void getGoodsDetail(Context context, int gid) {
        productRequestNet.getDataDetail(context,gid, this);
    }

    /**
     * 添加历史记录
     * @param activity
     * @param gid
     */
    public void addHistoryLog(Activity activity, int gid) {
        productRequestNet.requestAddHistoryLog(activity, gid);
    }

    public void getShopData(String mid) {
        productRequestNet.getShopData(mid, this);
    }

    public void getCollectBeanData(Context context,int gid) {
        collectRequestNet.getData(context,gid, this);
    }

    /**
     * 旧商品详情
     * @param productEntity
     */
    @Override
    public void onBackProductData(ProductEntity productEntity) {
        iProductView.obtainProductData(productEntity);

    }

    /**
     * 带现金券 新商品详情
     * @param goodDetail
     */
    @Override
    public void onBackProductDetail(GoodDetail goodDetail) {
        iProductView.obtainProductDetail(goodDetail);
    }

    @Override
    public void onBackShopData(ShopSimpleBean shopSimpleBean) {
        iProductView.obtainShopData(shopSimpleBean);

    }

    @Override
    public void onBackCollectData(CollectBean collectBean) {
        iProductView.getCollectData(collectBean);
    }
}
