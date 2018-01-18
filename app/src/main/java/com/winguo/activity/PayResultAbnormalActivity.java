package com.winguo.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.lbs.order.list.StoreOrderListActivity;
import com.winguo.mine.order.MyOrderActivity;
import com.winguo.utils.ActionUtil;


/**
 * 支付结果
 * Created by Admin on 2017/2/28.
 */

public class PayResultAbnormalActivity extends BaseTitleActivity implements View.OnClickListener{

    private TextView pay_result_abnormal_jump_order;
    private FrameLayout pay_result_abnormal_back_btn;
    private ImageView iv_pay_result_abnormal;
    private int isphysical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult_abnormal);
        setBackBtn();
        initViews();
        initDatas();
        initListener();
    }

    private void initListener() {
        pay_result_abnormal_jump_order.setOnClickListener(this);
        pay_result_abnormal_back_btn.setOnClickListener(this);
    }

    private void initViews() {
        iv_pay_result_abnormal = (ImageView) findViewById(R.id.iv_pay_result_abnormal);
        pay_result_abnormal_back_btn = (FrameLayout) findViewById(R.id.pay_result_abnormal_back_btn);
        pay_result_abnormal_jump_order = (TextView) findViewById(R.id.pay_result_abnormal_jump_order);
    }

    private void initDatas() {
        isphysical = getIntent().getIntExtra("isphysical", 0);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=1/4;
        iv_pay_result_abnormal.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pay_result_abnormal,options));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_result_abnormal_back_btn:
                finish();
                break;
            case R.id.pay_result_abnormal_jump_order:
                //跳转到全部订单列表页面
                if (isphysical==0) {
                    Intent intentMyOrder = new Intent(PayResultAbnormalActivity.this, MyOrderActivity.class);
                    intentMyOrder.putExtra(ActionUtil.ACTION_ORDER_STATUS, 0);
                    startActivity(intentMyOrder);
                }else if (isphysical==1){
                    Intent intentMyOrder = new Intent(PayResultAbnormalActivity.this, StoreOrderListActivity.class);
                    intentMyOrder.putExtra("store_order_state", 0);
                    startActivity(intentMyOrder);
                }
                finish();
                break;
        }
    }
}
