package com.winguo.cart;


import com.winguo.cart.bean.CartDataBean;

/**
 * Created by Admin on 2017/1/7.
 */

public interface ICartChangedGoodsCountCallBack {
    void onAfterUpdateCountData(CartDataBean cartDataBean);
}
