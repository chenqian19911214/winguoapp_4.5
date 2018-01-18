package com.winguo.cart;

import com.winguo.cart.bean.PhysicalStoreBean;

/**
 * Created by Admin on 2017/6/26.
 */

public interface IPhysicalCartCallBack {
    //获取实体店购物车数据列表
    void getPhysicalCartDataCallBack(PhysicalStoreBean physicalStoreBean);
    //修改实体店购物车商品数量
    void upDatePhysicalCartDataCallBack(PhysicalStoreBean physicalStoreBean);
    //删除实体店购物车商品数量
    void deletePhysicalCartDataCallBack(PhysicalStoreBean physicalStoreBean);
}
