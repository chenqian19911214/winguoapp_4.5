package com.winguo.pay;


import com.winguo.pay.modle.bean.AddressInfoBean;
import com.winguo.pay.modle.bean.ConfirmOrderBean;

/**
 * Created by Admin on 2017/1/9.
 */

public interface ISeekUserInfoCallBack {
        //用户地址数据
        void onUserInfoBackData(AddressInfoBean addressInfoBean);
        //结算商品数据
        void onConfirmBackData(ConfirmOrderBean confirmOrderBean);
}
