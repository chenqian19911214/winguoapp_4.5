package com.winguo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.lbs.order.list.StoreOrderListActivity;
import com.winguo.mine.order.MyOrderActivity;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.net.GlideUtil;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.utils.ActionUtil;

import java.util.ArrayList;

/**
 * Created by Admin on 2017/2/6.
 */

public class PayFailActivity extends BaseTitleActivity implements View.OnClickListener {

    private ArrayList<CommonBean> commonBeen;
    private ItemBean mItemBean;
    //    private FrameLayout pay_fail_back_btn;

    private TextView pay_fail_consignee_text;
    private TextView pay_fail_consignee_phone_text;
    private TextView pay_fail_consignee_address_text;

    private ImageView iv_pay_fail_icon;
    private TextView tv_pay_fail_goods_name;
    private TextView tv_pay_fail_goods_count;
    private TextView tv_pay_fail_goods_price;

    private TextView pay_again_btn;
    private String price;
    private int isphysical;
    private String name;
    private String phone;
    private LinearLayout ll_store_address;
    private RelativeLayout rl_physical_address;
    private TextView physical_fail_consignee_text;
    private TextView physical_pay_fail_consignee_phone_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fail);
        setBackBtn();
        initViews();
        initDatas();
        initListener();
    }

    private void initViews() {
        Intent intent = getIntent();
        isphysical = intent.getIntExtra("isphysical", 0);
        price = intent.getStringExtra("price");
        commonBeen = (ArrayList<CommonBean>) intent.getSerializableExtra("commonBeen");
        if (isphysical == 0) {
            mItemBean = (ItemBean) intent.getSerializableExtra("mItemBean");
        } else if (isphysical == 1) {
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");
        }
        initView();
    }

    private void initDatas() {
        //给收货地址赋值
        if (isphysical == 0) {
            ll_store_address.setVisibility(View.VISIBLE);
            rl_physical_address.setVisibility(View.GONE);
            if (mItemBean != null) {
                pay_fail_consignee_text.setText(getResources().getString(R.string.pay_result_consignee_text) + mItemBean.dinfo_received_name);
                if (TextUtils.isEmpty(mItemBean.dinfo_mobile)) {
                    pay_fail_consignee_phone_text.setText(mItemBean.dinfo_tel);
                }
                pay_fail_consignee_phone_text.setText(mItemBean.dinfo_mobile);
                pay_fail_consignee_address_text.setText(getResources().getString(R.string.pay_result_receiver_address_text) + mItemBean.provinceName + " " + mItemBean.cityName + " " + mItemBean.areaName + " " + mItemBean.townName + " " +
                        mItemBean.dinfo_address);
            }
        } else if (isphysical == 1) {
            ll_store_address.setVisibility(View.GONE);
            rl_physical_address.setVisibility(View.VISIBLE);
            physical_fail_consignee_text.setText(getResources().getString(R.string.pay_result_consignee_text) + name);
            physical_pay_fail_consignee_phone_text.setText(phone);
        }
        //给支付失败的商品展示
        if (commonBeen != null) {
            CommonBean commonBean = commonBeen.get(0);
            GlideUtil.getInstance().loadImage(PayFailActivity.this, commonBean.content,
                    R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, iv_pay_fail_icon);
            tv_pay_fail_goods_name.setText(commonBean.name);
            tv_pay_fail_goods_count.setText("x " + commonBean.num);
        }
        tv_pay_fail_goods_price.setText("¥ " + price);

    }

    private void initListener() {
        //        pay_fail_back_btn.setOnClickListener(this);
        pay_again_btn.setOnClickListener(this);
    }

    private void initView() {
        //        pay_fail_back_btn = (FrameLayout) findViewById(R.id.pay_fail_back_btn);
        ll_store_address = (LinearLayout) findViewById(R.id.ll_store_address);
        pay_fail_consignee_text = (TextView) findViewById(R.id.pay_fail_consignee_text);
        pay_fail_consignee_phone_text = (TextView) findViewById(R.id.pay_fail_consignee_phone_text);
        pay_fail_consignee_address_text = (TextView) findViewById(R.id.pay_fail_consignee_address_text);


        rl_physical_address = (RelativeLayout) findViewById(R.id.rl_physical_address);
        physical_fail_consignee_text = (TextView) findViewById(R.id.physical_pay_fail_consignee_text);
        physical_pay_fail_consignee_phone_text = (TextView) findViewById(R.id.physical_pay_fail_consignee_phone_text);

        iv_pay_fail_icon = (ImageView) findViewById(R.id.iv_pay_fail_icon);
        tv_pay_fail_goods_name = (TextView) findViewById(R.id.tv_pay_fail_goods_name);
        tv_pay_fail_goods_count = (TextView) findViewById(R.id.tv_pay_fail_goods_count);
        tv_pay_fail_goods_price = (TextView) findViewById(R.id.tv_pay_fail_goods_price);

        pay_again_btn = (TextView) findViewById(R.id.pay_again_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.pay_fail_back_btn:
            //                finish();
            //                break;
            case R.id.pay_again_btn:
                gotoPay();
                break;
        }
    }

    private void gotoPay() {
        //跳转到待支付订单列表页面
        if (isphysical == 0) {
            Intent intentMyOrder = new Intent(PayFailActivity.this, MyOrderActivity.class);
            intentMyOrder.putExtra(ActionUtil.ACTION_ORDER_STATUS, 1);
            startActivity(intentMyOrder);
        } else if (isphysical == 1) {
            Intent intentMyOrder = new Intent(PayFailActivity.this, StoreOrderListActivity.class);
            intentMyOrder.putExtra("store_order_state", 1);
            startActivity(intentMyOrder);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        gotoPay();
    }
}
