package com.winguo.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.wallet.AccountBankCardGettingActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.activity.CartActivity;
import com.winguo.activity.NearbyStoreHomeActivity;
import com.winguo.activity.ShopDetailActivity;
import com.winguo.activity.Student_creation_Activity;
import com.winguo.app.StartApp;
import com.winguo.base.BaseActivity;
import com.winguo.dynamic.FriendsDynamicActivity;
import com.winguo.login.fragment.PwdLoginFragment;
import com.winguo.login.fragment.SmsLoginFragment;
import com.winguo.mine.collect.MyCollectActivity;
import com.winguo.mine.history.HistoryLogActivity;
import com.winguo.mine.order.MyOrderActivity;
import com.winguo.personalcenter.MyPartnerActivity;
import com.winguo.personalcenter.MyQRCodeActivity;
import com.winguo.personalcenter.OpenSpaceActivity;
import com.winguo.personalcenter.safecenter.AccountSafeActivity;
import com.winguo.personalcenter.safecenter.paypwd.ModifyPayWayActivity;
import com.winguo.personalcenter.setting.FeedBackActivity;
import com.winguo.personalcenter.setting.SetCenterActivity;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.ToastUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/18.
 * 登录主界面
 */

public class LoginActivity extends BaseActivity {

    List<Fragment> data = new ArrayList<>();
    List<String> title = new ArrayList<>();
    private boolean isMySpace;
    private MyBroadcastReceiver receiver;
    private Intent flagIntent;

    @Override
    protected int getLayout() {
        return R.layout.activity_winguo_login;
    }

    @Override
    protected void initData() {

        flagIntent = getIntent();
        isMySpace = flagIntent.getBooleanExtra("isMySpace", false);
      /* if (flagIntent != null) {
            userName = flagIntent.getStringExtra("userName");
            userPwd = flagIntent.getStringExtra("userPwd");
        }*/
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("landscape");
        intentFilter.addAction("loginerror");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);

        title.add("短信登录");
        title.add("密码登录");
        SmsLoginFragment smsLoginFragment = new SmsLoginFragment();
        PwdLoginFragment pwdLoginFragment = new PwdLoginFragment();
        data.add(smsLoginFragment);
        data.add(pwdLoginFragment);
    }

    @Override
    protected void initViews() {
        ViewPager vp = (ViewPager) findViewById(R.id.winguo_login_vp);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.winguo_login_tl);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 40, 40);
            }
        });
        vp.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.hybrid4_account_login_back).setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    /**
     * 调用第三方微信登录
     */
    public void wxLogin() {
        if (!StartApp.mWxApi.isWXAppInstalled()) {
            ToastUtil.showToast(this, "您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        StartApp.mWxApi.sendReq(req);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.hybrid4_account_login_back:
                if (isMySpace) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    isMySpace = false;
                }
                finish();
                break;
        }

    }

    /**
     * 设置TabLayout 下划线的长度
     *
     * @param tabs
     * @param leftDip  leftMargin
     * @param rightDip rightMargin
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tabStrip == null) return;
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        if (llTab == null) return;
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }

    }

    /**
     * 主内容区 适配器
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        private String wxlongin;

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(Constants.LOGIN_SUCCESS)) {
                wxlongin = intent.getStringExtra(Constants.WX_LONING_SUCCESS);
                WinguoAccountGeneral info = (WinguoAccountGeneral) intent.getSerializableExtra("info");
                String pizza = WinguoAccountDataMgr.getPizza(context);//微信登录 未保存密码为null  否者为正常自动登录
                if (pizza == null) {//微信昵称
                    if (!TextUtils.isEmpty(info.userName)) {
                        ToastUtil.showToast(context, info.userName + "  登陆成功");
                    } else {
                        ToastUtil.showToast(context, info.accountName + "  登陆成功");
                    }
                } else { // 正常注册
                    ToastUtil.showToast(context, info.accountName + "  登陆成功");
                }
                // TODO: 2017/5/6
                Intent startintent = null;
                String Extera = flagIntent.getStringExtra("putExtra");
                String storeid = flagIntent.getStringExtra("storeid");
                String shopid = flagIntent.getStringExtra("shopid");
                if (Extera != null) {
                    switch (Extera) {

                        case Constants.LOGIN_WAY_WALLET://钱包
                            startintent = new Intent(mContext, MyWalletCenterActivity.class);
                            break;
                        case Constants.LOGIN_WAY_FEEDBACK://意见反馈
                            startintent = new Intent(mContext, FeedBackActivity.class);
                            break;
                        case Constants.LOGIN_WAY_OPEN_SPACE://开通空间站
                            startintent = new Intent(mContext, OpenSpaceActivity.class);
                            break;
                        case Constants.LOGIN_WAY_QR://二维码
                            startintent = new Intent(mContext, MyQRCodeActivity.class);
                            break;
                        case Constants.LOGIN_WAY_PAY_PWD_MOD://支付密码修改
                            startintent = new Intent(mContext, ModifyPayWayActivity.class);
                            break;
                        case Constants.LOGIN_WAY_MEASSGE://动态
                            startintent = new Intent(mContext, FriendsDynamicActivity.class);
                            break;
                        case Constants.LOGIN_SETTING://设置中心
                            startintent = new Intent(mContext, SetCenterActivity.class);
                            break;
                        case Constants.ACCOUNT_SAFE://账户与安全
                            startintent = new Intent(mContext, AccountSafeActivity.class);
                            break;
                        case Constants.LOGIN_WAY_CART://购物车
                            startintent = new Intent(mContext, CartActivity.class);
                            break;
                        case Constants.LOGIN_WAY_HISTORY_LOG://购物车
                            startintent = new Intent(mContext, HistoryLogActivity.class);
                            break;
                        case Constants.LOGIN_WAY_BANK_CARD://银行卡列表
                            startintent = new Intent(mContext, AccountBankCardGettingActivity.class);
                            break;
                        case Constants.LOGIN_WAY_ALL_ORDER://全部订单
                            startintent = new Intent();
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 0);
                            startintent.setClass(mContext, MyOrderActivity.class);
                            break;
                        case Constants.LOGIN_WAY_WAIT_PAY://待付款
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 1);
                            break;
                        case Constants.LOGIN_WAY_WAIT_DELIVER_GOODS://待发货
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 2);
                            break;
                        case Constants.LOGIN_WAY_WAIT_TAKE_DELIVER_GOODS://待收货
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 3);
                            break;
                        case Constants.LOGIN_WAY_WAIT_EVALUATE://待评价
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 4);
                            break;
                        case Constants.LOGIN_WAY_GOOD_ATTENTION://商品收藏
                            startintent = new Intent();
                            startintent.putExtra("collect", 1);
                            startintent.setClass(mContext, MyCollectActivity.class);
                            break;
                        case Constants.LOGIN_WAY_STORE_ATTENTION://店铺收藏
                            startintent = new Intent();
                            startintent.putExtra("collect", 2);
                            startintent.setClass(mContext, MyCollectActivity.class);
                            break;
                        case Constants.LOGIN_WAY_MY_PARTNER://我的伙伴
                            startintent = new Intent();
                            startintent.setClass(mContext, MyPartnerActivity.class);
                            break;
                        case Constants.NEARBY_STORE_HOME_CART://附近实体店 底部购物车
                            startintent = new Intent();
                            startintent.putExtra("store_id", storeid);
                            startintent.setClass(mContext, NearbyStoreHomeActivity.class);
                            break;
                        case Constants.NEARBY_STORE_SHOP_SPEC://商品详情 选着规格
                            startintent = new Intent();
                            startintent.putExtra("store_id", storeid);
                            startintent.putExtra("shop_id", shopid);
                            startintent.setClass(mContext, ShopDetailActivity.class);
                            break;

                        case Constants.LOGIN_STUDENT_CREATION://学生创客登陆
                            startintent = new Intent(mContext, Student_creation_Activity.class);
                            break;
                        default:
                    }
                    startActivity(startintent);
                }

         /*       if (wxlongin != null) {
                    if (wxlongin.equals(Constants.WX_LONING_SUCCESS)) {

                        if (!TextUtils.isEmpty(info.telMobile)) {

                            startActivity(new Intent(mContext, SetPhoneTelActivity.class));

                        }

                    }
                }*/
                finish();

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
