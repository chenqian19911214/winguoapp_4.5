package com.winguo.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.guobi.account.GBAccountError;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.winguo.R;
import com.winguo.alipay.ResultBean;
import com.winguo.app.StartApp;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.BalanceAndCash;
import com.winguo.confirmpay.controller.ConfirmPayController;
import com.winguo.confirmpay.modle.bean.MergeOrderBean;
import com.winguo.confirmpay.modle.bean.PayOrderBean;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.confirmpay.modle.bean.WXPayResultBean;
import com.winguo.confirmpay.view.IConfirmPayView;
import com.winguo.lbs.order.list.StoreOrderListActivity;
import com.winguo.mine.order.MyOrderActivity;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.mine.order.list.PayPwdPopuWindow;
import com.winguo.pay.modle.bean.BalancePayBean;
import com.winguo.pay.modle.bean.ReChargeBean;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.personalcenter.safecenter.paypwd.ModifyPayWayActivity;
import com.winguo.personalcenter.wallet.bankcard.control.BankCardControl;
import com.winguo.personalcenter.wallet.control.MyWalletControl;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Admin on 2017/1/13.
 * 下单成功  支付页面
 */

public class ConfirmOrderActivity extends BaseTitleActivity implements IConfirmPayView,WinguoAcccountManagerUtils.IBalanceAndCashCallback,View.OnClickListener {
    //    private FrameLayout confirm_pay_back_btn;
    private TextView tv_confirm_pay_price;
    private TextView tv_balance_total;
    private RelativeLayout rl_balance;
    private RelativeLayout rl_weixin;
    private RelativeLayout rl_alipay;
    private RelativeLayout rl_unionpay;
    private FrameLayout fl_confirm_pay_container;
    private View noNetView;
    private MergeOrderBean mergeOrderBean;
    private View confirmPayView;
    private ConfirmPayController confirmPayController;
    private final int SDK_PAY_FLAG = 001;
    private ItemBean mItemBean;
    private ArrayList<CommonBean> commonBeen;
    //链接网银的测试环境，00 正式环境；01 测试环境
    private final String mMode = "00";
    private ResultBean resultBean;
    //网银支付后返回的支付状态
    private int UNIONPAY_SUCCESS_CODE = 0;
    private int UNIONPAY_FAIL_CODE = -1;
    private int UNIONPAY_OTHER_CODE = -2;
    private int isphysical;
    private String name;
    private String phone;
    private String orderId;
    private String price;
    public static IWXAPI mWxapi;
    //微信支付结果
    private WXPayResultBroadcastReceiver mBroadcastReceiver;
    private MyWalletControl walletControl;
    private BankCardControl control;
    private double balanceTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);
        setBackBtn();
        initViews();
        initDatas();
        initListener();
    }

    private void initViews() {
        //        CommonUtil.stateSetting(this, R.color.white_top_color);
        //初始化主布局
        //        confirm_pay_back_btn = (FrameLayout) findViewById(R.id.confirm_pay_back_btn);
        fl_confirm_pay_container = (FrameLayout) findViewById(R.id.fl_confirm_pay_container);
        confirmPayView = View.inflate(ConfirmOrderActivity.this, R.layout.confirm_pay_view, null);
        tv_confirm_pay_price = (TextView) confirmPayView.findViewById(R.id.tv_confirm_pay_price);
        tv_balance_total = (TextView) confirmPayView.findViewById(R.id.tv_balance_total);
        rl_balance = (RelativeLayout) confirmPayView.findViewById(R.id.rl_balance);
        rl_weixin = (RelativeLayout) confirmPayView.findViewById(R.id.rl_weixin);
        rl_alipay = (RelativeLayout) confirmPayView.findViewById(R.id.rl_alipay);
        rl_unionpay = (RelativeLayout) confirmPayView.findViewById(R.id.rl_unionpay);
        fl_confirm_pay_container.addView(confirmPayView);

        noNetView = View.inflate(ConfirmOrderActivity.this, R.layout.no_net, null);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        fl_confirm_pay_container.addView(noNetView);
        noNetView.setVisibility(View.GONE);
        confirmPayView.setVisibility(View.GONE);
        if (!NetWorkUtil.isWifiConnected(ConfirmOrderActivity.this) && !NetWorkUtil.isNetworkAvailable(ConfirmOrderActivity.this)) {
            //没有网络
            noNetView.setVisibility(View.VISIBLE);
        }
    }


    private void initDatas() {
        confirmPayController = new ConfirmPayController(this);
        walletControl = new MyWalletControl(this);  //获取当前余额
        Intent intent = getIntent();
        isphysical = intent.getIntExtra("isphysical", 0);
        commonBeen = (ArrayList<CommonBean>) intent.getSerializableExtra("commonBeen");
        orderId = intent.getStringExtra("orderId");
        if (isphysical == 0) {  //商城订单支付
            mItemBean = (ItemBean) intent.getSerializableExtra("mItemBean");
            if (NetWorkUtil.noHaveNet(ConfirmOrderActivity.this)) {
                return;
            }
            LoadDialog.show(ConfirmOrderActivity.this);
            walletControl.getBalanceAndCash(this);

        } else if (isphysical == 1) {  //实体店 订单支付
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");
            //显示支付金额
            price = intent.getStringExtra("price");
            confirmPayView.setVisibility(View.VISIBLE);
            tv_confirm_pay_price.setText("¥ " + String.valueOf(price));

        }
    }

    @Override
    public void getBalanceAndCash(BalanceAndCash balanceAndCash) {
        if (balanceAndCash != null) {
            BigDecimal balance = new BigDecimal(balanceAndCash.getItem().getPurse_balance()+balanceAndCash.getItem().getCash_credit());
            balance.setScale(2, RoundingMode.UP);
            balanceTotal = balance.doubleValue();
            tv_balance_total.setText(String.format(getString(R.string.cart_totle_price2),String.valueOf(balanceTotal)));

            if (NetWorkUtil.noHaveNet(ConfirmOrderActivity.this)) {
                return;
            }
            confirmPayController.getMergeOrderData(this, orderId);
        }
    }

    @Override
    public void balanceAndCashErrorMsg(int message) {
        //获取余额和创客资金 网络请求超时  错误信息处理
        noNetView.setVisibility(View.VISIBLE);
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        ToastUtil.showToast(ConfirmOrderActivity.this, GBAccountError.getErrorMsg(ConfirmOrderActivity.this,message));
    }

    //返回的合并订单数据
    @Override
    public void mergeOrderData(MergeOrderBean mergeOrderBean) {
        this.mergeOrderBean = mergeOrderBean;
        //显示布局
        showView(mergeOrderBean);
    }

    //支付数据返回,开始支付
    @Override
    public void payOrderData(PayOrderBean payOrderBean) {
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        if (payOrderBean == null) {
            //没有网络
            ToastUtil.showToast(ConfirmOrderActivity.this, getResources().getString(R.string.timeout));
        } else {
            if ("success".equals(payOrderBean.status)) {
                final String orderInfo = payOrderBean.url;
                ThreadUtils.runOnBackThread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(ConfirmOrderActivity.this);
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

    //网银数据返回
   /* @Override
    public void unionPayOrderData(UnionpayOrderBean unionpayOrderBean) {
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        if (unionpayOrderBean == null) {
            //没有网络
            ToastUtil.showToast(ConfirmOrderActivity.this, getResources().getString(R.string.timeout));
        } else {
            if (UNIONPAY_SUCCESS_CODE == unionpayOrderBean.code) {
                String tn = unionpayOrderBean.tn;
                UPPayAssistEx.startPay(ConfirmOrderActivity.this, null, null, tn, mMode);
            } else if (UNIONPAY_FAIL_CODE == unionpayOrderBean.code) {
                ToastUtil.showToast(ConfirmOrderActivity.this, getResources().getString(R.string.unionpay_fail));
            } else if (UNIONPAY_OTHER_CODE == unionpayOrderBean.code) {
                ToastUtil.showToast(ConfirmOrderActivity.this, getResources().getString(R.string.unionpay_other_fail));
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {

            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                CommonUtil.printI("网银支付返回数据", result);
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            msg = "支付成功！";
            paySuccessed(null);
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            //支付失败
            payFailed();
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            payAbnormal();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    //支付结果数据返回
    @Override
    public void payResultData(PayResultBean payResultBean) {
        Log.e("balance PayResultBean", "" + payResultBean);
        if (payResultBean == null) {
            payAbnormal();
        } else {
            if (TextUtils.isEmpty(payResultBean.root.data.trade_status)) {
                payAbnormal();
            } else {
                if (TextUtils.equals("1", payResultBean.root.data.trade_status)) {
                    //支付成功
                    paySuccessed(payResultBean);
                } else {
                    //支付失败
                    payFailed();
                }
            }
        }
    }

    /**
     * 微信支付请求回调数据
     * @param wxPayResultBean
     */
    //调起
    @Override
    public void wxPayOrderData(final WXPayResultBean wxPayResultBean) {
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        if (wxPayResultBean == null) {
            //没有网络/请求失败
            ToastUtil.showToast(ConfirmOrderActivity.this, getResources().getString(R.string.timeout));
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

                        /*PayTask alipay = new PayTask(ConfirmOrderActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Message msg = handler.obtainMessage();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        handler.sendMessage(msg);*/
                }
            });
        }
    }

    /**
     *余额支付接口回调数据
     * @param balancePayBean
     */
    @Override
    public void balancePayOrderData(BalancePayBean balancePayBean) {
        Log.e("balancePayOrderData:  ", "" + balancePayBean);
        if (balancePayBean != null) {
            //查询订单支付情况 判断余额支付成功与否
            if (isphysical == 0) {
                confirmPayController.getPayResultData(ConfirmOrderActivity.this, mergeOrderBean.root.order_number);
            } else if (isphysical == 1) {
                confirmPayController.getPayResultData(ConfirmOrderActivity.this, orderId);
            }

        } else {
            //请求超时 网络异常
            //返回null,说明没有网络
            LoadDialog.dismiss(ConfirmOrderActivity.this);
            ToastUtil.showToast(ConfirmOrderActivity.this,getResources().getString(R.string.timeout));
        }
    }

    //忽略
    @Override
    public void getRechargeOrder(ReChargeBean reChargeBean) {
    }

    /**
     * 显示布局
     */
    private void showView(MergeOrderBean mergeOrderBean) {
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        if (mergeOrderBean == null) {
            //返回null,说明没有网络
            noNetView.setVisibility(View.VISIBLE);
        } else {
            //有数据返回
            confirmPayView.setVisibility(View.VISIBLE);
            //显示支付金额
            tv_confirm_pay_price.setText("¥ " + String.valueOf(mergeOrderBean.root.order_amount));
        }
    }

    private void initListener() {
        //        confirm_pay_back_btn.setOnClickListener(this);
        if (rl_weixin != null) {
            rl_weixin.setOnClickListener(this);
        }
        if (rl_alipay != null) {
            rl_alipay.setOnClickListener(this);
        }
        if (rl_balance != null) {
            rl_balance.setOnClickListener(this);
        }
        //        if (rl_unionpay != null) {
        //            rl_unionpay.setOnClickListener(this);
        //        }

        mBroadcastReceiver = new WXPayResultBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WXPayResultBroadcastReceiver");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    //@SuppressWarnings("unchecked")
                    //PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //final String resultInfo = payResult.getResult();
                    //ThreadUtils.runOnBackThread(new Runnable() {
                    //@Override
                    //public void run() {
                    //    resultBean = GsonUtil.json2Obj(resultInfo, ResultBean.class);
                    //
                    //}
                    //});
                    if (confirmPayController != null) {
                        if (isphysical == 0) {
                            confirmPayController.getPayResultData(ConfirmOrderActivity.this, mergeOrderBean.root.order_number);
                        } else if (isphysical == 1) {
                            confirmPayController.getPayResultData(ConfirmOrderActivity.this, orderId);
                        }
                    }
                    //CommonUtil.printI("支付宝同步返回的验证信息", resultInfo);

                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 支付异常（支付中没有网络或者没有收到服务器的支付结果）
     */
    private void payAbnormal() {
        Intent intent = new Intent(ConfirmOrderActivity.this, PayResultAbnormalActivity.class);
        intent.putExtra("isphysical", isphysical);
        startActivity(intent);
        finish();
    }

    /**
     * 支付失败
     */
    private void payFailed() {
        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        if (commonBeen.size() == 1) {
            //跳转到支付失败的页面
            Intent intent = new Intent(ConfirmOrderActivity.this, PayFailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("commonBeen", commonBeen);
            intent.putExtras(bundle);
            if (isphysical == 0) {
                intent.putExtra("isphysical", 0);
                intent.putExtra("mItemBean", mItemBean);
                if (mergeOrderBean != null) {
                    intent.putExtra("price", String.valueOf(mergeOrderBean.root.order_amount));
                }
            } else if (isphysical == 1) {
                intent.putExtra("isphysical", 1);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("price", price);
            }
            startActivity(intent);
            finish();

        }
        if (commonBeen.size() > 1) {
            //跳转到待支付订单列表页面
            if (isphysical == 0) {
                Intent intentMyOrder = new Intent(ConfirmOrderActivity.this, MyOrderActivity.class);
                intentMyOrder.putExtra(ActionUtil.ACTION_ORDER_STATUS, "1");
                startActivity(intentMyOrder);
            } else if (isphysical == 1) {
                Intent intentMyOrder = new Intent(ConfirmOrderActivity.this, StoreOrderListActivity.class);
                intentMyOrder.putExtra("store_order_state", 1);
                startActivity(intentMyOrder);
            }
            finish();
        }
        ToastUtil.showToast(ConfirmOrderActivity.this, "支付失败");
    }

    /**
     * 支付成功
     */
    private void paySuccessed(PayResultBean payResultBean) {
        //         该笔订单是否真实支付成功，需要依赖服务端的异步通知。
        LoadDialog.dismiss(ConfirmOrderActivity.this);
        ToastUtil.showToast(ConfirmOrderActivity.this, "支付成功");
        Intent intent = new Intent(ConfirmOrderActivity.this, PaySuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("commonBeen", commonBeen);
        intent.putExtras(bundle);
        if (isphysical == 0) {
            intent.putExtra("isphysical", 0);
            intent.putExtra("mItemBean", mItemBean);

        } else if (isphysical == 1) {
            intent.putExtra("isphysical", 1);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
        }
        if (payResultBean != null) {
            intent.putExtra("price", payResultBean.root.data.charge_total);
        }

        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.confirm_pay_back_btn:
            //                finish();
            //                break;
           case R.id.rl_balance:
                //余额支付
               if (isphysical == 0) {
                   requestBalancePay(mergeOrderBean.root.order_number);
               } else if (isphysical == 1) {
                   requestBalancePay(orderId);
               }

               break;
            case R.id.rl_alipay:
                //支付宝支付
                CommonUtil.printI("ispsical值", isphysical + "");
                if (isphysical == 0) {
                    if (confirmPayController != null && mergeOrderBean != null) {
                        if (NetWorkUtil.noHaveNet(ConfirmOrderActivity.this)) {
                            return;
                        }
                        LoadDialog.show(ConfirmOrderActivity.this);
                        confirmPayController.getPayOrderData(this, 0, 0, mergeOrderBean.root.order_number);
                    }
                } else if (isphysical == 1) {
                    if (confirmPayController != null) {
                        if (NetWorkUtil.noHaveNet(ConfirmOrderActivity.this)) {
                            return;
                        }
                        LoadDialog.show(ConfirmOrderActivity.this);
                        confirmPayController.getPayOrderData(this, 0, 0, orderId);
                    }
                }
                break;
            //微信支付
            case R.id.rl_weixin:
                if (NetWorkUtil.noHaveNet(ConfirmOrderActivity.this)) {
                    //无网络
                    ToastUtil.showToast(this, getString(R.string.no_net));
                    return;
                }
                //商城订单
                if (isphysical == 0) {
                    if (confirmPayController != null && mergeOrderBean != null) {
                        LoadDialog.show(ConfirmOrderActivity.this);
                        confirmPayController.getPayOrderData(this, 0, 6, mergeOrderBean.root.order_number);
                    }
                    //实体店订单
                } else if (isphysical == 1) {
                    if (confirmPayController != null) {
                        LoadDialog.show(ConfirmOrderActivity.this);
                        confirmPayController.getPayOrderData(this, 0, 6, orderId);
                    }
                }

                break;
            case R.id.rl_unionpay:
              /*  //网银支付
                if (confirmPayController != null && mergeOrderBean != null) {
                    if (NetWorkUtil.noHaveNet(ConfirmOrderActivity.this)) {
                        return;
                    }
                    LoadDialog.show(ConfirmOrderActivity.this);
                    confirmPayController.getUnionpayOrderData(this, mergeOrderBean.root.order_amount, mergeOrderBean.root.order_number);
                }*/
                break;
        }
    }

    private void requestBalancePay(final String orderId) {
        double order_amount = Double.valueOf(mergeOrderBean.root.order_amount);  //应支付订单总价
        if (order_amount <= balanceTotal) {
            //填写支付密码
            PayPwdPopuWindow payPwdPopuWindow = new PayPwdPopuWindow(ConfirmOrderActivity.this, new PayPwdPopuWindow.IPopuWindowConfirmListener() {
                @Override
                public void confirm(String pwd) {
                    LoadDialog.show(ConfirmOrderActivity.this, true);
                    //获取支付密码成功 调起余额支付
                    if (NetWorkUtil.isNetworkAvailable(ConfirmOrderActivity.this)) {
                        confirmPayController.getBalancePayOrderData(ConfirmOrderActivity.this, 0, orderId, pwd);
                    } else {
                        ToastUtil.showToast(ConfirmOrderActivity.this,getResources().getString(R.string.timeout));
                    }
                }

                @Override
                public void forgetPwd() {
                    Intent it = new Intent(ConfirmOrderActivity.this, ModifyPayWayActivity.class);
                    startActivity(it);
                    //ToastUtil.show(mActivity, "跳转到支付密码修改!");
                }
            });
            //payPwdPopuWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            payPwdPopuWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            payPwdPopuWindow.showAtLocation(rl_balance, Gravity.BOTTOM, 0, 0);
        } else {
            //余额不够支付订单
            ToastUtil.showToast(ConfirmOrderActivity.this,"余额不足，请选择其它支付方式！！");
        }
    }


    //接受微信支付回调发过来的广播
    private class WXPayResultBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (confirmPayController != null) {
                if (isphysical == 0) {
                    confirmPayController.getPayResultData(ConfirmOrderActivity.this, mergeOrderBean.root.order_number);
                } else if (isphysical == 1) {
                    confirmPayController.getPayResultData(ConfirmOrderActivity.this, orderId);
                }
            }
        }
    }
}
