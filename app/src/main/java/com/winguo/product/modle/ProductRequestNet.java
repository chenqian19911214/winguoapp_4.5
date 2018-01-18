package com.winguo.product.modle;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.product.IProductCallBack;
import com.winguo.product.modle.bean.AdditionalAttributesBean;
import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.bean.IconBean;
import com.winguo.product.modle.bean.ItemImagesBean;
import com.winguo.product.modle.bean.ItemSkuBean;
import com.winguo.product.modle.bean.LogoBean;
import com.winguo.product.modle.bean.PriceBean;
import com.winguo.product.modle.bean.ProductBean;
import com.winguo.product.modle.bean.ProductEntity;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ProductRequestNet {


    private String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_goods);
    private String url_detail = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_goods_detail);

    private String shop_url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_shop);

    /**
     * 获取网络数据
     * @param gid
     */
    @Deprecated
    public void getData(Context context,int gid, IProductCallBack iProductCallBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("a", "detail");
        params.put("gid", String.valueOf(gid));
        //获取用户名
        if (SPUtils.contains(context,"accountName")) {
            try {
                params.put("hash", WinguoAccountDataMgr.getHashCommon(context));
                request(params, iProductCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            request(params, iProductCallBack);
        }
    }

    /**
     *
     * @param context
     * @param gid
     * @param iProductCallBack
     * http://192.168.0.222/data/goods/info?a=detail&gid=86393
     */
    public void getDataDetail(Context context,int gid, IProductCallBack iProductCallBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("a", "detail");
        params.put("gid", String.valueOf(gid));
        //获取用户名
        if (SPUtils.contains(context,"accountName")) {
            try {
                params.put("hash", WinguoAccountDataMgr.getHashCommon(context));
                requestDetail(params, iProductCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            requestDetail(params, iProductCallBack);
        }
    }

    /**
     * 数据的回调
     * @param params
     * @param iProductCallBack
     */
    private void requestDetail(final HashMap<String, String> params, final IProductCallBack iProductCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url_detail, RequestCodeConstant.PRODUCT_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("商品详情网络数据:=", result);

                        try {
                            JSONObject root = new JSONObject(result);
                            int code = root.getInt("code");
                            if (code == 0) {
                                GoodDetail goodDetail = GsonUtil.json2Obj(result, GoodDetail.class);
                                iProductCallBack.onBackProductDetail(goodDetail);
                            }else if(code == 1) {
                                // {"msg":"商品可能已经下架","code":1,"product":[]}
                                GoodDetail goodDetail = new GoodDetail();
                                goodDetail.setCode(1);
                                goodDetail.setMsg("商品可能已经下架");
                                iProductCallBack.onBackProductDetail(goodDetail);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failReturn() {
                        iProductCallBack.onBackProductDetail(null);
                    }

                });
            }
        });
    }
    /**
     * 数据的回调
     *
     * @param params
     * @param iProductCallBack
     */
    @Deprecated
    private void request(final HashMap<String, String> params, final IProductCallBack iProductCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PRODUCT_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("商品详情网络数据:=", result);
                        if (result != null) {
                            //                            ProductEntity product = GsonUtil.json2Obj(result, ProductEntity.class);
                            //解析json数据
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(PriceBean.class, new PriceBeanDeserializer());
                            builder.registerTypeAdapter(ProductBean.class, new ProductBeanDeserializer());
                            builder.registerTypeAdapter(IconBean.class, new IconBeanDeserializer());
                            builder.registerTypeAdapter(ItemSkuBean.class, new ItemSkuBeanDeserializer());
                            builder.registerTypeAdapter(ItemImagesBean.class, new ItemImagesBeanDeserializer());
                            builder.registerTypeAdapter(AdditionalAttributesBean.class, new AdditionalAttributesBeanDeserializer());
                            Gson gson = builder.create();
//                            try {
                                final ProductEntity product = gson.fromJson(result, ProductEntity.class);

                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //返回数据通过回调函数
                                        iProductCallBack.onBackProductData(product);
                                    }
                                });
//                            }catch (){
//
//                            }
                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductCallBack.onBackProductData(null);
                    }

                });
            }
        });
    }

    /**
     * 获取网络数据
     *
     * @param mid
     */
    public void getShopData(String mid, IProductCallBack iProductCallBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("a", "getFirstPage");
        params.put("mid", mid);
        requestShop(params, iProductCallBack);
        Log.e("shopData::","url === "+shop_url+"a=getFirstPage&mid="+mid);
    }


    /**
     * 数据的回调
     *
     * @param params
     * @param iProductCallBack
     */
    private void requestShop(final HashMap<String, String> params, final IProductCallBack iProductCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(shop_url, RequestCodeConstant.SHOP_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("店铺详情网络数据:=", result);
                        if (result != null) {
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(LogoBean.class, new LogoBeanDeserializer());
                            Gson gson = builder.create();
                            final ShopSimpleBean shopSimpleBean = gson.fromJson(result, ShopSimpleBean.class);
                            CommonUtil.printI("店铺详情解析后数据:=", shopSimpleBean.toString());
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iProductCallBack.onBackShopData(shopSimpleBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductCallBack.onBackShopData(null);
                    }

                });
            }
        });
    }

    /**
     * 添加浏览记录
     */
    public void requestAddHistoryLog(final Activity activity, final int gid) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();

                map.put("a", "history");
                map.put("hash", WinguoAccountDataMgr.getHashCommon(activity));
                map.put("gid", gid + "");
                map.put("ctype", "1");
                String url = UrlConstant.BASE_URL + UrlConstant.ADD_HISTORYLOG_URL;
                MyOkHttpUtils2.post(url, RequestCodeConstant.SHOP_REQUEST_TAG, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("添加浏览记录", result + "******");

                    }

                    @Override
                    public void failReturn() {
                    }

                });
            }
        });
    }
}