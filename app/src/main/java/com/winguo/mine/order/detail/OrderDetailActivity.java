package com.winguo.mine.order.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.NetworkUtils;
import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.mine.order.OrderHandler;
import com.winguo.mine.order.bean.OrderDetailArryBean;
import com.winguo.mine.order.bean.OrderDetailObjBean;
import com.winguo.mine.order.bean.ShopDetailBean;
import com.winguo.mine.order.detail.delivery.controller.DeliveryActivity;
import com.winguo.mine.order.bean.DeliveryRootBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @author hcpai
 * @desc 订单详情
 */
public class OrderDetailActivity extends BaseActivity2 {
    /**
     * 返回
     */
    private RelativeLayout back_rl;

    private RelativeLayout detail_title_rl;
    /**
     * 订单状态
     */
    private TextView order_detail_status_tv;

    private TextView order_detail_status_tv2;
    /**
     * 订单图标
     */
    private ImageView order_detail_iv;

    /**
     * 物流信息布局
     */
    private RelativeLayout order_detail_deliverinfo_rl;
    /**
     * 物流信息
     */
    private TextView order_detail_deliverinfo_tv;
    /**
     * 物流时间
     */
    private TextView order_deliver__time_tv;
    /**
     * 收件人
     */
    private TextView detail_receiver_tv;
    /**
     * 收件人号码
     */
    //private TextView detail_receiver_phone_tv;
    /**
     * 收件人地址
     */
    private TextView detail_receiver_address_tv;
    /**
     * 店铺名称
     */
    private TextView order_shop_name_tv;
    /**
     * 快递费
     */
    private TextView order_detail_delivery_charges_tv;
    /**
     * 优惠价格
     */
    private TextView order_detail_shop_privilege_tv;
    /**
     * 总价
     */
    private TextView order_detail_all_price_tv;
    /**
     * 联系卖家
     */
    private TextView order_detail_contact_seller_tv;
    /**
     * 订单编号
     */
    private TextView order_detail_order_number_tv;
    /**
     * 创建时间
     */
    private TextView order_detail_setup_time_tv;
    /**
     * 付款时间
     */
    private TextView order_detail_pay_time_tv;
    /**
     * 发货时间
     */
    private TextView order_detail_deliver_time_tv;
    /**
     * 收货时间
     */
    private TextView order_detail_receive_time_tv;
    /**
     * 评论时间
     */
    private TextView order_detail_evaluate_time_tv;
    /**
     * 订单列表的容器
     */
    private OrderDetailItemView order_detail_order_detail_item_view;

    //private Button order_detail_left;
    //private Button order_detail_right;
    /**
     * 店铺id
     */
    private int Mmid;
    private int mOid;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.order_detail_activity;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        CommonUtil.stateSetting(this, R.color.orange1);
        back_rl = (RelativeLayout) findViewById(R.id.back_rl);
        detail_title_rl = (RelativeLayout) findViewById(R.id.detail_title_rl);
        order_detail_status_tv = (TextView) findViewById(R.id.order_detail_status_tv);
        order_detail_iv = (ImageView) findViewById(R.id.order_detail_iv);
        order_detail_status_tv2 = (TextView) findViewById(R.id.order_detail_status_tv2);
        order_detail_deliverinfo_rl = (RelativeLayout) findViewById(R.id.order_detail_deliverinfo_rl);
        order_detail_deliverinfo_tv = (TextView) findViewById(R.id.order_detail_deliverinfo_tv);
        order_deliver__time_tv = (TextView) findViewById(R.id.order_deliver__time_tv);
        detail_receiver_tv = (TextView) findViewById(R.id.detail_receiver_tv);
        //detail_receiver_phone_tv = (TextView) findViewById(R.id.detail_receiver_phone_tv);
        detail_receiver_address_tv = (TextView) findViewById(R.id.detail_receiver_address_tv);
        order_shop_name_tv = (TextView) findViewById(R.id.order_shop_name_tv);
        order_detail_delivery_charges_tv = (TextView) findViewById(R.id.order_detail_delivery_charges_tv);
        order_detail_shop_privilege_tv = (TextView) findViewById(R.id.order_detail_shop_privilege_tv);
        order_detail_all_price_tv = (TextView) findViewById(R.id.order_detail_all_price_tv);
        order_detail_contact_seller_tv = (TextView) findViewById(R.id.order_detail_contact_seller_tv);
        order_detail_order_number_tv = (TextView) findViewById(R.id.order_detail_order_number_tv);
        order_detail_setup_time_tv = (TextView) findViewById(R.id.order_detail_setup_time_tv);
        order_detail_pay_time_tv = (TextView) findViewById(R.id.order_detail_pay_time_tv);
        order_detail_deliver_time_tv = (TextView) findViewById(R.id.order_detail_deliver_time_tv);
        order_detail_receive_time_tv = (TextView) findViewById(R.id.order_detail_receive_time_tv);
        order_detail_evaluate_time_tv = (TextView) findViewById(R.id.order_detail_evaluate_time_tv);
        //订单列表的容器
        order_detail_order_detail_item_view = (OrderDetailItemView) findViewById(R.id.order_detail_order_detail_item_view);
        //order_detail_left = (Button) findViewById(R.id.order_detail_left);
        //order_detail_right = (Button) findViewById(R.id.order_detail_right);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        OrderDetailAllBean orderDetailAllBean = (OrderDetailAllBean) getIntent().getExtras().get(ActionUtil.ACTION_ORDER_DETAIL);
        mOid = (int) getIntent().getExtras().get(ActionUtil.ACTION_ORDER_ID);
        order_detail_deliverinfo_tv.setText("加载中...");
        order_detail_order_detail_item_view.setData(orderDetailAllBean);
        assert orderDetailAllBean != null;
        //DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (orderDetailAllBean.getIsObject()) {
            OrderDetailObjBean.RootBean.DataBean dataObj = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData();
            Mmid = dataObj.getMid();
            //公共部分赋值
            order_shop_name_tv.setText(dataObj.getMname());
            detail_receiver_tv.setText(getString(R.string.order_detail_receiver, dataObj.getReceived_name()));
            //detail_receiver_phone_tv.setText(dataObj.getMobile() + "");
            String address = dataObj.getProvinceName() + dataObj.getCityName() + " " + dataObj.getAreaName() + " " + dataObj.getTownName() + " " + dataObj.getAddress();
            detail_receiver_address_tv.setText(address);
            String fact_charges = dataObj.getFact_charges();
            String deliveryFree;
            if (fact_charges.trim().equals("0")) {
                deliveryFree = "包邮";
            } else {
                deliveryFree = "¥ " + fact_charges;
            }
            order_detail_delivery_charges_tv.setText(deliveryFree);
            //快递优惠
            /**
             * 格式化运算
             */
            BigDecimal delivery_charges = new BigDecimal(dataObj.getDelivery_charges());
            BigDecimal fact_charge = new BigDecimal(dataObj.getFact_charges());
            double deliveryPrivilege = delivery_charges.subtract(fact_charge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double deliveryPrivilege = Double.valueOf(dataObj.getDelivery_charges()) - Double.valueOf(dataObj.getFact_charges());
            //商品优惠
            OrderDetailItemBean item = dataObj.getData().getItems();
            /**
             * 格式化运算
             */
            BigDecimal cost_unit_price2 = new BigDecimal(item.getCost_unit_price());
            BigDecimal unit_price2 = new BigDecimal(item.getUnit_price());
            BigDecimal quantity2 = new BigDecimal(item.getQuantity());
            BigDecimal subtract = cost_unit_price2.subtract(unit_price2);
            double goodsPrivilege = subtract.multiply(quantity2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double goodsPrivilege = (item.getCost_unit_price() - item.getUnit_price()) * item.getQuantity();
            String privilege;
            //满就减
            double full_sale = dataObj.getData().getItems().getFull_sale();
            /**
             * 格式化运算
             */
            BigDecimal deliveryPrivilege2 = new BigDecimal(Double.toString(deliveryPrivilege));
            BigDecimal goodsPrivilege2 = new BigDecimal(Double.toString(goodsPrivilege));
            BigDecimal full_sale2 = new BigDecimal(Double.toString(full_sale));
            double number = deliveryPrivilege2.add(goodsPrivilege2).add(full_sale2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double number = deliveryPrivilege + goodsPrivilege + full_sale;
            if (number == 0) {
                privilege = "无优惠";
            } else {
                //privilege = "¥ - " + CommonUtil.formatPoint(number) + "";
                privilege = "¥ - " + number;
            }
            order_detail_shop_privilege_tv.setText(privilege);
            /**
             * 格式化运算
             */
            BigDecimal total_price2 = new BigDecimal(dataObj.getTotal_price());
            BigDecimal fact_charges2 = new BigDecimal(dataObj.getFact_charges());
            double all_price = total_price2.add(fact_charges2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double all_price = Double.valueOf(dataObj.getTotal_price()) + Double.valueOf(dataObj.getFact_charges());
            //CommonUtil.formatPoint(v)
            order_detail_all_price_tv.setText("¥ " + all_price);
            order_detail_order_number_tv.setText(String.format(getString(R.string.order_detail_order_number_tv), dataObj.getNo()));
            order_detail_setup_time_tv.setText(String.format(getString(R.string.order_detail_setup_time), dataObj.getOrder_date()));
            //根据订单状态的不同赋值
            initStatus(dataObj.getStatus(), orderDetailAllBean);
        } else {
            OrderDetailArryBean.RootBean.DataBean dataArry = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData();
            Mmid = dataArry.getMid();
            //公共部分赋值
            order_shop_name_tv.setText(dataArry.getMname());
            detail_receiver_tv.setText(dataArry.getReceived_name());
            //detail_receiver_phone_tv.setText(dataArry.getMobile() + "");
            detail_receiver_address_tv.setText(dataArry.getProvinceName() + " " + dataArry.getCityName() + " " + dataArry.getAreaName());
            double fact_charges = Double.valueOf(dataArry.getFact_charges());
            String deliveryFree;
            if (fact_charges == 0) {
                deliveryFree = "包邮";
            } else {
                deliveryFree = "¥ " + fact_charges;
            }
            order_detail_delivery_charges_tv.setText(deliveryFree);
            //快递优惠
            /**
             * 格式化运算
             */
            BigDecimal delivery_charges2 = new BigDecimal(dataArry.getDelivery_charges());
            BigDecimal fact_charges2 = new BigDecimal(dataArry.getFact_charges());
            double deliveryPrivilege = delivery_charges2.subtract(fact_charges2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double deliveryPrivilege = Double.valueOf(dataArry.getDelivery_charges()) - Double.valueOf(dataArry.getFact_charges());
            List<OrderDetailItemBean> items = dataArry.getData().getItems();
            //商品优惠
            double goodsPrivilege = 0;
            //满就减
            //double full_sale = 0;
            BigDecimal full_sale2 = new BigDecimal("0");
            //总价格
            for (int i = 0; i < items.size(); i++) {
                OrderDetailItemBean orderDetailItemBean = items.get(i);
                full_sale2.add(new BigDecimal(orderDetailItemBean.getFull_sale()));
                //full_sale += orderDetailItemBean.getFull_sale();
                BigDecimal cost_unit_price2 = new BigDecimal(orderDetailItemBean.getCost_unit_price());
                BigDecimal unit_price2 = new BigDecimal(orderDetailItemBean.getUnit_price());
                BigDecimal quantity2 = new BigDecimal(orderDetailItemBean.getQuantity());
                BigDecimal subtract = cost_unit_price2.subtract(unit_price2);
                goodsPrivilege = subtract.multiply(quantity2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //goodsPrivilege += (orderDetailItemBean.getCost_unit_price() - orderDetailItemBean.getUnit_price()) * orderDetailItemBean.getQuantity();

            }
            String privilege;
            BigDecimal deliveryPrivilege2 = new BigDecimal(deliveryPrivilege);
            BigDecimal goodsPrivilege2 = new BigDecimal(goodsPrivilege);
            double number = deliveryPrivilege2.add(goodsPrivilege2).add(full_sale2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double number = deliveryPrivilege + goodsPrivilege + full_sale2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (number == 0) {
                privilege = "无优惠";
            } else {
                privilege = "¥ - " + CommonUtil.formatPoint(number);
            }
            order_detail_shop_privilege_tv.setText(privilege);
            /**
             * 格式化运算
             */
            BigDecimal total_price2 = new BigDecimal(dataArry.getTotal_price());
            double all_price = total_price2.add(fact_charges2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //double all_price = Double.valueOf(dataArry.getTotal_price()) + Double.valueOf(dataArry.getFact_charges());
            order_detail_all_price_tv.setText("¥ " + all_price);
            order_detail_order_number_tv.setText(String.format(getString(R.string.order_detail_order_number_tv), dataArry.getNo()));
            order_detail_setup_time_tv.setText(String.format(getString(R.string.order_detail_setup_time), dataArry.getOrder_date()));
            initStatus(dataArry.getStatus(), orderDetailAllBean);
        }
    }

    /**
     * 根据订单状态进行赋值
     *
     * @param status 订单状态：1.未付款，2.待发货，3.已发货，4.取消，5.已签收
     */
    private void initStatus(String status, OrderDetailAllBean orderDetailAllBean) {
        switch (status) {
            case "待付款":
                LoadDialog.show(this);
                //
                order_detail_status_tv.setText(getString(R.string.order_detail_status_wait_pay));
                if (NetworkUtils.isConnectNet(this)) {
                    OrderHandler.searchOrderDetail(this, handler, mOid);
                } else {
                    order_detail_status_tv2.setText("");
                }
                // order_detail_status_tv2.setText(getString(R.string.order_detail_status_wait_pay2));
                order_detail_deliverinfo_rl.setVisibility(View.GONE);
                //order_detail_left.setText(R.string.order_wait_pay);
                //order_detail_right.setText(R.string.order_pay);
                break;
            case "待出货":
                detail_title_rl.setBackgroundColor(getResources().getColor(R.color.orange2));
                order_detail_iv.setImageDrawable(getResources().getDrawable(R.drawable.wait_deliver));
                order_detail_status_tv.setText(getString(R.string.order_detail_status_wait_deliver));
                order_detail_status_tv2.setText(getString(R.string.order_detail_status_wait_deliver2));
                order_detail_deliverinfo_rl.setVisibility(View.GONE);
                //order_detail_left.setVisibility(View.INVISIBLE);
                //order_detail_right.setText(R.string.order_remind_seller);
                //order_detail_right.setOnClickListener(new View.OnClickListener() {
                //@Override
                //public void onClick(View view) {
                //ToastUtil.show(OrderDetailActivity.this, "提醒卖家成功!");
                //}
                //});
                String pay_datetime;
                if (orderDetailAllBean.getIsObject()) {
                    pay_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getPay_datetime();
                } else {
                    pay_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getPay_datetime();
                }
                //显示付款时间
                order_detail_pay_time_tv.setText(String.format(getString(R.string.order_detail_pay_time), pay_datetime));
                order_detail_pay_time_tv.setVisibility(View.VISIBLE);
                order_detail_deliver_time_tv.setVisibility(View.GONE);
                order_detail_receive_time_tv.setVisibility(View.GONE);
                break;
            case "待收货":
                detail_title_rl.setBackgroundColor(getResources().getColor(R.color.orange2));
                order_detail_iv.setImageDrawable(getResources().getDrawable(R.drawable.wait_receive));
                order_detail_status_tv.setText(getString(R.string.order_detail_status_wait_receive));
                order_detail_status_tv2.setText(getString(R.string.order_detail_status_wait_receive2));
                order_detail_deliverinfo_rl.setVisibility(View.VISIBLE);
                //order_detail_left.setText(R.string.order_look_delivery);
                //order_detail_right.setText(R.string.order_confirm_receive);
                //显示物流
                showDeliverInfo(orderDetailAllBean);
                //显示发货时间
                String maker_deliveryDate;
                if (orderDetailAllBean.getIsObject()) {
                    pay_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getPay_datetime();
                    maker_deliveryDate = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getMaker_deliverydate();
                } else {
                    pay_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getPay_datetime();
                    maker_deliveryDate = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getMaker_deliverydate();
                }
                order_detail_pay_time_tv.setText(String.format(getString(R.string.order_detail_pay_time), pay_datetime));
                order_detail_pay_time_tv.setVisibility(View.VISIBLE);
                order_detail_deliver_time_tv.setText(String.format(getString(R.string.order_detail_deliver_time), maker_deliveryDate));
                order_detail_deliver_time_tv.setVisibility(View.VISIBLE);
                order_detail_receive_time_tv.setVisibility(View.GONE);
                break;
            case "已取消":
                detail_title_rl.setBackgroundColor(getResources().getColor(R.color.orange2));
                order_detail_iv.setImageDrawable(getResources().getDrawable(R.drawable.order_status_cancel));
                order_detail_status_tv.setText(getString(R.string.order_detail_status_cancel));
                order_detail_status_tv2.setText(getString(R.string.order_detail_status_cancel2));
                order_detail_deliverinfo_rl.setVisibility(View.GONE);
                //order_detail_left.setVisibility(View.INVISIBLE);
                //order_detail_right.setText(R.string.order_pay_chongxin_);
                break;
            case "待评价":
                detail_title_rl.setBackgroundColor(getResources().getColor(R.color.orange2));
                order_detail_iv.setImageDrawable(getResources().getDrawable(R.drawable.wait_deliver));
                order_detail_status_tv.setText(getString(R.string.order_detail_status_wait_comment));
                order_detail_status_tv2.setText(getString(R.string.order_detail_status_wait_comment2));
                order_detail_deliverinfo_rl.setVisibility(View.VISIBLE);
                //order_detail_left.setText(R.string.order_pay_zaici);
                //order_detail_right.setText(R.string.order_comment);
                //显示物流
                showDeliverInfo(orderDetailAllBean);
                //显示确认收货时间
                String confirmReceive_datetime;
                if (orderDetailAllBean.getIsObject()) {
                    pay_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getPay_datetime();
                    maker_deliveryDate = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getMaker_deliverydate();
                    confirmReceive_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getConfirmreceive_datetime();
                } else {
                    pay_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getPay_datetime();
                    maker_deliveryDate = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getMaker_deliverydate();
                    confirmReceive_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getConfirmreceive_datetime();
                }
                order_detail_pay_time_tv.setText(String.format(getString(R.string.order_detail_pay_time), pay_datetime));
                order_detail_pay_time_tv.setVisibility(View.VISIBLE);
                order_detail_deliver_time_tv.setText(String.format(getString(R.string.order_detail_deliver_time), maker_deliveryDate));
                order_detail_deliver_time_tv.setVisibility(View.VISIBLE);
                order_detail_receive_time_tv.setText(String.format(getString(R.string.order_detail_receive_time), confirmReceive_datetime));
                order_detail_receive_time_tv.setVisibility(View.VISIBLE);
                break;
            case "已完成":
                String evaluate_datetime;
                detail_title_rl.setBackgroundColor(getResources().getColor(R.color.orange2));
                order_detail_iv.setImageDrawable(getResources().getDrawable(R.drawable.wait_deliver));
                order_detail_status_tv.setText(getString(R.string.order_detail_status_wait_finish));
                order_detail_status_tv2.setText(getString(R.string.order_detail_status_wait_finish2));
                order_detail_deliverinfo_rl.setVisibility(View.VISIBLE);
                //显示物流
                showDeliverInfo(orderDetailAllBean);
                if (orderDetailAllBean.getIsObject()) {
                    pay_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getPay_datetime();
                    maker_deliveryDate = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getMaker_deliverydate();
                    confirmReceive_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getConfirmreceive_datetime();
                    evaluate_datetime = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getEvaluate_datetime();
                } else {
                    pay_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getPay_datetime();
                    maker_deliveryDate = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getMaker_deliverydate();
                    confirmReceive_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getConfirmreceive_datetime();
                    evaluate_datetime = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getEvaluate_datetime();
                }
                order_detail_pay_time_tv.setText(String.format(getString(R.string.order_detail_pay_time), pay_datetime));
                order_detail_pay_time_tv.setVisibility(View.VISIBLE);
                order_detail_deliver_time_tv.setText(String.format(getString(R.string.order_detail_deliver_time), maker_deliveryDate));
                order_detail_deliver_time_tv.setVisibility(View.VISIBLE);
                order_detail_receive_time_tv.setText(String.format(getString(R.string.order_detail_receive_time), confirmReceive_datetime));
                order_detail_receive_time_tv.setVisibility(View.VISIBLE);
                order_detail_evaluate_time_tv.setText(String.format(getString(R.string.order_detail_evaluate_time), evaluate_datetime));
                order_detail_evaluate_time_tv.setVisibility(View.VISIBLE);
                break;

        }
    }

    /**
     * 显示物流
     */
    private void showDeliverInfo(OrderDetailAllBean orderDetailAllBean) {
        //显示物流
        final DeliveryRootBean deliveryRootBean;
        if (orderDetailAllBean.getIsObject()) {
            deliveryRootBean = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getDeliveryRootBean();
        } else {
            deliveryRootBean = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getDeliveryRootBean();
        }
        if (deliveryRootBean != null) {
            int has_msg = deliveryRootBean.getHas_msg();
            DeliveryRootBean.ItemBean itemBean = deliveryRootBean.getItem().get(0);
            if (has_msg == 0) {
                //无详细信息时:
                order_detail_deliverinfo_tv.setText(itemBean.getContext());
                order_deliver__time_tv.setText(itemBean.getTime());
            } else {
                //有详细物流时:
                order_detail_deliverinfo_tv.setText(itemBean.getContext());
                order_deliver__time_tv.setText(itemBean.getTime());
            }
        }
        order_detail_deliverinfo_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, DeliveryActivity.class);
                intent.putExtra(ActionUtil.ACTION_DELIVERY, deliveryRootBean);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back_rl.setOnClickListener(this);
        order_detail_contact_seller_tv.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_GETSHOPDETAIL:
                LoadDialog.dismiss(this);
                ShopDetailBean shopDetailBean = (ShopDetailBean) msg.obj;
                if (shopDetailBean != null) {
                    String mobile = shopDetailBean.getMessage().getMobile();
                    if (mobile != null) {
                        Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopDetailBean.getMessage().getMobile()));
                        startActivity(intentCall);
                    }
                } else {
                    ToastUtil.show(this, getString(R.string.timeout));
                }
                break;
            case RequestCodeConstant.REQUEST_SEARCHORDER_DETAIL:
                LoadDialog.dismiss(this);
                OrderDetailAllBean orderDetailAllBean = (OrderDetailAllBean) msg.obj;
                if (orderDetailAllBean == null) {
                    Toast.makeText(this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                    return;
                }
                String order_close_date;
                if (orderDetailAllBean.getIsObject()) {
                    order_close_date = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getOrder_close_date();
                } else {
                    order_close_date = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getOrder_close_date();
                }
                if (Long.valueOf(order_close_date) < 0) {
                    order_detail_status_tv.setText(getString(R.string.order_detail_status_cancel));
                    order_detail_status_tv2.setText(getString(R.string.order_detail_status_cancel2));
                } else {
                    new CountDownTimer(Long.valueOf(order_close_date) + (16 * 60 * 60 * 1000), 1000) {
                        @Override
                        public void onTick(long l) {
                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
                            String hms = formatter.format(l);
                            order_detail_status_tv.setText(getString(R.string.order_detail_status_wait_pay));
                            order_detail_status_tv2.setText(hms + "后自动关闭");
                        }

                        @Override
                        public void onFinish() {
                            order_detail_status_tv.setText(getString(R.string.order_detail_status_cancel));
                            order_detail_status_tv2.setText(getString(R.string.order_detail_status_cancel2));
                        }
                    }.start();
                }
                break;
        }
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
            case R.id.order_detail_contact_seller_tv:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    LoadDialog.show(this);
                    OrderHandler.getShopDetail(handler, this, Mmid);
                } else {
                    ToastUtil.show(this, getString(R.string.offline));
                }
                break;
        }
    }
}
