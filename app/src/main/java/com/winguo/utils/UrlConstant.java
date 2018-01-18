package com.winguo.utils;

import com.guobi.account.WinguoAccountConfig;

/**
 * @author hcpai
 * @desc 请求接口的静态常量url地址
 */
public class UrlConstant {
    /**
     * 是否是调试模式，默认是true
     */
    public static final boolean IS_DEBUG_MODE = true;
    /**
     * 基础域名
     * http://api.winguo.com 这是正式版
     * "http://192.168.0.222:80" 开发版
     */
    //public static String BASE_URL = "http://192.168.0.222";
    //public static String BASE_URL ="http://apitest.winguo.com";
    //public static String BASE_URL = "http://api.winguo.com";
    public static String BASE_URL = WinguoAccountConfig.getDOMAIN();

    /**
     * 获取公钥
     */
    public static final String GETKEY_URL = "/data/index?a=getkey";
    /**
     * 请求首页广告位数据
     */
    public static String BANNER_URL = "http://api.winguo.com/data/index?a=getPreAppFirstPage";

    public static String NEWS_URL = "http://v.juhe.cn/toutiao/index?type=yule&key=b96c8d123161d40e2ef755bcb87a0227";

    /**
     * 每日 随机商品10条
     */
    public static String TODAY_SHOP = WinguoAccountConfig.getDOMAIN() + "/data/goods?a=randomgoods";
    // public static String TODAY_SHOP = "http://192.168.0.222" + "/data/goods?a=randomgoods";
    /**
     * 每日 随机商品10条
     */
    public static String THEME_ACTIVITY = WinguoAccountConfig.getDOMAIN() + "/data/topic?a=getTopic";
    //public static String THEME_ACTIVITY = "http://192.168.0.222" + "/data/topic?a=getTopic";

    /**
     * 今日商品 未登录
     */
    public static String TODAY_SHOP_NEW_NO_LOGIN = WinguoAccountConfig.getDOMAIN() + "/data/goods/list?a=hotsales";
    /**
     * 今日商品 登录 &account =18300602618
     */
    public static String TODAY_SHOP_NEW_LOGIN = WinguoAccountConfig.getDOMAIN() + "/data/goods/list?a=login_hotsales";
    /**
     * 获取用户标签状态 登录 &account =18300602618
     */
    public static String TODAY_SHOP_USER_LABEL_STATU = WinguoAccountConfig.getDOMAIN() + "/data/user/label?a=labelstatus";
    /**
     * 开启标签 登录 &account =18300602618
     */
    public static String TODAY_SHOP_OPEN_USER_LABEL = WinguoAccountConfig.getDOMAIN() + "/data/user/label?a=openlabel";
    /**
     * 获取伙伴相关数据 登录 &account =18300602618
     */
    public static String MY_FAMILY_PARTNER = WinguoAccountConfig.getDOMAIN() + "/data/partner?a=partnerInfo";


    /**
     * 添加浏览记录
     */
    public static final String ADD_HISTORYLOG_URL = "/data/addHistoryLog?";
    /**
     * 商品评论
     */
    public static final String PRODUCT_COMMENT_URL = "/data/comment";
    /**
     * 用户订单
     */
    public static final String USERORDER_URL = "/data/order";

    /**
     * 商品收藏
     */
    public static final String GOODS_COLLECT_URL = "/data/goods_collect";
    /**
     * 店铺收藏
     */
    public static final String SHOP_COLLECT_URL = "/data/shop";
    /**
     * 用户信息
     */
    public static final String USERINFO_URL = "/data/userinfo";
    /**
     * 更新地址
     */
    public static final String CART_URL = "/data/cart";

    /**
     * 获取公钥
     */
    public static final String INDEX_URL = "/data/index";
    /**
     * 获取浏览记录
     */
    public static final String GET_HISTORYLOG_URL = "/data/getHistoryLog";

    /**
     * 分类商品
     */
    public static final String GOODS_URL = "/data/goods";
    /**
     * 好友动态
     */
    public static final String PARTNER_URL = "/data/partner";
    /**
     * 根据个人信息查询附近商家
     */
    public static final String NEARBY_URL = "http://lbstest.winguo.com/gw.php";
    /**
     * 根据个人信息查询附近商家
     */
    public static final String NEARBY_TOPIC_URL = "/data/topic";

    /**
     * 实体店商品详情
     * &iid=85900
     * iid 商品id
     */
    public static final String STORE_SHOP_DETAIL_URL = WinguoAccountConfig.getDOMAIN() + "/data/entity?a=getEntityItemDetail";
    /**
     * 实体店商品列表
     * &eid=2422&limit=2&page=2
     * eid 实体店id
     */
    public static final String STORE_SHOP_LIST_URL = WinguoAccountConfig.getDOMAIN() + "/data/entity?a=getEntityItems";
    /**
     * 附近 实体店列表
     */
    public static final String NEARBY_STORE_LIST_URL = WinguoAccountConfig.getDOMAIN() + "/data/entity?a=getEntity";
    /**
     * 实体店详情2
     */
    public static final String NEARBY_STORE_DETAIL_URL = WinguoAccountConfig.getDOMAIN() + "/data/entity?a=getEntityDetail";
    /**
     * 实体店详情2
     *http://api.winguo.com/data/dianping?a=shopDetail&shopid=13840738
     */
    public static final String NEARBY_STORE_DETAIL_URL2 = WinguoAccountConfig.getDOMAIN() + "/data/dianping?a=shopDetail";
    /**
     * 添加购物车
     */
    public static final String NEARBY_STORE_ADD_CART = WinguoAccountConfig.getDOMAIN()+ "/data/search/add?";
    /**
     * 购物车列表
     */
    public static final String NEARBY_STORE_CART_LIST = WinguoAccountConfig.getDOMAIN() + "/data/search/add?a=user_gw_select";
    /**
     * 修改购物车商品数量
     */
    public static final String NEARBY_STORE_MODIFY_SHOP_NUMBER = WinguoAccountConfig.getDOMAIN() + "/data/search/add?a=user_gw_num";
    /**
     * 删除购物车商品
     */
    public static final String NEARBY_STORE_DEL_SHOP = WinguoAccountConfig.getDOMAIN() + "/data/search/add?a=user_gw_del";

    /**
     * 附近搜索商品结果
     */
    public static final String NEARBY_SEARCH_GOODS_RESULT = "/data/search/product";

    /**
     * 附近搜索店铺结果
     */
    public static final String NEARBY_SEARCH_SHOP_RESULT = "/data/entity";

    /**
     * 实体店订单列表
     */
    public static final String STORE_ORDER= "/data/search/order";
    /**
     * 余额充值
     */
    public static final String RECHARGEORDER= "/data/account";

}
