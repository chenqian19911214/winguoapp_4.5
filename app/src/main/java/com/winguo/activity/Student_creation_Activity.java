package com.winguo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.personalcenter.setting.ExplainActivity;
import com.winguo.personalcenter.wallet.balance.WalletActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 学生创客
 */
public class Student_creation_Activity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, WinguoAcccountManagerUtils.IRequstRechargeNumberCallback {

    private FrameLayout top_back;
    private TextView layout_title;
    private TextView top_title_right;
    private TextView student_creation_protocol;
    private ImageView image_id;
    /**
     * 同意 条款
     */
    private CheckBox student_creation_chekBox_id;
    private Button student_include_button;
    private String registercash;

    @Override
    protected int getLayout() {
        return R.layout.activity_student_creation;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initViews() {
        image_id = (ImageView) findViewById(R.id.studio_creation_image_id);
        layout_title = (TextView) findViewById(R.id.layout_title);
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        top_title_right = (TextView) findViewById(R.id.top_title_right);
        student_creation_protocol = (TextView) findViewById(R.id.student_creation_protocol);
        student_creation_chekBox_id = (CheckBox) findViewById(R.id.student_creation_chekBox_id);
        student_include_button = (Button) findViewById(R.id.student_include_button);
        student_creation_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击阅读条款
                Intent agreement = new Intent(Student_creation_Activity.this, ExplainActivity.class);
                agreement.putExtra("assetUrl", Constants.assetStudentURL);
                startActivity(agreement);
            }
        });
        layout_title.setText("轻松创业");
    }

    @Override
    protected void initData() {
        if (NetWorkUtil.isNetworkAvailable(Student_creation_Activity.this)) {
            LoadDialog.show(this);
            WinguoAcccountManagerUtils.requestRechargeNumber(Student_creation_Activity.this, this);
        } else {
            ToastUtil.showToast(this, getString(R.string.timeout));
        }
    }

    @Override
    protected void setListener() {
        image_id.setOnClickListener(this);
        student_creation_chekBox_id.setOnCheckedChangeListener(this);
        student_include_button.setOnClickListener(this);
        top_back.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.student_include_button://马上 报名
                if (registercash != null) {
                    Intent intent = new Intent(getApplication(), WalletActivity.class);
                    intent.setAction(Constants.STUDENT_CREATE_RECHARGE_FLAG);
                    intent.putExtra("rechargeNumber", registercash);
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(this, getString(R.string.timeout));
                }
                break;
            case R.id.top_back:
                finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            student_include_button.setEnabled(true);
            student_include_button.setBackgroundResource(R.drawable.kedianji);
        } else {
            student_include_button.setEnabled(false);
            student_include_button.setBackgroundResource(R.drawable.bukedianji);
        }
    }

    @Override
    public void requstRechargeNumberCallback(String result) {
        LoadDialog.dismiss(this);
       /* {
            "code": 0,
             "msg": "获取成功",
             "registercash": 2000
        }*/
        if (result != null) {
            try {
                JSONObject root = new JSONObject(result);
                int code = root.getInt("code");
                if (code == 0) {
                    registercash = root.getString("registercash");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void requstRechargeNumberCallbackError(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this, errorcode));
    }

}
