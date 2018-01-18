package com.winguo.utils;

import android.content.Context;

import java.io.File;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by admin on 2017/1/20.
 * Mob 分享工具
 */

public class MobSharedUtils {
    /**
     * mob分享实例模板
     * @param platform
     * @param context
     */
    public static void showShare(String platform, Context context) {

        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        //启动分享
        oks.show(context);
    }
    public static void showShopShare(String platform, Context context,String currURL,String currTitle) {

        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(currTitle);//链接title
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(currURL);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(currURL);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");//logo url
         //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (FileUtil.isSdcardAvailable()) {
            oks.setImagePath(Constants.logoSDPath);//确保SDcard下面存在此张图片
        } else {
            oks.setImagePath(context.getCacheDir()+"/"+"logo.png");
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(currURL);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("试着说点什么..");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("问果");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.winguo.com");

        //启动分享
        oks.show(context);
    }

    /**
     * 分享地址二维码
     * @param platform
     * @param context
     * @param shopUrl
     * @param shopName
     */
    public static void showShopUrlShare(String platform, Context context,String shopUrl,String shopName) {
        String userImagURL = "userQR";
        String iconName = userImagURL.substring(userImagURL.lastIndexOf("/") + 1);
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(shopName);//链接title
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(shopUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shopUrl);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");//logo url
         //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (FileUtil.isSdcardAvailable()) {
            oks.setImagePath(FileUtil.mSdRootPath+FileUtil.FOLDER_NAME+File.separator+iconName);//确保SDcard下面存在此张图片
           //  Constants.qrFilesDir = "/data/data/com.winguo/files/qr.png"
           // Constants.qrSDPath = "/storage/emulated/0/qr.png"
        } else {
            oks.setImagePath(FileUtil.mDataRootPath+FileUtil.FOLDER_NAME+File.separator+iconName);
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shopUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("试着说点什么..");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("问果");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.winguo.com");

        //启动分享
        oks.show(context);
    }
    /**
     * 分享二维码
     * */
    public static void showQRCodeShare(String platform, Context context,String currTitle,String currText) {

        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
      //  oks.setTitle(currTitle);//链接title
//        oks.setTitle(currTitle);//链接title
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//        oks.setTitleUrl(currURL);
        // text是分享文本，所有平台都需要这个字段
     //   oks.setText(currText);
//        oks.setText(currText);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");//logo url
        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

        /**
         *     String sdCardPath = Environment.getExternalStorageDirectory().getPath();
         // 图片文件路径
         String filePath = sdCardPath + File.separator + "screenshot.png";
         * */
        if (FileUtil.isSdcardAvailable()) {
            oks.setImagePath(FileUtil.mSdRootPath+FileUtil.FOLDER_NAME+File.separator+"screenshot.png");
        } else {
            oks.setImagePath(FileUtil.mDataRootPath+FileUtil.FOLDER_NAME+File.separator+"screenshot.png");
        }

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
       // oks.setComment("试着说点什么..");
        // site是分享此内容的网站名称，仅在QQ空间使用
       // oks.setSite("问果");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
       // oks.setSiteUrl("http://www.winguo.com");

        //启动分享
        oks.show(context);
    }

    /**
     * 分享QQ好友二维码
     * */
    public static void showQQShare(String platform, Context context,String currTitle,String currText) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    if (FileUtil.isSdcardAvailable()) {
                        paramsToShare.setImagePath(FileUtil.mSdRootPath+FileUtil.FOLDER_NAME+File.separator+"screenshot.png");

                    } else {
                        paramsToShare.setImagePath(FileUtil.mDataRootPath+FileUtil.FOLDER_NAME+File.separator+"screenshot.png");
                    }
                }
            }
        });
        // 启动分享GUI
        oks.show(context);
    }
}
