package com.winguo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.adapter.ShopRecycleViewAdapter;
import com.winguo.base.BaseTitleActivity;
import com.winguo.net.GlideUtil;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.productList.modle.ItemEntitys;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.bean.ValueEntity;
import com.winguo.productList.view.IProductListView;
import com.winguo.shop.controller.ShopController;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家店铺 及商品列表
 */

public class GoodsShopActivity extends BaseTitleActivity implements IProductListView, View.OnClickListener {
    private FrameLayout product_back_btn;
    private ImageView product_list_search;
    private TextView tv_synthesize;
    private TextView tv_sales_volume;
    private TextView tv_price;
    private View errorView;
    //获取数据的页码数
    private int page = 1;
    //排序方式,默认/价格/销量
    private String orders = "m_item_id";//默认
    //排序,默认是降序
    private String sort = "desc";
    //判断点击的是否还是已经加载的/default/sale/price
    private String isState = "default";
    //还有没有更多数据
    private boolean hasMore = true;
    //是否正在加载更多
    private boolean isLoadingMore = false;
    //返回数据的状态
    private static final int LOADING_STATE = 1;
    private static final int ERROR_STATE = 2;
    private static final int SUCCESS_STATE = 3;
    //返回数据的状态
    private int STATE = LOADING_STATE;
    //存放listView数据源数据集合
    private List<ItemEntitys> productList;
    private View successView;
    private ViewGroup.LayoutParams params;
    private DrawerLayout drawerLayout;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    private LinearLayout ll_right_menu;
    private ImageView iv_price;
    private RelativeLayout ll_default;
    private RelativeLayout ll_sales_volume;
    private RelativeLayout ll_price;
    private List<ValueEntity> values = new ArrayList<>();
    private List<ValueEntity> commonValues = new ArrayList<>();
    private final int SEAARCH_CHANGED = 88;
    private boolean hasMoreNotNet = false;
    private ImageView iv_product_screen_btn;
    private ImageView product_list_shopping_cart;
    private Button btn_request_net;
    private ShopController controller;
    private FrameLayout fl_common_container;
    private String shopId;
    private ShopSimpleBean shopSimpleBean;
    private NestedScrollView shop_no_data;
    private RecyclerView shop_recycle_view;
    private LinearLayoutManager layoutManager;
    private ShopRecycleViewAdapter mAdapter;
    private ImageView iv_shop_logo;
    private TextView tv_shop_name;
    private TextView tv_shop_product_count;
    private TextView tv_shop_collect_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_shop);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initDatas() {
        //获取外部传过来的数据
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        shopSimpleBean = (ShopSimpleBean) intent.getSerializableExtra("shopBean");
    }

    private void initViews() {
        //创建数据源集合
        productList = new ArrayList<>();
        //初始化控件
        initView();
        setShopData();
        //判断网络状态
        showNetState();
        showView();
    }

    /**
     * 根据网络状态显示界面
     */
    private void showNetState() {
        //切换显示状态
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            //初始化回调数据
            //城市的数据获取
            controller = new ShopController(this);
            LoadDialog.show(GoodsShopActivity.this);
            firstLoadingData(orders, sort);
        } else {
            //如果网络和WiFi都没有，就加载无网络
            STATE = ERROR_STATE;
            showView();
        }
    }

    /**
     * 第一次请求网络数据
     *
     * @param sort
     */
    private void firstLoadingData(String orders, String sort) {
        if (controller != null)
            controller.getData(GoodsShopActivity.this, page, orders, sort, shopId);
    }

    //获取网络数据
    @Override
    public void getProductList(ProductListBean productListBeen) {
        //        //调用这个方法要先清除之前的数据
        //        productList.clear();
        //判断是否有数据
        CommonUtil.printI("店铺详情回调函数返回到 结果:=", productListBeen.toString());
        LoadDialog.dismiss(GoodsShopActivity.this);
        if (productListBeen == null) {
            //没有网络
            STATE = ERROR_STATE;
            showView();
            return;
        }
        if (productListBeen.search != null) {
            //有网络
            if (productListBeen.search.products != null) {
                //给listView的数据源数据
                productList.clear();
                productList.addAll(productListBeen.search.products.item);
                //可以给控件赋值
                mAdapter.notifyDataSetChanged();
                //判断是否有更多数据
                int hasMorecount = productListBeen.search.has_more_items.get(0);//集合中的两个值,取其中的任何一个都行
                CommonUtil.printI("还有没有更多的值:+++++=", hasMorecount + "");
                setHasMore(hasMorecount);
                CommonUtil.printI("还有没有更多hasMore:+++++=", hasMore + "");
                //显示view
                STATE = SUCCESS_STATE;
                showView();
                //有数据时显示recycleView
                shop_no_data.setVisibility(View.GONE);
                shop_recycle_view.setVisibility(View.VISIBLE);
            } else {
                //没有数据
                STATE = SUCCESS_STATE;
                showView();
                shop_no_data.setVisibility(View.VISIBLE);
                shop_recycle_view.setVisibility(View.GONE);
            }
        }
    }

    //当加载更多时,获取更多数据返回
    @Override
    public void getMoreProductList(ProductListBean productListBeen) {
        if (productListBeen == null) {
            mAdapter.changeMoreStatus(ShopRecycleViewAdapter.LOADING_ERROR_STATUS);
            hasMoreNotNet = true;
            page -= 1;
            return;
        }
        int hasMorecount = productListBeen.search.has_more_items.get(0);//集合中的两个值,取其中的任何一个都行
        if (hasMore) {
            //没有在加载更多
            isLoadingMore = false;
            //有数据
            productList.addAll(productListBeen.search.products.item);
            mAdapter.notifyDataSetChanged();
        }
        setHasMore(hasMorecount);
    }

    /**
     * 判断是否有更多数据
     *
     * @param hasMorecount
     */
    private void setHasMore(int hasMorecount) {
        if (hasMorecount == 0) {
            //没有更多数据
            hasMore = false;
            mAdapter.changeMoreStatus(ShopRecycleViewAdapter.NO_MORE_STATUS);
        } else if (hasMorecount == 1) {
            //有更多数据
            hasMore = true;
            mAdapter.changeMoreStatus(ShopRecycleViewAdapter.LOADING_STATUS);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        fl_common_container = (FrameLayout) findViewById(R.id.fl_common_container);
        //创建不同的视图
        params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //2、没有网络或请求失败
        errorView = View.inflate(GoodsShopActivity.this, R.layout.no_net, null);
        TextView no_net_tv = (TextView) errorView.findViewById(R.id.no_net_tv);
        btn_request_net = (Button) errorView.findViewById(R.id.btn_request_net);
        Drawable drawableTop1 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop1, null, null);
        fl_common_container.addView(errorView, params);
//        fl_product_container = (FrameLayout) findViewById(R.id.fl_product_container);
        //3、返回数据不为空,再创建成功的界面
        successView = View.inflate(GoodsShopActivity.this, R.layout.goods_shop_layout, null);

        //店铺信息
        iv_shop_logo = (ImageView) successView.findViewById(R.id.iv_shop_logo);
        tv_shop_name = (TextView) successView.findViewById(R.id.tv_shop_name);
        tv_shop_product_count = (TextView) successView.findViewById(R.id.tv_shop_product_count);
        tv_shop_collect_count = (TextView) successView.findViewById(R.id.tv_shop_collect_count);

        //排序
        ll_default = (RelativeLayout) successView.findViewById(R.id.ll_default);
        ll_sales_volume = (RelativeLayout) successView.findViewById(R.id.ll_sales_volume);
        ll_price = (RelativeLayout) successView.findViewById(R.id.ll_price);
        tv_synthesize = (TextView) successView.findViewById(R.id.tv_synthesize);
        tv_sales_volume = (TextView) successView.findViewById(R.id.tv_sales_volume);
        tv_price = (TextView) successView.findViewById(R.id.tv_price);
        iv_price = (ImageView) successView.findViewById(R.id.iv_price);

        shop_no_data = (NestedScrollView) successView.findViewById(R.id.shop_no_data_view);
        shop_recycle_view = (RecyclerView) successView.findViewById(R.id.shop_recycle_view);
        layoutManager = new LinearLayoutManager(GoodsShopActivity.this);
        shop_recycle_view.setLayoutManager(layoutManager);
        mAdapter = new ShopRecycleViewAdapter(this, productList);
        shop_recycle_view.setAdapter(mAdapter);
        fl_common_container.addView(successView, params);
        //默认的选中默认
        tv_synthesize.setTextColor(getResources().getColor(R.color.selected_color));
    }

    private void setShopData() {
        //店铺logo
        if (shopSimpleBean.message.logo.content.length() == 0) {
            //没有上传logo,使用默认的
            iv_shop_logo.setImageResource(R.drawable.shop_default_bg);
        } else {
            GlideUtil.getInstance().loadImage(this, shopSimpleBean.message.logo.content, R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, iv_shop_logo);
        }
        //店铺名
        tv_shop_name.setText(shopSimpleBean.message.name);
        //店铺商品数
        tv_shop_product_count.setText(getString(R.string.tv_shop_product_count, shopSimpleBean.message.itemCount));
        //店铺收藏数
        tv_shop_collect_count.setText(String.valueOf(shopSimpleBean.message.brandCount));
    }

    private void initListener() {
        //顶部购物车的点击事件
//        product_list_shopping_cart.setOnClickListener(this);
        //综合排序
        ll_default.setOnClickListener(this);
        //销量排序
        ll_sales_volume.setOnClickListener(this);
        //价格排序
        ll_price.setOnClickListener(this);

        //无网络点击按钮 刷新并隐藏
        btn_request_net.setOnClickListener(this);

        shop_recycle_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == mAdapter.getItemCount() && hasMore && !isLoadingMore) {
                    isLoadingMore = true;
                    if (NetWorkUtil.noHaveNet(GoodsShopActivity.this)) return;
                    loadMoreData();
                }
            }
        });
        //条目点击事件
        mAdapter.setOnItemClickListener(new ShopRecycleViewAdapter.OnItemClicklistener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mAdapter.getItemViewType(position) == 0) {
                    //这个是itemView部分的点击事件
                    ItemEntitys itemEntity = productList.get(position);
                    Intent productActivity = new Intent(GoodsShopActivity.this, ProductActivity.class);
                    productActivity.putExtra("gid", itemEntity.entity_id);
                    startActivity(productActivity);
                } else if (mAdapter.getItemViewType(position) == 1 && hasMoreNotNet) {
                    //脚部局点击事件
                    hasMoreNotNet = false;
                    mAdapter.changeMoreStatus(ShopRecycleViewAdapter.LOADING_STATUS);
                    if (controller != null)
                        controller.getMoreData(GoodsShopActivity.this, page, orders, sort, shopId);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_net:
                //无网络 点击刷新 并隐藏
                errorView.setVisibility(View.GONE);
                showNetState();
                break;
//            case R.id.product_list_shopping_cart:
////                Intent cartActivity = new Intent(ProductListActivity.this, CartActivity.class);
////                startActivity(cartActivity);
//                Intents.startCart(SPUtils.contains(this, "accountName"), this);
//                break;
            case R.id.ll_default:
                if (!("default".equals(isState))) {
                    isState = "default";
                    tv_synthesize.setTextColor(getResources().getColor(R.color.selected_color));
                    tv_sales_volume.setTextColor(getResources().getColor(R.color.tv_product_list_name_color));
                    tv_price.setTextColor(getResources().getColor(R.color.tv_product_list_name_color));
                    iv_price.setSelected(false);
                    iv_price.setImageResource(R.drawable.press_default);
                    sort = "desc";
                    //综合排序
//                    STATE = LOADING_STATE;
//                    showView();
                    productList.clear();
                    mAdapter.notifyDataSetChanged();
                    page = 1;
                    orders = "m_item_id";
                    LoadDialog.show(GoodsShopActivity.this);
                    firstLoadingData(orders, sort);
                }
                break;
            case R.id.ll_sales_volume:
                if (!("sale".equals(isState))) {
                    isState = "sale";
                    tv_synthesize.setTextColor(getResources().getColor(R.color.tv_product_list_name_color));
                    tv_sales_volume.setTextColor(getResources().getColor(R.color.selected_color));
                    tv_price.setTextColor(getResources().getColor(R.color.tv_product_list_name_color));
                    iv_price.setSelected(false);
                    iv_price.setImageResource(R.drawable.press_default);
                    sort = "desc";
                    //销量排序
//                    STATE = LOADING_STATE;
//                    showView();
                    productList.clear();
                    mAdapter.notifyDataSetChanged();
                    page = 1;
                    orders = "sale_qty";
                    LoadDialog.show(GoodsShopActivity.this);
                    firstLoadingData(orders, sort);
                }
                break;
            case R.id.ll_price:
                isState = "price";
                tv_synthesize.setTextColor(getResources().getColor(R.color.tv_product_list_name_color));
                tv_sales_volume.setTextColor(getResources().getColor(R.color.tv_product_list_name_color));
                tv_price.setTextColor(getResources().getColor(R.color.selected_color));
                iv_price.setSelected(true);
                //价格排序
//                STATE = LOADING_STATE;
//                showView();
                productList.clear();
                mAdapter.notifyDataSetChanged();
                page = 1;
                orders = "price";
                if ("desc".equals(sort)) {
                    sort = "asc";
                    iv_price.setImageResource(R.drawable.press_asc_2);
                } else {
                    sort = "desc";
                    iv_price.setImageResource(R.drawable.press_desc_2);
                }
                LoadDialog.show(GoodsShopActivity.this);
                firstLoadingData(orders, sort);
                break;
        }
    }

    /**
     * 根据返回数据改变显示的view
     */
    private void showView() {

        if (errorView != null) {
            errorView.setVisibility(STATE == ERROR_STATE ? View.VISIBLE : View.GONE);
        }
        if (successView != null) {
            successView.setVisibility(STATE == SUCCESS_STATE ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 获取更多数据
     */
    private void loadMoreData() {
        page += 1;
        if (controller != null)
            controller.getMoreData(GoodsShopActivity.this, page, orders, sort, shopId);
    }

    @Override
    public void getProvinceCity(ProvinceCityBean provinceCityBean) {
    }

    @Override
    public void getClassifyName(ClassifyName classifyName) {
    }

}
