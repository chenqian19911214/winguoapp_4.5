package com.winguo.cart.model.store.seekcart;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;

import com.winguo.R;
import com.winguo.cart.bean.CartDataBean;
import com.winguo.cart.bean.DataBean;
import com.winguo.cart.bean.DetailBean;
import com.winguo.cart.bean.GoodsItemsBean;
import com.winguo.cart.bean.OrderBean;
import com.winguo.cart.model.store.ISeekCartCallBack;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/3.
 */

public class SeekCartNet {

    private String url;
    public void getData(Context context,ISeekCartCallBack iSeekCartCallBack){
//        HashMap<String ,String> params=new HashMap<>();
//        params.put("a","seek");
        if (SPUtils.contains(context,"accountName")){
            try {
                String hashUserId= WinguoAccountDataMgr.getHashCommon(context);
//                params.put("hash",hashUserId);
                url = UrlConstant.BASE_URL +
                        CommonUtil.getAppContext().getResources().getString(R.string.data_cart)+
                        "?a=seek&hash="+hashUserId;
                CommonUtil.printI("购物车的数据列表",url);
                request( null,iSeekCartCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void request(final HashMap<String, String> params, final ISeekCartCallBack iSeekCartCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_SEEK_CART, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("购物车中 数据=====",result);
                        if (result!=null) {
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
                                    //返回数据
                                    iSeekCartCallBack.onBackSeekCart(cartDataBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iSeekCartCallBack.onBackSeekCart(null);
                    }
                });

            }
        });
    }
}
