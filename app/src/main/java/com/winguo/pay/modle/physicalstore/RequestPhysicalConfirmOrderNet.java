package com.winguo.pay.modle.physicalstore;

import android.content.Context;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.IPhysicalComfirmOrderCallBack;
import com.winguo.pay.modle.bean.PhysicalConfirmOderBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Admin on 2017/6/29.
 */

public class RequestPhysicalConfirmOrderNet {
    public void getOrderData(Context context, String cart_ids, IPhysicalComfirmOrderCallBack iPhysicalComfirmOrderCallBack) {
        //获取用户id
        String userId = GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.id;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
        String url = UrlConstant.BASE_URL + context.getResources().getString(R.string.physical_confirm_order)+"?cart_ids="+
                cart_ids+"&hash="+hashUserId;//UrlConstant.BASE_URL
//        HashMap<String, String> params = new HashMap<>();
//        params.put("cart_ids", cart_ids);
////        params.put("customer_id", userId);//用户id
//        params.put("hash",hashUserId);
//        CommonUtil.printI("hash",hashUserId);
        request(url, null, iPhysicalComfirmOrderCallBack);
    }

    private void request(final String url, final HashMap<String, String> params, final IPhysicalComfirmOrderCallBack iPhysicalComfirmOrderCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_SEEK_PHYSICAL_CART, params, new
                        IStringCallBack2() {
                            @Override
                            public void stringReturn(String result) {
                                CommonUtil.printI("确认订单页面数据",result);
                                try {
                                    final PhysicalConfirmOderBean physicalConfirmOderBean = GsonUtil.json2Obj(result, PhysicalConfirmOderBean.class);
                                    ThreadUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            iPhysicalComfirmOrderCallBack.getPhysicalConfirmOrderCallBack(physicalConfirmOderBean);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    iPhysicalComfirmOrderCallBack.getPhysicalConfirmOrderCallBack(null);
                                }

                            }


                            @Override
                            public void failReturn() {
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iPhysicalComfirmOrderCallBack.getPhysicalConfirmOrderCallBack(null);
                                    }
                                });
                            }
                        });
            }
        });


    }


}
