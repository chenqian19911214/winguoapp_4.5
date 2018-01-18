package com.winguo.utils;

/**
 * @author hcpai
 * @desc 网络请求码常量
 */
public class RequestCodeConstant {
    /**
     * 发送消息的消息码
     */
    public static final int BANNER_MAGESS = -11;
    /**
     * 新闻的消息码
     */
    public static final int NEWS_DATA = -25;
    /**
     * 网络新闻消息的消息码
     */
    public static final int NEWS_MAGESS = -22;
    //开启Activity时使用有返回参数的请求码
    public static final int FIRST_ADD_ADDRESS = 77;
    //加入购物车成功
    public static final int CART_REQUEST_SUCCESS_CODE = 0;
    //放入购物车失败
    public static final int CART_REQUEST_ERROR_CODE = -1;
    //该规格的商品已经下架
    public static final int CART_REQUEST_ERROR_SOLD_OUT_CODE = -2;
    //下单成功
    public static final int CONFIRM_ORDER_SUCCESS_CODE = 1;
    //购物车中无商品数据
    public static final int CONFIRM_ORDER_ERROR_NO_GOODS_CODE = -1;
    //下单失败
    public static final int CONFIRM_ORDER_ERROR_CODE = -2;
    //渠道商标识符验证失败
    public static final int CONFIRM_ORDER_ERROR_SIGN_CODE = -200;

    /**
     * 获取公钥的请求码
     */
    public static final int REQUEST_PUBLIC_KEY = 101;
    /**
     * 获取首页广告位数据的请求码
     */
    public static final int REQUEST_BANNER_CODE = 102;
    /**
     * 网络新闻数据的请求码
     */
    public static final int REQUEST_NEWS_CODE = 103;
    /**
     * 广告位数据获取失败
     */
    public static final int REQUEST_BANNER_CODE_FALSE = 2000;


    //查看订单列表的请求标记
    public static final int REQUEST_SEARCH_ORDER = 800;

    //查看订单详情的请求标记
    public static final int REQUEST_SEARCHORDER_DETAIL = 801;

    //获取店铺详情的请求标记
    public static final int REQUEST_GETSHOPDETAIL = 802;
    //查看商品收藏的请求标记
    public static final int REQUEST_GOODS_COLLECT = 803;
    //添加商品收藏的请求标记
    public static final int REQUEST_ADDPRODUCTCOLLECT = 804;
    //删除商品收藏
    public static final int REQUEST_DELETE_GOODS_COLLECT = 805;
    //查看商品收藏的请求标记
    public static final int REQUEST_SHOP_COLLECT = 806;
    //删除店铺收藏
    public static final int REQUEST_DELETE_SHOP_COLLECT = 807;
    //获取收件人信息列表的请求标记
    public static final int REQUEST_GET_ADDRESS_LIST = 808;
    //删除地址的请求标记
    public static final int REQUEST_DELETEADDRESS = 809;
    //更新地址的请求标记
    public static final int REQUEST_UPDATEADDRESS = 810;
    //获取省的请求标记
    public static final int REQUEST_GETPROVINCE = 811;
    //获取市的请求标记
    public static final int REQUEST_GETCITY = 812;
    //获取县的请求标记
    public static final int REQUEST_GETAREA = 813;
    //获取镇的请求标记
    public static final int REQUEST_GETTOWN = 814;
    //获取一级分类的请求标记
    public static final int REQUEST_GET_FIRST_CATEGORIES = 815;
    //获取二级分类的请求标记
    public static final int REQUEST_GET_SECOND_CATEGORIES = 816;

    //商品列表
    public static final int PRODUCT_LIST_REQUEST_TAG = 503;
    //省市数据
    public static final int PRODUCT_LISTCITY_REQUEST_TAG = 514;
    //商品列表中的分类名称
    public static final int PRODUCT_LIST_CLASSIFY_REQUEST_TAG = 515;
    //获取商品评论的请求标记
    public static final int REQUEST_PRODUCT_COMMENT = 428;
    //商品是否收藏
    public static final int PRODUCT_COLLECT_REQUEST_TAG = 516;
    //商品详情
    public static final int PRODUCT_REQUEST_TAG = 504;
    //店铺首页
    public static final int SHOP_REQUEST_TAG = 505;
    //加入购物车
    public static final int CART_REQUEST_TAG = 506;
    //获取购物车数据
    public static final int REQUEST_SEEK_CART = 507;
    //删除购物车数据
    public static final int REQUEST_DELETE_CART = 508;
    //修改购物车数量
    public static final int REQUEST_UPDATE_CART = 509;
    //查询收件人信息
    public static final int SEEK_USERINFO_REQUEST_TAG = 510;
    //查询配送方式
    public static final int DISPATCH_METHOD_REQUEST_TAG = 511;
    //确认订单
    public static final int CONFIRM_ORDER_REQUEST_TAG = 512;
    //确认支付
    public static final int CONFIRM_PAY_REQUEST_TAG = 513;
    //支付宝支付的数据
    public static final int PAY_REQUEST_TAG = 518;
    //支付宝支付结果数据
    public static final int PAY_RESULT_REQUEST_TAG = 519;
    //网银支付结果数据
    public static final int UNIONPAY_PAY_REQUEST_TAG = 520;
    //获取实体店购物车数据
    public static final int REQUEST_SEEK_PHYSICAL_CART = 521;
    //修改实体店购物车数据数量
    public static final int REQUEST_UPDATE_PHYSICAL_CART = 522;
    //获取实体店下单
    public static final int REQUEST_SUBMIT_PHYSICAL_ORDER = 523;
    //查看订单的数量请求标记
    public static final int REQUEST_ORDERCOUNT = 815;
    //查看浏览记录的请求标记
    public static final int REQUEST_HISTORYLOG_HAS = 816;
    //删除浏览记录的请求标记
    public static final int REQUEST_DELHISTORYLOG_HAS = 817;
    //获取好友动态的请求标记
    public static final int REQUEST_FRIEND_DYNAMIC = 818;
    //确认收货的请求标记
    public static final int REQUEST_CONFIRM_RECEIVE = 819;
    //关键字
    public static final int SEARCH_WORDSlIST_REQUEST_TAG = 502;
    //根据个人信息查询附近商家的请求标记
    public static final int NEARBY_REQUEST_TAG = 820;
    //根据个人信息查询没有附近商家的
    public static final int NEARBY_REQUEST_NO_STORE_TAG = 801;
    //首页主题活动请求标记
    public static final int GOODS_THEME_TAG = 521;
    //根据个人信息查询附近商家主题活动的请求标记
    public static final int NEARBY_TOPIC_REQUEST_TAG = 821;
    //附近商家信息的请求标记
    public static final int NEARBY_SHOP_INFO_REQUEST_TAG = 822;
    //根据商家id获取附近商家的请求标记
    public static final int NEARBY_SHOP_BYNEAR_REQUEST_TAG = 823;

    //微信支付的数据
    public static final int WX_PAY_REQUEST_TAG = 824;

    //余额充值生成订单
    public static final int RECHARGE_ORDER_TAG = 825;
}
