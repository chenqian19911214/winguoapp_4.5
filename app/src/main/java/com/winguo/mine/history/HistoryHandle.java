package com.winguo.mine.history;

import android.app.Activity;
import android.os.Handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.mine.address.bean.HistoryBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * @author hcpai
 * @desc 历史记录处理类
 */
public class HistoryHandle {
    private static Handler mHandler;
    private static int mLimit;
    private static Activity mActivity;

    public static void getHistory(final Handler handler, final Activity activity, final int limit, final int page) {
        mHandler = handler;
        mLimit = limit;
        mActivity = activity;
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "history");
                map.put("hash", hash);
                map.put("limit", limit + "");
                map.put("page", page + "");
                map.put("ctype", "1");
                String url = WinguoAccountConfig.getDOMAIN() + UrlConstant.GET_HISTORYLOG_URL;
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_HISTORYLOG_HAS, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("获取浏览记录", result + "*****");
                        //有浏览记录
                        HistoryBean historyBean = GsonUtil.json2Obj(result, HistoryBean.class);
                        handler.obtainMessage(RequestCodeConstant.REQUEST_HISTORYLOG_HAS, historyBean).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        //连接超时
                        handler.obtainMessage(RequestCodeConstant.REQUEST_HISTORYLOG_HAS, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 获取历史记录的重载方法
     *
     * @param page
     */
    public static void getHistory(int page) {
        getHistory(mHandler, mActivity, mLimit, page);
    }

    /**
     * 删除浏览历史
     *
     * @param handler
     * @param activity
     * @param goodsid
     */
    public static void delHistory(final Handler handler, final Activity activity, final String goodsid) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "delhistory");
                map.put("hash", hash);
                map.put("goodsid", goodsid);
                map.put("ctype", "1");
                String url = WinguoAccountConfig.getDOMAIN() + UrlConstant.GET_HISTORYLOG_URL;
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_HISTORYLOG_HAS, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("删除浏览记录", result + "*****");
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(result);
                        JsonObject root = element.getAsJsonObject();
                        int code = root.getAsJsonPrimitive("code").getAsInt();
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELHISTORYLOG_HAS, code).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        //连接超时
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELHISTORYLOG_HAS, -1).sendToTarget();
                    }
                });
            }
        });
    }
}
