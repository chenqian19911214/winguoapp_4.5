package com.winguo.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guobi.account.NetworkUtils;
import com.guobi.gblocation.GBDLocation;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.webshell.BrowserActivity;
import com.winguo.R;
import com.winguo.activity.LBSThemeActivity;
import com.winguo.activity.NearbyStoreHomeActivity;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragment2;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.lbs.HorizontalListView;
import com.winguo.lbs.NearByAdapter;
import com.winguo.lbs.NearByHandler;
import com.winguo.lbs.bean.NearByTopicBean;
import com.winguo.lbs.bean.NearbyStoreBean;
import com.winguo.lbs.manuallocation.IBaseLocationListener;
import com.winguo.lbs.bean.LbsLocationBean;
import com.winguo.lbs.manuallocation.LocationService;
import com.winguo.lbs.manuallocation.ManuallyLocationActivity;
import com.winguo.net.GlideUtil;
import com.winguo.search.nearby.NearbySearchActivity;
import com.winguo.utils.Intents;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * @author hcpai
 * @desc ${TODD}
 */

public class NearbyFragment extends BaseFragment2 implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mNearby_rv;
    /**
     * RecycleView数据
     */
    private ArrayList<NearbyStoreBean.StoreListsBean> mNearbyRvDatas = new ArrayList<>();
    /**
     * 横向ListView数据
     */
    private NearByAdapter mNearByAdapter;
    private BGABanner mBanner, mNoGPSBanner, mNoShopBanner;
    private HorizontalListView mHorizontalListView, mNoGPSHorizontalListView, mNoShopHorizontalListView;
    /**
     * 没网络
     */
    private View noNetView;
    /**
     * 无网络时点击重新加载数据
     */
    private Button btn_request_net;
    /**
     * 有数据
     */
    private View mSuccessView;
    private View mNo_open_gps;
    private View mNearby_no_shop;
    private Button mOpenGpsBtn;
    private ImageView mAd1_iv, mNoGPSAd1_iv, mNoShopAd1_iv;
    private ImageView mAd2_iv, mNoGPSAd2_iv, mNoShopAd2_iv;
    private List<NearByTopicBean.ResultBean.Line2Bean> mAdDatas;
    private SwipeRefreshLayout nearby_swipe_refresh;
    private ArrayList<NearByTopicBean.ResultBean.Line1Bean> mHLVDatas;
    private List<NearByTopicBean.ResultBean.BanBean> mBannerData;
    /**
     * 第几页
     */
    private int mPage = 1;
    private boolean isRefresh;
    private boolean isManualLocation;
    private TextView mNearby_title_lbs_tv;
    private TextView mNearby_title_search_tv;
    private LocationService locationService;
    private MyBDLocationListener mListener = new MyBDLocationListener();
    private LbsLocationBean mLbsLocationBean;
    private RelativeLayout mNearby_title_lbs_rl;

    /**
     * 布局资源
     *
     * @return
     */
    @Override
    protected int getLayout() {
        return R.layout.fragment_nearby;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    @Override
    protected void initView(View view) {
        mNearby_title_lbs_rl = (RelativeLayout) view.findViewById(R.id.nearby_title_lbs_rl);
        mNearby_title_lbs_tv = (TextView) view.findViewById(R.id.nearby_title_lbs_tv);
        mNearby_title_search_tv = (TextView) view.findViewById(R.id.nearby_title_search_tv);

        FrameLayout nearby_container = (FrameLayout) view.findViewById(R.id.nearby_container);
        nearby_swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.nearby_swipe_refresh);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //头部
        View headView = mActivity.getLayoutInflater().inflate(R.layout.nearby_head, null, false);
        mBanner = (BGABanner) headView.findViewById(R.id.banner_guide_content);

        //填充广告数据
        mAd1_iv = (ImageView) headView.findViewById(R.id.ad1_iv);
        mAd2_iv = (ImageView) headView.findViewById(R.id.ad2_iv);

        mHorizontalListView = (HorizontalListView) headView.findViewById(R.id.horizontalListView);


        //无网络
        noNetView = View.inflate(mActivity, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        nearby_container.addView(noNetView, params);
        //没有打开GPS
        mNo_open_gps = View.inflate(mActivity, R.layout.no_open_gps, null);
        FrameLayout no_gps_fl = (FrameLayout) mNo_open_gps.findViewById(R.id.no_gps_fl);
        mOpenGpsBtn = (Button) mNo_open_gps.findViewById(R.id.gps_setting);
        //没有定位头部
        View noGPSHeadView = mActivity.getLayoutInflater().inflate(R.layout.nearby_head, null, false);
        mNoGPSBanner = (BGABanner) noGPSHeadView.findViewById(R.id.banner_guide_content);
        //noGPSNear_tv_location = (TextView) noGPSHeadView.findViewById(R.id.near_tv_location);

        //填充广告数据
        mNoGPSAd1_iv = (ImageView) noGPSHeadView.findViewById(R.id.ad1_iv);
        mNoGPSAd2_iv = (ImageView) noGPSHeadView.findViewById(R.id.ad2_iv);

        mNoGPSHorizontalListView = (HorizontalListView) noGPSHeadView.findViewById(R.id.horizontalListView);
        no_gps_fl.addView(noGPSHeadView);
        nearby_container.addView(mNo_open_gps, params);

        //附近没商家
        mNearby_no_shop = View.inflate(mActivity, R.layout.nearby_no_shop, null);
        FrameLayout no_shop_fl = (FrameLayout) mNearby_no_shop.findViewById(R.id.no_shop_fl);
        //没有附近商家头部
        View noShopHeadView = mActivity.getLayoutInflater().inflate(R.layout.nearby_head, null, false);
        mNoShopBanner = (BGABanner) noShopHeadView.findViewById(R.id.banner_guide_content);
        //noShopNear_tv_location = (TextView) noShopHeadView.findViewById(R.id.near_tv_location);

        //填充广告数据
        mNoShopAd1_iv = (ImageView) noShopHeadView.findViewById(R.id.ad1_iv);
        mNoShopAd2_iv = (ImageView) noShopHeadView.findViewById(R.id.ad2_iv);

        mNoShopHorizontalListView = (HorizontalListView) noShopHeadView.findViewById(R.id.horizontalListView);
        no_shop_fl.addView(noShopHeadView);
        nearby_container.addView(mNearby_no_shop, params);

        //有数据
        mSuccessView = View.inflate(mActivity, R.layout.nearby_rv, null);
        mNearby_rv = (RecyclerView) mSuccessView.findViewById(R.id.nearby_rv);
        mNearby_rv.setLayoutManager(new LinearLayoutManager(mContext));
        mNearby_rv.setItemAnimator(new DefaultItemAnimator());
        mNearByAdapter = new NearByAdapter(R.layout.nearby_list_item);
        mNearByAdapter.setOnLoadMoreListener(this, mNearby_rv);
        mNearByAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mNearByAdapter.setLoadMoreView(new CustomLoadMoreView());
        mNearByAdapter.addHeaderView(headView);
        nearby_container.addView(mSuccessView, params);
        //设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        nearby_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        nearby_swipe_refresh.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        nearby_swipe_refresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        setAllViewGone();

        //初始化百度定位
        locationService = ((StartApp) mActivity.getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

        mLbsLocationBean = new LbsLocationBean();
    }

    @Override
    protected void loadData() {
        if (NetworkUtils.isConnectNet(mContext)) {
            LoadDialog.show(mContext);
            NearByHandler.getNearLbsTopic(handle);
            //setLocation();
        } else {
            ToastUtil.show(mActivity, getString(R.string.offline));
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示定位信息
     */
    private void setLocation() {
        locationService.start();
    }

    /**
     * 初始化头布局
     */
    private void initHeadData(BGABanner banner) {
        banner.setAdapter(new BGABanner.Adapter<ImageView, NearByTopicBean.ResultBean.BanBean>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, NearByTopicBean.ResultBean.BanBean model, int position) {
                Glide.with(mContext)
                        .load(model.getM_topics_pic_url())
                        .placeholder(R.drawable.big_banner_bg)
                        .error(R.drawable.big_banner_error_bg)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
    }

    @Override
    public void onDestroy() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onDestroy();
    }

    /**
     * 所有的view Gone
     */
    private void setAllViewGone() {
        mNo_open_gps.setVisibility(View.GONE);
        mNearby_no_shop.setVisibility(View.GONE);
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LbsLocationBean currLocationBean = (LbsLocationBean) data.getExtras().get(NearbySearchActivity.LOCATION);
        GBLogUtils.DEBUG_DISPLAY("onActivityResult:", "" + currLocationBean);
        if (currLocationBean != null) {
            isManualLocation = true;
            //保存数据
            mLbsLocationBean.setLatitude(currLocationBean.getLatitude());
            mLbsLocationBean.setLongitude(currLocationBean.getLongitude());
            mLbsLocationBean.setCity(currLocationBean.getCity());
            mLbsLocationBean.setDescribe(currLocationBean.getDescribe());
            // TODO: 2017/7/7  手动定位地址 刷新页面 获取周围实体店列表
            mNearby_title_lbs_tv.setText(currLocationBean.getDescribe());
            GBLogUtils.DEBUG_DISPLAY("onActivityResult:", "" + mLbsLocationBean.getDescribe() + "===" + mLbsLocationBean.getLongitude() + "::" + mLbsLocationBean.getLatitude());
            mPage = 1;
            getDataAgain();
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {

        mNearby_title_search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, NearbySearchActivity.class);
                intent.putExtra(NearbySearchActivity.LOCATION, mLbsLocationBean);
                startActivity(intent);
            }
        });
        //下拉刷新
        nearby_swipe_refresh.setOnRefreshListener(this);
        //定位
        mNearby_title_lbs_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NearbySearchActivity.LOCATION, mLbsLocationBean);
                intent.setClass(mContext, ManuallyLocationActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //网络错误时,点击重新加载
        btn_request_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noNetView.setVisibility(View.GONE);
                LoadDialog.show(mContext);
                if (NetWorkUtil.isNetworkAvailable(mContext)) {
                    mPage = 1;
                    isManualLocation = false;
                    getDataAgain();
                } else {
                    noNetView.setVisibility(View.VISIBLE);
                    LoadDialog.dismiss(mContext);
                }

            }
        });
        //banner部分
        mBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                NearByTopicBean.ResultBean.BanBean banBean = mBannerData.get(position);

                if (!TextUtils.isEmpty(banBean.getM_topics_url())) {
                    Intent it = new Intent(mContext, BrowserActivity.class);
                    it.putExtra("TargetUrl", banBean.getM_topics_url());
                    startActivity(it);
                }
            }
        });
        mNoGPSBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                NearByTopicBean.ResultBean.BanBean banBean = mBannerData.get(position);

                if (!TextUtils.isEmpty(banBean.getM_topics_url())) {
                    Intent it = new Intent(mContext, BrowserActivity.class);
                    it.putExtra("TargetUrl", banBean.getM_topics_url());
                    startActivity(it);
                }
            }
        });
        mNoShopBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                NearByTopicBean.ResultBean.BanBean banBean = mBannerData.get(position);

                if (!TextUtils.isEmpty(banBean.getM_topics_url())) {
                    Intent it = new Intent(mContext, BrowserActivity.class);
                    it.putExtra("TargetUrl", banBean.getM_topics_url());
                    startActivity(it);
                }
            }
        });
        /*      //内容部分
        mNearby_rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                //跳转到实体店详情
                Intent intent = new Intent();
                intent.putExtra("store_id", mNearbyRvDatas.get(position).getShop_id());
                intent.setClass(mContext, NearbyStoreHomeActivity.class);
                startActivity(intent);
                //Toast.makeText(mContext, "内容部分:" + mNearbyRvDatas.get(position).getId(), Toast.LENGTH_LONG).show();
            }
        });*/
        //内容部分
        mNearByAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //if (isCanBuy) {
                //跳转到实体店详情
                NearbyStoreBean.StoreListsBean storeListsBean = mNearbyRvDatas.get(position);
                Intent intent = new Intent();
                intent.putExtra("store_id", storeListsBean.getShop_id());
                intent.putExtra("log", mLbsLocationBean.getLongitude());
                intent.putExtra("lat", mLbsLocationBean.getLatitude());
                intent.putExtra("isCanBuy", isCanBuy);
                if (!isCanBuy) {
                    intent.putExtra("distance", storeListsBean.getDistance());
                }
                intent.setClass(mContext, NearbyStoreHomeActivity.class);
                startActivity(intent);
                //Toast.makeText(mContext, "内容部分:" + mNearbyRvDatas.get(position).getId(), Toast.LENGTH_LONG).show();
                //  }
            }
        });

        //主题活动
        mHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearByTopicBean.ResultBean.Line1Bean line1Bean = mHLVDatas.get(position);
                startThemeActivity(line1Bean.getM_topics_title(), "", line1Bean.getM_topics_id());
            }
        });
        mNoGPSHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearByTopicBean.ResultBean.Line1Bean line1Bean = mHLVDatas.get(position);
                startThemeActivity(line1Bean.getM_topics_title(), "", line1Bean.getM_topics_id());
            }
        });
        mNoShopHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearByTopicBean.ResultBean.Line1Bean line1Bean = mHLVDatas.get(position);
                startThemeActivity(line1Bean.getM_topics_title(), "", line1Bean.getM_topics_id());
            }
        });

        //广告1
        mAd1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByTopicBean.ResultBean.Line2Bean line2Bean = mAdDatas.get(0);
                startThemeActivity(line2Bean.getM_topics_title(), "", line2Bean.getM_topics_id());
            }
        });
        mNoGPSAd1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByTopicBean.ResultBean.Line2Bean line2Bean = mAdDatas.get(0);
                startThemeActivity(line2Bean.getM_topics_title(), "", line2Bean.getM_topics_id());
            }
        });
        mNoShopAd1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByTopicBean.ResultBean.Line2Bean line2Bean = mAdDatas.get(0);
                startThemeActivity(line2Bean.getM_topics_title(), "", line2Bean.getM_topics_id());
            }
        });

        //广告2
        mAd2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByTopicBean.ResultBean.Line2Bean line2Bean = mAdDatas.get(1);
                startThemeActivity(line2Bean.getM_topics_title(), "", line2Bean.getM_topics_id());
            }
        });
        mNoGPSAd2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByTopicBean.ResultBean.Line2Bean line2Bean = mAdDatas.get(1);
                startThemeActivity(line2Bean.getM_topics_title(), "", line2Bean.getM_topics_id());
            }
        });
        mNoShopAd2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByTopicBean.ResultBean.Line2Bean line2Bean = mAdDatas.get(1);
                startThemeActivity(line2Bean.getM_topics_title(), "", line2Bean.getM_topics_id());
            }
        });

        //跳转到设置gps界面
        mOpenGpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.noPermissionStatus(mContext, "定位权限未开");
            }
        });
    }

    /**
     * 主题活动的跳转
     *
     * @param oneTheme 主标题
     * @param twoTheme 副标题
     * @param cate_id  主题活动id
     */
    private void startThemeActivity(String oneTheme, String twoTheme, String cate_id) {
        Intent themeLeft = new Intent(mContext, LBSThemeActivity.class);
        themeLeft.putExtra("oneTheme", oneTheme);//主标题
        themeLeft.putExtra("twoTheme", twoTheme);//副标题
        themeLeft.putExtra("cate_id", cate_id);//主题分类id
        startActivity(themeLeft);
    }

    /**
     * 重新加载数据:无网络,下拉刷新
     */
    private void getDataAgain() {
        if (mNearbyRvDatas != null) {
            mNearbyRvDatas.clear();
            mNearByAdapter.notifyDataSetChanged();
        }
        //有网络
        if (NetworkUtils.isConnectNet(mContext)) {
            //定位失败
            isRefresh = true;
            //获取活动主题
            NearByHandler.getNearLbsTopic(handle);
        } else {
            //无网络
            ToastUtil.show(mActivity, getString(R.string.offline));
            setAllViewGone();
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            //没有附近商家
            case RequestCodeConstant.NEARBY_REQUEST_NO_STORE_TAG:
                LoadDialog.dismiss(mContext);
                setAllViewGone();
                mNearby_no_shop.setVisibility(View.VISIBLE);
                nearby_swipe_refresh.setRefreshing(false);
                break;
            //附近商家
            case RequestCodeConstant.NEARBY_REQUEST_TAG:
                handleContentData((NearbyStoreBean) msg.obj);
                break;
            //主题广告
            case RequestCodeConstant.NEARBY_TOPIC_REQUEST_TAG:
                handleTopicData((NearByTopicBean) msg.obj);
                break;
        }
    }

    /**
     * 处理主题广告
     *
     * @param nearByTopicBean
     */
    private void handleTopicData(NearByTopicBean nearByTopicBean) {
        if (nearByTopicBean == null) {
            //连接超时
            setAllViewGone();
            noNetView.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, getString(R.string.timeout), Toast.LENGTH_LONG).show();
        } else {
            //有数据
            if (nearByTopicBean.getCode().equals("0")) {
                GBLogUtils.DEBUG_DISPLAY("onLoadMoreRequested:", "33");
                setTopData(nearByTopicBean, mNoShopBanner, mNoShopHorizontalListView, mNoShopAd1_iv, mNoShopAd2_iv);
                setTopData(nearByTopicBean, mNoGPSBanner, mNoGPSHorizontalListView, mNoGPSAd1_iv, mNoGPSAd2_iv);
                if (isRefresh) {
                    //刷新使用当前全局的经纬度（无论是否手动定位）
                    NearByHandler.takeNearByShop(handle, mPage, mLbsLocationBean.getLongitude(), mLbsLocationBean.getLatitude());
                   /* if (isManualLocation) {
                        //手动定位  刷新仍是该定位获取的经纬度
                        NearByHandler.takeNearByShop(handle, mPage, mLbsLocationBean.getLongitude(), mLbsLocationBean.getLatitude());
                    } else {
                        //没有手动定位 刷新时获取当前经纬度
                        //locationService.start();
                        NearByHandler.takeNearByShop(handle, mPage, mLbsLocationBean.getLongitude(), mLbsLocationBean.getLatitude());
                    }*/
                } else {
                    if (!GBDLocation.getInstance().isHasAdd()) {
                        LoadDialog.dismiss(mContext);
                        setAllViewGone();
                        mNo_open_gps.setVisibility(View.VISIBLE);
                    } else {
                        //NearByHandler.takeNearByShop(handle, mPage, GBDLocation.getInstance().getLongitude(), GBDLocation.getInstance().getLatitude());
                        //不是下拉刷新 且定位权限开启
                        locationService.start();
                    }
                }
                setTopData(nearByTopicBean, mBanner, mHorizontalListView, mAd1_iv, mAd2_iv);
            } else {
                setAllViewGone();
                noNetView.setVisibility(View.VISIBLE);
            }
        }
    }

    //TODO 参数添加
    private void setTopData(NearByTopicBean nearByTopicBean, BGABanner banner, HorizontalListView horizontalListView, ImageView ad1_iv, ImageView ad2_iv) {
        initHeadData(banner);
        NearByTopicBean.ResultBean result = nearByTopicBean.getResult();
        mHLVDatas = (ArrayList<NearByTopicBean.ResultBean.Line1Bean>) result.getLine1();
        mAdDatas = result.getLine2();
        mBannerData = result.getBan();
        //设置数据
        //banner
        banner.setData(mBannerData, null);
        banner.setAutoPlayInterval(5000);
        //主题
        horizontalListView.setAdapter(new HorizontalListViewAdapter(mHLVDatas));
        //广告
        GlideUtil.getInstance().loadImage(mContext, mAdDatas.get(0).getM_topics_pic_url(), R.drawable.little_banner_bg, R.drawable.little_banner_error_bg, ad1_iv);
        GlideUtil.getInstance().loadImage(mContext, mAdDatas.get(1).getM_topics_pic_url(), R.drawable.little_banner_bg, R.drawable.little_banner_error_bg, ad2_iv);
    }

    private boolean isCanBuy = true;

    /**
     * 处理内容数据
     *
     * @param nearByBean
     */
    private void handleContentData(NearbyStoreBean nearByBean) {
        if (nearByBean == null) {
            //连接超时
            setAllViewGone();
            noNetView.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, getString(R.string.timeout), Toast.LENGTH_LONG).show();
        } else {
            //无附近商家
            if (nearByBean.getTotals() == 0) {
                if (1 != mPage) {
                    mNearByAdapter.loadMoreEnd();
                } else {
                    setAllViewGone();
                    mNearby_no_shop.setVisibility(View.VISIBLE);
                }

            } else {
                //是否爬虫数据 （空不是 "NO"是爬虫数据）
                String buy_flag = nearByBean.getBuy_flag();
                if (!TextUtils.isEmpty(buy_flag)) {
                    isCanBuy = false;
                } else {
                    isCanBuy = true;
                }
                setAllViewGone();
                //附近有商家
                mSuccessView.setVisibility(View.VISIBLE);
                int total_page = nearByBean.getTotal_page();
                List<NearbyStoreBean.StoreListsBean> store_lists = nearByBean.getStore_lists();
                mNearbyRvDatas.addAll(store_lists);
                if (total_page == 0) {
                    mNearByAdapter.setData(mNearbyRvDatas);
                    mNearby_rv.setAdapter(mNearByAdapter);
                    mNearByAdapter.setEnableLoadMore(false);
                    mNearByAdapter.loadMoreEnd();
                    nearby_swipe_refresh.setRefreshing(false);
                    LoadDialog.dismiss(mContext);
                    return;
                }
                //有更多
                if (mPage <= total_page) {
                    //第一次加载
                    if (mPage == 1) {
                        mNearByAdapter.setData(mNearbyRvDatas);
                        mNearby_rv.setAdapter(mNearByAdapter);
                        if (mPage == total_page) {
                            mNearByAdapter.loadMoreEnd(false);
                            LoadDialog.dismiss(mContext);
                            return;
                        }
                        // 非第一次加载
                    } else {
                        mNearByAdapter.addData(store_lists);
                        // mNearByAdapter.notifyDataSetChanged();
                    }
                    mNearByAdapter.loadMoreComplete();
                    mNearByAdapter.setEnableLoadMore(true);
                    //没更多
                } else {
                    mNearByAdapter.loadMoreEnd();
                }

            }
            nearby_swipe_refresh.setRefreshing(false);

        }
        LoadDialog.dismiss(mContext);
    }

    /*****
     *
     * 定位结果回调
     *
     */

    private class MyBDLocationListener extends IBaseLocationListener {

        //定位成功
        @Override
        protected void onResponse(LbsLocationBean lbsLocationBean) {
          // if (isRefresh)
            NearByHandler.takeNearByShop(handle, mPage, lbsLocationBean.getLongitude(), lbsLocationBean.getLatitude());

            mNearby_title_lbs_tv.setText(lbsLocationBean.getDescribe());
            //保存数据
            mLbsLocationBean.setLatitude(lbsLocationBean.getLatitude());
            mLbsLocationBean.setLongitude(lbsLocationBean.getLongitude());
            mLbsLocationBean.setCity(lbsLocationBean.getCity());
            mLbsLocationBean.setDescribe(lbsLocationBean.getDescribe());
            locationService.stop();
        }

        //定位失败
        @Override
        protected void onError() {
            nearby_swipe_refresh.setRefreshing(false);
            if (isRefresh) {
                setAllViewGone();
                mNo_open_gps.setVisibility(View.VISIBLE);
            } else {
                mNearby_title_lbs_tv.setText(getString(R.string.location_failed));
            }
            locationService.stop();
        }
    }

    /**
     * 下拉加载更多监听回调
     */
    @Override
    public void onRefresh() {
        // TODO: 2017/5/11  去刷新
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            mNearByAdapter.setEnableLoadMore(false);  //下拉刷新 屏蔽上拉加载更多
            //isManualLocation = false;
            mPage = 1;
            getDataAgain();
        } else {
            nearby_swipe_refresh.setRefreshing(false);
            ToastUtil.showToast(mContext, getString(R.string.feedback_submit_error));
        }

    }

    /**
     * 上拉加载更多监听回调
     */
    @Override
    public void onLoadMoreRequested() {
        if (mNearByAdapter.isLoadMoreEnable()) {
            nearby_swipe_refresh.setRefreshing(false);
            if (NetworkUtils.isConnectNet(mContext)) {
                mPage++;
                NearByHandler.takeNearByShop(handle, mPage, mLbsLocationBean.getLongitude(), mLbsLocationBean.getLatitude());
                /*if (mLbsLocationBean.getLongitude() != 0.0d) {
                    GBLogUtils.DEBUG_DISPLAY("onLoadMoreRequested:", "if" + mLbsLocationBean.getLongitude() + mLbsLocationBean.getDescribe());
                    NearByHandler.takeNearByShop(handle, mPage, mLbsLocationBean.getLongitude(), mLbsLocationBean.getLatitude());
                } else {
                    GBLogUtils.DEBUG_DISPLAY("onLoadMoreRequested:", "else" + GBDLocation.getInstance().getLongitude());
                    NearByHandler.takeNearByShop(handle, mPage, GBDLocation.getInstance().getLongitude(), GBDLocation.getInstance().getLatitude());
                }*/
            } else {
                ToastUtil.show(mActivity, getString(R.string.offline));
                mNearByAdapter.loadMoreFail();
            }
        }
    }

    /*----------------------横向listView的adapter-----------------------*/
    private class HorizontalListViewAdapter extends BaseAdapter {
        private ArrayList<NearByTopicBean.ResultBean.Line1Bean> mData;

        public HorizontalListViewAdapter(ArrayList<NearByTopicBean.ResultBean.Line1Bean> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.horizontal_listview_item, null);
            ImageView iv = (ImageView) inflate.findViewById(R.id.iv);
            GlideUtil.getInstance().loadImage(mContext, mData.get(position).getM_topics_pic_url(), R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, iv);
            return inflate;
        }
    }
}
