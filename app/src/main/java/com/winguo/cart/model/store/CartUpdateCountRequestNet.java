package com.winguo.cart.model.store;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.cart.ICartChangedGoodsCountCallBack;
import com.winguo.cart.bean.CartDataBean;
import com.winguo.cart.bean.DataBean;
import com.winguo.cart.model.store.seekcart.DataBeanDeserializer;
import com.winguo.cart.bean.DetailBean;
import com.winguo.cart.model.store.seekcart.DetailBeanDeserializer;
import com.winguo.cart.bean.GoodsItemsBean;
import com.winguo.cart.model.store.seekcart.GoodsItemsBeanDeserializer;
import com.winguo.cart.bean.OrderBean;
import com.winguo.cart.model.store.seekcart.OrderBeanDeserializer;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/7.
 */

public class CartUpdateCountRequestNet {
    private String url;

    public void getData(Context context,String sku_id, String count, int is_prompt, ICartChangedGoodsCountCallBack iCartChangedGoodsCountCallBack) {
        if (SPUtils.contains(context,"accountName")){//没有登录时不能加入购物车

            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId =  WinguoAccountDataMgr.getHashCommon(context);
//            params.put("hash", hashUserId);
                url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart)+"?a=updateqty&hash="+ hashUserId+"&sku_id="+sku_id+"&qty="+count+"&is_prompt="+is_prompt;
                request(iCartChangedGoodsCountCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}

    private void request(final ICartChangedGoodsCountCallBack iCartChangedGoodsCountCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_UPDATE_CART, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        if (result != null) {
                            CommonUtil.printI("修改购物车数量后返回:=", result);
                            //解析json
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(OrderBean.class, new OrderBeanDeserializer());
                            builder.registerTypeAdapter(DetailBean.class, new DetailBeanDeserializer());
                            builder.registerTypeAdapter(GoodsItemsBean.class, new GoodsItemsBeanDeserializer());
                            builder.registerTypeAdapter(DataBean.class, new DataBeanDeserializer());
                            Gson gson = builder.create();
                            final CartDataBean cartDataBean = gson.fromJson(result, CartDataBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iCartChangedGoodsCountCallBack.onAfterUpdateCountData(cartDataBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iCartChangedGoodsCountCallBack.onAfterUpdateCountData(null);
                    }

                });
            }
        });
    }
}