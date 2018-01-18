package com.winguo.theme.modle;

import android.content.Context;

import com.guobi.account.WinguoAccountConfig;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.theme.IGoodsThemeCallBack;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/5/17.
 */

public class ThemeRequestNet {
    /**
     * 获取主题数据  UrlConstant.BASE_URL
     */
    public void getThemeData(Context context, String cateid, IGoodsThemeCallBack iGoodsThemeCallBack){
        String url= WinguoAccountConfig.getDOMAIN()+context.getString(R.string.goods_theme_url)+"?a=topicGoods"+"&tid="+cateid;
        request(url,iGoodsThemeCallBack);
    }

    private void request(final String url, final IGoodsThemeCallBack iGoodsThemeCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.printI("首页主题URL",url);
                MyOkHttpUtils2.post(url, RequestCodeConstant.GOODS_THEME_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        final GoodsThemesBean goodsThemesBean = GsonUtil.json2Obj(result, GoodsThemesBean.class);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iGoodsThemeCallBack.getGoodsTheme(goodsThemesBean);
                            }
                        });
                    }

                    @Override
                    public void failReturn() {
                        iGoodsThemeCallBack.getGoodsTheme(null);
                    }
                });
            }
        });
    }
}
