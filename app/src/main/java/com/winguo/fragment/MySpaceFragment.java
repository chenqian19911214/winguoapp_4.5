package com.winguo.fragment;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.gblocation.GBDLocation;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;
import com.guobi.gfc.gbmiscutils.thread.RunnableBus;
import com.winguo.R;
import com.winguo.adapter.MySpaceShopAdapter;
import com.winguo.adapter.MySpaceStoreAdapter;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseFragment;
import com.winguo.bean.StoreDetail;
import com.winguo.bean.StoreShop;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.login.LoginActivity;
import com.winguo.net.GlideUtil;
import com.winguo.product.controller.ProductController;
import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.bean.ProductEntity;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.product.modle.bean.CollectBean;
import com.winguo.product.view.IProductView;
import com.winguo.productList.modle.ItemEntitys;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.view.IProductListView;
import com.winguo.shop.controller.ShopController;
import com.winguo.utils.BitmapUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NearbyStoreUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WebViewQRUtils;
import com.winguo.view.CustomWebView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 我的空间
 * Created by admin on 2017/6/29.
 */

@Deprecated
public class MySpaceFragment extends BaseFragment implements CustomWebView.LongClickCallBack, BaseQuickAdapter.RequestLoadMoreListener, NearbyStoreUtil.IRequestStoreDetail, NearbyStoreUtil.IRequestStoreShopList, IProductView, IProductListView, Runnable {

    private View personWebView;
    private View storeView;
    private View shopView;
    private View noNetView;
    private Button btn_request_net;
    private String maker_id,uid;
    private CustomWebView webView;
    private ProgressBar mProgress;
    private FrameLayout webContainer;
    private String shopAddrUrl;
    private LinearLayout myspace_store_headview;
    private RelativeLayout myspace_store_container;
    private RecyclerView recommShopRV;
    private RecyclerView allShopRV;
    private TextView nearby_store_home_head_item_distance;
    private TextView nearby_store_home_head_item_price;
    private TextView nearby_store_home_head_item_type;
    private TextView nearby_store_home_head_item_title;
    private ImageView nearby_store_home_head_item_ic;
    private MySpaceStoreAdapter allAdapter;
    private View noShopData;
    private List<StoreShop.ResultBean> recommend = new ArrayList<>();
    private List<StoreShop.ResultBean> allShop = new ArrayList<>();
    private List<ItemEntitys> shopData = new ArrayList<>();
    private RecyclerView myspace_shop_rv;
    private View myspace_shop_head_layout;
    private TextView tv_shop_collect_count;
    private TextView tv_shop_product_count;
    private TextView tv_shop_name;
    private ImageView iv_shop_logo;
    private ProductController shopDetail;
    private ShopController shopController;

    //获取数据的页码数
    private int page = 1;
    //排序方式,默认/价格/销量
    private String orders = "m_item_id";//默认
    //排序,默认是降序
    private String sort = "desc";
    private MySpaceShopAdapter shopAdapter;
    private TextView myspace_store_recommend_shop;
    private TextView myspace_store_all_shop;
    private MyBroadcastReceiver receiver;
    private FrameLayout my_space_container;
    private MySpaceStoreAdapter recommAdapter;
    private SwipeRefreshLayout myspace_store_head_refresh;
    private SwipeRefreshLayout myspace_shop_refresh;
    private View noLoginView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_space;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //注册广播 获取登陆后用户账号 头像等
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGIN_SUCCESS);
        intentFilter.addAction(Constants.QUIT_SUCCESS);
        receiver = new MyBroadcastReceiver();
        context.registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void initData() {
        WinguoAccountGeneral winguoGeneral = GBAccountMgr.getInstance().getAccountInfo().winguoGeneral;
        if (winguoGeneral != null) {
            uid = winguoGeneral.id;
            maker_id = winguoGeneral.maker_id;
            shopAddrUrl = winguoGeneral.mobileShopAddr;
            GBLogUtils.DEBUG_DISPLAY("shopAddrUrl", "shopAddrUrl" + shopAddrUrl);
            String maker_shop_type = winguoGeneral.maker_shop_type;

            //   店铺类型 0旗舰店 1专卖店 2 专营店 3本地服务 4实体店
            if ("0".equals(maker_id)) {  //个人
                personWebView = mInflater.inflate(R.layout.search_ser_layout, null);
            } else {
                if ("4".equals(maker_shop_type)) {
                    //实体店
                    storeView = mInflater.inflate(R.layout.myspace_store_fragment, null);
                } else {
                    // 除4外  默认企业
                    shopView = mInflater.inflate(R.layout.myspace_shop_fragment, null);
                }

            }
        } else {
            //未登录
            noLoginView = mInflater.inflate(R.layout.no_login_myspace, null);
        }

    }

    @Override
    protected void initView(View view) {
        my_space_container = view.findViewById(R.id.my_space_container);
        //无网络
        //(给容器添加布局)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        if (noNetView == null) {
            noNetView = View.inflate(mContext, R.layout.no_net, null);
            btn_request_net = noNetView.findViewById(R.id.btn_request_net);
            TextView no_net_tv = noNetView.findViewById(R.id.no_net_tv);
            Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
            no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
            noNetView.setVisibility(View.GONE);
            //my_space_container.addView(noNetView, params);
        }


        //该店铺暂无商品
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params2.gravity = Gravity.CENTER;
        params2.setMargins(0,160,0,0);
        noShopData = View.inflate(context, R.layout.no_shop, null);
        TextView no_shop_tv = noShopData.findViewById(R.id.no_shop_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        no_shop_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        noShopData.setVisibility(View.GONE);
        my_space_container.addView(noShopData, params2);

        if (noLoginView != null) {
            FrameLayout no_login_myspace_fl = noLoginView.findViewById(R.id.no_login_myspace_fl);
            int screenWidth = ScreenUtil.getScreenWidth(context);
            int screenHeight = ScreenUtil.getScreenHeight(context);
            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.winguo_no_login_bg, screenWidth, screenHeight);
            no_login_myspace_fl.setBackgroundDrawable(BitmapUtil.bitMapToDrawable(bitmap));

            no_login_myspace_fl.setBackgroundResource(R.drawable.winguo_no_login_bg);
            TextView click_login = noLoginView.findViewById(R.id.click_login);
            click_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, LoginActivity.class);
                    it.putExtra("isMySpace",true);
                    startActivity(it);
                }
            });
            my_space_container.addView(noLoginView);
        }

        if (personWebView != null) {
            initWeb();
            my_space_container.removeView(noNetView);
            my_space_container.removeView(personWebView);
            my_space_container.removeView(shopView);
            my_space_container.removeView(storeView);

            my_space_container.addView(personWebView);
            my_space_container.addView(noNetView, params);
            webView.loadUrl(shopAddrUrl);
           // webView.loadUrl("http://atao.m.winguo.com");
        }
        if (storeView!=null) {
            no_shop_tv.setText(getString(R.string.store_no_data_message));
            initStoreView();
            my_space_container.removeView(noNetView);
            my_space_container.removeView(personWebView);
            my_space_container.removeView(shopView);
            my_space_container.removeView(storeView);

            my_space_container.addView(storeView);
            my_space_container.addView(noNetView, params);
            requestStoreData();
        }
        if (shopView!=null) {
            initShopView();
            my_space_container.removeView(noNetView);
            my_space_container.removeView(personWebView);
            my_space_container.removeView(shopView);
            my_space_container.removeView(storeView);

            my_space_container.addView(shopView);
            my_space_container.addView(noNetView, params);
            shopDetail = new ProductController(this);
            shopController = new ShopController(this);
            requestShopData();
        }

    }


    private void requestShopData() {
        if(!isHidden())
            LoadDialog.show(mContext,true);
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
           //shopDetail.getShopData(maker_id);
           shopDetail.getShopData(maker_id);
        } else {
            LoadDialog.dismiss(mContext);
            shopView.setVisibility(View.GONE);
            noNetView.setVisibility(View.VISIBLE);
            myspace_shop_refresh.setRefreshing(false);
        }
    }


    @Override
    public void obtainProductData(ProductEntity productEntity) {}

    /**
     * 新版商品详情
     * @param goodDetail
     */
    @Override
    public void obtainProductDetail(GoodDetail goodDetail) {}

    @Override
    public void obtainShopData(ShopSimpleBean shopSimpleBean) {
        if (shopSimpleBean != null) {
            //店铺logo
            if (shopSimpleBean.message.logo.content.length() == 0) {
                //没有上传logo,使用默认的
                iv_shop_logo.setImageResource(R.drawable.shop_default_bg);
            } else {
                GlideUtil.getInstance().loadImage(mContext, shopSimpleBean.message.logo.content, R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, iv_shop_logo);
            }
            //店铺名
            tv_shop_name.setText(shopSimpleBean.message.name);
            //店铺商品数
            tv_shop_product_count.setText(getString(R.string.tv_shop_product_count, shopSimpleBean.message.itemCount));
            //店铺收藏数
            tv_shop_collect_count.setText(String.valueOf(shopSimpleBean.message.brandCount));

            //请求 商品列表
            if (shopController!=null)
                shopController.getData(mContext, page, orders, sort, maker_id);

        } else {
            //网路链接超时
            ToastUtil.showToast(context,getString(R.string.timeout));
            noNetView.setVisibility(View.VISIBLE);
            myspace_shop_refresh.setRefreshing(false);
            shopView.setVisibility(View.GONE);

        }
    }
    @Override
    public void getCollectData(CollectBean collectBean) {}

    private boolean hasMore;
    // TODO: 2017/7/3 商品列表 回调结果
    @Override
    public void getProductList(ProductListBean productListBeen) {
        LoadDialog.dismiss(mContext);
        myspace_shop_refresh.setRefreshing(false);
        if (productListBeen == null) {
            //没有网络
            ToastUtil.showToast(context,getString(R.string.timeout));
            noNetView.setVisibility(View.VISIBLE);
            shopView.setVisibility(View.GONE);
            return;
        }
        if (productListBeen.search != null) {
            //有网络
            if (productListBeen.search.products != null) {
                //给listView的数据源数据
                List<ItemEntitys> item = productListBeen.search.products.item;
                if (item != null) {
                    initShopAdapter(item);
                    if (item.size() < 3) {
                        shopAdapter.loadMoreEnd(true);
                    }
                    //判断是否有更多数据
                    int hasMorecount = productListBeen.search.has_more_items.get(0);//集合中的两个值,取其中的任何一个都行
                    if (hasMorecount==0){
                        hasMore = false;
                    } else if (hasMorecount == 1) {
                        hasMore = true;
                    }

                }


            } else {
                //没有数据
               // noShopData.setVisibility(View.VISIBLE);
               // myspace_shop_rv.setVisibility(View.GONE);
                if (page == 1) {
                    //该实体店 没有商品
                    initShopAdapter(new ArrayList<ItemEntitys>());
                    noShopData.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void getMoreProductList(ProductListBean productListBeen) {
        if (productListBeen == null) {
            //没有网络
            ToastUtil.showToast(context,getString(R.string.timeout));
            noNetView.setVisibility(View.VISIBLE);
            page -= 1;
            return;
        }
        if (hasMore) {
            //有数据
            List<ItemEntitys> item = productListBeen.search.products.item;
            shopAdapter.addData(item);
            shopAdapter.loadMoreComplete();

        } else {
            shopAdapter.loadMoreEnd();
        }

        int hasMorecount = productListBeen.search.has_more_items.get(0);//集合中的两个值,取其中的任何一个都行
        if (hasMorecount==0){
            hasMore = false;
        } else if (hasMorecount == 1) {
            hasMore = true;
        }
    }

    @Override
    public void getProvinceCity(ProvinceCityBean provinceCityBean) {}

    @Override
    public void getClassifyName(ClassifyName classifyName) {}

    // TODO: 2017/6/30  请求实体店数据 实体店详情 实体店商品数据
    private void requestStoreData() {
        if (!isHidden())
            LoadDialog.show(context,true);
        if (NetWorkUtil.isNetworkAvailable(context)) {
            NearbyStoreUtil.requestStoreDetail(context, maker_id,GBDLocation.getInstance().getLongitude(), GBDLocation.getInstance().getLatitude(), this);
        } else {
            LoadDialog.dismiss(context);
            storeView.setVisibility(View.GONE);
            myspace_store_head_refresh.setRefreshing(false);
            ToastUtil.showToast(context,getString(R.string.timeout));
            noNetView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void storeDetail(StoreDetail storeDetail) {
        if (storeDetail != null) {
            initStoreDetail(storeDetail);
            // TODO: 2017/6/22 请求实体店商品
            if (NetWorkUtil.isNetworkAvailable(context)) {
                NearbyStoreUtil.requestStoreShopList(context, maker_id, "4", page, this);
            } else {
                LoadDialog.dismiss(context);
                storeView.setVisibility(View.GONE);
                myspace_store_head_refresh.setRefreshing(false);
                ToastUtil.showToast(context,getString(R.string.timeout));
                noNetView.setVisibility(View.VISIBLE);
            }

        } else {
            LoadDialog.dismiss(context);
            ToastUtil.showToast(context,"没有找到该店铺");
        }
    }

    /**
     * 实体店详情
     * @param storeDetail
     */
    private void initStoreDetail(StoreDetail storeDetail) {
        List<StoreDetail.ResultBean> result = storeDetail.getResult();
        StoreDetail.ResultBean resultBean = result.get(0);
        GlideUtil.getInstance().loadImage(context,resultBean.getThumbView(), R.drawable.big_theme_loading_bg, R.drawable.big_theme_error_bg , nearby_store_home_head_item_ic);
        nearby_store_home_head_item_title.setText(resultBean.getM_maker_name_ch());
        nearby_store_home_head_item_type.setText(resultBean.getTradeName());
        nearby_store_home_head_item_price.setText("¥"+resultBean.getM_entity_maker_average_consumption()+"/人");
        setStoreDistance(resultBean.getDistance()+"");

    }
    /**
     * 设置距离
     * @param distance
     */
    private void setStoreDistance(String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            double dis = Double.valueOf(distance);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format( dis / 1000);
                nearby_store_home_head_item_distance.setText(format + "km");
            } else {
                nearby_store_home_head_item_distance.setText(dis + "m");
            }
        }

    }

    /**
     * 实体店商品列表
     * @param storeShop
     */
    @Override
    public void storeShopList(StoreShop storeShop) {
        LoadDialog.dismiss(context);
        if (storeShop != null) {
            if (page == 1) {
                initStoreAdapter(storeShop);
                myspace_store_headview.setVisibility(View.VISIBLE);
            } else {

                if (allAdapter.isLoadMoreEnable()) {
                    List<StoreShop.ResultBean> result = storeShop.getResult();
                    allAdapter.addData(result);
                    allAdapter.loadMoreComplete();
                }
            }
        }else {

            if (page == 1) {
                //该实体店 没有商品
                initStoreAdapter(null);
                myspace_store_headview.setVisibility(View.VISIBLE);
                noShopData.setVisibility(View.VISIBLE);
                LoadDialog.dismiss(context);
            } else {
                //请求失败，没有更多商品
                allAdapter.loadMoreEnd();
            }

        }
        myspace_store_head_refresh.setRefreshing(false);

    }

    /**
     * 初始化实体店
     */
    private void initStoreView() {
        myspace_store_head_refresh = storeView.findViewById(R.id.myspace_store_head_refresh);
        myspace_store_headview = storeView.findViewById(R.id.myspace_store_headview);
        myspace_store_headview.setVisibility(View.GONE);
        myspace_store_container = storeView.findViewById(R.id.myspace_store_container);
        nearby_store_home_head_item_ic = storeView.findViewById(R.id.nearby_store_home_head_item_ic);
       // nearby_store_home_head_item_arrow = (TextView) storeView.findViewById(R.id.nearby_store_home_head_item_arrow);
        nearby_store_home_head_item_title = storeView.findViewById(R.id.nearby_store_home_head_item_title);
        nearby_store_home_head_item_type = storeView.findViewById(R.id.nearby_store_home_head_item_type);
        nearby_store_home_head_item_price = storeView.findViewById(R.id.nearby_store_home_head_item_price);
        nearby_store_home_head_item_distance = storeView.findViewById(R.id.nearby_store_home_head_item_distance);

        myspace_store_recommend_shop = storeView.findViewById(R.id.myspace_store_recommend_shop);
        myspace_store_all_shop = storeView.findViewById(R.id.myspace_store_all_shop);

        myspace_store_headview.setOnClickListener(this);
        myspace_store_all_shop.setOnClickListener(this);
        myspace_store_recommend_shop.setOnClickListener(this);
        recommShopRV = new RecyclerView(context);
        recommShopRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recommShopRV.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recommShopRV.setItemAnimator(new DefaultItemAnimator());
        recommShopRV.addItemDecoration(new SpacesItemDecoration(3));

        allShopRV = new RecyclerView(context);
        allShopRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        allShopRV.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        allShopRV.setItemAnimator(new DefaultItemAnimator());
        allShopRV.addItemDecoration(new SpacesItemDecoration(3));
        allShopRV.setVisibility(View.GONE);

        myspace_store_container.addView(recommShopRV);
        myspace_store_container.addView(allShopRV);
        //实体店下拉刷新监听
        setRefreshListener();

    }

    private void setRefreshListener() {
        myspace_store_head_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtil.isNetworkAvailable(mContext)) {
                    page = 1;
                    recommend.clear();
                    allShop.clear();
                    allAdapter.notifyDataSetChanged();
                    recommAdapter.notifyDataSetChanged();
                    requestStoreData();
                } else {
                    myspace_store_head_refresh.setRefreshing(false);
                    ToastUtil.showToast(mContext,context.getString(R.string.timeout));
                }
            }
        });
    }

    private void initStoreAdapter(StoreShop storeShop){

        if (storeShop != null) {
            recommend.addAll(storeShop.getRecommend());
            allShop.addAll(storeShop.getResult());
            recommAdapter = new MySpaceStoreAdapter(R.layout.myspace_store_shop_item, recommend);
            recommShopRV.setAdapter(recommAdapter);
            recommAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);

            recommAdapter.loadMoreEnd(true);


            allAdapter = new MySpaceStoreAdapter(R.layout.myspace_store_shop_item,allShop);
            allAdapter.setOnLoadMoreListener(this, allShopRV);
            CustomLoadMoreView loadMoreView = new CustomLoadMoreView();
            allAdapter.setLoadMoreView(loadMoreView);
            allShopRV.setAdapter(allAdapter);
        }

    }

    @Override
    public void onLoadMoreRequested() {
        // TODO: 2017/6/6 加载更多

        if (allAdapter != null) {
            if (allShop.size() < 3) {
                allAdapter.loadMoreEnd(true);
                return;
            }
            allAdapter.loadMoreLoading();
            //延迟1s加载更多
            new Handler().postDelayed(this, 1000);
        }
        if (shopAdapter != null) {
            shopAdapter.loadMoreLoading();
            //延迟1s加载更多
            new Handler().postDelayed(this, 1000);
        }

    }

    @Override
    public void storeShopListErrorMsg(String error) {
        LoadDialog.dismiss(context);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(context,getString(R.string.no_net_or_service_no));
    }

    @Override
    public void storeDetailErrorMsg(String error) {
        LoadDialog.dismiss(context);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(context,getString(R.string.no_net_or_service_no));
    }



    @Override
    public void run() {
        if (allAdapter != null) {
            if (allAdapter.isLoadMoreEnable()) {
                page++;
                NearbyStoreUtil.requestStoreShopList(context,maker_id,"4",page,this);
            }
        }

        if (shopAdapter != null) {
            if (shopAdapter.isLoadMoreEnable()) {
                page++;
                shopController.getMoreData(mContext,page, orders, sort, maker_id);
            }
        }
    }


    // TODO: 2017/6/30  初始化专卖店
    private void initShopView() {
        myspace_shop_rv = shopView.findViewById(R.id.myspace_shop_rv);
        myspace_shop_refresh = shopView.findViewById(R.id.myspace_shop_refresh);
        final ViewGroup parent = (ViewGroup) myspace_shop_rv.getParent();
        myspace_shop_head_layout = context.getLayoutInflater().inflate(R.layout.myspace_shop_head_layout, parent, false);
        myspace_shop_rv.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        myspace_shop_rv.setItemAnimator(new DefaultItemAnimator());
        myspace_shop_rv.addItemDecoration(new SpacesItemDecoration(3));

        //初始化头部空间
        iv_shop_logo = myspace_shop_head_layout.findViewById(R.id.iv_shop_logo);
        tv_shop_name = myspace_shop_head_layout.findViewById(R.id.tv_shop_name);
        tv_shop_product_count = myspace_shop_head_layout.findViewById(R.id.tv_shop_product_count);
        tv_shop_collect_count = myspace_shop_head_layout.findViewById(R.id.tv_shop_collect_count);
        myspace_shop_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtil.isNetworkAvailable(mContext)) {
                    page = 1;
                    shopData.clear();
                    shopAdapter.notifyDataSetChanged();
                    requestShopData();
                } else {
                    myspace_shop_refresh.setRefreshing(false);
                    ToastUtil.showToast(mContext,context.getString(R.string.timeout));
                }
            }
        });

    }

    /**
     * 初始化适配器
     */
    private void initShopAdapter(List<ItemEntitys> data) {
        shopData.addAll(data);
        if (shopAdapter == null) {
            shopAdapter = new MySpaceShopAdapter(R.layout.product_list_item, shopData);
            shopAdapter.setOnLoadMoreListener(this, myspace_shop_rv);
            shopAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);
            shopAdapter.setLoadMoreView(new CustomLoadMoreView());
            myspace_shop_rv.setAdapter(shopAdapter);

            myspace_shop_rv.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {

                }
            });

            shopAdapter.addHeaderView(myspace_shop_head_layout);
        } else {
            shopAdapter.notifyDataSetChanged();
        }

    }

    // TODO: 2017/6/30  初始化webview
    private void initWeb() {
        webContainer = personWebView.findViewById(R.id.search_ser_fl);
        mProgress = personWebView.findViewById(R.id.search_ser_web_progress);
        webView = new CustomWebView(mContext,this);
        webView.setScrollbarFadingEnabled(true);
        webContainer.addView(webView);
        //打开网页上的链接 继续使用webview打开网页
        webView.setWebViewClient(new IWebViewClient());
        webView.setWebChromeClient(new IWebChromeClient());
        webView.setClickable(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setPersistentDrawingCache(0);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                } else {
                    return false;
                }
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener() {
            float start_y;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start_y = event.getY();
                    if (!view.hasFocus()) {
                        view.requestFocus();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!view.hasFocus())
                        view.requestFocus();
                }

                return false;
            }
        });
        initWebSetting();
    }
    private void initWebSetting() {
        WebSettings settings = webView.getSettings();
        settings.setSaveFormData(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级
        settings.setBlockNetworkImage(true);//使把图片加载放在最后来加载渲染
        settings.setAppCacheEnabled(true);
        // webView数据缓存分为两种：AppCache和DOM Storage（Web Storage）。
        // 开启 DOM storage 功能
        settings.setDomStorageEnabled(true);

        // 应用可以有数据库
        settings.setDatabaseEnabled(true);
        // 根据网络连接情况，设置缓存模式，
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 先查找缓存，没有的情况下从网络获取。
        }
        // 可以读取文件缓存(manifest生效)
        settings.setAllowFileAccess(true);

        settings.setSavePassword(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath("/data/data/" + mContext.getPackageName() + "/databases/");
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        ScreenUtil.setWebView(context , webView);//页面适配屏幕

    }

    /**
     * 初始化进度条
     *
     * @param visibility
     */
    private void setmProgressBarState(int visibility) {
        mProgress.setProgress(0);
        mProgress.setVisibility(visibility);
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    private void setProgressBarProgress(int progress) {
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setProgress(progress);
    }

    /**
     * CustomWebView 长按识别二维码图片
     * @param imgUrl
     */
    @Override
    public void onLongClickCallBack(final String imgUrl) {
        currURL=imgUrl;
        new WebViewQRUtils(context).startOnLongCallBack(imgUrl);
    }



    /**
     * 更新进度条 获取网页标题
     */
    class IWebChromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            GBLogUtils.DEBUG_DISPLAY("personWebView","IWebChromeClient onProgressChanged "+newProgress);
            super.onProgressChanged(view, newProgress);
            //webTitle.setText(view.getTitle());
            setProgressBarProgress(newProgress);
            // currentProgress = newProgress;
            if (newProgress <= 10) {
                startTimer();
            } else {
                cancelTimer();
            }
            if (newProgress == 100) {
                setmProgressBarState(View.GONE);
            }
        }



        @Override
        public void onReceivedTitle(WebView view, String title) {
            GBLogUtils.DEBUG_DISPLAY("personWebView","IWebChromeClient onReceivedTitle "+title);
            super.onReceivedTitle(view, title);
        }


    }
    private String currURL = "";
    /**
     * WebViewClient
     */
    class IWebViewClient extends WebViewClient {


        @Override
        public void onLoadResource(WebView view, String url) {
            GBLogUtils.DEBUG_DISPLAY("personWebView","onLoadResource(WebView view, String url)"+url);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        
            GBLogUtils.DEBUG_DISPLAY("personWebView","IWebViewClient shouldOverrideUrlLoading"+url);
            currURL = url;
            // 判断重定向url是不是一个网址 还是一个启动服务
            if (Patterns.WEB_URL.matcher(url).matches()) {
                //符合标准
                GBLogUtils.DEBUG_DISPLAY("personWebView","IWebViewClient shouldOverrideUrlLoading true"+url);
                view.loadUrl(url);
                return true;
            } else{
                //不符合标准
                GBLogUtils.DEBUG_DISPLAY("personWebView","IWebViewClient shouldOverrideUrlLoading false"+url);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    /*"mqqwpa://im/chat?chat_type=wpa&uin=2567820876";
                       baidumap://map/place/search?*/
                    String uri = null;
                    if (url.contains("mqqwpa")) {
                        uri = "QQ";
                    } else if (url.contains("baidumap")){
                        uri = "百度地图";
                    }

                    ToastUtil.showToast(context,"找不到"+uri);
                    e.printStackTrace();
                }
                return true;
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            GBLogUtils.DEBUG_DISPLAY("personWebView","IWebViewClient onPageStarted "+url);
            //开始加载页面时调用  -->设定loading页面 告诉用户等待网络响应
            if (!Patterns.WEB_URL.matcher(url).matches()) {  //去除返回时，出现空白错误页url： data:text/html,<html><head><title>net error</title></head><body></body></html>
                view.goBack();
                return;
            }
            if (!isHidden()) // 当前fragment不是show  显示加载动画
                LoadDialog.show(mContext,true);
            startTimer();

        }



        @Override
        public void onPageFinished(WebView view, String url) {
            GBLogUtils.DEBUG_DISPLAY("personWebView","IWebViewClient onPageFinished "+url);
            LoadDialog.dismiss(mContext);
            cancelTimer();
            setmProgressBarState(View.GONE);
            view.getSettings().setBlockNetworkImage(false);
            //页面加载完成后调用 --> 处理关闭loading条 切换程序动作  加载 完成 可以改变控件 图片资源
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            GBLogUtils.DEBUG_DISPLAY("personWebView","IWebViewClient onReceivedError description "+description);
            //报告错误信息  可以处理无网络事件
            //view.loadUrl("html无网络事件");
            noNetView.setVisibility(View.VISIBLE);
            // showErrorPage();
            String errorHtml = "<html><head><title>net error</title></head><body></body></html>";
            webView.loadData(errorHtml, "text/html", "UTF-8");
        }



    }
    private static final int WAIT_TIME_OUT_MS = 8 * 1000;
    private Timer mTimer = null;
    private Timer timer = null;
    private final RunnableBus mRBus = RunnableBus.getInstance();
    /**
     * 加载超时响应 计时器
     */
    private boolean mIsDestroy = false;

    private class TimeoutTask extends TimerTask {

        @Override
        public void run() {
            mRBus.postMain(new Runnable() {
                @Override
                public void run() {
                    if (mIsDestroy) {
                        return;
                    }
                    //webvv.stopLoading();
                    //webvv.loadUrl("about:blank");
                    mProgress.setVisibility(View.GONE);

                    final String msg = GBResourceUtils.getString(context, "launcher_support_webshell_timeout");
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;

        }
    }

    private void startTimer() {
        cancelTimer();
        mTimer = new Timer();
        mTimer.schedule(new TimeoutTask(), WAIT_TIME_OUT_MS);
    }

    @Override
    protected void setListener() {
        btn_request_net.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_net:
                //无网络
                if (NetWorkUtil.isNetworkAvailable(mContext)) {
                    noNetView.setVisibility(View.GONE);
                    if (personWebView != null) {
                        if (TextUtils.isEmpty(currURL)) {
                            webView.loadUrl(shopAddrUrl);
                        } else {
                            webView.loadUrl(currURL);
                        }
                        return;
                    }
                    if (storeView != null) {
                        storeView.setVisibility(View.VISIBLE);
                        requestStoreData();
                        return;
                    }
                    if (shopView != null) {
                        shopView.setVisibility(View.VISIBLE);
                        requestShopData();
                        return;
                    }
                } else {
                    ToastUtil.showToast(mContext,mContext.getString(R.string.timeout));
                }

                break;
            case R.id.myspace_store_all_shop:
                //全部商品
                myspace_store_all_shop.setBackgroundResource(R.drawable.winguo_myspace_store_classify_selector_bg);
                myspace_store_all_shop.setTextColor(getResources().getColor(R.color.wh));
                myspace_store_recommend_shop.setBackgroundResource(R.color.wh);
                myspace_store_recommend_shop.setTextColor(getResources().getColor(R.color.main_navi_color_selector));
                recommShopRV.setVisibility(View.GONE);
                allShopRV.setVisibility(View.VISIBLE);
                break;
            case R.id.myspace_store_recommend_shop:
                //推荐商品
                myspace_store_recommend_shop.setBackgroundResource(R.drawable.winguo_myspace_store_classify_selector_bg);
                myspace_store_recommend_shop.setTextColor(getResources().getColor(R.color.wh));
                myspace_store_all_shop.setBackgroundResource(R.color.wh);
                myspace_store_all_shop.setTextColor(getResources().getColor(R.color.main_navi_color_selector));
                recommShopRV.setVisibility(View.VISIBLE);
                allShopRV.setVisibility(View.GONE);
                break;
            case R.id.myspace_store_headview:
                //实体店 头部view点击事件跳转详情页
//                Intent it = new Intent(context, StoreDetailActivity.class);
//                it.putExtra("store_id","2436");
//                it.putExtra("store_id",maker_id);
//                startActivity(it);
                break;
        }
    }

    @Override
    public void onDestroy() {
        context.unregisterReceiver(receiver);
        cancelTimer();
        if (webView != null) {
            Log.e(""+MySpaceFragment.class.getSimpleName(),"onDestroy webview!=null");
            webContainer.removeView(webView);
            //退出应用后 清除cookie
            CookieSyncManager.createInstance(mContext);
            CookieManager instance = CookieManager.getInstance();
            instance.removeAllCookie();
            CookieSyncManager.getInstance().sync();
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.stopLoading();
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        mIsDestroy = true;
        super.onDestroy();
    }

    /**
     * 接受广播 登录后用户信息
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {

            String action = intent.getAction();
            //登陆过后 通知修改头像、 账户名、余额、果币
            if (action.equals(Constants.LOGIN_SUCCESS)) {
                my_space_container.removeView(personWebView);
                my_space_container.removeView(shopView);
                my_space_container.removeView(storeView);
                noLoginView = null;

                page = 1;
                initData();
                initView(view);
            }

            if (action.equals(Constants.QUIT_SUCCESS)) {
                //退出移除全部布局 view = null
                my_space_container.removeView(noNetView);
                my_space_container.removeView(personWebView);
                my_space_container.removeView(shopView);
                my_space_container.removeView(storeView);
                personWebView = null;
                shopView = null;
                storeView = null;
                shopAdapter = null;
                noNetView = null;

                //加载 去登录view
               /* initData();
                initView(view);*/

                /*Intent it = new Intent(context, LoginActivity.class);
                it.putExtra("isMySpace",true);
                startActivity(it);*/
            }
        }
    }

}
