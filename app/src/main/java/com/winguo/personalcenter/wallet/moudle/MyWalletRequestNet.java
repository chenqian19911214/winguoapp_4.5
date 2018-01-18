package com.winguo.personalcenter.wallet.moudle;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.guobi.account.GBAccountError;
import com.guobi.account.NameValuePair;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountBalance;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.bean.BalanceAndCash;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.personalcenter.wallet.bean.BalanceDetail;
import com.winguo.personalcenter.wallet.bean.CashCouponDetail;
import com.winguo.personalcenter.wallet.bean.PrestoreDetail;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/24.
 * 我的钱包网络请求
 */

public class MyWalletRequestNet {

    private static final String TAG = MyWalletRequestNet.class.getSimpleName();
    /**
     * 获取账户余额
     * @param context
     * @param requestBalanceCallback
     */
    public void requestBalance(Context context, final RequestBalanceCallback requestBalanceCallback){
        WinguoAcccountManagerUtils.takeUserBalnce(context, new WinguoAcccountManagerUtils.BalanceData() {
            @Override
            public void getBalance(WinguoAccountBalance winguoAccountBalance) {
                requestBalanceCallback.requestBalanceResult(winguoAccountBalance);
            }

            @Override
            public void balanceErrorMsg(int message) {
                requestBalanceCallback.requestBalanceErrorCode(message);
            }
        });
    }
    /**
     * 新获取账户余额 现金券
     * @param context
     * @param requestBalanceCallback
     */
    public void requestBalanceAndCash(Context context, final WinguoAcccountManagerUtils.IBalanceAndCashCallback requestBalanceCallback){
        WinguoAcccountManagerUtils.requestBalanceAndCash(context, new WinguoAcccountManagerUtils.IBalanceAndCashCallback() {
            @Override
            public void getBalanceAndCash(BalanceAndCash balanceAndCash) {
                requestBalanceCallback.getBalanceAndCash(balanceAndCash);
            }

            @Override
            public void balanceAndCashErrorMsg(int message) {
                requestBalanceCallback.balanceAndCashErrorMsg(message);
            }
        });
    }

    /**
     * 现金券明细
     * @param context
     * @param requestCashCouponDetailCallback
     */
    public void requestCashCouponDetail(Context context, final RequestCashCouponDetailCallback requestCashCouponDetailCallback){

        // 获取用户名
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        if (id == null) {
            requestCashCouponDetailCallback.requestCashCouponDetailErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 获取公钥
        if (!mKey.verify()) {
            requestCashCouponDetailCallback.requestCashCouponDetailErrorCode(GBAccountError.INVALID_KEY);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            requestCashCouponDetailCallback.requestCashCouponDetailErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.CASH_COUPON_DETIAL;
        final int dataSize = 3;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 加密的密码信息
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "cashcoupon REQ:" + finalUrl);
        //Log.e("明细：：",""+finalUrl);

        // 网络连接
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null) {
                    requestCashCouponDetailCallback.requestCashCouponDetailErrorCode(GBAccountError.INVALID_PEER_DATA);
                   return -1;
                }
                CashCouponDetail cashCouponDetail = GsonUtil.json2Obj(result, CashCouponDetail.class);
                requestCashCouponDetailCallback.requestCashCouponDetailResult(cashCouponDetail);

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestCashCouponDetailCallback.requestCashCouponDetailErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }
    /**
     * 预存交易明细
     * @param context
     * @param prestoreDetailCallback
     */
    public void requestPrestoreDetail(Context context, final RequestPrestoreDetailCallback prestoreDetailCallback){

        // 获取用户名
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        if (id == null) {
            prestoreDetailCallback.requestPrestoreDetailErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 获取公钥
        if (!mKey.verify()) {
            prestoreDetailCallback.requestPrestoreDetailErrorCode(GBAccountError.INVALID_KEY);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            prestoreDetailCallback.requestPrestoreDetailErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.PRESTORE_DETIAL;
        final int dataSize = 3;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 加密的密码信息
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "prestore REQ:" + finalUrl);
        //Log.e("明细：：",""+finalUrl);

        // 网络连接
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null) {
                    prestoreDetailCallback.requestPrestoreDetailErrorCode(GBAccountError.INVALID_PEER_DATA);
                   return -1;
                }
                Log.e("prestoreDetail:",""+result);

                PrestoreDetail prestoreDetail = GsonUtil.json2Obj(result, PrestoreDetail.class);
                prestoreDetailCallback.requestPrestoreDetailResult(prestoreDetail);

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                prestoreDetailCallback.requestPrestoreDetailErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

    /**
     * 可提现交易明细
     * @param context
     * @param page 显示页码
     * @param flag  提现或者分成标志：不传查全部，1.充值,2.网店主分成,3.扣除个人所得税,4.介绍网店主入驻分成,5.提现,6.介绍商家入驻分成,7.支付订单货款,8.介绍关键词分成,9.退款,11.取消,17.订单(使用果币支付).18.分享用户分成,19.渠道商分成，20.购买果币，21.手机充值，22.游戏卡充值
     * @param type  类型1.收入 2.支出  0 全部
     * @param limit  每页面显示行数
     * @param balanceDetailCallback
     */
    public void requestBalanceDetail(Context context,int page,int flag,int type,int limit, final RequestBalanceDetailCallback balanceDetailCallback){

        // 获取用户名
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        if (id == null) {
            balanceDetailCallback.requestBalanceDetailErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 获取公钥
        if (!mKey.verify()) {
            balanceDetailCallback.requestBalanceDetailErrorCode(GBAccountError.INVALID_KEY);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            balanceDetailCallback.requestBalanceDetailErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.BALANCE_DETIAL;
        final int dataSize = 3;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));//
        valueList.add(new NameValuePair("page", page+""));//
        valueList.add(new NameValuePair("type", type+""));//
        valueList.add(new NameValuePair("limit", limit+""));//
        valueList.add(new NameValuePair("flag", flag+""));//
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "balanceDetail REQ:" + finalUrl);
        //Log.e("明细：：",""+finalUrl);

        // 网络连接
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null) {
                    balanceDetailCallback.requestBalanceDetailErrorCode(GBAccountError.INVALID_PEER_DATA);
                   return -1;
                }
                Log.e("balanceDetail",""+result);
                //{"root":{"all_balance":0,"balance":0,"has_more_items":0,"count":0,"items":""}}
                try {
                    JSONObject res = new JSONObject(result);
                    JSONObject root = res.getJSONObject("root");
                    String items = root.getString("items");

                    JSONObject rootjons=  root.getJSONObject("items");
                    if (!TextUtils.isEmpty(items)) {
                        if (items.contains("[")){
                            BalanceDetail balanceDetail = GsonUtil.json2Obj(result, BalanceDetail.class);
                            balanceDetailCallback.requestBalanceDetailResult(balanceDetail);
                        }else {
                            String data = rootjons.getString("data");
                            BalanceDetail balanceDetail = new BalanceDetail();
                            BalanceDetail.RootBean rootBean = new BalanceDetail.RootBean();

                            BalanceDetail.RootBean.ItemsBean itemsBean = new BalanceDetail.RootBean.ItemsBean();

                            rootBean.setBalance(root.getDouble("balance"));
                            rootBean.setCount(root.getInt("count"));
                            rootBean.setAll_balance(root.getDouble("all_balance"));
                            rootBean.setHas_more_items(root.getInt("has_more_items"));

                            BalanceDetail.RootBean.ItemsBean.DataBean dataBean = GsonUtil.json2Obj(data, BalanceDetail.RootBean.ItemsBean.DataBean.class);
                            List<BalanceDetail.RootBean.ItemsBean.DataBean> dataBeans = new ArrayList<>();
                            dataBeans.add(dataBean);
                            itemsBean.setData(dataBeans);
                            rootBean.setItems(itemsBean);
                            balanceDetail.setRoot(rootBean);
                            balanceDetailCallback.requestBalanceDetailResult(balanceDetail);

                        }

                    } else {
                        balanceDetailCallback.requestBalanceDetailResult(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                balanceDetailCallback.requestBalanceDetailErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }


}
