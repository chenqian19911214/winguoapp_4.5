package com.winguo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.guobi.account.NameValuePair;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.bean.CartShopList;
import com.winguo.bean.SectionType;
import com.winguo.bean.StoreDetail;
import com.winguo.bean.StoreDetail2;
import com.winguo.bean.StoreShop;
import com.winguo.bean.StoreShopDetail;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/20.
 */

public class NearbyStoreUtil {

    /**
     * http://apidev.winguo.com/data/entity?a=getEntityDetail&shopid=2436&lng=123&lat=123
     * 实体店详情
     *
     * @param context
     * @param shopid             实体店铺id
     * @param lng
     * @param lat
     * @param requestStoreDetail
     */
    public static void requestStoreDetail(Context context, String shopid, final double lng, final double lat, final IRequestStoreDetail requestStoreDetail) {
        String headUrl = UrlConstant.NEARBY_STORE_DETAIL_URL;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("shopid", shopid));
        // valueList.add(new NameValuePair("lng", 123 + ""));
        // valueList.add(new NameValuePair("lat", 123 + ""));
        valueList.add(new NameValuePair("lng", lng + ""));
        valueList.add(new NameValuePair("lat", lat + ""));
        final String finalUrl = URLCreator.create(headUrl, valueList);
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestStore", "requestStoreDetail result :" + result);
                //{"Code":"-1","Result":"NULL","Msg":"\u62b1\u6b49\uff01\u5217\u8868\u4e3a\u7a7a\uff01"}
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {
                        JSONObject root = new JSONObject(result);
                        String code = root.getString("Code");
                        if (code.equals("-1")) {
                            //没有找到该店铺
                            requestStoreDetail.storeDetail(null);
                        } else {
                            StoreDetail storeDetail = GsonUtil.json2Obj(result, StoreDetail.class);
                            requestStoreDetail.storeDetail(storeDetail);
                        }
                    } else {
                        requestStoreDetail.storeDetailErrorMsg("error");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestStoreDetail.storeDetailErrorMsg(message);
            }
        });


    }

    /**
     * http://apidev.winguo.com/data/entity?a=getEntityDetail&shopid=2436&lng=123&lat=123
     * 实体店详情
     *
     * @param context
     * @param shopid             实体店铺id
     * @param requestStoreDetail
     */
    public static void requestStoreDetail2(Context context, String shopid, final IRequestStoreDetail2 requestStoreDetail) {
        String headUrl = UrlConstant.NEARBY_STORE_DETAIL_URL2;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("shopid", shopid));
        // valueList.add(new NameValuePair("lng", 123 + ""));
        // valueList.add(new NameValuePair("lat", 123 + ""));
        final String finalUrl = URLCreator.create(headUrl, valueList);
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestStore", "requestStoreDetail result :" + result);
                //{"Code":"-1","Result":"Null","Msg":"\u62b1\u6b49\uff01\u5217\u8868\u4e3a\u7a7a\uff01"}
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {
                        JSONObject root = new JSONObject(result);
                        String flag = root.getString("Result");
                        if ("Null".equals(flag)) {
                            //没有找到该店铺
                            requestStoreDetail.storeDetail2(null);
                        } else {
                            StoreDetail2 storeDetail = GsonUtil.json2Obj(result, StoreDetail2.class);
                            requestStoreDetail.storeDetail2(storeDetail);
                        }
                    } else {
                        requestStoreDetail.storeDetailErrorMsg2("error");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestStoreDetail.storeDetailErrorMsg2(message);
            }
        });


    }


    /**
     * 实体店商品列表：$domain/data/entity?a=getEntityItems&eid=2422&limit=2&page=2
     *
     * @param context
     * @param eid                  实体店铺id
     * @param limit
     * @param page
     * @param requestStoreShopList
     */
    public static void requestStoreShopList(Context context, String eid, String limit, int page, final IRequestStoreShopList requestStoreShopList) {
        String headUrl = UrlConstant.STORE_SHOP_LIST_URL;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("eid", eid));
        //valueList.add(new NameValuePair("eid", 2422 + ""));
        valueList.add(new NameValuePair("limit", limit));
        valueList.add(new NameValuePair("page", page + ""));
        final String finalUrl = URLCreator.create(headUrl, valueList);
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestStore", "requestStoreShopList result :" + result);
                //{"Code":"-1","Result":"NULL","Msg":"\u62b1\u6b49\uff01\u5217\u8868\u4e3a\u7a7a\uff01"}
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {
                        JSONObject root = new JSONObject(result);
                        String code = root.getString("Code");
                        if (code.equals("-1")) {
                            //没有商品了
                            requestStoreShopList.storeShopList(null);
                        } else {
                            StoreShop storeShop = GsonUtil.json2Obj(result, StoreShop.class);
                            requestStoreShopList.storeShopList(storeShop);
                        }
                    } else {
                        requestStoreShopList.storeShopListErrorMsg("error");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestStoreShopList.storeShopListErrorMsg(message);
            }
        });

    }

    /**
     * 实体店商品详情：$domain/data/entity?a=getEntityItemDetail&iid=85900
     *
     * @param context
     * @param iid                    实体店商品id
     * @param requestStoreShopDetail
     */
    public static void requestStoreShopDetail(Context context, String iid, final IRequestStoreShopDetail requestStoreShopDetail) {

        String headUrl = UrlConstant.STORE_SHOP_DETAIL_URL;
        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("iid", iid));
        //valueList.add(new NameValuePair("iid", "85900"));
        final String finalUrl = URLCreator.create(headUrl, valueList);
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestStore", "requestStoreShopDetail result :" + result);
                //{"Code":"-1","Result":"NULL","Msg":"\u62b1\u6b49\uff01\u5217\u8868\u4e3a\u7a7a\uff01"}
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {
                        JSONObject root = new JSONObject(result);
                        String code = root.getString("Code");
                        if (code.equals("-1")) {
                            //没有商品了
                            requestStoreShopDetail.storeShopDetail(null);
                        } else {
                            StoreShopDetail storeShopDetail = GsonUtil.json2Obj(result, StoreShopDetail.class);
                            requestStoreShopDetail.storeShopDetail(storeShopDetail);
                        }
                    } else {
                        requestStoreShopDetail.storeShopDetailErrorMsg("error");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestStoreShopDetail.storeShopDetailErrorMsg(message);
            }
        });

    }

    /**
     * 加入购物车：http://apidev.winguo.com/data/search/add?num=1&productid=46554&uid=39629&sku_id=2013
     *
     * @param context
     * @param num            商品数量
     * @param productid      商品id
     * @param uid            用户id
     * @param sku_id         规格id
     * @param requestAddCart
     */
    public static void requestAddCart(Context context, String num, String productid, String uid, String sku_id, final IRequestAddCart requestAddCart, final double price, final List<SectionType> sectionType, final String make_id) {

        String headUrl = UrlConstant.NEARBY_STORE_ADD_CART;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
//        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
//        valueList.add(new NameValuePair("num", num));
//        valueList.add(new NameValuePair("productid", productid));
//        valueList.add(new NameValuePair("uid", uid));
//        //valueList.add(new NameValuePair("uid", 6523 + ""));
//        valueList.add(new NameValuePair("sku_id", sku_id));
//        final String finalUrl = URLCreator.create(headUrl, valueList);
        final String finalUrl = headUrl + "hash=" + hashUserId + "&productid=" + productid + "&num=" + num + "&sku_id=" + sku_id;
        GBLogUtils.DEBUG_DISPLAY("finalUrl", "" + finalUrl);
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestStore+", "requestStoreShopDetail result :" + result);
                //code 	203	商品成功加入购物车	201	缺少必要参数	202	购买数量不能小于1	204	系统繁忙！请重试
                //{"status":"error","text":"\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\u3002","code":-101}
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {

                        JSONObject root = new JSONObject(result);
                        String code = root.getString("code");
                        if (!"-101".equals(code)) {
                            String size = root.getString("size");
                            if (!size.equals("0")) {
                                CartShopList cartShopList = GsonUtil.json2Obj(result, CartShopList.class);
                                List<CartShopList.ContentBean> content = cartShopList.getContent();
                                for (CartShopList.ContentBean items : content) {
                                    if (items.getMaker().getM_maker_id().equals(make_id)) {
                                        // if (items.getMaker().getM_maker_id().equals("2145")) {
                                        List<CartShopList.ContentBean.ItemBean> item = items.getItem();
                                        switch (code) {
                                            case "203":
                                                requestAddCart.addCart("商品成功加入购物车", item, price, sectionType);
                                                break;
                                            case "201":
                                                requestAddCart.addCart("缺少必要参数", item, price, null);
                                                break;
                                            case "202":
                                                requestAddCart.addCart("购买数量不能小于1", item, price, null);
                                                break;
                                            case "204":
                                                requestAddCart.addCart("系统繁忙！请重试", item, price, null);
                                                break;
                                        }
                                    }
                                }
                            }
                        } else {
                            //登录过期  添加失败
                            requestAddCart.addCartErrorMsg("登陆过期，添加失败");

                        }


                    } else {
                        requestAddCart.addCartErrorMsg("数据异常");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestAddCart.addCartErrorMsg(message);
            }
        });

    }

    /**
     * 加入购物车：http://apidev.winguo.com/data/search/add?num=1&productid=46554&uid=39629&sku_id=2013
     *
     * @param context
     * @param num            商品数量
     * @param productid      商品id
     * @param uid            用户id
     * @param sku_id         规格id
     * @param requestAddCart
     */
    public static void shopRequestAddCart(Context context, String num, String productid, String uid, String sku_id, final IShopRequestAddCart requestAddCart, final String make_id) {

        String headUrl = UrlConstant.NEARBY_STORE_ADD_CART;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
//        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
//        valueList.add(new NameValuePair("num", num));
//        valueList.add(new NameValuePair("productid", productid));
//        valueList.add(new NameValuePair("uid", uid));
//        //valueList.add(new NameValuePair("uid", 6523 + ""));
//        valueList.add(new NameValuePair("sku_id", sku_id));
//        final String finalUrl = URLCreator.create(headUrl, valueList);
        final String finalUrl = headUrl + "hash=" + hashUserId + "&productid=" + productid + "&num=" + num + "&sku_id=" + sku_id;

        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestStore+", "requestStoreShopDetail result :" + result);
                //code 	203	商品成功加入购物车	201	缺少必要参数	202	购买数量不能小于1	204	系统繁忙！请重试
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {

                        JSONObject root = new JSONObject(result);
                        String code = root.getString("code");
                        if (!"-101".equals(code)) {
                            String size = root.getString("size");
                            if (!size.equals("0")) {
                                CartShopList cartShopList = GsonUtil.json2Obj(result, CartShopList.class);
                                List<CartShopList.ContentBean> content = cartShopList.getContent();
                                for (CartShopList.ContentBean items : content) {
                                    if (items.getMaker().getM_maker_id().equals(make_id)) {
                                        // if (items.getMaker().getM_maker_id().equals("2145")) {
                                        List<CartShopList.ContentBean.ItemBean> item = items.getItem();
                                        switch (code) {
                                            case "203":
                                                requestAddCart.addCart("商品成功加入购物车", item);
                                                break;
                                            case "201":
                                                requestAddCart.addCart("缺少必要参数", item);
                                                break;
                                            case "202":
                                                requestAddCart.addCart("购买数量不能小于1", item);
                                                break;
                                            case "204":
                                                requestAddCart.addCart("系统繁忙！请重试", item);
                                                break;
                                        }
                                    }
                                }

                            }
                        } else {
                            //登录过期  添加失败
                            requestAddCart.addCartErrorMsg("登陆过期，添加失败");
                        }

                    } else {
                        requestAddCart.addCartErrorMsg("数据异常");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestAddCart.addCartErrorMsg(message);
            }
        });

    }

    /**
     * 购物车：http://apidev.winguo.com/data/search/add?a=user_gw_select&uid=39629
     *
     * @param context
     * @param uid             用户id
     * @param make_id         规格id
     * @param requestCartList
     */
    public static void requestShopListCart(final Context context, String uid, final String make_id, final IRequestCartList requestCartList) {

        String headUrl = UrlConstant.NEARBY_STORE_CART_LIST;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
//        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
//        //valueList.add(new NameValuePair("uid", 39629 + ""));
//        valueList.add(new NameValuePair("uid", uid));
//        final String finalUrl = URLCreator.create(headUrl, valueList);
        final String finalUrl = headUrl + "&hash=" + hashUserId;
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestShopListCart", "requestShopListCart result :" + result);
                //{"content":null,"size":0,"price":null}
                try {
                    if (result != null && !TextUtils.isEmpty(result)) {
                        JSONObject root = new JSONObject(result);
                        String size = root.getString("size");
                        //{"status":"error","text":"\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\u3002","code":-101}
                        if (!"0".equals(size)) {
                            GBLogUtils.DEBUG_DISPLAY("cartLIst", "!0.equals(size)");
                            CartShopList cartShopList = GsonUtil.json2Obj(result, CartShopList.class);
                            List<CartShopList.ContentBean> content = cartShopList.getContent();
                            for (CartShopList.ContentBean items : content) {
                                if (items.getMaker().getM_maker_id().equals(make_id)) {
                                    // if (items.getMaker().getM_maker_id().equals("2145")) {
                                    List<CartShopList.ContentBean.ItemBean> item = items.getItem();
                                    requestCartList.cartList(item);
                                }
                            }
                        } else {
                            requestCartList.cartList(null);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestCartList.cartErrorMsg(message);
            }
        });

    }


    /**
     * 修改购物车数量：http://apidev.winguo.com/data/search/add?a=user_gw_num&num=21&id=7&state=2
     *
     * @param context
     * @param cartid                  购物车ID
     * @param state                   int	状态（默认3.直接输入）
     * @param number                  当前购买数量
     * @param requestModifyShopNumber
     */

    public static void requestModifyShopNumber(final Context context, String cartid, String uid, final String state, final IRequestModifyShopNumber requestModifyShopNumber, final int position, final int number, final String make_id) {


        String headUrl = UrlConstant.NEARBY_STORE_MODIFY_SHOP_NUMBER;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
//            ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
//            //valueList.add(new NameValuePair("id", 7 + ""));
//            valueList.add(new NameValuePair("id",cartid));
//            //valueList.add(new NameValuePair("uid", "39629"));
//            valueList.add(new NameValuePair("uid", uid));
//            valueList.add(new NameValuePair("state", "3"));
//            valueList.add(new NameValuePair("num", number + ""));
//            final String finalUrl = URLCreator.create(headUrl, valueList);
        final String finalUrl = headUrl + "&hash=" + hashUserId + "&id=" + cartid + "&state=3&num=" + number;
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestModifyShopNumber", "requestModifyShopNumber result :" + result);
                //{"content":null,"size":0,"price":null}

                try {
                    JSONObject root = new JSONObject(result);
                    String code = root.getString("code");
                    if (!"-101".equals(code)) {
                        String size = root.getString("size");
                        if (!size.equals("0")) {
                            CartShopList cartShopList = GsonUtil.json2Obj(result, CartShopList.class);
                            List<CartShopList.ContentBean> content = cartShopList.getContent();
                            for (CartShopList.ContentBean items : content) {
                                if (items.getMaker().getM_maker_id().equals(make_id)) {
                                    // if (items.getMaker().getM_maker_id().equals("2145")) {
                                    List<CartShopList.ContentBean.ItemBean> item = items.getItem();
                                    switch (code) {
                                        case "203":
                                            if (state.equals("1"))
                                                requestModifyShopNumber.addShopNumber("系统繁忙！请重试", item, -1, -1);
                                            else
                                                requestModifyShopNumber.delShopNumber("系统繁忙！请重试", item, -1, -1);
                                            break;
                                        case "201":

                                            if (state.equals("1"))
                                                requestModifyShopNumber.addShopNumber("缺少必要参数", item, -1, -1);
                                            else
                                                requestModifyShopNumber.delShopNumber("缺少必要参数", item, -1, -1);

                                            break;
                                        case "202":

                                            if (state.equals("1"))
                                                requestModifyShopNumber.addShopNumber("修改成功", item, position, number);
                                            else
                                                requestModifyShopNumber.delShopNumber("修改成功", item, position, number);

                                            break;
                                    }
                                    break;
                                }
                            }
                        }

                    } else {
                        //登录过期  修改失败
                        requestModifyShopNumber.modifyShopNumberErrorMsg("登陆过期，修改失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }


                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestModifyShopNumber.modifyShopNumberErrorMsg(message);
            }
        });


    }


    /**
     * 删除购物车指定商品：http://apidev.winguo.com/data/search/add?a=user_gw_del&uid=39629&id=7
     *
     * @param context
     * @param cartid             购物车ID
     * @param uid                用户id
     * @param requestDelCartShop
     */
    public static void requestDelShop(final Context context, final String cartid, String uid, final IRequestDelCartShop requestDelCartShop, final int position, final int number, final String make_id) {

        String headUrl = UrlConstant.NEARBY_STORE_DEL_SHOP;
        String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
//        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
//         valueList.add(new NameValuePair("id",cartid));
//        //valueList.add(new NameValuePair("id", 7 + ""));
//        //valueList.add(new NameValuePair("uid", 39629 + ""));
//        valueList.add(new NameValuePair("uid",uid));
//        final String finalUrl = URLCreator.create(headUrl, valueList);
        final String finalUrl = headUrl + "&hash=" + hashUserId + "&id=" + cartid;
        MyOkHttpUtils.post(finalUrl, 0, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                Log.i("requestDelShop", "requestDelShop result :" + result);
                try {
                    JSONObject root = new JSONObject(result);
                    String code = root.getString("code");
                    if (!"-101".equals(code)) {
                        String size = root.getString("size");
                        if (!size.equals("0")) {
                            CartShopList cartShopList = GsonUtil.json2Obj(result, CartShopList.class);
                            List<CartShopList.ContentBean> content = cartShopList.getContent();
                            boolean isSucess = false;
                            for (CartShopList.ContentBean items : content) {
                                if (items.getMaker().getM_maker_id().equals(make_id)) {
                                    // if (items.getMaker().getM_maker_id().equals("2145")) {
                                    List<CartShopList.ContentBean.ItemBean> item = items.getItem();
                                    switch (code) {
                                        case "203":
                                            requestDelCartShop.delCartShop("系统繁忙！请重试", item, -1, number);
                                            break;
                                        case "201":
                                            requestDelCartShop.delCartShop("缺少必要参数", item, -1, number);
                                            break;
                                        case "202":
                                            requestDelCartShop.delCartShop("删除成功", item, position, number);
                                            break;
                                    }
                                    isSucess = true;
                                    break;
                                }
                            }
                            // 该商品已被删除
                            if (!isSucess) {

                                switch (code) {
                                    case "203":
                                        requestDelCartShop.delCartShop("系统繁忙！请重试", null, -1, number);
                                        break;
                                    case "201":
                                        requestDelCartShop.delCartShop("缺少必要参数", null, -1, number);
                                        break;
                                    case "202":
                                        requestDelCartShop.delCartShop("删除成功", null, position, number);
                                        break;
                                }
                            }

                        } else {
                            // {"content":null,"size":0,"price":null,"code":"202"}
                            switch (code) {
                                case "203":
                                    requestDelCartShop.delCartShop("系统繁忙！请重试", null, -1, number);
                                    break;
                                case "201":
                                    requestDelCartShop.delCartShop("缺少必要参数", null, -1, number);
                                    break;
                                case "202":
                                    requestDelCartShop.delCartShop("删除成功", null, position, number);
                                    break;
                            }
                        }
                    } else {
                        //登陆过期 删除失败
                        requestDelCartShop.delCartShopErrorMsg("登陆过期，删除失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                requestDelCartShop.delCartShopErrorMsg(message);
            }
        });

    }

    /**
     * 修改购物车数量 接口回调
     */
    public interface IRequestModifyShopNumber {
        public void addShopNumber(String key, List<CartShopList.ContentBean.ItemBean> item, int position, int number);

        public void delShopNumber(String key, List<CartShopList.ContentBean.ItemBean> item, int position, int number);

        public void modifyShopNumberErrorMsg(String error);
    }

    /**
     * 购物车list 接口回调
     */
    public interface IRequestCartList {
        public void cartList(List<CartShopList.ContentBean.ItemBean> item);

        public void cartErrorMsg(String error);
    }

    /**
     * 添加购物车 接口回调
     */
    public interface IRequestAddCart {
        public void addCart(String key, List<CartShopList.ContentBean.ItemBean> item, double price, List<SectionType> sectionType);

        public void addCartErrorMsg(String error);
    }

    /**
     * 添加购物车 接口回调
     */
    public interface IShopRequestAddCart {
        public void addCart(String key, List<CartShopList.ContentBean.ItemBean> item);

        public void addCartErrorMsg(String error);
    }

    /**
     * 添加购物车 接口回调
     */
    public interface IRequestDelCartShop {
        public void delCartShop(String key, List<CartShopList.ContentBean.ItemBean> item, int position, int number);

        public void delCartShopErrorMsg(String error);
    }

    /**
     * 请求实体店详情 接口回调
     */
    public interface IRequestStoreDetail {
        public void storeDetail(StoreDetail storeDetail);

        public void storeDetailErrorMsg(String error);
    }

    /**
     * 请求实体店详情 接口回调
     */
    public interface IRequestStoreDetail2 {
        public void storeDetail2(StoreDetail2 storeDetail);

        public void storeDetailErrorMsg2(String error);
    }

    /**
     * 请求实体店商品列表 接口回调
     */
    public interface IRequestStoreShopList {
        public void storeShopList(StoreShop storeShop);

        public void storeShopListErrorMsg(String error);
    }

    /**
     * 请求实体店商品详情接口回调
     */
    public interface IRequestStoreShopDetail {
        public void storeShopDetail(StoreShopDetail storeShopDetail);

        public void storeShopDetailErrorMsg(String error);
    }

}
