package com.winguo.mine.address.acitvity;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.mine.address.AddressAdapter;
import com.winguo.mine.address.AddressManageHandle;
import com.winguo.mine.address.IAddressResult;
import com.winguo.mine.address.bean.AddressAllBean;
import com.winguo.mine.address.bean.AddressAreaBean;
import com.winguo.mine.address.bean.AddressInfoBean;
import com.winguo.mine.address.bean.AddressTownBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

/**
 * @author hcpai
 * @desc 选择市
 */
public class AddressAreaActivity extends BaseActivity2 implements AddressAdapter.IAddressListener {
    private RelativeLayout back;
    private View mSuccessView;
    private ListView mListView;
    private AddressAllBean mAddressAllBean;
    private TextView address_title_tv;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_addressselect;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        back = (RelativeLayout) findViewById(R.id.back);
        address_title_tv = (TextView) findViewById(R.id.address_title_tv);
        FrameLayout address_container = (FrameLayout) findViewById(R.id.address_container);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //有数据
        if (mSuccessView == null) {
            mSuccessView = View.inflate(this, R.layout.address_listview, null);
            mListView = (ListView) mSuccessView.findViewById(R.id.address_lv);
            address_container.addView(mSuccessView, params);
        }
        LoadDialog.show(AddressAreaActivity.this);
        mSuccessView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mAddressAllBean = (AddressAllBean) getIntent().getExtras().get(ActionUtil.ACTION_ADDRESS_SELECT);
        assert mAddressAllBean != null;
        AddressInfoBean city = mAddressAllBean.getCity();
        address_title_tv.setText(city.getName());
        AddressAreaBean areaBean = mAddressAllBean.getAreaBean();
        areaBean.getArea().getItem();
        LoadDialog.dismiss(this);
        mSuccessView.setVisibility(View.VISIBLE);
        AddressAdapter addressAdapter = new AddressAdapter(AddressAreaActivity.this, areaBean.getArea().getItem());
        addressAdapter.setListen(AddressAreaActivity.this);
        mListView.setAdapter(addressAdapter);
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {

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
        }
    }

    /**
     * item的点击回调
     *
     * @param addressInfoBean
     */
    @Override
    public void onClick(AddressInfoBean addressInfoBean) {
        mAddressAllBean.setArea(addressInfoBean);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            AddressManageHandle.getTown(addressInfoBean.getCode(), new IAddressResult<AddressTownBean>() {
                @Override
                public void getResult(AddressTownBean addressTownBean) {
                    //1.网络请求失败
                    if (addressTownBean == null) {
                        ToastUtil.show(AddressAreaActivity.this, getString(R.string.timeout));
                        //2.有镇数据:跳转到 镇Activity
                    } else if (addressTownBean.getTown() != null) {
                        mAddressAllBean.setTownBean(addressTownBean);
                        Intent intent = new Intent();
                        intent.putExtra(ActionUtil.ACTION_ADDRESS_SELECT, mAddressAllBean);
                        intent.setClass(AddressAreaActivity.this, AddressTownActivity.class);
                        startActivity(intent);
                    } else {
                        //3.没有镇数据:跳转到编辑界面
                        //发送广播
                        Intent intent1 = new Intent(ActionUtil.ACTION_ADDRESS_SELECT);
                        intent1.putExtra(ActionUtil.ACTION_ADDRESS_SELECT, mAddressAllBean);
                        sendBroadcast(intent1);
                        //跳转
                        Intent intent = new Intent();
                        intent.setClass(AddressAreaActivity.this, AddressEditActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else {
            ToastUtil.show(this, getString(R.string.offline));
        }
    }
}
