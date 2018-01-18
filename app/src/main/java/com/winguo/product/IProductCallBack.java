package com.winguo.product;


import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.bean.ProductEntity;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.product.modle.bean.CollectBean;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface IProductCallBack {
    @Deprecated
    void onBackProductData(ProductEntity productEntity);

    void onBackProductDetail(GoodDetail goodDetail);

    void onBackShopData(ShopSimpleBean shopSimpleBean);
    /**
     * 返回的商品是否收藏数据
     * @param collectBean
     */
    void onBackCollectData(CollectBean collectBean);
}
