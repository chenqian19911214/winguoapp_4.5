package com.winguo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.ValidateUtil;

/**
 * Created by Admin on 2017/6/29.
 */

public class Physical_Add_UserInfo_Activity extends BaseTitleActivity implements View.OnClickListener{
    private EditText physical_input_username;
    private EditText physical_input_userphone;
    private TextView tv_physical_userinfo_hint;
    private TextView physical_save_userinfo_btn;
    private SharedPreferences sharedPreferences;
    private String username;
    private String userphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_userinfo);
        initView();
        initListener();
        setBackBtn();
        sharedPreferences = getSharedPreferences("physical_pay_userInfo", MODE_PRIVATE);
    }

    private void initListener() {
        physical_save_userinfo_btn.setOnClickListener(this);
    }

    private void initView() {
        physical_input_username = (EditText) findViewById(R.id.physical_input_username);
        physical_input_userphone = (EditText) findViewById(R.id.physical_input_userphone);
        tv_physical_userinfo_hint = (TextView) findViewById(R.id.tv_physical_userinfo_hint);
        physical_save_userinfo_btn = (TextView) findViewById(R.id.physical_save_userinfo_btn);

    }

    private void setHintText(String hintText) {
        tv_physical_userinfo_hint.setText(hintText);
        tv_physical_userinfo_hint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.physical_save_userinfo_btn:
                //先判断是否为空，都不为空就判断是否符合格式
                username = physical_input_username.getText().toString().trim();
                userphone = physical_input_userphone.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    setHintText(getString(R.string.physical_username_empty_hint));
                    break;
                }else {
                    tv_physical_userinfo_hint.setVisibility(View.INVISIBLE);
                }
                if (TextUtils.isEmpty(userphone)) {
                    setHintText(getString(R.string.physical_username_empty_hint));
                    break;
                }else {
                    tv_physical_userinfo_hint.setVisibility(View.INVISIBLE);
                }
                if (!ValidateUtil.isLegalName(username)){
                    setHintText(getString(R.string.physical_username_error_hint));
                    break;
                }else {
                    tv_physical_userinfo_hint.setVisibility(View.INVISIBLE);
                }
                if (!ValidateUtil.isTelPhoneNumber(userphone)&&!ValidateUtil.isHKPhoneLegal(userphone)){
                    setHintText(getString(R.string.physical_userphone_error_hint));
                    break;
                }else {
                    tv_physical_userinfo_hint.setVisibility(View.INVISIBLE);
                }

                sharedPreferences.edit().putString("physical_user_name", username).apply();
                sharedPreferences.edit().putString("physical_user_phone", userphone).apply();
                Intent intent=new Intent();
                intent.putExtra("username",username);
                intent.putExtra("userphone",userphone);
                setResult(Constants.PHYSICAL_RESULT_CODE,intent);
                finish();
                break;
        }

    }
}
