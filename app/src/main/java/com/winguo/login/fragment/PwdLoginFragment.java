package com.winguo.login.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.client.android.camera.ZxingCaptureActivity;
import com.guobi.account.GBAccountError;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountKey;
import com.winguo.R;
import com.winguo.personalcenter.safecenter.loginpwd.ModifyPwdActivity;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragment;
import com.winguo.listener.ITextWatcher;
import com.winguo.login.LoginActivity;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/9/18.
 * 通过密码 进行账号登录
 */

public class PwdLoginFragment extends BaseFragment implements WinguoAcccountManagerUtils.ITakeWinguoGeneralResult{
    private EditText account_login_user_et_bypwd;
    private ImageView winguo_login_clear_account;
    private EditText account_login_pwd_et;
    private ToggleButton account_login_showpwd_tb;
    private TextView account_forgetpwd_tv;
    private Button winguo_account_login_button;
    private LinearLayout acccount_weixin_login_ll;
    private TextView account_register_tv;
    private LinearLayout account_login_container;
    private LoginActivity loginActivity;

    @Override
    protected int getLayout() {
        return R.layout.fragment_pwd_login;
    }

    @Override
    protected void initView(View view) {
        account_login_user_et_bypwd = (EditText) view.findViewById(R.id.account_login_user_et_bypwd);
        winguo_login_clear_account = (ImageView) view.findViewById(R.id.winguo_login_clear_account);
        account_login_pwd_et = (EditText) view.findViewById(R.id.account_login_pwd_et);
        account_login_showpwd_tb = (ToggleButton) view.findViewById(R.id.account_login_showpwd_tb);
        account_forgetpwd_tv = (TextView) view.findViewById(R.id.account_forgetpwd_tv);
        winguo_account_login_button = (Button) view.findViewById(R.id.winguo_account_login_button);
        acccount_weixin_login_ll = (LinearLayout) view.findViewById(R.id.acccount_weixin_login_ll);
        account_register_tv = (TextView) view.findViewById(R.id.account_register_tv);
        account_login_container = (LinearLayout) view.findViewById(R.id.account_login_container);
        // 获取 本地登录记录
       /* String userName = WinguoAccountDataMgr.getUserName(mContext);
          String pwd = WinguoAccountDataMgr.getPizza(mContext);
        if (pwd!=null) {
            //微信登录
            if (!TextUtils.isEmpty(userName)) {
                String pizza = WinguoAccountDataMgr.getPizza(mContext);
                winguo_login_clear_account.setVisibility(View.VISIBLE);
                account_login_user_et_bypwd.setText(userName);
                account_login_pwd_et.setText(pizza);
            }
        }*/
    }

    @Override
    protected void initData() {
        //获取LoginActivity 类   获取公有方法
        loginActivity = (LoginActivity) context;
    }

    @Override
    protected void setListener() {
        winguo_account_login_button.setOnClickListener(this);
        acccount_weixin_login_ll.setOnClickListener(this);
        account_register_tv.setOnClickListener(this);
        account_forgetpwd_tv.setOnClickListener(this);
        winguo_login_clear_account.setOnClickListener(this);
        setEditTextInhibitInputSpeChat(account_login_user_et_bypwd);
        account_login_user_et_bypwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String editable = account_login_user_et_bypwd.getText().toString();
                if (!TextUtils.isEmpty(editable)) {
                    winguo_login_clear_account.setVisibility(View.VISIBLE);
                } else {
                    winguo_login_clear_account.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        account_login_pwd_et.addTextChangedListener(new ITextWatcher(ITextWatcher.TYPE_PWD, account_login_pwd_et, mContext));
        account_login_showpwd_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    account_login_pwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) account_login_pwd_et.getText(), account_login_pwd_et.length());
                } else {
                    // 隐藏密码
                    account_login_pwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) account_login_pwd_et.getText(), account_login_pwd_et.length());
                }
            }
        });
    }
    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.winguo_account_login_button:
                //登录
                submit();
                break;
            case R.id.acccount_weixin_login_ll:
                //微信登录
                Log.i("SmsLoginFragment","微信登录");
                loginActivity.wxLogin();
                break;
            case R.id.account_register_tv:
                //注册
                Intent register = new Intent(getActivity(), ZxingCaptureActivity.class);
                startActivityForResult(register,0);
                break;
            case R.id.account_forgetpwd_tv:
                //忘记密码
                Intent reset = new Intent(getActivity(), ModifyPwdActivity.class);
                startActivityForResult(reset, FORGET_PWD);
                break;
            case R.id.winguo_login_clear_account:
                //清理账号
                account_login_user_et_bypwd.setText("");
                break;

        }
    }
    private static final int FORGET_PWD = 4;
    private void submit() {

        // WinguoAccountImpl.login(getApplicationContext(), "18602029479", "youketao1", StartApp.mKey);

        final String pwd = account_login_pwd_et.getText().toString().trim();
        final String name = account_login_user_et_bypwd.getText().toString().trim();

        if (pwd == null || name == null || pwd.length() == 0 || name.length() == 0) {
            String info = getString(R.string.hybrid4_account_userinfo_empty);
            Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
        } else {

            LoadDialog.show(mContext,true);
            if (NetworkUtils.isConnectNet(mContext)) {

                    WinguoAcccountManagerUtils.getKey(mContext, new WinguoAcccountManagerUtils.IPublicKey() {
                        @Override
                        public void publicKey(WinguoAccountKey key) {
                            StartApp.mKey = key;
                            //WinguoAccountImpl.login(getApplicationContext(), name, pwd, key);
                            WinguoAcccountManagerUtils.login(mContext,name,pwd,key,true,PwdLoginFragment.this);
                        }

                        @Override
                        public void publicKeyErrorMsg(int error) {

                            LoadDialog.dismiss(mContext);
                            Toast.makeText(mContext, getString(R.string.no_net_or_service_no), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    LoadDialog.dismiss(mContext);
                    Toast.makeText(mContext, mContext.getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FORGET_PWD:
                    if (data != null) {
                        String newPwd = data.getStringExtra("newPwd");
                        account_login_pwd_et.setText(newPwd);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void takeWinguoGeneralResult(int code, boolean isSuccess) {
         LoadDialog.dismiss(mContext);
        switch (code) {
            case 0:
                //成功
                break;
            case GBAccountError.REQUST_TIMEOUT:
                //请求超时 登录失败
                ToastUtil.showToast(mContext,getString(R.string.no_net_or_service_no));
                break;
            case GBAccountError.SESSION_TIMEOUT:
                //登陆过期
                ToastUtil.showToast(mContext,getString(R.string.session_out));
                break;
            case -3:
                //登陆失败
                ToastUtil.showToast(mContext,"用户名和密码输入错误！");
                break;
            case -2:
                ToastUtil.showToast(mContext,"登录密码超过6次输入错误，2小时候再试！");
                break;
            default:
                break;
        }
    }
}
