package com.winguo.pay.controller.physicalstore;

import android.content.Context;

import com.winguo.pay.IPhysicalComfirmOrderCallBack;
import com.winguo.pay.modle.bean.PhysicalConfirmOderBean;
import com.winguo.pay.modle.bean.PhysicalSubmitOrderBean;
import com.winguo.pay.modle.physicalstore.RequestPhysicalConfirmOrderNet;
import com.winguo.pay.modle.physicalstore.RequestPhysicalSubmitOrderNet;
import com.winguo.pay.view.IPhysicalComfirmOrderView;

/**
 * Created by Admin on 2017/6/29.
 */

public class PhysicalPayController implements IPhysicalComfirmOrderCallBack {
    private IPhysicalComfirmOrderView iPhysicalComfirmOrderView;
    private final RequestPhysicalConfirmOrderNet net;
    private final RequestPhysicalSubmitOrderNet physicalSubmitOrderNet;

    public PhysicalPayController( IPhysicalComfirmOrderView iPhysicalComfirmOrderView) {

        this.iPhysicalComfirmOrderView = iPhysicalComfirmOrderView;
        net = new RequestPhysicalConfirmOrderNet();
        physicalSubmitOrderNet = new RequestPhysicalSubmitOrderNet();
    }

    public void getPhysicalConfirmOrder(Context context,String cart_ids){
        net.getOrderData(context,cart_ids,this);
    }

    public void getPhysicalSubmitOrder(Context context,String name,String phone,String cart_ids,String str){
        physicalSubmitOrderNet.getOrderData(context,name,phone,cart_ids,str,this);
    }

    @Override
    public void getPhysicalConfirmOrderCallBack(PhysicalConfirmOderBean physicalConfirmOderBean) {
        iPhysicalComfirmOrderView.getPhysicalConfirmOderData(physicalConfirmOderBean);
    }

    @Override
    public void getPhysicalSubmitOrderCallBack(PhysicalSubmitOrderBean physicalSubmitOrderBean) {
        iPhysicalComfirmOrderView.getPhysicalSubmitOderData(physicalSubmitOrderBean);
    }
}
