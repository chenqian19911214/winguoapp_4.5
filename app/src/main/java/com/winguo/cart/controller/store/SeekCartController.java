package com.winguo.cart.controller.store;


import android.content.Context;

import com.winguo.cart.ICartChangedGoodsCountCallBack;
import com.winguo.cart.model.store.CartDeleteNet;
import com.winguo.cart.model.store.CartUpdateCountRequestNet;
import com.winguo.cart.model.store.ISeekCartCallBack;
import com.winguo.cart.bean.CartDataBean;
import com.winguo.cart.model.store.seekcart.SeekCartNet;
import com.winguo.cart.view.store.ISeekCartView;

/**
 * Created by Admin on 2017/1/4.
 */

public class SeekCartController implements ISeekCartCallBack,ICartChangedGoodsCountCallBack {
    private ISeekCartView iSeekCartView;
    private SeekCartNet seekCartNet;
    private final CartDeleteNet cartDeleteNet;
    private final CartUpdateCountRequestNet cartUpdateCountRequestNet;

    public SeekCartController(ISeekCartView iSeekCartView) {
        this.iSeekCartView = iSeekCartView;
        seekCartNet = new SeekCartNet();
        cartDeleteNet = new CartDeleteNet();
        cartUpdateCountRequestNet = new CartUpdateCountRequestNet();
    }
    //获取购物车的数据
    public void getData(Context context){
        seekCartNet.getData(context,this);
    }
    //删除后购物车数据
    public void getDeleteAfterData(Context context,String sku_ids){
        cartDeleteNet.deleteData(context,sku_ids,this);
    }
    //修改商品数量数据
    public void getUpdateCountData(Context context,String sku_id, String count, int is_prompt){
        cartUpdateCountRequestNet.getData(context,sku_id,count,is_prompt,this);
    }
    @Override
    public void onBackSeekCart(CartDataBean cartDataBean) {
        iSeekCartView.seekCartData(cartDataBean);
    }

    @Override
    public void onAfterDeleteCart(CartDataBean cartDataBean) {
        iSeekCartView.deleteAfterCartData(cartDataBean);
    }

    @Override
    public void onAfterUpdateCountData(CartDataBean cartDataBean) {
        iSeekCartView.updateCountData(cartDataBean);
    }
}
