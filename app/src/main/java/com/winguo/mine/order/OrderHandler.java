package com.winguo.mine.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.winguo.mine.order.bean.ConfirmReceiveBean;
import com.winguo.mine.order.bean.DeliveryRootBean;
import com.winguo.mine.order.bean.OrderDataBean;
import com.winguo.mine.order.bean.OrderDetailArryBean;
import com.winguo.mine.order.bean.OrderDetailObjBean;
import com.winguo.mine.order.bean.ShopDetailBean;
import com.winguo.mine.order.detail.OrderDetailAllBean;
import com.winguo.mine.order.list.OrderBeanList;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author hcpai
 * @desc 处理订单
 */
public class OrderHandler {
    private static String mId;
    private static Context mContext;

    /**
     * 查询订单列表
     *
     * @param handler
     * @param id      用户id
     * @param page    显示页码
     * @param limit   每页面显示行数
     * @param status  订单状态：0.全部订单，1.待付款，2.待出货，3.待收货，4.已取消，5.已完成 6.待评价
     * @param type    用户类型：0.买家，1.分销商
     */
    public static void searchOrder(final Context context, final Handler handler, final String id, final int page, final int limit, final String status, final int type) {
        mId = id;
        mContext = context;
        final OrderBeanList orderBeanList = new OrderBeanList();
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                try {
                    String hash = WinguoAccountDataMgr.getHashCommon(context);
                    map.put("a", "search");
                    map.put("hash", hash);
                    map.put("page", page + "");
                    map.put("limit", limit + "");
                    map.put("status", status + "");
                    map.put("type", type + "");
                    map.put("ctype", "1");
                    MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.USERORDER_URL, RequestCodeConstant.REQUEST_SEARCH_ORDER, map, new IStringCallBack2() {
                        @Override
                        public void stringReturn(String result) {
                            CommonUtil.printE("查看订单列表", result + "**************");
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(result);
                            JsonObject root = element.getAsJsonObject();
                            JsonObject rootBean = root.getAsJsonObject("root");
                            JsonObject otherinfo = rootBean.getAsJsonObject("otherinfo");
                            orderBeanList.setHas_more_items(otherinfo.getAsJsonPrimitive("has_more_items").getAsInt());
                            orderBeanList.setCount(otherinfo.getAsJsonPrimitive("count").getAsInt());

                            List<OrderDataBean> list = new ArrayList<>();
                            try {
                                //是对象
                                JsonObject items = rootBean.getAsJsonObject("items");
                                JsonObject data = items.getAsJsonObject("data");
                                OrderDataBean orderDataBean = new Gson().fromJson(data, OrderDataBean.class);
                                list.add(orderDataBean);
                            } catch (ClassCastException e) {
                                //是数组
                                try {
                                    JsonObject items = rootBean.getAsJsonObject("items");
                                    Gson gson = new Gson();
                                    JsonArray datas = items.getAsJsonArray("data");
                                    for (int i = 0; i < datas.size(); i++) {
                                        JsonElement data = datas.get(i);
                                        OrderDataBean orderDataBean = gson.fromJson(data, OrderDataBean.class);
                                        list.add(orderDataBean);
                                    }
                                } catch (ClassCastException classCastException) {
                                    //是null
                                }
                            }
                            orderBeanList.setOrderDataBeanList(list);
                            handler.obtainMessage(RequestCodeConstant.REQUEST_SEARCH_ORDER, orderBeanList).sendToTarget();
                        }

                        @Override
                        public void failReturn() {
                            handler.obtainMessage(RequestCodeConstant.REQUEST_SEARCH_ORDER, null).sendToTarget();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * searchOrder方法的重载
     *
     * @param page
     * @param limit
     * @param status
     */
    public static void searchOrder(Handler handler,int page, int limit, String status) {
        searchOrder(mContext, handler, mId, page, limit, status, 0);
    }

    /**
     * 查看订单详情
     *
     * @param handler
     * @param oid     订单id
     */
    public static void searchOrderDetail(final Context context, final Handler handler, final long oid) {
        final OrderDetailAllBean orderDetailAllBean = new OrderDetailAllBean();
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                String hash;
                hash = WinguoAccountDataMgr.getHashCommon(context);
                map.put("a", "detail");
                map.put("hash", hash);
                map.put("oid", oid + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.USERORDER_URL, RequestCodeConstant.REQUEST_SEARCHORDER_DETAIL, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        //节点解析:解析物流信息:有物流时就有delivery对象,没物流时没有该对象
                        CommonUtil.printE("订单详情", result + "*********");
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(result);
                        JsonObject root = element.getAsJsonObject();
                        JsonObject rootbean = root.getAsJsonObject("root");
                        JsonObject databean = rootbean.getAsJsonObject("data");
                        JsonObject delivery = databean.getAsJsonObject("delivery");
                        DeliveryRootBean deliveryRootBean = null;
                        if (delivery != null) {
                            deliveryRootBean = new Gson().fromJson(delivery, DeliveryRootBean.class);
                        }
                        //其中items可能是对象也可能是集合
                        try {

                            OrderDetailObjBean orderDetailObjBean = GsonUtil.json2Obj(result, OrderDetailObjBean.class);
                            orderDetailAllBean.setIsObject(true);
                            orderDetailAllBean.setOrderDetailObjBean(orderDetailObjBean);
                            if (deliveryRootBean != null) {
                                orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().setDeliveryRootBean(deliveryRootBean);
                            }
                        } catch (JsonSyntaxException e) {
                            OrderDetailArryBean orderDetailArryBean = GsonUtil.json2Obj(result, OrderDetailArryBean.class);
                            orderDetailAllBean.setIsObject(false);
                            orderDetailAllBean.setOrderDetailArryBean(orderDetailArryBean);
                            if (deliveryRootBean != null) {
                                orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().setDeliveryRootBean(deliveryRootBean);
                            }
                        }
                        handler.obtainMessage(RequestCodeConstant.REQUEST_SEARCHORDER_DETAIL, orderDetailAllBean).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_SEARCHORDER_DETAIL, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 获取店铺详细信息
     *
     * @param handler
     * @param mid
     */
    public static void getShopDetail(final Handler handler, final Activity activity, final int mid) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                try {
                    map.put("a", "detail");
                    map.put("mid", mid + "");
                    if (SPUtils.contains(mContext, "accountName")) {
                        String hash = WinguoAccountDataMgr.getHashCommon(activity);
                        map.put("hash", hash);
                    }
                    map.put("ctype", "1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.SHOP_COLLECT_URL, RequestCodeConstant.REQUEST_GETSHOPDETAIL, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("获取店铺详情", result + "**********");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GETSHOPDETAIL, GsonUtil.json2Obj(result, ShopDetailBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GETSHOPDETAIL, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 查看订单数
     */
    public static void getOrderCount(final Activity activity) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "getOrderCount");
                map.put("hash", hash);
                map.put("ctype", "1");
                final Intent intent = new Intent(ActionUtil.ACTION_ORDERCOUNT);
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.USERORDER_URL, RequestCodeConstant.REQUEST_ORDERCOUNT, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("查看订单数:", result + "********");
                        OrderCountBean.MessageBean messageBean = GsonUtil.json2Obj(result, OrderCountBean.class).getMessage();
                        intent.putExtra(ActionUtil.ACTION_ORDERCOUNT, messageBean);
                        activity.sendBroadcast(intent);
                    }

                    @Override
                    public void failReturn() {
                        intent.putExtra(ActionUtil.ACTION_ORDERCOUNT, (Parcelable[]) null);
                        activity.sendBroadcast(intent);
                    }
                });
            }
        });
    }

    /**
     * 确认收货
     *
     * @param activity
     * @param handler
     * @param oid      订单id
     * @param pa       支付密码
     */
    public static void ConfirmReceive(final Activity activity, final Handler handler, final int oid, final String pa) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                // 获取公钥
                final WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(mContext);
                String id = WinguoAccountDataMgr.getUserName(activity);
                String hash = "id=" + id + "&token=" + mKey.getToken() + "&uuid=" + mKey.getUUID() + "&pa=" + pa;
                hash = WinguoEncryption.commonEncryption(hash, mKey);
                map.put("a", "pay");
                map.put("hash", hash);
                map.put("oid", oid + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.USERORDER_URL, RequestCodeConstant.REQUEST_CONFIRM_RECEIVE, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("确认收货:", result + "********");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_CONFIRM_RECEIVE, GsonUtil.json2Obj(result, ConfirmReceiveBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_CONFIRM_RECEIVE, null).sendToTarget();
                    }
                });
            }
        });
    }
}
