package com.winguo.search.nearby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountConfig;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.search.modle.RootEntity;
import com.winguo.search.modle.RootEntityDeserializer;
import com.winguo.search.modle.WordsListBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import java.util.HashMap;
import java.util.List;

/**
 * @author hcpai
 * @desc 附近搜索的控制器
 */

public class NearbySearchControl {
    public static void requestSeek(final String kw, final INearbySeekResult searchCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("a", "getKeywordsList");
                map.put("page", "1");
                map.put("limit", "20");
                map.put("kw", kw);
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.GOODS_URL, -1, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("附近关键词搜索", result + "*******");
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(RootEntity.class, new RootEntityDeserializer());
                        Gson gson = builder.create();
                        final WordsListBean wordsListBean = gson.fromJson(result, WordsListBean.class);
                        if (wordsListBean != null) {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (wordsListBean.root.item != null) {
                                        searchCallBack.onBackWordsList(wordsListBean.root.item);
                                    } else {
                                        searchCallBack.onBackWordsList(null);
                                    }
                                }
                            });

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

    interface INearbySeekResult {
        void onBackWordsList(List<String> results);
    }
}
