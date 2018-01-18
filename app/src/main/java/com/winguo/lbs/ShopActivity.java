package com.winguo.lbs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.guobi.account.NetworkUtils;
import com.guobi.gblocation.GBDLocation;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseTitleActivity;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.lbs.bean.NearByBean;
import com.winguo.lbs.bean.NearByRootBean;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;
import com.winguo.view.ConfirmDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * @author hcpai
 * @desc 实体店首页
 */

public class ShopActivity extends BaseTitleActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * RecycleView数据
     */
    private ArrayList<NearByRootBean.ResultBean.StoreListsBean> mNearbyRvDatas = new ArrayList<>();
    /**
     * Banner广告数据
     */
    private ArrayList<String> mBannerData = new ArrayList<>();
    private ShopAdapter mNearByAdapter;
    private BGABanner mBanner;

    private String mShop_id;
    //无网络
    private View noNetView;
    //重新加载网络
    private Button btn_request_net;
    //内容view
    private View mSuccessView;
    private RecyclerView mNearby_rv;
    private TextView mShop_name_tv;
    private TextView mShop_per_consumption_tv;
    private TextView mShop_address_tv;
    private LinearLayout mShop_address_rl;
    private TextView mShop_open_time_tv;
    private TextView mShop_phone_tv;
    private LinearLayout mShop_phone_rl;
    private List<NearByRootBean.ResultBean.StoreListsBean> mNearByBeanResult;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initViews();
        initData();
        setListener();
        // 将当前activity加入到全局控制的activity集合中
        StartApp.getInstance().addActivity(this);
    }

    /**
     * 获取布局id
     *
     * @return
     */
    protected int getViewId() {
        return R.layout.activity_shop;
    }

    /**
     * 初始化视图
     */
    protected void initViews() {
        FrameLayout nearby_container = (FrameLayout) findViewById(R.id.nearby_shop_container);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //无网络
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);

        //有数据
        mSuccessView = View.inflate(this, R.layout.nearby_shop_rv, null);
        mNearby_rv = (RecyclerView) mSuccessView.findViewById(R.id.shop_rv);
        mNearby_rv.setLayoutManager(new LinearLayoutManager(this));
        mNearby_rv.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout = (SwipeRefreshLayout) mSuccessView.findViewById(R.id.shop_swipe_refresh);
        //设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        //添加到容器
        nearby_container.addView(mSuccessView, params);
        nearby_container.addView(noNetView, params);

        //所有的view gone掉
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);

        setBackBtn();
        LoadDialog.show(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        //获取店铺id
        mShop_id = (String) getIntent().getExtras().get("shop_id");
        //有网络
        if (NetworkUtils.isConnectNet(this)) {
            mSuccessView.setVisibility(View.VISIBLE);
            noNetView.setVisibility(View.GONE);
            //获取商家信息
            NearByHandler.getShopInfo(handler, null, mShop_id, GBDLocation.getInstance().getLongitude(), GBDLocation.getInstance().getLatitude());
            NearByHandler.getRecommendShop(handler, null, mShop_id, mPage);
        } else {
            ToastUtil.show(this, getString(R.string.offline));
            noNetView.setVisibility(View.VISIBLE);
            mSuccessView.setVisibility(View.GONE);
            LoadDialog.dismiss(this);
        }
        initAdapter();
        addHeadView();
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mNearByAdapter = new ShopAdapter(R.layout.nearby_shop_list_item);
        mNearByAdapter.setOnLoadMoreListener(this, mNearby_rv);
        mNearByAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mNearByAdapter.setLoadMoreView(new CustomLoadMoreView());
    }

    /**
     * 添加头布局
     */
    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.shop_head, (ViewGroup) mNearby_rv.getParent(), false);
        mBanner = (BGABanner) headView.findViewById(R.id.banner_guide_content);
        mShop_name_tv = (TextView) headView.findViewById(R.id.shop_name_tv);
        mShop_per_consumption_tv = (TextView) headView.findViewById(R.id.shop_per_consumption_tv);
        mShop_address_tv = (TextView) headView.findViewById(R.id.shop_address_tv);
        mShop_address_rl = (LinearLayout) headView.findViewById(R.id.shop_address_rl);
        mShop_open_time_tv = (TextView) headView.findViewById(R.id.shop_open_time_tv);
        mShop_phone_tv = (TextView) headView.findViewById(R.id.shop_phone_tv);
        mShop_phone_rl = (LinearLayout) headView.findViewById(R.id.shop_phone_rl);
        initHeadData();
        mNearByAdapter.addHeaderView(headView);
    }

    /**
     * 初始化头布局
     */
    private void initHeadData() {
        mBanner.setAutoPlayAble(false);
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(ShopActivity.this)
                        .load(model)
                        .placeholder(R.drawable.big_banner_bg)
                        .error(R.drawable.big_banner_error_bg)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
    }

    /**
     * 设置监听器
     */
    protected void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mNearby_rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                mShop_id = mNearbyRvDatas.get(position).getId();
                mNearbyRvDatas.clear();
                mPage = 1;
                getDataAgain();
            }
        });
        btn_request_net.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.NEARBY_SHOP_INFO_REQUEST_TAG:
                NearByBean nearByBean = (NearByBean) msg.obj;
                if (nearByBean != null) {
                    handleHeadData((NearByBean) msg.obj);
                } else {
                    //数据访问超时
                    ToastUtil.showToast(ShopActivity.this,getString(R.string.no_net_or_service_no));
                }
                LoadDialog.dismiss(this);
                break;
            case RequestCodeConstant.NEARBY_SHOP_BYNEAR_REQUEST_TAG:
                NearByRootBean nearByRootBean = (NearByRootBean) msg.obj;
                if (nearByRootBean != null) {
                    handleContentData((NearByRootBean) msg.obj);
                } else {
                    //数据访问超时
                    ToastUtil.showToast(ShopActivity.this,getString(R.string.no_net_or_service_no));
                }
                LoadDialog.dismiss(this);
                break;
        }
    }

    private void handleContentData(NearByRootBean nearByRootBean) {
        if (nearByRootBean.getCode() == 200) {
            NearByRootBean.ResultBean result = nearByRootBean.getResult();
            mNearByBeanResult = result.getStore_lists();
            int totals = result.getTotal_page();
            mNearbyRvDatas.addAll(mNearByBeanResult);
            mNearByAdapter.setData(mNearbyRvDatas);
            if (mPage <totals) {
                //第一次加载
                if (mPage == 1) {
                    mNearby_rv.setAdapter(mNearByAdapter);
                    if (mPage==totals){
                        mNearByAdapter.loadMoreEnd(false);
                        return;
                    }
                    // 非第一次加载
                } else {
                    mNearByAdapter.notifyDataSetChanged();
                }

                mNearByAdapter.loadMoreComplete();
                //没更多
            } else if (mPage == result.getTotal_page()) {
                mNearByAdapter.notifyDataSetChanged();
                mNearByAdapter.loadMoreEnd(false);
            }
        } else {
            Toast.makeText(this, "访问出错!", Toast.LENGTH_LONG).show();
        }
        //mSwipeRefreshLayout.setRefreshing(true);
    }

    private void handleHeadData(NearByBean nearByBean) {
        if (nearByBean.getCode() == 200) {
            final NearByBean.ResultBean resultBean = nearByBean.getResult().get(0);
            //设置商家大图片
            mBannerData.add(resultBean.getShop_pic());
            mBanner.setData(mBannerData, null);
            //设置商家信息
            mShop_name_tv.setText(resultBean.getShop_name());
            String per_consumption = resultBean.getPer_consumption();
            if (per_consumption.equals("/")) {
                mShop_per_consumption_tv.setText("");
            } else {
                mShop_per_consumption_tv.setText(String.format(getString(R.string.shop_per_consumption), resultBean.getPer_consumption()));
            }
            mShop_address_tv.setText(resultBean.getShop_address());
            String business_hours = resultBean.getBusiness_hours();
            if (business_hours.equals("/")) {
                mShop_open_time_tv.setText(String.format(getString(R.string.shop_open_time), ""));
            } else {
                mShop_open_time_tv.setText(String.format(getString(R.string.shop_open_time), resultBean.getBusiness_hours()));
            }
            if (resultBean.getTelphone().equals("/")) {
                mShop_phone_tv.setText("");
            } else {
                mShop_phone_tv.setText(resultBean.getTelphone());
            }
            //点击事件
            mShop_address_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //百度地图
                    if (isInstallByread("com.baidu.BaiduMap")) {
                        Intent baiDuIntent = new Intent();
                        baiDuIntent.setData(Uri.parse("baidumap://map/marker?location=" + resultBean.getShop_lat() + "," +
                                resultBean.getShop_lng() + "&title=" + resultBean.getShop_name() + "&traffic=on"));
                        startActivity(baiDuIntent);
                        return;
                    }
                    //高德地图
                    if (isInstallByread("com.autonavi.minimap")) {
                        Intent gaoDeIntent = new Intent();
                        gaoDeIntent.setData(Uri.parse("androidamap://viewMap?poiname=" + resultBean.getShop_name() + "&lat=" + resultBean.getShop_lat() + "&lon=" + resultBean.getShop_lng() + "&dev=0"));
                        startActivity(gaoDeIntent);
                        return;
                    }
                    final ConfirmDialog confirmDialog = new ConfirmDialog(ShopActivity.this, false);
                    confirmDialog.setDialogTitle("提示");
                    confirmDialog.setDialogContent("没有检测到第三方地图客户端");
                    confirmDialog.setDialogContent1("暂时无法为您服务");
                    confirmDialog.setPositiveButton("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialog.dismiss();
                        }
                    });
                }
            });
            mShop_phone_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到打电话
                    Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resultBean.getTelphone()));
                    startActivity(intentCall);
                }
            });
        } else {
            Toast.makeText(this, "访问出错!", Toast.LENGTH_LONG).show();
        }
       // mSwipeRefreshLayout.setRefreshing(true);
    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_net:
                getDataAgain();
                break;
        }
    }

    private void getDataAgain() {
        if (NetworkUtils.isConnectNet(this)) {
            if (mNearbyRvDatas != null) {
                mNearbyRvDatas.clear();
            }
            if (mBannerData != null) {
                mBannerData.clear();
            }
            mSuccessView.setVisibility(View.VISIBLE);
            noNetView.setVisibility(View.GONE);
            //获取商家信息
            NearByHandler.getShopInfo(handler, null, mShop_id, GBDLocation.getInstance().getLongitude(), GBDLocation.getInstance().getLatitude());
            NearByHandler.getRecommendShop(handler, null, mShop_id, mPage);
        } else {
            //无网络
            ToastUtil.show(this, getString(R.string.offline));
            if (mSuccessView.getVisibility() != View.VISIBLE) {
                noNetView.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 上拉加载更多监听回调
     */
    @Override
    public void onLoadMoreRequested() {
        //mSwipeRefreshLayout.setRefreshing(false);
        if (NetworkUtils.isConnectNet(this)) {
            mPage++;
            NearByHandler.getRecommendShop(handler, null, mShop_id, mPage);
        } else {
            ToastUtil.show(this, getString(R.string.offline));
            mNearByAdapter.loadMoreFail();
        }
    }


    /**
     * 下拉加载更多监听回调
     */
    @Override
    public void onRefresh() {
        mPage = 1;
        getDataAgain();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 是否安装某个应用
     *
     * @param packageName
     * @return
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
    /*----------------------弱引用-----------------------*/
    /**
     * handler机制
     */
    public Handler handler = new LeakHander(this);

    @Override
    public void onClick(View v) {
        doClick(v);
    }


    //弱引用，防止内存泄漏
    static class LeakHander extends Handler {
        WeakReference<ShopActivity> mWeakReference;

        public LeakHander(ShopActivity baseActivity) {
            mWeakReference = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().handleMsg(msg);
            }
        }
    }
}
