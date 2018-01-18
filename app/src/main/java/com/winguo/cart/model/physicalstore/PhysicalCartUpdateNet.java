package com.winguo.cart.model.physicalstore;

import android.content.Context;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountDataMgr;

import com.winguo.R;
import com.winguo.cart.IPhysicalCartCallBack;
import com.winguo.cart.bean.PhysicalStoreBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Admin on 2017/6/27.
 */

public class PhysicalCartUpdateNet {
    public void getData(Context context, String cartId,String num,IPhysicalCartCallBack iPhysicalCartCallBack) {
        //UrlConstant.BASE_URL
        String userId = GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.id;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
        String url = UrlConstant.BASE_URL + context.getResources().getString(R.string.physical_cart_search)+
                "?a=user_gw_num"+"&hash="+hashUserId+"&id="+cartId+"&state=3&num="+num;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("a", "user_gw_num");
//        map.put("uid", userId);
//        map.put("id", cartId);
//        map.put("state", "3");
//        map.put("num", num);
        request(url, null, iPhysicalCartCallBack);
    }

    private void request(final String url, final HashMap<String, String> map, final IPhysicalCartCallBack iPhysicalCartCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_UPDATE_PHYSICAL_CART, map, new

                        IStringCallBack2() {
                            @Override
                            public void stringReturn(String result) {
                                CommonUtil.printI("修改购物车数据",result);
                                try {
                                    final PhysicalStoreBean physicalStoreBean = GsonUtil.json2Obj(result, PhysicalStoreBean.class);
                                    ThreadUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            iPhysicalCartCallBack.upDatePhysicalCartDataCallBack(physicalStoreBean);
                                        }
                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ThreadUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            iPhysicalCartCallBack.getPhysicalCartDataCallBack(null);
                                        }
                                    });

                                }
                            }

                            @Override
                            public void failReturn() {
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iPhysicalCartCallBack.getPhysicalCartDataCallBack(null);
                                    }
                                });
                            }
                        });
            }
        });
    }

}
