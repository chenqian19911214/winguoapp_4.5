package com.winguo.lbs;

import android.os.Handler;

import com.guobi.account.WinguoAccountConfig;
import com.winguo.lbs.bean.NearByBean;
import com.winguo.lbs.bean.NearByRootBean;
import com.winguo.lbs.bean.NearByTopicBean;
import com.winguo.lbs.bean.NearbyStoreBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author hcpai
 * @desc 附近
 */

public class NearByHandler {

    /**
     * 根据个人信息获取附近商家
     *
     * @param handler
     * @param page
     * @param sign
     * @param lng
     * @param lat
     */
    //http://119.29.103.72/gw.php?service=winguo.stores.nearly&page=1&sign=d768d6bab9504a04ff5f6981952dc574
    public static void getNearByShop(final Handler handler, final int page, final String sign, final double lng, final double lat) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("service", "winguo.stores.nearly");
                map.put("page", page + "");
                map.put("sign", "d768d6bab9504a04ff5f6981952dc574");
                //map.put("lat", "23.05408162510");
                //map.put("lng", "113.74101176062");
                map.put("lng", lng + "");
                map.put("lat", lat + "");
                MyOkHttpUtils2.post(UrlConstant.NEARBY_URL, RequestCodeConstant.NEARBY_REQUEST_TAG, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("根据个人信息获取附近商家", result + "**************");
                        handler.obtainMessage(RequestCodeConstant.NEARBY_REQUEST_TAG, GsonUtil.json2Obj(result, NearByRootBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.NEARBY_REQUEST_TAG, null).sendToTarget();
                    }
                });
            }
        });
    }
    /**
     * 根据个人信息获取附近商家
     *
     * @param handler
     *
     */
    public static void takeNearByShop(final Handler handler, final int page, final double lng, final double lat) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("page", page + "");
                //map.put("lat", "22.910291959359414");
                //map.put("lng", "113.88261804506772");
                map.put("lng", lng + "");
                map.put("lat", lat + "");
                MyOkHttpUtils2.post(UrlConstant.NEARBY_STORE_LIST_URL, RequestCodeConstant.NEARBY_REQUEST_TAG, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("根据个人信息获取附近商家", result + "**************");
                        // {"totals":0,"total_page":0,"store_lists":[[]],"buy_flag":"NO"}
                        try {
                            JSONObject root = new JSONObject(result);
                            String totals = root.getString("totals");
                            String total_page = root.getString("total_page");

                            if ("0".equals(totals)&&"0".equals(total_page)) {
                                handler.obtainMessage(RequestCodeConstant.NEARBY_REQUEST_NO_STORE_TAG, null).sendToTarget();
                            } else {
                                handler.obtainMessage(RequestCodeConstant.NEARBY_REQUEST_TAG, GsonUtil.json2Obj(result, NearbyStoreBean.class)).sendToTarget();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.NEARBY_REQUEST_TAG, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 根据个人信息获取附近商家主题活动
     *
     * @param handler
     */
    public static void getNearLbsTopic(final Handler handler) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("a", "lbsTopic");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.NEARBY_TOPIC_URL, RequestCodeConstant.NEARBY_TOPIC_REQUEST_TAG, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("根据个人信息获取附近商家主题活动", result + "**************");
                        handler.obtainMessage(RequestCodeConstant.NEARBY_TOPIC_REQUEST_TAG, GsonUtil.json2Obj(result, NearByTopicBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.NEARBY_TOPIC_REQUEST_TAG, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 根据店铺id获得店铺信息
     *
     * @param handler
     * @param sign
     * @param store_id
     * @param lng
     * @param lat
     */
    public static void getShopInfo(final Handler handler, final String sign, final String store_id, final double lng, final double lat) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("service", "winguo.stores.get");
                map.put("sign", "d768d6bab9504a04ff5f6981952dc574");
                map.put("store_id", store_id);
                map.put("lng", lng + "");
                map.put("lat", lat + "");
                MyOkHttpUtils2.post(UrlConstant.NEARBY_URL, RequestCodeConstant.NEARBY_SHOP_INFO_REQUEST_TAG, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("商家信息", result + "**************");
                        handler.obtainMessage(RequestCodeConstant.NEARBY_SHOP_INFO_REQUEST_TAG, GsonUtil.json2Obj(result, NearByBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.NEARBY_SHOP_INFO_REQUEST_TAG, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 根据商家id获取附近商家
     *
     * @param handler
     * @param sign
     * @param store_id
     * @param page
     */
    public static void getRecommendShop(final Handler handler, final String sign, final String store_id, final int page) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("service", "winguo.stores.storeNearly");
                map.put("sign", "d768d6bab9504a04ff5f6981952dc574");
                map.put("store_id", store_id);
                map.put("page", page + "");
                MyOkHttpUtils2.post(UrlConstant.NEARBY_URL, RequestCodeConstant.NEARBY_SHOP_BYNEAR_REQUEST_TAG, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("附近推荐商家信息", result + "**************");
                        handler.obtainMessage(RequestCodeConstant.NEARBY_SHOP_BYNEAR_REQUEST_TAG, GsonUtil.json2Obj(result, NearByRootBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.NEARBY_SHOP_BYNEAR_REQUEST_TAG, null).sendToTarget();
                    }
                });
            }
        });
    }
}
