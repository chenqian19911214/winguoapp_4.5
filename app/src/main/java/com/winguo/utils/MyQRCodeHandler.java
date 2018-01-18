package com.winguo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 *
 * Created by chenq on 2018/1/2.
 *
 */

public class MyQRCodeHandler extends Handler {
    private Context context;
    private String status;
    private String text, accountName, requsturl;
    private String shopurl;
    private String code;
    private int screenWidth;
    private ImageView myQRClode;
    private FileUtil fileUtil;
    private   Activity activity;
    private WinguoAccountGeneral wing;

    public MyQRCodeHandler() {

    }
    public  void setQRCodeData(Context context, WinguoAccountGeneral wing, FileUtil fileUtil, ImageView myQRClode){
        this.wing = wing;
        this.accountName = wing.accountName;
        this.myQRClode = myQRClode;
        this.fileUtil = fileUtil;
        this.activity= (Activity) context;
    }
    public MyQRCodeHandler(Context context, WinguoAccountGeneral wing, FileUtil fileUtil, ImageView myQRClode) {
        this.context = context;
        this.wing = wing;
        this.accountName = wing.accountName;
        this.myQRClode = myQRClode;
        this.fileUtil = fileUtil;
        this.activity= (Activity) context;
        this.screenWidth= getScreen();
    }

    /**
     * 获取屏幕 截屏操作
     */
    public void screenshot() {
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + FileUtil.FOLDER_NAME+ File.separator + "screenshot.png";

                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取屏幕分辨率 的宽；
     */
    private int getScreen() {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        return screenWidth;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if (msg.what == 1) {
             /*   if (picByte != null)
                    iconurlBitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);*/
            if (accountName != null) {
                requsturl = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.QRCODE + accountName;//获取推广二维码的地址
            }
            if (requsturl != null) {
                MyOkHttpUtils.post(requsturl, 0, null, new IStringCallBack() {

                    private Bitmap shareqrClodebitmap;

                    @Override
                    public int stringReturn(String result) {
                        JSONTokener jsonTokener = new JSONTokener(result);
                        try {
                            JSONObject jsonObject = new JSONObject(jsonTokener);
                            status = jsonObject.getString("status");
                            text = jsonObject.getString("text");
                            shopurl = jsonObject.getString("url");
                            code = jsonObject.getString("code");
                            if (status.equals("success")) {
                                /**
                                 *  去除二维码头像，做缓存
                                 * if (iconurlBitmap != null) {
                                 qrClodebitmap = QRCode.createQRCodeWithLogo5(shopurl, (int) (screenWidth * 0.7), iconurlBitmap);
                                 } else {
                                 BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.app_icon);
                                 Bitmap bitmap = bd.getBitmap();
                                 qrClodebitmap = QRCode.createQRCodeWithLogo5(shopurl, (int) (screenWidth * 0.7), bitmap);
                                 }*/
                                // qrClodebitmap = QRCode.createQRCode(shopurl, (int) (screenWidth * 0.7));
                                String shareshopurl = shopurl.substring(0, shopurl.indexOf("?"));
                                shareshopurl = "http://" + shareshopurl;
                                shareqrClodebitmap = QRCode.createQRCode(shareshopurl, (int) (screenWidth * 0.4));

                            } else {
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {//wing.mobileShopAddr
                            // qrClodebitmap = QRCode.createQRCodeWithLogo5(wing.mobileShopAddr, (int) (screenWidth * 0.7), iconurlBitmap);//生成二维码
                            e.printStackTrace();
                        }
                        //   progressBar.setVisibility(View.GONE);
                        myQRClode.setImageBitmap(shareqrClodebitmap);
                        // fileUtil.savaBitmap("myQRClode.png", qrClodebitmap);
                        fileUtil.savaBitmap("shareQRClode.png", shareqrClodebitmap);
                        return 0;
                    }
                    @Override
                    public void exceptionMessage(String message) {

                    }
                });
            }
        }
    }


    public void shareShow(String platform) {
        Platform plat = ShareSDK.getPlatform(platform);
        // chenqian  MobSharedUtils.showShopUrlShare(plat.getName(), getApplicationContext(), "http://img.mp.itc.cn/upload/20170422/2c8a1531d7cf4fc2a9c9c467f9bc91fd_th.jpeg", "chenqian");
        MobSharedUtils.showQRCodeShare(plat.getName(), context, wing.mobileShopAddr, "扫描此二维码，注册空间站！我带你飞吧");
    }
}
