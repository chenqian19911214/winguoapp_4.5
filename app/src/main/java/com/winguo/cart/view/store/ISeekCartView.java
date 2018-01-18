package com.winguo.cart.view.store;


import com.winguo.cart.bean.CartDataBean;

/**
 * Created by Admin on 2017/1/4.
 */

public interface ISeekCartView {
    void seekCartData(CartDataBean cartDataBean);
    void deleteAfterCartData(CartDataBean cartDataBean);
    void updateCountData(CartDataBean cartDataBean);

}
