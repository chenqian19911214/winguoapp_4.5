package com.winguo.login.register.control;

import android.content.Context;
import android.content.Intent;

import com.guobi.account.MD5;
import com.guobi.account.NameValuePair;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.login.register.bean.NewRegisterBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.WinguoAcccountManagerUtils;

import java.util.ArrayList;


/**
 * @author hcpai
 * @desc 注册处理类
 */

public class RegisterHandle {

    /**
     * 快速填写注册
     * @param context
     * @param mobile
     * @param pwd
     * @param code
     * @param referee
     * @param mKey
     */
    public static void register(final Context context, final String mobile, final String pwd, String code, final String referee, final WinguoAccountKey mKey) {
        // 获取公钥
       // final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        // 创建Hash字符串
        StringBuilder strBuf = new StringBuilder();
        StringBuilder saveHash = new StringBuilder();
        strBuf.append("authcode=").append(code);
        strBuf.append("&pa=").append(pwd);
        saveHash.append("pa=").append(pwd);
        strBuf.append("&id=").append(mobile);
        saveHash.append("&id=").append(mobile);
        strBuf.append("&token=").append(mKey.getToken());
        saveHash.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        saveHash.append("&uuid=").append(mKey.getUUID());
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        final String saveHash1 = WinguoEncryption.commonEncryption(saveHash.toString(), mKey);
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.NEWREGISTER;
        ArrayList<NameValuePair> valueList = new ArrayList<>();
        valueList.add(new NameValuePair("hash", hash));
        valueList.add(new NameValuePair("clienttype", "1"));
        valueList.add(new NameValuePair("mobile", mobile));
        valueList.add(new NameValuePair("referee", referee));
        valueList.add(new NameValuePair("url", ""));
        final String finalUrl = URLCreator.create(url, valueList);
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(finalUrl, -1, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("hcp", "stringReturn---->result=" + result);
                        //发送广播
                        NewRegisterBean newRegisterBean = GsonUtil.json2Obj(result, NewRegisterBean.class);
                        //注册成功
                        if (newRegisterBean.getCode() == 0) {
                            String hashCommon = "id=" + mobile + "&token=" + mKey.getToken() + "&uuid=" + mKey.getUUID();
                            hashCommon = WinguoEncryption.commonEncryption(hashCommon, mKey);
                            WinguoAccountDataMgr.setUserName(context, mobile);
                            WinguoAccountDataMgr.setHashLogin(context, saveHash1);
                            WinguoAccountDataMgr.setMD5(context, MD5.md5Appkey(pwd));
                            WinguoAccountDataMgr.setKEY(context, mKey.getKey());
                            WinguoAccountDataMgr.setTOKEN(context, mKey.getToken());
                            WinguoAccountDataMgr.setUUID(context, mKey.getUUID());
                            WinguoAccountDataMgr.setHashCommon(context, hashCommon);
                            WinguoAccountDataMgr.setPizza(context, pwd);

                            //WinguoAccountImpl.onLoginSuc(context);
                            WinguoAcccountManagerUtils.loginSuccess(context,null);

                            Intent intent = new Intent("register_success2");
                            context.sendBroadcast(intent);
                        } else {
                            //注册失败
                            Intent intent = new Intent("register_fail2");
                            intent.putExtra("register_fail2", newRegisterBean.getText());
                            context.sendBroadcast(intent);
                        }
                    }

                    @Override
                    public void failReturn() {
                        //连接超时
                        Intent error = new Intent("registerNewerror2");
                        error.putExtra("registerNewerror2", "注册失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                });
            }
        });
    }

    /**
     * 扫码注册
     * @param context
     * @param mobile
     * @param code
     * @param QRurl
     * @param mKey
     */
    public static void scanRegister(final Context context, final String mobile, final String code, String QRurl,final WinguoAccountKey mKey) {
        // 获取公钥
       // final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        // 创建Hash字符串
        StringBuilder strBuf = new StringBuilder();
        StringBuilder saveHash = new StringBuilder();
        strBuf.append("authcode=").append(code);
        strBuf.append("&pa=").append("winguo").append(code);
        saveHash.append("pa=").append("winguo").append(code);
        strBuf.append("&id=").append(mobile);
        saveHash.append("&id=").append(mobile);
        strBuf.append("&token=").append(mKey.getToken());
        saveHash.append("&token=").append(mKey.getToken());
        strBuf.append("&uuid=").append(mKey.getUUID());
        saveHash.append("&uuid=").append(mKey.getUUID());
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.NEWREGISTER;
        ArrayList<NameValuePair> valueList = new ArrayList<>();
        valueList.add(new NameValuePair("hash", hash));
        valueList.add(new NameValuePair("clienttype", "1"));
        valueList.add(new NameValuePair("mobile", mobile));
        valueList.add(new NameValuePair("referee", ""));
        valueList.add(new NameValuePair("url", QRurl));
        final String finalUrl = URLCreator.create(url, valueList);
        final String saveHash1 = WinguoEncryption.commonEncryption(saveHash.toString(), mKey);

        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(finalUrl, -1, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("hcp", "stringReturn---->result=" + result);
                        //发送广播
                        NewRegisterBean newRegisterBean = GsonUtil.json2Obj(result, NewRegisterBean.class);
                        //注册成功
                        if (newRegisterBean.getCode() == 0) {
                            String hashCommon = "id=" + mobile + "&token=" + mKey.getToken() + "&uuid=" + mKey.getUUID();
                            hashCommon = WinguoEncryption.commonEncryption(hashCommon, mKey);
                            WinguoAccountDataMgr.setUserName(context, mobile);
                            WinguoAccountDataMgr.setHashLogin(context, saveHash1);
                            WinguoAccountDataMgr.setMD5(context, MD5.md5Appkey("winguo" + code));
                            WinguoAccountDataMgr.setKEY(context, mKey.getKey());
                            WinguoAccountDataMgr.setTOKEN(context, mKey.getToken());
                            WinguoAccountDataMgr.setUUID(context, mKey.getUUID());
                            WinguoAccountDataMgr.setHashCommon(context, hashCommon);
                            WinguoAccountDataMgr.setPizza(context, "winguo" + code);

                            //WinguoAccountImpl.onLoginSuc(context);
                            WinguoAcccountManagerUtils.loginSuccess(context,null);
                            Intent intent = new Intent("register_success3");
                            context.sendBroadcast(intent);
                        } else {
                            //注册失败
                            Intent intent = new Intent("register_fail3");
                            intent.putExtra("register_fail3", newRegisterBean.getText());
                            context.sendBroadcast(intent);
                        }
                    }

                    @Override
                    public void failReturn() {
                        //连接超时
                        Intent error = new Intent("registerNewerror3");
                        error.putExtra("registerNewerror3", "注册失败，服务器繁忙！稍后请尝试...");
                        context.sendBroadcast(error);
                    }
                });
            }
        });
    }
}