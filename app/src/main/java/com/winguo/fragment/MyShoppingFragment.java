package com.winguo.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.winguo.R;
import com.winguo.adapter.ShoppingViewPagerAdapter;
import com.winguo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class MyShoppingFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private TextView tv_store_cart_btn;
    private TextView tv_physical_store_cart_btn;
    private FrameLayout cart_fragment_container;
    private int CHACKEDSTATE = 0;
    private int lineMoveDistance;
    private View view_indicate_line;
    private FragmentManager fragmentManager;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager shopping_viewPager;
    private StoreCartFragment storeCartFragment;
    private PhysicalStoreCartFragment physicalStoreCartFragment;
    private ShoppingViewPagerAdapter adapter;

    @Override
    protected void initView(View view) {
        initViews(view);
    }

    protected int getLayout() {
        return R.layout.fragment_my_shopping;
    }
    @Override
    protected void initData() {
        initDatas();
    }

   @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (CHACKEDSTATE != 0) {
                changeState(0);
                shopping_viewPager.setCurrentItem(0);
                CHACKEDSTATE = 0;
            }else{
                storeCartFragment.getData();
            }
        }
    }

    @Override
    protected void setListener() {
        initListener();
    }

    @Override
    protected void doClick(View v) {

        switch (v.getId()) {
            case R.id.tv_store_cart_btn:
                if (CHACKEDSTATE != 0) {
                    changeState(0);
                    shopping_viewPager.setCurrentItem(0);
                    CHACKEDSTATE = 0;
                }
                break;
            case R.id.tv_physical_store_cart_btn:

                if (CHACKEDSTATE != 1) {
                    changeState(1);
                    shopping_viewPager.setCurrentItem(1);
                    CHACKEDSTATE = 1;
                }
                break;
        }

    }

    private void changeState(int state) {

        ViewPropertyAnimator.animate(tv_store_cart_btn).scaleX(state == 0 ? 1.1f : 1.0f).scaleY(state == 0 ? 1.1f : 1.0f);
        tv_store_cart_btn.setTextColor(state == 0 ? getResources().getColor(R.color.goods_theme_color) : getResources().getColor(R.color.cart_title_color));
        ViewPropertyAnimator.animate(tv_physical_store_cart_btn).scaleX(state == 1 ? 1.1f : 1.0f).scaleY(state == 1 ? 1.1f : 1.0f);
        tv_physical_store_cart_btn.setTextColor(state == 1 ? getResources().getColor(R.color.goods_theme_color) : getResources().getColor(R.color.cart_title_color));
        if (state == 0) {
            storeCartFragment.getData();
            ViewPropertyAnimator.animate(view_indicate_line).translationX(0);
        } else if (state == 1) {
            physicalStoreCartFragment.getData();
            ViewPropertyAnimator.animate(view_indicate_line).translationX(lineMoveDistance);
        }
    }

    /**
     * 初始化布局
     */
    private void initViews(View view) {
        tv_store_cart_btn = view.findViewById(R.id.tv_store_cart_btn);
        //默认选中的是商城,缩放文字
        ViewPropertyAnimator.animate(tv_store_cart_btn).scaleX(1.1f).scaleY(1.1f);
        tv_store_cart_btn.setTextColor(getResources().getColor(R.color.goods_theme_color));
        view_indicate_line = view.findViewById(R.id.view_indicate_line);
        //1. 指示线宽度 初始化 (屏幕宽度的四分之一)
        int screenWidth = getResources().getDisplayMetrics().widthPixels;//屏幕宽度
        //指示线滑动的距离
        lineMoveDistance = screenWidth / 2;
        view_indicate_line.getLayoutParams().width = screenWidth / 4;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view_indicate_line.getLayoutParams();
        lp.leftMargin = lineMoveDistance / 4;
        view_indicate_line.setLayoutParams(lp);
        shopping_viewPager = view.findViewById(R.id.shopping_viewPager_id);

        shopping_viewPager.setAdapter(adapter);
        shopping_viewPager.setCurrentItem(0);
        tv_physical_store_cart_btn = view.findViewById(R.id.tv_physical_store_cart_btn);
        cart_fragment_container = view.findViewById(R.id.cart_fragment_container);

    }

    /**
     * 初始化数据
     */
    private void initDatas() {

        fragmentManager = getFragmentManager();
        storeCartFragment = new StoreCartFragment();
        physicalStoreCartFragment = new PhysicalStoreCartFragment();
        fragments.add(storeCartFragment);
        fragments.add(physicalStoreCartFragment);
        adapter = new ShoppingViewPagerAdapter(fragmentManager, fragments);


    }

    /**
     * 初始化事件监听
     */
    private void initListener() {
        tv_store_cart_btn.setOnClickListener(this);
        tv_physical_store_cart_btn.setOnClickListener(this);
        shopping_viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeState(position);
        CHACKEDSTATE = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shopping_viewPager.removeOnPageChangeListener(this);
    }
}
