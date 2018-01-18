package com.winguo.productList.modle.classify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.productList.IProductListCallBack;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.bean.FiltersBean;
import com.winguo.productList.bean.FiltersItemBean;
import com.winguo.productList.bean.ValueEntity;
import com.winguo.productList.bean.ValuesEntity;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Admin on 2017/1/18.
 */

public class ClassifyRequestNet {
    private String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_goods);

    public void getData(String text, String orders,String sort,int cateid,IProductListCallBack iProductListCallBack) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("a","search");
        if (text != null) {
            String encodeText;
            //对搜索的关键字转码
            try {
                encodeText = URLEncoder.encode(text, "utf-8");
                params.put("kw",encodeText);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        params.put("so", sort);
        if (cateid != -1) {
            params.put("cateid", cateid + "");
        }
        params.put("or", orders);
        request(params, iProductListCallBack);


    }

    /**
     * 请求网络获取数据
     *
     * @param
     * @param
     */
    private void request(final HashMap<String, String> params, final IProductListCallBack iProductListCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PRODUCT_LIST_CLASSIFY_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("搜索商品列表获取的分类信息的结果%&", result);

                        if (result != null) {

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(FiltersBean.class,new FiltersBeanDeserializer());
                            gsonBuilder.registerTypeAdapter(FiltersItemBean.class,new FiltersItemBeanDeserializer());
                            gsonBuilder.registerTypeAdapter(ValuesEntity.class,new ValuesEntityDeserializer());
                            gsonBuilder.registerTypeAdapter(ValueEntity.class,new ValueEntityDeserializer());
                            Gson gson = gsonBuilder.create();
                            final ClassifyName classifyName = gson.fromJson(result, ClassifyName.class);
                            CommonUtil.printI("搜索商品列表获取的分类信息的结果%%%%%%%%%", classifyName.toString());
                            //传过去轮播图中的数据
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iProductListCallBack.onBackClassifyData(classifyName);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductListCallBack.onBackClassifyData(null);
                    }

                });
            }
        });
    }
}
