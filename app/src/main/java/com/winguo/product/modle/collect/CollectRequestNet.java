package com.winguo.product.modle.collect;


import android.content.Context;

import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.product.IProductCallBack;
import com.winguo.product.modle.bean.CollectBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/20.
 */

public class CollectRequestNet {

    /**
     * 获取网络数据
     * @param gid
     */
    public void getData(Context context,int gid, IProductCallBack iProductCallBack){
        //获取用户名
        if (SPUtils.contains(context,"accountName")){
            try {
                String url= UrlConstant.BASE_URL+ CommonUtil.getAppContext().getResources().getString(R.string.data_goods_collect);
                url+="?a=isCollectGoods&gid="+gid+"&hash="+ WinguoAccountDataMgr.getHashCommon(context);
                request(url,iProductCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void request(final String url, final IProductCallBack iProductCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PRODUCT_COLLECT_REQUEST_TAG,null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("商品是否收藏网络数据:=",result);
                        if (result != null) {
                            final CollectBean collectBean = GsonUtil.json2Obj(result, CollectBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iProductCallBack.onBackCollectData(collectBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iProductCallBack.onBackCollectData(null);
                    }

                });
            }
        });
    }

}
