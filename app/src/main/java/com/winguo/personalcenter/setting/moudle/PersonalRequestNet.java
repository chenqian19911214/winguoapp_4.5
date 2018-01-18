package com.winguo.personalcenter.setting.moudle;

import android.content.Context;

import com.guobi.account.GBAccountError;
import com.guobi.account.NameValuePair;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.utils.ImageUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by admin on 2017/10/16.
 * 请求网路
 */

public class PersonalRequestNet {
    private static final String TAG = PersonalRequestNet.class.getSimpleName();
    public PersonalRequestNet() {
    }

    public void requestModifyHead(Context context, File headFile, final RequestModifyHeadCallBack personalRequestCallBack){
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
            personalRequestCallBack.requestModifyHeadErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.MODIFY_HEAD;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        final String finalUrl = URLCreator.createNoAnd(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "requestModifyHead final REQ:" + finalUrl);
        GBLogUtils.DEBUG_DISPLAY(TAG, "headfile :" + headFile.getName());
        MyOkHttpUtils.upLoadOneFile(finalUrl, 1, null, headFile, "file", ImageUtil.CROP_CACHE_FILE_NAME, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                personalRequestCallBack.requestModifyHeadErrorCode(GBAccountError.REQUST_TIMEOUT);
            }

            @Override
            public void onResponse(String response, int id) {
                personalRequestCallBack.requestModifyHeadResult(response);
            }
        });


    }

    /**
     * 修改昵称
     * @param context
     * @param newNick
     * @param personalRequestCallBack
     */
    public void requestModifyNickName(Context context, String newNick, final RequestModifyNickCallback personalRequestCallBack){
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
            personalRequestCallBack.requestModifyNickErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.MODIFY_NICKNAME;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("nickname", newNick));// HASH
        final String finalUrl = URLCreator.create(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "requestModifyNickName final REQ:" + finalUrl);
        MyOkHttpUtils.post(finalUrl, 2, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "requestModifyNickName result:" + result);
                personalRequestCallBack.requestModifyNickResult(result);
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                personalRequestCallBack.requestModifyNickErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

    /**
     * 绑定手机号
     * @param context
     * @param tel
     * @param bindTelCallback
     */
    public void requestBindingRefereeTel(Context context, String tel, final RequestBindTelCallback bindTelCallback){
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
            bindTelCallback.requestBindTelErrorCode(GBAccountError.HASH_FAILED);
            return;
        }

        // 生成URL
        String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.MODIFY_NICKNAME;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("hash", hashCommon));// HASH
        valueList.add(new NameValuePair("mobile", tel));// HASH
        final String finalUrl = URLCreator.create(url, valueList);
        GBLogUtils.DEBUG_DISPLAY(TAG, "requestBindingRefereeTel final REQ:" + finalUrl);
        MyOkHttpUtils.post(finalUrl, 2, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "requestBindingRefereeTel result:" + result);
                bindTelCallback.requestBindTelResult(result);
                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                bindTelCallback.requestBindTelErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });

    }

}
