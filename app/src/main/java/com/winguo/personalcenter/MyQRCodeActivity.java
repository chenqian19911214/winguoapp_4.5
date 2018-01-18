package com.winguo.personalcenter;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.utils.FileUtil;
import com.winguo.utils.MobSharedUtils;
import com.winguo.utils.MyQRCodeHandler;
import com.winguo.view.SharedPopWindow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MyQRCodeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView myQRClode;
    private FrameLayout qrclide_actionbar_back_id;
    private FrameLayout qr_clide_action_share_id;
    private GBAccountMgr mGMaccountMgr;
    private WinguoAccountGeneral wing;
    private byte[] picByte;
    private ProgressBar progressBar;
    private SharedPopWindow sharedPopWindow;
    private Button scan_ImageView;
    private FrameLayout share_img;
    private TextView qr_username, qr_Telephone;
    private MyQRCodeHandler myQRCodeHandler;
    private String accountName;
    private int screenWidth;
    private int sponseCode;
    private Bitmap qrClodebitmap;
    private FileUtil fileUtil;
    private String mobileShopAddr;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_qrcode;
    }

    @Override
    protected void initData() {
        mGMaccountMgr = GBAccountMgr.getInstance();
        wing = mGMaccountMgr.getAccountInfo().winguoGeneral;
        sharedPopWindow = new SharedPopWindow(this, this);
    }

    private String iconurl;

    @Override
    protected void initViews() {
        screenWidth = getScreen();//获取屏幕宽度
        myQRClode = (ImageView) findViewById(R.id.my_qr_clode_id);
        qrclide_actionbar_back_id = (FrameLayout) findViewById(R.id.qrclide_actionbar_back_id);

        qr_username = (TextView) findViewById(R.id.qr_username_id);
        qr_Telephone = (TextView) findViewById(R.id.qr_telephone_id);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar_id);
        scan_ImageView = (Button) findViewById(R.id.scan_imageView_id);
        share_img = (FrameLayout) findViewById(R.id.qr_clide_action_share_id);
        share_img.setVisibility(View.GONE);
        qr_clide_action_share_id = (FrameLayout) findViewById(R.id.qr_clide_action_share_id);
        if (wing != null) {
            iconurl = wing.icoUrl;//获取头像
            accountName = wing.accountName;//绑定的用户名
            mobileShopAddr = wing.mobileShopAddr;

            qr_username.setText(wing.userName);
            qr_Telephone.setText(wing.telMobile);
        }
        fileUtil = new FileUtil(getApplicationContext());
        myQRCodeHandler = new MyQRCodeHandler(this, wing, fileUtil, myQRClode);
        // 二维码缓存
        if (fileUtil.isFileExists("shareQRClode.png")) {
            qrClodebitmap = fileUtil.getBitmap("shareQRClode.png");
            progressBar.setVisibility(View.GONE);
            myQRClode.setImageBitmap(qrClodebitmap);
        } else {
            // if (iconurl != null) {
            getUrlBitmap(iconurl);
            // }
            Message message = new Message();
            message.what = 1;
            // handler.sendMessage(message);
            myQRCodeHandler.sendMessage(message);
        }
    }

    /**
     * 获取屏幕分辨率 的宽；
     */
    private int getScreen() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        //    int screenHeight = display.getHeight();
        return screenWidth;
    }

    @Override
    protected void setListener() {
        qrclide_actionbar_back_id.setOnClickListener(this);
        qr_clide_action_share_id.setOnClickListener(this);
        scan_ImageView.setOnClickListener(this);
        share_img.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    private void getUrlBitmap(final String bitmapUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(bitmapUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.connect();
                    sponseCode = conn.getResponseCode();
                    Log.i("", "");
                    if (sponseCode == 200) {
                        InputStream fis = conn.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length = fis.read(bytes)) != -1) {
                            bos.write(bytes, 0, length);
                        }
                        picByte = bos.toByteArray();
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        bos.close();
                        fis.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

  /*  private void shareShow(String platform) {
        Platform plat = ShareSDK.getPlatform(platform);
        // chenqian  MobSharedUtils.showShopUrlShare(plat.getName(), getApplicationContext(), "http://img.mp.itc.cn/upload/20170422/2c8a1531d7cf4fc2a9c9c467f9bc91fd_th.jpeg", "chenqian");
        MobSharedUtils.showQRCodeShare(plat.getName(), getApplicationContext(), mobileShopAddr, "扫描此二维码，注册空间站！我带你飞吧");
    }*/

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.qrclide_actionbar_back_id://返回
                finish();
                break;
            case R.id.qr_clide_action_share_id://分享
            case R.id.share_img://分享
                //分享 店铺网址
                sharedPopWindow.showAtLocation(qr_clide_action_share_id, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.scan_imageView_id:// 分享
                // startActivity(new Intent(getBaseContext(), ZxingCaptureActivity.class));
                //截屏操作
                myQRCodeHandler.screenshot();
                sharedPopWindow.showAtLocation(qr_clide_action_share_id, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            //分享
            case R.id.share_dao1_1:
                sharedPopWindow.dismiss();

                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                MobSharedUtils.showQQShare(plat.getName(), getApplicationContext(), mobileShopAddr, "扫描此二维码，注册空间站！我带你飞吧");
                break;
            case R.id.share_dao1_2:
                sharedPopWindow.dismiss();
                myQRCodeHandler.shareShow(SinaWeibo.NAME);
                break;
            case R.id.share_dao1_3:
                sharedPopWindow.dismiss();
                myQRCodeHandler.shareShow(Wechat.NAME);
                break;
            case R.id.share_dao1_4:
                sharedPopWindow.dismiss();
                myQRCodeHandler.shareShow(WechatMoments.NAME);
                break;
            default:
        }
    }

}
