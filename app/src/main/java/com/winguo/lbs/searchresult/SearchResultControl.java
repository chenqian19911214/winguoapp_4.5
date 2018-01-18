package com.winguo.lbs.searchresult;

import com.guobi.account.WinguoAccountConfig;
import com.winguo.lbs.bean.SearchResultGoodsBean;
import com.winguo.lbs.bean.SearchResultShopBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class SearchResultControl {
    private static String mVal;
    private static double mLng;
    private static double mLat;

    /**
     * @param val            关键词
     * @param state          0:综合1：销量 、降序。2：销量、升序。3：价格、降序。4：价格、升序。
     * @param lng            经度
     * @param lat            纬度
     * @param page           页数
     * @param searchCallBack 回调
     */
    public static void requestSearchGoodsResult(final String val, final int state, final double lng, final double lat, final int page, final INearbySearchGoodsResult searchCallBack) {
        mVal = val;
        mLng = lng;
        mLat = lat;
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("val", val);
                map.put("state", state + "");
                map.put("lng", lng + "");
                map.put("lat", lat + "");
                map.put("page", page + "");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.NEARBY_SEARCH_GOODS_RESULT, -1, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        try {
                            CommonUtil.printE("附近搜索商品结果", result + "*******");
                            JSONObject object = new JSONObject(result);
                            int size = object.getInt("size");
                            if (size != 0) {
                                searchCallBack.onBackWordsList(GsonUtil.json2Obj(result, SearchResultGoodsBean.class));
                            } else {
                                searchCallBack.onBackWordsList(new SearchResultGoodsBean());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failReturn() {
                        searchCallBack.onBackWordsList(null);
                    }
                });
            }
        });
    }

    /**
     * 分页加载/重新排序
     *
     * @param state
     * @param page
     * @param searchCallBack
     */
    public static void requestSearchGoodsResult(final int state, final int page, INearbySearchGoodsResult searchCallBack) {
        requestSearchGoodsResult(mVal, state, mLng, mLat, page, searchCallBack);
    }

    /**
     * 实体店商品回调接口
     */
    interface INearbySearchGoodsResult {
        void onBackWordsList(SearchResultGoodsBean searchResultGoodsBean);
    }

    /*----------------------实体店搜索-----------------------*/
    public static void requestSearchShopResult(final String name, final int state, final double lng, final double lat, final int page, final INearbySearchShopResult searchCallBack) {
        mVal = name;
        mLng = lng;
        mLat = lat;
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("a", "entitySearch");
                map.put("name", name);
                map.put("state", state + "");
                map.put("lng", lng + "");
                map.put("lat", lat + "");
                map.put("page", page + "");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.NEARBY_SEARCH_SHOP_RESULT, -1, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        try {
                            CommonUtil.printE("附近搜索店铺结果", result + "*******");
                            JSONObject object = new JSONObject(result);
                            int total = object.getInt("Total");
                            if (total != 0) {
                                searchCallBack.onBackWordsList(GsonUtil.json2Obj(result, SearchResultShopBean.class));
                            } else {
                                searchCallBack.onBackWordsList(new SearchResultShopBean());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failReturn() {
                        searchCallBack.onBackWordsList(null);
                    }
                });
            }
        });
    }

    /**
     * 分页加载/重新排序
     *
     * @param state
     * @param page
     * @param searchCallBack
     */
    public static void requestSearchShopResult(final int state, final int page, INearbySearchShopResult searchCallBack) {
        requestSearchShopResult(mVal, state, mLng, mLat, page, searchCallBack);
    }

    /**
     * 实体店店铺回调接口
     */
    interface INearbySearchShopResult {
        void onBackWordsList(SearchResultShopBean searchResultShopBean);
    }

}
