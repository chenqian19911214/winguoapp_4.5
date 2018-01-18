package com.winguo.personalcenter.wallet.balance;


import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.personalcenter.MyQRCodeActivity;
import com.winguo.utils.Constants;

/**
 * Created by admin on 2017/12/29.
 * 创客充值
 */

public class CreaterRechargeResultActivity extends BaseActivity {

    private ImageView creater_wallet_back_iv;
    private TextView creater_recharge_state_tv;
    private TextView creater_recharge_result_error;
    private ImageView creater_recharge_result_ic;
    private LinearLayout creater_recharge_success_tip;
    private Button creater_recharge_btn;
    private Button creater_recharge_btn_share;

    @Override
    protected int getLayout() {
        return R.layout.activity_creater_recharge_result;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        creater_wallet_back_iv = (ImageView) findViewById(R.id.creater_wallet_back_iv);
        creater_recharge_state_tv = (TextView) findViewById(R.id.creater_recharge_state_tv);
        creater_recharge_result_error = (TextView) findViewById(R.id.creater_recharge_result_error);
        creater_recharge_success_tip = (LinearLayout) findViewById(R.id.creater_recharge_success_tip);
        creater_recharge_btn = (Button) findViewById(R.id.creater_recharge_btn);
        creater_recharge_btn_share = (Button) findViewById(R.id.creater_recharge_btn_share);
        creater_recharge_result_ic = (ImageView) findViewById(R.id.creater_recharge_result_ic);

        setData();

    }

    private void setData() {
        PayResultBean mRecharge_result = (PayResultBean) getIntent().getExtras().get("recharge_result");
        assert mRecharge_result != null;
        PayResultBean.RootBean.DataBean data = mRecharge_result.root.data;
        String trade_status = data.trade_status;
        //支付成功
        // 支付结果的网络数据:=: {"root":{"data":{"charge_total":0.01,"balance_total":"","winguo_total":"","winguo_largess_total":"","trade_status":0,"pay_type":1}}}
        if ("1".equals(trade_status)) {
            creater_recharge_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //立即购物 回首页
                  startActivity(new Intent(CreaterRechargeResultActivity.this,MainActivity.class));
                }
            });
            //充值成功 通知左侧菜单 显示创客图标
            Intent openSuccess = new Intent();
            openSuccess.setAction(Constants.OPEN_SUCCESS);
            sendBroadcast(openSuccess);

            //支付失败
        } else {
            creater_recharge_btn_share.setVisibility(View.GONE);
            creater_recharge_success_tip.setVisibility(View.GONE);
            creater_recharge_result_error.setVisibility(View.VISIBLE);
            creater_recharge_result_ic.setImageResource(R.drawable.pay_fail);
            creater_recharge_state_tv.setText("充值失败");
            creater_recharge_btn.setText("重新充值");

            creater_recharge_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void setListener() {
        creater_wallet_back_iv.setOnClickListener(this);
        creater_recharge_btn_share.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.creater_wallet_back_iv:
                finish();
                break;
            case R.id.creater_recharge_btn_share:
                //分享好友
                startActivity(new Intent(CreaterRechargeResultActivity.this, MyQRCodeActivity.class));
                break;
        }
    }

}
