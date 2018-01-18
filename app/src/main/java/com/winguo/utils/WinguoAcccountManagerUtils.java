package com.winguo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.GBAccountStatus;
import com.guobi.account.HttpKey;
import com.guobi.account.MD5;
import com.guobi.account.NameValuePair;
import com.guobi.account.StatisticsAgentAdvanced;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountBalance;
import com.guobi.account.WinguoAccountBankCard;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountCurrency;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.account.WinguoAccountImpl;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseRequestCallBack;
import com.winguo.bean.BalanceAndCash;
import com.winguo.bean.PublicKeyJson;
import com.winguo.bean.UserTodayShop;
import com.winguo.bean.WeixinLoginBean;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.personalcenter.OpenSpaceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/17.
 */

public class WinguoAcccountManagerUtils {

    public final static String TAG = WinguoAcccountManagerUtils.class.getSimpleName();


    /**
     * 获取公钥
     *
     * @param context
     * @param
     * @return
     */
    public static int getKey(Context context, final IPublicKey publicKey) {

        if (!NetWorkUtil.isNetworkAvailable(context)) {
            return GBAccountError.NETWORK_CONN_FAILED;
        }
        // 生成URL
        final String finalUrl = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETKEY;

        Log.i("chenqianokhtp","公钥地址:"+finalUrl);

        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Gson datagson = new Gson();
                PublicKeyJson keydatajson = datagson.fromJson(result, PublicKeyJson.class);
                String hast = keydatajson.getMessage().getHash();
                int cole = keydatajson.getMessage().getCode();
                String text = keydatajson.getMessage().getText();
                WinguoEncryption.analyzerHashContent1(hast, publicKey);
                Log.i("chenqianokhtp","公钥json:"+hast +"\n"+text);
                Log.i("chenqianokhtp","公钥json hou:"+result);

                return cole;
            }

            @Override
            public void exceptionMessage(String message) {
                if (message != null) {
                    publicKey.publicKeyErrorMsg(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });
        return 0;
    }

    /**
     * 登陆，保存账户信息
     *
     * @param id       帐户名
     * @param password 密码
     * @return 返回状态码
     */
    public static void login(final Context context, final String id, final String password, final WinguoAccountKey mKey, final boolean save, final ITakeWinguoGeneralResult requesLogin) {
        // 创建Hash字符串
        final String hash;

        if ((id == null) || (id.length() <= 0) || (password == null) || (password.length() <= 0)) {
            requesLogin.takeWinguoGeneralResult(GBAccountError.UNKNOWN, false);
        }

        if (mKey == null) {
            requesLogin.takeWinguoGeneralResult(GBAccountError.WINGUOACCOUNTKEY_NULL, false);
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&pa=").append(password);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());

        hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            requesLogin.takeWinguoGeneralResult(GBAccountError.HASH_FAILED, false);
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.LOGIN;
        final int dataSize = 6;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 账号密码加密
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("source_type", "1"));// 客户端类型：桌面
        String finalUrl = URLCreator.createNoAnd(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ  :" + finalUrl);


        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                // result = {"message":{"not_verified":0,"text":"登录成功","code":0,"status":"success","name":18300602618}}
                //<message><status>error</status><text>登录已过期，请重新登录。</text><code>-101</code></message>
                JSONTokener jsont = new JSONTokener(result);
                if (jsont == null || result == null) {
                    requesLogin.takeWinguoGeneralResult(GBAccountError.INVALID_PEER_DATA, false);
                } else try {

                    JSONObject object = new JSONObject(jsont);
                    JSONObject message = object.getJSONObject("message");
                    String text = message.getString("text");
                    String status = message.getString("status");
                    int code = message.getInt("code");
                    if (status.equals("error")) {
                        requesLogin.takeWinguoGeneralResult(code, false);
                    } else {
                        if (save) {
                            String hashCommon = "id=" + id + "&token=" + mKey.getToken() + "&uuid=" + mKey.getUUID();
                            hashCommon = WinguoEncryption.commonEncryption(hashCommon, mKey);

                            WinguoAccountDataMgr.setUserName(context, id);
                            WinguoAccountDataMgr.setHashLogin(context, hash);
                            WinguoAccountDataMgr.setMD5(context, MD5.md5Appkey(password));
                            WinguoAccountDataMgr.setKEY(context, mKey.getKey());
                            WinguoAccountDataMgr.setTOKEN(context, mKey.getToken());
                            WinguoAccountDataMgr.setUUID(context, mKey.getUUID());
                            WinguoAccountDataMgr.setHashCommon(context, hashCommon);
                            WinguoAccountDataMgr.setPizza(context, password);
                        }
                        loginSuccess(context, requesLogin);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    //网络超时 登录失败
                    requesLogin.takeWinguoGeneralResult(GBAccountError.REQUST_TIMEOUT, false);
                }
            }
        });

    }

    /**
     * 自动登录
     *
     * @param context
     * @param requestAutoLogin
     */
    public static void autoLogin(final Context context, final IRequestAutoLogin requestAutoLogin) {
        autoLogin(context, WinguoAccountDataMgr.getUserName(context), WinguoAccountDataMgr.getPizza(context), WinguoAccountDataMgr.getTOKEN(context), WinguoAccountDataMgr.getUUID(context), WinguoAccountDataMgr.getWinguoAccountKey(context), requestAutoLogin);
    }


    public static void autoLogin(final Context context, String id, String password, String token, String uuid, WinguoAccountKey mKey, final IRequestAutoLogin requestAutoLogin) {
        // 创建Hash字符串
        final String hash;
        GBLogUtils.DEBUG_DISPLAY("autologin:","pwd:"+password);
        if (!mKey.verify()) {
            requestAutoLogin.requestAutoLoginErrorMsg(GBAccountError.INVALID_KEY);
            return;
        }
        final int resultCode[] = {0};
        if ((id == null) || (id.length() <= 0) || (password == null) || (password.length() <= 0)) {
            requestAutoLogin.requestAutoLoginErrorMsg(GBAccountError.INVALID_INPUT);
            return;
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&pa=").append(password);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);

        if (hash == null) {
            requestAutoLogin.requestAutoLoginErrorMsg(GBAccountError.HASH_FAILED);
            return;
        }

        // 创建Hash2字符串
        String hash2 = "token=" + token + "&uuid=" + uuid;
        hash2 = WinguoEncryption.commonEncryption(hash2, mKey);
        if (hash2 == null) {
            requestAutoLogin.requestAutoLoginErrorMsg(GBAccountError.HASH_FAILED);
            return;
        }
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.LOGIN;
        final int dataSize = 6;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 账号密码加密
        valueList.add(new NameValuePair("hash2", hash2));// 自动登陆的附加信息
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("source_type", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("memorize", "1"));// 手工登录
        String finalUrl = URLCreator.createNoAnd(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:  " + finalUrl);
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                try {

                    JSONObject object = new JSONObject(result);
                    JSONObject message = object.getJSONObject("message");
                    String text = message.getString("text");
                    String status = message.getString("status");
                    requestAutoLogin.requestAutoLogin(status);

                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    requestAutoLogin.requestAutoLoginErrorMsg(GBAccountError.SESSION_TIMEOUT);

                } catch (Exception e) {
                    e.printStackTrace();

                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                //默认请求超时
                requestAutoLogin.requestAutoLoginErrorMsg(GBAccountError.REQUST_TIMEOUT);
            }
        });


    }

    public interface IRequestAutoLogin {
        void requestAutoLogin(String statu);

        void requestAutoLoginErrorMsg(int message);
    }

    /**
     * 获取用余额
     *
     * @param context
     * @param balance
     */
    public static void takeUserBalnce(final Context context, final BalanceData balance) {

        // 获取用户名
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        if (id == null) {
            balance.balanceErrorMsg(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 获取公钥
        if (!mKey.verify()) {
            balance.balanceErrorMsg(GBAccountError.INVALID_KEY);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            balance.balanceErrorMsg(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BALANCE;
        final int dataSize = 3;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 加密的密码信息
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        //valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "balance REQ:" + finalUrl);

        // 网络连接
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                //登陆过期
                //<root><latest_balance>0</latest_balance><unable_balance>0</unable_balance><income_time>2017-01-19 10:09</income_time><income_str>2个工作日到账</income_str><balance_out>1</balance_out></root>
                JSONTokener jsont = new JSONTokener(result);
                if (jsont == null) {
                    balance.balanceErrorMsg(GBAccountError.INVALID_PEER_DATA);
                }

                try {

                    JSONObject roots = new JSONObject(jsont);
                    JSONObject root = roots.getJSONObject("root");
                    String latest_balance = root.getString("latest_balance");
                    String unable_balance = root.getString("unable_balance");
                    String income_time = root.getString("income_time");
                    String income_str = root.getString("income_str");
                    String balance_out = root.getString("balance_out");

                    WinguoAccountBalance info = new WinguoAccountBalance();
                    info.balance = Double.valueOf(latest_balance);
                    info.incomeTime = income_time;
                    info.incomeDescription = income_str;
                    info.withdrawalTimes = Integer.valueOf(balance_out);
                    info.unable_balance = Float.valueOf(unable_balance);
                    balance.getBalance(info);


                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    Intent filter = new Intent();
                    filter.setAction("quitLogin");
                    context.sendBroadcast(filter);
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                balance.balanceErrorMsg(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

    /**
     * 获取果币信息
     *
     * @param context
     * @param currencyData
     */
    public static void takeUserCurrency(final Context context, final UserCurrencyData currencyData) {

        // 获取KEY
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        if (!mKey.verify()) {
            currencyData.currencyErrorMsg(GBAccountError.INVALID_KEY);
            return;
        }
        // mKey.dump();
        // 获取用户名

        if (id == null) {
            currencyData.currencyErrorMsg(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            currencyData.currencyErrorMsg(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_CURRENCY;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        //  valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "currency REQ:" + finalUrl);
        //http://api.winguo.com/data/account?a=get_winguo&hash=eDbC4CTGg%2FmGiTtS1B9kdYqu58zvaNcf%2BltCl2gykRAt1CGIwOYcS7zJSoz1JtvluYzEfDLC7Q%2FiZWGsavBgruCgTWWtsKE7pzOk%2BsMSD6AYNcwk%2B%2FZmlsb1snJaCF1dNk%2BVmT3B24zkENuss8vna0FE44uivMCZTRsbbf372HthFXouncfW9tcGO0cMjG9%2FvnKNufiGKR9sWhta1k2UNIdX4ok0J%2BG7DJ9gtSwlO%2FmffBRBZu6lOe9Xrbudrov7fXrxCScnbkZgNrFZtF6RTtGIBeKe002L8USjsCoS4HSopohNSfd7mS2c7IcWcLwoZOFKtkKQZkjQz%2B%2BHynlAIA%3D%3D&clienttype=1&cudid=4909733da628e62844fd2fd58b48acc7

        // 网络连接
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                // 分析返回内容{"root":{"frozen":0,"allwinguo":0,"winguo":0}}
                JSONTokener jsont = new JSONTokener(result);
                if (jsont == null) {
                    currencyData.currencyErrorMsg(GBAccountError.INVALID_PEER_DATA);
                }

                try {

                    JSONObject roots = new JSONObject(jsont);
                    JSONObject root = roots.getJSONObject("root");
                    String frozen = root.getString("frozen");
                    String allwinguo = root.getString("allwinguo");
                    String winguo = root.getString("winguo");

                    WinguoAccountCurrency info = new WinguoAccountCurrency();
                    info.all = Float.valueOf(allwinguo);
                    info.frozen = Float.valueOf(frozen);
                    info.avaliable = Float.valueOf(winguo);
                    currencyData.getCurrency(info);


                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    Intent filter = new Intent();
                    filter.setAction("quitLogin");
                    context.sendBroadcast(filter);
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "获取果币信息失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });

    }

    /**
     * 获取用户银行卡列表
     *
     * @param context
     * @param userBankCard
     */
    public static final synchronized void takeBankCardList(final Context context, final IUserBankCard userBankCard) {
        final List<WinguoAccountBankCard> cards = new ArrayList<>();
        // 获取KEY
        WinguoAccountKey mkey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mkey.getToken();
        final String uuid = mkey.getUUID();
        if (!mkey.verify()) {
            userBankCard.bankCardErrorMsg(GBAccountError.INVALID_KEY);
            return;
        }
        // mKey.dump();

        // 获取用户名
        if (id == null) {
            userBankCard.bankCardErrorMsg(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mkey);
        if (hashCommon == null) {
            userBankCard.bankCardErrorMsg(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANKCARDLIST;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        //valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "bankcard list REQ:" + finalUrl);


        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                if (result == null || result.length() == 0) {
                    userBankCard.bankCardErrorMsg(GBAccountError.NETWORK_FAILED);
                }

                GBLogUtils.DEBUG_DISPLAY(TAG, "backcard RETURN:" + result);
                //{"root":{"item":[{"is_default":1,"bank_name":"民生银行","tail_number":5100,"p_bid":1036,"account_name":"陈伟涛"},{"is_default":0,"bank_name":"中国农业银行","tail_number":3132,"p_bid":1034,"account_name":"ll"}]}}

                JSONTokener tokener = new JSONTokener(result);
                if (tokener == null) {
                    userBankCard.bankCardErrorMsg(GBAccountError.INVALID_INPUT);
                }

                try {
                    JSONObject roots = new JSONObject(tokener);
                    String root1 = roots.getString("root");
                    //{"root":""}
                    if (root1.equals("")) {
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
                            item.isDefault = Integer.valueOf(is_default) > 0;
                            cards.add(item);

                            userBankCard.getUserBankCard(cards);
                        }
                    } else {
                        JSONObject card = root.getJSONObject("item");
                        WinguoAccountBankCard item = new WinguoAccountBankCard();
                        item.accountName = card.getString("account_name");
                        item.p_bid = card.getString("p_bid");
                        item.bankName = card.getString("bank_name");
                        item.cardNumberTail = card.getString("tail_number");
                        String is_default = card.getString("is_default");
                        item.isDefault = Integer.valueOf(is_default) > 0;
                        cards.add(item);

                        userBankCard.getUserBankCard(cards);
                    }


                    return 0;
                } catch (final JSONException e) {
                    //异常解析 登录失效处理 期初数据
                  /*  Intent filter = new Intent();
                    filter.setAction("quitLogin");
                    context.sendBroadcast(filter);
                    e.printStackTrace();*/
                    getKey(context, new IPublicKey() {
                        @Override
                        public void publicKey(WinguoAccountKey key) {
                            if (key != null) {
                                WinguoAccountDataMgr.setKEY(context, key.getKey());
                                WinguoAccountDataMgr.setTOKEN(context, key.getToken());
                                WinguoAccountDataMgr.setUUID(context, key.getUUID());
                                String userName = WinguoAccountDataMgr.getUserName(context);
                                String hashCommon = "id=" + userName + "&token=" + key.getToken() + "&uuid=" + key.getUUID();
                                hashCommon = WinguoEncryption.commonEncryption(hashCommon, key);
                                WinguoAccountDataMgr.setHashCommon(context, hashCommon);

                                autoLogin(context, WinguoAccountDataMgr.getUserName(context), WinguoAccountDataMgr.getPizza(context), WinguoAccountDataMgr.getTOKEN(context), WinguoAccountDataMgr.getUUID(context), key, new IRequestAutoLogin() {
                                    @Override
                                    public void requestAutoLogin(String statu) {
                                        if (!"error".equals(statu)) {
                                            takeBankCardList(context, userBankCard);
                                        } else {
                                            Intent filter = new Intent();
                                            filter.setAction("quitLogin");
                                            context.sendBroadcast(filter);
                                        }
                                    }

                                    @Override
                                    public void requestAutoLoginErrorMsg(int message) {
                                        Intent filter = new Intent();
                                        filter.setAction("quitLogin");
                                        context.sendBroadcast(filter);
                                    }
                                });
                            }

                        }

                        @Override
                        public void publicKeyErrorMsg(int error) {
                            ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,error));
                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "银行卡列表获取失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });

    }

    /**
     * 获取个人资料
     * 1.账号密码登录 takeWinguoAccountGeneral
     * 2.微信 短信验证码登录获取 requestWeixinInfo
     * @param context
     * @param generalData
     */
    public static final synchronized void takeWinguoAccountGeneral(final Context context, final IGeneralData generalData) {
        // 获取用户名
        WinguoAccountKey mkey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mkey.getToken();
        final String uuid = mkey.getUUID();
        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mkey);
        if (hashCommon == null) {
            generalData.generalDataErrorMsg(GBAccountError.HASH_FAILED);
        }

        // 生成URL
        String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETUSERINFO;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        // valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);
        Log.e("genenral final ===",""+finalUrl);
        GBLogUtils.DEBUG_DISPLAY(TAG, "genenarl final REQ:" + finalUrl);

        //获取账户信息
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                // 没有完善个人信息的 缺少部分字段 {"root":{"dinfo_zip":"","president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"capital":"","tel2":"","countItem":[0,0],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=d1fbcb5e814c970106c74ae4ee5a8203","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"否","fax":"","user_type":0,"dinfo_fax":"","lastlogin":"2017-01-07 13:24:40","url":"","rdatetime":"2017-01-06 16:11:11","shared_flag":0,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"","sex":0,"furikomi_account":"","dinfo_address":"","ico":{"modifyTime":""},"udatetime":"2017-01-06 16:11:11","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=d1fbcb5e814c970106c74ae4ee5a8203","tel_mobi":18300602618,"finfo_kouza_type":"","finfo_finance_code":"","id":43825,"unique_identifier":"d1fbcb5e814c970106c74ae4ee5a8203","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","finfo_kouza_meigi":"","countShop":[0,0],"finfo_kouza_number":"","mailmagazine_flag":0,"address":"","account":18300602618,"employees":"","name_kana":""}}
                //完善过个人信息 {"root":{"dinfo_zip":510000,"president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"adwords":"","capital":"","tel2":"","countItem":[1,1],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=c370cf9d1feb779f5c0c0c055bb254f3","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"是","fax":"","user_type":3,"dinfo_fax":"","lastlogin":"2017-01-07 13:48:53","temp_type":1,"url":"","rdatetime":"2016-11-18 16:44:00","shared_flag":1,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"陈生","sex":0,"furikomi_account":"","dinfo_address":"龙膜","ico":{"modifyTime":""},"udatetime":"2017-01-07 12:55:30","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=c370cf9d1feb779f5c0c0c055bb254f3","sname":"啊涛空间站","tel_mobi":18602029479,"finfo_kouza_type":"","finfo_finance_code":"","id":42426,"unique_identifier":"c370cf9d1feb779f5c0c0c055bb254f3","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","pc_shop_address":"http:\/\/atao.winguo.com","finfo_kouza_meigi":"","countShop":[0,0],"shop_address":"http:\/\/atao.m.winguo.com","finfo_kouza_number":"","mailmagazine_flag":0,"resume":"","address":"","account":"youketao1","QR_code":"http:\/\/api.winguo.com\/data\/qrcode?chl=http%3A%2F%2Fatao.m.winguo.com%2F","shop_url":"atao","employees":"","name_kana":""}}
                JSONTokener jsont = new JSONTokener(result);
                Log.e("login:","result:"+result);
                GBLogUtils.DEBUG_DISPLAY(TAG, "loginRETURN:" + result);
                if (jsont == null) {
                    generalData.generalDataErrorMsg(GBAccountError.INVALID_PEER_DATA);
                }
                /*<message> <satus>error</status> <text>登录已过期，请重新登录。</text><code>-101</code> </message>*/
                try {

                    JSONObject roots = new JSONObject(jsont);
                    JSONObject root = roots.getJSONObject("root");
                    String void_flag = root.getString("void_flag");
                    WinguoAccountGeneral info = new WinguoAccountGeneral();
                    if (void_flag.equals("0")) { //0 表示有效

                        String shared_flag = root.getString("shared_flag");//是否是分销  1是（有店铺名 pc 店铺 店铺网址）其他 没有上述字段
                        String sname = root.getString("sname");
                        info.shopName = sname;
                        if (shared_flag.equals("1")) {
                            String pc_shop_address = root.getString("pc_shop_address");
                            String shop_address = root.getString("shop_address");
                            String qr_code = root.getString("QR_code");
                            info.pcShopAddr = pc_shop_address;
                            info.mobileShopAddr = shop_address;
                            info.qr_code = qr_code;
                        }
                        String lastlogin = root.getString("lastlogin");
                        String password = root.getString("password");
                        String rdatetime = root.getString("rdatetime");
                        String tel_mobi = root.getString("tel_mobi");
                        String id1 = root.getString("id");
                        String account = root.getString("account");
                        String userName = root.getString("name");
                        String udatetime = root.getString("udatetime");
                        double cashCoupon = root.getDouble("cashCoupon");
                        String cashCredit = root.getString("cashCredit");
                        int isCreater = root.getInt("isCreater");
                        info.isCreater =isCreater;
                        info.cashCoupon =cashCoupon;
                        info.cashCredit =cashCredit;

                        String ico = root.getString("ico");
                        if (ico.contains("content")) {
                            JSONObject ico1 = new JSONObject(ico);
                            String modifyTime = ico1.getString("modifyTime");
                            //"ico":{"content":"http://img.winguo.com/group1/M00/00/40/wKgAUVicBwCAAJFYAAIvEeUbksA832.png","modifyTime":1486620416},
                            info.icoUrl = ico1.getString("content");
                        } else {
                            //无头像"ico":{"modifyTime":""}
                            info.icoUrl = "";
                        }
                        info.accountName = account;
                        info.password = password;
                        info.userName = userName;
                        info.id = id1;
                        info.lastLoginDate = lastlogin;
                        info.regDate = rdatetime;
                        info.updateDate = udatetime;
                        info.telMobile = tel_mobi;
                        info.type = root.getInt("type");
                        info.shared = shared_flag.equals("1");

                        info.maker_id = root.getString("maker_id");
                        info.maker_shop_type = root.getString("maker_shop_type");

                    }

                    generalData.getGeneral(info);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常解析 登录失效处理 期初数据
                   /* Intent filter = new Intent();
                    filter.setAction("quitLogin");
                    context.sendBroadcast(filter);
                    */
                    getKey(context, new IPublicKey() {
                        @Override
                        public void publicKey(WinguoAccountKey key) {
                            if (key != null) {
                                StartApp.mKey = key;
                                WinguoAccountDataMgr.setKEY(context, key.getKey());
                                WinguoAccountDataMgr.setTOKEN(context, key.getToken());
                                WinguoAccountDataMgr.setUUID(context, key.getUUID());
                                String userName = WinguoAccountDataMgr.getUserName(context);
                                String hashCommon = "id=" + userName + "&token=" + key.getToken() + "&uuid=" + key.getUUID();
                                hashCommon = WinguoEncryption.commonEncryption(hashCommon, key);
                                WinguoAccountDataMgr.setHashCommon(context, hashCommon);

                                login(context, WinguoAccountDataMgr.getUserName(context), WinguoAccountDataMgr.getPizza(context), key, false, new ITakeWinguoGeneralResult() {
                                    @Override
                                    public void takeWinguoGeneralResult(int code, boolean isSuccess) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void publicKeyErrorMsg(int error) {
                            ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,error));
                        }

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "用户信息失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });


    }

    /**
     * 个人资料 requestWeixinInfo
     * @param context
     * @param generalData
     */
    public static final synchronized void requestWeixinInfo(final Context context, final IGeneralData generalData) {
        // 获取用户名
        WinguoAccountKey mkey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mkey.getToken();
        final String uuid = mkey.getUUID();
        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mkey);
        if (hashCommon == null) {
            generalData.generalDataErrorMsg(GBAccountError.HASH_FAILED);
        }

        // 生成URL
        String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETUSERINFO;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        // valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "genenarl final REQ:" + finalUrl);

        //获取账户信息
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                // 没有完善个人信息的 缺少部分字段 {"root":{"dinfo_zip":"","president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"capital":"","tel2":"","countItem":[0,0],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=d1fbcb5e814c970106c74ae4ee5a8203","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"否","fax":"","user_type":0,"dinfo_fax":"","lastlogin":"2017-01-07 13:24:40","url":"","rdatetime":"2017-01-06 16:11:11","shared_flag":0,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"","sex":0,"furikomi_account":"","dinfo_address":"","ico":{"modifyTime":""},"udatetime":"2017-01-06 16:11:11","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=d1fbcb5e814c970106c74ae4ee5a8203","tel_mobi":18300602618,"finfo_kouza_type":"","finfo_finance_code":"","id":43825,"unique_identifier":"d1fbcb5e814c970106c74ae4ee5a8203","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","finfo_kouza_meigi":"","countShop":[0,0],"finfo_kouza_number":"","mailmagazine_flag":0,"address":"","account":18300602618,"employees":"","name_kana":""}}
                //完善过个人信息 {"root":{"dinfo_zip":510000,"president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"adwords":"","capital":"","tel2":"","countItem":[1,1],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=c370cf9d1feb779f5c0c0c055bb254f3","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"是","fax":"","user_type":3,"dinfo_fax":"","lastlogin":"2017-01-07 13:48:53","temp_type":1,"url":"","rdatetime":"2016-11-18 16:44:00","shared_flag":1,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"陈生","sex":0,"furikomi_account":"","dinfo_address":"龙膜","ico":{"modifyTime":""},"udatetime":"2017-01-07 12:55:30","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=c370cf9d1feb779f5c0c0c055bb254f3","sname":"啊涛空间站","tel_mobi":18602029479,"finfo_kouza_type":"","finfo_finance_code":"","id":42426,"unique_identifier":"c370cf9d1feb779f5c0c0c055bb254f3","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","pc_shop_address":"http:\/\/atao.winguo.com","finfo_kouza_meigi":"","countShop":[0,0],"shop_address":"http:\/\/atao.m.winguo.com","finfo_kouza_number":"","mailmagazine_flag":0,"resume":"","address":"","account":"youketao1","QR_code":"http:\/\/api.winguo.com\/data\/qrcode?chl=http%3A%2F%2Fatao.m.winguo.com%2F","shop_url":"atao","employees":"","name_kana":""}}
                JSONTokener jsont = new JSONTokener(result);
                GBLogUtils.DEBUG_DISPLAY(TAG, "loginRETURN:" + result);
                if (jsont == null) {
                    generalData.generalDataErrorMsg(GBAccountError.INVALID_PEER_DATA);
                }
                /*<message> <satus>error</status> <text>登录已过期，请重新登录。</text><code>-101</code> </message>*/
                try {

                    JSONObject roots = new JSONObject(jsont);
                    JSONObject root = roots.getJSONObject("root");
                    String void_flag = root.getString("void_flag");
                    WinguoAccountGeneral info = new WinguoAccountGeneral();
                    if (void_flag.equals("0")) { //0 表示有效

                        String shared_flag = root.getString("shared_flag");//是否是分销  1是（有店铺名 pc 店铺 店铺网址）其他 没有上述字段
                        String sname = root.getString("sname");
                        info.shopName = sname;
                        if (shared_flag.equals("1")) {
                            String pc_shop_address = root.getString("pc_shop_address");
                            String shop_address = root.getString("shop_address");
                            String qr_code = root.getString("QR_code");
                            info.pcShopAddr = pc_shop_address;
                            info.mobileShopAddr = shop_address;
                            info.qr_code = qr_code;
                        }
                        String lastlogin = root.getString("lastlogin");
                        String password = root.getString("password");
                        String rdatetime = root.getString("rdatetime");
                        String tel_mobi = root.getString("tel_mobi");
                        String id1 = root.getString("id");
                        String account = root.getString("account");
                        String userName = root.getString("name");
                        String udatetime = root.getString("udatetime");
                        double cashCoupon = root.getDouble("cashCoupon");
                        String cashCredit = root.getString("cashCredit");
                        int isCreater = root.getInt("isCreater");
                        info.isCreater =isCreater;
                        info.cashCoupon =cashCoupon;
                        info.cashCredit =cashCredit;

                        String ico = root.getString("ico");
                        if (ico.contains("content")) {
                            JSONObject ico1 = new JSONObject(ico);
                            String modifyTime = ico1.getString("modifyTime");
                            //"ico":{"content":"http://img.winguo.com/group1/M00/00/40/wKgAUVicBwCAAJFYAAIvEeUbksA832.png","modifyTime":1486620416},
                            info.icoUrl = ico1.getString("content");
                        } else {
                            //无头像"ico":{"modifyTime":""}
                            info.icoUrl = "";
                        }
                        info.accountName = account;
                        info.password = password;
                        info.userName = userName;
                        info.id = id1;
                        info.lastLoginDate = lastlogin;
                        info.regDate = rdatetime;
                        info.updateDate = udatetime;
                        info.telMobile = tel_mobi;
                        info.type = root.getInt("type");
                        info.shared = shared_flag.equals("1");

                        info.maker_id = root.getString("maker_id");
                        info.maker_shop_type = root.getString("maker_shop_type");

                    }

                    generalData.getGeneral(info);

                } catch (JSONException e) {
                    //异常解析 登录失效处理 期初数据
                    Intent filter = new Intent();
                    filter.setAction(Constants.QUIT_SUCCESS);
                    context.sendBroadcast(filter);
                    generalData.generalDataErrorMsg(GBAccountError.SESSION_TIMEOUT);
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", context.getString(R.string.no_net_or_service_no));
                    context.sendBroadcast(error);
                }
            }
        });


    }

    /**
     * 客户端获取授权码Code  访问后台 后台返回微信用户数据
     * @param context
     * @param loginCallBack
     */
    public static final  void weixinLoginSendCode(final Context context,WinguoAccountKey key,String code, final WeixinLoginCallBack loginCallBack) {
        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append("test");
        strBuf.append("&token=").append(key.getToken());
        strBuf.append("&uuid=").append(key.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), key);
        if (hashCommon == null) {
            loginCallBack.weixinLoginCallBackErrorMsg(GBAccountError.HASH_FAILED);
        }

        // 生成URL
        String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.WEIXIN_LOGIN;

        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("code", code));// 客户端类型：桌面
        final String finalUrl = URLCreator.createNoAnd(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "weixin final REQ:" + finalUrl);

        //获取账户信息
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.e("login","result；"+result);
                if (result != null) {
                    WeixinLoginBean weixinLoginBean = GsonUtil.json2Obj(result, WeixinLoginBean.class);
                    loginCallBack.weixinLoginCallBack(weixinLoginBean);
                } else {
                    loginCallBack.weixinLoginCallBackErrorMsg(GBAccountError.UNKNOWN);
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    //网络超时
                    loginCallBack.weixinLoginCallBackErrorMsg(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });


    }

    /**
     * 验证手机号 是否注册 或 存在此账号
     *
     * @param mobile
     * @param checkPhone
     */
    public static void checkPhone(final Context context, String mobile, final IUserCheckPhone checkPhone) {
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.PHONE_CHECK;
        final int dataSize = 1;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("name", mobile));// 客户端类型：桌面
        final String finalUrl = URLCreator.create(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 1, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "check phone :" + result);
                //
                try {
                    JSONObject root = new JSONObject(result);
                    JSONObject message = root.getJSONObject("message");
                    String status = message.getString("status");
                    int code = message.getInt("code");
                    String text = message.getString("text");
                    checkPhone.checkPhone(code,text);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    checkPhone.checkPhoneErrorMsg(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });


    }

    /**
     * 获取用户标签是否开启
     *
     * @param account 账户名
     */
    public static void checkLabelOpen(String account, final IUserCheckLabel userCheckLabel) {
        String finalURL = UrlConstant.TODAY_SHOP_USER_LABEL_STATU + "&account=" + account;
        GBLogUtils.DEBUG_DISPLAY("today shop checkLabelOpen : finalURL", finalURL);
        MyOkHttpUtils.post(finalURL, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                try {
                    GBLogUtils.DEBUG_DISPLAY("today shop checkLabelOpen : result", result);
                    // result {"msg":"\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a","code":2,"status":0}
                    JSONObject root = new JSONObject(result);
                    String status = root.getString("status");
                    userCheckLabel.checkLabel(status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                userCheckLabel.checkLabelErrorMsg(0);
            }
        });
    }


    /**
     * 登陆获取今日商品
     *
     * @param
     */
    public static void takeShopDataLogin(String account, final IUserLabelShopData userLabelShopData) {
        String finalURL = UrlConstant.TODAY_SHOP_NEW_LOGIN + "&account=" + account;
        GBLogUtils.DEBUG_DISPLAY("today shop takeShopDataLogin : finalURL", finalURL);
        MyOkHttpUtils.post(finalURL, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                try {
                    GBLogUtils.DEBUG_DISPLAY("dnw takeShopDataLogin: result", result);
                    JSONObject root = new JSONObject(result);
                    JSONArray goodsitem = root.getJSONArray("goodsitem");
                    List<UserTodayShop> data = new ArrayList<UserTodayShop>();
                    for (int i = 0; i < goodsitem.length(); i++) {
                        UserTodayShop item = GsonUtil.json2Obj(goodsitem.get(i).toString(), UserTodayShop.class);
                        data.add(item);
                    }
                    userLabelShopData.labelShopData(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {

            }
        });
    }

    /**
     * 未登录 获取商品
     *
     * @param todayShopData
     */
    public static void takeShopDataNoLogin(final ITodayShopData todayShopData) {

        GBLogUtils.DEBUG_DISPLAY("takeShopDataNoLogin: final：", UrlConstant.TODAY_SHOP_NEW_NO_LOGIN);
        MyOkHttpUtils.post(UrlConstant.TODAY_SHOP_NEW_NO_LOGIN, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                try {
                    GBLogUtils.DEBUG_DISPLAY("dnw takeShopDataNoLogin: result", result);
                    JSONObject root = new JSONObject(result);
                    JSONArray goodsitem = root.getJSONArray("goodsitem");
                    List<UserTodayShop> data = new ArrayList<UserTodayShop>();
                    for (int i = 0; i < goodsitem.length(); i++) {
                        UserTodayShop item = GsonUtil.json2Obj(goodsitem.get(i).toString(), UserTodayShop.class);
                        data.add(item);
                    }
                    todayShopData.shopData(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {

            }
        });
    }

    /**
     * 开启标签
     *
     * @param account
     */
    public static void userLabelOpen(String account, final IOpenLabel openLabel) {
        String finalURL = UrlConstant.TODAY_SHOP_OPEN_USER_LABEL + "&account=" + account;
        GBLogUtils.DEBUG_DISPLAY("dnw userLabelOpen", finalURL);
        MyOkHttpUtils.post(finalURL, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                GBLogUtils.DEBUG_DISPLAY("userLabelOpen result:", result);
                openLabel.openLabel(result);

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {

            }
        });
    }

    /**
     * 开通空间站成功  左侧菜单重新获取个人资料
     * @param mContext
     */
    public static void refreshGeneral(final Context mContext){
        takeWinguoAccountGeneral(mContext, new IGeneralData() {

            @Override
            public void getGeneral(WinguoAccountGeneral winguoAccountGeneral) {
                Intent intent = new Intent("landscape");
                intent.putExtra("info", winguoAccountGeneral);
                mContext.sendBroadcast(intent);
                GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = winguoAccountGeneral;
            }

            @Override
            public void generalDataErrorMsg(int message) {
                ToastUtil.showToast(mContext, WinguoAccountImpl.getErrorMsg(mContext,message));
            }
        });
    }

    /**
     * 登录成功后 获取个人信息（果币 余额 个人资料）
     *
     * @param mContext
     */
    public static void loginSuccess(final Context mContext, final ITakeWinguoGeneralResult takeWinguoGeneralResult) {

        //获取个人信息
        takeWinguoAccountGeneral(mContext, new IGeneralData() {
            @Override
            public void getGeneral(WinguoAccountGeneral winguoAccountGeneral) {
                GBAccountMgr.getInstance().mAccountInfo.status = GBAccountStatus.usr_logged;
                GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = winguoAccountGeneral;
                //登陆成功后发出一条广播
                Intent intent = new Intent(Constants.LOGIN_SUCCESS);
                intent.putExtra("info", winguoAccountGeneral);
                mContext.sendBroadcast(intent);
                Log.e("login:",""+winguoAccountGeneral);
                if (takeWinguoGeneralResult != null)
                    takeWinguoGeneralResult.takeWinguoGeneralResult(0, true);
            }

            @Override
            public void generalDataErrorMsg(int message) {
                if (takeWinguoGeneralResult != null)
                    takeWinguoGeneralResult.takeWinguoGeneralResult(GBAccountError.REQUST_TIMEOUT, false);
            }
        });
    }

    public interface ITakeWinguoGeneralResult {
        void takeWinguoGeneralResult(int code, boolean isSuccess);
    }

    /**
     * 获取伙伴信息
     *
     * @param account
     */
    public static void userMyPartner(final Context context, final String account, final IUserMyPartner myPartner) {

        StringBuffer strBuf = new StringBuffer();
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        //  final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        strBuf.append("id=").append(account);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);

        String finalURL = UrlConstant.MY_FAMILY_PARTNER + "&hash=" + hash + "&account=" + account;
        GBLogUtils.DEBUG_DISPLAY("userMyPartner", finalURL);
        MyOkHttpUtils.post(finalURL, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                GBLogUtils.DEBUG_DISPLAY("userMyPartner result:", result);
                try {
                    JSONObject root = new JSONObject(result);
                    String result1 = root.getString("Result");

                } catch (JSONException e) {
                    e.printStackTrace();
                    getKey(context, new IPublicKey() {
                        @Override
                        public void publicKey(WinguoAccountKey key) {
                            if (key != null) {
                                StartApp.mKey = key;
                                WinguoAccountDataMgr.setKEY(context, key.getKey());
                                WinguoAccountDataMgr.setTOKEN(context, key.getToken());
                                WinguoAccountDataMgr.setUUID(context, key.getUUID());
                                String userName = WinguoAccountDataMgr.getUserName(context);
                                String hashCommon = "id=" + userName + "&token=" + key.getToken() + "&uuid=" + key.getUUID();
                                hashCommon = WinguoEncryption.commonEncryption(hashCommon, key);
                                WinguoAccountDataMgr.setHashCommon(context, hashCommon);
                                userMyPartner(context, account, myPartner);
                            }

                        }

                        @Override
                        public void publicKeyErrorMsg(int error) {
                            ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,error));
                        }

                    });

                }
                myPartner.userMyPartner(result);


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                myPartner.userMyPartnerErrorMsg(0);
            }
        });
    }

    /**
     * 修改支付密码 通过验证码
     *
     * @param account
     */
    public static void resetPayPW(Context context, String account, String authcode, String payPw, String send_page,final IResetPayPwd reset) {

        // 获取公钥
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
       // final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(account);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        strBuf.append("&mobile=").append(account);
        strBuf.append("&authcode=").append(authcode);
        strBuf.append("&pa=").append(payPw);
        strBuf.append("&send_page=").append(send_page);
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        GBLogUtils.DEBUG_DISPLAY("resetPayPW hash", hash);
        if (hash == null) {
            reset.resetPayPwdErrorMsg(GBAccountError.HASH_FAILED);
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.SET_PAY_PWD;
        final int dataSize = 6;
        ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// HASH
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "dnw resetPayPW REQ:" + finalUrl);


        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                GBLogUtils.DEBUG_DISPLAY("dnw resetPayPW result:", result);
                // {"message":{"status":"error","text":"抱歉！您的验证码错误或已过期！","code":-3}}
                reset.resetPayPwd(result);


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                reset.resetPayPwdErrorMsg(GBAccountError.REQUST_TIMEOUT);
            }
        });
    }

    /**
     * 修改支付密码 通过旧密码
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    public static void resetPayPwByOld(Context context, String oldPwd, String newPwd, final IResetPayPwd reset) {

        // 获取公钥
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        strBuf.append("&np=").append(newPwd);
        strBuf.append("&op=").append(oldPwd);
        GBLogUtils.DEBUG_DISPLAY(TAG, "dnw resetPayPW by old REQ:" + strBuf.toString());
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            reset.resetPayPwdErrorMsg(GBAccountError.HASH_FAILED);
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.SET_PAY_PWD_BY_OLD;
        final int dataSize = 6;
        ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// HASH
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "resetPayPW by old REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                GBLogUtils.DEBUG_DISPLAY("resetPayPW by old  result:", result);
                reset.resetPayPwd(result);


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                reset.resetPayPwdErrorMsg(GBAccountError.REQUST_TIMEOUT);
            }
        });
    }

    /**
     * 开通空间站
     */
    public static void openSpaceStation(final Context context, String accountName, String spaceName,final IOpenSpaceStation openSpaceStation) {


        final String openHead = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.OPEN_SPACE_STATION;
        final int dataSize = 5;
        ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
        try {
            valueList.add(new NameValuePair("account", URLEncoder.encode(accountName, "UTF-8")));// 手机号
            valueList.add(new NameValuePair("name", URLEncoder.encode(spaceName, "UTF-8")));// 手机号
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String finalUrl = URLCreator.create(openHead, valueList);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                //{"message":{"status":"success","text":"开店成功","code":0}}

                GBLogUtils.DEBUG_DISPLAY(OpenSpaceActivity.class.getSimpleName(), "==========re:" + result);
                //{"message":{"status":"error","text":"该姓名或域名已经存在","code":-3}}

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject  message =jsonObject.getJSONObject("message");
                    String status = message.getString("status");
                    String text = message.getString("text");
                    openSpaceStation.requestOpenSpaceStation(status,text);

                   // Intent openSpaceMess = new Intent("openSpace");
                   // openSpaceMess.putExtra("status", status);
                    //openSpaceMess.putExtra("text", text);
                   // context.sendBroadcast(openSpaceMess);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }


            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    openSpaceStation.requestOpenError("开通空间站失败，服务器繁忙！稍后请尝试...");
                }
            }
        });


    }


    /**
     * 忘记密码，对密码进行重置
     *
     * @param context  上下文
     * @param id       用户名
     * @param mobile   手机号码
     * @param authcode 验证码
     * @param newPwd   新密码
     * @return
     */
    public static void resetLoginPwd(final Context context, String id, String mobile, String authcode, String newPwd,String pwType,final IResetLoginPwd resetLoginPwd) {

        try {
            // 判断参数是否合法
            if ((id == null) || (id.length() <= 0) || (mobile == null) || (mobile.length() <= 0) || (authcode == null)
                    || (authcode.length() <= 0) || (newPwd == null) || (newPwd.length() <= 0)) {
                resetLoginPwd.resetLoginPwdErrorMsg(GBAccountError.INVALID_INPUT);
                return;
            }

            // 获取公钥
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            strBuf.append("&mobile=").append(mobile);
            strBuf.append("&authcode=").append(authcode);
            strBuf.append("&pa=").append(newPwd);
            final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hash == null) {
                resetLoginPwd.resetLoginPwdErrorMsg(GBAccountError.HASH_FAILED);
                return ;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.SET_PWD;
            final int dataSize = 6;
            ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
            valueList.add(new NameValuePair("hash", hash));// HASH
            valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
            valueList.add(new NameValuePair("send_page", pwType));// 发送页面  1 注册页面  2. 忘记登入密码  3.忘记支付密码
            valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
            String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

            //网络请求
            MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
                @Override
                public int stringReturn(String returnStr) {
                    if ((returnStr == null) || (returnStr.length() == 0)) {
                        resetLoginPwd.resetLoginPwdErrorMsg(GBAccountError.NETWORK_FAILED);
                    }

                    Intent intentresetPaw = new Intent("intentresetPaw");

                    intentresetPaw.putExtra("intentresetPawjson", returnStr);

                    context.sendBroadcast(intentresetPaw);

                    Log.i("chenqian", "重置密码的json 数据:::" + returnStr);

                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                       /* Intent error = new Intent("O");
                        error.putExtra("errorMsg", "重置密码失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);*/
                       resetLoginPwd.resetLoginPwdErrorMsg(GBAccountError.REQUST_TIMEOUT);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void submitFeedback(final Context context, final String content,final ISubmitFeedBack submitFeedBack) {

        try {
            final int[] code = new int[1];
            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                submitFeedBack.requestSubmitFeedBackError(GBAccountError.INVALID_KEY);
                return;
            }
            // mKey.dump();

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            final String token = WinguoAccountDataMgr.getTOKEN(context);
            final String uuid = WinguoAccountDataMgr.getUUID(context);
            if (id == null) {
                submitFeedBack.requestSubmitFeedBackError(GBAccountError.USER_NAME_NOT_FOUND);
                return;
            }
            // 创建Hash字符串
            final String hash;

            if (!mKey.verify()) {
                submitFeedBack.requestSubmitFeedBackError(GBAccountError.INVALID_KEY);
                return;
            }

            if ((id == null) || (id.length() <= 0)) {
                submitFeedBack.requestSubmitFeedBackError(GBAccountError.INVALID_INPUT);
                return;
            }
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(token);
            strBuf.append("&uuid=").append(uuid);
            strBuf.append("&mobile_type=").append(Build.MANUFACTURER);
            strBuf.append("&network_type=").append(NetWorkUtil.getNetworkType(context));
            strBuf.append("&browse_type=").append("0");
            strBuf.append("&content=").append(URLEncoder.encode(content, "utf-8"));
            hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);

            if (hash == null) {
                submitFeedBack.requestSubmitFeedBackError(GBAccountError.HASH_FAILED);
                return;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.FEEDBACK;
            final int dataSize = 6;
            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
            valueList.add(new NameValuePair("hash", hash));// 账号密码加密
            String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "feedback REQ:" + finalUrl);

            MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    // result = {"message":{"not_verified":0,"text":"登录成功","code":0,"status":"success","name":18300602618}}
                    //<message><status>success</status><text>反馈成功，感谢您的参与</text><code>0</code></message>
                    JSONTokener jsont = new JSONTokener(result);
                    if (jsont == null) {
                        return GBAccountError.INVALID_PEER_DATA;
                    }

                    try {
                        JSONObject object = new JSONObject(jsont);
                        JSONObject message = object.getJSONObject("message");
                        String text = message.getString("text");
                        String status = message.getString("status");
                        submitFeedBack.requestSubmitFeedBack(status,text);

                    } catch (JSONException e) {
                        //异常解析 登录失效处理 期初数据
                        ToastUtil.showToast(context,"登陆过期，请重新登录！");
                        Intent filter = new Intent();
                        filter.setAction("quitLogin");
                        context.sendBroadcast(filter);
                        e.printStackTrace();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return code[0];
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        submitFeedBack.requestSubmitFeedBackError(GBAccountError.REQUST_TIMEOUT);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送手机验证码
     * @param mobile
     * @param sendPageType 发送页面：默认为1即注册页面 2.忘记登入密码 3.忘记支付密码 16.绑定手机
     * @return
     */
    public static void sendVerificationCode(final Context context, String mobile, String sendPageType,final ISendVerificationCode sendVerificationCode) {
        final Context mcontext = context;
        long time = System.currentTimeMillis() / 1000;
        String secretkey;
        if (WinguoAccountConfig.isDEBUG()) {
            secretkey = "Vj~6KhNt@QM87v-WE9^2";//测试版
        } else {
            secretkey = "3msQh~nSs3!Rqe-daQn";// 正式版
        }
        String keys = time + "|" + HttpKey.getEncryptText(secretkey + time);

        // 判断参数是否合法
        if ((mobile == null) || (mobile.length() <= 0) || (sendPageType == null) || (sendPageType.length() <= 0)) {
            sendVerificationCode.requestSendVerificationCodeError(GBAccountError.INVALID_INPUT);
            return;
        }
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETMOBILECODE;
        final int dataSize = 5;
        ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);

        valueList.add(new NameValuePair("mobile", mobile));// 手机号
        valueList.add(new NameValuePair("keys", keys));// keys
        valueList.add(new NameValuePair("send_page", sendPageType));// 发送页面
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        // valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        String finalUrl = URLCreator.create(url, valueList);
        //网络请求
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String returnStr) {
                if ((returnStr == null) || (returnStr.length() == 0)) {
                    return GBAccountError.NETWORK_FAILED;
                }
                sendVerificationCode.requestSendVerificationCode(returnStr);
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    sendVerificationCode.requestSendVerificationCodeError(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });


    }
    /**
     * 提交意见反馈
     */
    public interface ISendVerificationCode{
        void requestSendVerificationCode(String result);
        void requestSendVerificationCodeError(int errorcode);
    }
    /**
     * 手机号 登录
     * 发送手机验证码
     * /data/user/smslogin?a=sms&mobile=13416290389
     * @param mobile
     * @return
     */
    public static void requestCodeToLogin(final Context context, String mobile,final IRequstLoginCodeCallback requstLoginCodeCallback) {

        // 判断参数是否合法
        if ((mobile == null) || (mobile.length() <= 0)  ) {
            requstLoginCodeCallback.requstLoginCodeCallbackError(GBAccountError.INVALID_INPUT);
            return;
        }
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.LOGIN_CHECKCODE;
        ArrayList<NameValuePair> valueList = new ArrayList<>();
        valueList.add(new NameValuePair("mobile", mobile));// 手机号
        String finalUrl = URLCreator.create(url, valueList);
        //网络请求
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String returnStr) {
                if ((returnStr == null) || (returnStr.length() == 0)) {
                    requstLoginCodeCallback.requstLoginCodeCallbackError(GBAccountError.NETWORK_FAILED);
                } else {
                    requstLoginCodeCallback.requstLoginCodeCallback(returnStr);
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    requstLoginCodeCallback.requstLoginCodeCallbackError(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });


    }

    /**
     * 创客 充值数量
     * @param context
     * @param rechargeNumberCallback
     */
    public static void requestRechargeNumber(final Context context, final IRequstRechargeNumberCallback rechargeNumberCallback) {

        // 获取公钥
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            rechargeNumberCallback.requstRechargeNumberCallbackError(GBAccountError.HASH_FAILED);
        }

        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.RECHARGE_NUMBER;
        ArrayList<NameValuePair> valueList = new ArrayList<>();
        valueList.add(new NameValuePair("hash", hash));
        String finalUrl = URLCreator.create(url, valueList);
        //网络请求
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String returnStr) {
                if ((returnStr == null) || (returnStr.length() == 0)) {
                    rechargeNumberCallback.requstRechargeNumberCallbackError(GBAccountError.NETWORK_FAILED);
                } else {
                    rechargeNumberCallback.requstRechargeNumberCallback(returnStr);
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    rechargeNumberCallback.requstRechargeNumberCallbackError(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });
    }
    /**
     * 创客充值金额 获取接口
     */
    public interface IRequstRechargeNumberCallback{
        void requstRechargeNumberCallback(String result);
        void requstRechargeNumberCallbackError(int errorcode);
    }

    /**
     * 登录验证码 获取接口
     */
    public interface IRequstLoginCodeCallback{
        void requstLoginCodeCallback(String result);
        void requstLoginCodeCallbackError(int errorcode);
    }


    /**
     * 手机号 登录
     * 发送手机验证码
     * /data/user/smslogin?a=login&mobile=13100138000&vcode=755460&hash=sdsd
     * @param mobile
     * @return
     */
    public static void requestLoginByCode(final Context context, String mobile,String code ,final WinguoAccountKey mKey,final IRequstLoginByCodeCallback requstLoginCallback) {
        // 创建Hash字符串
        final String hash;

        if (mKey == null) {
            requstLoginCallback.requstLoginByCodeCallbackError(GBAccountError.WINGUOACCOUNTKEY_NULL);
            return;
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(mobile);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());

        hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            requstLoginCallback.requstLoginByCodeCallbackError(GBAccountError.HASH_FAILED);
            return;
        }

        // 判断参数是否合法
        if ((mobile == null) || (mobile.length() <= 0)  ) {
            requstLoginCallback.requstLoginByCodeCallbackError(GBAccountError.INVALID_INPUT);
            return;
        }
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.LOGIN_BYCODE;
        ArrayList<NameValuePair> valueList = new ArrayList<>();
        valueList.add(new NameValuePair("mobile", mobile));// 手机号
        valueList.add(new NameValuePair("vcode", code));// 验证码
        valueList.add(new NameValuePair("hash", hash));// 加密hash
        String finalUrl = URLCreator.create(url, valueList);
        //网络请求
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String returnStr) {
                if ((returnStr == null) || (returnStr.length() == 0)) {
                    requstLoginCallback.requstLoginByCodeCallbackError(GBAccountError.NETWORK_FAILED);
                } else {
                    requstLoginCallback.requstLoginByCodeCallback(returnStr);
                }
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    requstLoginCallback.requstLoginByCodeCallbackError(GBAccountError.REQUST_TIMEOUT);
                }
            }
        });

    }

    /**
     * 获取现金券 余额 创客基金
     * @param context
     * @param balanceAndCashCallback
     */
    public static void requestBalanceAndCash(final Context context,final IBalanceAndCashCallback balanceAndCashCallback) {
        // 获取用户名
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();
        if (id == null) {
            balanceAndCashCallback.balanceAndCashErrorMsg(GBAccountError.USER_NAME_NOT_FOUND);
            return;
        }

        // 获取公钥
        if (!mKey.verify()) {
            balanceAndCashCallback.balanceAndCashErrorMsg(GBAccountError.INVALID_KEY);
            return;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            balanceAndCashCallback.balanceAndCashErrorMsg(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BALANCE_CASH;
        final int dataSize = 3;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 加密的密码信息
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "balance cash  REQ:" + finalUrl);

        // 网络连接
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

               /* {
                    "code": 0,
                        "msg": "获取成功",
                        "item": {
                    "cash_coupon": "2100.00",
                            "cash_credit": "2000.00",
                            "purse_balance": "973094.90"
                     }
                }*/

                JSONTokener jsont = new JSONTokener(result);
                if (jsont == null) {
                    balanceAndCashCallback.balanceAndCashErrorMsg(GBAccountError.INVALID_PEER_DATA);
                }
                BalanceAndCash balanceAndCash = GsonUtil.json2Obj(result, BalanceAndCash.class);
                balanceAndCashCallback.getBalanceAndCash(balanceAndCash);

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                balanceAndCashCallback.balanceAndCashErrorMsg(GBAccountError.REQUST_TIMEOUT);
            }
        });


    }

    /**
     * 验证码 登录
     * 接口回调
     */
    public interface IRequstLoginByCodeCallback{
        void requstLoginByCodeCallback(String result);
        void requstLoginByCodeCallbackError(int errorcode);
    }


    /**
     * 提交意见反馈
     */
    public interface ISubmitFeedBack{
        void requestSubmitFeedBack(String status,String text);
        void requestSubmitFeedBackError(int errorcode);
    }


    /**
     * 修改支付密码 通过验证码
     */
    public interface IResetLoginPwd {
        void resetLoginPwd(String result);

        void resetLoginPwdErrorMsg(int message);
    }


    /**
     * 开通空间站
     */
    public interface IOpenSpaceStation{
        void requestOpenSpaceStation(String status,String text);
        void requestOpenError(String error);
    }


    public interface IPublicKey {
        void publicKey(WinguoAccountKey key);

        void publicKeyErrorMsg(int error);
    }

    /**
     * 修改支付密码 通过验证码
     */
    public interface IUserMyPartner {
        void userMyPartner(String result);
        void userMyPartnerErrorMsg(int message);
    }

    /**
     * 修改支付密码 通过验证码
     */
    public interface IResetPayPwd {
        void resetPayPwd(String result);

        void resetPayPwdErrorMsg(int message);
    }


    /**
     * 登陆 用户开启标签状态
     */
    public interface IOpenLabel {
        void openLabel(String result);

        void openLabelErrorMsg(int message);
    }

    /**
     * 未登陆 获取今日商品
     */
    public interface ITodayShopData {
        void shopData(List<UserTodayShop> data);

        void shopDataErrorMsg(int message);
    }

    /**
     * 登陆获取今日商品
     */
    public interface IUserLabelShopData {
        void labelShopData(List<UserTodayShop> data);

        void labelShopDataErrorMsg(int message);
    }

    /**
     * 检测用户是否开启标签
     */
    public interface IUserCheckLabel {
        void checkLabel(String status);

        void checkLabelErrorMsg(int message);
    }

    /**
     * 检测用户手机号是否注册
     */
    public interface IUserCheckPhone {
        void checkPhone(int code, String text);

        void checkPhoneErrorMsg(int message);
    }

    public interface IUserBankCard {
        void getUserBankCard(List<WinguoAccountBankCard> bankCards);

        void bankCardErrorMsg(int message);
    }

    public interface UserCurrencyData {
        void getCurrency(WinguoAccountCurrency currency);

        void currencyErrorMsg(int message);
    }

    public interface BalanceData {
        void getBalance(WinguoAccountBalance winguoAccountBalance);

        void balanceErrorMsg(int message);
    }
    public interface IBalanceAndCashCallback extends BaseRequestCallBack{

        void getBalanceAndCash(BalanceAndCash balanceAndCash);

        void balanceAndCashErrorMsg(int message);
    }

    public interface IGeneralData {
        void getGeneral(WinguoAccountGeneral winguoAccountGeneral);

        void generalDataErrorMsg(int message);
    }

    /**
     * 微信获取授权码code  访问后台 后台返回数据
     */
    public interface WeixinLoginCallBack {

        void weixinLoginCallBack(WeixinLoginBean weixinLoginBean);

        void weixinLoginCallBackErrorMsg(int message);
    }

}
