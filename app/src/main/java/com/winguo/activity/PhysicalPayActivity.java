package com.winguo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.pay.controller.physicalstore.PhysicalPayController;
import com.winguo.pay.modle.bean.PhysicalConfirmOderBean;
import com.winguo.pay.modle.bean.PhysicalSubmitOrderBean;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.pay.view.IPhysicalComfirmOrderView;
import com.winguo.pay.view.PayExpandableListView;
import com.winguo.pay.view.PhysicalPayExpandableAdapter;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.ConfirmDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/6.
 * 实体店 订单支付
 */

public class PhysicalPayActivity extends BaseTitleActivity implements IPhysicalComfirmOrderView, View.OnClickListener {
    private TextView tv_consignee_info1;
    private TextView tv_consignee_info2;
    private LinearLayout ll_add_consignee_info;
    private PayExpandableListView pay_ex_list_view;
    private TextView pay_goods_count;
    private TextView pay_total_price;
    private TextView tv_pay_btn;
    private View ll_order_view;
    private FrameLayout fl_order_container;
    private int totalCount;
    private double totalPrice;
    //    private FrameLayout order_back_btn;
    //是否填写了地址信息
    private boolean isAddAddress = false;

    //要下单的IDS
    private String cart_ids;
    private ItemBean mItemBean;
    //    private UpdatePriceReceiver updatePriceReceiver;
    private View noView;
    private TextView tv_no_contact;
    private SharedPreferences sharedPreferences;
    private PhysicalPayController physicalPayController;
    private PhysicalPayExpandableAdapter physicalPayExpandableAdapter;
    private LinearLayout ll_expandable_container;
    private String physical_user_name;
    private String physical_user_phone;
    private PhysicalConfirmOderBean physicalConfirmOderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setBackBtn();
        initDatas();
        initView();
        initListener();
    }

    private void initDatas() {

        sharedPreferences = getSharedPreferences("physical_pay_userInfo", MODE_PRIVATE);
        //获取传递过来的数据
        getIntentData();
        //注册广播
//        registerBoradcastReceiver();
    }

    /**
     * 获取传递过来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        cart_ids = intent.getStringExtra("cart_ids");
    }

    private void initView() {

//        order_back_btn = (FrameLayout) findViewById(R.id.order_back_btn);
        fl_order_container = (FrameLayout) findViewById(R.id.fl_order_container);
        noView = View.inflate(PhysicalPayActivity.this, R.layout.no_net, null);
        TextView no_net_tv = (TextView) noView.findViewById(R.id.no_net_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        fl_order_container.addView(noView);
        noView.setVisibility(View.GONE);
        if (!NetWorkUtil.isNetworkAvailable(PhysicalPayActivity.this) && !NetWorkUtil.isWifiConnected(PhysicalPayActivity.this)) {
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            return;
        }
        //显示订单信息
        ll_order_view = View.inflate(PhysicalPayActivity.this, R.layout.physical_pay_view, null);
        //收货人信息
        tv_consignee_info1 = (TextView) ll_order_view.findViewById(R.id.tv_consignee_info1);
        tv_no_contact = (TextView) ll_order_view.findViewById(R.id.tv_no_contact);
        tv_consignee_info2 = (TextView) ll_order_view.findViewById(R.id.tv_consignee_info2);
        //添加或修改收货人信息
        ll_add_consignee_info = (LinearLayout) ll_order_view.findViewById(R.id.ll_add_consignee_info);
        //支付商品信息
        ll_expandable_container = (LinearLayout) ll_order_view.findViewById(R.id.ll_expandable_container);
        pay_ex_list_view = (PayExpandableListView) ll_order_view.findViewById(R.id.pay_ex_list_view);
        //底部字符属性
        pay_goods_count = (TextView) ll_order_view.findViewById(R.id.pay_goods_count);
        pay_total_price = (TextView) ll_order_view.findViewById(R.id.pay_total_price);
        //提交订单按钮
        tv_pay_btn = (TextView) ll_order_view.findViewById(R.id.tv_pay_btn);
        fl_order_container.addView(ll_order_view);
        ll_order_view.setVisibility(View.GONE);
        //网络传递过来的数据
        userData();

    }

    /**
     * 请求网络用户地址信息
     */
    private void userData() {
        LoadDialog.show(PhysicalPayActivity.this);
        physicalPayController = new PhysicalPayController(this);
        physicalPayController.getPhysicalConfirmOrder(this, cart_ids);
    }

    @Override
    public void getPhysicalConfirmOderData(PhysicalConfirmOderBean physicalConfirmOderBean) {
        LoadDialog.dismiss(PhysicalPayActivity.this);
        if (physicalConfirmOderBean == null) {
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            ll_order_view.setVisibility(View.GONE);
            return;
        }
        this.physicalConfirmOderBean = physicalConfirmOderBean;

        //判断是否有用户信息
        physical_user_name = sharedPreferences.getString("physical_user_name", "");
        physical_user_phone = sharedPreferences.getString("physical_user_phone", "");
        if (TextUtils.isEmpty(physical_user_name) || TextUtils.isEmpty(physical_user_phone)) {
            tv_consignee_info1.setVisibility(View.GONE);
            tv_consignee_info2.setVisibility(View.GONE);
            tv_no_contact.setVisibility(View.VISIBLE);
            isAddAddress = false;
        } else {
            tv_consignee_info1.setVisibility(View.VISIBLE);
            tv_consignee_info2.setVisibility(View.VISIBLE);
            tv_no_contact.setVisibility(View.GONE);
            tv_consignee_info1.setText(String.format(getResources().getString(R.string.physical_username),physical_user_name));
            tv_consignee_info2.setText(String.format(getResources().getString(R.string.physical_userpphone),physical_user_phone));
            isAddAddress = true;
        }
        obtainData(physicalConfirmOderBean);

    }

    //处理数据
    private void obtainData(PhysicalConfirmOderBean physicalConfirmOderBean) {
        //设置适配器
        physicalPayExpandableAdapter = new PhysicalPayExpandableAdapter(this, physicalConfirmOderBean.result.list);
        pay_ex_list_view.setAdapter(physicalPayExpandableAdapter);
        openGroup();
        pay_goods_count.setText("共 " + physicalConfirmOderBean.result.total_qty + " 件");
        // 显示总价格
        pay_total_price.setText(String.valueOf(physicalConfirmOderBean.result.total_price));
        noView.setVisibility(View.GONE);
        ll_order_view.setVisibility(View.VISIBLE);
    }

    /**
     * 默认group是打开的
     */
    private void openGroup() {
        for (int i = 0; i < physicalPayExpandableAdapter.getGroupCount(); i++) {
            pay_ex_list_view.expandGroup(i);
        }
        pay_ex_list_view.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    //接受打开页面返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Constants.PHYSICAL_RESULT_CODE) {
                physical_user_name = data.getStringExtra("username");
                physical_user_phone = data.getStringExtra("userphone");
                tv_consignee_info1.setVisibility(View.VISIBLE);
                tv_consignee_info2.setVisibility(View.VISIBLE);
                tv_no_contact.setVisibility(View.GONE);
                tv_consignee_info1.setText(String.format(getResources().getString(R.string.physical_username),physical_user_name));
                tv_consignee_info2.setText(String.format(getResources().getString(R.string.physical_userpphone),physical_user_phone));
                isAddAddress = true;
            }
        }
    }

    private void initListener() {
        ll_add_consignee_info.setOnClickListener(this);
        tv_pay_btn.setOnClickListener(this);
        ll_expandable_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_expandable_container.setFocusable(true);
                ll_expandable_container.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_consignee_info:
                //跳转到选择地址页面
                Intent adressIntent = new Intent(PhysicalPayActivity.this, Physical_Add_UserInfo_Activity.class);
                startActivityForResult(adressIntent, Constants.PHYSICAL_REQUEST_CODE);
                break;
            case R.id.tv_pay_btn:
                //判断有没有网络
                if (!NetWorkUtil.isNetworkAvailable(PhysicalPayActivity.this) && !NetWorkUtil.isWifiConnected(PhysicalPayActivity.this)) {
                    final ConfirmDialog confirmDialog = new ConfirmDialog(PhysicalPayActivity.this, false);
                    confirmDialog.setDialogTitle(getResources().getString(R.string.confirm_order_dialog_title));
                    confirmDialog.setDialogContent(getResources().getString(R.string.no_net_dialog_title));
                    confirmDialog.setPositiveButton(getResources().getString(R.string.dialog_positive_btn_text), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialog.dismiss();
                            finish();
                        }
                    });

                }

                //地址信息有和配送信息有
                if (!isAddAddress) {
                    ToastUtil.showToast(PhysicalPayActivity.this, getString(R.string.no_address_reminder));
                    return;
                }
                //跳转到支付页面,并提交服务器
                getConfirmOrderData();
                break;
        }
    }

    private void getConfirmOrderData() {
        String str = "";//商家ID,产品ID,产品属性ID,数量,商品单价)多条记录中间用（|||）隔开

        List<PhysicalConfirmOderBean.ResultBean.ListBean> listBeen = physicalConfirmOderBean.result.list;
        for (int i = 0; i < listBeen.size(); i++) {
            String m_maker_id = listBeen.get(i).shop.m_maker_id;
            List<PhysicalConfirmOderBean.ResultBean.ListBean.ItemBean> items = listBeen.get(i).item;
            for (int j = 0; j < items.size(); j++) {
                if (i == listBeen.size() - 1 && j == items.size() - 1) {
                    str +=  m_maker_id + ","+items.get(j).m_item_id + "," + items.get(j).m_sku_id + "," + items.get(j).m_entity_cart_qty + "," + items.get(j).m_sku_price;
                } else {
                    str += m_maker_id + ","+ items.get(j).m_item_id + "," + items.get(j).m_sku_id + "," + items.get(j).m_entity_cart_qty + "," + items.get(j).m_sku_price + "|||";
                }
            }

        }

        CommonUtil.printI("下单的参数str", str);
        //请求网络,根据返回的数据决定跳转
        //判断网络状态
        if (NetWorkUtil.noHaveNet(PhysicalPayActivity.this)) {
            return;
        }
//        LoadDialog.show(PayActivity.this, false);
        if (physicalPayController != null) {
            LoadDialog.show(PhysicalPayActivity.this, true);
            physicalPayController.getPhysicalSubmitOrder(PhysicalPayActivity.this, physical_user_name, physical_user_phone, cart_ids, str);
        }
    }

    //下订单返回的数据
    @Override
    public void getPhysicalSubmitOderData(PhysicalSubmitOrderBean physicalSubmitOrderBean) {
        LoadDialog.dismiss(PhysicalPayActivity.this);
        if (physicalSubmitOrderBean == null) {
            //没有网络
            ll_order_view.setVisibility(View.GONE);
            noView.setVisibility(View.VISIBLE);
            return;
        }
        ArrayList<CommonBean> commonBeen = setData();
        //下单成功发送广播
        Intent receiverIntent = new Intent();
        receiverIntent.setAction("com.winguo.physicalconfirmpay.success");
        sendBroadcast(receiverIntent);
        //下单成功,跳转
        Intent intent = new Intent(PhysicalPayActivity.this, ConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("commonBeen", commonBeen);
        intent.putExtras(bundle);
        intent.putExtra("isphysical",1);
        intent.putExtra("name",physical_user_name);
        intent.putExtra("phone",physical_user_phone);
        intent.putExtra("price",physicalConfirmOderBean.result.total_price);
        intent.putExtra("orderId", physicalSubmitOrderBean.result.merge_order_no);
        startActivity(intent);
        finish();
    }

    /**
     * 封装数据
     */
    private ArrayList<CommonBean> setData() {
        ArrayList<CommonBean> commonBeans = new ArrayList<>();
        if (physicalConfirmOderBean != null) {
            List<PhysicalConfirmOderBean.ResultBean.ListBean> lists = physicalConfirmOderBean.result.list;
            for (PhysicalConfirmOderBean.ResultBean.ListBean listBean : lists) {
                List<PhysicalConfirmOderBean.ResultBean.ListBean.ItemBean> items = listBean.item;
                for (PhysicalConfirmOderBean.ResultBean.ListBean.ItemBean itemBean : items) {
                    CommonBean commonBean = new CommonBean();
                    commonBean.content = itemBean.m_item_list_img;
                    commonBean.name = itemBean.m_item_name;
                    commonBean.num = Integer.valueOf(itemBean.m_entity_cart_qty);
                    commonBeans.add(commonBean);
                }
            }
        }
        return commonBeans;
    }
}
