package com.winguo.mine.collect;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.mine.collect.bean.AddCollectBean;
import com.winguo.mine.collect.bean.DeleteCollectBean;
import com.winguo.mine.collect.goods.GoodsCollectAllBean;
import com.winguo.mine.collect.goods.GoodsCollectArryBean;
import com.winguo.mine.collect.goods.GoodsCollectItemBean;
import com.winguo.mine.collect.goods.GoodsCollectObjBean;
import com.winguo.mine.collect.goods.bean.GoodsCollectItem;
import com.winguo.mine.collect.goods.bean.GoodsCollectItems;
import com.winguo.mine.collect.goods.bean.GoodsCollectItemsDeserializer;
import com.winguo.mine.collect.goods.bean.GoodsCollectRoot;
import com.winguo.mine.collect.goods.bean.GoodsCollectRootBean;
import com.winguo.mine.collect.goods.bean.GoodsCollectRootDeserializer;
import com.winguo.mine.collect.shop.ShopCollectAllBean;
import com.winguo.mine.collect.shop.ShopCollectArryBean;
import com.winguo.mine.collect.shop.ShopCollectItemBean;
import com.winguo.mine.collect.shop.ShopCollectObjBean;
import com.winguo.mine.collect.shop.bean.Logo;
import com.winguo.mine.collect.shop.bean.ShopCollectDataBean;
import com.winguo.mine.collect.shop.bean.ShopCollectItemsBean;
import com.winguo.mine.collect.shop.bean.ShopCollectItemsBeanDeserializer;
import com.winguo.mine.collect.shop.bean.ShopCollectOtherDeserializer;
import com.winguo.mine.collect.shop.bean.ShopCollectOtherInfoBean;
import com.winguo.mine.collect.shop.bean.ShopCollectRoot;
import com.winguo.mine.collect.shop.bean.ShopCollectRootBean;
import com.winguo.mine.collect.shop.bean.ShopCollectRootBeanDeserializer;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ThreadPoolManager;
import com.winguo.utils.UrlConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hcpai
 * @desc 用户收藏处理类
 */
public class CollectHandler {

    /**
     * 获取商品收藏
     * 1. hash 加密串等于 (id=test&token=sdfalsd&uuid=323u9we) 用openssl加密后，再base64_encode加密，再用urlencode转码，其中id 用户登录账号
     * 2. list 是否需要返回列表 0：不返回 只返回数量1：返回数量及列表内容
     * 3. limit 每页显示行数
     * 4. limit 显示页码
     * 5. vt 是否有效0：有效 1：无效 不传此参数为全部
     * 6. shop_id 店铺ID
     */
    public static void getGoodsCollect(final Activity activity, final Handler handler, final int limit, final int page, final boolean isGetCount) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> map = new HashMap<>();
                    //hash
                    String hash = WinguoAccountDataMgr.getHashCommon(activity);
                    //其他
                    map.put("a", "search");
                    map.put("hash", hash);
                    map.put("list", "1");
                    map.put("limit", limit + "");
                    map.put("page", page + "");
                    map.put("vt", "1");
                    map.put("ctype", "1");
                    final Intent intent = new Intent(ActionUtil.ACTION_GOODS_COLLECTCOUNT);
                    MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.GOODS_COLLECT_URL, RequestCodeConstant.REQUEST_GOODS_COLLECT, map, new IStringCallBack2() {
                        @Override
                        public void stringReturn(String result) {
                            CommonUtil.printE("商品收藏", result + "************");
                            //商品收藏数量
                            int goodsCount=0;
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(GoodsCollectRootBean.class, new GoodsCollectRootDeserializer());
                            builder.registerTypeAdapter(GoodsCollectItems.class, new GoodsCollectItemsDeserializer());
                            Gson gson = builder.create();
                            GoodsCollectRoot goodsCollectRoot = gson.fromJson(result, GoodsCollectRoot.class);
                            GoodsCollectAllBean goodsCollectAllBean = new GoodsCollectAllBean();
                            List<GoodsCollectItem> items = goodsCollectRoot.root.items.item;
                            if (items == null) {
                                goodsCollectAllBean.setEmpty(true);
                            } else {
                                if (items.size() == 1) {
                                    goodsCount = 1;
                                    goodsCollectAllBean.setIsObject(true);
                                    GoodsCollectObjBean goodsCollectObjBean = new GoodsCollectObjBean();
                                    GoodsCollectObjBean.RootBean rootBean = new GoodsCollectObjBean.RootBean();
                                    GoodsCollectObjBean.ItemsBean itemsBean = new GoodsCollectObjBean.ItemsBean();
                                    GoodsCollectItemBean goodsCollectItemBean = new GoodsCollectItemBean();
                                    GoodsCollectItem goodsCollectItem = items.get(0);
                                    goodsCollectItemBean.setGid(goodsCollectItem.gid);
                                    goodsCollectItemBean.setName(goodsCollectItem.name);
                                    goodsCollectItemBean.setT_freight_temp_id(goodsCollectItem.t_freight_temp_id);
                                    goodsCollectItemBean.setPrice(goodsCollectItem.price);
                                    GoodsCollectItemBean.PicBean picBean = new GoodsCollectItemBean.PicBean();
                                    if (goodsCollectItem.pic.content == null) {
                                        goodsCollectItemBean.setPic(new GoodsCollectItemBean.PicBean());
                                    } else {
                                        picBean.setContent(goodsCollectItem.pic.content);
                                        //modifyTime字段暂时不用
                                        //picBean.setModifyTime(goodsCollectItem.pic.modifyTime);
                                        goodsCollectItemBean.setPic(picBean);
                                    }
                                    goodsCollectItemBean.setM_item_counts(goodsCollectItem.m_item_counts);
                                    goodsCollectItemBean.setCity_name(goodsCollectItem.city_name);
                                    goodsCollectItemBean.setSale_qty(goodsCollectItem.sale_qty);
                                    goodsCollectItemBean.setStock(goodsCollectItem.stock);
                                    itemsBean.setItem(goodsCollectItemBean);
                                    rootBean.setItems(itemsBean);
                                    rootBean.setCount(goodsCollectRoot.root.count);
                                    rootBean.setHas_more(goodsCollectRoot.root.has_more);
                                    goodsCollectObjBean.setRoot(rootBean);
                                    goodsCollectAllBean.setGoodsCollectObjBean(goodsCollectObjBean);
                                } else {
                                    goodsCollectAllBean.setIsObject(false);
                                    goodsCount = goodsCollectRoot.root.count;
                                    GoodsCollectArryBean goodsCollectArryBean = new GoodsCollectArryBean();
                                    GoodsCollectArryBean.RootBean rootBean = new GoodsCollectArryBean.RootBean();
                                    GoodsCollectArryBean.RootBean.ItemsBean itemsBean = new GoodsCollectArryBean.RootBean.ItemsBean();
                                    List<GoodsCollectItemBean> list = new ArrayList<>();
                                    for (int i = 0; i < items.size(); i++) {
                                        GoodsCollectItemBean goodsCollectItemBean = new GoodsCollectItemBean();
                                        GoodsCollectItem goodsCollectItem = items.get(i);
                                        goodsCollectItemBean.setGid(goodsCollectItem.gid);
                                        goodsCollectItemBean.setName(goodsCollectItem.name);
                                        goodsCollectItemBean.setT_freight_temp_id(goodsCollectItem.t_freight_temp_id);
                                        goodsCollectItemBean.setPrice(goodsCollectItem.price);
                                        GoodsCollectItemBean.PicBean picBean = new GoodsCollectItemBean.PicBean();
                                        if (goodsCollectItem.pic.content == null) {
                                            goodsCollectItemBean.setPic(new GoodsCollectItemBean.PicBean());
                                        } else {
                                            picBean.setContent(goodsCollectItem.pic.content);
                                            //modifyTime字段暂时不用
                                            //picBean.setModifyTime(goodsCollectItem.pic.modifyTime);
                                            goodsCollectItemBean.setPic(picBean);
                                        }
                                        goodsCollectItemBean.setM_item_counts(goodsCollectItem.m_item_counts);
                                        goodsCollectItemBean.setCity_name(goodsCollectItem.city_name);
                                        goodsCollectItemBean.setSale_qty(goodsCollectItem.sale_qty);
                                        goodsCollectItemBean.setStock(goodsCollectItem.stock);
                                        list.add(goodsCollectItemBean);
                                    }
                                    itemsBean.setItem(list);
                                    rootBean.setItems(itemsBean);
                                    rootBean.setCount(goodsCollectRoot.root.count);
                                    rootBean.setHas_more(goodsCollectRoot.root.has_more);
                                    goodsCollectArryBean.setRoot(rootBean);
                                    goodsCollectAllBean.setGoodsCollectArryBean(goodsCollectArryBean);
                                }
                                //TODO
                                //goodsCollectRoot.root.items;
                          /*  try {
                                GoodsCollectObjBean goodsCollectObjBean = GsonUtil.json2Obj(result, GoodsCollectObjBean.class);
                                goodsCollectAllBean.setIsObject(true);
                                goodsCollectAllBean.setEmpty(false);
                                goodsCollectAllBean.setGoodsCollectObjBean(goodsCollectObjBean);
                                goodsCount = 1;
                            } catch (JsonSyntaxException e) {
                                try {
                                    GoodsCollectArryBean goodsCollectArryBean = GsonUtil.json2Obj(result, GoodsCollectArryBean.class);
                                    goodsCollectAllBean.setIsObject(false);
                                    goodsCollectAllBean.setEmpty(false);
                                    goodsCollectAllBean.setGoodsCollectArryBean(goodsCollectArryBean);
                                    goodsCount = goodsCollectArryBean.getRoot().getCount();
                                } catch (JsonSyntaxException e1) {
                                    goodsCount = 0;
                                    goodsCollectAllBean.setEmpty(true);
                                }
                            }*/
                            }
                                if (isGetCount) {
                                    intent.putExtra(ActionUtil.ACTION_GOODS_COLLECTCOUNT, goodsCount);
                                    activity.sendBroadcast(intent);
                                } else {
                                    handler.obtainMessage(RequestCodeConstant.REQUEST_GOODS_COLLECT, goodsCollectAllBean).sendToTarget();
                                }
                        }

                        @Override
                        public void failReturn() {
                            if (isGetCount) {
                                intent.putExtra(ActionUtil.ACTION_GOODS_COLLECTCOUNT, -1);
                                activity.sendBroadcast(intent);
                            } else {
                                handler.obtainMessage(RequestCodeConstant.REQUEST_GOODS_COLLECT, null).sendToTarget();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 添加商品收藏
     *
     * @param activity
     * @param handler
     * @param gid
     */
    public static void addProductCollect(final Activity activity, final Handler handler, final int gid) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                //hash
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "add");
                map.put("hash", hash);
                map.put("gid", gid + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.GOODS_COLLECT_URL, RequestCodeConstant.REQUEST_ADDPRODUCTCOLLECT, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("添加商品收藏", result + "************");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_ADDPRODUCTCOLLECT, GsonUtil.json2Obj(result, AddCollectBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_ADDPRODUCTCOLLECT, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 删除商品收藏
     *
     * @param activity
     * @param handler
     * @param gid      商品id
     */
    public static void deleteGoodsCollect(final Activity activity, final Handler handler, final int gid) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                //hash
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "del");
                map.put("hash", hash);
                map.put("gid", gid + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.GOODS_COLLECT_URL, RequestCodeConstant.REQUEST_DELETE_GOODS_COLLECT, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("删除商品收藏", result + "**********");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELETE_GOODS_COLLECT, GsonUtil.json2Obj(result, DeleteCollectBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELETE_GOODS_COLLECT, null).sendToTarget();
                    }
                });
            }
        });
    }

    /**
     * 查看店铺收藏
     *
     * @param activity
     * @param handler
     * @param limit
     * @param page
     */
    public static void getShopCollect(final Activity activity, final Handler handler, final int limit, final int page, final boolean isGetCount) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                //hash
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "fb");
                map.put("hash", hash);
                map.put("limit", limit + "");
                map.put("page", page + "");
                map.put("ctype", "1");
                final Intent intent = new Intent(ActionUtil.ACTION_SHOP_COLLECTCOUNT);
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.SHOP_COLLECT_URL, RequestCodeConstant.REQUEST_SHOP_COLLECT, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        //店铺收藏数量
                        int shopCount;
                        CommonUtil.printE("查看店铺收藏", result + "**********");
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(ShopCollectRootBean.class, new ShopCollectRootBeanDeserializer());
                        builder.registerTypeAdapter(ShopCollectOtherInfoBean.class, new ShopCollectOtherDeserializer());
                        builder.registerTypeAdapter(ShopCollectItemsBean.class, new ShopCollectItemsBeanDeserializer());
                        Gson gson = builder.create();
                        ShopCollectRoot shopCollectRootBean = gson.fromJson(result, ShopCollectRoot.class);
                        ShopCollectAllBean shopCollectAllBean = new ShopCollectAllBean();
                        ShopCollectItemsBean items = shopCollectRootBean.root.items;
                        if (items == null) {
                            shopCollectAllBean.setEmpty(true);
                            shopCount = 0;
                        } else {
                            List<ShopCollectDataBean> data = items.data;
                            shopCount = shopCollectRootBean.root.otherinfo.count;
                            if (data.size() == 1) {
                                shopCollectAllBean.setObject(true);
                                ShopCollectObjBean shopCollectObjBean = new ShopCollectObjBean();
                                ShopCollectObjBean.RootBean rootBean = new ShopCollectObjBean.RootBean();
                                ShopCollectObjBean.RootBean.ItemsBean itemsBean = new ShopCollectObjBean.RootBean.ItemsBean();
                                ShopCollectObjBean.RootBean.OtherinfoBean otherinfoBean = new ShopCollectObjBean.RootBean.OtherinfoBean();
                                ShopCollectItemBean shopCollectItemBean = new ShopCollectItemBean();
                                ShopCollectDataBean shopCollectDataBean = data.get(0);
                                //data
                                shopCollectItemBean.setAdwords(shopCollectDataBean.adwords);
                                shopCollectItemBean.setId(shopCollectDataBean.id);
                                shopCollectItemBean.setItem_counts(shopCollectDataBean.item_counts);
                                shopCollectItemBean.setName(shopCollectDataBean.name);
                                shopCollectItemBean.setMobile_url(shopCollectDataBean.mobile_url);
                                shopCollectItemBean.setShop_counts(shopCollectDataBean.shop_counts);
                                if (shopCollectDataBean.logo.content == null) {
                                    shopCollectItemBean.setLogo(null);
                                } else {
                                    Logo logo = new Logo();
                                    logo.content = shopCollectDataBean.logo.content;
                                    logo.modifyTime = shopCollectDataBean.logo.modifyTime;
                                    shopCollectItemBean.setLogo(logo);
                                }
                                //otherinfoBean
                                otherinfoBean.setCount(shopCollectRootBean.root.otherinfo.count);
                                otherinfoBean.setHas_more_items(shopCollectRootBean.root.otherinfo.has_more_items);

                                itemsBean.setData(shopCollectItemBean);
                                rootBean.setItems(itemsBean);
                                rootBean.setOtherinfo(otherinfoBean);
                                shopCollectObjBean.setRoot(rootBean);
                                shopCollectAllBean.setGoodsCollectObjBean(shopCollectObjBean);
                            } else {
                                shopCollectAllBean.setObject(false);
                                ShopCollectArryBean shopCollectArryBean = new ShopCollectArryBean();
                                ShopCollectArryBean.RootBean rootBean = new ShopCollectArryBean.RootBean();
                                ShopCollectArryBean.RootBean.ItemsBean itemsBean = new ShopCollectArryBean.RootBean.ItemsBean();
                                ShopCollectArryBean.RootBean.OtherinfoBean otherinfoBean = new ShopCollectArryBean.RootBean.OtherinfoBean();
                                ArrayList<ShopCollectItemBean> list = new ArrayList<>();
                                for (int i = 0; i < data.size(); i++) {
                                    ShopCollectItemBean shopCollectItemBean = new ShopCollectItemBean();
                                    ShopCollectDataBean shopCollectDataBean = data.get(i);
                                    //data
                                    shopCollectItemBean.setAdwords(shopCollectDataBean.adwords);
                                    shopCollectItemBean.setId(shopCollectDataBean.id);
                                    shopCollectItemBean.setItem_counts(shopCollectDataBean.item_counts);
                                    shopCollectItemBean.setName(shopCollectDataBean.name);
                                    shopCollectItemBean.setMobile_url(shopCollectDataBean.mobile_url);
                                    shopCollectItemBean.setShop_counts(shopCollectDataBean.shop_counts);
                                    if (shopCollectDataBean.logo.content == null) {
                                        shopCollectItemBean.setLogo(null);
                                    } else {
                                        Logo logo = new Logo();
                                        logo.content = shopCollectDataBean.logo.content;
                                        logo.modifyTime = shopCollectDataBean.logo.modifyTime;
                                        shopCollectItemBean.setLogo(logo);
                                    }
                                    list.add(shopCollectItemBean);
                                }
                                itemsBean.setData(list);
                                //otherinfoBean
                                otherinfoBean.setCount(shopCollectRootBean.root.otherinfo.count);
                                otherinfoBean.setHas_more_items(shopCollectRootBean.root.otherinfo.has_more_items);
                                shopCollectAllBean.setEmpty(false);
                                rootBean.setItems(itemsBean);
                                rootBean.setOtherinfo(otherinfoBean);
                                shopCollectArryBean.setRoot(rootBean);
                                shopCollectAllBean.setGoodsCollectArryBean(shopCollectArryBean);
                            }
                        }
                        if (isGetCount) {
                            intent.putExtra(ActionUtil.ACTION_SHOP_COLLECTCOUNT, shopCount);
                            activity.sendBroadcast(intent);
                        } else {
                            handler.obtainMessage(RequestCodeConstant.REQUEST_SHOP_COLLECT, shopCollectAllBean).sendToTarget();
                        }
                    }

                    @Override
                    public void failReturn() {
                        if (isGetCount) {
                            intent.putExtra(ActionUtil.ACTION_SHOP_COLLECTCOUNT, -1);
                            activity.sendBroadcast(intent);
                        } else {
                            handler.obtainMessage(RequestCodeConstant.REQUEST_SHOP_COLLECT, null).sendToTarget();
                        }
                    }
                });
            }
        });
    }

    /**
     * 删除商品收藏
     *
     * @param activity
     * @param handler
     * @param mid      店铺id
     */
    public static void deleteShopCollect(final Activity activity, final Handler handler, final int mid) {
        ThreadPoolManager.getInstance().createNetThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                //hash
                String hash = WinguoAccountDataMgr.getHashCommon(activity);
                map.put("a", "del_ad");
                map.put("hash", hash);
                map.put("mid", mid + "");
                map.put("ctype", "1");
                MyOkHttpUtils2.post(WinguoAccountConfig.getDOMAIN() + UrlConstant.SHOP_COLLECT_URL, RequestCodeConstant.REQUEST_DELETE_SHOP_COLLECT, map, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printE("删除商品收藏", result + "**********");
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELETE_SHOP_COLLECT, GsonUtil.json2Obj(result, DeleteCollectBean.class)).sendToTarget();
                    }

                    @Override
                    public void failReturn() {
                        handler.obtainMessage(RequestCodeConstant.REQUEST_DELETE_SHOP_COLLECT, null).sendToTarget();
                    }
                });
            }
        });
    }


}
