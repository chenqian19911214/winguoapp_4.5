package com.winguo.pay.modle.physicalstore;

import android.content.Context;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.IPhysicalComfirmOrderCallBack;
import com.winguo.pay.modle.bean.PhysicalSubmitOrderBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Admin on 2017/6/29.
 */

public class RequestPhysicalSubmitOrderNet {
    public void getOrderData(Context context, String name,String phone,String cart_ids,
                             String str,IPhysicalComfirmOrderCallBack iPhysicalComfirmOrderCallBack) {
        //获取用户id
        String userId = GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.id;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
        String url = UrlConstant.BASE_URL + context.getResources().getString(R.string.physical_submit_order)+
                "?hash="+hashUserId+"&cart_ids="+cart_ids+"&name="+name+"&phone="+phone+"&str="+str;
//        HashMap<String, String> params = new HashMap<>();
//        params.put("cart_ids", cart_ids);
//        params.put("hash", hashUserId);//用户id
//        params.put("name", name);
//        params.put("phone", phone);
//        params.put("str", str);
        request(url, null, iPhysicalComfirmOrderCallBack);
    }

    private void request(final String url, final HashMap<String, String> params, final IPhysicalComfirmOrderCallBack iPhysicalComfirmOrderCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_SUBMIT_PHYSICAL_ORDER, params, new
                        IStringCallBack2() {
                            @Override
                            public void stringReturn(String result) {
                                CommonUtil.printI("实体店下单返回数据",result);
                                try {
                                    final PhysicalSubmitOrderBean physicalSubmitOrderBean = GsonUtil.json2Obj(result, PhysicalSubmitOrderBean.class);
                                    ThreadUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            iPhysicalComfirmOrderCallBack.getPhysicalSubmitOrderCallBack(physicalSubmitOrderBean);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    iPhysicalComfirmOrderCallBack.getPhysicalSubmitOrderCallBack(null);
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
