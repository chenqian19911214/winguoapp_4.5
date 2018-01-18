package com.winguo.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.camera.ZxingCaptureActivity;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.login.LoginActivity;
import com.winguo.personalcenter.MyQRCodeActivity;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;
import com.winguo.utils.BitmapUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.LruCacheUtils;
import com.winguo.utils.MobSharedUtils;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.view.CircleImageView;
import com.winguo.view.HomePopWindow;
import com.winguo.view.SharedPopWindow;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class BaseTitleFragmentActivity extends FragmentActivity{

    private LinearLayout linearLayout;
    private RelativeLayout layout_title_bar;
    private LinearLayout root_layout_view;
    private TextView title_name;
    private FrameLayout fl_title_back;
    private FrameLayout fl_title_more;
    private FrameLayout fl_title_wallet;
    private CircleImageView title_account;
    private WinguoAccountGeneral info;
    private HomePopWindow morePop;
    private SharedPopWindow sharedPopWindow;
    private boolean isHaveReadExternal = true;
    private String shopName;
    private String userShopURL;
    private ImageView title_iv;
    private ImageView title_more_iv;
    private ImageView title_wallet_iv;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 经测试在代码里直接声明透明状态栏更有效
        CommonUtil.stateSetting(this,R.color.title_color);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_title_base);
        initBar();
        initData();
        // 将当前activity加入到全局控制的activity集合中
        StartApp.getInstance().addActivity(this);
    }

    private void initData() {
        info= GBAccountMgr.getInstance().getAccountInfo().winguoGeneral;
       // shopName = (String) SPUtils.get(this, "shopName", "问果空间站");
       // title_name.setText(shopName);
        if (info != null) {
            String pizza = WinguoAccountDataMgr.getPizza(this);//微信登录 未保存密码来区分
            if (pizza==null) {
                if (!TextUtils.isEmpty(info.userName)) {
                    title_name.setText(info.userName);  //微信昵称
                } else {
                    title_name.setText(info.accountName); //问果账号
                }
            } else {
                title_name.setText(info.userName); //问果账号
            }
        }

        if (SPUtils.contains(this, "accountName")) {
           // String userImagURL = (String) SPUtils.get(this, "userImagURL", "");
            String userImagURL = info.icoUrl;
            if (!TextUtils.isEmpty(userImagURL)) {
                title_account.setTag(userImagURL);
                StartApp.lruCache.downLoaderBitmap(title_account, new LruCacheUtils.ImageLoaderlistener() {
                    @Override
                    public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                        if (bitmap!=null)
                            imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }

        //钱包
        fl_title_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPUtils.contains(BaseTitleFragmentActivity.this, "accountName")){
                    Intent accountWalletInent=new Intent(BaseTitleFragmentActivity.this,MyWalletCenterActivity.class);
                    startActivity(accountWalletInent);
                }else {
                    Intent it = new Intent(BaseTitleFragmentActivity.this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_WALLET);
                    startActivity(it);
                }
            }
        });
        //更多
        fl_title_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWin();
            }
        });
    }

    /**
     * 设置title的字体颜色
     * @param res 颜色
     */
    public void setTitleName(int res){
        if (title_name!=null){
            title_name.setTextColor(res);
        }
    }

    /**
     * 设置title上按钮的背景图
     * @param backBtnRes 返回键背景
     * @param moreBtnRes 更多背景
     * @param walletBtnRes 钱包背景
     */
    public  void setBackgroundRes(int backBtnRes,int moreBtnRes,int walletBtnRes){
        if (title_iv!=null){
            title_iv.setImageResource(backBtnRes);
        }
        if (title_more_iv!=null){
            title_more_iv.setImageResource(moreBtnRes);
        }
        if (title_wallet_iv!=null){
            title_wallet_iv.setImageResource(walletBtnRes);
        }
    }

    private void initBar() {
        fl_title_back = (FrameLayout) findViewById(R.id.fl_title_back);
        title_iv = (ImageView) findViewById(R.id.title_iv);
        title_more_iv = (ImageView) findViewById(R.id.title_more_iv);
        title_wallet_iv = (ImageView) findViewById(R.id.title_wallet_iv);

        fl_title_more = (FrameLayout) findViewById(R.id.fl_title_more);
        fl_title_wallet = (FrameLayout) findViewById(R.id.fl_title_wallet);
        title_account = (CircleImageView) findViewById(R.id.title_account);
        layout_title_bar = (RelativeLayout) findViewById(R.id.layout_title_bar);
        root_layout_view = (LinearLayout) findViewById(R.id.root_layout_view);
        title_name = (TextView) findViewById(R.id.title_name);
        sharedPopWindow = new SharedPopWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.share_dao1_1:
                        sharedPopWindow.dismiss();
                        //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                        Platform plat = ShareSDK.getPlatform(QQ.NAME);
                        MobSharedUtils.showQQShare(plat.getName(), BaseTitleFragmentActivity.this, info.mobileShopAddr, "扫描此二维码，注册空间站！我带你飞吧");
                        break;
                    case R.id.share_dao1_2:
                        sharedPopWindow.dismiss();
                        shareShow(SinaWeibo.NAME);
                        break;
                    case R.id.share_dao1_3:
                        sharedPopWindow.dismiss();
                        shareShow(Wechat.NAME);
                        break;
                    case R.id.share_dao1_4:
                        sharedPopWindow.dismiss();
                        shareShow(WechatMoments.NAME);
                        break;
                }
            }
        });
        morePop = new HomePopWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.main_shared:

                        morePop.dismiss();
                        Intent it = null;
                        //二维码
                        if (info != null) {
                            if (info.shared) {
                                it = new Intent(BaseTitleFragmentActivity.this, MyQRCodeActivity.class);
                            } else {
                                Toast.makeText(BaseTitleFragmentActivity.this, R.string.you_on_stop, Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            it = new Intent(BaseTitleFragmentActivity.this, LoginActivity.class);
                            it.putExtra("putExtra", Constants.LOGIN_WAY_QR);
                        }
                        startActivity(it);
                        break;
                    case R.id.main_more_qr:
                        //扫码
                        morePop.dismiss();
                        startActivity(new Intent(BaseTitleFragmentActivity.this, ZxingCaptureActivity.class));
                        break;
                }
            }
        });

    }
    /**
     * 更多功能菜单
     */
    private void showPopWin() {

        int[] location = new int[2];
        fl_title_more.getLocationOnScreen(location);

        GBLogUtils.DEBUG_DISPLAY("moreimage", location[1] + ":" + location[0] + ":" + fl_title_more.getWidth());
        //  pop.showAtLocation(moreImage, Gravity.NO_GRAVITY,location[0]-moreImage.getWidth(),location[1]);
        int screenWidth = ScreenUtil.getScreenWidth(this);
        switch (screenWidth) {
            case 1080:
                morePop.showAsDropDown(fl_title_more, -180, 0);
                break;
            case 720:
                morePop.showAsDropDown(fl_title_more, -150, 0);
                break;
        }


    }

    /**
     * 返回键功能
     */
    protected void setBackBtn() {
        if (fl_title_back != null) {
            fl_title_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 隐藏返回键
     */
    protected void setHiddenBack() {
        fl_title_back.setVisibility(View.GONE);
    }

    /**
     * 设置title名称
     *
     * @param titleText
     */
    protected void setTitle(String titleText) {
        title_name.setText(titleText);
    }

    /**
     * 修改跟布局背景
     *
     * @param layoutId
     */
    protected void setRootBackground(int layoutId) {
        int screenWidth = ScreenUtil.getScreenWidth(this);
        int screenHeight = ScreenUtil.getScreenHeight(this);
        bitmap = BitmapUtil.decodeSampledBitmapFromResource(getResources(), layoutId, screenWidth, screenHeight);
        root_layout_view.setBackgroundDrawable(BitmapUtil.bitMapToDrawable(bitmap));
    }
    /**
     * 修改title头布局背景
     *
     * @param layoutId
     */
    protected void setTitleBarBackground(int layoutId) {
        layout_title_bar.setBackgroundResource(layoutId);
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        linearLayout = (LinearLayout) findViewById(R.id.root_layout_view);
        if (linearLayout == null) return;
        linearLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initBar();
    }
    private void checkReadExternal() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isHaveReadExternal = false;

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 23);
                }
            } else {
                isHaveReadExternal = true;
            }

        }

    }

    /**
     * 分享集成平台
     *
     * @param platform
     */
    private void shareShow(String platform) {
        Platform plat = ShareSDK.getPlatform(platform);

        if (userShopURL == null || shopName == null) {
            userShopURL = (String) SPUtils.get(this, "userShopURL", "http://www.winguo.com");
        }
        MobSharedUtils.showQRCodeShare(plat.getName(), this, userShopURL, "扫描此二维码，注册空间站！我带你飞吧");
    }

    /**
     * 接受广播 登录后用户信息
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.MODIFY_NICK)) {
                String newNick = intent.getStringExtra("newNick");
                title_name.setText(newNick);
            }
            if (action.equals(Constants.MODIFY_PHOTO)) {
                Bitmap newPhoto = intent.getParcelableExtra("newPhoto");
                title_account.setImageBitmap(newPhoto);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除Activity
        StartApp.getInstance().removeActivity(this);
    }


}
