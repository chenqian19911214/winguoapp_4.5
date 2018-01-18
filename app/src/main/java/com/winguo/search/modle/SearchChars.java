package com.winguo.search.modle;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.search.ISearchCallBack;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/6.
 * 搜索关键字关联列表
 */

public class SearchChars {
    //获取基础url
    //private String url= UrlConstant.BASE_URL+CommonUtil.getAppContext().getResources().getString(R.string.data_goods);
    private String url= UrlConstant.BASE_URL +"/data/goods/";
    private HashMap<String, String> params=new HashMap<>();
    public synchronized void getData(String text,ISearchCallBack searchCallBack){
        String encodeText = text;
        params.put("a","getKeywordsList");
        params.put("limit","20");
        params.put("kw",encodeText);
        String accountName = (String) SPUtils.get(CommonUtil.getAppContext(), "accountName", "");
        if (SPUtils.contains(CommonUtil.getAppContext(),"accountName")&& TextUtils.isEmpty(accountName)){
            //登录
            try {
                params.put("hash",CommonUtil.getHashValue(accountName));
                request(params,searchCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            //没有登录
            request(params,searchCallBack);
        }

    }

    /**
     * 请求网络获取数据
     * @param params
     * @param searchCallBack
     */
    private void request(final HashMap<String, String> params, final ISearchCallBack searchCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils.post(url, RequestCodeConstant.SEARCH_WORDSlIST_REQUEST_TAG, params, new IStringCallBack() {

                    @Override
                    public int stringReturn(String result) {
                        CommonUtil.printI("搜索数据：=",result);
                        if (result!=null){
                            GsonBuilder builder=new GsonBuilder();
                            builder.registerTypeAdapter(RootEntity.class,new RootEntityDeserializer());
                            Gson gson = builder.create();
                            final WordsListBean wordsListBean = gson.fromJson(result, WordsListBean.class);
//                            final WordsListBean wordsListBean = GsonUtil.json2Obj(result, WordsListBean.class);
                            if (wordsListBean!=null){
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (wordsListBean.root.has_more!=0) {
                                            searchCallBack.onBackwordsList(wordsListBean.root.item);
                                            CommonUtil.printI("搜索数据item：=", wordsListBean.root.item.toString());
                                        }else{
                                            searchCallBack.onBackwordsList(null);
                                        }
                                    }
                                });

                            }
                        }

                        return 0;
                    }

                    @Override
                    public void exceptionMessage(String message) {
                        searchCallBack.onBackwordsList(null);
                    }
                });
            }
        });
    }

}
