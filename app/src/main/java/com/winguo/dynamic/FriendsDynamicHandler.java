package com.winguo.dynamic;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;


/**
 * @author hcpai
 * @desc ${TODD}
 */
public class FriendsDynamicHandler {
    public static void getFriendsDynamic(final Activity activity, final Handler handler, final int limit, final int page) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                String accountName = (String) SPUtils.get(activity, "accountName", "");
                String url = "?a=partnerShop&hash=" + hash + "&account=" + accountName + "&limit=" + limit + "&page=" + page;
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.PARTNER_URL + url, RequestCodeConstant.REQUEST_FRIEND_DYNAMIC, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        Log.e("好友动态", result + "**********");
                        try {
                            FriendDynamicBean friendDynamicBean = GsonUtil.json2Obj(result, FriendDynamicBean.class);
                            handler.obtainMessage(RequestCodeConstant.REQUEST_FRIEND_DYNAMIC, friendDynamicBean).sendToTarget();
                        } catch (JsonSyntaxException e) {
                            FriendDynamicBean friendDynamicBean = new FriendDynamicBean();
                            friendDynamicBean.setCode("-1");

                            handler.obtainMessage(RequestCodeConstant.REQUEST_FRIEND_DYNAMIC, friendDynamicBean).sendToTarget();
                        }

                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_FRIEND_DYNAMIC, null).sendToTarget();
                    }
                });
            }
        });
    }
}
