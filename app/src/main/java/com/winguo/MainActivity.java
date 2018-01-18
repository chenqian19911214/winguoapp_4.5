package com.winguo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.camera.ZxingCaptureActivity;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.gblocation.GBDLocation;
import com.guobi.winguoapp.voice.SpeechiFlytek;
import com.guobi.winguoapp.voice.VoiceFunActivity;
import com.winguo.activity.Search2Activity;
import com.winguo.activity.SearchActivity;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragActivity;
import com.winguo.fragment.HomePageFragment;
import com.winguo.fragment.MyQRCodeFragment;
import com.winguo.fragment.MyShoppingFragment;
import com.winguo.fragment.NearbyFragment;
import com.winguo.listener.IDrawerListener;
import com.winguo.login.LoginActivity;
import com.winguo.personalcenter.MyQRCodeActivity;
import com.winguo.personalcenter.OpenSpaceActivity;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;
import com.winguo.utils.BitmapUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.FileUtil;
import com.winguo.utils.Intents;
import com.winguo.utils.LruCacheUtils;
import com.winguo.utils.MobSharedUtils;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.view.CircleImageView;
import com.winguo.view.GBDialog;
import com.winguo.view.HomePopWindow;
import com.winguo.view.SharedPopWindow;
import com.winguo.view.VoucherDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 主菜单（主页 语音 商城 搜索 个人中心 更多）
 */
public class MainActivity extends BaseFragActivity {

    public static int VOICE_RESULT = 1;
    public static int MY_SPACE_RESULT = 3;
    public static int SECRETARY_IMAGE_SET = 2;
    public static String TAG = MainActivity.class.getSimpleName();

    private ImageView main_stroll;
    private ImageView main_nearby, main_bottom_mycode_ic, main_bottom_space_ic;
    private PermissionBroadReceiver receiver;
    private LinearLayout speakClick;
    private LinearLayout homeClick;
    private LinearLayout findClick, main_bottom_mycode_ll, main_bottom_space_ll;

    public DrawerLayout mDrawerLayout;
    private ImageView main_speak;
    private Intent secretaryService;

    //侧拉菜单 顶部菜单
    private CircleImageView account;
    private FrameLayout accountClick;
    private String accountName;
    private FrameLayout moreClick, walletClick;
    private PopupWindow pop;
    private TextView topNickName;
    private String shopName = "";//登陆后 头部店铺名字
    private SharedPopWindow sharedPopWindow;
    private String userShopURL;
    private String userImagURL;//登陆后 头部店铺名字
    private WinguoAccountGeneral info;
    private Fragment visibleFragment;
    private SpeechiFlytek speechiFlytek;
    private String imageuri="http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYSB6AfLYkAAPqLFQ7-vk99..png";


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String[] requestedPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_SMS};
        Intents.checkPermissions(this, requestedPermissions, Constants.REQUEST_CODE_PERMISSIONS);
    }

    /**
     * 初始化 顶部更多、分享弹窗
     */
    private void initMorePop() {
        //分享pop初始化
        sharedPopWindow = new SharedPopWindow(this, this);
        pop = new HomePopWindow(this, this);
    }

    @Override
    protected void initViews() {

        speakClick =  findViewById(R.id.main_bottom_search);
        homeClick =  findViewById(R.id.main_bottom_home);
        findClick =  findViewById(R.id.main_bottom_find);
        main_bottom_space_ll =  findViewById(R.id.main_bottom_space_ll);
        main_bottom_mycode_ll =  findViewById(R.id.main_bottom_mycode_ll);
        main_bottom_mycode_ic =  findViewById(R.id.main_bottom_mycode_ic);
        main_bottom_space_ic =  findViewById(R.id.main_bottom_space_ic);
        RelativeLayout bottomLayout =  findViewById(R.id.main_bottom_layout);
        mDrawerLayout =  findViewById(R.id.main_drawer);
        //设置左侧菜单 全屏显示
        View v =  findViewById(R.id.id_left_menu);
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) v.getLayoutParams();
        layoutParams.setMargins(0,0,ScreenUtil.dipToPx(this,-65),0); // 设置-65dp，占满剩余无法滑动到的65dp
        v.setLayoutParams(layoutParams);
        //顶部导航栏initView
        //返回键 默认隐藏
        findViewById(R.id.fl_title_back).setVisibility(View.GONE);
        account =  findViewById(R.id.title_account);
        topNickName =  findViewById(R.id.title_name);//登陆后 填充店铺名
        walletClick =  findViewById(R.id.fl_title_wallet);
        moreClick =  findViewById(R.id.fl_title_more);

        main_speak =  findViewById(R.id.main_speak);
        main_nearby =  findViewById(R.id.main_nearby);
        main_stroll =  findViewById(R.id.main_stroll);
        defaulthHome();//默认主内容区main_content 显示首页
    }

    private String fragment1Tag = "fragment1Tag";
    private String fragment2Tag = "fragment2Tag";
    private String fragment3Tag = "fragment3Tag";
    private String fragment4Tag = "fragment4Tag";

    /**
     * 底部栏切换
     *
     * @param checkedId
     */
    public void onCheckedChanged(int checkedId) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        /*Fragment fragment1 = null;
        Fragment fragment2 = null;
        if (visibleFragment != null) {
            Log.i("visibleFragment ", "" + visibleFragment);
            ft.remove(visibleFragment);
            if (visibleFragment instanceof HomeFragment1) {
                fragment2 = fm.findFragmentByTag(fragment2Tag);
                visibleFragment = null;
            } else if (visibleFragment instanceof NearbyFragment) {
                fragment1 = fm.findFragmentByTag(fragment1Tag);
                visibleFragment = null;
            }

        } else {
             fragment1 = fm.findFragmentByTag(fragment1Tag);
             fragment2 = fm.findFragmentByTag(fragment2Tag);
        }*/
        Fragment fragment1 = fm.findFragmentByTag(fragment1Tag);
        Fragment fragment2 = fm.findFragmentByTag(fragment2Tag);
        Fragment fragment3 = fm.findFragmentByTag(fragment3Tag);
        Fragment fragment4 = fm.findFragmentByTag(fragment4Tag);
        if (fragment1 != null) {
            ft.hide(fragment1);
        }
        if (fragment2 != null) {
            ft.hide(fragment2);
        }
        if (fragment3 != null) {
            ft.hide(fragment3);
        }
        if (fragment4 != null) {
            ft.hide(fragment4);
        }
        switch (checkedId) {
            case R.id.main_bottom_home:
                if (fragment1 == null) {
                   // fragment1 = new HomeFragment();
                    fragment1 = new HomePageFragment();
                    ft.add(R.id.main_content, fragment1, fragment1Tag);
                } else {
                    ft.show(fragment1);
                }
                break;
            case R.id.main_bottom_find:
                if (fragment2 == null) {
                    //TODO
                    fragment2 = new NearbyFragment();
                    ft.add(R.id.main_content, fragment2, fragment2Tag);
                } else {
                    ft.show(fragment2);
                }
                break;
            case R.id.main_bottom_mycode_ll:
                //二维码
                if (fragment3 == null) {
                    //TODO
                    fragment3 = new MyQRCodeFragment();
                    ft.add(R.id.main_content, fragment3, fragment3Tag);
                } else {
                    ft.show(fragment3);
                }
                break;
            case R.id.main_bottom_space_ll:
             //购物车
                if (fragment4 == null) {
                    //TODO
                    fragment4 = new MyShoppingFragment();
                    ft.add(R.id.main_content, fragment4, fragment4Tag);
                } else {
                    ft.show(fragment4);
                }
                break;
            default:
                break;
        }
       ft.commitAllowingStateLoss();
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
            shopName = (String) SPUtils.get(this, "shopName", "问果空间站");
        }
        MobSharedUtils.showQRCodeShare(plat.getName(), this, userShopURL, "扫描此二维码，注册空间站！我带你飞吧");
        // MobSharedUtils.showShopUrlShare(plat.getName(), this, userShopURL, shopName);
    }

    /**
     * 分享菜单
     */
    private void sharedShow() {
        sharedPopWindow.showAtLocation(moreClick, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 更多功能菜单
     */
    private void showPopWin() {

        int screenWidth = ScreenUtil.getScreenWidth(this);
        switch (screenWidth) {
            case 1080:
                pop.showAsDropDown(moreClick, -180, 0);
                break;
            case 720:
                pop.showAsDropDown(moreClick, -150, 0);
                break;
        }
    }

    /**
     * 默认首页
     */
    private void defaulthHome() {
        onCheckedChanged(R.id.main_bottom_home);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setStateColor() {
        CommonUtil.stateSetting(this, R.color.title_color);
    }

    @Override
    protected void initData() {
        if (SPUtils.contains(getApplicationContext(), "accountName")) {
            accountName = (String) SPUtils.get(this, "accountName", "");
            shopName = (String) SPUtils.get(this, "shopName", "问果空间站");
            userImagURL = (String) SPUtils.get(this, "userImagURL", "");
        }
        receiver = new PermissionBroadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NO_NET_STATU);
        filter.addAction(Constants.NET_STATU_MOBILE);
        filter.addAction(Constants.NET_STATU_WIFI);
        filter.addAction(Constants.OPEN_LEFT_MENU);
        filter.addAction(Constants.LOGIN_SUCCESS);
        filter.addAction(Constants.QUIT_SUCCESS);
        filter.addAction(Constants.MODIFY_PHOTO);
        filter.addAction(Constants.MODIFY_NICK);
        filter.addAction("com.winguo.isMySpace");
        registerReceiver(receiver, filter);
        initMorePop();
    }

   public static String imagepath;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Constants.REQUEST_CODE_PERMISSIONS == requestCode) {
            int length = grantResults.length;
            StringBuffer name = new StringBuffer();
            for (int i = 0; i < length; i++) {

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //ToastUtil.showToast(getApplicationContext(),permissions[i]+"open");
                    Intent toRefresh = new Intent("toRefresh");
                    sendBroadcast(toRefresh);

                } else {

                    switch (permissions[i]) {
                        case Manifest.permission.READ_SMS:
                            name.append("短信、");
                            break;
                        case Manifest.permission.READ_CONTACTS:
                            name.append("通讯录、");
                            break;
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            name.append("存储、");
                            break;
                        case Manifest.permission.RECORD_AUDIO:
                            name.append("录音、");
                            break;
                        case Manifest.permission.ACCESS_COARSE_LOCATION:
                            name.append("定位、");
                            break;
                    }
                }
            }
            int nameLen = name.length();
            if (nameLen > 2) {
                name.replace(nameLen - 1, nameLen, "权限需要打开");
                Intents.noPermissionStatus(this, name.toString());
            }

        }

        if (Constants.REQUEST_CODE_PERMISSIONS_VOICE == requestCode) {
            int length = grantResults.length;
            StringBuffer name = new StringBuffer();
            for (int i = 0; i < length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    switch (permissions[i]) {

                        case Manifest.permission.RECORD_AUDIO:
                            name.append("录音、");
                            break;
                        case Manifest.permission.ACCESS_COARSE_LOCATION:
                            name.append("定位、");
                            break;
                    }
                }
            }
            int nameLen = name.length();
            if (nameLen > 2) {
                name.replace(nameLen - 1, nameLen, "权限需要打开");
                Intents.noPermissionStatus(this, name.toString());
            }

        }
        if (23 == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GBDLocation.getInstance().startLocation(null);
            } else {
                Intents.noPermissionStatus(this, "定位权限需要打开");
            }
        }
        if (REQUEST_WRITE_EXTERNAL_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toSaveShareQR(); //保存分享二维码
            } else {
                Intents.noPermissionStatus(this, "存储权限需要打开");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void setListener() {
        account.setOnClickListener(this);
        moreClick.setOnClickListener(this);
        walletClick.setOnClickListener(this);

        homeClick.setOnClickListener(this);
        findClick.setOnClickListener(this);
        speakClick.setOnClickListener(this);
        main_bottom_mycode_ll.setOnClickListener(this);
        main_bottom_space_ll.setOnClickListener(this);
        IDrawerListener drawerListener = new IDrawerListener(this, mDrawerLayout);
        mDrawerLayout.setDrawerListener(drawerListener);
        speakClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO: 2017/10/12
                launchSpeak();
                return false;
            }
        });
        speakClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        Downtime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        long Uptime = System.currentTimeMillis();
                        long time_difference = Uptime - Downtime;
                        if (speechiFlytek == null) {
                            if (time_difference > 1000) {
                                speechiFlytek = new SpeechiFlytek(getApplicationContext());
                            }
                        }

                        if (speechiFlytek != null) {
                            speechiFlytek.mIat.stopListening();
                        }
                        break;
                    default:
                }

                return false;
            }
        });
    }

    long Downtime;

    @Override
    protected void handleMsg(Message msg) {
    }

    @Override
    protected void doClick(View v) {

        switch (v.getId()) {
            case R.id.main_bottom_home:
                main_stroll.setImageResource(R.drawable.winguo_main_bottom_home_seletor);
                main_nearby.setImageResource(R.drawable.winguo_main_bottom_nearby_default);
                main_bottom_space_ic.setImageResource(R.drawable.winguo_main_bottom_shopping_default);
                main_bottom_mycode_ic.setImageResource(R.drawable.winguo_main_bottom_share_default);
                onCheckedChanged(R.id.main_bottom_home);
                break;
            case R.id.main_bottom_space_ll:
                //我家
                //是否登录
                if (SPUtils.contains(getApplicationContext(), "accountName")) {

                  /* if (info != null) {
                        if ("0".equals(info.maker_id)) {//是个人用户
                            if (info.shared) {
                                //开通空间站
                                jumpMySpace();
                            } else {
                                //没有开通空间站
                                Intent intent = new Intent(MainActivity.this, OpenSpaceActivity.class);
                                startActivityForResult(intent, MY_SPACE_RESULT);
                            }
                        } else {//是企业或实体店用户
                            jumpMySpace();
                        }
                    }*/
                    jumpMySpace();

                } else {
                    //去登陆
                    startActivity(new Intent(this, LoginActivity.class));
                }

                break;
            case R.id.main_bottom_mycode_ll:
                //是否登录
                if (SPUtils.contains(getApplicationContext(), "accountName")) {
                    if (info != null) {
                        //是否开通空间站
                        if (info.shared) {
                            main_bottom_mycode_ic.setImageResource(R.drawable.winguo_main_bottom_share_selector);
                            main_bottom_space_ic.setImageResource(R.drawable.winguo_main_bottom_shopping_default);
                            main_stroll.setImageResource(R.drawable.winguo_main_bottom_home_default);
                            main_nearby.setImageResource(R.drawable.winguo_main_bottom_nearby_default);
                            //我的二维码
                            onCheckedChanged(R.id.main_bottom_mycode_ll);
                        } else {
                            //去开通空间站
                            startActivity(new Intent(this, OpenSpaceActivity.class));
                        }
                    }
                } else {
                    //去登陆
                    startActivity(new Intent(this, LoginActivity.class));
                }

                break;

            case R.id.main_bottom_find:
                // TODO: 2017/5/15 附近需要获取定位信息
                main_nearby.setImageResource(R.drawable.winguo_main_bottom_nearby_seletor);
                main_stroll.setImageResource(R.drawable.winguo_main_bottom_home_default);
                main_bottom_space_ic.setImageResource(R.drawable.winguo_main_bottom_shopping_default);
                main_bottom_mycode_ic.setImageResource(R.drawable.winguo_main_bottom_share_default);
                onCheckedChanged(R.id.main_bottom_find);

                break;
            case R.id.main_bottom_search://语音按钮
                //    launchSpeak();

              //  Intent intent = new Intent(getApplicationContext(), StoreSearchActivity.class);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                break;
            //头部点击事件
            case R.id.title_account:
                //顶部头像 打开左侧菜单
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.fl_title_more:
                //弹出菜单
                showPopWin();
                break;
            case R.id.fl_title_wallet:
                //跳转钱包
                if (SPUtils.contains(getApplicationContext(), "accountName")) {
                    startActivity(new Intent(this, MyWalletCenterActivity.class));
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_WALLET);
                    startActivity(it);
                }
                break;
            case R.id.search_edit:
                //检测存储权限 否则会崩掉
                checkReadExternal();
                if (isHaveReadExternal) {
                    startActivity(new Intent(this, Search2Activity.class));
                } else {
                    Intents.noPermissionStatus(this, "请先打开存储权限");
                }
                break;
            //以下为 更多功能点击处理
            case R.id.main_shared:
                pop.dismiss();
                Intent it = null;
                //二维码
                if (info != null) {
                    if (info.shared) {
                        it = new Intent(MainActivity.this, MyQRCodeActivity.class);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.you_on_stop, Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    it = new Intent(MainActivity.this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_QR);
                }
                startActivity(it);
                break;
            case R.id.main_more_qr:
                //扫码
                pop.dismiss();
                startActivity(new Intent(this, ZxingCaptureActivity.class));
                break;
            //分享
            case R.id.share_dao1_1:
                sharedPopWindow.dismiss();
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                MobSharedUtils.showQQShare(plat.getName(), this, info.mobileShopAddr, "扫描此二维码，注册空间站！我带你飞吧");
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

            default:
                break;

        }

    }

    private void jumpMySpace() {
        main_bottom_space_ic.setImageResource(R.drawable.winguo_main_bottom_shopping_seletor);
        main_bottom_mycode_ic.setImageResource(R.drawable.winguo_main_bottom_share_default);
        main_stroll.setImageResource(R.drawable.winguo_main_bottom_home_default);
        main_nearby.setImageResource(R.drawable.winguo_main_bottom_nearby_default);
        onCheckedChanged(R.id.main_bottom_space_ll);
    }

    private boolean isHaveReadExternal = true;

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
     * 启动语音
     */
    private void launchSpeak() {

        if (Intents.checkPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || Intents.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_CODE_PERMISSIONS_VOICE);
            }
        } else {
            Intent voice = new Intent(this, VoiceFunActivity.class);
            //analysis这个字段 false：语音-->文字 直接跳转到SearchActivity ; true：加词句分析（打电话 发短息）
            voice.putExtra("analysis", "true");
            startActivityForResult(voice, VOICE_RESULT);
        }
    }

    /**
     * 解决app退入后台 再次唤醒多fragment 重叠问题
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment1 = fm.findFragmentByTag(fragment1Tag);
        Fragment fragment2 = fm.findFragmentByTag(fragment2Tag);
        Fragment fragment3 = fm.findFragmentByTag(fragment3Tag);
        Fragment fragment4 = fm.findFragmentByTag(fragment4Tag);
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment1 != null) {
            //默认显示
            ft.show(fragment1);
        }
        if (fragment2 != null) {
            //默认隐藏
            ft.hide(fragment2);
        }
        if (fragment3 != null) {
            //默认隐藏
            ft.hide(fragment3);
        }
        if (fragment4 != null) {
            //默认隐藏
            ft.hide(fragment4);
        }
        ft.commit();
    }

    /**
     * 接受语音解析结果
     * 这里加判断（词法分析） 直接打电话 发短息 搜索关键词
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (resultCode == RESULT_OK && requestCode == VOICE_RESULT) {
                String result = data.getStringExtra("result");
                if (result.equals("input")) {
                    String voiceRes = data.getStringExtra("rawinput");//语音解释出的结果
                    Intent it = new Intent(this, Search2Activity.class);
                    it.putExtra("key", voiceRes);
                    it.putExtra("type", "voice");
                    startActivity(it);
                }
            }
            if (resultCode == RESULT_OK && requestCode == MY_SPACE_RESULT) {
                //开通空间站后要重新进入我+
                jumpMySpace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private GBDialog gbDialog;

    /**
     *注册送现金券
     * @param context
     * @param cash
     */
    private void  openVoucherDialog(Context context,String cash){
        VoucherDialog dialog = new VoucherDialog(MainActivity.this,"注册获得现金券",cash);
        dialog.show();
    }
    /**
     * 权限检测接收广播
     */
    public class PermissionBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            boolean networkAvailable = NetWorkUtil.isNetworkAvailable(getApplicationContext());
            String action = intent.getAction();

            //无网络 ,打开Dialog
            if (action.equals(Constants.NO_NET_STATU) && !networkAvailable) {
                if (gbDialog == null) {
                    gbDialog = Intents.noNetStatus(context);
                    gbDialog.show();
                }
            }
            //网络（手机、wifi）打开 ,关闭Dialog
            if (action.equals(Constants.NET_STATU_MOBILE) || action.equals(Constants.NET_STATU_WIFI)) {
                if (gbDialog != null) {
                    gbDialog.dismiss();
                }
               /* visibleFragment = getVisibleFragment();
                Log.i("visibleFragment action",visibleFragment+"");
                if (visibleFragment instanceof HomeFragment1) {
                    defaulthHome();
                } else if (visibleFragment instanceof NearbyFragment) {
                    onCheckedChanged(R.id.main_bottom_find);
                }*/
                Intent toRefresh = new Intent(Constants.AUTO_REFRESH);
                context.sendBroadcast(toRefresh);
            }

            if (action.equals(Constants.OPEN_LEFT_MENU)) {
                //打开左侧
                if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            if (action.equals(Constants.LOGIN_SUCCESS)) {
                //登陆成功后 关闭侧滑菜单
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                WinguoAccountGeneral winuoAccountGeneral = (WinguoAccountGeneral) intent.getSerializableExtra("info");

                try {
                    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date rDate = ss.parse(winuoAccountGeneral.regDate);
                    long time = rDate.getTime();
                    long timeInMillis = Calendar.getInstance().getTimeInMillis();
                    Log.e("login:",""+winuoAccountGeneral.regDate+"time:"+time+"curr time"+timeInMillis+"t====="+(timeInMillis-time));  // 2017-01-06 16:11:11
                    if((timeInMillis-time) < 30000){
                        //注册时间在1分钟内 则弹出送现金券  否者不弹
                        openVoucherDialog(MainActivity.this,winuoAccountGeneral.cashCoupon+"");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                info = winuoAccountGeneral;
                String pizza = WinguoAccountDataMgr.getPizza(context);//微信登录 未保存密码来区分
                if (pizza==null) {
                    if (!TextUtils.isEmpty(info.userName)) {
                        topNickName.setText(info.userName);  //微信昵称
                    } else {
                        topNickName.setText(info.accountName); //问果账号
                    }
                } else {
                    topNickName.setText(info.userName); //问果账号
                }
                toSaveShareQR();
                if (!TextUtils.isEmpty(info.icoUrl)) {
                    account.setTag(info.icoUrl);
                    if (StartApp.lruCache == null) {
                        StartApp.lruCache = new LruCacheUtils(getApplicationContext());
                    }
                    StartApp.lruCache.downLoaderBitmap(account, new LruCacheUtils.ImageLoaderlistener() {
                        @Override
                        public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                            if (bitmap!=null)
                                imageView.setImageBitmap(bitmap);
                        }
                    });

                }
            }
            if (action.equals(Constants.MODIFY_PHOTO)) {
                Bitmap newPhoto = intent.getParcelableExtra("newPhoto");
                account.setImageBitmap(newPhoto);
            }
            if (action.equals(Constants.MODIFY_NICK)) {
                String newNick = intent.getStringExtra("newNick");
                topNickName.setText(newNick);
            }
            //退出登录 置空顶部商铺名字
            if (action.equals(Constants.QUIT_SUCCESS)) {
                //删除本地缓存 图片
                FileUtil util = new FileUtil(context);
                util.deleteFile();
                SPUtils.remove(context, "shopName");
                topNickName.setText(getString(R.string.shop_name_def));
                account.setImageResource(R.drawable.winguo_personal_deafault_icon);
                //isLogin = false;
            }
        }
    }

    /**
     * 获取当前显示fragment
     *
     * @return
     */
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private static final int REQUEST_WRITE_EXTERNAL_CODE = 0x43;
    /**
     * 去保存 分享二维码
     */
    private void toSaveShareQR() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            BitmapUtil.saveUserShareQR2(this, info.accountName, ScreenUtil.getScreenWidth(this));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_EXTERNAL_CODE);
            }
        }
     /*
        默认头像
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.mipmap.logo);
        Bitmap bitmap = bd.getBitmap();*/
        //FileUtil.saveUserShareQR(mContext, info.icoUrl, info.accountName, ScreenUtil.getScreenWidth((Activity) mContext), bitmap);

    }

    @Override
    protected void onRestart() {
        // isLogin = SPUtils.contains(getApplicationContext(), "accountName");
        super.onRestart();
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }

    }

    private boolean isOpenSer = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        isOpenSer = true;
        //执行完 基类的onDestroy();  杀死进程
        System.exit(0);
        Process.killProcess(Process.myPid());
    }

}
