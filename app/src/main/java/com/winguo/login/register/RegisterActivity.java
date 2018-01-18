package com.winguo.login.register;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountError;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountKey;
import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseActivity;
import com.winguo.login.register.control.RegisterHandle;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.ValidateUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/9/18.
 * 通过验证码注册，设置密码 完成注册并登录
 */
public class RegisterActivity extends BaseActivity implements WinguoAcccountManagerUtils.ISendVerificationCode {

    private RelativeLayout account_register_back;
    private EditText account_register_phone_et;
    private EditText account_register_send_code;
    private TextView account_register_gainedcode_tv;
    private EditText account_register_setpwd;
    private EditText account_register_set_inviter;
    private Button account_register_bt;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("register_success2");
        intentFilter.addAction("register_fail2");
        intentFilter.addAction("registerNewerror2");
        registerReceiver(MyBroadcastReceiver, intentFilter);
    }

    @Override
    protected void initViews() {
          account_register_back = (RelativeLayout) findViewById(R.id.account_register_back);
          account_register_phone_et = (EditText) findViewById(R.id.account_register_phone_et);
          account_register_send_code = (EditText) findViewById(R.id.account_register_send_code);
          account_register_gainedcode_tv = (TextView) findViewById(R.id.account_register_gainedcode_tv);
          account_register_setpwd = (EditText) findViewById(R.id.account_register_setpwd);
          account_register_set_inviter = (EditText) findViewById(R.id.account_register_set_inviter);
          account_register_bt = (Button) findViewById(R.id.account_register_bt);

    }

    @Override
    protected void setListener() {
        account_register_back.setOnClickListener(this);
        account_register_gainedcode_tv.setOnClickListener(this);
        account_register_bt.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.account_register_back:
                finish();
                break;  //注册
            case R.id.account_register_bt:
                final String  pwd = account_register_setpwd.getText().toString();
                final String name = account_register_phone_et.getText().toString();
                final String idCode = account_register_send_code.getText().toString();
                final String recommendPhone = account_register_set_inviter.getText().toString();
                if (pwd == null || name == null || pwd.length() == 0 || name.length() == 0) {
                    String info = getString(R.string.hybrid4_account_userinfo_empty);
                    Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!ValidateUtil.isPassword(pwd)){
                    String info = getString(R.string.hybrid4_account_check_pwd_type);
                    ToastUtil.show(this, info);
                    return;
                }
                if (idCode.length() == 0) {
                    Toast.makeText(this, getString(R.string.hybrid4_account_idcode_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (recommendPhone.length() > 0) {
                    if (!checkPhoneNumber(recommendPhone)) {
                        Toast.makeText(this, getString(R.string.register_recommend_phone_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (checkPhoneNumber(name)) {
                    if (NetworkUtils.isConnectNet(this)) {
                        showDialog(REGISTER_DIALOG);
                        WinguoAcccountManagerUtils.getKey(getApplicationContext(), new WinguoAcccountManagerUtils.IPublicKey() {
                            @Override
                            public void publicKey(WinguoAccountKey key) {
                                StartApp.mKey = key;
                                RegisterHandle.register(RegisterActivity.this, name, pwd, idCode, recommendPhone,key);
                            }

                            @Override
                            public void publicKeyErrorMsg(int error) {
                                ToastUtil.showToast(RegisterActivity.this,getString(R.string.no_net_or_service_no));
                            }
                        });

                    } else {
                        Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.register_no_phone_number), Toast.LENGTH_SHORT).show();
                }
                break;
            //发送验证码
            case R.id.account_register_gainedcode_tv:

                final String phoneNum = account_register_phone_et.getText().toString();
                if (phoneNum.length() == 0) {
                    Toast.makeText(getApplicationContext(), getString(
                            R.string.hybrid4_account_phone_empty), Toast.LENGTH_SHORT).show();
                    break;
                }
                if (checkPhoneNumber(phoneNum)) {

                    if (NetworkUtils.isConnectNet(this)) {
                        showDialog(CAPTCHA_DIALOG);
                        //获取注册验证码
                        //WinguoAccountImpl.sendVerificationcode(getApplicationContext(), phoneNum, "1");
                        WinguoAcccountManagerUtils.sendVerificationCode(getApplicationContext(), phoneNum, "1",RegisterActivity.this);

                    } else {
                        removeDialog(CAPTCHA_DIALOG);
                        Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), getString(
                            R.string.register_no_phone_number), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 检查手机号码
     *
     * @param phoneNumber
     * @return
     */
    private boolean checkPhoneNumber(String phoneNumber) {
        String phone = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";
        Pattern regexPhone = Pattern.compile(phone);
        Matcher matcherPhone = regexPhone.matcher(phoneNumber);
        return matcherPhone.matches();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        switch (id) {
            case REGISTER_DIALOG: {
                String message = getString(R.string.hybrid4_account_info_register);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyBroadcastReceiver);
    }
    private static final int REGISTER_DIALOG = 7;
    private static final int CAPTCHA_DIALOG = 8;


    private BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //注册连接超时
            if (action.equals("registerNewerror2")) {
                removeDialog(REGISTER_DIALOG);
                String result = intent.getStringExtra("registerNewerror2");
                ToastUtil.show(RegisterActivity.this, result);
            }

            //注册成功
            if (action.equals("register_success2")) {
                removeDialog(REGISTER_DIALOG);
                CommonUtil.printE("注册成功", "register_success2*****");
                Toast.makeText(context, R.string.hybrid4_account_info_register_success, Toast.LENGTH_SHORT).show();
                //跳转到个人中心
                Intent login = new Intent(context, MainActivity.class);
                startActivity(login);
                finish();
            }
            //注册失败
            if (action.equals("register_fail2")) {
                removeDialog(REGISTER_DIALOG);
                String result = intent.getStringExtra("register_fail2");
                ToastUtil.show(RegisterActivity.this, result);
            }
        }
    };

    /**
     * 发送验证码 结果处理
     * @param result
     */
    @Override
    public void requestSendVerificationCode(String result) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            JSONObject message = jsonObject.getJSONObject("message");
            String status = message.getString("status");
            String text = message.getString("text");

            if (status.equals("success")) {//发送成功
                //获取验证码后 一分钟后才能重新获取
                removeDialog(CAPTCHA_DIALOG);
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        account_register_gainedcode_tv.setText((l / 1000) + "秒后重新获取");
                    }

                    @Override
                    public void onFinish() {
                        account_register_gainedcode_tv.setText(R.string.hybrid4_account_send_captcha2);
                    }
                }.start();
                              //短信已发送过，1分钟后没有收到短信再重新获取！
                Toast.makeText(this, R.string.gb_account_err_msg_wg_sendvercode_no_4, Toast.LENGTH_SHORT).show();

            } else {//发送失败
                removeDialog(CAPTCHA_DIALOG);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收验证码失败
     * @param errorcode
     */
    @Override
    public void requestSendVerificationCodeError(int errorcode) {
        removeDialog(CAPTCHA_DIALOG);
        if (errorcode == GBAccountError.REQUST_TIMEOUT) {
            ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
            return;
        }
        ToastUtil.show(RegisterActivity.this, GBAccountError.getErrorMsg(this,errorcode));
    }

}
