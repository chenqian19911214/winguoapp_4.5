package com.winguo.mine.order.detail.delivery.controller;

import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.mine.order.bean.DeliveryRootBean;
import com.winguo.mine.order.detail.delivery.model.LogisticsAdapter;
import com.winguo.mine.order.detail.delivery.view.CustomNodeListView;
import com.winguo.utils.ActionUtil;

import java.util.List;


/**
 * @author hcpai
 * @desc 物流信息
 */
public class DeliveryActivity extends BaseActivity2 {

    private RelativeLayout back_rl;
    private TextView deliver_company_name_tv;
    private TextView deliver_waybill_number_tv;
    private CustomNodeListView mListView;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_delivery;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        back_rl = (RelativeLayout) findViewById(R.id.back_rl);
        deliver_company_name_tv = (TextView) findViewById(R.id.deliver_company_name_tv);
        deliver_waybill_number_tv = (TextView) findViewById(R.id.deliver_waybill_number_tv);
        mListView = (CustomNodeListView) findViewById(R.id.deliver_listview);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        DeliveryRootBean deliveryRootBean = (DeliveryRootBean) getIntent().getExtras().get(ActionUtil.ACTION_DELIVERY);
        assert deliveryRootBean != null;
        List<DeliveryRootBean.ItemBean> items = deliveryRootBean.getItem();
        DeliveryRootBean.ItemBean itemBean = items.get(items.size() - 1);
        deliver_company_name_tv.setText(itemBean.getContext());
        deliver_waybill_number_tv.setText(itemBean.getTime());
        items.remove(items.get(items.size() - 1));
        LogisticsAdapter adapter = new LogisticsAdapter(items, this);
        mListView.setAdapter(adapter);
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back_rl.setOnClickListener(this);
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
            case R.id.back_rl:
                finish();
                break;
        }
    }
}
