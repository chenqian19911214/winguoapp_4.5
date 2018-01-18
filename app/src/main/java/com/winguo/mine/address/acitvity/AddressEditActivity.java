package com.winguo.mine.address.acitvity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.mine.address.AddressManageHandle;
import com.winguo.mine.address.bean.AddressAllBean;
import com.winguo.mine.address.bean.AddressInfoBean;
import com.winguo.mine.address.bean.AddressUpdateBean;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.FieldValidator;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author hcpai
 * @desc 地址编辑
 */
public class AddressEditActivity extends BaseActivity2 {
    private RelativeLayout back;
    /**
     * 收货人
     */
    private EditText address_receiver_et;
    /**
     * 收货电话
     */
    private EditText address_phone_et;
    /**
     * 邮政编码
     */
    private EditText address_postalcode_et;
    /**
     * 省市区
     */
    private TextView address_location_tv;
    /**
     * 详细地址
     */
    private EditText address_detail_et;
    /**
     * 确认
     */
    private Button address_confirm_btn;
    /**
     * 收件人
     */
    private String mReceiver;
    /**
     * 手机号码
     */
    private String mPhone;
    private String mLocation;
    private String mPostalcode;
    private String mDetail;
    /**
     * 广播接收者
     */
    private BroadcastReceiver mBroadcastReceiver;
    /**
     * 省市区
     */
    private AddressAllBean mAddressAllBean;
    private ItemBean mItemBean;
    private boolean mIsEdit;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_address_edit;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        back = (RelativeLayout) findViewById(R.id.back);
        address_receiver_et = (EditText) findViewById(R.id.address_receiver_et);
        address_phone_et = (EditText) findViewById(R.id.address_phone_et);
        address_postalcode_et = (EditText) findViewById(R.id.address_postalcode_et);
        address_location_tv = (TextView) findViewById(R.id.address_location_tv);
        address_detail_et = (EditText) findViewById(R.id.address_detail_et);
        address_confirm_btn = (Button) findViewById(R.id.address_confirm_btn);
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionUtil.ACTION_ADDRESS_SELECT);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mAddressAllBean = (AddressAllBean) intent.getExtras().get(ActionUtil.ACTION_ADDRESS_SELECT);
                if (mAddressAllBean != null) {
                    address_receiver_et.setText(mReceiver);
                    address_phone_et.setText(mPhone);
                    address_postalcode_et.setText(mPostalcode);
                    String address = mAddressAllBean.getProvince().getName() + "省 " + mAddressAllBean.getCity().getName() + " ";
                    if (mAddressAllBean.getArea() != null) {
                        address = address + mAddressAllBean.getArea().getName() + " ";
                    }
                    if (mAddressAllBean.getTown() != null) {
                        address = address + mAddressAllBean.getTown().getName();
                    }
                    address_location_tv.setText(address);
                    address_detail_et.setText(mDetail);
                }
            }
        };
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mItemBean = (ItemBean) getIntent().getExtras().get(ActionUtil.ADDRESS_EDIT_INFO);
        mIsEdit = (boolean) getIntent().getExtras().get(ActionUtil.ADDRESS_EDIT_OR_ADD);
        if (mItemBean != null) {
            address_receiver_et.setText(mItemBean.dinfo_received_name);
            address_phone_et.setText(mItemBean.dinfo_mobile);
            address_postalcode_et.setText(mItemBean.dinfo_zip + "");
            address_location_tv.setText(mItemBean.provinceName + "省 " + mItemBean.cityName + " " + mItemBean.areaName + " " + mItemBean.townName);
            address_detail_et.setText(mItemBean.dinfo_address);
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        address_location_tv.setOnClickListener(this);
        address_confirm_btn.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_UPDATEADDRESS:
                LoadDialog.dismiss(this);
                AddressUpdateBean addressUpdateBean = (AddressUpdateBean) msg.obj;
                if (addressUpdateBean == null) {
                    ToastUtil.show(this, getString(R.string.timeout));
                    return;
                }
                if (addressUpdateBean.getMessage().getCode() == 0) {
                    ToastUtil.showToast(this, addressUpdateBean.getMessage().getText());
                    finish();
                } else {
                    ToastUtil.showToast(this, addressUpdateBean.getMessage().getText());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.address_confirm_btn:
                confirm();
                break;
            case R.id.address_location_tv:
                //获取已输入的信息,以便回显
                mReceiver = address_receiver_et.getText().toString().trim();
                mPhone = address_phone_et.getText().toString().trim();
                mPostalcode = address_postalcode_et.getText().toString().trim();
                mLocation = address_location_tv.getText().toString().trim();
                mDetail = address_detail_et.getText().toString().trim();
                Intent intent = new Intent();
                intent.setClass(AddressEditActivity.this, AddressProvinceActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 处理提交按钮
     */
    private void confirm() {
        mReceiver = address_receiver_et.getText().toString().trim();
        mPhone = address_phone_et.getText().toString().trim();
        mPostalcode = address_postalcode_et.getText().toString().trim();
        mLocation = address_location_tv.getText().toString().trim();
        mDetail = address_detail_et.getText().toString().trim();
        if (TextUtils.isEmpty(mReceiver) || TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mLocation) || TextUtils.isEmpty(mDetail)) {
            ToastUtil.show(this, "请填写完整的信息!");
        } else {
            Pattern regexPhone = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$");
            Matcher matcher = regexPhone.matcher(mPhone);
            boolean isPhone = matcher.matches();
            if (!isPhone) {
                ToastUtil.show(this, "请输入正确的手机号!");
                return;
            }
            if (!FieldValidator.checkNoAllowChar(mDetail, "&")) {
                ToastUtil.show(this, "输入内容不符合要求(不能有字符：&)");
                return;
            }
            if (!FieldValidator.checkNoAllowChar(mReceiver, "&")) {
                ToastUtil.show(this, "输入内容不符合要求(不能有字符：&)");
                return;
            }
            //编辑地址
            if (mIsEdit) {
                if (mAddressAllBean == null) {
                    //为空表示没有修改省市区镇,直接用传过来的item数据
                    if (NetWorkUtil.isNetworkAvailable(this)) {
                        LoadDialog.show(this);
                        AddressManageHandle.updateAddressInfo(this, handler, mReceiver, mPostalcode, mDetail, mPhone, mItemBean.provinceCode, mItemBean.cityCode, mItemBean.areaCode, mItemBean.townCode, 0, mItemBean.addressId, 0);
                    } else {
                        ToastUtil.show(this, getString(R.string.offline));
                    }
                } else {
                    //不为空表示修改了省市区,用mAddressAllBean的数据
                    if (NetWorkUtil.isNetworkAvailable(this)) {
                        LoadDialog.show(this);
                        AddressManageHandle.updateAddressInfo(this, handler, mReceiver, mPostalcode, mDetail, mPhone, mAddressAllBean.getProvince().getCode(), mAddressAllBean.getCity().getCode(), mAddressAllBean.getArea().getCode(), getTownCode(mAddressAllBean), 0, mItemBean.addressId, 0);
                    } else {
                        ToastUtil.show(this, getString(R.string.offline));
                    }
                }
                //新增地址
            } else {
                if (mAddressAllBean != null) {
                    if (NetWorkUtil.isNetworkAvailable(this)) {
                        LoadDialog.show(this);
                        AddressManageHandle.updateAddressInfo(this, handler, mReceiver, mPostalcode, mDetail, mPhone, mAddressAllBean.getProvince().getCode(), mAddressAllBean.getCity().getCode(), mAddressAllBean.getArea().getCode(), getTownCode(mAddressAllBean), 1, -1, 0);
                    } else {
                        ToastUtil.show(this, getString(R.string.offline));
                    }
                } else {
                    ToastUtil.show(this, "请填写完整的信息!");
                }
            }
        }
    }

    /**
     * 获取镇代码
     *
     * @param addressAllBean
     * @return
     */
    private int getTownCode(AddressAllBean addressAllBean) {
        int townCode;
        AddressInfoBean area = addressAllBean.getTown();
        if (area == null) {
            townCode = -1;
        } else {
            townCode = area.getCode();
        }
        return townCode;
    }
}
