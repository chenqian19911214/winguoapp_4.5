package com.winguo.login.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.camera.ZxingCaptureActivity;
import com.guobi.account.GBAccountError;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.base.BaseFragment;
import com.winguo.login.LoginActivity;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/9/18.
 */

public class SmsLoginFragment extends BaseFragment implements WinguoAcccountManagerUtils.IRequstLoginCodeCallback, WinguoAcccountManagerUtils.IRequstLoginByCodeCallback, WinguoAcccountManagerUtils.ITakeWinguoGeneralResult {

    private EditText account_login_user_et;
    private EditText account_login_send_code;
    private TextView account_login_gainedcode_tv;
    private Button account_login_bt;
    private LinearLayout acccount_weixin_login_ll;
    private TextView account_register_tv;
    private LinearLayout account_login_container;
    private LoginActivity loginActivity;

    @Override
    protected int getLayout() {
        return R.layout.fragment_sms_login;
    }
    @Override
    protected void initData() {
        loginActivity = (LoginActivity) context;
    }

    @Override
    protected void initView(View view) {
          account_login_user_et = (EditText) view.findViewById(R.id.account_login_user_et);
          account_login_send_code= (EditText) view.findViewById(R.id.account_login_send_code);
          account_login_gainedcode_tv= (TextView) view.findViewById(R.id.account_login_gainedcode_tv);
          account_login_bt= (Button) view.findViewById(R.id.account_login_bt);
          acccount_weixin_login_ll= (LinearLayout) view.findViewById(R.id.acccount_weixin_login_ll);
          account_register_tv= (TextView) view.findViewById(R.id.account_register_tv);
          account_login_container= (LinearLayout) view.findViewById(R.id.account_login_container);
    }

    @Override
    protected void setListener() {
        account_login_bt.setOnClickListener(this);
        acccount_weixin_login_ll.setOnClickListener(this);
        account_register_tv.setOnClickListener(this);
        account_login_gainedcode_tv.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.account_login_bt:
                //登录
                loginByCode();
                break;
            case R.id.acccount_weixin_login_ll:
                //微信登录
                loginActivity.wxLogin();
                break;
            case R.id.account_register_tv:
                //注册(先是扫码注册 - 快速注册)
                Intent register = new Intent(getActivity(), ZxingCaptureActivity.class);
                startActivityForResult(register,0);
                break;
            case R.id.account_login_gainedcode_tv:
                //发送验证码
                requestCode();
                break;
        }
    }
   private WinguoAccountKey mkey;
    /**
     * 请求通过验证码登录
     */
    private void loginByCode() {
        if (NetWorkUtil.isNetworkAvailable(context)) {
            final String phone = account_login_user_et.getText().toString().trim();
            final String code = account_login_send_code.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToast(context, "手机号不能为空！");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtil.showToast(context, "验证码不能为空！");
                return;
            }
            LoadDialog.show(context, true);
            WinguoAcccountManagerUtils.getKey(context, new WinguoAcccountManagerUtils.IPublicKey() {
                @Override
                public void publicKey(WinguoAccountKey key) {
                    if (key != null) {
                        mkey = key;
                        WinguoAcccountManagerUtils.requestLoginByCode(context, phone, code, key, SmsLoginFragment.this);
                    } else {
                        LoadDialog.dismiss(context);
                        ToastUtil.showToast(context,"登录失败！请稍后再试..");
                    }

                }

                @Override
                public void publicKeyErrorMsg(int error) {
                    LoadDialog.dismiss(context);
                    ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,error));
                }
            });
        } else {
            ToastUtil.showToast(context,context.getString(R.string.no_net_dialog_title));
        }
    }

    /**
     * 获取验证码
     */
    private void requestCode() {
        if (NetWorkUtil.isNetworkAvailable(context)) {
            String phone = account_login_user_et.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToast(context, "手机号不能为空！");
                return;
            }
            LoadDialog.show(context, true);
            WinguoAcccountManagerUtils.requestCodeToLogin(context, phone, SmsLoginFragment.this);
            account_login_gainedcode_tv.setClickable(false);
        } else {
            ToastUtil.showToast(context,context.getString(R.string.no_net_dialog_title));
        }
    }


    @Override
    public void requstLoginCodeCallback(String result) {
       /* {
             "status": "success",
             "text": "验证码短信发送成功",
             "code": 0
        }*/
        try {
            JSONObject root = new JSONObject(result);
            String status = root.getString("status");
            String text = root.getString("text");
            int code = root.getInt("code");
            if (code == 0) {
                //获取验证码后 一分钟后才能重新获取
                LoadDialog.dismiss(context);
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        account_login_gainedcode_tv.setText((l / 1000) + "秒后重新获取");
                    }

                    @Override
                    public void onFinish() {
                        account_login_gainedcode_tv.setText(R.string.hybrid4_account_send_captcha2);
                        account_login_gainedcode_tv.setClickable(true);
                    }
                }.start();
                //短信已发送过，1分钟后没有收到短信再重新获取！
                ToastUtil.showToast(context, context.getString(R.string.gb_account_err_msg_wg_sendvercode_no_4));
            } else {//发送失败
                LoadDialog.dismiss(context);
                ToastUtil.showToast(context, text);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requstLoginCodeCallbackError(int errorcode) {
        LoadDialog.dismiss(context);
        ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,errorcode));
        switch (errorcode) {
            case GBAccountError.REQUST_TIMEOUT:

                break;
            case GBAccountError.NETWORK_FAILED:
                //接口返回数据为null
                break;
            case GBAccountError.INVALID_INPUT:

                break;
        }
    }


    @Override
    public void requstLoginByCodeCallback(String result) {
        /*{
                "status": "success",
                "text": "登录成功",
                "code": 0,
                "mobile": "13100138000"
                "account": "13100138000"
          }*/
        GBLogUtils.DEBUG_DISPLAY("requstLoginByCodeCallback",": "+result);
        try {
            JSONObject root = new JSONObject(result);
            String status = root.getString("status");
            String text = root.getString("text");
            int code = root.getInt("code");
            String account = root.getString("mobile");
            if (code == 0) {
                //登陆成功 保存
                String hashCommon = "id=" + account + "&token=" + mkey.getToken() + "&uuid=" + mkey.getUUID();
                hashCommon = WinguoEncryption.commonEncryption(hashCommon, mkey);
                WinguoAccountDataMgr.setUserName(context, account);
                WinguoAccountDataMgr.setKEY(context, mkey.getKey());
                WinguoAccountDataMgr.setTOKEN(context, mkey.getToken());
                WinguoAccountDataMgr.setUUID(context, mkey.getUUID());
                WinguoAccountDataMgr.setHashCommon(context, hashCommon);
                //获取个人信息
                WinguoAcccountManagerUtils.loginSuccess(context,SmsLoginFragment.this);
            } else {//登录失败
                LoadDialog.dismiss(context);
                ToastUtil.showToast(context, text);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requstLoginByCodeCallbackError(int errorcode) {
        LoadDialog.dismiss(context);
        ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,errorcode));
        switch (errorcode) {
            case GBAccountError.REQUST_TIMEOUT:
                //请求超时
                break;
            case GBAccountError.NETWORK_FAILED:
                //接口返回数据为null
                break;
            case GBAccountError.INVALID_INPUT:
                break;
        }
    }

    //登陆成功 获取用户信息成功
    @Override
    public void takeWinguoGeneralResult(int code, boolean isSuccess) {
        LoadDialog.dismiss(context);
        if (code!=0)
            ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,code));
    }

}
