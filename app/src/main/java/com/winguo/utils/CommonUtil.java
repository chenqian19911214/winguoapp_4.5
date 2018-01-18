package com.winguo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import com.guobi.account.RSA;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.winguo.app.StartApp;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;


/**
 * @author hcpai
 * @desc 常用工具类
 */
public class CommonUtil {

    /**
     * 加密后的id=test&token=dksks&uuid=hdhfdfh
     *
     * @param userName 传入一个id
     * @return 返回加密后的字符串
     * @throws Exception
     */
    public static String getHashValue(String userName) throws Exception {

        StringBuffer sb = new StringBuffer();
        sb.append("id=");
        sb.append(userName);
        sb.append("&token=");
        sb.append(StartApp.mKey.getToken());
        sb.append("&uuid=");
        sb.append(StartApp.mKey.getUUID());
        return encodeData(sb.toString(), StartApp.mKey.getKey(), "utf-8");

    }
    /**
     * 加密方法
     *
     * @param data      要加密的数据
     * @param publicKey 加密的公钥
     * @param charSet   把String转换成byte的编码格式
     * @return 返回一个加密后的字符串
     * @throws Exception 加密中出现的异常
     */

    public static String encodeData(String data, String publicKey, String charSet) throws Exception {
        CommonUtil.printI("加密前的数据",data);
        CommonUtil.printI("加密需要的公钥",publicKey);
        // 实例化RSA对象
        RSA rsa = new RSA();
        rsa.loadPublicKey(publicKey);
        // 加密
        byte[] contentBytes = rsa.encryptString(rsa.getPublicKey(), data,
                "utf-8");
        // 64转化
        String hashContentString = rsa.encryptBytes64EncoderWithSpit(contentBytes);
        CommonUtil.printI("加密和Base64转码后的数据",hashContentString);
        // URL转码
        return URLEncoder.encode(hashContentString, charSet);
    }
    /**
     * 加密方法
     * @param data 要加密的数据
     * @param publicKey 加密的公钥
     * @param charSet 把String转换成byte的编码格式
     * @return 返回一个加密后的字符串
     * @throws Exception 加密中出现的异常
     */
    public static String encodeData(String data, RSAPublicKey publicKey,String charSet) throws Exception {

        //先进行RSA加密
        byte[] rsaData = RsaUtil.encryptData(publicKey, data, charSet);
        //进行Base64加密
        String base64Data = Base64.encodeToString(rsaData, Base64.DEFAULT);
        //进行URLEncoder转码
        String encodeResult = URLEncoder.encode(base64Data, charSet);
        return encodeResult;

    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static double formatPoint(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 获取全局的context
     * @return
     */
    public static Context getAppContext(){
        return StartApp.getInstance();
    }

    /**
     * 打印内容,VERBOSE级别，2
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void printV(String tag, String msg) {
        if (UrlConstant.IS_DEBUG_MODE) {
            Log.v(tag, msg);
        }
    }

    /**
     * 打印内容，DEBUG级别，3
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void printD(String tag, String msg) {
        if (UrlConstant.IS_DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }

    /**
     * 打印内容，INFO级别，4
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void printI(String tag, String msg) {
        if (UrlConstant.IS_DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }

    /**
     * 打印内容，WARN级别，5
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void printW(String tag, String msg) {
        if (UrlConstant.IS_DEBUG_MODE) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印内容，ERROR级别，6
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void printE(String tag, String msg) {
        if (UrlConstant.IS_DEBUG_MODE) {
            Log.e(tag, msg);
        }
    }
    public static void stateSetting(Activity context,int res){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(res);
        }
    }
}
