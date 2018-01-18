package com.winguo.cart.controller.store;

import android.content.Context;

import com.winguo.cart.ICartCallBack;
import com.winguo.cart.bean.AddCartCodeBean;
import com.winguo.cart.model.store.CartRequestNet;
import com.winguo.cart.view.store.IAddCartView;

/**
 * Created by Administrator on 2016/12/30.
 */

public class CartController implements ICartCallBack {
    private IAddCartView iAddCartView;
    private CartRequestNet cartRequestNet;

    public CartController(IAddCartView iAddCartView) {
        this.iAddCartView = iAddCartView;
        cartRequestNet = new CartRequestNet();
    }
    public void getCartCodeData(Context context,String sku_id, String count, int is_prompt){
        cartRequestNet.getData(context,sku_id,count, is_prompt,this);
    }

    @Override
    public void onBackCode(AddCartCodeBean addCartCodeBean) {
        iAddCartView.showAddCartCode(addCartCodeBean);
    }
}
