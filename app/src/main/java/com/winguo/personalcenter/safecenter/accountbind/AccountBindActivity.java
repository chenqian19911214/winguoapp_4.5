package com.winguo.personalcenter.safecenter.accountbind;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseActivity;

/**
 * Created by admin on 2017/10/12.
 * 账户 绑定
 */

public class AccountBindActivity extends BaseActivity {
    private FrameLayout top_back;
    private TextView layout_title;
    private TextView account_bind_weixin_bind_tv;
    private FrameLayout account_bind_weixin_bind;

    @Override
    protected int getLayout() {
        return R.layout.activity_account_binding;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        layout_title.setText("账号绑定");
        account_bind_weixin_bind_tv = (TextView) findViewById(R.id.account_bind_weixin_bind_tv);
        account_bind_weixin_bind = (FrameLayout) findViewById(R.id.account_bind_weixin_bind);
    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        account_bind_weixin_bind.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
        }
    }

}
