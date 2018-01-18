package com.winguo.personalcenter.safecenter.loginpwd;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountImpl;
import com.guobi.account.WinguoAccountKey;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.ValidateUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记密码
 * 通过验证码
 * 重置登录密码 重置支付密码
 * Created by admin on 2017/4/7.
 */

public class ModifyPwdActivity extends BaseTitleActivity implements WinguoAcccountManagerUtils.IResetPayPwd ,View.OnClickListener, WinguoAcccountManagerUtils.IResetLoginPwd {

    private View lastOpen;
    private GBAccountMgr mAccountMgr;
    //	private EditText mUserEditText;
    private EditText mPhoneEditText;
    private EditText mCaptchaEdText;
    private EditText mPwdEditText;
    private EditText mPwdEditText1;

    private Button mSendCaptcha;
    private ImageView delete;
    private ToggleButton show;
    private ImageView delete1;
    private ToggleButton show1;
    private Button modify;
    private Button send;
    private TextView accountTip;
    //支付密码相关
    private boolean isPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_pwd_activity);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initDatas() {
        mAccountMgr = GBAccountMgr.getInstance();

        Intent intent = getIntent();
        String payPwd = intent.getStringExtra("payPwd");
        if (payPwd!=null) {
            isPayPwd = true;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendsmssuccess");
        intentFilter.addAction("intentresetPaw");
        intentFilter.addAction("resetPwderror");
        registerReceiver(MyBroadcastReceiver, intentFilter);
    }

    private void initViews() {
//        if (isPayPwd) {
//            ((TextView)findViewById(R.id.hybrid4_account_register_title)).setText(getString(R.string.winguo_pay_pwd_title));
//        }
//        findViewById(R.id.winguo_account_reset_back).setOnClickListener(this);

        final EditText phoneEt = (EditText) findViewById(R.id.winguo_account_reset_phone_et);
//		final EditText userEt = (EditText) findViewById (R.id.hybrid4_account_login_user_et);

        final EditText captchaEt = (EditText) findViewById(R.id.hybrid4_account_reset_captcha_et);
        final EditText pwdEt = (EditText) findViewById(R.id.winguo_account_reset_pwd_et);
        final EditText pwdEt1 = (EditText) findViewById(R.id.winguo_account_reset_pwd_et1);
        delete = (ImageView) findViewById(R.id.winguo_account_reset_pwd_et_delete_iv);
        show = (ToggleButton) findViewById(R.id.winguo_account_reset_pwd_et_showpwd_cb2);
        delete1 = (ImageView) findViewById(R.id.winguo_reset_pwd_et1_delete_iv);
        show1 = (ToggleButton) findViewById(R.id.winguo_reset_pwd_et1_showpwd_cb2);

        accountTip = (TextView) findViewById(R.id.winguo_app_acoount_reset_tip);

//		pwdEt.setTypeface(mPreferences.getCurTypeface());
//		userEt.setTypeface(mPreferences.getCurTypeface());

        modify = (Button) findViewById(R.id.winguo_account_modify_button);
        if (isPayPwd) {
            modify.setText(getString(R.string.modify_pay_pwd));
        }
        send = (Button) findViewById(R.id.hybrid4_account_send_captcha_btn);
        mSendCaptcha = send;

        mPhoneEditText = phoneEt;
        mCaptchaEdText = captchaEt;
        mPwdEditText = pwdEt;
        mPwdEditText1 = pwdEt1;

    }

    private boolean isShowLoginDialog;
    private boolean isShowCaptchaDialog;

    @Override
    public void onBackPressed() {
        if (isShowCaptchaDialog) {
            removeDialog(CAPTCHA_DIALOG);
            isShowCaptchaDialog = false;
        } else if (isShowLoginDialog) {
            removeDialog(MODIFY_DIALOG);
            isShowLoginDialog = false;
        } else {
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        switch (id) {
            case MODIFY_DIALOG: {
                String message = getString(R.string.hybrid4_account_info_pwd_modyfing);
                dialog.setMessage(message);

                return dialog;
            }
            case CAPTCHA_DIALOG: {
                String message = getString(R.string.hybrid4_account_info_captcha_sending);
                dialog.setMessage(message);
                return dialog;
            }
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void initListener() {

        modify.setOnClickListener(this);
        send.setOnClickListener(this);

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                modify.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText user = (EditText) v;
                if (!hasFocus) {
                    boolean telephone2 = ValidateUtil.isTelephone2(user.getText().toString());
                    if (!TextUtils.isEmpty(user.getText().toString())) {
                        if (!telephone2) {
                            accountTip.setText(getString(R.string.winguo_app_acoount_phone_tip));
                            modify.setTextColor(Color.argb(0xff, 0xff, 0xa7, 0xa7));
                            modify.setEnabled(false);
                        } else {
                            accountTip.setText("");
                            modify.setTextColor(Color.argb(0xff, 0xff, 0xff, 0xff));
                            modify.setEnabled(true);
                        }
                    } else {
                        accountTip.setText(getString(R.string.winguo_app_acoount_phone_tip_null));
                        modify.setTextColor(Color.argb(0xff, 0xff, 0xa7, 0xa7));
                        modify.setEnabled(false);
                    }

                }
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String account = s.toString();
                if (!TextUtils.isEmpty(account)) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPwdEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String account = s.toString();
                if (!TextUtils.isEmpty(account)) {
                    delete1.setVisibility(View.VISIBLE);
                } else {
                    delete1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwdEditText.setText("");
            }
        });
        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwdEditText1.setText("");
            }
        });

        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    mPwdEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) mPwdEditText.getText(), mPwdEditText.length());
                } else {
                    // 隐藏密码
                    mPwdEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) mPwdEditText.getText(), mPwdEditText.length());
                }
            }
        });
        show1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    mPwdEditText1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) mPwdEditText1.getText(), mPwdEditText1.length());
                } else {
                    // 隐藏密码
                    mPwdEditText1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) mPwdEditText1.getText(), mPwdEditText1.length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.winguo_account_reset_back:
//                finish();
//                break;

            case R.id.hybrid4_account_send_captcha_btn:

                final String phoneNum = mPhoneEditText.getText().toString();

                if (phoneNum != null && phoneNum.length() > 0) {
                    showDialog(CAPTCHA_DIALOG);

                    if (NetworkUtils.isConnectNet(this)) {
                        isShowCaptchaDialog = true;
                        //通过手机号码获取短信验证码
                        if (isPayPwd) {
                            WinguoAccountImpl.sendVerificationcode(getApplicationContext(), phoneNum, "3");
                        } else {
                            WinguoAccountImpl.sendVerificationcode(getApplicationContext(), phoneNum, "2");
                        }

                    } else {
                        removeDialog(CAPTCHA_DIALOG);
                        Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), getString(
                            R.string.hybrid4_account_phone_empty), Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.winguo_account_modify_button:

                final String phone = mPhoneEditText.getText().toString();
                final String captcha = mCaptchaEdText.getText().toString();
                final String pwd = mPwdEditText.getText().toString();
                String pwd1 = mPwdEditText1.getText().toString();

                if (phone == null || phone.length() == 0) {
                    String info = getString(R.string.hybrid4_account_phone_empty);
                    ToastUtil.show(this, info);

                    break;
                } else if (captcha == null || captcha.length() == 0) {
                    String info = getString(R.string.hybrid4_account_captcha_empty);
                    ToastUtil.show(this, info);

                    break;
                } else if (pwd == null || pwd.length() == 0) {
                    String info = getString(R.string.hybrid4_account_pwd_empty);
                    ToastUtil.show(this, info);


                    break;
                } else if (pwd1.length() == 0 || pwd1 == null) {
                    String info = getString(R.string.hybrid4_account_check_pwd_empty);
                    ToastUtil.show(this, info);

                    break;
                } else if (!ValidateUtil.isPassword(pwd)){
                    String info = getString(R.string.hybrid4_account_check_pwd_type);
                    ToastUtil.show(this, info);
                    break;

                }else if (pwd1 != null && pwd != null) {

                    if (!pwd.equals(pwd1)) {
                        String info = getString(R.string.hybrid4_account_check_pwd_unequal);
                        ToastUtil.show(this, info);
                        break;
                    } else {
                        accountTip.setText("");
                        showDialog(MODIFY_DIALOG);
                        if (NetworkUtils.isConnectNet(this)) {

                            isShowLoginDialog = true;
                            if (isPayPwd) {
                                //重置支付密码的函数
                                WinguoAcccountManagerUtils.resetPayPW(ModifyPwdActivity.this,phone,captcha,pwd,"3",ModifyPwdActivity.this);
                            } else {
                                //重置登录密码的函数
                                //WinguoAccountImpl.resetPwd(getApplicationContext(), phone, phone, captcha, pwd, "2");
                                WinguoAcccountManagerUtils.resetLoginPwd(getApplicationContext(),phone,phone,captcha,pwd,"2",ModifyPwdActivity.this);
                            }

                        } else {
                            removeDialog(MODIFY_DIALOG);
                            Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                        }

                    }

                }


                break;
            default:
                break;
        }
    }



    /**
     * 重置支付密码 回调结果处理
     * @param result
     */
    @Override
    public void resetPayPwd(String result) {
        removeDialog(MODIFY_DIALOG);
        try {
            JSONObject root = new JSONObject(result);
            JSONObject message = root.getJSONObject("message");
            String text = message.getString("text");
            String status = message.getString("status");
            // {"message":{"status":"error","text":"抱歉！您的验证码错误或已过期！","code":-3}}
            if (status.equals(getString(R.string.result_status_error))) {
                //失败时处理
                ToastUtil.showToast(this, text);

            } else {
                //成功时处理
                ToastUtil.showToast(this, text);
                finish();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置支付密码 访问异常
     * @param message
     */
    @Override
    public void resetPayPwdErrorMsg(int message) {
        removeDialog(MODIFY_DIALOG);
        if (message == GBAccountError.REQUST_TIMEOUT) {
            ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
            return;
        }
        ToastUtil.show(ModifyPwdActivity.this, GBAccountError.getErrorMsg(this,message));
    }

    @Override
    public void resetLoginPwd(String result) {

    }

    @Override
    public void resetLoginPwdErrorMsg(int message) {
        removeDialog(MODIFY_DIALOG);
        if (message == GBAccountError.REQUST_TIMEOUT) {
            ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
            return;
        }
        ToastUtil.show(ModifyPwdActivity.this, GBAccountError.getErrorMsg(this,message));
    }

    private static final int MODIFY_DIALOG = 7;
    private static final int CAPTCHA_DIALOG = 8;

    private static final int LOGIN_OVER = 3;
    private static final int SEND_CAPTCHA_OVER = 4;


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
                        removeDialog(CAPTCHA_DIALOG);
                        isShowCaptchaDialog = false;
                        //获取验证码后 一分钟后才能重新获取
                        new CountDownTimer(60 * 1000, 1000) {
                            @Override
                            public void onTick(long l) {
                                mSendCaptcha.setEnabled(false);
                                mSendCaptcha.setText((l / 1000) + "秒后重新获取");
                            }

                            @Override
                            public void onFinish() {
                                mSendCaptcha.setEnabled(true);
                                mSendCaptcha.setText(R.string.hybrid4_account_send_captcha);
                            }
                        }.start();
                        //短信已发送过，1分钟后没有收到短信再重新获取！
                        Toast.makeText(context, R.string.gb_account_err_msg_wg_sendvercode_no_4, Toast.LENGTH_SHORT).show();

                    } else {//发送失败
                        removeDialog(CAPTCHA_DIALOG);
                        isShowCaptchaDialog = false;
                        mSendCaptcha.setEnabled(true);
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (action.equals("intentresetPaw")) {//重置密码

                String restpawjson = intent.getStringExtra("intentresetPawjson");

                try {//jsons数据的解析
                    JSONObject jsonObject = new JSONObject(restpawjson);
                    JSONObject message = jsonObject.getJSONObject("message");
                    String status = message.getString("status");
                    String text = message.getString("text");

                    if (status.equals("success")) {//修改密码成功
                        removeDialog(MODIFY_DIALOG);
                        final String pwd = mPwdEditText.getText().toString();
                        WinguoAccountDataMgr.setPizza(ModifyPwdActivity.this,pwd);
                        isShowLoginDialog = false;
                        Toast.makeText(context, "修改密码成功，请重新登陆", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        data.putExtra("newPwd",pwd);
                        ModifyPwdActivity.this.setResult(RESULT_OK,data);
                        //修改密码成功后的操作在以下方法中执行
                        finish();

                    } else {//修改密码失败
                        removeDialog(MODIFY_DIALOG);
                        isShowLoginDialog = false;
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e("json", e.getMessage());
                    e.printStackTrace();
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
