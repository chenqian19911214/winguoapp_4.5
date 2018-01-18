package com.winguo.productList.modle.city;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.productList.IProductListCallBack;
import com.winguo.productList.bean.CitiesBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ProvinceItemBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Admin on 2017/1/16.
 */

public class CityRequestNet {
    private String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_index);

    public void getData(IProductListCallBack iProductListCallBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("a", "getProvince");
        params.put("with_city", String.valueOf(1));
            request(params,iProductListCallBack);

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
                MyOkHttpUtils2.post(url, RequestCodeConstant.PRODUCT_LISTCITY_REQUEST_TAG, params, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        if (result != null) {
                            GsonBuilder builder=new GsonBuilder();
                            builder.registerTypeAdapter(ProvinceItemBean.class,new ProvinceItemBeanDeserializer());
                            builder.registerTypeAdapter(CitiesBean.class,new CitiesBeanDeserializer());
                            Gson gson = builder.create();
                            final ProvinceCityBean provinceCityBean = gson.fromJson(result, ProvinceCityBean.class);

                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iProductListCallBack.onBackProvinceCityData(provinceCityBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductListCallBack.onBackProvinceCityData(null);
                    }

                });
            }
        });
    }
}
