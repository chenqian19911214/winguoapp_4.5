package com.winguo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.BalanceAndCash;
import com.winguo.mine.address.acitvity.AddressManageActivity;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.pay.controller.store.ConfirmOrderController;
import com.winguo.pay.controller.store.ExpressMethodController;
import com.winguo.pay.controller.store.SeekUserInfoController;
import com.winguo.pay.modle.bean.AddressInfoBean;

import com.winguo.pay.modle.store.ItemBean;
import com.winguo.pay.modle.bean.ConfirmOrderBean;
import com.winguo.pay.modle.bean.DataBean;
import com.winguo.pay.modle.bean.GoodsItemsBean;
import com.winguo.pay.modle.bean.OrderResultBean;
import com.winguo.pay.modle.bean.ShopInfoBean;
import com.winguo.pay.modle.bean.ExpressMethodBean;
import com.winguo.pay.modle.store.selfbean.SelfGoodsItem;
import com.winguo.pay.modle.bean.SelfOrderBean;
import com.winguo.pay.modle.store.selfbean.SelfShopInfo;
import com.winguo.pay.view.IConfirmOrderView;
import com.winguo.pay.view.IExpressageView;
import com.winguo.pay.view.ISeekUserInfoView;
import com.winguo.pay.view.PayExpandableAdapter;
import com.winguo.pay.view.PayExpandableListView;
import com.winguo.personalcenter.wallet.control.MyWalletControl;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.Base64;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;
import com.winguo.view.ConfirmDialog;
import com.winguo.view.CustomDialog2;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/6.
 * 商品下单页面
 */

public class PayActivity extends BaseTitleActivity implements WinguoAcccountManagerUtils.IBalanceAndCashCallback,ISeekUserInfoView, IExpressageView, IConfirmOrderView, PayExpandableAdapter.OnClickPropertyInterface ,View.OnClickListener{
    private TextView tv_consignee_info1;
    private TextView tv_consignee_info2;
    private ImageView iv_add_consignee_info;
    private CheckBox tv_order_sales_promotion_cb;
    private TextView tv_order_sales_method_cash;
    private TextView pay_favorable_price;
    private TextView tv_consignee_info3;
    private RelativeLayout ll_add_consignee_info;
    private PayExpandableListView pay_ex_list_view;
    private TextView pay_goods_count;
    private TextView pay_total_price;
    private TextView tv_pay_btn;
    private View ll_order_view;
    private FrameLayout fl_order_container;
    private PayExpandableAdapter payExpandableAdapter;
    private int totalCount;
    private double totalPrice;
//    private FrameLayout order_back_btn;
    //是否填写了地址信息
    private boolean isAddAddress = false;

    //商品sku_ids
    private String sku_ids;
    //是否立即购买
    private int is_prompt;
    //收件人信息
    private int recid;

    private SeekUserInfoController seekUserInfoController;
    private SelfOrderBean confirmOrderBean;
    private ItemBean mItemBean;
    private LinearLayout ll_expandable_container;
    private UpdatePriceReceiver updatePriceReceiver;
    private View noView;
    private String amount;
    private double totalCash;
    private BalanceAndCash cash;
    private MyWalletControl myWalletControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setBackBtn();
        initView();
        initDatas();
        initListener();
    }

    private void initView() {

//        order_back_btn = (FrameLayout) findViewById(R.id.order_back_btn);
        fl_order_container = (FrameLayout) findViewById(R.id.fl_order_container);
        noView = View.inflate(PayActivity.this, R.layout.no_net, null);
        TextView no_net_tv = (TextView) noView.findViewById(R.id.no_net_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        fl_order_container.addView(noView);
        noView.setVisibility(View.GONE);
        if (!NetWorkUtil.isNetworkAvailable(PayActivity.this) && !NetWorkUtil.isWifiConnected(PayActivity.this)) {
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            return;
        }
        //显示订单信息
        ll_order_view = View.inflate(PayActivity.this, R.layout.pay_view, null);
        //收货人信息
        tv_consignee_info1 = (TextView) ll_order_view.findViewById(R.id.tv_consignee_info1);
        tv_consignee_info2 = (TextView) ll_order_view.findViewById(R.id.tv_consignee_info2);
        tv_consignee_info3 = (TextView) ll_order_view.findViewById(R.id.tv_consignee_info3);
        //添加或修改收货人信息
        ll_add_consignee_info = (RelativeLayout) ll_order_view.findViewById(R.id.ll_add_consignee_info);
        //支付商品信息
        ll_expandable_container = (LinearLayout) ll_order_view.findViewById(R.id.ll_expandable_container);
        pay_ex_list_view = (PayExpandableListView) ll_order_view.findViewById(R.id.pay_ex_list_view);
        //底部字符属性
        pay_goods_count = (TextView) ll_order_view.findViewById(R.id.pay_goods_count);
        pay_total_price = (TextView) ll_order_view.findViewById(R.id.pay_total_price);

        //现金券 是否使用按钮
        tv_order_sales_promotion_cb = (CheckBox) ll_order_view.findViewById(R.id.tv_order_sales_promotion_cb);
        tv_order_sales_method_cash = (TextView) ll_order_view.findViewById(R.id.tv_order_sales_method_cash); //现金券可用
        pay_favorable_price = (TextView) ll_order_view.findViewById(R.id.pay_favorable_price); //优惠价格
        tv_order_sales_promotion_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (cash.getItem().getCash_coupon() >= totalCash) {  //账户现金券 大于等于本次要抵扣的现金券数
                        tv_order_sales_method_cash.setText(String.format(getString(R.string.cart_totle_price2), String.valueOf(totalCash)));
                        pay_favorable_price.setText(String.format(getString(R.string.cart_totle_price2), String.valueOf(totalCash)));//优惠价
                        BigDecimal bigDecimal = new BigDecimal((confirmOrderBean.totalprice - totalCash));
                        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                        pay_total_price.setText(String.format(getString(R.string.cart_totle_price2), String.valueOf(bigDecimal.doubleValue())));//合计价格 （减去优惠价）
                    } else {
                        tv_order_sales_method_cash.setText(String.format(getString(R.string.cart_totle_price2), String.valueOf(cash.getItem().getCash_coupon())));
                        pay_favorable_price.setText(String.format(getString(R.string.cart_totle_price2), String.valueOf(cash.getItem().getCash_coupon())));//优惠价
                        BigDecimal bigDecimal = new BigDecimal((confirmOrderBean.totalprice - cash.getItem().getCash_coupon()));
                        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                        pay_total_price.setText(String.format(getString(R.string.cart_totle_price2), String.valueOf(bigDecimal.doubleValue())));//合计价格 （减去优惠价）
                    }

                } else {
                    pay_favorable_price.setText(String.format(getString(R.string.cart_totle_price2),String.valueOf(0.00)));
                    pay_total_price.setText(String.format(getString(R.string.cart_totle_price2),String.valueOf(confirmOrderBean.totalprice)));//总计
                }
            }
        });

        //提交订单按钮
        tv_pay_btn = (TextView) ll_order_view.findViewById(R.id.tv_pay_btn);
        fl_order_container.addView(ll_order_view);
        ll_order_view.setVisibility(View.GONE);
        //获取传递过来的数据
        getIntentData();
        //网络传递过来的数据
        //用户地址信息
        userData();

    }

    /**
     * 获取传递过来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        sku_ids = intent.getStringExtra("sku_ids");
        amount = intent.getStringExtra("amount");
        is_prompt = intent.getIntExtra("is_prompt", 0);
    }

    private void initDatas() {
        //注册广播
        registerBoradcastReceiver();
    }

    /**
     * 注册广播
     */
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(getResources().getString(R.string.update_price_url));
        updatePriceReceiver = new UpdatePriceReceiver();
        registerReceiver(updatePriceReceiver, myIntentFilter);
    }

    @Override
    protected void onDestroy() {
        //取消注册
        unregisterReceiver(updatePriceReceiver);
        super.onDestroy();
    }

    /**
     * 默认group是打开的
     */
    private void openGroup() {
        for (int i = 0; i < payExpandableAdapter.getGroupCount(); i++) {
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
            Bundle bundle = data.getExtras();
            ItemBean itemBean = (ItemBean) bundle.get("itemBean");
            if (itemBean != null) {
                mItemBean = itemBean;
                String address = itemBean.provinceName + itemBean.cityName + itemBean.areaName + itemBean.townName +
                        itemBean.dinfo_address;

                tv_consignee_info1.setText(itemBean.dinfo_received_name);
                tv_consignee_info2.setText(itemBean.dinfo_mobile);
                tv_consignee_info3.setText(address);
                recid = itemBean.addressId;
                isAddAddress = true;
            } else {
                if (!isAddAddress) {
                    //如果是第一次进来,但是没有添加地址就直接关闭页面
                    finish();
                }
            }
        }
    }

    /**
     * 请求网络用户地址信息
     */
    private void userData() {
        LoadDialog.show(PayActivity.this);
        //获取账户可抵用现金券
        myWalletControl = new MyWalletControl(PayActivity.this);
        myWalletControl.getBalanceAndCash(PayActivity.this);
    }

    //获取余额 现金券
    @Override
    public void getBalanceAndCash(BalanceAndCash balanceAndCash) {
        cash = balanceAndCash;
        //查询用户地址信息
        seekUserInfoController = new SeekUserInfoController(PayActivity.this);
        seekUserInfoController.onBackUserInfoData(PayActivity.this);
    }

    @Override
    public void balanceAndCashErrorMsg(int message) {
        LoadDialog.dismiss(PayActivity.this);
        //没有网络时显示没有网络
        noView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(PayActivity.this, GBAccountError.getErrorMsg(PayActivity.this,message));
    }

    //请求网络返回的数据
    @Override
    public void userInfoData(AddressInfoBean addressInfoBean) {
        if (addressInfoBean == null) {
            LoadDialog.dismiss(PayActivity.this);
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            return;
        }
        //判断是否有用户信息
        if (addressInfoBean.userInfos == null) {
            // 没有用户信息,就直接去添加,后显示
            final CustomDialog2 customDialog = new CustomDialog2(PayActivity.this);
            customDialog.setDialogTitle(getResources().getString(R.string.no_address_dialog_title));
            customDialog.setNegativeButton(getResources().getString(R.string.cancel_text), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                    finish();
                }
            });
            customDialog.setPositiveButton(getResources().getString(R.string.comfirm_text), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                    Intent adressIntent = new Intent(PayActivity.this, AddressManageActivity.class);
                    adressIntent.putExtra(ActionUtil.ACTION_ADDRESS_MANAGE, false);
                    startActivityForResult(adressIntent, RequestCodeConstant.FIRST_ADD_ADDRESS);
                }
            });

        } else {
            //有用户信息
            List<ItemBean> items = addressInfoBean.userInfos.item;
            //标记,判断是否有默认
            boolean tag = false;
            for (int i = 0; i < items.size(); i++) {
                //1代表是默认地址,0不是默认
                if (items.get(i).isDefaultAddress == 1) {
                    mItemBean = items.get(i);
                    tag = true;
                    String address = items.get(i).provinceName + " " + items.get(i).cityName + " " + items.get(i).areaName + " " + items.get(i).townName + " " +
                            items.get(i).dinfo_address;
                    if (items.get(i).dinfo_mobile.length() != 0) {
                        tv_consignee_info1.setText(items.get(i).dinfo_received_name);
                        tv_consignee_info2.setText(items.get(i).dinfo_mobile);
                        tv_consignee_info3.setText(address);
                    } else {
                        tv_consignee_info1.setText(items.get(i).dinfo_received_name);
                        tv_consignee_info2.setText(items.get(i).dinfo_tel);
                        tv_consignee_info3.setText(address);
                    }
                    recid = items.get(i).addressId;
                    isAddAddress = true;
                }

            }
            if (!tag) {
                //没有默认,取数据中的最后一个
                ItemBean itemBean = items.get(items.size() - 1);
                mItemBean = itemBean;
                String address = itemBean.provinceName + " " + itemBean.cityName + " " + itemBean.areaName + " " + itemBean.townName + " " +
                        itemBean.dinfo_address;
                if (itemBean.dinfo_mobile.length() != 0) {
                    tv_consignee_info1.setText(itemBean.dinfo_received_name);
                    tv_consignee_info2.setText(itemBean.dinfo_mobile);
                    tv_consignee_info3.setText(address);
                } else {
                    tv_consignee_info1.setText(itemBean.dinfo_received_name);
                    tv_consignee_info2.setText(itemBean.dinfo_tel);
                    tv_consignee_info3.setText(address);
                }
                recid = itemBean.addressId;
                isAddAddress = true;
            }
        }

        //确定订单的数据
        if (seekUserInfoController != null) {
            if (NetWorkUtil.noHaveNet(PayActivity.this)) {
                LoadDialog.dismiss(PayActivity.this);
                return;
            }
            seekUserInfoController.onBackConfirmData(PayActivity.this, sku_ids, is_prompt);
        }
    }

    /**
     * 结算商品数据
     *
     * @param confirmOrderBean 返回是数据
     */
    @Override
    public void confirmData(ConfirmOrderBean confirmOrderBean) {
        if (confirmOrderBean == null) {
            LoadDialog.dismiss(PayActivity.this);
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            return;
        }
        //TODO 整理返回的确认订单数据
        clearUpData(confirmOrderBean);


//        List<ShopInfoBean> shopInfos = confirmOrderBean.order.detail.shopInfo;
//        ExpressMethodController expressMethodController = new ExpressMethodController(PayActivity.this,PayActivity.this, shopInfos.size());
//        if (!NetWorkUtil.isNetworkAvailable(PayActivity.this) && !NetWorkUtil.isWifiConnected(PayActivity.this)) {
//            //没有网络时显示没有网络
//            noView.setVisibility(View.VISIBLE);
//            return;
//        }
//        for (int i = 0; i < shopInfos.size(); i++) {
//            int shopId = shopInfos.get(i).shopId;
//            List<GoodsItemsBean> goodsItems = shopInfos.get(i).tempItems.goodsItems;
//            for (GoodsItemsBean goodsItemsBean:goodsItems){
//                int temp_id = goodsItemsBean.temp_id;
//                expressMethodController.onBackDispatchMethodData(PayActivity.this,temp_id, shopId, recid);
//            }
//        }

    }

    private void clearUpData(ConfirmOrderBean confirmOrderBean) {
        SelfOrderBean selfOrder = new SelfOrderBean();
        selfOrder.goodsNum = confirmOrderBean.order.goodsNum;
        selfOrder.max_largess_qty = confirmOrderBean.order.max_largess_qty;
        selfOrder.pay_method = confirmOrderBean.order.pay_method;
        selfOrder.totalprice = confirmOrderBean.order.totalprice;
        //整理每个店铺里的数据
        List<SelfShopInfo> selfShopInfos = new ArrayList<>();
        List<ShopInfoBean> shopInfo = confirmOrderBean.order.detail.shopInfo;
        if (shopInfo == null) {
            ToastUtil.showToast(PayActivity.this,"订单商品信息有误，请联系客服");
            LoadDialog.dismiss(PayActivity.this);
            finish();
            return;
        }
        for (ShopInfoBean shopInfoBean : shopInfo) {
            List<GoodsItemsBean> goodsItems = shopInfoBean.tempItems.goodsItems;
            for (GoodsItemsBean goodsItemsBean : goodsItems) {
                SelfShopInfo selfShop = new SelfShopInfo();
                selfShop.goodsNum = shopInfoBean.goodsNum;
                selfShop.has_invoice = shopInfoBean.has_invoice;
                selfShop.is_repaired = shopInfoBean.is_repaired;
                selfShop.is_returned = shopInfoBean.is_returned;
                selfShop.shopId = shopInfoBean.shopId;
                selfShop.shopName = shopInfoBean.shopName;
                selfShop.totalPrice = shopInfoBean.totalPrice;

                SelfGoodsItem selfGoods = new SelfGoodsItem();
                List<DataBean> datas = goodsItemsBean.data;
                selfGoods.data = datas;
                selfGoods.temp_id = goodsItemsBean.temp_id;

                selfShop.goodsItem = selfGoods;
                selfShopInfos.add(selfShop);
            }
            selfOrder.shopInfos = selfShopInfos;
        }
        this.confirmOrderBean = selfOrder;
        CommonUtil.printI("整理后的订单数据", selfOrder.toString());
        if (!NetWorkUtil.isNetworkAvailable(PayActivity.this) && !NetWorkUtil.isWifiConnected(PayActivity.this)) {
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            return;
        }
        ExpressMethodController expressMethodController = new ExpressMethodController(PayActivity.this, PayActivity.this, selfShopInfos.size());
        for (SelfShopInfo selfShopInfo : selfShopInfos) {
            int shopId = selfShopInfo.shopId;
            int temp_id = selfShopInfo.goodsItem.temp_id;
            expressMethodController.onBackDispatchMethodData(PayActivity.this, temp_id, shopId, recid);
        }
    }


    //返回的配送方式数据
    @Override
    public void expressMethodData(List<ExpressMethodBean> expressMethodBeans) {
        if (expressMethodBeans == null) {
            //没有网络时显示没有网络
            noView.setVisibility(View.VISIBLE);
            return;
        }
        for (int i = 0; i < expressMethodBeans.size(); i++) {
            //默认第一个是被选中的
            expressMethodBeans.get(i).expressage.item.get(0).isChoosed = true;
        }
        CommonUtil.printI("梳理后的配送信息++++", expressMethodBeans.toString());
        List<SelfShopInfo> shopInfos = confirmOrderBean.shopInfos;
        for (SelfShopInfo selfShopInfo : shopInfos) {
            SelfGoodsItem goodsItem = selfShopInfo.goodsItem;
            for (ExpressMethodBean expressMethodBean : expressMethodBeans) {
                if (expressMethodBean.temp_id == goodsItem.temp_id) {
                    goodsItem.expressMethodBean = expressMethodBean;
                }
            }
        }
        //获取数据完成，显示数据界面
        ll_order_view.setVisibility(View.VISIBLE);
        obtainData(shopInfos);
    }

    //处理数据
    private void obtainData(List<SelfShopInfo> shopInfos) {
        LoadDialog.dismiss(PayActivity.this);
        //设置适配器
        payExpandableAdapter = new PayExpandableAdapter(PayActivity.this, shopInfos, fl_order_container, pay_goods_count, pay_total_price);
//        PayExpandableAdapter1 payExpandableAdapter = new PayExpandableAdapter1(PayActivity.this, shopInfos, fl_order_container);
        pay_ex_list_view.setAdapter(payExpandableAdapter);
        //setListViewHeightBasedOnChildren(pay_ex_list_view);
        openGroup();
        pay_goods_count.setText("共 " + confirmOrderBean.goodsNum + " 件");
        // 显示总价格
        showTotalPrice(shopInfos);
    }

    /**
     * 显示总价格
     */
    private void showTotalPrice(List<SelfShopInfo> shopInfos) {

        double tempTotal = 0.00;
        BigDecimal bigTotalCash = new BigDecimal(tempTotal);
        for (SelfShopInfo shopInfo:shopInfos) {
            List<DataBean> data = shopInfo.goodsItem.data;
            for (DataBean dataBean: data) {
                Log.e("CASH:: ",""+dataBean);
                if (dataBean.num == 1) {
                    bigTotalCash = bigTotalCash.add(new BigDecimal(dataBean.cash_coupon));
                } else {
                    bigTotalCash = bigTotalCash.add(new BigDecimal(dataBean.cash_coupon * dataBean.num));
                }
            }
        }
        bigTotalCash.setScale(2, BigDecimal.ROUND_HALF_UP);
        totalCash = bigTotalCash.doubleValue();
        //tv_order_sales_method_cash.setText(String.format(getString(R.string.cart_totle_price2),String.valueOf(totalCash)));
        // pay_favorable_price.setText(String.format(getString(R.string.cart_totle_price2),String.valueOf(totalCash)));
//        double price = 0.00;
//        Log.i(ExpressMethodController.class.getSimpleName(), "3+++++" + shopInfos.size());
//        BigDecimal bigTotalPrice = new BigDecimal(confirmOrderBean.totalprice);
//        for (SelfShopInfo shopInfoBean : shopInfos) {
//            Log.i(ExpressMethodController.class.getSimpleName(), "4+++++" + shopInfoBean.shopName);
//            if (shopInfoBean.goodsItem.expressMethodBean != null) {
//                List<com.winguo.pay.modle.bean.ItemBean> item = shopInfoBean.goodsItem.expressMethodBean.expressage.item;
//                for (com.winguo.pay.modle.bean.ItemBean itemBean : item) {
//                    if (itemBean.isChoosed) {
//                        bigTotalPrice = bigTotalPrice.add(new BigDecimal(itemBean.price));
//                    }
//                }
//            }
//
//        }
//        bigTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
//        price = bigTotalPrice.doubleValue();
       // BigDecimal bigDecimal = new BigDecimal((confirmOrderBean.totalprice-totalCash));
      //  bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
      //  pay_total_price.setText(String.format(getString(R.string.cart_totle_price2),String.valueOf(bigDecimal.doubleValue())));//合计价格 （减去优惠价）

        tv_order_sales_promotion_cb.setChecked(true);//默认选中

    }

    //点击促销的处理
    @Override
    public void onClickSales(String gid, TextView textView) {

    }

    //点击配送的处理
    @Override
    public void onClickDispatchMethod(String gid, TextView textView) {

    }

    private void initListener() {
//        order_back_btn.setOnClickListener(this);
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
//            case R.id.order_back_btn:
//                finish();
//                break;
            case R.id.ll_add_consignee_info:
                //跳转到选择地址页面
                Intent adressIntent = new Intent(PayActivity.this, AddressManageActivity.class);
                adressIntent.putExtra(ActionUtil.ACTION_ADDRESS_MANAGE, false);
                startActivityForResult(adressIntent, RequestCodeConstant.FIRST_ADD_ADDRESS);
                break;
            case R.id.tv_pay_btn:
                //判断有没有网络
                if (!NetWorkUtil.isNetworkAvailable(PayActivity.this) && !NetWorkUtil.isWifiConnected(PayActivity.this)) {
                    final ConfirmDialog confirmDialog = new ConfirmDialog(PayActivity.this, false);
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
                    ToastUtil.showToast(PayActivity.this, getString(R.string.no_address_reminder));
                    return;
                }

                //跳转到支付页面,并提交服务器
                //店铺id
                String shop_ids = "";
                //配送模板
                String temp_ids = "";
                //运费
                String ct = "";
                //买家留言
                String userMsgs = "";
                getConfirmOrderData(shop_ids, temp_ids, ct, userMsgs);
                break;
        }
    }

    /**
     * 确认下单
     * @param shop_ids
     * @param temp_ids
     * @param ct
     * @param userMsgs
     */
    private void getConfirmOrderData(String shop_ids, String temp_ids, String ct, String userMsgs) {
        List<SelfShopInfo> shopInfo = confirmOrderBean.shopInfos;
        for (int i = 0; i < shopInfo.size(); i++) {
            String shopId = String.valueOf(shopInfo.get(i).shopId);
            if (i != shopInfo.size() - 1) {
                temp_ids += shopInfo.get(i).goodsItem.temp_id + ",";
            } else {
                temp_ids += shopInfo.get(i).goodsItem.temp_id;
            }
            //物流配送方式
            List<com.winguo.pay.modle.bean.ItemBean> item = shopInfo.get(i).goodsItem.expressMethodBean.expressage.item;
            for (int j = 0; j < item.size(); j++) {
                if (item.get(j).isChoosed) {
                    String code = String.valueOf(item.get(j).code);
                    if (i != shopInfo.size() - 1) {
                        ct += code + ",";
                    } else {
                        ct += code;
                    }
                }
            }
            if (i != shopInfo.size() - 1) {
                shop_ids += shopId + ",";
            } else {
                shop_ids += shopId;
            }
            //留言的数组
            String userMsg = shopInfo.get(i).goodsItem.userMsg;
            if (!TextUtils.isEmpty(userMsg)) {
                try {
                    userMsg = URLEncoder.encode(userMsg, "utf-8");
                    userMsg = Base64.encode(userMsg.getBytes());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (i != shopInfo.size() - 1) {

                userMsgs += userMsg + ",";
            } else {
                userMsgs += userMsg;
            }
        }
        CommonUtil.printI("加入订单的数据", ct + "," + temp_ids + "," + userMsgs);
        //请求网络,根据返回的数据决定跳转
        //判断网络状态
        if (NetWorkUtil.noHaveNet(PayActivity.this)) {
            return;
        }
//        LoadDialog.show(PayActivity.this, false);
        ConfirmOrderController confirmOrderController = new ConfirmOrderController(this);
        if (tv_order_sales_promotion_cb.isChecked()) { // 选中 使用现金券
            confirmOrderController.getRegistOrderData(PayActivity.this, sku_ids, String.valueOf(0), shop_ids, ct, temp_ids, userMsgs, String.valueOf(recid), is_prompt, true);
        } else {  //不选 不适用现金券
            confirmOrderController.getRegistOrderData(PayActivity.this, sku_ids, String.valueOf(0), shop_ids, ct, temp_ids, userMsgs, String.valueOf(recid), is_prompt, false);
        }
    }

    //下订单返回的数据
    @Override
    public void registOrderData(OrderResultBean orderResultBean) {
        LoadDialog.dismiss(PayActivity.this);
        if (orderResultBean == null) {
            //没有网络
            ll_order_view.setVisibility(View.GONE);
            noView.setVisibility(View.VISIBLE);
            return;
        }
        int code = orderResultBean.message.code;
        if (code == RequestCodeConstant.CONFIRM_ORDER_ERROR_NO_GOODS_CODE ||
                code == RequestCodeConstant.CONFIRM_ORDER_ERROR_CODE ||
                code == RequestCodeConstant.CONFIRM_ORDER_ERROR_SIGN_CODE) {
            final ConfirmDialog confirmDialog = new ConfirmDialog(PayActivity.this, false);
            confirmDialog.setDialogTitle(getResources().getString(R.string.confirm_order_dialog_title));
            confirmDialog.setDialogContent(orderResultBean.message.text);
            confirmDialog.setPositiveButton(getResources().getString(R.string.dialog_positive_btn_text), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    finish();
                }
            });
        }
        if (code == RequestCodeConstant.CONFIRM_ORDER_SUCCESS_CODE) {
            ArrayList<CommonBean> commonBeen = setData();
            //下单成功发送广播
            Intent receiverIntent=new Intent();
            receiverIntent.setAction("com.winguo.confirmpay.success");
            sendBroadcast(receiverIntent);
            //下单成功,跳转
            Intent intent = new Intent(PayActivity.this, ConfirmOrderActivity.class);
            intent.putExtra("mItemBean", mItemBean);
            Bundle bundle = new Bundle();
            bundle.putSerializable("commonBeen", commonBeen);
            intent.putExtras(bundle);
            intent.putExtra("isphysical",0);
            intent.putExtra("goodsCount", confirmOrderBean.goodsNum);
            intent.putExtra("orderId", orderResultBean.message.order_id);
            startActivity(intent);
            finish();
        }
    }

    private class UpdatePriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals(getResources().getString(R.string.update_price_url))) {
                //接到广播就更新价格
                showTotalPrice(confirmOrderBean.shopInfos);
                CommonUtil.printI("接受到广播++++", confirmOrderBean.shopInfos.toString());
            }
        }
    }

    /**
     * 封装数据
     */
    private ArrayList<CommonBean> setData() {
        ArrayList<CommonBean> commonBeans = new ArrayList<>();
        if (confirmOrderBean != null) {
            List<SelfShopInfo> shopInfos = confirmOrderBean.shopInfos;
            for (SelfShopInfo shopInfo : shopInfos) {
                List<DataBean> data = shopInfo.goodsItem.data;
                for (DataBean dataBean : data) {
                    CommonBean commonBean = new CommonBean();
                    commonBean.content = dataBean.icon.content;
                    commonBean.name = dataBean.name;
                    commonBean.num = dataBean.num;
                    commonBeans.add(commonBean);
                }
            }
        }
        return commonBeans;
    }
}
