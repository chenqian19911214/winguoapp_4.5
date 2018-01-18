package com.winguo.personalcenter.wallet.balance;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.guobi.account.WinguoAccountBankCard;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.mine.order.list.PayPwdPopuWindow;
import com.winguo.personalcenter.safecenter.loginpwd.ModifyPwdActivity;
import com.winguo.personalcenter.safecenter.paypwd.ModifyPayWayActivity;
import com.winguo.personalcenter.wallet.bankcard.BankCardActivity;
import com.winguo.personalcenter.wallet.bankcard.control.BankCardControl;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardListCallback;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceWithdrawCashCallback;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/31.
 * 余额提现
 */

public class BalanceWithdrawCashActivity extends BaseActivity implements RequestAccountBankCardListCallback,RequestBalanceWithdrawCashCallback, AdapterView.OnItemSelectedListener {

    private FrameLayout top_back;
    private TextView layout_title;
    private TextView top_title_right;
    private Spinner balance_withdraw_cash_spinner;
    private EditText balance_withdraw_cash_et;
    private TextView balance_cash_now;
    private ImageView balance_withdraw_cash_add_card;
    private TextView balance_withdraw_cash_submit;
    private BankCardControl control;
    private ArrayAdapter adapter;
    private List<String> data = new ArrayList<>();
    private LinearLayout withdraw_cash_content;

    @Override
    protected int getLayout() {
        return R.layout.activity_withdraw_cash;
    }

    @Override
    protected void initData() {
        control = new BankCardControl(BalanceWithdrawCashActivity.this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this, true);
            control.getAccountBankCardList(this);
        } else {
            ToastUtil.showToast(this,getString(R.string.timeout));
        }
    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        top_title_right = (TextView) findViewById(R.id.top_title_right);
        withdraw_cash_content = (LinearLayout) findViewById(R.id.withdraw_cash_content);
        balance_withdraw_cash_spinner = (Spinner) findViewById(R.id.balance_withdraw_cash_spinner);
        balance_withdraw_cash_add_card = (ImageView) findViewById(R.id.balance_withdraw_cash_add_card);
        balance_withdraw_cash_et = (EditText) findViewById(R.id.balance_withdraw_cash_et);
        balance_cash_now = (TextView) findViewById(R.id.balance_cash_now);
        balance_withdraw_cash_submit = (TextView) findViewById(R.id.balance_withdraw_cash_submit);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        balance_withdraw_cash_spinner.setAdapter(adapter);
        layout_title.setText("钱包提现");
        double balance = getIntent().getDoubleExtra("balance", 0.00);
        balance_cash_now.setText(String.format(getString(R.string.balance_cash_now), balance + ""));
        //balance_cash_now.setText(String.format(getString(R.string.balance_cash_now),  "0.00"));
    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        balance_withdraw_cash_submit.setOnClickListener(this);
        balance_withdraw_cash_add_card.setOnClickListener(this);
        balance_withdraw_cash_spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CRAD:
                    if (data != null) {
                        if (NetWorkUtil.isNetworkAvailable(this)) {
                            control.getAccountBankCardList(BalanceWithdrawCashActivity.this);
                        } else {
                            ToastUtil.showToast(this,getString(R.string.timeout));
                        }
                    }
                    break;
            }
        }
    }

    private static final int ADD_CRAD = 0x789;
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.balance_withdraw_cash_add_card:
                Intent addNewCard = new Intent(BalanceWithdrawCashActivity.this, BankCardActivity.class);
                startActivityForResult(addNewCard,ADD_CRAD);
                break;
            case R.id.balance_withdraw_cash_submit:
                //提交
                final String cash = balance_withdraw_cash_et.getText().toString().trim();
                if (nowCard == null) {
                    ToastUtil.showToast(this, "您还未绑定银行卡！");
                    Intent addCard = new Intent(BalanceWithdrawCashActivity.this, BankCardActivity.class);
                    startActivityForResult(addCard,ADD_CRAD);
                    return;
                }

                if (TextUtils.isEmpty(cash)) {
                    ToastUtil.showToast(this,"提现金额不能为空！");
                    return;
                }
                //填写支付密码
                PayPwdPopuWindow payPwdPopuWindow = new PayPwdPopuWindow(BalanceWithdrawCashActivity.this, new PayPwdPopuWindow.IPopuWindowConfirmListener() {
                    @Override
                    public void confirm(String pwd) {
                        LoadDialog.show(BalanceWithdrawCashActivity.this, true);
                        control.getWinguoBalanceWithdraw(BalanceWithdrawCashActivity.this,cash,nowCard.p_bid,pwd);
                    }

                    @Override
                    public void forgetPwd() {
                        Intent it = new Intent(BalanceWithdrawCashActivity.this, ModifyPayWayActivity.class);
                        startActivity(it);
                        //ToastUtil.show(mActivity, "跳转到忘记支付密码!");
                    }
                });
                //payPwdPopuWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                payPwdPopuWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                payPwdPopuWindow.showAtLocation(balance_withdraw_cash_submit, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    private List<WinguoAccountBankCard> tempCard= new ArrayList<>();
    private WinguoAccountBankCard nowCard = null;
    @Override
    public void requestAccountBankCardList(List<WinguoAccountBankCard> cards) {
        LoadDialog.dismiss(this);
        withdraw_cash_content.setVisibility(View.VISIBLE);
        if (cards != null) {
            if (cards.isEmpty()) {
                ToastUtil.showToast(this, "您还未绑定银行卡！");
            } else {
                tempCard.addAll(cards);
                nowCard = tempCard.get(0);
                String temp = null;
                for (WinguoAccountBankCard card: cards) {
                    temp = card.bankName + "("+card.cardNumberTail+")";
                    data.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void requestAccountBankCardListErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }

    /**
     * 提现结果处理
     * @param result
     */
    @Override
    public void requestBalanceWithdrawCashResult(String result) {
        LoadDialog.dismiss(this);
        //{"message":{"status":"error","text":"支付密码验证失败。密码输入错误，请重试.","code":-1}}
        try {
            JSONObject root = new JSONObject(result);
            JSONObject message = root.getJSONObject("message");
            String text = message.getString("text");
            int code = message.getInt("code");
            ToastUtil.showToast(this,text);
            if (code == 0) {
                //提现成功
                Intent it = new Intent();
                it.putExtra("result",true);
                setResult(RESULT_OK,it);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestBalanceWithdrawCashErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this,GBAccountError.getErrorMsg(this,errorcode));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //获取当前提现银行卡 p_bid
        nowCard = tempCard.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
