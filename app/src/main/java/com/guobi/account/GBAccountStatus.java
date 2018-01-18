package com.guobi.account;

/**
 * 状态码
 * Created by chenq on 2017/1/3.
 */
public class GBAccountStatus {
    /**
     * 帐号系统还没有启动，应该调用先startup方法启动
     */
    public static final int not_startup = -1;

    /**
     * 帐号系统正在初始化
     * 处于这种状态表示：
     * 由于第一次启动，或者数据被清除，需要抓取用户的手机号码，并进行自动登录等初始化操作
     * 若处于这种状态，客户端界面应显示：
     * 正在初始化帐号信息，请稍候。。。
     */
    public static final int initializing = 0;

    /**
     * 帐号系统初始化完毕，但用户名等各项信息都是空的
     * 处于未注册，未登录的游客状态
     * 若处于这种状态，客户端界面应显示：
     * 请登录 / 免费注册
     */
    public static final int tourist = 1;

    /**
     * 账户名具备，
     * 但尚未登录（即处于登出状态），故其他信息不具备
     * 若处于这种状态，客户端界面应显示：
     * XXXXXXX 您好！请先登录
     */
    public static final int usr_not_logged_in = 2;


    /**
     * 账户已经处于成功登录状态
     * 各项信息具备
     * 若处于这种状态，客户端界面应显示：
     * XXXXXXX 您好！已登录
     */
    public static final int usr_logged = 4;
}
