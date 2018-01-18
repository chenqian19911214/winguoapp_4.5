package com.winguo.personalcenter.myorder.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.winguo.R;
import com.winguo.base.BaseFragment;
import com.winguo.mine.order.BaseViewPagerAdapter;
import com.winguo.mine.order.list.fragment.AllFragment;
import com.winguo.mine.order.list.fragment.WaitCommentFragment;
import com.winguo.mine.order.list.fragment.WaitDeliverFragment;
import com.winguo.mine.order.list.fragment.WaitPayFragment;
import com.winguo.mine.order.list.fragment.WaitReceiveFragment;
import com.winguo.mine.order.viewpager.TabPageIndicator;
import com.winguo.utils.ActionUtil;

import java.util.ArrayList;

/**
 * Created by admin on 2017/10/16.
 * 线上订单
 */

public class OnlineOrderFragment extends BaseFragment {
    private ArrayList<Fragment> mFragments;
    private String[] mTitles;
    private int mStatus;
    @Override
    protected int getLayout() {
        return R.layout.activity_myorder;
    }

    @Override
    protected void initView(View view) {
        TabPageIndicator pagerIndicator = (TabPageIndicator)view.findViewById(R.id.pagerIndicator);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(6);
        BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(pagerAdapter);
        pagerIndicator.setViewPager(viewPager);
        pagerIndicator.setOnTabReselectedListener(new TabPageIndicator.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                //发送广播
                Intent positionIntent = new Intent("reselectedListener");
                positionIntent.putExtra("position", position);
                getActivity().sendBroadcast(positionIntent);
            }
        });
        viewPager.setCurrentItem(mStatus);
    }

    @Override
    protected void initData() {
        mStatus = (int) getActivity().getIntent().getExtras().get(ActionUtil.ACTION_ORDER_STATUS);
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

    @Override
    protected void setListener() {

    }

    @Override
    protected void doClick(View v) {

    }
}
