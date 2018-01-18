package com.winguo.personalcenter.wallet.bankcard;

import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.guobi.account.WinguoAccountBankCard;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.personalcenter.wallet.bankcard.control.BankCardControl;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardDeleteCallback;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardDetailCallback;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CommonPopWindow;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/10/30.
 * 银行卡详情
 */

public class BankCardDetailActivity extends BaseActivity implements RequestAccountBankCardDetailCallback,RequestAccountBankCardDeleteCallback {

    private FrameLayout top_back;
    private TextView layout_title;
    private TextView top_title_right;
    private ImageView my_bank_card_detail_type_icon;
    private TextView my_bank_detail_type;
    private TextView my_bank_card_type;
    private TextView my_bank_card_detail_code;
    private LinearLayout my_bank_card_detail;
    private String p_bid;
    private BankCardControl cardControl;
    private CommonPopWindow popWindow;

    @Override
    protected int getLayout() {
        return R.layout.activity_bankcard_detail;
    }

    @Override
    protected void initData() {
        p_bid = getIntent().getStringExtra("p_bid");
        cardControl = new BankCardControl(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this,true);
            cardControl.getAccountBankCardDetail(this,p_bid);
        } else {
            ToastUtil.showToast(this,getString(R.string.timeout));
        }

    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        top_title_right = (TextView) findViewById(R.id.top_title_right);
        my_bank_card_detail_type_icon = (ImageView) findViewById(R.id.my_bank_card_detail_type_icon);
        my_bank_detail_type = (TextView) findViewById(R.id.my_bank_detail_type);
        my_bank_card_type = (TextView) findViewById(R.id.my_bank_card_type);
        my_bank_card_detail_code = (TextView) findViewById(R.id.my_bank_card_detail_code);
        my_bank_card_detail = (LinearLayout) findViewById(R.id.my_bank_card_detail);

        layout_title.setText("银行卡详情");
        top_title_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        top_title_right.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.top_title_right:
                //弹窗
                popWindow = new CommonPopWindow(this, R.layout.bank_card_detail_pop,new int[]{R.id.bank_card_detail_remove},this);
                popWindow.showAtLocation(top_back, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,0,0);
                break;
            case R.id.bank_card_detail_remove:
                //解绑银行卡
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    LoadDialog.show(this, true);
                    cardControl.delBankCard(this, p_bid);
                } else {
                    ToastUtil.showToast(this,getString(R.string.timeout));
                }
                break;
        }
    }

    @Override
    public void requestAccountBankCardDetail(WinguoAccountBankCard card) {
        LoadDialog.dismiss(this);
        int bankIcon = cardControl.getBankIcon(card.bankName);
        if (bankIcon != 0) {
            my_bank_card_detail_type_icon.setImageResource(bankIcon);
        }
        my_bank_detail_type.setText(card.bankName);
        my_bank_card_detail_code.setText(card.cardNumberFull);
        //my_bank_card_type.setText("储蓄卡");
        my_bank_card_detail.setVisibility(View.VISIBLE);

    }

    @Override
    public void requestAccountBankCardDetailErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }

    @Override
    public void requestAccountBankCardDelete(String result) {
        LoadDialog.dismiss(this);
        try {
            JSONObject root = new JSONObject(result);
            JSONObject message = root.getJSONObject("message");
            //<message><status>success</status><text>删除成功</text><code>0</code></message>
            if (message == null) {
                return ;
            }
            String text = message.getString("text");
            int code = message.getInt("code");
            ToastUtil.showToast(this,text);
            if (code==0) {
                //成功
                Intent it = new Intent();
                setResult(RESULT_OK,it);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestAccountBankCardDeleteErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }

}
