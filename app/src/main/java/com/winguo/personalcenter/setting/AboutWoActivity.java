package com.winguo.personalcenter.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.winguo.BuildConfig;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.utils.Constants;

/**
 * 关于我们
 */

public class AboutWoActivity extends BaseTitleActivity implements View.OnClickListener {

    //    private ImageView feedBack;
//    private TextView title;
//    private LinearLayout topBack;
    private TextView permissionIll;
    private TextView userAgree;
    private String titleType;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_about_wo);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initDatas() {
//        CommonUtil.stateSetting(this,R.color.white_top_color);
//        Intent it = getIntent();
//        titleType =  it.getStringExtra("title");

    }

    private void initViews() {
//        topBack = (LinearLayout) findViewById(R.id.top_back_about);
//        title = (TextView) findViewById(R.id.top_layout_title_about);
        version = (TextView) findViewById(R.id.about_version);
//        feedBack = (ImageView) findViewById(R.id.top_user_feedback_about);
        permissionIll = (TextView) findViewById(R.id.about_permission_illustrate);
        userAgree = (TextView) findViewById(R.id.about_user_agreement);
        setData();
    }


    private void setData() {
//      title.setText(titleType);
        String currVer = BuildConfig.VERSION_NAME + " " + getString(R.string.version_date);
        version.setText(currVer);
    }

    private void initListener() {
//        feedBack.setOnClickListener(this);
//        topBack.setOnClickListener(this);
        permissionIll.setOnClickListener(this);
        userAgree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.top_back_about:
//                //顶部返回
//                finish();
//                break;
//            case R.id.top_user_feedback_about:
//                //顶部用户反馈
//                Intents.feedBack(this,true);
//                break;
            case R.id.about_permission_illustrate:
                //权限使用说明
                Intent permission = new Intent(this, ExplainActivity.class);
                permission.putExtra("assetUrl", Constants.assetPermissionURL);
                startActivity(permission);
                break;
            case R.id.about_user_agreement:
                //用户使用协议
                Intent agreement = new Intent(this, ExplainActivity.class);
                agreement.putExtra("assetUrl", Constants.assetUserURL);
                startActivity(agreement);
                break;
        }
    }
}
