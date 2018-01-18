package com.winguo.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.winguo.R;
import com.winguo.base.BaseTitleFragmentActivity;
import com.winguo.mine.order.list.fragment.AllFragment;
import com.winguo.mine.order.list.fragment.WaitCommentFragment;
import com.winguo.mine.order.list.fragment.WaitDeliverFragment;
import com.winguo.mine.order.list.fragment.WaitPayFragment;
import com.winguo.mine.order.list.fragment.WaitReceiveFragment;
import com.winguo.mine.order.viewpager.TabPageIndicator;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;

import java.util.ArrayList;

/**
 * @author hcpai
 * @desc 我的订单
 */
public class MyOrderActivity extends BaseTitleFragmentActivity {

    private ArrayList<Fragment> mFragments;
    private String[] mTitles;
    private int mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        setBackBtn();
        initData();
        initViews();
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        mStatus = (int) getIntent().getExtras().get(ActionUtil.ACTION_ORDER_STATUS);
        mTitles = this.getResources().getStringArray(R.array.fragments_titles);
        AllFragment allFragment = AllFragment.newInstance(0);
        WaitPayFragment waitPayFragment = WaitPayFragment.newInstance(1);
        WaitDeliverFragment waitDeliverFragment = WaitDeliverFragment.newInstance(2);
        WaitReceiveFragment waitReceiveFragment = WaitReceiveFragment.newInstance(3);
        WaitCommentFragment waitCommentFragment = WaitCommentFragment.newInstance(4);
        mFragments = new ArrayList<>();
        mFragments.add(allFragment);
        mFragments.add(waitPayFragment);
        mFragments.add(waitDeliverFragment);
        mFragments.add(waitReceiveFragment);
        mFragments.add(waitCommentFragment);
    }

    /**
     * 初始化视图
     */
    protected void initViews() {
        TabPageIndicator pagerIndicator = (TabPageIndicator) findViewById(R.id.pagerIndicator);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(6);
        BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(pagerAdapter);
        pagerIndicator.setViewPager(viewPager);
        pagerIndicator.setOnTabReselectedListener(new TabPageIndicator.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                //发送广播
                Intent positionIntent = new Intent("reselectedListener");
                positionIntent.putExtra("position", position);
                sendBroadcast(positionIntent);
            }
        });
        viewPager.setCurrentItem(mStatus);
    }
}
