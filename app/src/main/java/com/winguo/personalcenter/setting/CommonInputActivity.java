package com.winguo.personalcenter.setting;

import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountError;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.personalcenter.setting.control.PersonalControl;
import com.winguo.personalcenter.setting.moudle.RequestModifyNickCallback;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/10/16.
 * 个人资料 通用信息设置界面
 */

public class CommonInputActivity extends BaseActivity implements RequestModifyNickCallback {
    private FrameLayout common_input_back;
    private TextView common_input_title;
    private TextView common_input_submit;
    private EditText common_input_et;
    private String type;
    private PersonalControl control;

    @Override
    protected int getLayout() {
        return R.layout.activity_common_input;
    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("type");
        control = new PersonalControl(this);
    }

    @Override
    protected void initViews() {
        common_input_back = (FrameLayout) findViewById(R.id.common_input_back);
        common_input_title = (TextView) findViewById(R.id.common_input_title);
        common_input_submit = (TextView) findViewById(R.id.common_input_submit);
        common_input_et = (EditText) findViewById(R.id.common_input_et);
        switch (type) {
            case "昵称":
                common_input_et.setInputType(InputType.TYPE_CLASS_TEXT);
                //common_input_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});  //没提示
                common_input_et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Editable editableText = common_input_et.getEditableText();
                        int length = s.toString().trim().length();
                        if (length > 8) {
                            ToastUtil.showToast(CommonInputActivity.this,"昵称的长度不能超过8个字符！");
                            int selectionEnd = Selection.getSelectionEnd(editableText);
                            String str = editableText.toString();
                            //截取新字符
                            String newStr = str.substring(0,8);
                            common_input_et.setText(newStr);
                            //新字符串长度
                            editableText = common_input_et.getText();
                            int newLen = editableText.length();
                            if (selectionEnd > newLen) {
                                selectionEnd = editableText.length();
                            }
                            //设置新光标所在的位置
                            Selection.setSelection(editableText,selectionEnd);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
        }
        common_input_title.setText(type);
    }

    @Override
    protected void setListener() {
        common_input_back.setOnClickListener(this);
        common_input_submit.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.common_input_back:
                finish();
                break;
            case R.id.common_input_submit:
                String trim = common_input_et.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.showToast(this, "输入内容不能为空！");
                    return;
                }

                switch (type) {
                    case "昵称":
                        //请求
                        if (trim.length() < 2) {
                            ToastUtil.showToast(this, "昵称的长度不能少于2个字符！");
                            return;
                        }
                        if (NetWorkUtil.isNetworkAvailable(this)) {
                            control.setUserNick(this, trim);
                        } else {
                            ToastUtil.showToast(this,getString(R.string.timeout));
                        }
                        break;
                }

                break;

        }
    }

    @Override
    public void requestModifyNickResult(String result) {
        try {
            JSONObject root = new JSONObject(result);
            int code = root.getInt("code");
            String msg = root.getString("msg");
            if (code == 0) {
                ToastUtil.showToast(this, msg);
                String trim = common_input_et.getText().toString().trim();
                Intent it = new Intent();
                it.putExtra("result", trim);
                setResult(RESULT_OK, it);
                finish();
            } else {
                ToastUtil.showToast(this, "上传失败，请稍后再试！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestModifyNickErrorCode(int errorcode) {
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }

}
