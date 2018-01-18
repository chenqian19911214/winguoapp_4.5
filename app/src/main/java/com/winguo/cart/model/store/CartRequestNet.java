package com.winguo.cart.model.store;


import android.content.Context;

import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.cart.ICartCallBack;
import com.winguo.cart.bean.AddCartCodeBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/29.
 */

public class CartRequestNet {

//    String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart);
    String url;


    public void getData(Context context,String sku_id, String count, int is_prompt, ICartCallBack iCartCallBack) {
        if (SPUtils.contains(context,"accountName")){//没有登录时不能加入购物车
        //获取用户id,因为使用map集合出现问题,直接拼接URL使用
        try {
            String hashUserId =  WinguoAccountDataMgr.getHashCommon(context);
//            params.put("hash", hashUserId);
            url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart)+"?a=add&hash="+ hashUserId+"&sku_id="+sku_id+"&amount="+count+"&is_prompt="+is_prompt;
            CommonUtil.printI("加入购物车URL",url);
            request(null, iCartCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }


    /**
     * 数据的回调
     *
     * @param params
     * @param iCartCallBack
     */
    private void request(final HashMap<String, String> params, final ICartCallBack iCartCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.CART_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        if (result != null) {
                            //解析数据
                            final AddCartCodeBean addCartCodeBean = GsonUtil.json2Obj(result, AddCartCodeBean.class);

                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.printI("购物车网络数据:=", addCartCodeBean.toString());
                                    //返回数据通过回调函数
                                    iCartCallBack.onBackCode(addCartCodeBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iCartCallBack.onBackCode(null);
                    }

                });
            }
        });
    }
}
