package com.winguo.pay;


import com.winguo.pay.modle.bean.OrderResultBean;

/**
 * Created by Admin on 2017/1/14.
 */

public interface IConfirmOrderCallBack {
    //下单的数据
    void completeOrderCallBack(OrderResultBean orderResultBean);
}
