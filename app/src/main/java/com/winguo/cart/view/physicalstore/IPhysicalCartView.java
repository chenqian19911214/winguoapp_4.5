package com.winguo.cart.view.physicalstore;

import com.winguo.cart.bean.PhysicalStoreBean;

/**
 * Created by Admin on 2017/6/26.
 */

public interface IPhysicalCartView {
    //获取购物车数据列表
    void getPhysicalCartData(PhysicalStoreBean physicalStoreBean);
    //获取修改实体店购物车商品数量数据
    void upDatePhysicalCartData(PhysicalStoreBean physicalStoreBean);
    //获取删除实体店购物车商品数量数据
    void deletePhysicalCartData(PhysicalStoreBean physicalStoreBean);
}
