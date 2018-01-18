package com.winguo.personalcenter.safecenter.loginpwd;

import android.content.Intent;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guobi.account.GBAccountError;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/10/10.
 * 设置登录密码
 */

public class SetLoginPwdActivity extends BaseActivity implements View.OnClickListener, IRequestResultHandle {

    private boolean isSetPwd;
    private EditText set_loginpwd_et;
    private ToggleButton set_loginpwd_tb;
    private EditText set_loginpwd_et2;
    private ToggleButton set_loginpwd_tb2;
    private Button set_loginpwd_submit;
    private EditText modify_loginpwd_byold_oldet;
    private ToggleButton modify_loginpwd_byold_oldtb;
    private EditText modify_loginpwd_byold_et;
    private ToggleButton modify_loginpwd_byold_tb;
    private EditText modify_loginpwd_byold_et2;
    private ToggleButton modify_loginpwd_byold_tb2;
    private Button modify_loginpwd_byold_submit;
    private TextView modify_loginpwd_byold_forgetpwd_tv;
    private RelativeLayout set_login_pwd_back;
    private SetLoginPwdControl control;

    @Override
    protected int getLayout() {
        return R.layout.activity_set_login_pwd_head;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        isSetPwd = intent.getBooleanExtra(Constants.IS_SET_PWD, false);
        control = new SetLoginPwdControl(this);
    }

    @Override
    protected void initViews() {
        FrameLayout set_login_pwd_container = (FrameLayout) findViewById(R.id.set_login_pwd_container);
        set_login_pwd_back = (RelativeLayout) findViewById(R.id.set_login_pwd_back);
        View content = null;
        if (isSetPwd) {
            content = mInflater.inflate(R.layout.activity_set_login_pwd, null);
            initSetPwdView(content);
        } else {
            content = mInflater.inflate(R.layout.activity_modify_loginpwd_byold, null);
            initModifyPwdView(content);
        }
        set_login_pwd_container.addView(content);
    }

    @Override
    protected void setListener() {
        set_login_pwd_back.setOnClickListener(this);
        if (isSetPwd) {
            setPwdListener();
        } else {
            setModifyPwdListener();
        }
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.set_login_pwd_back:
                finish();
                break;
            case R.id.modify_loginpwd_byold_submit:
                //旧密码 修改密码
                String old = modify_loginpwd_byold_oldet.getText().toString().trim();
                String newpwd1 = modify_loginpwd_byold_et.getText().toString().trim();
                String newpwd2 = modify_loginpwd_byold_et2.getText().toString().trim();
                if (TextUtils.isEmpty(old)) {
                    ToastUtil.showToast(this,"旧密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(newpwd1)) {
                    ToastUtil.showToast(this,"新密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(newpwd2)) {
                    ToastUtil.showToast(this,"确认新密码不能为空");
                    return;
                }
                if (!newpwd2.equals(newpwd1)) {
                    ToastUtil.showToast(this,"两次密码不相同");
                    return;
                }
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    control.modifyLoginPwdByOld(SetLoginPwdActivity.this, old, newpwd1);
                } else {
                    ToastUtil.showToast(this, getString(R.string.timeout));
                }
                break;
            case R.id.modify_loginpwd_byold_forgetpwd_tv:
                //忘记旧密码 通过验证码修改
                Intent reset = new Intent(SetLoginPwdActivity.this, ModifyPwdActivity.class);
                startActivity(reset);
                break;
            case R.id.set_loginpwd_submit:
                //设置登录密码 提交
                break;
        }
    }

    private void initModifyPwdView(View v) {
        modify_loginpwd_byold_oldet = (EditText) v.findViewById(R.id.modify_loginpwd_byold_oldet);
        modify_loginpwd_byold_oldtb = (ToggleButton) v.findViewById(R.id.modify_loginpwd_byold_oldtb);
        modify_loginpwd_byold_et = (EditText) v.findViewById(R.id.modify_loginpwd_byold_et);
        modify_loginpwd_byold_tb = (ToggleButton) v.findViewById(R.id.modify_loginpwd_byold_tb);
        modify_loginpwd_byold_et2 = (EditText) v.findViewById(R.id.modify_loginpwd_byold_et2);
        modify_loginpwd_byold_tb2 = (ToggleButton) v.findViewById(R.id.modify_loginpwd_byold_tb2);
        modify_loginpwd_byold_submit = (Button) v.findViewById(R.id.modify_loginpwd_byold_submit);
        modify_loginpwd_byold_forgetpwd_tv = (TextView) v.findViewById(R.id.modify_loginpwd_byold_forgetpwd_tv);
    }

    private void setModifyPwdListener() {

        modify_loginpwd_byold_submit.setOnClickListener(this);
        modify_loginpwd_byold_forgetpwd_tv.setOnClickListener(this);
        modify_loginpwd_byold_oldtb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    modify_loginpwd_byold_oldet.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    modify_loginpwd_byold_oldet.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
        modify_loginpwd_byold_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    modify_loginpwd_byold_et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    modify_loginpwd_byold_et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
        modify_loginpwd_byold_tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    modify_loginpwd_byold_et2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    modify_loginpwd_byold_et2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
    }


    private void initSetPwdView(View v) {

        set_loginpwd_et = (EditText)v.findViewById(R.id.set_loginpwd_et);
        set_loginpwd_tb = (ToggleButton)v.findViewById(R.id.set_loginpwd_tb);
        set_loginpwd_et2 = (EditText) v.findViewById(R.id.set_loginpwd_et2);
        set_loginpwd_tb2 = (ToggleButton) v.findViewById(R.id.set_loginpwd_tb2);
        set_loginpwd_submit = (Button) v.findViewById(R.id.set_loginpwd_submit);

    }

    private void setPwdListener() {
        set_loginpwd_submit.setOnClickListener(this);
        set_loginpwd_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    set_loginpwd_et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    set_loginpwd_et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
        set_loginpwd_tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    set_loginpwd_et2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    set_loginpwd_et2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
    }

    @Override
    public void requestSetLoginPwdResult(String result) {

    }

    @Override
    public void requestSetLoginPwdErrorCode(int errorcode) {

    }

    @Override
    public void requestModfifyLoginPwdByOldResult(String result) {
        ToastUtil.showToast(this,result);
        try {

            JSONObject root = new JSONObject(result);
            JSONObject message = root.getJSONObject("message");
            String status = message.getString("status");
            String text = message.getString("text");
            String code = message.getString("code");
            ToastUtil.showToast(this, text);
            if ("0".equals(code)) {
                String newpwd = modify_loginpwd_byold_et.getText().toString().trim();
                WinguoAccountDataMgr.setPizza(this,newpwd);
                finish();
            } else {
                ToastUtil.showToast(this, text);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestModfifyLoginPwdByOldErrorCode(int errorcode) {
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }

}
