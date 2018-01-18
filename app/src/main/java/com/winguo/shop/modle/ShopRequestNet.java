package com.winguo.shop.modle;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.productList.IProductListCallBack;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProductsEntity;
import com.winguo.productList.modle.ProductsEntityDeserializer;
import com.winguo.productList.bean.SearchEntity;
import com.winguo.productList.modle.SearchEntityDeserializer;
import com.winguo.productList.bean.ValueEntity;
import com.winguo.productList.modle.classify.ValueEntityDeserializer;
import com.winguo.productList.bean.ValuesEntity;
import com.winguo.productList.modle.classify.ValuesEntityDeserializer;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/9.
 */

public class ShopRequestNet {
    private String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_goods);


    public void getData(Context context, int index, IProductListCallBack iProductListCallBack, String orders,
                        String sort, String mid) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("a", "search");
        params.put("page", String.valueOf(index));
        params.put("limit", String.valueOf(20));
        params.put("or", orders);
        params.put("so", sort);
        params.put("ctype", String.valueOf(1));
        if (mid != null) {
            params.put("mid", mid);
        }
        //判断是否登录
        if (SPUtils.contains(context,"accountName")) {
            try {
                params.put("hash",  WinguoAccountDataMgr.getHashCommon(context));
                request(params, iProductListCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            request(params, iProductListCallBack);
        }
    }

    /**
     * 请求网络获取数据
     *
     * @param params
     * @param
     */
    private void request(final HashMap<String, String> params, final IProductListCallBack iProductListCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PRODUCT_LIST_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printD("店铺商品列表获取的结果：=", result);
                        if (result != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(SearchEntity.class, new SearchEntityDeserializer());
                            gsonBuilder.registerTypeAdapter(ProductsEntity.class, new ProductsEntityDeserializer());
                            Gson gson = gsonBuilder.create();
                            final ProductListBean productListBean = gson.fromJson(result, ProductListBean.class);


                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.printD("店铺商品列表获取的结果：=", productListBean.toString());
                                    iProductListCallBack.onBackProductList(productListBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductListCallBack.onBackProductList(null);
                    }

                });
            }
        });
    }

    /**
     * 加载更多数据的请求
     *
     * @param index
     * @param iProductListCallBack
     * @param sort
     * @param orders
     */
    public void getMoreData(Context context, int index, IProductListCallBack iProductListCallBack, String orders,
                            String sort, String mid) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("a", "search");
        params.put("page", String.valueOf(index));
        params.put("limit", String.valueOf(20));
        params.put("or", orders);
        params.put("so", sort);
        params.put("ctype", String.valueOf(1));
        if (mid != null) {
            params.put("mid", mid);
        }
        //判断是否登录
        if (SPUtils.contains(context,"accountName")) {
            try {
                params.put("hash",  WinguoAccountDataMgr.getHashCommon(context));
                requestMore(params, iProductListCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            requestMore(params, iProductListCallBack);
        }
    }

    /**
     * 请求网络获取数据
     *
     * @param params
     * @param
     */
    private void requestMore(final HashMap<String, String> params, final IProductListCallBack iProductListCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PRODUCT_LIST_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {


                        if (result != null) {
                            //final ProductListBean productListBean = GsonUtil.json2Obj(result, ProductListBean.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(SearchEntity.class, new SearchEntityDeserializer());
                            gsonBuilder.registerTypeAdapter(ProductsEntity.class, new ProductsEntityDeserializer());
                            gsonBuilder.registerTypeAdapter(ValuesEntity.class, new ValuesEntityDeserializer());
                            gsonBuilder.registerTypeAdapter(ValueEntity.class, new ValueEntityDeserializer());
                            Gson gson = gsonBuilder.create();
                            final ProductListBean productListBean = gson.fromJson(result, ProductListBean.class);


                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.printE("店铺更多商品列表获取的结果：=", productListBean.toString());
                                    iProductListCallBack.onBackMoreProductList(productListBean);
                                }
                            });


                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductListCallBack.onBackMoreProductList(null);
                    }

                });
            }
        });
    }

}
