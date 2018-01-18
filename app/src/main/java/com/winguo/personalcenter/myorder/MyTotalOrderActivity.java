package com.winguo.personalcenter.myorder;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.base.BaseFragActivity;
import com.winguo.base.BaseTitleActivity;
import com.winguo.base.BaseTitleFragmentActivity;
import com.winguo.login.fragment.PwdLoginFragment;
import com.winguo.login.fragment.SmsLoginFragment;
import com.winguo.personalcenter.myorder.fragment.OnlineOrderFragment;
import com.winguo.personalcenter.myorder.fragment.StoreOrderFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/16.
 * 我的订单类型 （实体店订单 商城订单）
 */

public class MyTotalOrderActivity extends BaseTitleActivity {

    private TabLayout my_total_order_tl;
    private ViewPager my_total_order_vp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_total_order);
        setBackBtn();
        initData();
        initViews();
    }

    private void initData() {
        title.add("线上订单");
        title.add("实体店订单");
        OnlineOrderFragment onlineOrderFragment = new OnlineOrderFragment();
        StoreOrderFragment storeOrderFragment = new StoreOrderFragment();
        data.add(onlineOrderFragment);
        data.add(storeOrderFragment);
    }

    private void initViews() {
        my_total_order_tl = (TabLayout) findViewById(R.id.my_total_order_tl);
        my_total_order_vp = (ViewPager) findViewById(R.id.my_total_order_vp);
        my_total_order_tl.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(my_total_order_tl,40,40);
            }
        });
        my_total_order_vp.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        my_total_order_tl.setupWithViewPager(my_total_order_vp);
    }


    /**
     * 设置TabLayout 下划线的长度
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
        if (llTab==null) return;
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

    private List<Fragment> data = new ArrayList<>();
    private List<String> title = new ArrayList<>();
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


}
