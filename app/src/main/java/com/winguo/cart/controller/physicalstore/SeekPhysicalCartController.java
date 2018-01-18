package com.winguo.cart.controller.physicalstore;

import android.content.Context;

import com.winguo.cart.IPhysicalCartCallBack;
import com.winguo.cart.model.physicalstore.PhysicalCartDeleteNet;
import com.winguo.cart.model.physicalstore.PhysicalCartNet;
import com.winguo.cart.model.physicalstore.PhysicalCartUpdateNet;
import com.winguo.cart.bean.PhysicalStoreBean;
import com.winguo.cart.view.physicalstore.IPhysicalCartView;

/**
 * Created by Admin on 2017/6/26.
 */

public class SeekPhysicalCartController implements IPhysicalCartCallBack {

    private final PhysicalCartNet net;
    private IPhysicalCartView iPhysicalCartView;
    private final PhysicalCartUpdateNet physicalCartUpdateNet;
    private final PhysicalCartDeleteNet physicalCartDeleteNet;

    public SeekPhysicalCartController(IPhysicalCartView iPhysicalCartView) {
        this.iPhysicalCartView = iPhysicalCartView;
        net = new PhysicalCartNet();
        physicalCartUpdateNet = new PhysicalCartUpdateNet();
        physicalCartDeleteNet = new PhysicalCartDeleteNet();

    }

    /**
     * 获取实体店购物车数据列表
     *
     * @param context
     */
    public void getPhysicalCartDta(Context context) {
        net.getData(context, this);
    }

    /**
     * 修改实体店购物车数据数量
     *
     * @param context
     * @param cartId
     * @param num
     */
    public void upDatePhysicalCartData(Context context, String cartId, String num) {
        physicalCartUpdateNet.getData(context, cartId, num, this);
    }
    /**
     * 删除实体店购物车数据列表
     *
     * @param context
     */
    public void deletePhysicalCartDta(Context context,String cartId) {
       physicalCartDeleteNet.getdeletePhysicalCartData(context, cartId,this);
    }

    @Override
    public void getPhysicalCartDataCallBack(PhysicalStoreBean physicalStoreBean) {
        iPhysicalCartView.getPhysicalCartData(physicalStoreBean);
    }

    @Override
    public void upDatePhysicalCartDataCallBack(PhysicalStoreBean physicalStoreBean) {
        iPhysicalCartView.upDatePhysicalCartData(physicalStoreBean);
    }

    @Override
    public void deletePhysicalCartDataCallBack(PhysicalStoreBean physicalStoreBean) {
        iPhysicalCartView.deletePhysicalCartData(physicalStoreBean);
    }
}
