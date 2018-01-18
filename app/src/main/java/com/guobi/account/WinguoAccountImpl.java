package com.guobi.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;
import com.winguo.bean.BindMobileGson;
import com.winguo.personalcenter.OpenSpaceActivity;
import com.winguo.app.StartApp;
import com.winguo.bean.PublicKeyJson;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * 问果账号接口具体实现
 * Created by chenq on 2017/1/3.
 */
public final class WinguoAccountImpl {

    private static final String TAG = "WinguoAccountImpl";

    private static final String NODE_TAG_MSG = "message";
    private static final String NODE_TAG_ROOT = "root";

    private static String sLastErrMsg = "";

    private static void setErrMsg(Context context, String tag, int code) {
        final int num = Math.abs(code);
        final String resName = "gb_account_err_msg_wg_" + tag + "_no_" + num;
        sLastErrMsg = GBResourceUtils.getString(context, resName);
    }

    public static final String getErrorMsg(Context context, int code) {
        return ErrorLog.combineErrorMsg(context, code, sLastErrMsg);
    }

    /**
     * 获取公钥
     *
     * @return 包含key、token和uuid
     */
    public static int getKey(Context context, final WinguoAccountKey key) {

        final int[] keycode = new int[1];
        if (!NetworkUtils.isConnectNet(context)) {
            return GBAccountError.NETWORK_CONN_FAILED;
        }
        // 生成URL
        final String finalUrl = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETKEY;
        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Gson datagson = new Gson();
                PublicKeyJson keydatajson = datagson.fromJson(result, PublicKeyJson.class);
                String hast = keydatajson.getMessage().getHash();
                int cole = keydatajson.getMessage().getCode();
                String text = keydatajson.getMessage().getText();
                WinguoEncryption.analyzerHashContent(hast, key);
                keycode[0] = cole;
                return cole;
            }

            @Override
            public void exceptionMessage(String message) {

            }
        });
        return keycode[0];
    }


    /**
     * 查询余额信息
     *
     * @param context
     * @param info
     * @return
     */

    public static final synchronized int getBalance2(final Context context, final WinguoAccountBalance info) {
        final int[] resultCode = {GBAccountError.UNKNOWN};
        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            resultCode[0] = GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 获取公钥
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            resultCode[0] = GBAccountError.INVALID_KEY;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            resultCode[0] = GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BALANCE;
        final int dataSize = 3;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 加密的密码信息
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        String finalUrl = URLCreator.create(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "balance REQ:" + finalUrl);

        // 网络连接
        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {

            @Override
            public int stringReturn(String result) {
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {

            }
        }, finalUrl);

        //登陆过期
        //<root><latest_balance>0</latest_balance><unable_balance>0</unable_balance><income_time>2017-01-19 10:09</income_time><income_str>2个工作日到账</income_str><balance_out>1</balance_out></root>
        JSONTokener jsont = new JSONTokener(result);
        if (jsont == null) {
            resultCode[0] = GBAccountError.INVALID_PEER_DATA;
        }

        if (result.contains("登录已过期，请重新登录。")) {
            ToastUtil.showToast(context, "登录已过期，请重新登录。");
            GBAccountMgr.getInstance().doClears();
            resultCode[0] = GBAccountError.SESSION_TIMEOUT;
        }
        try {

            JSONObject roots = new JSONObject(jsont);
            JSONObject root = roots.getJSONObject("root");
            String latest_balance = root.getString("latest_balance");
            String unable_balance = root.getString("unable_balance");
            String income_time = root.getString("income_time");
            String income_str = root.getString("income_str");
            String balance_out = root.getString("balance_out");
            info.balance = Float.valueOf(latest_balance);
            info.incomeTime = income_time;
            info.incomeDescription = income_str;
            info.withdrawalTimes = Integer.valueOf(balance_out);
            info.unable_balance = Float.valueOf(unable_balance);

            GBLogUtils.DEBUG_DISPLAY(context, "latest_balance" + latest_balance);
            resultCode[0] = 0;
            Intent intent = new Intent("balance");
            intent.putExtra("latest_balance", info.balance);
            context.sendBroadcast(intent);
            GBAccountMgr.getInstance().mAccountInfo.winguoBalance = info;

        } catch (JSONException e) {
            //异常解析 登录失效处理 期初数据
           /* Intent filter = new Intent();
            filter.setAction("quitLogin");
            context.sendBroadcast(filter);*/
            WinguoAcccountManagerUtils.getKey(context, new WinguoAcccountManagerUtils.IPublicKey() {
                @Override
                public void publicKey(WinguoAccountKey key) {
                    WinguoAccountDataMgr.setKEY(context,key.getKey());
                    WinguoAccountDataMgr.setTOKEN(context,key.getToken());
                    WinguoAccountDataMgr.setUUID(context,key.getUUID());
                    String userName = WinguoAccountDataMgr.getUserName(context);
                    String hashCommon = "id=" + userName+ "&token=" + key.getToken() + "&uuid=" + key.getUUID();
                    hashCommon = WinguoEncryption.commonEncryption(hashCommon, key);
                    WinguoAccountDataMgr.setHashCommon(context,hashCommon);
                    getBalance2(context,new WinguoAccountBalance());
                }

                @Override
                public void publicKeyErrorMsg(int error) {

                }

            });
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultCode[0];

    }



    /**
     * 登陆，保存账户信息
     *
     * @param id       帐户名
     * @param password 密码
     * @return 返回状态码
     */
    public static final int login(Context context, String id, String password, WinguoAccountKey mKey) {
        return login(context, id, password, true, mKey);
    }

    /**
     * 登陆，不保存账户信息
     *
     * @param id       帐户名
     * @param password 密码
     * @return 返回状态码
     */
    public static final int loginNoSave(Context context, String channelId, String id, String password, WinguoAccountKey mKey) {
        return login(context, channelId, password, false, mKey);
    }

    /**
     * 使用保存的信息进行自动登录
     *
     * @return 返回状态码
     */
    public static final synchronized int autoLogin(Context context, WinguoAccountKey mKey) {
        return autologin(context, WinguoAccountDataMgr.getUserName(context), WinguoAccountDataMgr.getPizza(context), WinguoAccountDataMgr.getTOKEN(context), WinguoAccountDataMgr.getUUID(context), mKey);
    }

    private static int autologin(final Context context, String id, String password, String token, String uuid, WinguoAccountKey mKey) {
        // 创建Hash字符串
        final String hash;

        if (!mKey.verify()) {
            return GBAccountError.INVALID_KEY;
        }
        final int resultCode[] = {0};
        if ((id == null) || (id.length() <= 0) || (password == null) || (password.length() <= 0)) {
            return GBAccountError.INVALID_INPUT;
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&pa=").append(password);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);

        if (hash == null) {
            return GBAccountError.HASH_FAILED;
        }

        // 创建Hash2字符串
        String hash2 = "token=" + token + "&uuid=" + uuid;
        hash2 = WinguoEncryption.commonEncryption(hash2, mKey);
        if (hash2 == null) {
            return GBAccountError.HASH_FAILED;
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

        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                //处理-- 网络请求响应信息失败时
                if (!message.equals("ok")) {
                    Intent error = new Intent("autologinerror");
                    error.putExtra("errorMsg", "登录失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        }, finalUrl);

        // result = {"message":{"not_verified":0,"text":"登录成功","code":0,"status":"success","name":18300602618}}
        JSONTokener jsont = new JSONTokener(result);
        if (jsont == null) {
            return GBAccountError.INVALID_PEER_DATA;
        }
        try {

            JSONObject object = new JSONObject(jsont);
            JSONObject message = object.getJSONObject("message");
            String text = message.getString("text");
            String status = message.getString("status");
            resultCode[0] = message.getInt("code");

            if (status.equals("error")) {
                setErrMsg(context, "login", resultCode[0]);
                Toast.makeText(context, text, LENGTH_SHORT).show();

            } else {
                //status no equals error 登陆成功 保存
                //登陆成功后发出一条广播
               // onLoginSuc(context);
                WinguoAcccountManagerUtils.loginSuccess(context,null);
            }
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
        return resultCode[0];
    }

    /**
     * 登陆，可设置是否保存账户信息
     *
     * @param id       帐户名
     * @param password 密码
     * @param save     true:保存登陆信息;false:不保存
     * @param context  true:自动登录;false:不自动登录
     * @return 返回状态码
     */
    private static int login(final Context context, final String id, final String password, final boolean save, final WinguoAccountKey mKey) {
        final int[] code = new int[1];
        // 创建Hash字符串
       final String hash ;

        if ((id == null) || (id.length() <= 0) || (password == null) || (password.length() <= 0)) {
            return GBAccountError.INVALID_INPUT;
        }

        if (mKey == null) {
            Intent error = new Intent("loginerror");
            error.putExtra("errorMsg", "公钥为空");
            context.sendBroadcast(error);
            getKey(context, mKey);
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&pa=").append(password);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());

        hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            return GBAccountError.HASH_FAILED;
        }


        // 创建Hash2字符串
        String hash2 = "token=" + mKey.getToken() + "&uuid=" + mKey.getUUID();
        hash2 = WinguoEncryption.commonEncryption(hash2, mKey);
        if (hash2 == null) {
            return GBAccountError.HASH_FAILED;
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
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "");
                    context.sendBroadcast(error);
                    return GBAccountError.INVALID_PEER_DATA;
                } else try {

                    JSONObject object = new JSONObject(jsont);
                    JSONObject message = object.getJSONObject("message");
                    String text = message.getString("text");
                    String status = message.getString("status");

                    if (status.equals("error")) {
                        setErrMsg(context, "login", code[0]);
                        Intent error = new Intent("loginerror");
                        error.putExtra("errorMsg", text);
                        context.sendBroadcast(error);

                    } else {
                        String name = message.getString("name");
                        code[0] = message.getInt("code");
                        int not_verified = message.getInt("not_verified");
                        if (save) {
                            String hashCommon = "id=" + id + "&token=" + mKey.getToken() + "&uuid=" + mKey.getUUID();
                            hashCommon = WinguoEncryption.commonEncryption(hashCommon, mKey);
                            if (hashCommon == null) {
                                return GBAccountError.HASH_FAILED;
                            }
                            WinguoAccountDataMgr.setUserName(context, id);
                            WinguoAccountDataMgr.setHashLogin(context, hash);
                            WinguoAccountDataMgr.setMD5(context, MD5.md5Appkey(password));
                            WinguoAccountDataMgr.setKEY(context, mKey.getKey());
                            WinguoAccountDataMgr.setTOKEN(context, mKey.getToken());
                            WinguoAccountDataMgr.setUUID(context, mKey.getUUID());
                            WinguoAccountDataMgr.setHashCommon(context, hashCommon);
                            WinguoAccountDataMgr.setPizza(context, password);
                        }
                        //onLoginSuc(context);
                        WinguoAcccountManagerUtils.loginSuccess(context,null);

                    }
                } catch (JSONException e) {
                    //登录过时 通知删除本地缓存数据
                    ToastUtil.showToast(context,"登陆过期，请重新登录！");
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
                    //网络超时 登录失败

                 }
            }
        });
        return GBAccountError.UNKNOWN;
    }

    /**
     * 退出登录，保留用户名，清除其他信息
     *
     * @param context
     * @return
     */
    public static int logout(Context context) {
        return logout(context, false);
    }

    /**
     * 登出系统，清除所有信息
     *
     * @return 返回用户信息以及状态码
     */
    public static int logout(final Context context, boolean clearUsrName) {

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }
        // 读取password加密成的MD5串
        final String pwd = WinguoAccountDataMgr.getMD5(context);
        if (pwd == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }
        // 获取公钥
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            return GBAccountError.INVALID_KEY;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&pa=").append(pwd);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            return GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.LOGOUT;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 账号密码加密
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
//        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.createNoAnd(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "logout REQ:" + finalUrl);


        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null || result.length() == 0) {
                    return GBAccountError.NETWORK_FAILED;
                }

                GBLogUtils.DEBUG_DISPLAY(TAG, "logout RETURN:" + result);


                JSONTokener tokener = new JSONTokener(result);
                if (tokener == null) {
                    return GBAccountError.INVALID_INPUT;
                }
                try {
                    JSONObject roots = new JSONObject(tokener);
                    JSONObject message = roots.getJSONObject("message");
                    // {"message":{"status":"success","text":"成功退出。","code":0}}
                    String status = message.getString("status");
                    String text = message.getString("text");
                    String code = message.getString("code");
                    if (status.equals("success")) {
                        //发广播 通知删除本地账号信息 通知首页 顶部店铺名置空
                        Intent filter = new Intent();
                        filter.setAction("quitLogin");
                        context.sendBroadcast(filter);

                        ToastUtil.showToast(context, text);
                    } else {
                        ToastUtil.showToast(context, text);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //登录过时 通知删除本地缓存数据
                    ToastUtil.showToast(context,"登陆过期，请重新登录！");
                    Intent filter = new Intent();
                    filter.setAction("quitLogin");
                    context.sendBroadcast(filter);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("logouterror");
                    error.putExtra("errorMsg", "退出失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });


        return GBAccountError.UNKNOWN;

    }

    /**
     * 切换正式版和测试版 退出问题
     * @param context
     * @param clearUsrName
     * @return
     */
    public static int changeDebugLogout(final Context context, boolean clearUsrName) {

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }
        // 读取password加密成的MD5串
        final String pwd = WinguoAccountDataMgr.getMD5(context);
        if (pwd == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }
        // 获取公钥
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);;
        if (!mKey.verify()) {
            return GBAccountError.INVALID_KEY;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&pa=").append(pwd);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            return GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url;
        if (!WinguoAccountConfig.isDEBUG()) {
            url = "http://192.168.0.222" + WinguoAccountConfig.LOGOUT;
        } else {
            url = "http://api.winguo.com" + WinguoAccountConfig.LOGOUT;
        }
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// 账号密码加密
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.createNoAnd(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "logout REQ:" + finalUrl);


        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                if (result == null || result.length() == 0) {
                    return GBAccountError.NETWORK_FAILED;
                }

                GBLogUtils.DEBUG_DISPLAY(TAG, "logout RETURN:" + result);


                JSONTokener tokener = new JSONTokener(result);
                if (tokener == null) {
                    return GBAccountError.INVALID_INPUT;
                }
                try {
                    JSONObject roots = new JSONObject(tokener);
                    JSONObject message = roots.getJSONObject("message");
                    // {"message":{"status":"success","text":"成功退出。","code":0}}
                    String status = message.getString("status");
                    String text = message.getString("text");
                    String code = message.getString("code");
                    if (status.equals("success")) {
                        //发广播 通知删除本地账号信息 通知首页 顶部店铺名置空
                        Intent filter = new Intent();
                        filter.setAction("quitLogin");
                        context.sendBroadcast(filter);

                        ToastUtil.showToast(context, "切换域名成功，退出上次登录状态");
                    } else {
                        ToastUtil.showToast(context, text);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //登录过时 通知删除本地缓存数据
                    ToastUtil.showToast(context,"登陆过期，请重新登录！");
                    Intent filter = new Intent();
                    filter.setAction("quitLogin");
                    context.sendBroadcast(filter);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("logouterror");
                    error.putExtra("errorMsg", "退出失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });


        return GBAccountError.UNKNOWN;

    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @param sendPageType 发送页面：默认为1即注册页面 2.忘记登入密码 3.忘记支付密码 16.绑定手机
     * @return
     */
    public static int sendVerificationcode(final Context context, String mobile, String sendPageType) {
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
            return GBAccountError.INVALID_INPUT;
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
    /*    Log.e("sendVerificationcode","send_page:  "+sendPageType);
        Log.e("sendVerificationcode","keys:  "+keys);
        Log.e("sendVerificationcode","mobile:  "+mobile);
        Log.e("sendVerificationcode","finalurl:  "+finalUrl);*/
        //网络请求
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String returnStr) {
                if ((returnStr == null) || (returnStr.length() == 0)) {
                    return GBAccountError.NETWORK_FAILED;
                }
                //发送广播
                Intent intent = new Intent("sendsmssuccess");
                intent.putExtra("sendsms", returnStr);
                context.sendBroadcast(intent);
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {

            }
        });

        return GBAccountError.UNKNOWN;

    }


    /**
     * 获取用户信息
     *
     * @return 返回用户信息以及状态码（url 为 加密hash  + 客户端类型 +设备id）
     */

    public static int getGeneralInfo2(final Context context, final WinguoAccountGeneral info) {
        final int[] resultCode = {GBAccountError.UNKNOWN};

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);

        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            resultCode[0] = GBAccountError.INVALID_KEY;
        }

        if (id == null) {
            resultCode[0] = GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            resultCode[0] = GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETUSERINFO;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);
        //获取账户信息
        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "用户信息获取失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                    Log.i("dnw exception login", message);
                }
            }
        }, finalUrl);
        // 没有完善个人信息的 缺少部分字段 {"root":{"dinfo_zip":"","president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"capital":"","tel2":"","countItem":[0,0],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=d1fbcb5e814c970106c74ae4ee5a8203","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"否","fax":"","user_type":0,"dinfo_fax":"","lastlogin":"2017-01-07 13:24:40","url":"","rdatetime":"2017-01-06 16:11:11","shared_flag":0,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"","sex":0,"furikomi_account":"","dinfo_address":"","ico":{"modifyTime":""},"udatetime":"2017-01-06 16:11:11","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=d1fbcb5e814c970106c74ae4ee5a8203","tel_mobi":18300602618,"finfo_kouza_type":"","finfo_finance_code":"","id":43825,"unique_identifier":"d1fbcb5e814c970106c74ae4ee5a8203","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","finfo_kouza_meigi":"","countShop":[0,0],"finfo_kouza_number":"","mailmagazine_flag":0,"address":"","account":18300602618,"employees":"","name_kana":""}}
        //完善过个人信息 {"root":{"dinfo_zip":510000,"president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"adwords":"","capital":"","tel2":"","countItem":[1,1],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=c370cf9d1feb779f5c0c0c055bb254f3","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"是","fax":"","user_type":3,"dinfo_fax":"","lastlogin":"2017-01-07 13:48:53","temp_type":1,"url":"","rdatetime":"2016-11-18 16:44:00","shared_flag":1,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"陈生","sex":0,"furikomi_account":"","dinfo_address":"龙膜","ico":{"modifyTime":""},"udatetime":"2017-01-07 12:55:30","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=c370cf9d1feb779f5c0c0c055bb254f3","sname":"啊涛空间站","tel_mobi":18602029479,"finfo_kouza_type":"","finfo_finance_code":"","id":42426,"unique_identifier":"c370cf9d1feb779f5c0c0c055bb254f3","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","pc_shop_address":"http:\/\/atao.winguo.com","finfo_kouza_meigi":"","countShop":[0,0],"shop_address":"http:\/\/atao.m.winguo.com","finfo_kouza_number":"","mailmagazine_flag":0,"resume":"","address":"","account":"youketao1","QR_code":"http:\/\/api.winguo.com\/data\/qrcode?chl=http%3A%2F%2Fatao.m.winguo.com%2F","shop_url":"atao","employees":"","name_kana":""}}
        JSONTokener jsont = new JSONTokener(result);
        GBLogUtils.DEBUG_DISPLAY(TAG, "loginRETURN:" + result);
        if (jsont == null) {
            resultCode[0] = GBAccountError.INVALID_PEER_DATA;
        }

        try {
            JSONObject roots = new JSONObject(jsont);
            JSONObject root = roots.getJSONObject("root");
            String void_flag = root.getString("void_flag");
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
                double cashCoupon = root.getDouble("cashCoupon");
                String cashCredit = root.getString("cashCredit");
                info.cashCoupon =cashCoupon;
                info.cashCredit =cashCredit;
                int isCreater = root.getInt("isCreater");
                info.isCreater =isCreater;

                String udatetime = root.getString("udatetime");
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
                info.maker_id  = root.getString("maker_id");
                info.maker_shop_type = root.getString("maker_shop_type");
            }
            resultCode[0] = 0;
            //登陆成功后发出一条广播
            Intent intent = new Intent("landscape");
            intent.putExtra("info", info);
            context.sendBroadcast(intent);
            GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = info;
        } catch (JSONException e) {
            e.printStackTrace();
            //登录过时 通知删除本地缓存数据
            ToastUtil.showToast(context,"登陆过期，请重新登录！");
            Intent filter = new Intent();
            filter.setAction("quitLogin");
            context.sendBroadcast(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultCode[0];

    }

    /**
     * 获取用户信息
     * @param context
     * @param info
     * @return
     */
    public static int getGeneralInfo(final Context context, final WinguoAccountGeneral info) {

        final int[] resultCode = {GBAccountError.UNKNOWN};

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);

        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            resultCode[0] = GBAccountError.INVALID_KEY;
        }

        if (id == null) {
            resultCode[0] = GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            resultCode[0] = GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GETUSERINFO;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

        //获取账户信息
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                // 没有完善个人信息的 缺少部分字段 {"root":{"dinfo_zip":"","president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"capital":"","tel2":"","countItem":[0,0],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=d1fbcb5e814c970106c74ae4ee5a8203","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"否","fax":"","user_type":0,"dinfo_fax":"","lastlogin":"2017-01-07 13:24:40","url":"","rdatetime":"2017-01-06 16:11:11","shared_flag":0,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"","sex":0,"furikomi_account":"","dinfo_address":"","ico":{"modifyTime":""},"udatetime":"2017-01-06 16:11:11","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=d1fbcb5e814c970106c74ae4ee5a8203","tel_mobi":18300602618,"finfo_kouza_type":"","finfo_finance_code":"","id":43825,"unique_identifier":"d1fbcb5e814c970106c74ae4ee5a8203","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","finfo_kouza_meigi":"","countShop":[0,0],"finfo_kouza_number":"","mailmagazine_flag":0,"address":"","account":18300602618,"employees":"","name_kana":""}}
                //完善过个人信息 {"root":{"dinfo_zip":510000,"president":"","tel":"","address2":"","address3":"","makermail_notify":0,"type":0,"adwords":"","capital":"","tel2":"","countItem":[1,1],"company_name":"","register_url":"http:\/\/sell.winguo.com\/regist\/?k=c370cf9d1feb779f5c0c0c055bb254f3","mail_mobi":"","void_flag":0,"finfo_branch_name":"","shared":"是","fax":"","user_type":3,"dinfo_fax":"","lastlogin":"2017-01-07 13:48:53","temp_type":1,"url":"","rdatetime":"2016-11-18 16:44:00","shared_flag":1,"finfo_finance_name":"","dinfo_company_name":"","auth":0,"birthday":"0000-00-00","dinfo_received_name":"陈生","sex":0,"furikomi_account":"","dinfo_address":"龙膜","ico":{"modifyTime":""},"udatetime":"2017-01-07 12:55:30","open_url":"http:\/\/sell.winguo.com\/mobile\/userinfo?a=openshop&k=c370cf9d1feb779f5c0c0c055bb254f3","sname":"啊涛空间站","tel_mobi":18602029479,"finfo_kouza_type":"","finfo_finance_code":"","id":42426,"unique_identifier":"c370cf9d1feb779f5c0c0c055bb254f3","division":"","name":"","dinfo_tel":"","note":"","qq":"","logo":"","zip":"","mail":"","finfo_branch_code":"","pc_shop_address":"http:\/\/atao.winguo.com","finfo_kouza_meigi":"","countShop":[0,0],"shop_address":"http:\/\/atao.m.winguo.com","finfo_kouza_number":"","mailmagazine_flag":0,"resume":"","address":"","account":"youketao1","QR_code":"http:\/\/api.winguo.com\/data\/qrcode?chl=http%3A%2F%2Fatao.m.winguo.com%2F","shop_url":"atao","employees":"","name_kana":""}}
                JSONTokener jsont = new JSONTokener(result);
                GBLogUtils.DEBUG_DISPLAY(TAG, "loginRETURN:" + result);
                if (jsont == null) {
                    resultCode[0] = GBAccountError.INVALID_PEER_DATA;
                }
                            /*<message> <satus>error</status> <text>登录已过期，请重新登录。</text><code>-101</code> </message>*/
                try {

                    JSONObject roots = new JSONObject(jsont);
                    JSONObject root = roots.getJSONObject("root");
                    String void_flag = root.getString("void_flag");
                    if (void_flag.equals("0")) { //0 表示有效
                        String sname = root.getString("sname");
                        info.shopName = sname;
                        String shared_flag = root.getString("shared_flag");//是否是分销  1是（有店铺名 pc 店铺 店铺网址）其他 没有上述字段

                        if (shared_flag.equals("1")) {
                            String pc_shop_address = root.getString("pc_shop_address");
                            String shop_address = root.getString("shop_address");
                            String qr_code = root.getString("QR_code");
                            info.pcShopAddr = pc_shop_address;
                            info.mobileShopAddr = shop_address;
                            info.qr_code = qr_code;
                        }
                        String lastlogin = root.getString("lastlogin");
                        String rdatetime = root.getString("rdatetime");
                        String tel_mobi = root.getString("tel_mobi");
                        String id1 = root.getString("id");

                        String account = root.getString("account");
                        String password = root.getString("password");
                        String userName = root.getString("name");
                        String udatetime = root.getString("udatetime");
                        String maker_id = root.getString("maker_id");
                        String maker_shop_type = root.getString("maker_shop_type");
                        double cashCoupon = root.getDouble("cashCoupon");
                        String cashCredit = root.getString("cashCredit");
                        info.cashCoupon =cashCoupon;
                        info.cashCredit =cashCredit;
                        int isCreater = root.getInt("isCreater");
                        info.isCreater =isCreater;

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
                        info.maker_id = maker_id;
                        info.maker_shop_type = maker_shop_type;
                    }

                    //登陆成功后发出一条广播
                    Intent intent = new Intent("landscape");
                    intent.putExtra("info", info);
                    context.sendBroadcast(intent);
                    GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = info;
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
                    error.putExtra("errorMsg", "用户信息失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });


        return resultCode[0];

    }

    /**
     * 查询果币
     *
     * @param context
     * @param info
     * @return
     */

    public static int getCurrency2(final Context context, final WinguoAccountCurrency info) {
        final int[] resultCode = {GBAccountError.UNKNOWN};
        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            resultCode[0] = GBAccountError.INVALID_KEY;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            resultCode[0] = GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            resultCode[0] = GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_CURRENCY;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "currency REQ:" + finalUrl);
            //http://api.winguo.com/data/account?a=get_winguo&hash=eDbC4CTGg%2FmGiTtS1B9kdYqu58zvaNcf%2BltCl2gykRAt1CGIwOYcS7zJSoz1JtvluYzEfDLC7Q%2FiZWGsavBgruCgTWWtsKE7pzOk%2BsMSD6AYNcwk%2B%2FZmlsb1snJaCF1dNk%2BVmT3B24zkENuss8vna0FE44uivMCZTRsbbf372HthFXouncfW9tcGO0cMjG9%2FvnKNufiGKR9sWhta1k2UNIdX4ok0J%2BG7DJ9gtSwlO%2FmffBRBZu6lOe9Xrbudrov7fXrxCScnbkZgNrFZtF6RTtGIBeKe002L8USjsCoS4HSopohNSfd7mS2c7IcWcLwoZOFKtkKQZkjQz%2B%2BHynlAIA%3D%3D&clienttype=1&cudid=4909733da628e62844fd2fd58b48acc7


        // 网络连接
        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
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
        }, finalUrl);

        // 分析返回内容{"root":{"frozen":0,"allwinguo":0,"winguo":0}}
        JSONTokener jsont = new JSONTokener(result);
        if (jsont == null) {
            resultCode[0] = GBAccountError.INVALID_PEER_DATA;
        }

        try {

            JSONObject roots = new JSONObject(jsont);
            JSONObject root = roots.getJSONObject("root");
            String frozen = root.getString("frozen");
            String allwinguo = root.getString("allwinguo");
            String winguo = root.getString("winguo");
            info.all = Float.valueOf(allwinguo);
            info.frozen = Float.valueOf(frozen);
            info.avaliable = Float.valueOf(winguo);
            GBLogUtils.DEBUG_DISPLAY(context, "frozen" + frozen + ":" + winguo + ":" + winguo + "allwinguo" + allwinguo);

            resultCode[0] = 0;
            Intent intent = new Intent("guobi");
            intent.putExtra("allwinguo", info.all);
            context.sendBroadcast(intent);
            GBAccountMgr.getInstance().mAccountInfo.winguoCurrency = info;

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


        return resultCode[0];

    }

    public static final synchronized int getBankCardList(final Context context, final ArrayList<WinguoAccountBankCard> cards) {

        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);;
        if (!mKey.verify()) {
            return GBAccountError.INVALID_KEY;
        }
        // mKey.dump();


        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            return GBAccountError.HASH_FAILED;
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANKCARDLIST;
        final int dataSize = 4;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        final String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "bankcard list REQ:" + finalUrl);

        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "银行卡列表获取失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                    Log.i("dnw exception login", message);
                }
            }
        }, finalUrl);

        if (result == null || result.length() == 0) {
            return GBAccountError.NETWORK_FAILED;
        }
        GBLogUtils.DEBUG_DISPLAY(TAG, "backcard RETURN:" + result);
        //{"root":{"item":[{"is_default":1,"bank_name":"民生银行","tail_number":5100,"p_bid":1036,"account_name":"陈伟涛"},{"is_default":0,"bank_name":"中国农业银行","tail_number":3132,"p_bid":1034,"account_name":"ll"}]}}

        JSONTokener tokener = new JSONTokener(result);
        if (tokener == null) {
            return GBAccountError.INVALID_INPUT;
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

            return 0;
        } catch (JSONException e) {
            //异常解析 登录失效处理 期初数据
            ToastUtil.showToast(context,"登陆过期，请重新登录！");
            Intent filter = new Intent();
            filter.setAction("quitLogin");
            context.sendBroadcast(filter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return GBAccountError.UNKNOWN;






           /* // 分析返回内容
            Element elt = null;
            NodeList nl = XMLUtils.getNodeList(returnStr, "item");
            // 尝试获取正常返回值
            if (nl != null) {
                if (cards != null) {
                    synchronized (cards) {
                        cards.clear();
                        final int total = nl.getLength();
                        String val;
                        for (int i = 0; i < total; i++) {
                            WinguoAccountBankCard card = new WinguoAccountBankCard();
                            elt = (Element) nl.item(i);
                            val = XMLUtils.getSubTagVal(elt, "p_bid");
                            if (val != null) {
                                card.p_bid = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "bank_name");
                            if (val != null) {
                                card.bankName = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "branches_id");
                            if (val != null) {
                                card.bankBranchesID = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "branches_name");
                            if (val != null) {
                                card.bankBranchesName = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "provinceid");
                            if (val != null) {
                                card.provinceID = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "province_name");
                            if (val != null) {
                                card.provinceName = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "cityid");
                            if (val != null) {
                                card.cityID = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "city_name");
                            if (val != null) {
                                card.cityName = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "tail_number");
                            if (val != null) {
                                card.cardNumberTail = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "is_default");
                            if (val != null) {
                                card.isDefault = Integer.valueOf(val) > 0 ? true : false;
                            }
                            val = XMLUtils.getSubTagVal(elt, "account_name");
                            if (val != null) {
                                card.accountName = val;
                            }
                            cards.add(card);
                        }
                    }
                }
                return 0;
            } else { // 失败返回值
                elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
                if (elt == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
                if (code != 0) {
                    setErrMsg(context, "bankcardlist", code);
                }

                return code;
            }*/


    }

    /**
     * 用户银行卡 详情
     *
     * @param context
     * @param p_bid
     * @param info
     * @return
     */
    public static final synchronized int getBankCardDetail(final Context context, String p_bid, WinguoAccountBankCard info) {

        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            return GBAccountError.INVALID_KEY;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            return GBAccountError.HASH_FAILED;
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


        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "银行卡详情获取失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                    Log.i("dnw exception login", message);
                }
            }
        }, finalUrl);

        if (result == null || result.length() == 0) {
            return GBAccountError.NETWORK_FAILED;
        }

        GBLogUtils.DEBUG_DISPLAY(TAG, "backcarddetail RETURN:" + result);
            // {"root":{"branches_id":53400,"city_name":"广州市","bank_name":"民生银行","province_name":"广东","branches_name":"中国广州体育西支行","number":6216910304325100,"provinceid":19,"bid":10,"p_bid":1036,"account_name":"陈伟涛","cityid":1601}}


        try {

            JSONObject roots = new JSONObject(result);
            String jsonStr = roots.toString();

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
                return 0;

            } else {
                JSONObject message = roots.getJSONObject(NODE_TAG_MSG);
                if (message == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = message.getInt("code");
                if (code != 0) {
                    setErrMsg(context, "bankcarddetail", code);
                }
                return code;


            }

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



           /* // 分析返回内容
            Element elt = null;
            elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_ROOT);
            // 尝试获取正常返回值
            if (elt != null) {
                if (info != null) {
                    synchronized (info) {
                        String val = null;
                        val = XMLUtils.getSubTagVal(elt, "p_bid");
                        if (val != null) {
                            info.p_bid = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "bid");
                        if (val != null) {
                            info.bid = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "bank_name");
                        if (val != null) {
                            info.bankName = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "branches_id");
                        if (val != null) {
                            info.bankBranchesID = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "branches_name");
                        if (val != null) {
                            info.bankBranchesName = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "provinceid");
                        if (val != null) {
                            info.provinceID = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "province_name");
                        if (val != null) {
                            info.provinceName = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "cityid");
                        if (val != null) {
                            info.cityID = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "city_name");
                        if (val != null) {
                            info.cityName = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "tail_number");
                        if (val != null) {
                            info.cardNumberTail = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "number");
                        if (val != null) {
                            info.cardNumberFull = val;
                        }
                        val = XMLUtils.getSubTagVal(elt, "is_default");
                        if (val != null) {
                            info.isDefault = Integer.valueOf(val) > 0 ? true : false;
                        }
                        val = XMLUtils.getSubTagVal(elt, "account_name");
                        if (val != null) {
                            info.accountName = val;
                        }
                    }
                }
                return 0;
            } else { // 失败返回值
                elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
                if (elt == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
                if (code != 0) {
                    setErrMsg(context, "bankcarddetail", code);
                }

                return code;
            }*/

        return GBAccountError.UNKNOWN;


    }

    public static final synchronized int deleteBankCard(final Context context, String p_bid) {

        try {
            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            if (id == null) {
                return GBAccountError.USER_NAME_NOT_FOUND;
            }

            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            strBuf.append("&p_bid=").append(p_bid);
            String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hashCommon == null) {
                return GBAccountError.HASH_FAILED;
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

            String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        Intent error = new Intent("loginerror");
                        error.putExtra("errorMsg", "删除银行卡失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                        Log.i("dnw exception login", message);
                    }
                }
            }, finalUrl);

            try {
                JSONObject root = new JSONObject(result);
                JSONObject message = root.getJSONObject(NODE_TAG_MSG);
                if (message == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }
                final int code = message.getInt("code");
                if (code != 0) {
                    setErrMsg(context, "delbankcard", code);
                }
                return code;
            } catch (JSONException e) {
                //异常解析 登录失效处理 期初数据
               /* Intent filter = new Intent();
                filter.setAction("quitLogin");
                context.sendBroadcast(filter);*/
                e.printStackTrace();


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
          /*  // 分析返回内容
            final Element elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
            if (elt == null) {
                return GBAccountError.INVALID_PEER_DATA;
            }

            final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
            if (code != 0) {
                setErrMsg(context, "delbankcard", code);
            }

            return code;*/


        return GBAccountError.UNKNOWN;

    }

    public static final synchronized int addBankCard(Context context, WinguoAccountBankCard info) {
        return setBankCardDetail(context, null, info);
    }

    public static final synchronized int setBankCardDetail(final Context context, String p_bid, WinguoAccountBankCard info) {

        try {

            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }
            // mKey.dump();

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            if (id == null) {
                return GBAccountError.USER_NAME_NOT_FOUND;
            }

            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            if (p_bid != null && p_bid.length() > 0) {
                strBuf.append("&p_bid=").append(p_bid);
            }
            strBuf.append("&bid=").append(info.bid);
            strBuf.append("&branches_id=").append(info.bankBranchesID);
            strBuf.append("&number=").append(info.cardNumberFull);
            // strBuf.append("&bank_name=").append(URLEncoder.encode(info.bankName,"UTF-8"));
            strBuf.append("&account_name=").append(URLEncoder.encode(info.accountName, "UTF-8"));
            strBuf.append("&is_default=").append(info.isDefault ? 1 : 0);
            String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hashCommon == null) {
                return GBAccountError.HASH_FAILED;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.ADD_BANKCARD;
            final int dataSize = 4;
            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
            valueList.add(new NameValuePair("hash", hashCommon));// HASH
            valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
            valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
            final String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "bankcard set REQ:" + finalUrl);

            String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        Intent error = new Intent("loginerror");
                        error.putExtra("errorMsg", "添加银行卡失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                        Log.i("dnw exception login", message);
                    }
                }
            }, finalUrl);

            final int code;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject message = root.getJSONObject(NODE_TAG_MSG);
                if (message == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }
                code = message.getInt("code");
                if (code != 0) {
                    setErrMsg(context, "setbankcard", code);
                }
                return code;
            } catch (JSONException e) {
                //异常解析 登录失效处理 期初数据
                Intent filter = new Intent();
                filter.setAction("quitLogin");
                context.sendBroadcast(filter);
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*// 分析返回内容
            final Element elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
            if (elt == null) {
                return GBAccountError.INVALID_PEER_DATA;
            }

            final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
            if (code != 0) {
                setErrMsg(context, "setbankcard", code);
            }

            return code;*/

        } catch (Exception e) {
            e.printStackTrace();

        }

        return GBAccountError.UNKNOWN;

    }

    /**
     * 得到银行列表
     *
     * @param context
     * @param pay_method
     * @param pay_type
     * @param banks
     * @return
     */
    public static final synchronized int getBankList(final Context context, final String pay_method, final String pay_type,
                                                     final ArrayList<WinguoAccountBank> banks) {
        // 获取KEY
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        if (!mKey.verify()) {
            return GBAccountError.INVALID_KEY;
        }
        // mKey.dump();

        // 获取用户名
        final String id = WinguoAccountDataMgr.getUserName(context);
        if (id == null) {
            return GBAccountError.USER_NAME_NOT_FOUND;
        }

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hashCommon == null) {
            return GBAccountError.HASH_FAILED;
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


        String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("loginerror");
                    error.putExtra("errorMsg", "银行列表获取失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);

                }
            }
        }, finalUrl);

        if (result == null || result.length() == 0) {
            return GBAccountError.NETWORK_FAILED;
        }

        GBLogUtils.DEBUG_DISPLAY(TAG, "banklist RETURN:" + result);


        try {
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
            GBAccountMgr.getInstance().mAccountInfo.banks.clear();
            GBAccountMgr.getInstance().mAccountInfo.banks.addAll(banks);
            return 0;
        } catch (JSONException e) {
            //异常解析 登录失效处理 期初数据
            /*Intent filter = new Intent();
            filter.setAction("quitLogin");
            context.sendBroadcast(filter);*/
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return GBAccountError.UNKNOWN;


    }

    /**
     * 新版注册 （用手机号码，验证码注册）
     */
    @Deprecated
    public static int RegisterNew(final Context context, String mobile, String authcode, String Password) {

        // 判断参数是否合法
        if ((mobile == null) || (mobile.length() <= 0) || (mobile == null) || (mobile.length() <= 0) || (authcode == null)
                || (authcode.length() <= 0) || (Password == null) || (Password.length() <= 0)) {
            return GBAccountError.INVALID_INPUT;
        }

        // 获取公钥
        final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("authcode=").append(authcode);
        strBuf.append("&pa=").append(Password);
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            return GBAccountError.HASH_FAILED;
        }
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.REGISTER_NEW;
        final int dataSize = 6;
        ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// HASH
        valueList.add(new NameValuePair("sourcetype ", "1"));
        valueList.add(new NameValuePair("mobile", mobile));
        valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                if ((result == null) || (result.length() == 0)) {
                    return GBAccountError.NETWORK_FAILED;
                }
                Intent intent = new Intent("register_success");
                intent.putExtra("register_json", result);
                context.sendBroadcast(intent);
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("RegisterNewerror");
                    error.putExtra("errorMsg", "注册失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                    Log.i("dnw exception login", message);
                }
            }
        });
        return 0;
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
    public static int resetPwd(final Context context, String id, String mobile, String authcode,
                               String newPwd,String pwType) {

        try {

            // 判断参数是否合法
            if ((id == null) || (id.length() <= 0) || (mobile == null) || (mobile.length() <= 0) || (authcode == null)
                    || (authcode.length() <= 0) || (newPwd == null) || (newPwd.length() <= 0)) {
                return GBAccountError.INVALID_INPUT;
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
                return GBAccountError.HASH_FAILED;
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
                        return GBAccountError.NETWORK_FAILED;
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
                        Intent error = new Intent("O");
                        error.putExtra("errorMsg", "重置密码失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        return GBAccountError.UNKNOWN;

    }

    /**
     * 获取省份列表
     */
    public static final synchronized int getProvinceList(final Context context, ArrayList<WinguoAccountBranchesEntity> provinces) {

        try {

            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }
            // mKey.dump();

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            if (id == null) {
                return GBAccountError.USER_NAME_NOT_FOUND;
            }

            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hashCommon == null) {
                return GBAccountError.HASH_FAILED;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANK_PROVINCE_LIST;
            final int dataSize = 4;
            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
            valueList.add(new NameValuePair("hash", hashCommon));// HASH
            // valueList.add(new NameValuePair("pay_method", pay_method));//
            // valueList.add(new NameValuePair("payType", pay_type));//
            valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
            valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
            final String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "province REQ:" + finalUrl);


            String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        Intent error = new Intent("loginerror");
                        error.putExtra("errorMsg", "省份列表获取失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                }
            }, finalUrl);

            if (result == null || result.length() == 0) {
                return GBAccountError.NETWORK_FAILED;
            }


            GBLogUtils.DEBUG_DISPLAY(TAG, "province RETURN:" + result);

            JSONObject roots = new JSONObject(result);
            if (roots.toString().contains("province")) {

                if (provinces != null) {
                    synchronized (provinces) {
                        provinces.clear();
                        JSONObject province1 = roots.getJSONObject("province");
                        JSONArray item = province1.getJSONArray("item");
                        final int total = item.length();
                        for (int i = 0; i < total; i++) {
                            WinguoAccountBranchesEntity province = new WinguoAccountBranchesEntity();
                            JSONObject pro_item = (JSONObject) item.get(i);
                            String code = pro_item.getString("code");
                            String name = pro_item.getString("name");

                            if (code != null) {
                                province.id = code;
                            }
                            if (name != null) {
                                province.name = name;
                            }
                            provinces.add(province);
                        }
                    }
                }
                return 0;
            } else {
                JSONObject message = roots.getJSONObject(NODE_TAG_MSG);
                if (message == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = message.getInt("code");
                if (code != 0) {
                    setErrMsg(context, "getprovince", code);
                }

                return code;
            }





          /*  // 分析返回内容
            Element elt = null;
            NodeList nl = XMLUtils.getNodeList(returnStr, "item");
            // 尝试获取正常返回值
            if (nl != null) {
                if (provinces != null) {
                    synchronized (provinces) {
                        provinces.clear();
                        final int total = nl.getLength();
                        String val;
                        for (int i = 0; i < total; i++) {
                            WinguoAccountBranchesEntity province = new WinguoAccountBranchesEntity();
                            elt = (Element) nl.item(i);
                            val = XMLUtils.getSubTagVal(elt, "code");
                            if (val != null) {
                                province.id = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "name");
                            if (val != null) {
                                province.name = val;
                            }
                            provinces.add(province);
                        }
                    }
                }
                return 0;
            } else { // 失败返回值
                elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
                if (elt == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
                if (code != 0) {
                    setErrMsg(context, "getprovince", code);
                }

                return code;
            }
*/
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

        return GBAccountError.UNKNOWN;
    }

    /**
     * 获取城市列表
     *
     * @param provinces_id 省份id
     */
    public static final synchronized int getCityList(final Context context, String provinces_id,
                                                     ArrayList<WinguoAccountBranchesEntity> cities) {
        try {

            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }
            // mKey.dump();

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            if (id == null) {
                return GBAccountError.USER_NAME_NOT_FOUND;
            }

            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hashCommon == null) {
                return GBAccountError.HASH_FAILED;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANK_CITY_LIST + "&pid="
                    + provinces_id;
            final int dataSize = 4;
            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
            valueList.add(new NameValuePair("hash", hashCommon));// HASH
            // valueList.add(new NameValuePair("pay_method", pay_method));//
            // valueList.add(new NameValuePair("payType", pay_type));//
            valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
            valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
            final String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "citylist REQ:" + finalUrl);
            String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        Intent error = new Intent("loginerror");
                        error.putExtra("errorMsg", "城市列表获取失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                        Log.i("dnw exception login", message);
                    }
                }
            }, finalUrl);

            if (result == null || result.length() == 0) {
                return GBAccountError.NETWORK_FAILED;
            }

            GBLogUtils.DEBUG_DISPLAY(TAG, "citylist RETURN:" + result);


            JSONObject roots = new JSONObject(result);
            if (roots.toString().contains("city")) {

                if (cities != null) {
                    synchronized (cities) {
                        cities.clear();
                        JSONObject province1 = roots.getJSONObject("city");
                        //{"city":{"item":{"code":2754,"name":"香港特别行政区"}}}
                        String itemStr = province1.getString("item");
                        if (!TextUtils.isEmpty(itemStr)) {

                            if (itemStr.contains("[")) {
                                JSONArray item = province1.getJSONArray("item");
                                final int total = item.length();
                                for (int i = 0; i < total; i++) {
                                    WinguoAccountBranchesEntity province = new WinguoAccountBranchesEntity();
                                    JSONObject pro_item = (JSONObject) item.get(i);
                                    String code = pro_item.getString("code");
                                    String name = pro_item.getString("name");

                                    if (code != null) {
                                        province.id = code;
                                    }
                                    if (name != null) {
                                        province.name = name;
                                    }
                                    cities.add(province);
                                }

                            } else {
                                JSONObject item = province1.getJSONObject("item");
                                String code = item.getString("code");
                                String name = item.getString("name");
                                WinguoAccountBranchesEntity province = new WinguoAccountBranchesEntity();
                                if (code != null) {
                                    province.id = code;
                                }
                                if (name != null) {
                                    province.name = name;
                                }
                                cities.add(province);
                            }

                        }

                    }
                }
                return 0;
            } else {
                JSONObject message = roots.getJSONObject(NODE_TAG_MSG);
                if (message == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = message.getInt("code");
                if (code != 0) {
                    setErrMsg(context, "getcity", code);
                }

                return code;
            }

/*
            // 分析返回内容
            Element elt = null;
            NodeList nl = XMLUtils.getNodeList(returnStr, "item");
            // 尝试获取正常返回值
            if (nl != null) {
                if (cities != null) {
                    synchronized (cities) {
                        cities.clear();
                        final int total = nl.getLength();
                        String val;
                        for (int i = 0; i < total; i++) {
                            WinguoAccountBranchesEntity city = new WinguoAccountBranchesEntity();
                            elt = (Element) nl.item(i);
                            val = XMLUtils.getSubTagVal(elt, "code");
                            if (val != null) {
                                city.id = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "name");
                            if (val != null) {
                                city.name = val;
                            }
                            cities.add(city);
                        }
                    }
                }
                return 0;
            } else { // 失败返回值
                elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
                if (elt == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
                if (code != 0) {
                    setErrMsg(context, "getcity", code);
                }

                return code;
            }*/

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

        return GBAccountError.UNKNOWN;
    }

    /**
     * 获取支行列表
     *
     * @param bank_id 银行id
     * @param city_id 城市id
     */
    public static final synchronized int getBankBrancheList(final Context context, String bank_id, String city_id,
                                                            ArrayList<WinguoAccountBranchesEntity> bankBranches) {
        try {
            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }
            // mKey.dump();

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            if (id == null) {
                return GBAccountError.USER_NAME_NOT_FOUND;
            }

            // 创建Hash字符串
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(id);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            String hashCommon = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hashCommon == null) {
                return GBAccountError.HASH_FAILED;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_BANK_BRANCHE_LIST + "&bank_id="
                    + bank_id + "&city_code=" + city_id;
            final int dataSize = 4;
            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>(dataSize);
            valueList.add(new NameValuePair("hash", hashCommon));// HASH

            valueList.add(new NameValuePair("clienttype", "1"));// 客户端类型：桌面
            valueList.add(new NameValuePair("cudid", StatisticsAgentAdvanced.getDeviceId(context)));// 设备ID
            final String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "BankBrancheList REQ:" + finalUrl);


            String result = MyOkHttpUtils.getJsonStrFormUrlByPost(new IStringCallBack() {
                @Override
                public int stringReturn(String result) {
                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        Intent error = new Intent("loginerror");
                        error.putExtra("errorMsg", "银行城市列表获取失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                }
            }, finalUrl);

            if (result == null || result.length() == 0) {
                return GBAccountError.NETWORK_FAILED;
            }

            GBLogUtils.DEBUG_DISPLAY(TAG, "BankBrancheList RETURN:" + result);


            JSONObject roots = new JSONObject(result);
            //BankBrancheList RETURN:{"message":{"items":""}}
            if (roots.toString().contains("items")) {
                if (bankBranches != null) {
                    synchronized (bankBranches) {
                        bankBranches.clear();
                        JSONObject message = roots.getJSONObject("message");
                        String itemStr = message.getString("items");
                        if (!TextUtils.isEmpty(itemStr)) {
                            JSONObject item = message.getJSONObject("items");
                            JSONArray data = item.getJSONArray("data");

                            final int total = data.length();
                            for (int i = 0; i < total; i++) {
                                WinguoAccountBranchesEntity bankBranche = new WinguoAccountBranchesEntity();
                                JSONObject bank_item = (JSONObject) data.get(i);
                                String brancheId = bank_item.getString("brancheId");
                                if (brancheId != null) {
                                    bankBranche.id = brancheId;
                                }
                                String name = bank_item.getString("name");
                                if (name != null) {
                                    bankBranche.name = name;
                                }
                                bankBranches.add(bankBranche);
                            }
                        }

                    }
                }
                return 0;

            } else { // 失败返回值
                JSONObject message = roots.getJSONObject(NODE_TAG_MSG);
                if (message == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = message.getInt("code");
                if (code != 0) {
                    setErrMsg(context, "getbranches", code);
                }

                return code;
            }

           /* // 分析返回内容
            Element elt = null;
            NodeList nl = XMLUtils.getNodeList(returnStr, "data");
            // 尝试获取正常返回值
            if (nl != null) {
                if (bankBranches != null) {
                    synchronized (bankBranches) {
                        bankBranches.clear();
                        final int total = nl.getLength();
                        String val;
                        for (int i = 0; i < total; i++) {
                            WinguoAccountBranchesEntity bankBranche = new WinguoAccountBranchesEntity();
                            elt = (RangeValueIterator.Element) nl.item(i);
                            val = XMLUtils.getSubTagVal(elt, "brancheId");
                            if (val != null) {
                                bankBranche.id = val;
                            }
                            val = XMLUtils.getSubTagVal(elt, "name");
                            if (val != null) {
                                bankBranche.name = val;
                            }
                            bankBranches.add(bankBranche);
                        }
                    }
                }
                return 0;
            } else { // 失败返回值
                elt = XMLUtils.getNodeElement(returnStr, NODE_TAG_MSG);
                if (elt == null) {
                    return GBAccountError.INVALID_PEER_DATA;
                }

                final int code = Integer.valueOf(XMLUtils.getSubTagVal(elt, "code"));
                if (code != 0) {
                    setErrMsg(context, "getbranches", code);
                }

                return code;
            }*/

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

        return GBAccountError.UNKNOWN;
    }

    /**
     *
     * @param context
     * @param content
     * @return
     */
    public static int submitFeedback(final Context context, final String content) {

        try {
            final int[] code = new int[1];
            // 获取KEY
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }
            // mKey.dump();

            // 获取用户名
            final String id = WinguoAccountDataMgr.getUserName(context);
            final String token = WinguoAccountDataMgr.getTOKEN(context);
            final String uuid = WinguoAccountDataMgr.getUUID(context);
            if (id == null) {
                return GBAccountError.USER_NAME_NOT_FOUND;
            }
            // 创建Hash字符串
            final String hash;

            if (!mKey.verify()) {
                return GBAccountError.INVALID_KEY;
            }

            if ((id == null) || (id.length() <= 0)) {
                return GBAccountError.INVALID_INPUT;
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
                return GBAccountError.HASH_FAILED;
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

                        if (status.equals("error")) {
                            setErrMsg(context, "feedback", code[0]);
                            Intent error = new Intent("feedbackError");
                            error.putExtra("errorMsg", text);
                            context.sendBroadcast(error);
                        }
                        Intent intent = new Intent("feedback");
                        intent.putExtra("message", text);
                        intent.putExtra("status", status);
                        context.sendBroadcast(intent);

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
                        Intent error = new Intent("submitFeedbackerror");
                        error.putExtra("errorMsg", "提交失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return GBAccountError.UNKNOWN;
    }

    /**
     * 开通空间站
     */
    public static void openSpaceStation(final Context context, String accountName, String spaceName) {


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
                    Intent openSpaceMess = new Intent("openSpace");
                    openSpaceMess.putExtra("status", status);
                    openSpaceMess.putExtra("text", text);
                    context.sendBroadcast(openSpaceMess);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }


            @Override
            public void exceptionMessage(String message) {

                if (!TextUtils.isEmpty(message)) {
                    Intent error = new Intent("openSpaceStationerror");
                    error.putExtra("errorMsg", "开通空间站失败，服务器繁忙！稍后请尝试...");
                    context.sendBroadcast(error);
                }
            }
        });
    }
   /**
    * 绑定手机号
    * */
    public static int bindingPhoneNumber(final Context context, String number,  String authcode) {

        try {

            // 判断参数是否合法
            if ((number == null) || (number.length() <= 0) || (authcode == null)
                    || (authcode.length() <= 0) ) {
                return GBAccountError.INVALID_INPUT;
            }

            // 获取公钥
            final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
            // 创建Hash字符串
            String username=  WinguoAccountDataMgr.getUserName(context);
           // String username = GBAccountMgr.getInstance().getAccountInfo().winguoGeneral.accountName;
            Log.i("chenqianchenqian","username::"+username);
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("id=").append(username);
            strBuf.append("&token=").append(mKey.getToken());
            strBuf.append("&uuid=").append(mKey.getUUID());
            final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
            if (hash == null) {
                return GBAccountError.HASH_FAILED;
            }

            // 生成URL
            final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.BINDING_PHONE_NUMBER;
            final int dataSize = 6;
            ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
            valueList.add(new NameValuePair("hash", hash));// HASH
            valueList.add(new NameValuePair("mobile",number));
            valueList.add(new NameValuePair("authcode",authcode));
            String finalUrl = URLCreator.create(url, valueList);

            GBLogUtils.DEBUG_DISPLAY(TAG, "REQ:" + finalUrl);
            Log.i("chenqianchenqian","finalUrl::"+finalUrl);
            //网络请求
            MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
                @Override
                public int stringReturn(String returnStr) {
                    if ((returnStr == null) || (returnStr.length() == 0)) {
                        return GBAccountError.NETWORK_FAILED;
                    }
                    Intent intent = new Intent();
                    intent.setAction("bindMobileSuccess");
                    intent.putExtra("bindMobilejson",returnStr);
                    context.sendBroadcast(intent);
                    return 0;
                }

                @Override
                public void exceptionMessage(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        Intent error = new Intent("O");
                        error.putExtra("errorMsg", "绑定手机失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return GBAccountError.UNKNOWN;

    }


}
