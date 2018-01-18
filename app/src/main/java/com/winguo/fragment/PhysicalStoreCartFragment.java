package com.winguo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guobi.gblocation.GBDLocation;
import com.winguo.R;
import com.winguo.activity.NearbyStoreHomeActivity;
import com.winguo.activity.PhysicalPayActivity;
import com.winguo.cart.bean.PhysicalStoreBean;
import com.winguo.cart.controller.physicalstore.SeekPhysicalCartController;
import com.winguo.cart.view.physicalstore.IPhysicalCartView;
import com.winguo.cart.view.physicalstore.PhysicalShopCartAdapter;
import com.winguo.cart.view.store.ChangedGoodsCountWindow;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class PhysicalStoreCartFragment extends Fragment implements IPhysicalCartView, View.OnClickListener,
        PhysicalShopCartAdapter.PhysicalCheckInterface, PhysicalShopCartAdapter.PhysicalChangedCountInterface, ChangedGoodsCountWindow.IChangedAfterCountInterface {

    private FrameLayout physicalStoreFrameLayout;
    private Context mContext;

    private Button btn_request_net;
    private ExpandableListView lv_cart_shop;
    private CheckBox all_selected_btn;
    private TextView cart_total_price;
    private LinearLayout ll_cart_pay_btn;
    private TextView pay_product_count;
    private PhysicalShopCartAdapter physicalShopCartAdapter;
    //返回的数据
    private ViewGroup.LayoutParams params;
    private View noNetView;
    private View cart_no_data_view;
    private View cart_data_view;
    private View view;
    private int totalCount = 0;
    private double totalPrice = 0.00;
    private BigDecimal bigTotalPrice;
    private int sku_id;
    private int is_prompt;
    private TextView showCountView;
    private int count;
    private SeekPhysicalCartController seekPhysicalCartController;
    private PhysicalStoreBean physicalStoreBean;
    private List<PhysicalStoreBean.ContentBean> contentBeans=new ArrayList<>();
    private String cartId;
    private int packedPositionChild;
    private int packedPositionGroup;
    private boolean isLongClick;
    private boolean isItemClick;
    private PhysicalConfirmPayReceiver physicalConfirmPayReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        physicalStoreFrameLayout = new FrameLayout(mContext);
        initData();
        initViews();
        initListener();
        return physicalStoreFrameLayout;
    }

    private void initData() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.winguo.physicalconfirmpay.success");
        physicalConfirmPayReceiver = new PhysicalConfirmPayReceiver();
        mContext.registerReceiver(physicalConfirmPayReceiver, myIntentFilter);
    }
    class PhysicalConfirmPayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            resetBottomState();
            getData();
        }
    }

    private void initViews() {
        //初始化布局控件
        init();
        //调用回调函数获取数据
        getData();
    }

    private void initListener() {
        //点击事件
        btn_request_net.setOnClickListener(this);
        ll_cart_pay_btn.setOnClickListener(this);
        lv_cart_shop.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    //点击进入商品详情
                    String m_maker_id = contentBeans.get(groupPosition).maker.m_maker_id;
                    Intent storeActivity = new Intent(mContext, NearbyStoreHomeActivity.class);
                    storeActivity.putExtra("store_id", m_maker_id);
                    storeActivity.putExtra("log", GBDLocation.getInstance().getLongitude());
                    storeActivity.putExtra("lat", GBDLocation.getInstance().getLatitude());
                    storeActivity.putExtra("isFromPhyCart", true);
                    startActivity(storeActivity);

                return true;
            }
        });
        lv_cart_shop.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                    //获取group的位置，这个group是在group的集合中的位置
                    packedPositionGroup = ExpandableListView.getPackedPositionGroup(packedPos);
                    //获取child的位置，是在自己对应group中集合的位置
                    packedPositionChild = ExpandableListView.getPackedPositionChild(packedPos);
                    if (packedPositionChild != -1) {
                        //如果点击的是group，child就为-1
                        final CustomDialog2 customDialog = new CustomDialog2(mContext);
                        customDialog.setDialogTitle(mContext.getResources().getString(R.string.delete_physical_cart_goods));
                        customDialog.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        });
                        customDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击删除
                                doDelete();
                                customDialog.dismiss();
                            }
                        });


                    }

                return true;
            }
        });
    }

    //删除购物车数据
    private void doDelete() {
        String cartId = contentBeans.get(packedPositionGroup).item.get(packedPositionChild).id;
        if (seekPhysicalCartController != null) {
            //判断网络状态
            if (NetWorkUtil.noHaveNet(mContext)) {
                return;
            }
            LoadDialog.show(mContext);
            seekPhysicalCartController.deletePhysicalCartDta(mContext, cartId);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_net:
                if (noNetView != null) {
                    noNetView.setVisibility(View.GONE);
                }
                getData();
                break;
            case R.id.ll_cart_pay_btn:
                if (NetWorkUtil.noHaveNet(mContext)) {
                    return;
                }
                if (totalCount == 0) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.cart_no_selected_goods));
                    return;
                }
                //获取到要传入支付的数据
                String cart_ids = "";
                for (int i = 0; i < contentBeans.size(); i++) {
                    //遍历所有的商品,拿到被选择的
                    List<PhysicalStoreBean.ContentBean.ItemBean> items = contentBeans.get(i).item;
                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).isChoosed()) {
                            cart_ids += items.get(j).id + ",";
                        }
                    }
                }
                cart_ids=cart_ids.substring(0,cart_ids.length()-1);
                CommonUtil.printI("cart_ids", cart_ids);
                //点击结算
                Intent payIntent = new Intent(mContext, PhysicalPayActivity.class);
                payIntent.putExtra("cart_ids", cart_ids);
                startActivity(payIntent);
                break;
        }

    }

    private void init() {
        //几种不同的状态
        params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //没有网络时
        noNetView = View.inflate(mContext, R.layout.no_net, null);
        physicalStoreFrameLayout.addView(noNetView, params);
        btn_request_net = noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        noNetView.setVisibility(View.GONE);

        //没有数据时
        cart_no_data_view = View.inflate(mContext, R.layout.cart_no_data, null);
        physicalStoreFrameLayout.addView(cart_no_data_view, params);
        cart_no_data_view.setVisibility(View.GONE);
        //有数据时
        cart_data_view = View.inflate(mContext, R.layout.cart_data_view, null);
        physicalStoreFrameLayout.addView(cart_data_view, params);
        //店铺的listView
        lv_cart_shop = cart_data_view.findViewById(R.id.lv_cart_shop);
        //全选
        all_selected_btn = cart_data_view.findViewById(R.id.all_selected_btn);
        //合计
        cart_total_price = cart_data_view.findViewById(R.id.cart_total_price);
        //结算
        ll_cart_pay_btn = cart_data_view.findViewById(R.id.ll_cart_pay_btn);
        pay_product_count = cart_data_view.findViewById(R.id.pay_product_count);
        cart_data_view.setVisibility(View.GONE);
    }
    protected void getData() {
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            LoadDialog.show(mContext);
            seekPhysicalCartController = new SeekPhysicalCartController(this);
            seekPhysicalCartController.getPhysicalCartDta(mContext);
        } else {
            if (noNetView != null) {
                noNetView.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void getPhysicalCartData(PhysicalStoreBean physicalStoreBean) {
        CommonUtil.printI("实体店购物车中数据", physicalStoreBean.toString());
        LoadDialog.dismiss(mContext);
        //网络请求失败
        if (physicalStoreBean == null) {
            if (noNetView != null) {
                noNetView.setVisibility(View.VISIBLE);
            }
            return;
        }
//        //网络请求成功
//        if (physicalStoreBean.order == null) {
//            //登录过期了,要重新去登录
////            MyApplication.isLogin = false;
//            Intent loginIntent = new Intent(mContext, WinguoLoginActivity.class);
//            startActivity(loginIntent);
//            return;
//        }
        resetBottomState();
        this.physicalStoreBean = physicalStoreBean;
        if (physicalShopCartAdapter == null) {
            //显示数据
            showView();
        } else {
            contentBeans.clear();
            if (contentBeans.size() != 0) {//当购物车中商品没有了,就会为null
                contentBeans.addAll(physicalStoreBean.content);
                openGroup();
                physicalShopCartAdapter.notifyDataSetChanged();
            }
            //显示数据
            showView();
        }
    }

    @Override
    public void upDatePhysicalCartData(PhysicalStoreBean physicalStoreBean) {
        LoadDialog.dismiss(mContext);
        //网络请求失败
        if (physicalStoreBean == null) {
            ToastUtil.showToast(mContext, getString(R.string.timeout));
            return;
        }
        resetBottomState();
        //网络请求成功
        contentBeans.clear();
        if (physicalStoreBean.size!=0)
            contentBeans.addAll(physicalStoreBean.content);
        if (physicalShopCartAdapter != null)
            physicalShopCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void deletePhysicalCartData(PhysicalStoreBean physicalStoreBean) {
        LoadDialog.dismiss(mContext);
        //网络请求失败
        if (physicalStoreBean == null) {
            ToastUtil.showToast(mContext, getString(R.string.timeout));
            return;
        }
        resetBottomState();
        //网络请求成功
        contentBeans.clear();
        contentBeans.addAll(physicalStoreBean.content);
        if (physicalShopCartAdapter != null)
            physicalShopCartAdapter.notifyDataSetChanged();

    }

    private void openGroup() {
        for (int i = 0; i < physicalShopCartAdapter.getGroupCount(); i++) {
            lv_cart_shop.expandGroup(i);
        }
    }

    private void showView() {
        if (physicalStoreBean == null) {
            //没有网络
            if (noNetView != null) {
                noNetView.setVisibility(View.VISIBLE);
            }
            if (cart_no_data_view != null) {
                cart_no_data_view.setVisibility(View.GONE);
            }
            if (cart_data_view != null) {
                cart_data_view.setVisibility(View.GONE);
            }
            return;
        }
        if (physicalStoreBean.size == 0) {
            //购物车没有数据
            if (noNetView != null) {
                noNetView.setVisibility(View.GONE);
            }
            if (cart_no_data_view != null) {
                cart_no_data_view.setVisibility(View.VISIBLE);
            }
            if (cart_data_view != null) {
                cart_data_view.setVisibility(View.GONE);
            }
        } else {
            //有数据
            if (noNetView != null) {
                noNetView.setVisibility(View.GONE);
            }
            if (cart_no_data_view != null) {
                cart_no_data_view.setVisibility(View.GONE);
            }
            if (cart_data_view != null) {
                cart_data_view.setVisibility(View.VISIBLE);
            }
            //可以设置适配器
            setDataToListView();
            //全选的点击事件
            all_selected_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCheckAll();
                }
            });
        }
    }

    private void setDataToListView() {
        //这是店铺的数据,外面listView的数据源
        contentBeans = physicalStoreBean.content;
        physicalShopCartAdapter = new PhysicalShopCartAdapter(mContext, contentBeans);
        physicalShopCartAdapter.setOnCheckInterface(this);
        physicalShopCartAdapter.setOnChangedCountInterface(this);
        lv_cart_shop.setAdapter(physicalShopCartAdapter);

        //设置父容器都是打开的
        openGroup();
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < contentBeans.size(); i++) {
            contentBeans.get(i).maker.setChoosed(all_selected_btn.isChecked());
            List<PhysicalStoreBean.ContentBean.ItemBean> datas = contentBeans.get(i).item;
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).setChoosed(all_selected_btn.isChecked());
            }
        }
        physicalShopCartAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        PhysicalStoreBean.ContentBean contentBean = contentBeans.get(groupPosition);
        List<PhysicalStoreBean.ContentBean.ItemBean> dataBeans = contentBean.item;
        for (int i = 0; i < dataBeans.size(); i++) {
            dataBeans.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            all_selected_btn.setChecked(true);
        else
            all_selected_btn.setChecked(false);
        physicalShopCartAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        PhysicalStoreBean.ContentBean contentBean = contentBeans.get(groupPosition);
        List<PhysicalStoreBean.ContentBean.ItemBean> items = contentBean.item;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            contentBean.maker.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            contentBean.maker.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck())
            all_selected_btn.setChecked(true);
        else
            all_selected_btn.setChecked(false);
        physicalShopCartAdapter.notifyDataSetChanged();
        calculate();
    }

    //是否是全选
    private boolean isAllCheck() {
        for (PhysicalStoreBean.ContentBean contentBean : contentBeans) {
            if (!contentBean.maker.isChoosed())
                return false;
        }
        return true;
    }


    /**
     * 把结算的价格和数量显示到控件上
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        bigTotalPrice = new BigDecimal(0.00);
        for (int i = 0; i < contentBeans.size(); i++) {
            PhysicalStoreBean.ContentBean contentBean = contentBeans.get(i);
            List<PhysicalStoreBean.ContentBean.ItemBean> dataBeans = contentBean.item;
            for (int j = 0; j < dataBeans.size(); j++) {
                PhysicalStoreBean.ContentBean.ItemBean itemBean = dataBeans.get(j);
                if (itemBean.isChoosed()) {
                    totalCount += Integer.valueOf(itemBean.num);
                    bigTotalPrice = bigTotalPrice.add(new BigDecimal(Double.toString(Double.valueOf(itemBean.num_price))));
                    bigTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                    totalPrice = bigTotalPrice.doubleValue();
                    CommonUtil.printI("计算后的结果====", bigTotalPrice.toString());
                }
            }
        }
        cart_total_price.setText(String.format(mContext.getResources().getString(R.string.cart_totle_price),String.valueOf(totalPrice)));
        pay_product_count.setText(String.format(getString(R.string.cart_goods_selected_count), String.valueOf(totalCount)));
    }

    @Override
    public void doChanged(int groupPosition, int childPosition, String cartId, TextView showCountView, String limit_buy, int goodsNum, boolean isChecked) {
        this.cartId = cartId;
        ChangedGoodsCountWindow changedGoodsCountWindow = new ChangedGoodsCountWindow(getActivity(), Integer.valueOf(limit_buy), goodsNum);
        changedGoodsCountWindow.setOnChangedAfterCount(this);
        changedGoodsCountWindow.showAtLocation(physicalStoreFrameLayout, Gravity.BOTTOM, 0, 0);
        calculate();
    }

    @Override
    public void getCount(int count) {
        if (seekPhysicalCartController != null) {
            //判断网络状态
            if (NetWorkUtil.noHaveNet(mContext)) {
                return;
            }
            LoadDialog.show(mContext);
            seekPhysicalCartController.upDatePhysicalCartData(mContext, cartId, String.valueOf(count));
        }

    }

    //修改数据后，底部栏要重新刷新状态
    private void resetBottomState() {
        if (isAllCheck()) {
            all_selected_btn.setChecked(false);
        }
        totalCount = 0;
        totalPrice = 0.00;
        cart_total_price.setText(String.format(mContext.getResources().getString(R.string.cart_totle_price),String.valueOf(totalPrice)));
        pay_product_count.setText(String.format(getString(R.string.cart_goods_selected_count), String.valueOf(totalCount)));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(physicalConfirmPayReceiver);
    }
}
