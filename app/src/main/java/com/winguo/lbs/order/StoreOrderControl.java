package com.winguo.lbs.order;

import android.content.Context;

import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.lbs.bean.StoreOrderDetailBean;
import com.winguo.lbs.order.list.StoreOrderBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class StoreOrderControl {
    private static Context mContext;
    private static String mUid;
    private static int mT_juchu_payment_status;

    /**
     * 实体店订单列表
     *
     * @param context
     * @param uid
     * @param t_juchu_payment_status
     * @param page
     * @param storeOrderCallback
     */
    public static void requestStoreOrder(final Context context, final String uid, final int t_juchu_payment_status, final int page, final IStoreOrder storeOrderCallback) {
        mContext = context;
        mUid = uid;
        mT_juchu_payment_status = t_juchu_payment_status;
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                String hash = WinguoAccountDataMgr.getHashCommon(context);
                String url = "?uid=" + uid + "&hash=" + hash + "&t_juchu_payment_status=" + t_juchu_payment_status + "&page=" + page;
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.STORE_ORDER + url, -1, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        try {
                            CommonUtil.printE("实体店订单列表", result + "*******");
                            JSONObject object = new JSONObject(result);
                            int size = object.getInt("size");
                            if (size != 0) {
                                storeOrderCallback.callback(GsonUtil.json2Obj(result, StoreOrderBean.class));
                            } else {
                                storeOrderCallback.callback(new StoreOrderBean());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failReturn() {
                        storeOrderCallback.callback(null);
                    }
                });
            }
        });
    }

    /**
     * 分页加载更多
     *
     * @param page
     * @param storeOrderCallback
     */
    public static void requestStoreOrder(int page, final IStoreOrder storeOrderCallback) {
        requestStoreOrder(mContext, mUid, mT_juchu_payment_status, page, storeOrderCallback);
    }

    public interface IStoreOrder {
        void callback(StoreOrderBean storeOrderBean);
    }

    /**
     * 实体店订单详情
     *
     * @param id
     * @param storeOrderDetail
     */
    public static void requestStoreOrderDetail(final Context context, final String id, final IStoreOrderDetail storeOrderDetail) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                String hash = WinguoAccountDataMgr.getHashCommon(context);
                String url = "?a=order_content&hash=" + hash + "&id=" + id;
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.STORE_ORDER + url, -1, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("实体店订单详情", result + "*******");
                        storeOrderDetail.callback(GsonUtil.json2Obj(result, StoreOrderDetailBean.class));
                    }

                    @Override
                    public void failReturn() {
                        storeOrderDetail.callback(null);
                    }
                });
            }
        });
    }

    public interface IStoreOrderDetail {
        void callback(StoreOrderDetailBean storeOrderDetailBean);
    }
}
