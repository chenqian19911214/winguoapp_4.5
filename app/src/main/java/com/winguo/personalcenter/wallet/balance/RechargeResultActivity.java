package com.winguo.personalcenter.wallet.balance;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;


/**
 * @author hcpai
 * @time 2017/10/18  17:50
 * @desc ${TODD}
 * 普通充值 支付结果
 */

public class RechargeResultActivity extends BaseActivity2 {

    private ImageView wallet_back_iv;
    private ImageView recharge_iv;
    private TextView recharge_state_tv;
    private TextView recharge_price_tv;
    private Button recharge_btn;
    private Button recharge_btn_wallet;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_recharge_result;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        wallet_back_iv = (ImageView) findViewById(R.id.wallet_back_iv);
        recharge_iv = (ImageView) findViewById(R.id.recharge_iv);
        recharge_state_tv = (TextView) findViewById(R.id.recharge_state_tv);
        recharge_price_tv = (TextView) findViewById(R.id.recharge_price_tv);
        recharge_btn = (Button) findViewById(R.id.recharge_btn);
        recharge_btn_wallet = (Button) findViewById(R.id.recharge_btn_wallet);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        PayResultBean mRecharge_result = (PayResultBean) getIntent().getExtras().get("recharge_result");
        assert mRecharge_result != null;
        PayResultBean.RootBean.DataBean data = mRecharge_result.root.data;
        String trade_status = data.trade_status;
        //充值金额
        //支付成功
        // 支付结果的网络数据:=: {"root":{"data":{"charge_total":0.01,"balance_total":"","winguo_total":"","winguo_largess_total":"","trade_status":0,"pay_type":1}}}
        if ("1".equals(trade_status)) {
            recharge_price_tv.setText(String.format(getString(R.string.recharge_price), data.charge_total));
            recharge_iv.setImageResource(R.drawable.pay_success);
            recharge_state_tv.setText("充值成功");
            recharge_btn_wallet.setVisibility(View.VISIBLE);
            recharge_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RechargeResultActivity.this, MainActivity.class));
                }
            });
            Intent recharge = new Intent();
            recharge.putExtra("result",true);
            setResult(RESULT_OK,recharge);
            //支付失败
        } else {
            recharge_price_tv.setText("充值失败");
            recharge_iv.setImageResource(R.drawable.pay_fail);
            recharge_state_tv.setText("充值失败");
            recharge_btn.setText("重新充值");
            recharge_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        wallet_back_iv.setOnClickListener(this);
        recharge_btn_wallet.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {

    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_back_iv:
                finish();
                break;
            case R.id.recharge_btn_wallet:  //我的钱包
                startActivity(new Intent(RechargeResultActivity.this, MyWalletCenterActivity.class));
                break;

        }
    }
}
