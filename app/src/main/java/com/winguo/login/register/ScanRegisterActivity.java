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
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扫码注册  完成自动登录 密码默认
 */
public class ScanRegisterActivity extends BaseActivity implements WinguoAcccountManagerUtils.ISendVerificationCode {

    private EditText mUser_et;
    private EditText mIdCode_et;

    private Button mRegister_btn;
    private Button mRequestCode_btn;
    private String mobileShopAddr;


    /**
     * 加载布局
     */
    @Override
    protected int getLayout() {
        return R.layout.winguo_account_register3;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        Intent intent = getIntent();
        intent.getAction();
        if (intent.getAction().equals("zxingRegister")) {
            mobileShopAddr = intent.getStringExtra("mobileShopAddr");
            String telMobile = intent.getStringExtra("telMobile");
        }
        findViewById(R.id.hybrid4_account_register_back3).setOnClickListener(this);
        mUser_et = (EditText) findViewById(R.id.hybrid4_account_login_user_et3);
        mIdCode_et = (EditText) findViewById(R.id.winguo_account_register_idCode_et3);
        mRequestCode_btn = (Button) findViewById(R.id.send_idCode_request_button3);
        mRegister_btn = (Button) findViewById(R.id.hybrid4_account_register_btn3);
    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction("sendsmssuccess");
        intentFilter.addAction("register_success3");
        intentFilter.addAction("RegisterNewerror3");
        intentFilter.addAction("register_fail3");
        registerReceiver(MyBroadcastReceiver, intentFilter);
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        mRegister_btn.setOnClickListener(this);
        mRequestCode_btn.setOnClickListener(this);
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
            //返回
            case R.id.hybrid4_account_register_back3:
                finish();
                break;
            //注册
            case R.id.hybrid4_account_register_btn3:
                final String name = mUser_et.getText().toString();
                final String idCode = mIdCode_et.getText().toString();
                if (name == null || name.length() == 0) {
                    String info = getString(R.string.hybrid4_account_phone_empty);
                    Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                    break;
                }

                if (idCode.length() == 0) {
                    Toast.makeText(this, getString(R.string.hybrid4_account_idcode_empty), Toast.LENGTH_SHORT).show();
                    break;
                }

                if (checkPhoneNumber(name)) {
                    if (NetworkUtils.isConnectNet(this)) {
                        showDialog(REGISTER_DIALOG);
                        WinguoAcccountManagerUtils.getKey(getApplicationContext(), new WinguoAcccountManagerUtils.IPublicKey() {
                            @Override
                            public void publicKey(WinguoAccountKey key) {
                                StartApp.mKey = key;
                                RegisterHandle.scanRegister(ScanRegisterActivity.this, name, idCode, mobileShopAddr,key);
                            }

                            @Override
                            public void publicKeyErrorMsg(int error) {
                                removeDialog(REGISTER_DIALOG);
                                Toast.makeText(ScanRegisterActivity.this, getString(R.string.no_net_or_service_no), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        removeDialog(REGISTER_DIALOG);
                        Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    removeDialog(REGISTER_DIALOG);
                    Toast.makeText(this, getString(R.string.register_no_phone_number), Toast.LENGTH_SHORT).show();
                }
                break;
            //发送验证码
            case R.id.send_idCode_request_button3:
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                final String phoneNum = mUser_et.getText().toString();
                if (phoneNum.length() == 0) {
                    Toast.makeText(getApplicationContext(), getString(
                            R.string.hybrid4_account_phone_empty), Toast.LENGTH_SHORT).show();
                    break;
                }
                if (checkPhoneNumber(phoneNum)) {

                    String message = getString(R.string.hybrid4_account_info_captcha_sending);
                    dialog.setMessage(message);
                    if (NetworkUtils.isConnectNet(this)) {
                        showDialog(CAPTCHA_DIALOG);
                       // WinguoAccountImpl.sendVerificationcode(getApplicationContext(), phoneNum, "1");
                        WinguoAcccountManagerUtils.sendVerificationCode(getApplicationContext(), phoneNum, "1",ScanRegisterActivity.this);

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

    @Override
    public void onBackPressed() {
        removeDialog(REGISTER_DIALOG);
        finish();
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


    private static final int REGISTER_DIALOG = 7;
    private static final int CAPTCHA_DIALOG = 8;


    private BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //注册连接超时
            if (action.equals("registerNewerror3")) {
                removeDialog(REGISTER_DIALOG);
                String result = intent.getStringExtra("registerNewerror3");
                ToastUtil.show(ScanRegisterActivity.this, result);
            }

            //注册成功
            if (action.equals("register_success3")) {
                //跳转到个人中心
                removeDialog(REGISTER_DIALOG);
                CommonUtil.printE("注册成功", "register_success2*****");
                Toast.makeText(context, R.string.hybrid4_account_info_register_success, Toast.LENGTH_SHORT).show();
                Intent login = new Intent(context, MainActivity.class);
                startActivity(login);
                finish();
            }
            //注册失败
            if (action.equals("register_fail3")) {
                removeDialog(REGISTER_DIALOG);
                String result = intent.getStringExtra("register_fail3");
                ToastUtil.show(ScanRegisterActivity.this, result);
            }
        }
    };


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
                        mRequestCode_btn.setText((l / 1000) + "秒后重新获取");
                    }

                    @Override
                    public void onFinish() {
                        mRequestCode_btn.setText(R.string.hybrid4_account_send_captcha);
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

    @Override
    public void requestSendVerificationCodeError(int errorcode) {
        removeDialog(CAPTCHA_DIALOG);
        if (errorcode == GBAccountError.REQUST_TIMEOUT) {
            ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
            return;
        }
        ToastUtil.show(ScanRegisterActivity.this, GBAccountError.getErrorMsg(this,errorcode));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyBroadcastReceiver);
    }


}