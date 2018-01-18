package com.winguo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.winguo.R;
import com.winguo.base.BaseTitleFragmentActivity;
import com.winguo.fragment.PhysicalStoreCartFragment;
import com.winguo.fragment.StoreCartFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class NewCartActivity extends BaseTitleFragmentActivity implements View.OnClickListener {
    private TextView tv_store_cart_btn;
    private TextView tv_physical_store_cart_btn;
    private FrameLayout cart_fragment_container;
    private View view_indicate_line;
    //是否被选中
    private int CHACKEDSTATE = 0;
    private List<Fragment> fragments = new ArrayList<>();
    private int lineMoveDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cart);
        setBackBtn();
        initViews();
        initDatas();
        initListener();
    }

    /**
     * 初始化布局
     */
    private void initViews() {
        tv_store_cart_btn = findViewById(R.id.tv_store_cart_btn);
        //默认选中的是商城,缩放文字
        ViewPropertyAnimator.animate(tv_store_cart_btn).scaleX(1.1f).scaleY(1.1f);
        tv_store_cart_btn.setTextColor(getResources().getColor(R.color.cart_title_color2));
        view_indicate_line = findViewById(R.id.view_indicate_line);
        //1. 指示线宽度 初始化 (屏幕宽度的四分之一)
        int screenWidth = getResources().getDisplayMetrics().widthPixels;//屏幕宽度
        //指示线滑动的距离
        lineMoveDistance = screenWidth / 2;
        view_indicate_line.getLayoutParams().width = screenWidth / 4;
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) view_indicate_line.getLayoutParams();
        lp.leftMargin=lineMoveDistance/4;
        view_indicate_line.setLayoutParams(lp);

        tv_physical_store_cart_btn =  findViewById(R.id.tv_physical_store_cart_btn);
        cart_fragment_container =  findViewById(R.id.cart_fragment_container);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        fragments.add(new StoreCartFragment());
        fragments.add(new PhysicalStoreCartFragment());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.cart_fragment_container, fragments.get(0)).commit();

    }

    /**
     * 初始化事件监听
     */
    private void initListener() {
        tv_store_cart_btn.setOnClickListener(this);
        tv_physical_store_cart_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_store_cart_btn:
                if (CHACKEDSTATE != 0) {
                    changeState(0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.cart_fragment_container, fragments.get(0)).commit();
                    CHACKEDSTATE = 0;
                }
                break;
            case R.id.tv_physical_store_cart_btn:
                if (CHACKEDSTATE != 1) {
                    changeState(1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.cart_fragment_container, fragments.get(1)).commit();
                    CHACKEDSTATE = 1;
                }
                break;
        }

    }

    private void changeState(int state) {

        ViewPropertyAnimator.animate(tv_store_cart_btn).scaleX(state == 0 ? 1.1f : 1.0f).scaleY(state == 0 ? 1.1f : 1.0f);
        tv_store_cart_btn.setTextColor(state == 0 ? getResources().getColor(R.color.cart_title_color2) : getResources().getColor(R.color.cart_title_color));
        ViewPropertyAnimator.animate(tv_physical_store_cart_btn).scaleX(state == 1 ? 1.1f : 1.0f).scaleY(state == 1 ? 1.1f : 1.0f);
        tv_physical_store_cart_btn.setTextColor(state == 1 ? getResources().getColor(R.color.cart_title_color2) : getResources().getColor(R.color.cart_title_color));
        if (state == 0) {
            ViewPropertyAnimator.animate(view_indicate_line).translationX(0);
        } else if (state == 1) {
            ViewPropertyAnimator.animate(view_indicate_line).translationX(lineMoveDistance);
        }

    }
}
