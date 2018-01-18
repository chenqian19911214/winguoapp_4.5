package com.winguo.personalcenter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

/**
 * Created by admin on 2017/2/22.
 * 开通空间站
 */

public class OpenSpaceActivity extends BaseActivity implements WinguoAcccountManagerUtils.IOpenSpaceStation {

    private RelativeLayout back;
    private EditText spaceName;
    private Button submit;
    private String accountName;
    @Override
    protected void initViews() {
        back = (RelativeLayout) findViewById(R.id.my_open_space_station_back);
        spaceName = (EditText) findViewById(R.id.space_station_name);
        submit = (Button) findViewById(R.id.open_space_station_bt);
    }

    @Override
    protected int getLayout() {
        return R.layout.my_open_space_station;
    }

    @Override
    protected void initData() {
        if (SPUtils.contains(this, "accountName")) {
            accountName = (String) SPUtils.get(this, "accountName", "");
        }
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        spaceName.addTextChangedListener(new TextChangeListener());
        submit.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {}

    private static final int OPEN_SPACE_STATION = 7;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case OPEN_SPACE_STATION:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                String message = getString(R.string.hybrid4_account_info_open_space);
                dialog.setMessage(message);
                return dialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeDialog(OPEN_SPACE_STATION);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.my_open_space_station_back:
                finish();
                break;
            case R.id.open_space_station_bt:
                //提交空间站名接口
              //  WinguoAccountImpl.openSpaceStation(this, accountName, spaceName.getText().toString() + "空间站");
                String name = spaceName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast(OpenSpaceActivity.this,"空间站名不能为空！");
                    return;
                }
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    showDialog(OPEN_SPACE_STATION);
                    WinguoAcccountManagerUtils.openSpaceStation(this,WinguoAccountDataMgr.getUserName(OpenSpaceActivity.this), name, OpenSpaceActivity.this);
                } else {
                    ToastUtil.showToast(OpenSpaceActivity.this,getString(R.string.timeout));
                }
                break;
        }
    }

    @Override
    public void requestOpenSpaceStation(String statu,String text) {

       String action =  getIntent().getAction();
        if (statu.equals("success")) {
            removeDialog(OPEN_SPACE_STATION);
            ToastUtil.showToast(this, text);
            Intent openSuccess = new Intent();
            openSuccess.setAction(Constants.OPEN_SUCCESS);
            sendBroadcast(openSuccess);
            OpenSpaceActivity.this.setResult(RESULT_OK,new Intent());
            if (action.equals(Constants.LOGIN_WAY_QR)){
                Intent myqrintent = new Intent();
                myqrintent.setClass(this,MyQRCodeActivity.class);
                startActivity(myqrintent);
            }
            finish();
        } else {
            removeDialog(OPEN_SPACE_STATION);
            ToastUtil.showToast(this, text);
        }
    }

    @Override
    public void requestOpenError(String error) {
        removeDialog(OPEN_SPACE_STATION);
        ToastUtil.show(OpenSpaceActivity.this, error);
    }

    /**
     * 监听文本改变
     */
    class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(charSequence) && charSequence.length() >= 2) {
                submit.setBackgroundResource(R.drawable.space_station_name_inputing);
                submit.setEnabled(true);
            } else {
                submit.setBackgroundResource(R.drawable.space_station_name_null);
                submit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
