package com.winguo.pay.controller.store;


import android.content.Context;

import com.winguo.pay.IConfirmOrderCallBack;
import com.winguo.pay.modle.bean.OrderResultBean;
import com.winguo.pay.modle.confirmorder.RegistOrderRequestNet;
import com.winguo.pay.view.IConfirmOrderView;

/**
 * Created by Admin on 2017/1/14.
 */

public class ConfirmOrderController implements IConfirmOrderCallBack {

    private final RegistOrderRequestNet registOrderRequestNet;
    private IConfirmOrderView iConfirmOrderView;

    public ConfirmOrderController(IConfirmOrderView iConfirmOrderView) {
        this.iConfirmOrderView = iConfirmOrderView;
        registOrderRequestNet = new RegistOrderRequestNet();
    }
    /**
     * 下单时调用的方法
     */
    public void getRegistOrderData(Context context, String sku_ids, String type, String shop_ids, String ct, String temp_ids,
                                   String memo, String recid, int is_prompt,boolean is_use){
        registOrderRequestNet.getAddressData(context,sku_ids,type,shop_ids,ct,temp_ids,memo,recid,is_prompt,is_use,this);
    }
    @Override
    public void completeOrderCallBack(OrderResultBean orderResultBean) {
        iConfirmOrderView.registOrderData(orderResultBean);
    }
}
