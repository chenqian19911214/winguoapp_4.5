package com.winguo.personalcenter.safecenter.paypwd;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.guobi.account.GBAccountError;
import com.guobi.account.NetworkUtils;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.ValidateUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通过旧密码 修改支付密码
 * Created by admin on 2017/5/5.
 */

public class ModifyPayPwdActivity extends BaseTitleActivity implements WinguoAcccountManagerUtils.IResetPayPwd,View.OnClickListener {

//    private TextView pay_modify_cancel;
    private Button modify_pay_bt;
    private ImageView delete, delete1,oldPwdDelete;
    private ToggleButton show, show1,oldPwdShow;
    private EditText pwdEt,oldPwdEt,pwdEt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_modify_pay_layout);
        setBackBtn();
        initViews();
        initListener();
    }
    protected void initViews() {
//        pay_modify_cancel = (TextView) findViewById(R.id.pay_modify_cancel);
        modify_pay_bt = (Button) findViewById(R.id.winguo_account_modify_pay_bt);
        //旧密码
        oldPwdEt = (EditText) findViewById(R.id.winguo_pay_old_pwd_et);
        oldPwdDelete = (ImageView) findViewById(R.id.winguo_account_old_pwd_et_delete_iv);
        oldPwdShow = (ToggleButton) findViewById(R.id.winguo_account_old_pwd_et_showpwd_cb2);
        //新密码
        pwdEt = (EditText) findViewById(R.id.winguo_account_reset_pwd_et);
        show = (ToggleButton) findViewById(R.id.winguo_account_reset_pwd_et_showpwd_cb2);
        delete = (ImageView) findViewById(R.id.winguo_account_reset_pwd_et_delete_iv);
        //确认密码
        pwdEt1 = (EditText) findViewById(R.id.winguo_account_reset_pwd_et1);
        delete1 = (ImageView) findViewById(R.id.winguo_reset_pwd_et1_delete_iv);
        show1 = (ToggleButton) findViewById(R.id.winguo_reset_pwd_et1_showpwd_cb2);
    }

    private final static int MODIFY_PAY = 0;
    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        switch (id) {
            case MODIFY_PAY: {
                String message = getString(R.string.hybrid4_account_info_pwd_modyfing);
                dialog.setMessage(message);

                return dialog;
            }

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void initListener() {
//        pay_modify_cancel.setOnClickListener(this);
        modify_pay_bt.setOnClickListener(this);

        oldPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String account = s.toString();
                if (!TextUtils.isEmpty(account)) {
                    oldPwdDelete.setVisibility(View.VISIBLE);
                } else {
                    oldPwdDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwdEt.addTextChangedListener(new TextWatcher() {
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
        pwdEt1.addTextChangedListener(new TextWatcher() {
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
                pwdEt.setText("");
            }
        });
        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdEt1.setText("");
            }
        });
        oldPwdDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPwdEt.setText("");
            }
        });

        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) pwdEt.getText(), pwdEt.length());
                } else {
                    // 隐藏密码
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) pwdEt.getText(), pwdEt.length());
                }
            }
        });
        show1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    pwdEt1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) pwdEt1.getText(), pwdEt1.length());
                } else {
                    // 隐藏密码
                    pwdEt1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) pwdEt1.getText(), pwdEt1.length());
                }
            }
        });
        oldPwdShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    oldPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) oldPwdEt.getText(), oldPwdEt.length());
                } else {
                    // 隐藏密码
                    oldPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) oldPwdEt.getText(), oldPwdEt.length());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.pay_modify_cancel:
//                finish();
//                break;
            case R.id.winguo_account_modify_pay_bt:
                //提交
                String old = oldPwdEt.getText().toString();
                String pwd = pwdEt.getText().toString();
                String pwd1 = pwdEt1.getText().toString();
                if (old == null || old.length() == 0) {
                    String info = getString(R.string.old_pay_pwd_null);
                    ToastUtil.show(this, info);

                    break;
                }  else if (pwd == null || pwd.length() == 0) {
                    String info = getString(R.string.new_pay_pwd_null);
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
                        showDialog(MODIFY_PAY);
                        if (NetworkUtils.isConnectNet(this)) {
                            WinguoAcccountManagerUtils.resetPayPwByOld(this,old,pwd,this);

                        } else {
                            removeDialog(MODIFY_PAY);
                            Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                break;
        }
    }

    @Override
    public void resetPayPwd(String result) {
        removeDialog(MODIFY_PAY);
        try {
            JSONObject root = new JSONObject(result);
            JSONObject message = root.getJSONObject("message");
            String text = message.getString("text");
            String status = message.getString("status");
            //result:: {"message":{"status":"error","text":"原密码输入错误。","code":-2}}
            // result:: {"message":{"status":"success","text":"支付密码修改成功。","code":0}}
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

    @Override
    public void resetPayPwdErrorMsg(int message) {
        removeDialog(MODIFY_PAY);
        if (message == GBAccountError.REQUST_TIMEOUT) {
            ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
            return;
        }
        ToastUtil.show(ModifyPayPwdActivity.this, GBAccountError.getErrorMsg(this,message));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
