package com.winguo.mine.address.acitvity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.winguo.mine.address.bean.AddressInfoBean;
import com.winguo.mine.address.bean.AddressProvinceBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

/**
 * @author hcpai
 * @desc 选择省
 */
public class AddressProvinceActivity extends BaseActivity2 implements AddressAdapter.IAddressListener {
    private RelativeLayout back;
    private View mSuccessView;
    private ListView mListView;
    private View noNetView;
    private Button btn_request_net;

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
        //无网络
        if (noNetView == null) {
            noNetView = View.inflate(this, R.layout.no_net, null);
            btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
            TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
            no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            address_container.addView(noNetView);
        }
        noNetView.setVisibility(View.GONE);
        LoadDialog.show(this);
        mSuccessView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getDataByNetStatus();
    }

    /**
     * 根据网路状态加载数据
     */
    private void getDataByNetStatus() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            AddressManageHandle.getProvince(new IAddressResult<AddressProvinceBean>() {
                private AddressAdapter mAdapter;

                @Override
                public void getResult(AddressProvinceBean addressProvinceBean) {
                    LoadDialog.dismiss(AddressProvinceActivity.this);
                    if (addressProvinceBean == null) {
                        ToastUtil.show(AddressProvinceActivity.this, getString(R.string.timeout));
                        return;
                    }
                    mSuccessView.setVisibility(View.VISIBLE);
                    noNetView.setVisibility(View.GONE);
                    mAdapter = new AddressAdapter(AddressProvinceActivity.this, addressProvinceBean.getProvince().getItem());
                    mAdapter.setListen(AddressProvinceActivity.this);
                    mListView.setAdapter(mAdapter);
                }
            });
        } else {
            LoadDialog.dismiss(AddressProvinceActivity.this);
            noNetView.setVisibility(View.VISIBLE);
            mSuccessView.setVisibility(View.GONE);
            ToastUtil.show(this, getString(R.string.offline));
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        btn_request_net.setOnClickListener(this);
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
            case R.id.btn_request_net:
                getDataByNetStatus();
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
        AddressAllBean addressAllBean = new AddressAllBean();
        addressAllBean.setProvince(addressInfoBean);
        Intent intent = new Intent();
        intent.putExtra(ActionUtil.ACTION_ADDRESS_SELECT, addressAllBean);
        intent.setClass(AddressProvinceActivity.this, AddressCityActivity.class);
        startActivity(intent);
    }
}
