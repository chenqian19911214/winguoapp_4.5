package com.winguo.personalcenter.wallet.balance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.guobi.account.WinguoAccountBalance;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.bean.BalanceAndCash;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;
import com.winguo.personalcenter.wallet.WalletCashCouponActivity;
import com.winguo.personalcenter.wallet.control.MyWalletControl;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceCallback;
import com.winguo.utils.Constants;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/10/24.
 * 账户余额管理  （充值 提现）
 */

public class MyBalanceManagerActivity extends BaseActivity implements WinguoAcccountManagerUtils.IBalanceAndCashCallback {

    private FrameLayout top_back;
    private TextView layout_title;
    private TextView mywallet_balance_manager;
    private LinearLayout mywallet_balance_ll;
    private RelativeLayout mywallet_center_balance_rl;
    private TextView mywallet_balance_manager_recharge;
    private TextView mywallet_balance_manager_withdraw_cash;
    private MyWalletControl control;
    private double balance, balanceyc, balancektx;
    private TextView mywallet_balance_yucun;
    private TextView mywallet_balance_ketix;

    @Override
    protected int getLayout() {
        return R.layout.activity_mywallet_balance;
    }

    @Override
    protected void initData() {
        control = new MyWalletControl(this);
    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        mywallet_balance_manager = (TextView) findViewById(R.id.mywallet_balance_manager);
        mywallet_balance_manager_recharge = (TextView) findViewById(R.id.mywallet_balance_manager_recharge);
        mywallet_balance_manager_withdraw_cash = (TextView) findViewById(R.id.mywallet_balance_manager_withdraw_cash);

        mywallet_balance_yucun = (TextView) findViewById(R.id.mywallet_balance_yucun);
        mywallet_balance_ketix = (TextView) findViewById(R.id.mywallet_balance_ketix);
        layout_title.setText("我的现金");
        balance = getIntent().getDoubleExtra("Purse_balance", 0.00);
        balanceyc = getIntent().getDoubleExtra("Cash_credit", 0.00);

        BigDecimal bigDecimal = new BigDecimal(balance + balanceyc);
        double total = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        mywallet_balance_manager.setText(String.format(getString(R.string.mywallet_balance_manager), total + ""));
        mywallet_balance_yucun.setText(String.format(getString(R.string.mywallet_balance_yucun), balanceyc + ""));

        mywallet_balance_ketix.setText(String.format(getString(R.string.mywallet_balance_ketix), balance + ""));

    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        mywallet_balance_manager_recharge.setOnClickListener(this);
        mywallet_balance_manager_withdraw_cash.setOnClickListener(this);
        mywallet_balance_yucun.setOnClickListener(this);
        mywallet_balance_ketix.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    private final static int BALANCE_REQUEST_CODE = 56;
    private final static int BALANCE_REQUEST_WITHDRAW = 0X795;

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.mywallet_balance_ketix:
                //可提现金额交易明细
                Intent balanceDetail =  new Intent(MyBalanceManagerActivity.this, BalanceDetailActivity.class);
                balanceDetail.putExtra(Constants.BALANCE_DETAIL,balance);
                startActivity(balanceDetail);
                break;
            case R.id.mywallet_balance_yucun:
                //预存金额交易明细
                Intent prestore =  new Intent(MyBalanceManagerActivity.this, PrestoreDetailActivity.class);
                prestore.putExtra(Constants.PRESTORE_DETAIL,balanceyc);
                startActivity(prestore);
                break;
            case R.id.mywallet_balance_manager_recharge:
                //充值
                startActivityForResult(new Intent(MyBalanceManagerActivity.this, WalletActivity.class), BALANCE_REQUEST_CODE);
                break;
            case R.id.mywallet_balance_manager_withdraw_cash:
                //提现
                Intent it = new Intent(this, BalanceWithdrawCashActivity.class);
                it.putExtra("balance", balance);
                startActivityForResult(it, BALANCE_REQUEST_WITHDRAW);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BALANCE_REQUEST_CODE:
                    if (data != null) {
                        doResult(data);
                    }
                    break;
                case BALANCE_REQUEST_WITHDRAW:
                    if (data != null) {
                        doResult(data);
                    }
                    break;
            }
        }
    }

    private void doResult(Intent data) {
        boolean result = data.getBooleanExtra("result", false);
        if (result) {
            //充值成功 刷新数据
            //充值成功 刷新个人资料 获取新的现金券数量
            MyWalletControl control = new MyWalletControl(this);
            if (NetWorkUtil.isNetworkAvailable(MyBalanceManagerActivity.this)) {
                control.getBalanceAndCash(MyBalanceManagerActivity.this);
            } else {
                ToastUtil.showToast(MyBalanceManagerActivity.this, "刷新现金失败，请稍后再试!");
            }
        }
    }

    @Override
    public void getBalanceAndCash(BalanceAndCash balanceAndCash) {
        if (balanceAndCash != null) {
            BalanceAndCash.ItemBean item = balanceAndCash.getItem();
            BigDecimal bigDecimal = new BigDecimal(item.getPurse_balance() + item.getCash_credit());
            double total = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            mywallet_balance_manager.setText(String.format(getString(R.string.mywallet_balance_manager), total + ""));
            balance = item.getPurse_balance();
            balanceyc = item.getCash_credit();
            mywallet_balance_yucun.setText(String.format(getString(R.string.mywallet_balance_yucun), item.getCash_credit() + ""));
            mywallet_balance_ketix.setText(String.format(getString(R.string.mywallet_balance_ketix), item.getPurse_balance() + ""));
            Intent it = new Intent();
            setResult(RESULT_OK, it);
        } else {
            ToastUtil.showToast(MyBalanceManagerActivity.this, "刷新失败，请稍后再试！");
        }
    }

    @Override
    public void balanceAndCashErrorMsg(int message) {
        ToastUtil.showToast(MyBalanceManagerActivity.this, GBAccountError.getErrorMsg(this, message));
    }

}
