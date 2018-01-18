package com.winguo.listener;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

import com.winguo.utils.ToastUtil;
import com.winguo.utils.ValidateUtil;

/**
 * 关键字搜索 输入框文本改变监听
 * Created by admin on 2016/12/2.
 */

public class ITextWatcher implements TextWatcher {

    private static final String TAG = "ITextWatcher";
    /**
     * 手机号
     */
    public static final int TYPE_ACCOUNT = 1;

    /**
     * 密码
     */
    public static final int TYPE_PWD = 2;

    /**
     * 输入框
     */
    private EditText mEditText;

    /**
     * 验证类型
     */
    private int examineType;

    /**
     * 输入前的文本内容
     */
    private String beforeText;

    private Activity context;

    /**
     * 构造器
     *
     * @param type     验证类型
     * @param editText 输入框
     */
    public ITextWatcher(int type, EditText editText, Context context) {
        this.examineType = type;
        this.mEditText = editText;
        this.context = (Activity) context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // 输入前的字符
        beforeText = s.toString();
        Log.d(TAG, "beforeText =>>>" + beforeText);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 输入后的字符
        String afterText = s.toString();
        Log.d(TAG, "afterText =>>>" + afterText);
        String tip = "";
        boolean isValid = true;
        if (!TextUtils.isEmpty(afterText)) {
            switch (examineType) {
                case TYPE_ACCOUNT:

                    break;
                case TYPE_PWD:
                    isValid = ValidateUtil.isPassword(afterText);
                    if (!isValid) {
                        tip = "请确认输入正确的密码（不超过16位）";
                    }
                    break;
            }
            if (!isValid) {
                // 用户现在输入的字符数减去之前输入的字符数，等于新增的字符数
                int differ = afterText.length() - beforeText.length();
                // 如果用户的输入不符合规范，则显示之前输入的文本
                mEditText.setText(beforeText);
                // 光标移动到文本末尾
                mEditText.setSelection(afterText.length() - differ);
                ToastUtil.show(context, tip);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
