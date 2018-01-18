package com.winguo.pay.modle.store;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.ISeekUserInfoCallBack;
import com.winguo.pay.modle.bean.AddressInfoBean;
import com.winguo.pay.modle.bean.UserInfosBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/9.
 */

public class SeekAddressRequestNet {
    private String url;
    public void getAddressData(Context context,ISeekUserInfoCallBack iSeekUserInfoCallBack){
        if (SPUtils.contains(context,"accountName")){//没有登录时不能加入购物车
            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
                url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_userinfo)+"?a=getAddressLists&hash="+ hashUserId;
                request(iSeekUserInfoCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            ToastUtil.showToast(context,context.getString(R.string.no_login_text));
        }
    }

    private void request(final ISeekUserInfoCallBack iSeekUserInfoCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.SEEK_USERINFO_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("收件人信息网络数据:=", result);
                        if (result != null) {
                            //解析数据
                            GsonBuilder builder=new GsonBuilder();
                            builder.registerTypeAdapter(AddressInfoBean.class,new AddressInfoBeanDeserializer());
                            builder.registerTypeAdapter(UserInfosBean.class,new UserInfosBeanDeserializer());
                            builder.registerTypeAdapter(ItemBean.class,new ItemBeanDeserializer());
                            Gson gson = builder.create();
                            final AddressInfoBean addressInfoBean = gson.fromJson(result, AddressInfoBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iSeekUserInfoCallBack.onUserInfoBackData(addressInfoBean);

                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iSeekUserInfoCallBack.onUserInfoBackData(null);
                    }

                });
            }
        });
    }
}
