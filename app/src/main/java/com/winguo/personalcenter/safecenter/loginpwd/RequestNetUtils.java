package com.winguo.personalcenter.safecenter.loginpwd;

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

import java.util.ArrayList;

/**
 * Created by admin on 2017/10/11.
 */

public class RequestNetUtils {
    public static final String TAG = RequestNetUtils.class.getSimpleName();

    public static void requestSetLoginPwd(){

    }

    public static void requestModifyoginPwdByOld(Context context, String oldPwd , String newPwd, final IRequestResultHandle resultHandle){
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
        final String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);
        if (hash == null) {
            resultHandle.requestModfifyLoginPwdByOldErrorCode(GBAccountError.HASH_FAILED);
        }

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.MODIFY_PWD;
        final int dataSize = 6;
        ArrayList<NameValuePair> valueList = new ArrayList<>(dataSize);
        valueList.add(new NameValuePair("hash", hash));// HASH
        String finalUrl = URLCreator.create(url, valueList);

        GBLogUtils.DEBUG_DISPLAY(TAG, "requestModifyoginPwdByOld by old REQ:" + finalUrl);

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                GBLogUtils.DEBUG_DISPLAY("requestModifyoginPwdByOld by old  result:", result);
                // {"message":{"status":"success","text":"密码修改成功。","code":0}}
                resultHandle.requestModfifyLoginPwdByOldResult(result);

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                resultHandle.requestModfifyLoginPwdByOldErrorCode(GBAccountError.REQUST_TIMEOUT);
            }
        });
    }

}
