package com.winguo.personalcenter.safecenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.login.LoginActivity;
import com.winguo.personalcenter.safecenter.loginpwd.ModifyPwdActivity;
import com.winguo.personalcenter.safecenter.loginpwd.SetLoginPwdActivity;
import com.winguo.personalcenter.safecenter.paypwd.ModifyPayWayActivity;
import com.winguo.personalcenter.safecenter.phonetel.SetPhoneTelActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;

/**
 * Created by admin on 2017/5/5.
 * 账户安全中心
 */

public class AccountSafeActivity extends BaseTitleActivity implements View.OnClickListener {

    private TextView safe_center_identity_authentication_tv, safe_center_phone_tv, safe_center_account_pwd_tv, safe_center_weixin_bind_tv, safe_center_pay_pwd_tv, safe_center_email_bind_tv;
    private FrameLayout safe_center_identity_authentication, safe_center_phone, safe_center_account_pwd, safe_center_pay_pwd, safe_center_account_bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        setBackBtn();
        initViews();
        initListener();
    }

    private void initViews() {
        winguoGeneral = GBAccountMgr.getInstance().getAccountInfo().winguoGeneral;

        safe_center_account_pwd = (FrameLayout) findViewById(R.id.safe_center_account_pwd);
        safe_center_identity_authentication = (FrameLayout) findViewById(R.id.safe_center_identity_authentication);
        safe_center_phone = (FrameLayout) findViewById(R.id.safe_center_phone);
        safe_center_pay_pwd = (FrameLayout) findViewById(R.id.safe_center_pay_pwd);
        safe_center_account_bind = (FrameLayout) findViewById(R.id.safe_center_account_bind);

        safe_center_identity_authentication_tv = (TextView) findViewById(R.id.safe_center_identity_authentication_tv);
        safe_center_phone_tv = (TextView) findViewById(R.id.safe_center_phone_tv);
        safe_center_account_pwd_tv = (TextView) findViewById(R.id.safe_center_account_pwd_tv);
        safe_center_pay_pwd_tv = (TextView) findViewById(R.id.safe_center_pay_pwd_tv);
        if (winguoGeneral != null) {
            if (!TextUtils.isEmpty(winguoGeneral.telMobile)) {
                safe_center_phone_tv.setText("更换");
            }
            String pizza = WinguoAccountDataMgr.getPizza(this);
            if (pizza != null ) {
                safe_center_account_pwd_tv.setText("修改");
            } else {
                if (!TextUtils.isEmpty(winguoGeneral.password))
                    safe_center_account_pwd_tv.setText("修改");

            }
        }
    }

    private void initListener() {
        safe_center_identity_authentication.setOnClickListener(this);
        safe_center_phone.setOnClickListener(this);
        safe_center_account_pwd.setOnClickListener(this);
        safe_center_pay_pwd.setOnClickListener(this);
        safe_center_account_bind.setOnClickListener(this);

    }

    private WinguoAccountGeneral winguoGeneral = null;
    private static final int IS_BIND = 0x56;
    private static final int IS_MODIFY = 0x55;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safe_center_phone:
                //手机号
                bindTel();
                break;
            case R.id.safe_center_account_pwd:
                //登录密码
                modifyLoginPwd();
                break;
            case R.id.safe_center_pay_pwd:
                //修改支付密码
                if (SPUtils.contains(this, "accountName")) {
                    startActivity(new Intent(this, ModifyPayWayActivity.class));
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_PAY_PWD_MOD);
                    startActivity(it);
                }
                break;
            case R.id.safe_center_identity_authentication:
                //身份认证
                break;
            case R.id.safe_center_account_bind:
                //账号绑定
                // startActivity(new Intent(AccountSafeActivity.this, AccountBindActivity.class));
                ToastUtil.showToast(this, "暂未开通此功能！");
                break;

        }
    }

    /**
     * 绑定手机号
     */
    private void bindTel() {
        if (winguoGeneral != null) {
            String telMobile = winguoGeneral.telMobile;
            Intent intent = new Intent(this, SetPhoneTelActivity.class);
            if (!TextUtils.isEmpty(telMobile)) {
                //暂定 手机号不为空 修改绑定
                intent.putExtra(Constants.IS_BIND, false);
                startActivityForResult(intent, IS_MODIFY);
            } else {
                //暂定 手机号为空 绑定手机号
                intent.putExtra(Constants.IS_BIND, true);
                startActivityForResult(intent, IS_BIND);
            }
        }
    }

    /**
     * 修改登录密码
     */
    private void modifyLoginPwd() {

        String pizza = WinguoAccountDataMgr.getPizza(this);
        WinguoAccountGeneral winguoGeneral = GBAccountMgr.getInstance().getAccountInfo().winguoGeneral;
        Intent setLoginPwd = null;
        if (pizza != null) {
            //通过账号密码登录
            setLoginPwd = new Intent(AccountSafeActivity.this, SetLoginPwdActivity.class);
            setLoginPwd.putExtra(Constants.IS_SET_PWD, false);
        } else {  //通过快捷登录
            if (!TextUtils.isEmpty(winguoGeneral.password)) {
                //快捷登录  短信登录 已经设置完密码  跳转修改密码
                setLoginPwd = new Intent(AccountSafeActivity.this, SetLoginPwdActivity.class);
                setLoginPwd.putExtra(Constants.IS_SET_PWD, false);
            } else {
                //无设置密码 （第三方微信登录 或 验证码快速注册登录）需要设置密码 （默认通过验证码设置新密码）
                if (TextUtils.isEmpty(winguoGeneral.telMobile)) {
                    //手机号为空
                    ToastUtil.showToast(AccountSafeActivity.this,"请先绑定手机号！");
                    return;
                }
                setLoginPwd = new Intent(AccountSafeActivity.this, ModifyPwdActivity.class);
            }
        }
        startActivity(setLoginPwd);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IS_BIND:
                    if (data != null) {
                        String bindPhone = data.getStringExtra(Constants.BIND_PHONE);
                        safe_center_account_pwd_tv.setText("修改");
                        if (GBAccountMgr.getInstance().getAccountInfo().winguoGeneral != null)
                            GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.telMobile = bindPhone;
                    }
                    break;
                case IS_MODIFY:
                    if (data != null) {
                        String modifyPhone = data.getStringExtra(Constants.BIND_PHONE);
                        if (GBAccountMgr.getInstance().getAccountInfo().winguoGeneral != null)
                            GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.telMobile = modifyPhone;
                    }
                    break;
            }
        }
    }
}
