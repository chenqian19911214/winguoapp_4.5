package com.winguo.personalcenter.wallet.bankcard.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.NameValuePair;
import com.guobi.account.StatisticsAgentAdvanced;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountBalance;
import com.guobi.account.WinguoAccountBank;
import com.guobi.account.WinguoAccountBankCard;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.app.StartApp;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.personalcenter.wallet.moudle.MyWalletRequestNet;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceCallback;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceWithdrawCashCallback;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/30.
 * 网络请求
 */

public class MyBankCardRequestNet {

    private static final String TAG = MyBankCardRequestNet.class.getSimpleName();
    /**
     * 获取账户余额
     * @param context
     * @param requestBalanceCallback
     */
    public void requestBankCardList(final Context context, final RequestAccountBankCardListCallback requestBalanceCallback){
        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);;
        if (!mKey.verify()) {
            requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.INVALID_KEY);
            return;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANKCARDLIST;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        MyOkHttpUtils.post(finalUrl, 1, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null || result.length() == 0) {
                    requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.NETWORK_FAILED);
                    return GBAccountError.NETWORK_FAILED;
                }
                GBLogUtils.DEBUG_DISPLAY(TAG, "backcard RETURN:" + result);
                //{"root":{"item":[{"is_default":1,"bank_name":"民生银行","tail_number":5100,"p_bid":1036,"account_name":"陈伟涛"},{"is_default":0,"bank_name":"中国农业银行","tail_number":3132,"p_bid":1034,"account_name":"ll"}]}}

                JSONTokener tokener = new JSONTokener(result);
                if (tokener == null) {
                    requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.INVALID_INPUT);
                    return GBAccountError.INVALID_INPUT;
                }

                try {
                    JSONObject roots = new JSONObject(tokener);
                    String root1 = roots.getString("root");
                    List<WinguoAccountBankCard> cards = new ArrayList<WinguoAccountBankCard>();
                    //{"root":""}
                    if (root1.equals("")) {
                        requestBalanceCallback.requestAccountBankCardList(cards);
                        return 0;
                    }
                    JSONObject root = roots.getJSONObject("root");
                    if (!cards.isEmpty())
                        cards.clear();//如果不为空 清空

                    if (result.contains("[") || result.contains("]")) {

                        JSONArray cardList = root.getJSONArray("item");
                        for (int i = 0; i < cardList.length(); i++) {
                            JSONObject card = cardList.getJSONObject(i);
                            WinguoAccountBankCard item = new WinguoAccountBankCard();
                            item.accountName = card.getString("account_name");
                            item.p_bid = card.getString("p_bid");
                            item.bankName = card.getString("bank_name");
                            item.cardNumberTail = card.getString("tail_number");
                            String is_default = card.getString("is_default");
                            item.isDefault = Integer.valueOf(is_default) > 0 ? true : false;
                            cards.add(item);
                        }
                    } else {
                        JSONObject card = root.getJSONObject("item");
                        WinguoAccountBankCard item = new WinguoAccountBankCard();
                        item.accountName = card.getString("account_name");
                        item.p_bid = card.getString("p_bid");
                        item.bankName = card.getString("bank_name");
                        item.cardNumberTail = card.getString("tail_number");
                        String is_default = card.getString("is_default");
                        item.isDefault = Integer.valueOf(is_default) > 0 ? true : false;
                        cards.add(item);
                    }
                    requestBalanceCallback.requestAccountBankCardList(cards);
                    return 0;
                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.SESSION_TIMEOUT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestBalanceCallback.requestAccountBankCardListErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

    /**
     * 绑定银行卡详情
     * @param context
     * @param p_bid
     * @param detailCallback
     */
    public void requestBankCardDetail(final Context context,String p_bid, final RequestAccountBankCardDetailCallback detailCallback){
        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            detailCallback.requestAccountBankCardDetailErrorCode(GBAccountError.INVALID_KEY);
            return;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            detailCallback.requestAccountBankCardDetailErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return ;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            detailCallback.requestAccountBankCardDetailErrorCode( GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANKCARDDETAIL;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("p_bid", p_bid));
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "backcarddetail REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null || result.length() == 0) {
                    detailCallback.requestAccountBankCardDetailErrorCode( GBAccountError.NETWORK_FAILED);
                    return GBAccountError.NETWORK_FAILED;
                }

                GBLogUtils.DEBUG_DISPLAY(TAG, "backcarddetail RETURN:" + result);
                // {"root":{"branches_id":53400,"city_name":"广州市","bank_name":"民生银行","province_name":"广东","branches_name":"中国广州体育西支行","number":6216910304325100,"provinceid":19,"bid":10,"p_bid":1036,"account_name":"陈伟涛","cityid":1601}}
                try {

                    JSONObject roots = new JSONObject(result);
                    String jsonStr = roots.toString();
                    WinguoAccountBankCard info = new WinguoAccountBankCard();
                    if (jsonStr.contains("root")) {
                        JSONObject root = roots.getJSONObject("root");
                        String p_bid1 = root.getString("p_bid");
                        String bid = root.getString("bid");
                        String bank_name = root.getString("bank_name");
                        String branches_id = root.getString("branches_id");
                        String branches_name = root.getString("branches_name");
                        String provinceid = root.getString("provinceid");
                        String province_name = root.getString("province_name");
                        String cityid = root.getString("cityid");
                        String city_name = root.getString("city_name");
                        String number = root.getString("number");
                        String account_name = root.getString("account_name");
                        if (p_bid1 != null) {
                            info.p_bid = p_bid1;
                        }
                        if (bid != null) {
                            info.bid = bid;
                        }
                        if (bank_name != null) {
                            info.bankName = bank_name;
                        }
                        if (branches_id != null) {
                            info.bankBranchesID = branches_id;
                        }
                        if (branches_name != null) {
                            info.bankBranchesName = branches_name;
                        }
                        if (provinceid != null) {
                            info.provinceID = provinceid;
                        }
                        if (province_name != null) {
                            info.provinceName = province_name;
                        }
                        if (cityid != null) {
                            info.cityID = cityid;
                        }
                        if (city_name != null) {
                            info.cityName = city_name;
                        }
                        if (number != null) {
                            info.cardNumberFull = number;
                        }

                        if (account_name != null) {
                            info.accountName = account_name;
                        }
                        detailCallback.requestAccountBankCardDetail(info);

                    } else {
                        //登录过期或错误信息
                        JSONObject message = roots.getJSONObject("message");
                        if (message == null) {
                            detailCallback.requestAccountBankCardDetailErrorCode(GBAccountError.INVALID_PEER_DATA);
                            return GBAccountError.INVALID_PEER_DATA;
                        }
                        final int code = message.getInt("code");
                        detailCallback.requestAccountBankCardDetailErrorCode(code);
                    }

                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    detailCallback.requestAccountBankCardDetailErrorCode(GBAccountError.SESSION_TIMEOUT);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                detailCallback.requestAccountBankCardDetailErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

    /**
     * 删除银行卡
     * @param context
     * @param p_bid
     * @param deleteCallback
     * @return
     */
    public synchronized void deleteBankCard(final Context context, String p_bid ,final RequestAccountBankCardDeleteCallback deleteCallback) {

            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                deleteCallback.requestAccountBankCardDeleteErrorCode(GBAccountError.INVALID_KEY);
                return;
            }

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            if (id == null) {
                deleteCallback.requestAccountBankCardDeleteErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
                return ;
            }

            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            strBuf.append("&p_bid=").append(p_bid);
            String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hashCommon == null) {
                deleteCallback.requestAccountBankCardDeleteErrorCode(GBAccountError.HASH_FAILED);
                return ;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.DEL_BANKCARD;
            final int dataSize = 4;
            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
            valueList.add(new NameValuePair("hash", hashCommon));// HASH
            valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
            valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
            final String finalUrl = URLCreator.create(url, valueList);
            GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

            MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    try {
                        /*JSONObject root = new JSONObject(result);
                        JSONObject message = root.getJSONObject("message");
                        if (message == null) {
                            return GBAccountError.INVALID_PEER_DATA;
                        }
                        final int code = message.getInt("code");
                        if (code != 0) {
                           // setErrMsg(context, "delbankcard", code);

                        }*/
                        deleteCallback.requestAccountBankCardDelete(result);
                    }  catch (Exception e) {
                        deleteCallback.requestAccountBankCardDeleteErrorCode(GBAccountError.SESSION_TIMEOUT);
                        e.printStackTrace();
                    }

                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    deleteCallback.requestAccountBankCardDeleteErrorCode(GBAccountError.REQUST_TIMEOUT);
                }
            });

    }

    /**
     * 得到银行列表
     *
     * @param context
     * @param
     * @return
     */
    public  synchronized void getBankList(final Context context,final RequestBankListCallback bankListCallback) {
        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            bankListCallback.requestBankListErrorCode(GBAccountError.INVALID_KEY);
            return;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            bankListCallback.requestBankListErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            bankListCallback.requestBankListErrorCode(GBAccountError.HASH_FAILED);
            return ;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANKLIST;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        // valueList.add(new NameValuePair("pay_method", pay_method));//
        // valueList.add(new NameValuePair("payType", pay_type));//
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "banklist REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null || result.length() == 0) {
                    bankListCallback.requestBankListErrorCode(GBAccountError.NETWORK_FAILED);
                    return GBAccountError.NETWORK_FAILED;
                }

                GBLogUtils.DEBUG_DISPLAY(TAG, "banklist RETURN:" + result);
                try {
                    List<WinguoAccountBank> banks = new ArrayList<WinguoAccountBank>();
                    JSONObject roots = new JSONObject(result);
                    JSONObject message = roots.getJSONObject("message");
                    JSONObject items = message.getJSONObject("items");
                    JSONArray datas = items.getJSONArray("data");
                    //{"m_bank_id":1,"m_bank_ab":"icbc","m_bank_ico":"/group1/M00/04/1B/wKgAjFV_gnKAO5KUAAAM4KogQjg330.jpg","m_bank_code":1020000,"m_bank_name":"中国工商银行"}
                    for (int i = 0; i < datas.length(); i++) {

                        WinguoAccountBank bank = new WinguoAccountBank();
                        JSONObject item = (JSONObject) datas.get(i);
                        String m_bank_id = item.getString("m_bank_id");
                        String m_bank_name = item.getString("m_bank_name");
                        String m_bank_ico = item.getString("m_bank_ico");
                        String m_bank_ab = item.getString("m_bank_ab");
                        String m_bank_code = item.getString("m_bank_code");
                        bank.id = m_bank_id;
                        bank.name = m_bank_name;
                        if (!m_bank_ab.equals("")) {
                            bank.nameAB = m_bank_ab;
                        }
                        bank.code = m_bank_code;
                        banks.add(bank);
                    }
                    bankListCallback.requestBankList(banks);
                    return 0;
                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    e.printStackTrace();
                    bankListCallback.requestBankListErrorCode(GBAccountError.SESSION_TIMEOUT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                bankListCallback.requestBankListErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

    /**
     * 获取账户余额提现
     * @param context
     * @param withdrawCashCallback
     */
    public void requestBalanceWithdraw(Context context,String amount, String p_bid, String paypd, final RequestBalanceWithdrawCashCallback withdrawCashCallback){
        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            withdrawCashCallback.requestBalanceWithdrawCashErrorCode(GBAccountError.INVALID_KEY);
            return;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            withdrawCashCallback.requestBalanceWithdrawCashErrorCode(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        strBuf.append("&password=").append(paypd);
        strBuf.append("&p_bid=").append(p_bid);
        strBuf.append("&amount=").append(amount);
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            withdrawCashCallback.requestBalanceWithdrawCashErrorCode(GBAccountError.HASH_FAILED);
            return ;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.WITHDRAW_CASH;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "requestBalanceWithdraw REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null || result.length() == 0) {
                    withdrawCashCallback.requestBalanceWithdrawCashErrorCode(GBAccountError.NETWORK_FAILED);
                    return GBAccountError.NETWORK_FAILED;
                }
                GBLogUtils.DEBUG_DISPLAY(TAG, "requestBalanceWithdraw RETURN:" + result);
                withdrawCashCallback.requestBalanceWithdrawCashResult(result);
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                withdrawCashCallback.requestBalanceWithdrawCashErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });
    }


}
