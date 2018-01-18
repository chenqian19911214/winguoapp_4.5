package com.winguo.personalcenter.safecenter.paypwd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.personalcenter.safecenter.loginpwd.ModifyPwdActivity;
import com.winguo.utils.SPUtils;

/**
 * Created by admin on 2017/5/5.
 * 修改支付密码 选择
 */

public class ModifyPayWayActivity extends BaseTitleActivity implements View.OnClickListener {

//    private FrameLayout back;
//    private TextView topTitle;
    private FrameLayout modify_pay_way_old;
    private FrameLayout modify_pay_way_code;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pay_way);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initDatas() {
//        CommonUtil.stateSetting(this,R.color.white_top_color);
        isLogin = SPUtils.contains(this, "accountName");
    }

    private void initViews() {
//        back = (FrameLayout) findViewById(R.id.top_back);
//        topTitle = (TextView) findViewById(R.id.layout_title);

        modify_pay_way_old = (FrameLayout) findViewById(R.id.modify_pay_way_old);
        modify_pay_way_code = (FrameLayout) findViewById(R.id.modify_pay_way_code);

    }


    private void initListener() {
//        back.setOnClickListener(this);
        modify_pay_way_old.setOnClickListener(this);
        modify_pay_way_code.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.top_back:
//                finish();
//                break;
            case R.id.modify_pay_way_old:
                startActivity(new Intent(this, ModifyPayPwdActivity.class));
                break;
            case R.id.modify_pay_way_code:

                Intent it = new Intent(this, ModifyPwdActivity.class);
                it.putExtra("payPwd", "pay");
                startActivity(it);

                break;

        }
    }
}
