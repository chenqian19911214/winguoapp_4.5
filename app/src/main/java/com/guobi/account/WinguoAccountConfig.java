package com.guobi.account;

/**
 * 问果后台的域名与接口地址
 * Created by chenq on 2017/1/3.
 */
public class WinguoAccountConfig {

    public static String getDOMAIN() {
        if (DEBUG) {
            return TEST_DOMAIN;
        } else {
            return DOMAIN;
        }
    }

    private static boolean DEBUG = false;

    public static void setDEBUG(boolean debug) {
        DEBUG = debug;
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    /**
     * 正式域名
     */
    private static final String DOMAIN = "http://api.winguo.com";
    /**
     * 新版注册页面接口程序 (先取得公钥)
     */

    public static final String REGISTER_NEW = "/data/register?a=regist_new";
    /**
     * 测试域名
     */
    public static final String TEST_DOMAIN = "http://192.168.0.222";
    //private static final String TEST_DOMAIN = "http://apitest.winguo.com";

    /**
     * 获取公钥接口
     */
    public static final String GETKEY = "/data/index?a=getkey";

    /**
     * 获取银行列表
     */
    public static final String GET_BANKLIST = "/data/index?a=getBank";
    /**
     * 获取省份列表
     */
    public static final String GET_BANK_PROVINCE_LIST = "/data/index?a=getProvince";
    /**
     * 获取城市列表
     */
    public static final String GET_BANK_CITY_LIST = "/data/index?a=getCity";
    /**
     * 获取支行列表
     */
    public static final String GET_BANK_BRANCHE_LIST = "/data/index?a=getBankBranches";
    /**
     * 注册接口
     */
    public static final String REGISTER = "/data/register?a=regist";

    /**
     * 手机注册接口（自动注册接口）
     */
    public static final String AUTO_REGISTER = "/data/register?a=regist_by_mobile";
    /**
     * 手机开通空间站接口
     */
    public static final String OPEN_SPACE_STATION = "/data/register?a=open_free_shop";

    /**
     * 登陆接口
     */
    public static final String LOGIN = "/data/login?";

    /**
     * 获取推广二维码
     */
    public static final String QRCODE = "/data/user/qrcode?a=code&account=";
    /**
     * 登出接口
     */
    public static final String LOGOUT = "/data/logout?";

    /**
     * 获取短信验证码接口
     */
    public static final String GETMOBILECODE = "/data/register?a=getcode";
    /**
     * 获取登录 短信验证码接口
     */
    public static final String LOGIN_CHECKCODE = "/data/user/smslogin?a=sms";
    /**
     * 获取创客金额
     */
    public static final String RECHARGE_NUMBER = "/data/user/info?a=registercash";
    /**
     * 获取验证码登录
     */
    public static final String LOGIN_BYCODE = "/data/user/smslogin?a=login";

    /**
     * 验证短信验证码接口
     */
    public static final String VERIFY = "/data/register?a=_regist";

    /**
     * 查看用户信息接口
     */
    public static final String GETUSERINFO = "/data/userinfo?a=info";
    /**
     * 查询可提现余额明细
     */
    public static final String CASH_COUPON_DETIAL = "/data/user/info?a=cash_coupon";
    /**
     * 查询现金券明细
     */
    public static final String PRESTORE_DETIAL = "/data/user/info?a=cash_credit";
    /**
     * 查询预存明细
     */
    public static final String BALANCE_DETIAL = "/data/account?a=detail";
    /**
     * 修改用户头像
     */
    public static final String MODIFY_HEAD = "/data/user/uphead?";
    /**
     * 绑定推荐人手机号
     */
    public static final String BINDING_REFEREE_TEL = "/data/user/info?a=bandreferee";
    /**
     * 修改用户头像
     */
    public static final String MODIFY_NICKNAME = "/data/user/info?a=nickname";
    /**
     * 查看用户信息接口
     */
    public static final String WEIXIN_LOGIN = "/data/user/wxapp?";
    /**
     * 申请提现接口
     */
    public static final String WITHDRAW_CASH = "/data/account?a=cash";

    /**
     * 绑定手机号码接口
     */
    public static final String BINDMOBILE = "/data/userinfo?a=bindMobile";

    /**
     * 查询余额接口
     */
    public static final String GET_BALANCE = "/data/account?a=get_jpy";
    /**
     * 新查询余额 现金券 创客基金接口
     */
    public static final String GET_BALANCE_CASH = "/data/user/info?a=balance";
    /**
     * 在线支付
     */
    public static final String GET_OnLinePay = "/data/cart?a=onlinePay";

    /**
     * 查询果币
     */
    public static final String GET_CURRENCY = "/data/account?a=get_winguo";

    /**
     * 查询银行卡列表
     */
    public static final String GET_BANKCARDLIST = "/data/account?a=getBankList";

    /**
     * 查询银行卡信息
     */
    public static final String GET_BANKCARDDETAIL = "/data/account?a=getBank";


    /**
     * 添加银行卡
     */
    public static final String ADD_BANKCARD = "/data/account?a=addBank";

    /**
     * 删除银行卡
     */
    public static final String DEL_BANKCARD = "/data/account?a=delBank";

    /**
     * 忘记密码后，设置新密码
     */
    public static final String SET_PWD = "/data/remind?a=set_password";

    /**
     * 忘记支付密码后，设置新支付密码
     */
    public static final String SET_PAY_PWD_BY_OLD = "/data/userinfo?a=modifyCash";
    /**
     * 忘记支付密码后，设置新支付密码
     */
    public static final String SET_PAY_PWD = "/data/remind?a=set_cash_password";

    /**
     * 意见反馈
     */
    public static final String FEEDBACK = "/data/feedback?a=regist";
    /**
     * 验证手机号是否注册过
     */
    public static final String PHONE_CHECK = "/data/register?a=checkaccount";
    /**
     * 绑定手机号
     */

    public static final String BINDING_PHONE_NUMBER = "/data/userinfo?a=bindMobile";
    /**
     * 修改密码
     */
    public static final String MODIFY_PWD = "/data/userinfo?a=password";

    /**
     * 测试版密码加密公钥
     */
    public static final String TEST_PWD_MODULE =
            "C11D5B794F43CD7261DC30B7FD3C7508972533C7E1B2FCE20B3E7820FA259494DB396220FA2159A6BC00511E438763FB5F84CAF98180988F8076E8DDA3881BCF";

    public static final String PWD_EXPONENT = "3";

    /**
     * 正式版密码加密公钥
     */
    public static final String PWD_MODULE =
            "B250F0A9B5FCFDD1AC03568E7777960BF84D7A694C2B331488CDAB1D64726875367D55EC4857DAB0B03F7D4E964FF3771E675679E0588657D1732C48C304A699";


    /**
     * 2.0注册
     */
    public static final String NEWREGISTER = "/data/user/register?a=regist";

    // http://api.winguo.com/data/index?a=getPreAppFirstPage
    public static final String getPwdModule() {
        if (DEBUG) {
            return TEST_PWD_MODULE;
        } else {
            return PWD_MODULE;
        }
    }

    public static final String getPwdExponent() {
        return PWD_EXPONENT;
    }
}
