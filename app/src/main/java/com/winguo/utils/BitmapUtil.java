package com.winguo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.guobi.account.WinguoAccountConfig;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtil {

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // 给定的BitmapFactory设置解码的参数
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // 压缩完后便可以将inJustDecodeBounds设置为false了。
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 指定图片的缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 原始图片的宽、高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

//		if (height > reqHeight || width > reqWidth) {
//			//这里有两种压缩方式，可供选择。
//			/**
//			 * 压缩方式二
//			 */
//			// final int halfHeight = height / 2;
//			// final int halfWidth = width / 2;
//			// while ((halfHeight / inSampleSize) > reqHeight
//			// && (halfWidth / inSampleSize) > reqWidth) {
//			// inSampleSize *= 2;
//			// }
//
        /**
         * 压缩方式一
         */
        // 计算压缩的比例：分为宽高比例
        final int heightRatio = Math.round((float) height
                / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

//		}

        return inSampleSize;
    }


    public static Drawable bitMapToDrawable(Bitmap bitmap){
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }


    /**
     * 保存二维码 分享
     * @param mContext
     * @param userIcoUrl
     * @param accountName
     * @param screenWidth
     * @param defBitmap
     */
    public static void saveUserShareQR(final Context mContext, final String userIcoUrl, final String accountName, final int screenWidth, final Bitmap defBitmap) {
        //
        final FileUtil util = new FileUtil(mContext);
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                //获取用户头像
                final Bitmap iconurlBitmap = MyOkHttpUtils.getBitmapFormUrl(userIcoUrl);
                String requsturl = null;
                if (accountName != null) {
                    requsturl = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.QRCODE + accountName;//获取推广二维码的地址
                }
                if (requsturl != null) {
                    MyOkHttpUtils.post(requsturl, 0, null, new IStringCallBack() {
                        @Override
                        public int stringReturn(String result) {
                            Bitmap qr = null;
                            JSONTokener jsonTokener = new JSONTokener(result);
                            try {
                                JSONObject jsonObject = new JSONObject(jsonTokener);
                                String status = jsonObject.getString("status");
                                String text = jsonObject.getString("text");
                                String shopurl = jsonObject.getString("url");
                                String code = jsonObject.getString("code");
                                if (status.equals("success")) {
                                    if (iconurlBitmap != null) {
                                        qr = QRCode.createQRCodeWithLogo5(shopurl, (int) (screenWidth * 0.7), iconurlBitmap);//生成二维码
                                    } else {
                                        qr =   QRCode.createQRCodeWithLogo5(shopurl, (int) (screenWidth * 0.7), defBitmap);//生成二维码
                                    }
                                }

                            } catch (JSONException e) {//wing.mobileShopAddr
                                // qr = QRCode.createQRCodeWithLogo5(wing.mobileShopAddr, (int) (screenWidth * 0.7), iconurlBitmap);//生成二维码
                                e.printStackTrace();
                            }finally {
                                //保存 生成的二维码
                                util.savaBitmap("userQR",qr);
                            }

                            return 0;
                        }

                        @Override
                        public void exceptionMessage(String message) {

                        }
                    });
                }

            }
        });

    }
    /**
     * 保存二维码 分享
     * @param mContext
     * @param accountName
     * @param screenWidth
     */
    public static void saveUserShareQR2(final Context mContext, final String accountName, final int screenWidth) {
        //
        final FileUtil util = new FileUtil(mContext);
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {

                String requsturl = null;
                if (accountName != null) {
                    requsturl = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.QRCODE + accountName;//获取推广二维码的地址
                }
                if (requsturl != null) {
                    MyOkHttpUtils.post(requsturl, 0, null, new IStringCallBack() {
                        @Override
                        public int stringReturn(String result) {
                            Bitmap qr = null;
                            JSONTokener jsonTokener = new JSONTokener(result);
                            try {
                                JSONObject jsonObject = new JSONObject(jsonTokener);
                                String status = jsonObject.getString("status");
                                String text = jsonObject.getString("text");
                                String shopurl = jsonObject.getString("url");
                                String code = jsonObject.getString("code");
                                if (status.equals("success")) {
                                   //QRCode.createQRCode(shopurl, (int) (screenWidth * 0.7));
                                    String  shareshopurl=shopurl.substring(0,shopurl.indexOf("?"));
                                    shareshopurl="http://"+shareshopurl;
                                    qr = QRCode.createQRCode(shareshopurl, (int) (screenWidth * 0.4));
                                }
                            } catch (JSONException e) {//wing.mobileShopAddr
                                // qr = QRCode.createQRCodeWithLogo5(wing.mobileShopAddr, (int) (screenWidth * 0.7), iconurlBitmap);//生成二维码
                                e.printStackTrace();
                            }finally {
                                //保存 生成的二维码
                                util.savaBitmap("shareQRClode.png",qr);
                            }
                            return 0;
                        }

                        @Override
                        public void exceptionMessage(String message) {

                        }
                    });
                }

            }
        });

    }





}