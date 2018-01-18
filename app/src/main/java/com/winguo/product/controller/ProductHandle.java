package com.winguo.product.controller;

import android.os.Handler;

import com.google.gson.JsonSyntaxException;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.product.modle.bean.EvaluateArrBean;
import com.winguo.product.modle.bean.EvaluateObjectBean;
import com.winguo.product.modle.ProductEvaluateAllBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * @author hcpai
 * @desc 商品
 */
public class ProductHandle {
    private static int mLimit;

    /**
     * 获取商品评论
     *
     * @param handler
     * @param gid
     */
    public static void getEvaluate(final Handler handler, final int gid, final int limit, final int page) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                mLimit = limit;
                HashMap<String, String> map = new HashMap<>();
                map.put("a", "search");
                map.put("gid", gid + "");
                map.put("limit", limit + "");
                map.put("page", page + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(UrlConstant.BASE_URL + UrlConstant.PRODUCT_COMMENT_URL, RequestCodeConstant.REQUEST_PRODUCT_COMMENT, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("商品评论", result + "*************");
                        ProductEvaluateAllBean productEvaluateAllBean = new ProductEvaluateAllBean();
                        try {
                            //一个评论
                            EvaluateObjectBean evaluateObjectBean = GsonUtil.json2Obj(result, EvaluateObjectBean.class);
                            productEvaluateAllBean.setEvaluateObjectBean(evaluateObjectBean);
                            productEvaluateAllBean.setHasData(true);
                            productEvaluateAllBean.setObject(true);
                        } catch (JsonSyntaxException e) {
                            try {
                                //多个评论
                                EvaluateArrBean evaluateArrBean = GsonUtil.json2Obj(result, EvaluateArrBean.class);
                                productEvaluateAllBean.setEvaluateArrBean(evaluateArrBean);
                                productEvaluateAllBean.setObject(false);
                                productEvaluateAllBean.setHasData(true);
                            } catch (JsonSyntaxException classCastException) {
                                //没有评论
                                productEvaluateAllBean.setHasData(false);
                            }
                        }
                        handler.obtainMessage(RequestCodeConstant.REQUEST_PRODUCT_COMMENT, productEvaluateAllBean).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_PRODUCT_COMMENT, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 加载更多
     *
     * @param gid
     * @param page
     */
    public static void getEvaluate(Handler handler,final int gid, final int page) {
        getEvaluate(handler, gid, mLimit, page);
    }
}
