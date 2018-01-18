package com.winguo.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.base.BaseFragment;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.utils.FileUtil;
import com.winguo.utils.MobSharedUtils;
import com.winguo.utils.MyQRCodeHandler;
import com.winguo.utils.QRCode;
import com.winguo.view.SharedPopWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
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

/**
 * 我的二维码
 * Created by admin on 2017/6/29.
 */

public class MyQRCodeFragment extends BaseFragment {

    private ImageView myQRClode;//,main_mycode_header;
    private WinguoAccountGeneral wing;
    private GBAccountMgr mGMaccountMgr;
    private String accountName;
    private int screenWidth;
    private int sponseCode;
    private Bitmap qrClodebitmap, iconurlBitmap;
    private FileUtil fileUtil;
    private String mobileShopAddr;
    private String requsturl;
    private String shopurl;
    private String status;
    private String text;
    private String code;
    private String iconurl;
    private String shopName;
    private TextView main_mycode_space_name, qr_Telephone;
    private byte[] picByte;
    private Button scanimageViewButton;
    private  MyQRCodeHandler myQRCodeHandler;
    private SharedPopWindow sharedPopWindow;
    private MyBroadcastReceiver receiver;
    @Override
    protected int getLayout() {
        return R.layout.fragment_my_qrcode;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //注册广播 获取登陆后用户账号 头像等
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("landscape");
        intentFilter.addAction("quitLogin");
        receiver = new MyBroadcastReceiver();
        context.registerReceiver(receiver, intentFilter);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
            initView(view);
        }
    }

    @Override
    protected void initView(View view) {
        screenWidth = getScreen(getActivity());//获取屏幕宽度
        myQRClode = view.findViewById(R.id.my_qr_clode_id);
        main_mycode_space_name = view.findViewById(R.id.qr_username_id);
        qr_Telephone = view.findViewById(R.id.qr_telephone_id);
        //  main_mycode_header = (ImageView)view.findViewById(R.id.main_mycode_header);
        scanimageViewButton = view.findViewById(R.id.scan_imageView_id);
        if (wing != null) {
            iconurl = wing.icoUrl;//获取头像
            Log.i("chenqian", "iconurl :" + iconurl);
            if (!TextUtils.isEmpty(iconurl)) {
                getUrlBitmap(iconurl);
            } else {
                //   main_mycode_header.setImageResource(R.drawable.winguo_personal_deafault_icon);
            }
            accountName = wing.accountName;//绑定的用户名
            shopName = wing.shopName;
            mobileShopAddr = wing.mobileShopAddr;
            main_mycode_space_name.setText(wing.userName);
            qr_Telephone.setText(wing.telMobile);
        }
        fileUtil = new FileUtil(getContext());
        myQRCodeHandler = new MyQRCodeHandler(mContext,wing,fileUtil,myQRClode);

        // 二维码缓存
        //fileUtil.getBitmap("myQRClode");
        if (fileUtil.isFileExists("myQRClode.png")) {
            qrClodebitmap = fileUtil.getBitmap("myQRClode.png");
            //  progressBar.setVisibility(View.GONE);
            myQRClode.setImageBitmap(qrClodebitmap);
        } else {
            // if (iconurl != null) {
            //   getUrlBitmap(iconurl);
            // }

            Message message = new Message();
            message.what = 1;
            myQRCodeHandler.sendMessage(message);
        }
    }

    @Override
    protected void initData() {

        mGMaccountMgr = GBAccountMgr.getInstance();
        wing = mGMaccountMgr.getAccountInfo().winguoGeneral;
        sharedPopWindow = new SharedPopWindow(context,this);
    }

    @Override
    protected void setListener() {
        scanimageViewButton.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.scan_imageView_id:
                myQRCodeHandler. screenshot();
                sharedPopWindow.showAtLocation(scanimageViewButton, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.share_dao1_1:
                sharedPopWindow.dismiss();

                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                MobSharedUtils.showQQShare(plat.getName(), context, mobileShopAddr, "扫描此二维码，注册空间站！我带你飞吧");
                break;
            case R.id.share_dao1_2:
                sharedPopWindow.dismiss();
                myQRCodeHandler.shareShow(SinaWeibo.NAME);
                break;
            case R.id.share_dao1_3:
                sharedPopWindow.dismiss();
                myQRCodeHandler. shareShow(Wechat.NAME);
                break;
            case R.id.share_dao1_4:
                sharedPopWindow.dismiss();
                myQRCodeHandler.shareShow(WechatMoments.NAME);
                break;
            default:
        }

    }

    /**
     * 获取屏幕分辨率 的宽；
     */
    private int getScreen(Activity context) {
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        //    int screenHeight = display.getHeight();
        return display.getWidth();
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
                        myQRCodeHandler.sendMessage(message);
                        bos.close();
                        fis.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    /**
     * 接受广播 登录后用户信息
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {

            String action = intent.getAction();
            //登陆过后 通知修改头像、 账户名、余额、果币
            if ("landscape".equals(action)) {
                initData();
                initView(view);
            }

            if ("quitLogin".equals(action)) {
                //startActivity(new Intent(context, WinguoLoginActivity.class));
            }
        }
    }

}
