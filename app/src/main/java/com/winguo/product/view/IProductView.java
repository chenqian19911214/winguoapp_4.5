package com.winguo.product.view;


import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.bean.ProductEntity;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.product.modle.bean.CollectBean;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface IProductView {
    //获取商品信息
    @Deprecated
    void obtainProductData(ProductEntity productEntity);

    void obtainProductDetail(GoodDetail goodDetail);

    //获取店铺信息
    void obtainShopData(ShopSimpleBean shopSimpleBean);
    /**
     *收藏数据
     * @param collectBean
     */
    void getCollectData(CollectBean collectBean);
}
