package com.winguo.pay.view;


import com.winguo.pay.modle.bean.AddressInfoBean;
import com.winguo.pay.modle.bean.ConfirmOrderBean;

/**
 * Created by Admin on 2017/1/9.
 */

public interface ISeekUserInfoView {
    //用户地址数据
    void userInfoData(AddressInfoBean addressInfoBean);
    //要结算的商品数据
    void confirmData(ConfirmOrderBean confirmOrderBean);

}
