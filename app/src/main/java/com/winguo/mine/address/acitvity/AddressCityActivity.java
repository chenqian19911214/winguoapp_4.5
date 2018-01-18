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
import com.winguo.mine.address.bean.AddressAreaBean;
import com.winguo.mine.address.bean.AddressCityAllBean;
import com.winguo.mine.address.bean.AddressInfoBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hcpai
 * @desc 选择市
 */
public class AddressCityActivity extends BaseActivity2 implements AddressAdapter.IAddressListener {
    private RelativeLayout back;
    private View mSuccessView;
    private ListView mListView;
    private AddressAllBean mAddressAllBean;
    private TextView address_title_tv;
    private boolean mIsObj = false;
    private View noNetView;
    private Button btn_request_net;
    private AddressInfoBean mProvince;

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
        mAddressAllBean = (AddressAllBean) getIntent().getExtras().get(ActionUtil.ACTION_ADDRESS_SELECT);
        assert mAddressAllBean != null;
        mProvince = mAddressAllBean.getProvince();
        address_title_tv.setText(mProvince.getName() + "省");
        getDataByNetStatue(mProvince);
    }

    /**
     * 根据网络状态加载数据
     *
     * @param province
     */
    private void getDataByNetStatue(AddressInfoBean province) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            AddressManageHandle.getCity(province.getCode(), new IAddressResult<AddressCityAllBean>() {
                private AddressAdapter mAdapter;

                @Override
                public void getResult(AddressCityAllBean addressCityAllBean) {
                    LoadDialog.dismiss(AddressCityActivity.this);
                    mSuccessView.setVisibility(View.VISIBLE);
                    if (addressCityAllBean == null) {
                        ToastUtil.show(AddressCityActivity.this, getString(R.string.timeout));
                        return;
                    }
                    ArrayList<AddressInfoBean> list = new ArrayList<>();
                    if (addressCityAllBean.isObj()) {
                        mIsObj = true;
                        list.clear();
                        AddressInfoBean item = addressCityAllBean.getAddressCityObjBean().getCity().getItem();
                        list.add(item);
                    } else {
                        mIsObj = false;
                        list.clear();
                        List<AddressInfoBean> items = addressCityAllBean.getAddressCityArrBean().getCity().getItem();
                        list.addAll(items);
                    }
                    mAdapter = new AddressAdapter(AddressCityActivity.this, list);
                    mAdapter.setListen(AddressCityActivity.this);
                    mListView.setAdapter(mAdapter);
                }
            });
        } else {
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
                getDataByNetStatue(mProvince);
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
        mAddressAllBean.setCity(addressInfoBean);
        if (mIsObj) {
            //一个城市时,直接跳转到编辑界面
            startToEditActivity();
        } else {
            //多个城市时,跳转到县/区
            if (NetWorkUtil.isNetworkAvailable(this)) {
                AddressManageHandle.getArea(addressInfoBean.getCode(), new IAddressResult<AddressAreaBean>() {
                    @Override
                    public void getResult(AddressAreaBean addressAreaBean) {
                        if (addressAreaBean == null) {
                            ToastUtil.show(AddressCityActivity.this, getString(R.string.timeout));
                        } else if (addressAreaBean.getArea() == null) {
                            //没有县时
                            startToEditActivity();
                        } else {
                            //有县
                            mAddressAllBean.setAreaBean(addressAreaBean);
                            Intent intent = new Intent();
                            intent.putExtra(ActionUtil.ACTION_ADDRESS_SELECT, mAddressAllBean);
                            intent.setClass(AddressCityActivity.this, AddressAreaActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            } else {
                ToastUtil.show(this, getString(R.string.offline));
            }
        }
    }

    /**
     * 跳转到地址编辑界面
     */
    private void startToEditActivity() {
        Intent intent1 = new Intent(ActionUtil.ACTION_ADDRESS_SELECT);
        intent1.putExtra(ActionUtil.ACTION_ADDRESS_SELECT, mAddressAllBean);
        sendBroadcast(intent1);
        //跳转
        Intent intent = new Intent();
        intent.setClass(AddressCityActivity.this, AddressEditActivity.class);
        startActivity(intent);
    }
}
