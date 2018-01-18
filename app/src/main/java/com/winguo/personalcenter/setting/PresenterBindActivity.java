package com.winguo.personalcenter.setting;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.personalcenter.setting.control.PersonalControl;
import com.winguo.personalcenter.setting.moudle.RequestBindTelCallback;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/10/12.
 * 推荐者 绑定
 */

public class PresenterBindActivity extends BaseActivity implements View.OnClickListener,RequestBindTelCallback {
    private FrameLayout top_back;
    private TextView layout_title;
    private EditText presenter_binding_phone_et;
    private Button presenter_binding_phone_submit;

    @Override
    protected int getLayout() {
        return R.layout.activity_presenter_binding;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        layout_title.setText("推荐者绑定");

        presenter_binding_phone_et = (EditText) findViewById(R.id.presenter_binding_phone_et);
        presenter_binding_phone_submit = (Button) findViewById(R.id.presenter_binding_phone_submit);
    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        presenter_binding_phone_submit.setOnClickListener(this);
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
            case R.id.presenter_binding_phone_submit:
                //绑定推荐人
                toBindingTel();
                break;
        }
    }

    private void toBindingTel() {
        String tel = presenter_binding_phone_et.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            ToastUtil.showToast(PresenterBindActivity.this, "请输入推荐人手机号！");
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(PresenterBindActivity.this)) {
            ToastUtil.showToast(PresenterBindActivity.this, getString(R.string.no_net));
        } else {
            PersonalControl control = new PersonalControl(PresenterBindActivity.this);
            LoadDialog.show(this,true);
            control.bindReferrTel(PresenterBindActivity.this,tel);
        }

    }

    @Override
    public void requestBindTelResult(String result) {
        //  {"code": 0, "msg": "绑定成功15112119980"}
        LoadDialog.dismiss(PresenterBindActivity.this);
        try {
            JSONObject root = new JSONObject(result);
            int code = root.getInt("code");
            String msg = root.getString("msg");
            ToastUtil.showToast(PresenterBindActivity.this,msg);
            if (code == 0) {
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestBindTelErrorCode(int errorcode) {
        LoadDialog.dismiss(PresenterBindActivity.this);
        ToastUtil.showToast(PresenterBindActivity.this, GBAccountError.getErrorMsg(PresenterBindActivity.this,errorcode));
    }

}
