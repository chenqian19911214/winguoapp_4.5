package com.winguo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guobi.account.GBAccountInfo;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.adapter.NearbyHomeAdapter;
import com.winguo.adapter.NearbyStoreHomeAdapter;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.CartSectionType;
import com.winguo.bean.CartShopList;
import com.winguo.bean.SectionType;
import com.winguo.bean.SectionType2;
import com.winguo.bean.StoreDetail;
import com.winguo.bean.StoreDetail2;
import com.winguo.bean.StoreShop;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.login.LoginActivity;
import com.winguo.net.GlideUtil;
import com.winguo.utils.CalculateUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NearbyStoreUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.NearbyStoreCartPopWindow;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体店 店铺 首页
 * Created by admin on 2017/6/2.
 */

public class NearbyStoreHomeActivity extends BaseTitleActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, NearbyStoreHomeAdapter.OnAddOrDelClickListener, NearbyStoreUtil.IRequestStoreDetail, NearbyStoreUtil.IRequestStoreShopList, Runnable, NearbyStoreUtil.IRequestAddCart, NearbyStoreUtil.IRequestCartList, NearbyStoreUtil.IRequestStoreDetail2, PopupWindow.OnDismissListener {

    private RecyclerView nearby_store_home_rv;
    private FrameLayout nearby_store_home_cart_fl;
    private TextView nearby_store_home_cart_number;
    private TextView nearby_store_home_bt_price;
    private TextView nearby_store_home_bt_pay;
    private View nearby_home_bt_cart_view;
    private FrameLayout container;
    private Button btn_request_net;
    private View headView;
    private View noShopData;
    private NearbyStoreHomeAdapter pullToRefreshAdapter;
    private ImageView nearby_store_home_head_item_ic;
    private TextView nearby_store_home_head_item_title;
    private TextView nearby_store_home_head_item_grade;
    private TextView nearby_store_home_head_item_type;
    private TextView nearby_store_home_head_item_price;
    private TextView nearby_store_home_head_item_distance;
    private View noNetView;
    private String store_id;
    private double log,lat;
    private String uid;
    private String make_id;
    private boolean isCanBuy;
    private String distance;
    private NearbyHomeAdapter adapter;
    private CartPayBroadReceiver receiver;
    private boolean isFromPhyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_store_home);
        setBackBtn();
        initView();
        requestData();
        setListener();

    }

    @Override
    protected void backEvent() {
        super.backEvent();
        if (isFromPhyCart){
            //返回时刷新购物车数据
            Intent receiverIntent = new Intent();
            receiverIntent.setAction("com.winguo.physicalconfirmpay.success");
            sendBroadcast(receiverIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFromPhyCart){
            //返回时刷新购物车数据
            Intent receiverIntent = new Intent();
            receiverIntent.setAction("com.winguo.physicalconfirmpay.success");
            sendBroadcast(receiverIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GBAccountInfo accountInfo = GBAccountMgr.getInstance().getAccountInfo();
        if (accountInfo != null) {
            WinguoAccountGeneral winguoGeneral = accountInfo.winguoGeneral;
            if (winguoGeneral != null) {
                uid = winguoGeneral.id;
            }
        }
        GBLogUtils.DEBUG_DISPLAY("onStart",""+uid);

    }

    private void initView() {
        receiver = new CartPayBroadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.winguo.physicalconfirmpay.success");
        registerReceiver(receiver, intentFilter);

        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id");
        log = intent.getDoubleExtra("log",0.00d);
        lat = intent.getDoubleExtra("lat",0.00d);
        isCanBuy = intent.getBooleanExtra("isCanBuy", true);
        isFromPhyCart = intent.getBooleanExtra("isFromPhyCart", false);
        if (!isCanBuy) {
            distance = intent.getStringExtra("distance");
        }
        GBLogUtils.DEBUG_DISPLAY("uid", "" + uid + ": " + isCanBuy + ": " + store_id);
        container = (FrameLayout) findViewById(R.id.nearby_store_home_content_container);
        //无网络
        //(给容器添加布局)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        noNetView.setVisibility(View.GONE);
        container.addView(noNetView, params);
        //该店铺暂无商品
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params2.gravity = Gravity.CENTER;
        params2.setMargins(0, 140, 0, 0);
        noShopData = View.inflate(this, R.layout.no_shop, null);
        TextView no_shop_tv = (TextView) noShopData.findViewById(R.id.no_shop_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        no_shop_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        noShopData.setVisibility(View.GONE);
        container.addView(noShopData, params2);
        //初始化商品列表RecyclerView
        nearby_store_home_rv = (RecyclerView) findViewById(R.id.nearby_store_home_rv);
        nearby_store_home_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        nearby_store_home_rv.setItemAnimator(new DefaultItemAnimator());
        nearby_store_home_rv.addItemDecoration(new SpacesItemDecoration(3));
        ViewGroup parent = (ViewGroup) nearby_store_home_rv.getParent();
        headView = getLayoutInflater().inflate(R.layout.nearby_store_home_head_item, parent, false);

        //初始化头控件
        nearby_store_home_head_item_ic = (ImageView) headView.findViewById(R.id.nearby_store_home_head_item_ic);
        nearby_store_home_head_item_title = (TextView) headView.findViewById(R.id.nearby_store_home_head_item_title);
        nearby_store_home_head_item_grade = (TextView) headView.findViewById(R.id.nearby_store_home_head_item_grade);
        nearby_store_home_head_item_type = (TextView) headView.findViewById(R.id.nearby_store_home_head_item_type);
        nearby_store_home_head_item_price = (TextView) headView.findViewById(R.id.nearby_store_home_head_item_price);
        nearby_store_home_head_item_distance = (TextView) headView.findViewById(R.id.nearby_store_home_head_item_distance);

        //底部购物车view
        nearby_home_bt_cart_view = findViewById(R.id.nearby_home_bt_cart_view);
        nearby_store_home_cart_fl = (FrameLayout) findViewById(R.id.nearby_store_home_cart_fl);
        nearby_store_home_cart_number = (TextView) findViewById(R.id.nearby_store_home_cart_number);
        nearby_store_home_bt_price = (TextView) findViewById(R.id.nearby_store_home_bt_price);
        nearby_store_home_bt_pay = (TextView) findViewById(R.id.nearby_store_home_bt_pay);


    }

    private void requestData() {
        LoadDialog.show(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (store_id != null) {
                if (isCanBuy) {
                    NearbyStoreUtil.requestStoreDetail(NearbyStoreHomeActivity.this, store_id, log, lat, this);
                } else {
                    NearbyStoreUtil.requestStoreDetail2(this, store_id, this);
                }
            }
        } else {
            LoadDialog.dismiss(this);
            noNetView.setVisibility(View.VISIBLE);
        }
    }


    private void setListener() {
        nearby_store_home_cart_fl.setOnClickListener(this);
        btn_request_net.setOnClickListener(this);
        nearby_store_home_bt_pay.setOnClickListener(this);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/6/6  头部实体店 点击获取详情界面
                if (isCanBuy) {
                    Intent it = new Intent(NearbyStoreHomeActivity.this, StoreDetailActivity.class);
                    it.putExtra("store_id", store_id);
                    it.putExtra("log", log);
                    it.putExtra("lat", lat);
                    startActivity(it);
                } else {
                    ToastUtil.showToast(NearbyStoreHomeActivity.this, "暂无数据");
                }
            }
        });


    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        pullToRefreshAdapter = new NearbyStoreHomeAdapter(this, R.layout.nearby_store_home_shop_item, R.layout.def_section_head, data, uid, store_id);
        pullToRefreshAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);
        CustomLoadMoreView loadMoreView = new CustomLoadMoreView();
        pullToRefreshAdapter.setLoadMoreView(loadMoreView);
        nearby_store_home_rv.setAdapter(pullToRefreshAdapter);
        pullToRefreshAdapter.addHeaderView(headView);
        pullToRefreshAdapter.setOnLoadMoreListener(this, nearby_store_home_rv);
        pullToRefreshAdapter.setOnItemClickListener(this);
        pullToRefreshAdapter.setOnAddOrDelClickListener(this);

    }

    /**
     * 购物车列表
     */
    private List<CartSectionType> tempCart = new ArrayList<>();
    private boolean isFirst;

    @Override
    public void cartList(List<CartShopList.ContentBean.ItemBean> item) {
        Log.e("cartList","item:"+item);
        LoadDialog.dismiss(NearbyStoreHomeActivity.this);
        if (item != null) {
            tempCart.clear();
            double total_price = 0.00;
            int total_num = 0;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < item.size(); i++) {
                CartShopList.ContentBean.ItemBean itemBean = item.get(i);
                CartSectionType c = new CartSectionType(itemBean);
                c.setAdd(true);
                tempCart.add(c);
                if (i == (item.size() - 1)) {
                    sb.append(itemBean.getId());
                } else {
                    sb.append(itemBean.getId()).append(",");
                }
                total_num += Double.valueOf(itemBean.getNum());
                total_price = new BigDecimal(total_price).add(new BigDecimal(itemBean.getNum_price())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            if (isFirst) {

                NearbyStoreCartPopWindow cartPopWindow = new NearbyStoreCartPopWindow(this, tempCart, pullToRefreshAdapter, uid, make_id);
                cartPopWindow.showAtLocation(container, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                nearby_store_home_cart_fl.setClickable(true);
                cartPopWindow.setOnDismissListener(this);

            } else {
                shopNumber = total_num;
                if (shopNumber > 0) {
                    nearby_store_home_cart_number.setVisibility(View.VISIBLE);
                    nearby_store_home_cart_number.setText(shopNumber + "");
                    nearby_store_home_bt_price.setText(total_price + "");
                }
                cartIdStr = sb.toString();
                isFirst = true;
            }

        }
    }

    @Override
    public void cartErrorMsg(String error) {
       // nearby_store_home_cart_fl.setClickable(true);
        ToastUtil.showToast(this, error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby_store_home_cart_fl:
                // TODO: 2017/6/6 弹窗 展示添加商品列表
                if (uid != null) {
                    if (shopNumber != 0) {
                        if (NetWorkUtil.isNetworkAvailable(this)) {
                            nearby_store_home_cart_fl.setClickable(false);
                            NearbyStoreUtil.requestShopListCart(this, uid, make_id, this);
                        } else {
                            ToastUtil.showToast(this, getString(R.string.timeout));
                        }
                    } else {
                        ToastUtil.showToast(this, "你还没添加商品 -_-");
                    }
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.NEARBY_STORE_HOME_CART);
                    it.putExtra("storeid", store_id);
                    startActivity(it);
                }


                break;
            case R.id.nearby_store_home_bt_pay:
                // TODO: 2017/6/6 结算
               /* StringBuffer sb = new StringBuffer();
                List<CartSectionType> datas = adapter.getDatas();
                for (int i = 0; i < datas.size(); i++) {
                    CartSectionType item = datas.get(i);
                    if (i == (datas.size() - 1)) {
                        sb.append(item.t.getId());
                    } else {
                        sb.append(item.t.getId()).append(",");
                    }
                }

                // ToastUtil.showToast(this,"结算");*/
              //  GBLogUtils.DEBUG_DISPLAY("cartIds", cartIdStr);
                if (cartIdStr != null) {
                    if (!TextUtils.isEmpty(cartIdStr)) {
                        Intent it = new Intent(this, PhysicalPayActivity.class);
                        it.putExtra("cart_ids", cartIdStr);
                        startActivity(it);
                    } else {
                        ToastUtil.showToast(this, "你还没添加商品 -_-");
                    }
                }

                break;
            case R.id.btn_request_net:
                // TODO: 2017/6/6 无网络 点击刷新
                noNetView.setVisibility(View.GONE);
                requestData();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        // TODO: 2017/6/6 加载更多
        if (adapter != null) {
            if (allNum < 3){
                adapter.loadMoreEnd(true);
                return;
            }
            adapter.loadMoreEnd();
        }
        if (pullToRefreshAdapter != null) {

            if (allNum < 3){
                pullToRefreshAdapter.loadMoreEnd(true);
                return;
            }
            pullToRefreshAdapter.loadMoreLoading();
            //延迟1s加载更多
            new Handler().postDelayed(this, 1000);
        }

    }

    /**
     * 记录当前点击商品的位置
     */
    private int currShopPosition = 0; //

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        // TODO: 2017/6/6 条目点击事件 标题也算条目
        List<SectionType> data = pullToRefreshAdapter.getData();
        SectionType sectionType = data.get(position);
        if (!sectionType.isHeader) {
            currShopPosition = position;
            Intent it = new Intent(NearbyStoreHomeActivity.this, ShopDetailActivity.class);
            it.putExtra("store_id", store_id);
            it.putExtra("log", log);
            it.putExtra("lat", lat);
            it.putExtra("shop_id", sectionType.t.getM_entity_item_m_item_id());
            startActivityForResult(it, 23);
        }
    }


    private int shopNumber = 0;

    @Override
    public void addNumber(int number, double price, String sku_id, String productid, List<SectionType> sectionType) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this, true);
            NearbyStoreUtil.requestAddCart(this, number + "", productid, uid, sku_id, this, price, sectionType,make_id);
        } else {
            ToastUtil.showToast(this, getString(R.string.timeout));
        }
    }

    @Override
    public void addCart(String key,List<CartShopList.ContentBean.ItemBean> item, double price, List<SectionType> sectionType) {
        isFirst = true;
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, key);
        if (sectionType != null) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i <item.size() ; i++) {
                CartShopList.ContentBean.ItemBean itemBean = item.get(i);
                if (i == (item.size() - 1)) {
                    sb.append(itemBean.getId());
                } else {
                    sb.append(itemBean.getId()).append(",");
                }
            }
            cartIdStr = sb.toString();
            double total_price = Double.valueOf(nearby_store_home_bt_price.getText().toString());
            shopNumber++;
            nearby_store_home_cart_number.setVisibility(View.VISIBLE);
            nearby_store_home_cart_number.setText(shopNumber + "");
            nearby_store_home_bt_price.setText(CalculateUtil.sum(total_price, price) + "");
        }


    }

    @Override
    public void addCartErrorMsg(String error) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, error);
    }


    /**
     * 商品列表 删除商品 -->改变
     * @param number
     * @param price
     */
    @Override
    public void delNumber(int number, double price) {
        double total_price = Double.valueOf(nearby_store_home_bt_price.getText().toString());
        shopNumber--;
        if (shopNumber == 0) {
            nearby_store_home_cart_number.setVisibility(View.GONE);
        }
        nearby_store_home_cart_number.setText(shopNumber + "");
        nearby_store_home_bt_price.setText(CalculateUtil.sub(total_price, price) + "");

    }

    private String cartIdStr = "";

    public void addNumberByCart(int position, int number, double price, StringBuffer cartIds) {
        // String total_price = nearby_store_home_bt_price.getText().toString();
        shopNumber++;
        nearby_store_home_cart_number.setVisibility(View.VISIBLE);
        nearby_store_home_cart_number.setText(number + "");
        nearby_store_home_bt_price.setText(price + "");
        cartIdStr = cartIds.toString();
        // BigDecimal price1=new BigDecimal(total_price);
        // BigDecimal price2=new BigDecimal(price);
        //pullToRefreshAdapter.modifyNumber(position,number);
    }

    public void delNumberByCart(int position, int number, double price, StringBuffer cartIds) {
        // String total_price = nearby_store_home_bt_price.getText().toString();
        shopNumber--;
        if (number == 0) {
            nearby_store_home_cart_number.setVisibility(View.GONE);
        }
        nearby_store_home_cart_number.setText(shopNumber + "");
        nearby_store_home_bt_price.setText(price + "");
        if (cartIds!=null)
            cartIdStr = cartIds.toString();
        // BigDecimal price1=new BigDecimal(total_price);
        //BigDecimal price2=new BigDecimal(price);
        //nearby_store_home_bt_price.setText(String.valueOf(price1.subtract(price2).setScale(2, BigDecimal.ROUND_HALF_UP).intValue()));
        //pullToRefreshAdapter.modifyNumber(position,number);
    }

    /**
     * 返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String cartids = data.getStringExtra("cartids");
                    int total_num = data.getIntExtra("total_num", 0);
                    double total_price = data.getDoubleExtra("total_price", 0.00);

                    GBLogUtils.DEBUG_DISPLAY("cartidsss",total_num+""+total_price+""+cartids);
                    isFirst = true;
                    cartIdStr = cartids;
                    shopNumber = total_num;
                    nearby_store_home_cart_number.setVisibility(View.VISIBLE);
                    nearby_store_home_cart_number.setText(shopNumber + "");
                    nearby_store_home_bt_price.setText(total_price+ "");

                    // pullToRefreshAdapter.addShopByShopDetail(sectionType, currShopPosition);

                    // sectionType.t = oldSectionType.t;
                   /* if (oldSectionType.isAdd()) {
                       // pullToRefreshAdapter.modifyShopByShopDetail(sectionType,currShopPosition);
                        double total_price = Double.valueOf(nearby_store_home_bt_price.getText().toString());
                        double oldPrice = oldSectionType.getSelectPrice();
                        double newPrice = sectionType.getSelectPrice();
                        nearby_store_home_bt_price.setText((total_price+newPrice-oldPrice)+"");

                    } else {
                        pullToRefreshAdapter.addShopByShopDetail(sectionType,currShopPosition);
                    }*/

                }
            }
        }
    }

    private int page = 1;

    /**
     * 实体店详情 请求结果
     *
     * @param storeDetail
     */
    @Override
    public void storeDetail(StoreDetail storeDetail) {
        if (storeDetail != null) {
            initStoreDetail(storeDetail);
            // TODO: 2017/6/22 请求实体店商品
            NearbyStoreUtil.requestStoreShopList(this, store_id, "4", page, this);
            //获取购物车是否添加的有该店铺的商品
            if (uid != null)
                NearbyStoreUtil.requestShopListCart(this, uid, make_id, this);
        } else {
            LoadDialog.dismiss(this);
            ToastUtil.showToast(this, "没有找到该店铺");
            finish();
        }

    }

    private void initStoreDetail(StoreDetail storeDetail) {
        List<StoreDetail.ResultBean> result = storeDetail.getResult();
        StoreDetail.ResultBean resultBean = result.get(0);
        String thumb_url = resultBean.getThumbView();
        if (TextUtils.isEmpty(thumb_url)) {
            nearby_store_home_head_item_ic.setImageResource(R.mipmap.ic_launcher);
        } else {
            GlideUtil.getInstance().loadImage(NearbyStoreHomeActivity.this, thumb_url, R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, nearby_store_home_head_item_ic);
        }
        nearby_store_home_head_item_title.setText(resultBean.getM_maker_name_ch());
        nearby_store_home_head_item_type.setText(resultBean.getTradeName());
        nearby_store_home_head_item_price.setText("¥" + resultBean.getM_entity_maker_average_consumption() + "/人");
        setStoreDistance(resultBean.getDistance() + "");
        make_id = resultBean.getM_maker_id();
    }

    /**
     * 设置距离
     *
     * @param distance
     */
    private void setStoreDistance(String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            double dis = Double.valueOf(distance);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format(dis / 1000);
                nearby_store_home_head_item_distance.setText(format + "km");
            } else {
                nearby_store_home_head_item_distance.setText(dis + "m");
            }
        }

    }

    @Override
    public void storeDetailErrorMsg(String error) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(this, getString(R.string.no_net_or_service_no));
    }

    private List<SectionType> temp = new ArrayList<SectionType>();

    /**
     * 实体店商品列表
     *
     * @param storeShop
     */
    @Override
    public void storeShopList(StoreShop storeShop) {
        LoadDialog.dismiss(this);
        if (storeShop != null) {
            if (page == 1) {
                initStoreShopList(storeShop);
            } else {

                if (pullToRefreshAdapter.isLoadMoreEnable()) {
                    if (!temp.isEmpty())
                        temp.clear();
                    List<StoreShop.ResultBean> result = storeShop.getResult();
                    for (StoreShop.ResultBean item : result) {
                        temp.add(new SectionType(item));
                    }
                    pullToRefreshAdapter.addData(temp);
                    pullToRefreshAdapter.loadMoreComplete();
                }
            }
        } else {

            if (page == 1) {
                    //该实体店 没有商品
                initAdapter();
                noShopData.setVisibility(View.VISIBLE);
            } else {
                //请求失败，没有更多商品
                pullToRefreshAdapter.loadMoreEnd();
            }

        }
    }

    /**
     * 初始化 实体店 商品列表
     *
     * @param storeShop
     */
    private void initStoreShopList(StoreShop storeShop) {

        List<StoreShop.ResultBean> result = storeShop.getResult();
        List<StoreShop.ResultBean> recommend = storeShop.getRecommend();
        if (recommend != null) {
            if (!recommend.isEmpty()) {
                data.add(new SectionType(true, "商家推荐", false));
                for (StoreShop.ResultBean item : recommend) {
                    data.add(new SectionType(item));
                }
            }
        }
        if (result != null) {
            if (!result.isEmpty()) {
                data.add(new SectionType(true, "全部商品", false));
                for (StoreShop.ResultBean item : result) {
                    data.add(new SectionType(item));
                }
            }
            allNum = result.size();
        }
        initAdapter();
    }
    private int allNum = 0;
    private List<SectionType> data = new ArrayList<>();

    @Override
    public void storeShopListErrorMsg(String error) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(this, getString(R.string.no_net_or_service_no));
    }


    /**
     * 延迟1s 加载更多
     */
    @Override
    public void run() {
        if (pullToRefreshAdapter != null) {
            if (pullToRefreshAdapter.isLoadMoreEnable()) {
                page++;
                NearbyStoreUtil.requestStoreShopList(NearbyStoreHomeActivity.this, store_id, "4", page, this);
            }
        }

    }

    @Override
    public void storeDetail2(StoreDetail2 storeDetail) {
        LoadDialog.dismiss(this);
        if (storeDetail != null) {
            fillData(storeDetail);
        } else {
            ToastUtil.showToast(this, "没有找到该店铺");
            finish();
        }
    }

    private void fillData(StoreDetail2 storeDetail) {
        StoreDetail2.ResultBean result = storeDetail.getResult();
        String thumb_url = result.getLogo_url();
        if (TextUtils.isEmpty(thumb_url)) {
            nearby_store_home_head_item_ic.setImageResource(R.mipmap.ic_launcher);
        } else {
            GlideUtil.getInstance().loadImage(NearbyStoreHomeActivity.this, thumb_url, R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, nearby_store_home_head_item_ic);
        }
        nearby_store_home_head_item_title.setText(result.getName());
        nearby_store_home_head_item_type.setText("未知类别");
        nearby_store_home_head_item_price.setText("¥" + result.getConsumption() + "/人");
        setDistance2(distance + "");
        List<StoreDetail2.ResultBean.ItemsBean> items = result.getItems();
        initAdapter2(items);
    }

    /**
     * 爬虫数据 填充
     *
     * @param items
     */
    private void initAdapter2(List<StoreDetail2.ResultBean.ItemsBean> items) {
        List<SectionType2> d = new ArrayList<>();
        if (items != null) {
            allNum = items.size();
            d.add(new SectionType2(true, "全部商品", false));
            for (StoreDetail2.ResultBean.ItemsBean it : items) {
                d.add(new SectionType2(it));
            }
            adapter = new NearbyHomeAdapter(R.layout.nearby_store_home_shop_item, R.layout.def_section_head, d);
            adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            // pullToRefreshAdapter.setAutoLoadMoreSize(3);
            CustomLoadMoreView loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
            nearby_store_home_rv.setAdapter(adapter);

            adapter.addHeaderView(headView);
            adapter.setOnLoadMoreListener(this, nearby_store_home_rv);

        } else {

            adapter = new NearbyHomeAdapter(R.layout.nearby_store_home_shop_item, R.layout.def_section_head, d);
            adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            // pullToRefreshAdapter.setAutoLoadMoreSize(3);
            CustomLoadMoreView loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
            nearby_store_home_rv.setAdapter(adapter);

            adapter.addHeaderView(headView);
            adapter.setOnLoadMoreListener(this, nearby_store_home_rv);
            noShopData.setVisibility(View.VISIBLE);
        }
    }

    private void setDistance2(String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            String substring = distance;
            if (distance.contains("m")) {
                substring = distance.substring(0, distance.length() - 2);
            }
            double dis = Double.valueOf(substring);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format(dis / 1000);
                nearby_store_home_head_item_distance.setText(String.format(this.getString(R.string.shop_distance), format + "km"));
            } else {
                nearby_store_home_head_item_distance.setText(String.format(this.getString(R.string.shop_distance), dis + "m"));
            }
        }
    }

    @Override
    public void storeDetailErrorMsg2(String error) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(this, getString(R.string.no_net_or_service_no));
    }

    @Override
    public void onDismiss() {
        //购物车弹窗关闭 恢复购物车点击事件
        nearby_store_home_cart_fl.setClickable(true);
    }

    /**
     * 权限检测接收广播
     */
    public class CartPayBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.winguo.physicalconfirmpay.success".equals(action)) {
                //跳转结算页面 清除该店铺下 购物车数据
                shopNumber = 0;
                nearby_store_home_cart_number.setVisibility(View.GONE);
                nearby_store_home_cart_number.setText(shopNumber + "");
                nearby_store_home_bt_price.setText(0.00 + "");
                cartIdStr = "";

            }

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
