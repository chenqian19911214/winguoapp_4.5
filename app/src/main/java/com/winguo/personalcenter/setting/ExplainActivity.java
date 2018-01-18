package com.winguo.personalcenter.setting;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;

import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.utils.CommonUtil;

/**
 * Created by admin on 2017/2/7.
 * 加载asset 权限说明.html /  用户使用协议.html
 */

public class ExplainActivity extends BaseActivity {

    private WebView webView;
    private String assetUrl;

    @Override
    protected void initViews() {
        webView = (WebView) findViewById(R.id.explain_wv);
        webView.loadUrl(assetUrl);
    }

    @Override
    protected int getLayout() {
        return R.layout.explain_activity;
    }

    @Override
    protected void initData() {
        CommonUtil.stateSetting(this,R.color.white_top_color);
        Intent intent = getIntent();
        if (intent != null) {
            assetUrl = intent.getStringExtra("assetUrl");
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {

    }
}
