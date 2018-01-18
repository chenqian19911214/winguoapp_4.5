package com.winguo.utils;

import android.os.Environment;

/**
 * 常量
 * Created by admin on 2016/12/1.
 */

public class Constants {

    public static String isGuide = "isGuide";//是否引导 关键字sharePreference存储

    public static String[] lifeServices = {"公交","地铁","火车","旅游","音乐","菜谱","星座","股票"};

    /**
     * 手机归属地查询api  mob
     */
    public static final String URL = "http://apicloud.mob.com/v1/mobile/address/query?key=1a341cbdda9b2&phone=";
    public static final String key = "1a341cbdda9b2";

    //App获取权限 是否打开（主要是打电话 定位 短信）
    public static final int REQUEST_CODE_PERMISSIONS = 0x456;
    public static final int REQUEST_CODE_PERMISSIONS_SEARCH = 0x4235;
    public static final int REQUEST_CODE_PERMISSIONS_CALL_PHONE = 0x4275;
    public static final int REQUEST_CODE_PERMISSIONS_SEND_SMS = 0x4265;
    public static final int REQUEST_CODE_PERMISSIONS_WRITE_SD = 0x4785;
    public static final int REQUEST_CODE_PERMISSIONS_VOICE = 0x4796;
    public static final int REQUEST_CODE_PERMISSIONS_TEL = 0x4590;
    public static final String PERMISSION_CALL = "android.permission.CALL_PHONE";
    public static final String PERMISSION_SEND_SMS = "android.permission.SEND_SMS";
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String PERMISSION_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    public static final String NO_NET_STATU= "NO_NET_STATU";
    public static final String NET_STATU_WIFI= "NET_STATU_WIFI";
    public static final String NET_STATU_MOBILE= "NET_STATU_MOBILE";

    public static final String OPEN_LEFT_MENU= "OPEN_LEFT_MENU";
    public static final String AUTO_REFRESH= "toRefresh";
    public static final String LOGIN_SUCCESS= "landscape";
    public static final String QUIT_SUCCESS= "quitLogin";
    public static final String OPEN_SUCCESS= "openSuc";
    public static final String AUTO_LOGIN= "autologinerror";
    public static final String MODIFY_PHOTO= "modifyPhoto";
    public static final String MODIFY_NICK= "modifyNick";
    public static final String WX_LONING_SUCCESS= "wxloginsuccess";
    public static final String BALANCE_DETAIL= "balanceDetail";
    public static final String PRESTORE_DETAIL= "prestoreDetail";


    //手机生产商
    public static final String BUILDE_PRODUCT_MIUI = "Xiaomi";
    public static final String BUILDE_PRODUCT_MEIZU = "Meizu";
    public static final String BUILDE_PRODUCT_HUAWEI = "Huawei";


    /**
     * logo sd的根目录
     */
    public static String logoSDPath = Environment.getExternalStorageDirectory()+"/"+"logo.png";
    public static String dbSDPath = Environment.getExternalStorageDirectory()+"/"+"telocation.db";
    public static String newsurl = "http://v.juhe.cn/toutiao/index?type=yule&key=b96c8d123161d40e2ef755bcb87a0227";


    public static  String assetPermissionURL = "file:///android_asset/权限使用协议.html";
    public static  String assetUserURL ="file:///android_asset/用户使用协议.html";
    public static  String assetStudentURL ="file:///android_asset/创客协议.html";
    public static  String assetRechargeURL ="file:///android_asset/充值协议.html";


    //登录入口
    public final static  String LOGIN_WAY_MEASSGE = "LOGIN_WAY_MEASSGE";
    public final static  String LOGIN_WAY_WALLET = "LOGIN_WAY_WALLET";
    public final static  String LOGIN_WAY_BANK_CARD = "LOGIN_WAY_BANK_CARD";
    public final static  String LOGIN_WAY_PAY_PWD_MOD = "LOGIN_WAY_PAY_PWD_MOD";
    public final static  String LOGIN_WAY_QR = "LOGIN_WAY_QR";
    public final static  String LOGIN_WAY_MY_PARTNER = "LOGIN_WAY_MY_PARTNER";
    public final static  String LOGIN_WAY_FEEDBACK = "LOGIN_WAY_FEEDBACK";
    public final static  String LOGIN_WAY_OPEN_SPACE = "LOGIN_WAY_OPEN_SPACE";
    public final static  String LOGIN_WAY_CART = "LOGIN_WAY_CART";
    public final static  String LOGIN_WAY_HISTORY_LOG = "LOGIN_WAY_HISTORY_LOG";
    public final static  String LOGIN_STUDENT_CREATION = "LOGIN_STUDENT_CREATION";

    public final static  String LOGIN_WAY_ALL_ORDER= "LOGIN_WAY_ALL_ORDER";
    public final static  String LOGIN_WAY_WAIT_PAY= "LOGIN_WAY_WAIT_PAY";
    public final static  String LOGIN_WAY_WAIT_DELIVER_GOODS= "LOGIN_WAY_WAIT_DELIVER_GOODS";
    public final static  String LOGIN_WAY_WAIT_TAKE_DELIVER_GOODS= "LOGIN_WAY_WAIT_TAKE_DELIVER_GOODS";
    public final static  String LOGIN_WAY_WAIT_EVALUATE= "LOGIN_WAY_WAIT_EVALUATE";

    public final static  String LOGIN_WAY_GOOD_ATTENTION= "LOGIN_WAY_GOOD_ATTENTION";
    public final static  String LOGIN_WAY_STORE_ATTENTION= "LOGIN_WAY_STORE_ATTENTION";
    public final static  String LOGIN_SETTING= "LOGIN_SETTING";
    public final static  String ACCOUNT_SAFE= "ACCOUNT_SAFE";
    public final static  String WXPAY_RESULT= "WXPayResultBroadcastReceiver";

    public final static  String STUDENT_CREATE_RECHARGE_FLAG= "student_creation_action";

    //实体店下单页面跳转到填写联系人的页面的结果码
    public final static  int PHYSICAL_RESULT_CODE= 999;
    //实体店下单页面跳转到填写联系人的页面的请求码
    public final static  int PHYSICAL_REQUEST_CODE= 9990;
    public final static  String NEARBY_STORE_HOME_CART= "NEARBY_STORE_HOME_CART";
    public final static  String NEARBY_STORE_SHOP_SPEC= "NEARBY_STORE_SHOP_SPEC";

    public final static  String APP_ID_WEIXIN_LOGIN= "wx8e9301cd64ba6263";
    public final static  String APP_KEY_BUGLY= "469d979492";

    //手机号绑定、修改
    public final static  String BIND_PHONE= "bindPhone";
    public final static  String MODIFY_PHONE= "modifPhone";
    public final static  String IS_BIND= "isBindPhone";
    //设置密码
    public final static  String IS_SET_PWD= "isSetPwd";


    //商品分类
    public final static  String TYPE_WINE= "中外名酒";
    public final static  String TYPE_CLOTHES= "服饰鞋包";
    public final static  String TYPE_DAY_USE= "家居日用";
    public final static  String TYPE_HOUSE_ELE= "家用电器";
    public final static  String TYPE_SNACK= "零食年货";
    public final static  String TYPE_BEAUTY= "美妆护肤";

}
