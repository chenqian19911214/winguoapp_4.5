package com.winguo.mine.address;

import android.app.Activity;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.mine.address.bean.AddressAreaBean;
import com.winguo.mine.address.bean.AddressCityAllBean;
import com.winguo.mine.address.bean.AddressCityArrBean;
import com.winguo.mine.address.bean.AddressCityObjBean;
import com.winguo.mine.address.bean.AddressDeleteBean;
import com.winguo.mine.address.bean.AddressInfoBean;
import com.winguo.mine.address.bean.AddressProvinceBean;
import com.winguo.mine.address.bean.AddressTownBean;
import com.winguo.mine.address.bean.AddressUpdateBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.modle.store.AddressInfoBeanDeserializer;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.pay.modle.store.ItemBeanDeserializer;
import com.winguo.pay.modle.bean.UserInfosBean;
import com.winguo.pay.modle.store.UserInfosBeanDeserializer;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hcpai
 * @desc 地址处理类
 */
public class AddressManageHandle {
    /**
     * 获取收件人信息列表
     *
     * @param activity
     * @param handler
     */
    public static void getAddressList(final Activity activity, final Handler handler) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.USERINFO_URL + "?a=getAddressLists&hash=" + hash, RequestCodeConstant.REQUEST_GET_ADDRESS_LIST, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("收件人地址", result + "************");
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(com.winguo.pay.modle.bean.AddressInfoBean.class, new AddressInfoBeanDeserializer());
                        builder.registerTypeAdapter(UserInfosBean.class, new UserInfosBeanDeserializer());
                        builder.registerTypeAdapter(ItemBean.class, new ItemBeanDeserializer());
                        Gson gson = builder.create();

                        com.winguo.pay.modle.bean.AddressInfoBean addressInfoBean = gson.fromJson(result,
                                com.winguo.pay.modle.bean.AddressInfoBean.class);
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GET_ADDRESS_LIST, addressInfoBean).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_GET_ADDRESS_LIST, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 删除地址
     *
     * @param activity
     * @param handler
     * @param rid      地址id
     */
    public static void deleteAddress(final Activity activity, final Handler handler, final int rid) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("a", "deleteAddress");
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("hash", hash);
                map.put("rid", rid + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.USERINFO_URL, RequestCodeConstant.REQUEST_DELETEADDRESS, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("删除地址", result + "************");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELETEADDRESS, GsonUtil.json2Obj(result, AddressDeleteBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELETEADDRESS, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 更新或者添加收货地址
     *
     * @param activity
     * @param handler
     * @param drname     收件人名
     * @param dzip       邮政编码
     * @param daddress   邮件人地址
     * @param dMobile    电话
     * @param dpid       收件人省份
     * @param dcid       收件人城市
     * @param dareaid    收件人地区
     * @param dtownid    收件人镇
     * @param dIsNewAddr 用于标志是新增加还是修改原有地址（1时为新增加，0时为修改）
     * @param dAddressId 当 dIsNewAddr为1时(即是新增加地址时)，这个参数后台不使用;当dIsNewAddr为0时（即是修改时）,该参数表示要修改的地址ID。
     * @param o          是否修改默认配送地址：0.不修改，1.修改
     **/
    public static void updateAddressInfo(final Activity activity, final Handler handler, final String drname, final String dzip, final String daddress, final String dMobile, final int dpid, final int dcid, final int dareaid, final int dtownid, final int dIsNewAddr, final int dAddressId, final int o) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            HashMap<String, String> map = new HashMap<>();
            String hash;

            @Override
            public void run() {
                try {
                    map.put("a", "delivery");
                    map.put("ctype", "1");
                    //注意:这里的id指的是account(在个人信息可以获取),可能是手机号,邮箱号或者是其他格式,看注册时是用什么注册
                    hash = "&id=" + SPUtils.get(activity, "accountName", "");
                    hash = hash + "&drname=" + URLEncoder.encode(drname, "utf-8");
                    hash = hash + "&dzip=" + dzip;
                    hash = hash + "&daddress=" + URLEncoder.encode(daddress, "utf-8");
                    hash = hash + "&dMobile=" + dMobile;
                    hash = hash + "&o=" + o;
                    hash = hash + "&dpid=" + dpid;
                    hash = hash + "&dcid=" + dcid;
                    hash = hash + "&dareaid=" + dareaid;
                    hash = hash + "&dtownid=" + dtownid;
                    hash = hash + "&token=" + WinguoAccountDataMgr.getTOKEN(activity);
                    hash = hash + "&uuid=" + WinguoAccountDataMgr.getUUID(activity);
                    hash = hash + "&dIsNewAddr=" + dIsNewAddr;
                    hash = hash + "&dAddressId=" + dAddressId;
                    hash = CommonUtil.encodeData(hash, WinguoAccountDataMgr.getKEY(activity), "utf-8");
                    map.put("hash", hash);
                    map.put("ctype", "1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.CART_URL, RequestCodeConstant.REQUEST_UPDATEADDRESS, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("更新地址", result + "************");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_UPDATEADDRESS, GsonUtil.json2Obj(result, AddressUpdateBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_UPDATEADDRESS, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 获取省
     */
    public static void getProvince(final IAddressResult<AddressProvinceBean> iAddressResult) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.INDEX_URL + "?a=getProvince&ctype=1", RequestCodeConstant.REQUEST_GETPROVINCE, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("获取省", result + "*****");
                        AddressProvinceBean addressProvinceBean = GsonUtil.json2Obj(result, AddressProvinceBean.class);
                        iAddressResult.getResult(addressProvinceBean);
                    }

                    @Override
                    public void failReturn() {
                        iAddressResult.getResult(null);
                    }
                });
            }
        });
    }

    /**
     * 获取市
     *
     * @param pid 省id
     */
    public static void getCity(final int pid, final IAddressResult<AddressCityAllBean> iAddressResult) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.INDEX_URL + "?a=getCity&pid=" + pid + "&ctype=1", RequestCodeConstant.REQUEST_GETCITY, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("获取市", result + "*****");
                        //有些城市只有一个(钓鱼岛)
                        AddressCityAllBean addressCityAllBean = new AddressCityAllBean();
                        try {
                            AddressCityArrBean addressCityArrBean = GsonUtil.json2Obj(result, AddressCityArrBean.class);
                            addressCityAllBean.setAddressCityArrBean(addressCityArrBean);
                            addressCityAllBean.setObj(false);
                        } catch (JsonSyntaxException e) {
                            AddressCityObjBean addressCityObjBean = GsonUtil.json2Obj(result, AddressCityObjBean.class);
                            addressCityAllBean.setAddressCityObjBean(addressCityObjBean);
                            addressCityAllBean.setObj(true);
                        }
                        iAddressResult.getResult(addressCityAllBean);
                    }

                    @Override
                    public void failReturn() {
                        iAddressResult.getResult(null);
                    }
                });
            }
        });
    }

    /**
     * 获取县
     *
     * @param cid 市id
     */
    public static void getArea(final int cid, final IAddressResult<AddressAreaBean> iAddressResult) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.INDEX_URL + "?a=getArea&cid=" + cid + "&ctype=1", RequestCodeConstant.REQUEST_GETAREA, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("获取县", result + "*****");
                        try {
                            AddressAreaBean addressAreaBean = GsonUtil.json2Obj(result, AddressAreaBean.class);
                            iAddressResult.getResult(addressAreaBean);
                        } catch (JsonSyntaxException e) {
                            try {
                                //只有一个县(全境)
                                AddressAreaBean addressAreaBean = new AddressAreaBean();
                                AddressAreaBean.AreaBean areaBean = new AddressAreaBean.AreaBean();
                                List<AddressInfoBean> items = new ArrayList<>();
                                AddressInfoBean addressInfoBean = new AddressInfoBean();
                                JsonParser parser = new JsonParser();
                                JsonElement element = parser.parse(result);
                                JsonObject root = element.getAsJsonObject();
                                JsonObject areaRoot = root.getAsJsonObject("area");
                                JsonObject item = areaRoot.getAsJsonObject("item");
                                int code = item.getAsJsonPrimitive("code").getAsInt();
                                String name = item.getAsJsonPrimitive("name").getAsString();
                                addressInfoBean.setCode(code);
                                addressInfoBean.setName(name);
                                items.add(addressInfoBean);
                                areaBean.setItem(items);
                                addressAreaBean.setArea(areaBean);
                                //iAddressResult.getResult(addressAreaBean);
                            } catch (JsonSyntaxException e1) {
                                //没有县
                                AddressAreaBean addressAreaBean = new AddressAreaBean();
                                iAddressResult.getResult(addressAreaBean);
                            }
                        }
                    }

                    @Override
                    public void failReturn() {
                        iAddressResult.getResult(null);
                    }
                });
            }
        });
    }

    /**
     * 获取镇
     *
     * @param aid 县id
     */
    public static void getTown(final int aid, final IAddressResult<AddressTownBean> iAddressResult) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.INDEX_URL + "?a=getTown&aid=" + aid + "&ctype=1", RequestCodeConstant.REQUEST_GETTOWN, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("获取镇", result + "*****");
                        //有些没有镇(第四级)
                        try {
                            AddressTownBean addressTownBean = GsonUtil.json2Obj(result, AddressTownBean.class);
                            iAddressResult.getResult(addressTownBean);
                        } catch (JsonSyntaxException e) {
                            //传个空对象:表示没有镇
                            AddressTownBean addressTownBean = new AddressTownBean();
                            iAddressResult.getResult(addressTownBean);
                        }
                    }

                    @Override
                    public void failReturn() {
                        //传null,表示请求网络失败
                        iAddressResult.getResult(null);
                    }
                });
            }
        });
    }
}
