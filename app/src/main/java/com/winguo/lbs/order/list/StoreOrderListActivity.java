package com.winguo.lbs.order.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleFragmentActivity;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class StoreOrderListActivity extends BaseTitleFragmentActivity implements View.OnClickListener {

    private TextView mStore_order_all_tv;
    private TextView mStore_order_wait_pay_tv;
    private TextView mStore_order_wait_stock_up_tv;
    private TextView mStore_order_wait_pick_up_tv;
    //全部
    private StoreOrderAllFragment mStoreOrderAllFragment;
    //待付款
    private StoreOrderWaitPayFragment mStoreOrderWaitPayFragment;
    //待备货
    private StoreOrderWaitStockUpFragment mStoreOrderWaitStockUpFragment;
    //待提货
    private StoreOrderWaitPickUpFragment mStoreOrderWaitPickUpFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_list);
        setBackBtn();
        initView();
    }

    private void initView() {
        mStore_order_all_tv = (TextView) findViewById(R.id.store_order_all_tv);
        mStore_order_all_tv.setOnClickListener(this);

        mStore_order_wait_pay_tv = (TextView) findViewById(R.id.store_order_wait_pay_tv);
        mStore_order_wait_pay_tv.setOnClickListener(this);

        mStore_order_wait_stock_up_tv = (TextView) findViewById(R.id.store_order_wait_stock_up_tv);
        mStore_order_wait_stock_up_tv.setOnClickListener(this);

        mStore_order_wait_pick_up_tv = (TextView) findViewById(R.id.store_order_wait_pick_up_tv);
        mStore_order_wait_pick_up_tv.setOnClickListener(this);
        Intent intent = getIntent();
        int store_order_state = intent.getIntExtra("store_order_state", 0);
        setState(store_order_state);
    }

    private void setState(int state) {
        showFragment(state);
        changeTitleColor(state);
    }

    /**
     * 显示fragment
     *
     * @param type
     */
    private void showFragment(int type) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (type) {
            case 0:
                if (mStoreOrderAllFragment == null) {
                    mStoreOrderAllFragment = new StoreOrderAllFragment();
                }
                ft.replace(R.id.store_container, mStoreOrderAllFragment);
                break;
            case 1:
                if (mStoreOrderWaitPayFragment == null) {
                    mStoreOrderWaitPayFragment = new StoreOrderWaitPayFragment();
                }
                ft.replace(R.id.store_container, mStoreOrderWaitPayFragment);
                break;
            case 2:
                if (mStoreOrderWaitStockUpFragment == null) {
                    mStoreOrderWaitStockUpFragment = new StoreOrderWaitStockUpFragment();
                }
                ft.replace(R.id.store_container, mStoreOrderWaitStockUpFragment);
                break;
            case 3:
                if (mStoreOrderWaitPickUpFragment == null) {
                    mStoreOrderWaitPickUpFragment = new StoreOrderWaitPickUpFragment();
                }
                ft.replace(R.id.store_container, mStoreOrderWaitPickUpFragment);
                break;
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全部
            case R.id.store_order_all_tv:
               setState(0);
                break;
            //待付款
            case R.id.store_order_wait_pay_tv:
                setState(1);
                break;
            //待备货
            case R.id.store_order_wait_stock_up_tv:
                setState(2);
                break;
            //待自提
            case R.id.store_order_wait_pick_up_tv:
                setState(3);
                break;
        }
    }

    /**
     * 改变标题颜色
     *
     * @param type
     */
    private void changeTitleColor(int type) {
        switch (type) {
            case 0:
                mStore_order_all_tv.setTextColor(getResources().getColor(R.color.viewpager_select));
                mStore_order_wait_pay_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_stock_up_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_pick_up_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                break;
            case 1:
                mStore_order_all_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_pay_tv.setTextColor(getResources().getColor(R.color.viewpager_select));
                mStore_order_wait_stock_up_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_pick_up_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                break;
            case 2:
                mStore_order_all_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_pay_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_stock_up_tv.setTextColor(getResources().getColor(R.color.viewpager_select));
                mStore_order_wait_pick_up_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                break;
            case 3:
                mStore_order_all_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_pay_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_stock_up_tv.setTextColor(getResources().getColor(R.color.viewpager_default));
                mStore_order_wait_pick_up_tv.setTextColor(getResources().getColor(R.color.viewpager_select));
                break;
        }
    }
}
