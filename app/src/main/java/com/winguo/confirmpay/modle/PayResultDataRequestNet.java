package com.winguo.confirmpay.modle;


import android.content.Context;
import android.util.Log;

import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.confirmpay.IConfirmPayCallBack;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/23.
 */

public class PayResultDataRequestNet {
    public void getPayOrderData(Context context,String orderNumber, IConfirmPayCallBack iConfirmPayCallBack){
        if (SPUtils.contains(context,"accountName")){//没有登录时不能加入购物车
            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
                String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_order)+
                        "?a=pay_result&hash="+ hashUserId+"&orderNumber="+orderNumber;
                request(url,iConfirmPayCallBack);
                Log.e("pay result",""+url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void request(final  String url, final IConfirmPayCallBack iConfirmPayCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PAY_RESULT_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("支付结果的网络数据:=", result);
                        final PayResultBean payResultBean = GsonUtil.json2Obj(result, PayResultBean.class);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iConfirmPayCallBack.onBackPayResultData(payResultBean);
                            }
                        });
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackPayResultData(null);
                    }

                });
            }
        });
    }
}
