package com.winguo.cart.model.store;


import com.winguo.cart.bean.CartDataBean;

/**
 * Created by Admin on 2017/1/4.
 */

public interface ISeekCartCallBack {
    void onBackSeekCart(CartDataBean cartDataBean);
    //删除后的数据
    void onAfterDeleteCart(CartDataBean cartDataBean);
}
