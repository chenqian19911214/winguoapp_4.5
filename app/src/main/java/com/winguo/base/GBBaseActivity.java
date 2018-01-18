package com.winguo.base;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by admin on 2017/3/17.
 */

public class GBBaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
