package com.winguo.cart.model.physicalstore;

import android.content.Context;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.cart.IPhysicalCartCallBack;
import com.winguo.cart.bean.PhysicalStoreBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Admin on 2017/6/26.
 */

public class PhysicalCartNet {
    public void getData(Context context, IPhysicalCartCallBack iPhysicalCartCallBack) {
        //UrlConstant.BASE_URL
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
        String userId = GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.id;
        String url = UrlConstant.BASE_URL + context.getResources().getString(R.string.physical_cart_search)+
                "?a=user_gw_select"+"&hash="+hashUserId;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("a", "user_gw_select");
//        map.put("uid", userId);
        request(url, null, iPhysicalCartCallBack);
    }

    private void request(final String url, final HashMap<String, String> map, final IPhysicalCartCallBack iPhysicalCartCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_SEEK_PHYSICAL_CART, map, new

                        IStringCallBack2() {
                            @Override
                            public void stringReturn(String result) {
                                try {
                                    final PhysicalStoreBean physicalStoreBean = GsonUtil.json2Obj(result, PhysicalStoreBean.class);
                                    ThreadUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            iPhysicalCartCallBack.getPhysicalCartDataCallBack(physicalStoreBean);
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
