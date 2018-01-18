package com.winguo.personalcenter.safecenter.phonetel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountImpl;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.bean.BindMobileGson;
import com.winguo.utils.Constants;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/10/11.
 * 绑定 / 更换绑定手机号
 */

public class SetPhoneTelActivity extends BaseActivity implements View.OnClickListener, WinguoAcccountManagerUtils.IUserCheckPhone {

    private boolean isBindPhone;
    private EditText modify_bind_phone_second_et;
    private EditText modify_bind_phone_second_code;
    private Button modify_bind_phone_send_captcha_btn;
    private Button modify_bind_phone_second_submit;
    private String phoneNum, authcode;
    private RelativeLayout set_phone_tel_back;


    @Override
    protected int getLayout() {
        return R.layout.activity_set_phone_tel_head;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        isBindPhone = intent.getBooleanExtra(Constants.IS_BIND, false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendsmssuccess");
        intentFilter.addAction("bindMobileSuccess");
        registerReceiver(MyBroadcastReceiver, intentFilter);
    }

    @Override
    protected void initViews() {
        modify_bind_phone_second_et = (EditText) findViewById(R.id.modify_bind_phone_second_et);
        modify_bind_phone_second_code = (EditText) findViewById(R.id.modify_bind_phone_second_code);
        modify_bind_phone_send_captcha_btn = (Button) findViewById(R.id.modify_bind_phone_send_captcha_btn);
        modify_bind_phone_second_submit = (Button) findViewById(R.id.modify_bind_phone_second_submit);
        set_phone_tel_back = (RelativeLayout) findViewById(R.id.set_phone_tel_back);

    }

    @Override
    protected void setListener() {
        modify_bind_phone_send_captcha_btn.setOnClickListener(this);
        modify_bind_phone_second_submit.setOnClickListener(this);
        set_phone_tel_back.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.modify_bind_phone_send_captcha_btn:// 短信 验证码
                getSMSauthenticationCode();
                break;
            case R.id.modify_bind_phone_second_submit://提交手机号
                bindMoile();
                break;
            case R.id.set_phone_tel_back:

                finish();
                break;
            default:
        }
    }

    /**
     * 绑定手机号
     */
    private void bindMoile() {
        authcode = modify_bind_phone_second_code.getText().toString();
        phoneNum = modify_bind_phone_second_et.getText().toString();
        if (phoneNum != null && phoneNum.length() > 0 && authcode != null && authcode.length() == 6) {
            WinguoAccountImpl.bindingPhoneNumber(getApplicationContext(), phoneNum, authcode);
        } else {
            Toast.makeText(getApplicationContext(), getString(
                    R.string.hybrid4_account_phone_empty), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取短信验证码
     */
    private void getSMSauthenticationCode() {
        phoneNum = modify_bind_phone_second_et.getText().toString();

        if (phoneNum != null && phoneNum.length() > 0) {
            if (NetworkUtils.isConnectNet(this)) {
                WinguoAcccountManagerUtils.checkPhone(SetPhoneTelActivity.this, phoneNum, SetPhoneTelActivity.this);
            } else {
                Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(
                    R.string.hybrid4_account_phone_empty), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void checkPhone(int code, String text) {
        if (code == 0) {  //可以绑定
            if (NetworkUtils.isConnectNet(this)) {
                WinguoAccountImpl.sendVerificationcode(getApplicationContext(), phoneNum, "16");
            } else {
                Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            ToastUtil.showToast(this, text);
        }

    }

    @Override
    public void checkPhoneErrorMsg(int message) {
        ToastUtil.showToast(SetPhoneTelActivity.this, GBAccountError.getErrorMsg(SetPhoneTelActivity.this, message));
    }

    private BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("sendsmssuccess")) { //获取验证码sendsmssuccess
                String sendsms = intent.getStringExtra("sendsms");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(sendsms.toString());
                    JSONObject message = jsonObject.getJSONObject("message");
                    String status = message.getString("status");
                    String text = message.getString("text");

                    if (status.equals("success")) {//发送成功
                        //  removeDialog(CAPTCHA_DIALOG);
                        //  isShowCaptchaDialog = false;
                        //获取验证码后 一分钟后才能重新获取
                        new CountDownTimer(60 * 1000, 1000) {
                            @Override
                            public void onTick(long l) {
                                modify_bind_phone_send_captcha_btn.setEnabled(false);
                                modify_bind_phone_send_captcha_btn.setText((l / 1000) + "秒后重新获取");
                            }

                            @Override
                            public void onFinish() {
                                modify_bind_phone_send_captcha_btn.setEnabled(true);
                                modify_bind_phone_send_captcha_btn.setText(R.string.hybrid4_account_send_captcha);
                            }
                        }.start();
                        //短信已发送过，1分钟后没有收到短信再重新获取！
                        Toast.makeText(context, R.string.gb_account_err_msg_wg_sendvercode_no_4, Toast.LENGTH_SHORT).show();

                    } else {//发送失败
                        // removeDialog(CAPTCHA_DIALOG);
                        // isShowCaptchaDialog = false;
                        modify_bind_phone_send_captcha_btn.setEnabled(true);
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //
            } else if (action.equals("bindMobileSuccess")) {
                String json = intent.getStringExtra("bindMobilejson");

                Gson gson = new Gson();
                BindMobileGson bindMobileGson = gson.fromJson(json, BindMobileGson.class);

                BindMobileGson.MessageBean data = bindMobileGson.getMessage();

                String status = data.getStatus();
                String message = data.getText();
                int code = data.getCode();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                if (code == 0) {
                    Intent bindTel = new Intent();
                    bindTel.putExtra(Constants.BIND_PHONE,""+phoneNum);
                    SetPhoneTelActivity.this.setResult(RESULT_OK,bindTel);
                    finish(); //如果绑定成功 返回前一页  否则当前页
                }

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyBroadcastReceiver);
    }

}
