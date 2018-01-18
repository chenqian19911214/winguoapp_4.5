package com.winguo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.winguo.R;
import com.winguo.activity.PayActivity;
import com.winguo.activity.ProductActivity;
import com.winguo.cart.bean.CartDataBean;
import com.winguo.cart.bean.DataBean;
import com.winguo.cart.controller.store.SeekCartController;
import com.winguo.cart.model.store.seekcart.ShopInfoBean;
import com.winguo.cart.view.store.ChangedGoodsCountWindow;
import com.winguo.cart.view.store.ISeekCartView;
import com.winguo.cart.view.store.ShopGoodsAdapter;
import com.winguo.login.LoginActivity;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class StoreCartFragment extends Fragment implements ISeekCartView, ShopGoodsAdapter.CheckInterface,
        ShopGoodsAdapter.ChangedCountInterface, ChangedGoodsCountWindow.IChangedAfterCountInterface, View.OnClickListener {

    private Context mContext;
    private SeekCartController seekCartController;

    private Button btn_request_net;
    private ExpandableListView lv_cart_shop;
    private CheckBox all_selected_btn;
    private TextView cart_total_price;
    private LinearLayout ll_cart_pay_btn;
    private TextView pay_product_count;

    //返回的数据
    private CartDataBean cartDataBean;
    private ViewGroup.LayoutParams params;
    private View noNetView;
    private View cart_no_data_view;
    private View cart_data_view;
    private View view;

    private List<ShopInfoBean> shopInfos=new ArrayList<>();
    private ShopGoodsAdapter shopGoodsAdapter;
    private int totalCount = 0;
    private double totalPrice = 0.00;
    private BigDecimal bigTotalPrice;
    private int sku_id;
    private int is_prompt;
    private TextView showCountView;
    private int count;
    private HashMap<String, List<DataBean>> datas;
    private ConfirmPayReceiver confirmPayReceiver;
    private FrameLayout storeFrameLayout;
    private int packedPositionGroup;
    private int packedPositionChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        storeFrameLayout = new FrameLayout(mContext);
        initData();
        initViews();
        initListener();
        return storeFrameLayout;
    }
    private void initData() {
        if(shopGoodsAdapter!=null){
            shopGoodsAdapter=null;
        }
        if (shopInfos!=null){
            shopInfos.clear();
        }
        if (datas!=null){
            datas.clear();
        }

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.winguo.confirmpay.success");
        confirmPayReceiver = new ConfirmPayReceiver();
        mContext.registerReceiver(confirmPayReceiver, myIntentFilter);
    }

    private void initViews() {
        //初始化布局控件
        init();
        //调用回调函数获取数据
        getData();
    }

    private void init() {
        //几种不同的状态
        params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //没有网络时
        noNetView = View.inflate(mContext, R.layout.no_net, null);
        storeFrameLayout.addView(noNetView, params);
        btn_request_net = noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        noNetView.setVisibility(View.GONE);

        //没有数据时
        cart_no_data_view = View.inflate(mContext, R.layout.cart_no_data, null);
        storeFrameLayout.addView(cart_no_data_view, params);
        cart_no_data_view.setVisibility(View.GONE);
        //有数据时
        cart_data_view = View.inflate(mContext, R.layout.cart_data_view, null);
        storeFrameLayout.addView(cart_data_view, params);
        //店铺的listView
        lv_cart_shop = cart_data_view.findViewById(R.id.lv_cart_shop);
        //全选
        all_selected_btn = cart_data_view.findViewById(R.id.all_selected_btn);
        //合计
        cart_total_price = cart_data_view.findViewById(R.id.cart_total_price);
        cart_total_price.setText(String.format(mContext.getResources().getString(R.string.cart_totle_price),String.valueOf(totalPrice)));
        //结算
        ll_cart_pay_btn = cart_data_view.findViewById(R.id.ll_cart_pay_btn);
        pay_product_count = cart_data_view.findViewById(R.id.pay_product_count);
        cart_data_view.setVisibility(View.GONE);
    }
    //接收广播处理数据  com.winguo.confirmpay.success


    protected void getData() {
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            LoadDialog.show(mContext);
            seekCartController = new SeekCartController(this);
            seekCartController.getData(mContext);
        } else {
            if (noNetView != null) {
                noNetView.setVisibility(View.VISIBLE);
            }
        }

    }

    //回调函数返回的数据
    @Override
    public void seekCartData(CartDataBean cartDataBean) {
        LoadDialog.dismiss(mContext);
        //网络请求失败
        if (cartDataBean == null) {
            if (noNetView != null) {
                noNetView.setVisibility(View.VISIBLE);
            }
            return;
        }
        CommonUtil.printI("购物车看看看安卡n++++++++++", cartDataBean.toString());
        //网络请求成功
        if (cartDataBean.order == null) {
            //登录过期了,要重新去登录
//            MyApplication.isLogin = false;
            Intent loginIntent = new Intent(mContext, LoginActivity.class);
            startActivity(loginIntent);
            return;
        }
        resetBottomState();
        this.cartDataBean = cartDataBean;
        CommonUtil.printI("购物车数据的JavaBean数据====", cartDataBean.toString());
        if (shopGoodsAdapter == null) {
            //显示数据
            showView();
        } else {
            shopInfos.clear();
            datas.clear();
            if (cartDataBean.order.detail != null) {//当购物车中商品没有了,就会为null
                shopInfos.addAll(cartDataBean.order.detail.shopInfo);
                for (int i = 0; i < shopInfos.size(); i++) {
                    datas.put(String.valueOf(i), shopInfos.get(i).goodsItems.data);
                }
                openGroup();
                shopGoodsAdapter.notifyDataSetChanged();
            }
            //显示数据
            showView();
        }
    }

    private void showView() {
        if (cartDataBean == null) {
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
        if (cartDataBean.order.detail == null) {
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
        shopInfos = cartDataBean.order.detail.shopInfo;
        datas = new HashMap<>();
        for (int i = 0; i < shopInfos.size(); i++) {
            datas.put(String.valueOf(i), shopInfos.get(i).goodsItems.data);
        }

        shopGoodsAdapter = new ShopGoodsAdapter(mContext, shopInfos, datas);
        shopGoodsAdapter.setOnCheckInterface(this);
        shopGoodsAdapter.setOnChangedCountInterface(this);
        lv_cart_shop.setAdapter(shopGoodsAdapter);

        //设置父容器都是打开的
        openGroup();
    }

    class ConfirmPayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            totalCount = 0;
            totalPrice = 0.00;
            all_selected_btn.setChecked(false);
            cart_total_price.setText(String.format(mContext.getResources().getString(R.string.cart_totle_price),String.valueOf(totalPrice)));
            pay_product_count.setText(String.format(mContext.getResources().getString(R.string.cart_goods_selected_count), String.valueOf(totalCount)));
            getData();
        }
    }


    private void initListener() {
        //点击事件
        btn_request_net.setOnClickListener(this);
        ll_cart_pay_btn.setOnClickListener(this);
        lv_cart_shop.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //点击进入商品详情
                int item_id = shopInfos.get(groupPosition).goodsItems.data.get(childPosition).item_id;
                Intent productActivity = new Intent(mContext, ProductActivity.class);
                productActivity.putExtra("gid", item_id);
                productActivity.putExtra("isFromCart", true);
                startActivity(productActivity);
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

    private void openGroup() {
        for (int i = 0; i < shopGoodsAdapter.getGroupCount(); i++) {
            lv_cart_shop.expandGroup(i);
        }
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < shopInfos.size(); i++) {
            shopInfos.get(i).setChoosed(all_selected_btn.isChecked());
            List<DataBean> datas = shopInfos.get(i).goodsItems.data;
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).setChoosed(all_selected_btn.isChecked());
            }
        }
        shopGoodsAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        ShopInfoBean shopInfoBean = shopInfos.get(groupPosition);
        List<DataBean> dataBeans = shopInfoBean.goodsItems.data;
        for (int i = 0; i < dataBeans.size(); i++) {
            dataBeans.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            all_selected_btn.setChecked(true);
        else
            all_selected_btn.setChecked(false);
        shopGoodsAdapter.notifyDataSetChanged();
        calculate();
    }

    //是否是全选
    private boolean isAllCheck() {
        for (ShopInfoBean shopInfoBean : shopInfos) {
            if (!shopInfoBean.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {

        //要删除的sku_ids
//        String sku_ids = "";
//        for (int i = 0; i < toBeDeleteGoods.size(); i++) {
//            String skuid = String.valueOf(toBeDeleteGoods.get(i).skuid);
//            if (i != toBeDeleteGoods.size() - 1) {
//                //如果不是最后一个
//                sku_ids += skuid + ",";
//            } else {
//                sku_ids += skuid;
//            }
//        }
        int sku_id = shopInfos.get(packedPositionGroup).goodsItems.data.get(packedPositionChild).skuid;
        if (seekCartController != null) {
            //判断网络状态
            if (NetWorkUtil.noHaveNet(mContext)) {
                return;
            }
            LoadDialog.show(mContext);
            seekCartController.getDeleteAfterData(mContext, String.valueOf(sku_id));
        }
        calculate();
    }

    @Override
    public void deleteAfterCartData(CartDataBean cartDataBean) {
        LoadDialog.dismiss(mContext);
        //网络请求失败
        if (cartDataBean == null) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.timeout));
            return;
        }
        resetBottomState();
        //网络请求成功
        shopInfos.clear();
        datas.clear();
        //{"order":{"max_largess_qty":0,"detail":"","pay_method":0,"totalprice":0,"goodsNum":0}}
        if (cartDataBean.order.detail != null) {
            shopInfos.addAll(cartDataBean.order.detail.shopInfo);
            for (int i = 0; i < shopInfos.size(); i++) {
                datas.put(String.valueOf(i), shopInfos.get(i).goodsItems.data);
            }
            if (shopGoodsAdapter != null) {
                shopGoodsAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        ShopInfoBean shopInfoBean = shopInfos.get(groupPosition);
        List<DataBean> dataBeans = shopInfoBean.goodsItems.data;
        for (int i = 0; i < dataBeans.size(); i++) {
            if (dataBeans.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            shopInfoBean.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            shopInfoBean.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }
        if (isAllCheck())
            all_selected_btn.setChecked(true);
        else
            all_selected_btn.setChecked(false);
        shopGoodsAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doChanged(int groupPosition, int childPosition, int sku_id, int is_prompt, TextView showCountView, String limit_buy, int goodsNum, boolean isChecked) {
        this.sku_id = sku_id;
        this.is_prompt = is_prompt;
        this.showCountView = showCountView;
        //数量改变
        int limit;
        if (TextUtils.isEmpty(limit_buy)) {
            limit = 0;
        } else {
            limit = Integer.valueOf(limit_buy);
        }
        ChangedGoodsCountWindow changedGoodsCountWindow = new ChangedGoodsCountWindow(getActivity(), limit, goodsNum);
        changedGoodsCountWindow.setOnChangedAfterCount(this);
        changedGoodsCountWindow.showAtLocation(storeFrameLayout, Gravity.BOTTOM, 0, 0);
        calculate();
    }

    @Override
    public void getCount(int count) {
        this.count = count;
        if (seekCartController != null) {
            //判断网络状态
            if (NetWorkUtil.noHaveNet(mContext)) {
                return;
            }
            LoadDialog.show(mContext);
            seekCartController.getUpdateCountData(mContext, String.valueOf(sku_id), String.valueOf(count), is_prompt);
        }
    }

    @Override
    public void updateCountData(CartDataBean cartDataBean) {
        LoadDialog.dismiss(mContext);
        if (cartDataBean == null) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.timeout));
            return;
        }
        resetBottomState();
        shopInfos.clear();
        datas.clear();
        shopInfos.addAll(cartDataBean.order.detail.shopInfo);
        for (int i = 0; i < shopInfos.size(); i++) {
            datas.put(String.valueOf(i), shopInfos.get(i).goodsItems.data);
        }
        if (shopGoodsAdapter != null) {
            shopGoodsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 把结算的价格和数量显示到控件上
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        bigTotalPrice = new BigDecimal(0.00);
        for (int i = 0; i < shopInfos.size(); i++) {
            ShopInfoBean shopInfoBean = shopInfos.get(i);
            List<DataBean> dataBeans = shopInfoBean.goodsItems.data;
            for (int j = 0; j < dataBeans.size(); j++) {
                DataBean dataBean = dataBeans.get(j);
                if (dataBean.isChoosed()) {
                    totalCount += dataBean.num;
                    bigTotalPrice = bigTotalPrice.add(new BigDecimal(Double.toString(dataBean.price)).multiply(new BigDecimal(Double.toString((double) dataBean.num))));
                    bigTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                    totalPrice = bigTotalPrice.doubleValue();
                    CommonUtil.printI("计算后的结果====", bigTotalPrice.toString());
                }
            }
        }
        cart_total_price.setText(String.format(mContext.getResources().getString(R.string.cart_totle_price),String.valueOf(totalPrice)));
        pay_product_count.setText(String.format(mContext.getResources().getString(R.string.cart_goods_selected_count), String.valueOf(totalCount)));
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
                    ToastUtil.showToast(mContext,mContext.getResources().getString(R.string.cart_no_selected_goods));
                    return;
                }
                //获取到要传入支付的数据
                String sku_ids = "";
                for (int i = 0; i < shopInfos.size(); i++) {
                    //遍历所有的商品,拿到被选择的
                    List<DataBean> data = shopInfos.get(i).goodsItems.data;
                    CommonUtil.printI("传递数据前的数据查看==", shopInfos.toString());
                    for (int j = 0; j < data.size(); j++) {
                        if (data.get(j).isChoosed()) {
                                sku_ids += data.get(j).skuid + ",";
                        }
                    }
                }
                sku_ids=sku_ids.substring(0,sku_ids.length()-1);
                CommonUtil.printI("传递的数据sku_ids", sku_ids);
                //点击结算
                Intent payIntent = new Intent(mContext, PayActivity.class);
                payIntent.putExtra("sku_ids", sku_ids);
                payIntent.putExtra("is_prompt", 0);//代表的是不是立即购买
                startActivity(payIntent);
                break;
        }
    }
    //修改数据后，底部栏要重新刷新状态
    private void resetBottomState() {
        if (isAllCheck()){
            all_selected_btn.setChecked(false);
        }
        totalCount=0;
        totalPrice=0.00;
        cart_total_price.setText(String.format(mContext.getResources().getString(R.string.cart_totle_price),String.valueOf(totalPrice)));
        pay_product_count.setText(String.format(mContext.getResources().getString(R.string.cart_goods_selected_count), String.valueOf(totalCount)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(confirmPayReceiver);
    }
}
