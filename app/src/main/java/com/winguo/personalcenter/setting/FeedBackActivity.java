package com.winguo.personalcenter.setting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountError;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

/**
 * 意见反馈
 */

public class FeedBackActivity extends BaseTitleActivity implements View.OnClickListener, WinguoAcccountManagerUtils.ISubmitFeedBack {

    private static final int FEEDBACK_SUBMIT = 0x12356 ;
//    private FrameLayout back;
    private TextView phoneInfo;
    private EditText feedBack;
    private RadioButton confirm;
//    private TextView topTitle;

    private String phoneInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_feedback_layout);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initViews() {
//        back = (FrameLayout) findViewById(R.id.top_back);
//        topTitle = (TextView) findViewById(R.id.layout_title);
        phoneInfo = (TextView) findViewById(R.id.feedback_phoneInfo);
        feedBack = (EditText) findViewById(R.id.feedback_content);
        confirm = (RadioButton) findViewById(R.id.feedback_confirm);
        setData();
    }

    private void setData() {
//        topTitle.setText("意见反馈");
        phoneInfo.setText(phoneInfos);
    }

    private void initDatas() {
//        CommonUtil.stateSetting(this,R.color.white_top_color);
        //异步获取手机信息 填充数据
        String product = Build.MANUFACTURER; //制造商
        String release = Build.VERSION.RELEASE;//设备的系统版本
        int screenWidth = ScreenUtil.getScreenWidth(this);
        int screenHeight = ScreenUtil.getScreenHeight(this);
        phoneInfos = product+" "+" Android "+release+" "+screenWidth+"*"+screenHeight;
    }


    private void initListener() {
//        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.top_back:
//                finish();
//                break;
            case R.id.feedback_confirm:
                //确认提交意见 如果为空 提示请填写
                submitContent();
                //ToastUtil.showToast(getBaseContext(), "确定提交");
                break;
        }
    }

    private void submitContent() {
        if (!isNetConnected(this)) {
            Toast.makeText(this, R.string.feedback_submit_error, Toast.LENGTH_SHORT).show();
        } else {
            String msg = feedBack.getText().toString();
            if (TextUtils.isEmpty(msg)) {
                Toast.makeText(this, R.string.feedback_input_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            showDialog(FEEDBACK_SUBMIT);
            //WinguoAccountImpl.submitFeedback(this,msg);
            WinguoAcccountManagerUtils.submitFeedback(this,msg,FeedBackActivity.this);
        }

    }

    public boolean isNetConnected(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case FEEDBACK_SUBMIT:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                String message = getString(R.string.hybrid4_account_info_feedback);
                dialog.setMessage(message);

                return dialog;
            default:
                break;
        }

        return super.onCreateDialog(id);

    }

    @Override
    public void requestSubmitFeedBack(String status, String text) {
        if (status.equals("success")) {
            removeDialog(FEEDBACK_SUBMIT);
            Toast.makeText(this, getString(R.string.feedback_submit_ok), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            removeDialog(FEEDBACK_SUBMIT);
            ToastUtil.showToast(this,text);
        }
    }

    @Override
    public void requestSubmitFeedBackError(int errorcode) {
        removeDialog(FEEDBACK_SUBMIT);
        if (errorcode == GBAccountError.REQUST_TIMEOUT) {
            ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
            return;
        }
        ToastUtil.show(FeedBackActivity.this, GBAccountError.getErrorMsg(this,errorcode));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
