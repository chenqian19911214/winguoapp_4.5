package com.winguo.pay.controller.store;


import android.content.Context;

import com.winguo.pay.ISeekUserInfoCallBack;
import com.winguo.pay.modle.bean.AddressInfoBean;
import com.winguo.pay.modle.store.SeekAddressRequestNet;
import com.winguo.pay.modle.bean.ConfirmOrderBean;
import com.winguo.pay.modle.confirmorder.ConfirmOrderRequestNet;
import com.winguo.pay.view.ISeekUserInfoView;

/**
 * Created by Admin on 2017/1/9.
 * 支付订单页面
 */

public class SeekUserInfoController implements ISeekUserInfoCallBack {
    private ISeekUserInfoView iSeekUserInfoView;
    private SeekAddressRequestNet net;
    private ConfirmOrderRequestNet confirmOrderRequestNet;

    public SeekUserInfoController(ISeekUserInfoView iSeekUserInfoView) {
        this.iSeekUserInfoView = iSeekUserInfoView;
        net = new SeekAddressRequestNet();
        confirmOrderRequestNet = new ConfirmOrderRequestNet();
    }

    /**
     * 返回地址
     * @param context
     */
    public void onBackUserInfoData(Context context){
        net.getAddressData(context,this);
    }

    /**
     * 返回 订单信息
     * @param context
     * @param sku_ids
     * @param is_prompt
     */
    public void onBackConfirmData(Context context,String sku_ids, int is_prompt){
        confirmOrderRequestNet.getOrderData(context,sku_ids,is_prompt,this);
    }

    @Override
    public void onUserInfoBackData(AddressInfoBean addressInfoBean) {
        iSeekUserInfoView.userInfoData(addressInfoBean);
    }

    @Override
    public void onConfirmBackData(ConfirmOrderBean confirmOrderBean) {
        iSeekUserInfoView.confirmData(confirmOrderBean);
    }

}
