package com.winguo.cart;


import com.winguo.cart.bean.AddCartCodeBean;

/**
 * Created by Administrator on 2016/12/30.
 */

public interface ICartCallBack {
    //返回添加购物车的返回码
    void onBackCode(AddCartCodeBean addCartCodeBean);
}
