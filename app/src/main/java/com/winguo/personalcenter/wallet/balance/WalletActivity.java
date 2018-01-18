package com.winguo.personalcenter.wallet.balance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.app.StartApp;
import com.winguo.confirmpay.controller.ConfirmPayController;
import com.winguo.confirmpay.modle.bean.MergeOrderBean;
import com.winguo.confirmpay.modle.bean.PayOrderBean;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.confirmpay.modle.bean.WXPayResultBean;
import com.winguo.confirmpay.view.IConfirmPayView;
import com.winguo.pay.modle.bean.BalancePayBean;
import com.winguo.pay.modle.bean.ReChargeBean;
import com.winguo.personalcenter.setting.ExplainActivity;
import com.winguo.utils.CashierInputFilter;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.ToastUtil;

import java.util.Map;

/**
 * @author hcpai
 * @time 2017/10/17  15:28
 * @desc 预存充值
 */

public class WalletActivity extends BaseActivity2 implements IConfirmPayView {

    private ImageView wallet_back_iv;
    private CheckBox wallet_bankcard_cb;
    private CheckBox wallet_wx_cb;
    private CheckBox wallet_ali_cb;
    private CheckBox wallet_recharge_cb;
    private TextView wallet_recharge_cb_protocol;
    private EditText wallet_number_et;
    private Button wallet_charge_btn;
    private ConfirmPayController confirmPayController;
    private static final int SDK_PAY_FLAG = 1;
    /**
     * 选择充值方式:-1 没有选择  1 微信支付  2 支付宝支付  3 银联支付
     */
    private int payMode = -1;
    private String mOrderNumber;
    private WxPayResultBroadcast resultBroadcast;
    private String intentaction;


    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_wallet;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        intentaction = getIntent().getAction();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.WXPAY_RESULT);
        resultBroadcast = new WxPayResultBroadcast();
        registerReceiver(resultBroadcast, filter);

        wallet_back_iv = (ImageView) findViewById(R.id.wallet_back_iv);
        wallet_bankcard_cb = (CheckBox) findViewById(R.id.wallet_bankcard_cb);
        wallet_wx_cb = (CheckBox) findViewById(R.id.wallet_wx_cb);
        wallet_ali_cb = (CheckBox) findViewById(R.id.wallet_ali_cb);
        wallet_recharge_cb = (CheckBox) findViewById(R.id.wallet_recharge_cb);
        wallet_recharge_cb_protocol = (TextView) findViewById(R.id.wallet_recharge_cb_protocol);
        wallet_charge_btn = (Button) findViewById(R.id.wallet_charge_btn);

        wallet_back_iv = findViewById(R.id.wallet_back_iv);
        wallet_wx_cb = findViewById(R.id.wallet_wx_cb);
        wallet_ali_cb = findViewById(R.id.wallet_ali_cb);
        wallet_number_et = findViewById(R.id.wallet_number_et);
        wallet_charge_btn = findViewById(R.id.wallet_charge_btn);
        if (intentaction != null) {
            String rechargeNumber = getIntent().getStringExtra("rechargeNumber");
            switch (intentaction) {
                case Constants.STUDENT_CREATE_RECHARGE_FLAG://学生创客
                    wallet_number_et.setFocusable(false);
                    wallet_number_et.setEnabled(false);
                    wallet_number_et.setText(rechargeNumber);
                    break;
            }
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        //对输入框进行金额格式过滤
        InputFilter[] filters = {new CashierInputFilter()};
        wallet_number_et.setFilters(filters);
        confirmPayController = new ConfirmPayController(this);
        wallet_wx_cb.setChecked(true);
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        wallet_back_iv.setOnClickListener(this);
        wallet_bankcard_cb.setOnClickListener(this);
        wallet_wx_cb.setOnClickListener(this);
        wallet_ali_cb.setOnClickListener(this);
        wallet_charge_btn.setOnClickListener(this);
        wallet_recharge_cb_protocol.setOnClickListener(this);

    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            //请求后台支付结果
            case SDK_PAY_FLAG:
                confirmPayController.getPayResultData(this, mOrderNumber);//支付宝 支付结果处理
                break;
        }
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
            case R.id.wallet_recharge_cb_protocol:
                //协议呢
                //点击阅读条款
                Intent agreement = new Intent(WalletActivity.this, ExplainActivity.class);
                agreement.putExtra("assetUrl", Constants.assetRechargeURL);
                startActivity(agreement);
                break;
            //选择银行卡
            case R.id.wallet_bankcard_cb:
                wallet_bankcard_cb.setChecked(true);
                wallet_ali_cb.setChecked(false);
                wallet_wx_cb.setChecked(false);
                break;
            //选择微信支付
            case R.id.wallet_wx_cb:
                wallet_bankcard_cb.setChecked(false);
                wallet_ali_cb.setChecked(false);
                wallet_wx_cb.setChecked(true);
                break;
            //选择支付宝支付
            case R.id.wallet_ali_cb:
                wallet_bankcard_cb.setChecked(false);
                wallet_ali_cb.setChecked(true);
                wallet_wx_cb.setChecked(false);
                break;
            //充值
            case R.id.wallet_charge_btn:
                if (wallet_recharge_cb.isChecked()) {
                    String number = wallet_number_et.getText().toString();
                    if (intentaction != null) {
                        switch (intentaction) {
                            case Constants.STUDENT_CREATE_RECHARGE_FLAG://学生创客
                                selectPayWayAndCreate(number, 20);  // op 20 为创客功劳
                                break;
                            default:
                        }
                    } else {
                        if (TextUtils.isEmpty(number)) {
                            ToastUtil.showToast(this, "充值金额不能为空!");
                            return;
                        }
                        Double number1 = Double.valueOf(number);
                        if (number1 <= 0) {
                            ToastUtil.showToast(this, "充值金额至少大于零!");
                            return;
                        }
                        selectPayWayAndCreate(number, 21);   // op 21 表示不可提现   1 表示可提现
                    }
                } else {
                    ToastUtil.showToast(WalletActivity.this,"请先同意预存充值协议");
                }
                break;
        }
    }

    private void selectPayWayAndCreate(String number, int op) {
        //至少选择一个支付方式
        if (wallet_wx_cb.isChecked() | wallet_ali_cb.isChecked()) {
            if (NetWorkUtil.noHaveNet(this)) {
                return;
            }
            if (wallet_wx_cb.isChecked()) {
                payMode = 1;
            } else {
                payMode = 2;
            }
            LoadDialog.show(this);
            confirmPayController.getRechargeOrder(this, op, Double.valueOf(number));

        } else {
            ToastUtil.showToast(this, "请选择支付方式!");
        }
    }

    //合并商品订单(忽略)
    @Override
    public void mergeOrderData(MergeOrderBean mergeOrderBean) {
    }

    //支付宝请求支付
    @Override
    public void payOrderData(PayOrderBean payOrderBean) {
        LoadDialog.dismiss(this);
        if (payOrderBean == null) {
            //没有网络
            ToastUtil.showToast(this, getResources().getString(R.string.timeout));
        } else {
            if ("success".equals(payOrderBean.status)) {
                final String orderInfo = payOrderBean.url;
                ThreadUtils.runOnBackThread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(WalletActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Message msg = handler.obtainMessage();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
            }
        }
    }

    //支付结果
    @Override
    public void payResultData(PayResultBean payResultBean) {
        Log.e("支付结果:", "payResultData---->trade_status=" + payResultBean.root.data.trade_status);
        if (intentaction != null) {  //学生创客 充值2000

            if (TextUtils.isEmpty(payResultBean.root.data.trade_status)) {
                //支付异常
                ToastUtil.showToast(this, "支付异常,请稍后重试!");
            } else {
                //跳转到充值结果界面
                // TODO: 2017/12/29  支付宝支付结果
                Intent intent = new Intent();
                intent.putExtra("recharge_result", payResultBean);
                intent.setClass(WalletActivity.this, CreaterRechargeResultActivity.class);
                startActivityForResult(intent, RECHARGE_RESULT);
            }

        } else {
            //普通充值
            if (TextUtils.isEmpty(payResultBean.root.data.trade_status)) {
                //支付异常
                ToastUtil.showToast(this, "支付异常,请稍后重试!");
            } else {
                wallet_number_et.setText("");
                //跳转到充值结果界面
                // TODO: 2017/12/29  支付宝支付结果
                Intent intent = new Intent();
                intent.putExtra("recharge_result", payResultBean);
                intent.setClass(WalletActivity.this, RechargeResultActivity.class);
                startActivityForResult(intent, RECHARGE_RESULT);
            }
        }

    }

    private final static int RECHARGE_RESULT = 0X456;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RECHARGE_RESULT && data != null) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    //微信支付订单
    @Override
    public void wxPayOrderData(final WXPayResultBean wxPayResultBean) {
        LoadDialog.dismiss(this);
        if (wxPayResultBean == null) {
            //没有网络/请求失败
            ToastUtil.showToast(this, getResources().getString(R.string.timeout));
        } else {
            ThreadUtils.runOnBackThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("微信支付bean", "run---->wxPayResultBean=" + wxPayResultBean);

                    PayReq request = new PayReq();
                    request.appId = wxPayResultBean.getAppid();
                    request.partnerId = wxPayResultBean.getPartnerid();
                    request.prepayId = wxPayResultBean.getPrepayid();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = wxPayResultBean.getNoncestr();
                    request.timeStamp = wxPayResultBean.getTimestamp();
                    request.sign = wxPayResultBean.getSign();
                    StartApp.mWxApi.sendReq(request);
                }
            });
        }
    }

    @Override
    public void balancePayOrderData(BalancePayBean balancePayBean) {//无用 忽略
    }

    //余额充值
    @Override
    public void getRechargeOrder(ReChargeBean reChargeBean) {
        if (NetWorkUtil.noHaveNet(this) || reChargeBean == null) {
            ToastUtil.showToast(this, "网络断开,请检查网络");
            LoadDialog.dismiss(this);
            return;
        }
        if (reChargeBean.getCode() != 0) {
            LoadDialog.dismiss(WalletActivity.this);
            ToastUtil.showToast(WalletActivity.this, reChargeBean.getText());

        } else {
            mOrderNumber = reChargeBean.getOrderNumber();
            switch (payMode) {
                //微信支付
                case 1:
                    confirmPayController.getPayOrderData(this, 0, 6, mOrderNumber);
                    break;
                //支付宝支付
                case 2:
                    confirmPayController.getPayOrderData(this, 0, 0, mOrderNumber);
                    break;
            }
        }

    }

    private class WxPayResultBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.WXPAY_RESULT.equals(action)) {
                // TODO: 2017/12/29  微信登录成功
                int respCode = intent.getIntExtra("respCode", 0);
                switch (respCode) {
                    //支付成功  返回前页刷新数据
                    case 0:
                        Intent wxPay = new Intent();
                        wxPay.putExtra("result", true);
                        WalletActivity.this.setResult(RESULT_OK, wxPay);
                        break;
                    //支付失败  不刷新前页余额  跳转支付失败页面
                    case -1:
                        //支付取消   跳转支付失败页面
                        break;
                    case -2:
                        break;
                }
                confirmPayController.getPayResultData(WalletActivity.this, mOrderNumber);//微信 支付结果处理

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(resultBroadcast);
    }
}
