package com.winguo.lbs.order.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guobi.account.NetworkUtils;
import com.winguo.R;
import com.winguo.activity.ConfirmOrderActivity;
import com.winguo.base.BaseTitleActivity;
import com.winguo.lbs.bean.StoreOrderDetailBean;
import com.winguo.lbs.order.StoreOrderControl;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.utils.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class StoreOrderDetailActivity extends BaseTitleActivity implements View.OnClickListener {
    private ImageView store_order_detail_iv;
    private TextView store_order_detail_status_tv;
    private TextView store_order_detail_status_tv2;
    private TextView store_order_detail_contact_name_tv;
    private TextView store_order_detail_contact_phone_tv;
    private RelativeLayout store_order_detail_code_rl;
    private TextView store_order_detail_code_tv;
    //private TextView store_order_detail_shop_name_tv;
    private TextView store_order_detail_address_tv;
    private StoreOrderDetailItemView store_order_detail_item;
    private Button store_detail_pay_btn;
    private String mStatus;
    private LinearLayout not_net_view;
    private String mId;
    private StoreOrderDetailBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_detail);
        setBackBtn();
        initDatas();
        initView();
        requestData();
    }

    private void initView() {
        store_order_detail_iv = (ImageView) findViewById(R.id.store_order_detail_iv);
        store_order_detail_status_tv = (TextView) findViewById(R.id.store_order_detail_status_tv);
        store_order_detail_status_tv2 = (TextView) findViewById(R.id.store_order_detail_status_tv2);
        store_order_detail_contact_name_tv = (TextView) findViewById(R.id.store_order_detail_name_tv);
        store_order_detail_contact_phone_tv = (TextView) findViewById(R.id.store_order_detail_phone_tv);
        store_order_detail_address_tv = (TextView) findViewById(R.id.store_order_detail_address_tv);
        store_order_detail_code_rl = (RelativeLayout) findViewById(R.id.store_order_detail_code_rl);
        store_order_detail_code_tv = (TextView) findViewById(R.id.store_order_detail_code_tv);
        //store_order_detail_shop_name_tv = (TextView) findViewById(R.id.store_order_detail_shop_name_tv);
        store_detail_pay_btn = (Button) findViewById(R.id.store_detail_pay_btn);
        store_order_detail_item = (StoreOrderDetailItemView) findViewById(R.id.store_order_detail_item);
        not_net_view = (LinearLayout) findViewById(R.id.ll_request);
        findViewById(R.id.btn_request_net).setOnClickListener(this);
        store_detail_pay_btn.setOnClickListener(this);
        not_net_view.setVisibility(View.GONE);
    }

    private void initDatas() {

        mId = (String) getIntent().getExtras().get("store_order_id");
        mStatus = (String) getIntent().getExtras().get("order_status");
        mData = new StoreOrderDetailBean();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        if (NetworkUtils.isConnectNet(this)) {
            LoadDialog.show(this);
            StoreOrderControl.requestStoreOrderDetail(StoreOrderDetailActivity.this, mId, new StoreOrderControl.IStoreOrderDetail() {
                @Override
                public void callback(StoreOrderDetailBean storeOrderDetailBean) {
                    LoadDialog.dismiss(StoreOrderDetailActivity.this);
                    if (storeOrderDetailBean != null) {
                        not_net_view.setVisibility(View.GONE);
                        mData = storeOrderDetailBean;
                        handlerData();
                    } else {
                        not_net_view.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            not_net_view.setVisibility(View.VISIBLE);
        }
    }

    private void handlerData() {
        StoreOrderDetailBean.MakerBean maker = mData.getMaker();
        if ("1".equals(mStatus)) {
            store_order_detail_iv.setBackgroundDrawable(getResources().getDrawable(R.drawable.store_order_detail_wait_pay));
            store_order_detail_status_tv.setText("待付款");
            store_order_detail_status_tv2.setText("等待买家付款...");
            store_detail_pay_btn.setVisibility(View.VISIBLE);
            store_order_detail_code_rl.setVisibility(View.GONE);
        } else if ("2".equals(mStatus)) {
            store_order_detail_iv.setBackgroundDrawable(getResources().getDrawable(R.drawable.store_order_detail_wait_stock_up));
            store_order_detail_status_tv.setText("备货中");
            store_order_detail_status_tv2.setText("卖家努力备货中...");
            store_detail_pay_btn.setVisibility(View.GONE);
            store_order_detail_code_rl.setVisibility(View.GONE);
        } else if ("3".equals(mStatus)) {
            store_order_detail_iv.setBackgroundDrawable(getResources().getDrawable(R.drawable.store_order_detail_wait_pick_up));
            store_order_detail_status_tv.setText("待自提");
            store_order_detail_status_tv2.setText("等待买家提货...");
            store_detail_pay_btn.setVisibility(View.GONE);
            store_order_detail_code_rl.setVisibility(View.VISIBLE);
            store_order_detail_code_tv.setText(maker.getT_juchu_pick_code());
        }
        store_order_detail_contact_name_tv.setText(maker.getM_maker_name());
        store_order_detail_contact_phone_tv.setText(maker.getM_maker_mobile());
        store_order_detail_address_tv.setText(maker.getM_maker_address_ch());
        //store_order_detail_shop_name_tv.setText(maker.getM_maker_name());
        store_order_detail_item.setData(mData.getItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_net:
                mData = null;
                requestData();
                break;
            case R.id.store_detail_pay_btn:
                if (mData != null) {
                    jumpToPay();
                }
                break;
        }
    }

    /**
     * 跳转到支付界面
     */
    private void jumpToPay() {
        Intent intent = new Intent(this, ConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        StoreOrderDetailBean.MakerBean maker = mData.getMaker();
        bundle.putSerializable("commonBeen", setData());
        intent.putExtras(bundle);
        intent.putExtra("isphysical", 1);
        intent.putExtra("name", maker.getT_juchu_delivery_received_name());
        intent.putExtra("phone", maker.getM_customer_tel_mobi());
        intent.putExtra("price", maker.getT_juchu_price());
        intent.putExtra("orderId", maker.getM_merge_order_no());
        startActivity(intent);
    }

    /**
     * 处理 跳转到支付所需的javaBean
     *
     * @return
     */
    private ArrayList<CommonBean> setData() {
        ArrayList<CommonBean> commonBeans = new ArrayList<>();
        List<StoreOrderDetailBean.ItemBean> item = mData.getItem();
        for (int i = 0; i < item.size(); i++) {
            StoreOrderDetailBean.ItemBean itemBean = item.get(i);
            CommonBean commonBean = new CommonBean();
            commonBean.name = itemBean.getM_item_name();
            commonBean.num = Integer.valueOf(itemBean.getT_juchuitem_quantity().split("\\.")[0]);
            commonBean.content = itemBean.getImg_url();
            commonBeans.add(commonBean);
        }
        return commonBeans;
    }
}
