package com.winguo.personalcenter;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseActivity;

/**
 * Created by admin on 2017/12/29.
 * 创客收益
 */

public class CreaterIncomeActivity extends BaseActivity {

    private ImageView creater_income_back_iv;
    private TextView creater_income_state_tv;

    @Override
    protected int getLayout() {
        return R.layout.activity_creater_income;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
          creater_income_back_iv = (ImageView) findViewById(R.id.creater_income_back_iv);
          creater_income_state_tv = (TextView) findViewById(R.id.creater_income_state_tv);
    }

    @Override
    protected void setListener() {
        creater_income_back_iv.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.creater_income_back_iv:
                finish();
                break;
        }
    }

}
