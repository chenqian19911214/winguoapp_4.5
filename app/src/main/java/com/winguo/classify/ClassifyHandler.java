package com.winguo.classify;

import android.app.Activity;
import android.os.Handler;

import com.guobi.account.WinguoAccountConfig;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.UrlConstant;


/**
 * @author hcpai
 * @desc 分类处理类
 */
public class ClassifyHandler {
    /**
     * 获取一级分类
     *
     * @param page
     * @param limit
     */
    public static void getFirstCategories(final Handler handler, final int page, final int limit) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {

            @Override
            public void run() {
                String url = "?a=getVirtualCategories";
                url = url + "&page=" + page;
                url = url + "&limit=" + limit;
                url = url + "&ctype=1";
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.GOODS_URL + url, RequestCodeConstant.REQUEST_GET_FIRST_CATEGORIES, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("一级数据", result + "*******");
                        ClassifyFirstBean classifyFirstBean = GsonUtil.json2Obj(result, ClassifyFirstBean.class);
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GET_FIRST_CATEGORIES, classifyFirstBean).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GET_FIRST_CATEGORIES, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 获取二级分类
     *
     * @param partUrl 部分url,从一级分类获取
     * @param vid     分类id,从一级分类获取
     */
    public static void getSecondCategories(final Activity activity, final Handler handler, final String partUrl, final int vid, final int page, final int limit, final boolean isLogin, final int position) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url = "?a=getCategoryList";
                url = url + "&page=" + page;
                url = url + "&limit=" + limit;
                url = url + "&vid=" + vid;
                url = url + "&ctype=1";
                url = WinguoAccountConfig.getDOMAIN() + UrlConstant.GOODS_URL + url;
                MyOkHttpUtils2.post(url, RequestCodeConstant.REQUEST_GET_SECOND_CATEGORIES, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        //CommonUtil.printE("二级数据", result + "*********");
                        ClassifySecondBean classifySecondBean = GsonUtil.json2Obj(result, ClassifySecondBean.class);
                        try {
                            //设置父id
                            classifySecondBean.getRoot().getCategories().setPosition(position);
                            //发送
                            handler.obtainMessage(RequestCodeConstant.REQUEST_GET_SECOND_CATEGORIES, classifySecondBean).sendToTarget();
                        } catch (NullPointerException e) {
                            ToastUtil.show(activity, "二级分类解析异常");
                        }
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GET_SECOND_CATEGORIES, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 获取分类商品
     *
     * @param activity
     * @param handler
     * @param cateid
     * @param page
     * @param limit
     * @param isLogin
     * @param or
     * @param so
     */
    /*public static void getCategoryGoods(final Activity activity, final Handler handler, final int cateid, final int page, final int limit, final boolean isLogin, final String or, final String so) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("a", "category");
                if (isLogin) {
                    String userId = ConfigUtil.getInstance(activity).getUserId();
                    try {
                        String hashValue = CommonUtil.getHashValue(userId);
                        String hash = CommonUtil.encodeData(hashValue, MyApplication.publicKey, "utf-8");
                        map.put("hash", hash);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                map.put("cateid", cateid + "");
                map.put("page", page + "");
                map.put("limit", limit + "");
                map.put("or", or + "");
                map.put("or", so + "");
                MyOkHttpUtils.post(UrlConstant.BASE_URL + UrlConstant.GOODS_URL, RequestCodeConstant.REQUEST_GET_CATEGORIES_GOODS, map, new IStringCallBack() {
                    @Override
                    public void stringReturn(String result) {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GET_CATEGORIES_GOODS, GsonUtil.json2Obj(result, ClassifyGoodsBean.class)).sendToTarget();
                    }
                });
            }
        });
    }*/
}
