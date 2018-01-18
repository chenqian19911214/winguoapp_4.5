package com.winguo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.GBAccountStatus;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.account.WinguoAccountImpl;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.webshell.BrowserActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.winguo.R;
import com.winguo.activity.HomeClassify1Activity;
import com.winguo.activity.HomeClassify2Activity;
import com.winguo.activity.ProductActivity;
import com.winguo.activity.Search2Activity;
import com.winguo.activity.StoreSearchActivity;
import com.winguo.activity.ThemeActivity;
import com.winguo.adapter.PullToRefreshAdapter;
import com.winguo.adapter.RecyclerAdapter;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragment;
import com.winguo.bean.ThemeProductBean;
import com.winguo.bean.TodayShop;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.home.bean.BannerBean;
import com.winguo.home.BannerRequestNet;
import com.winguo.home.bean.ItemsBean;
import com.winguo.net.GlideUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.HomeDatahandle;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;
import com.winguo.view.IGridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 首页
 */

public class HomePageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    public static String TAG = HomePageFragment.class.getSimpleName();
    //商品分类
    List<Map<String, Object>> comm_data = new ArrayList<>();
    private IGridView shopClassify;
    private SimpleAdapter commAdapter;
    private PullToRefreshAdapter pullToRefreshAdapter;

    private MyBroadcastReceiver receiver;
    private BGABanner home_page_banner_content, home_page_self_run_banner;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FrameLayout container;
    //猜你喜欢
    private RecyclerView today_shop_rv, home_page_snack_rv, home_page_beauty_rv;
    private ImageView home_page_snack_shop, home_page_beauty_shop;
    private List<TodayShop> todayShops = new ArrayList<>();
    private int total_size;
    private View headView;
    private CustomLoadMoreView loadMoreView;
    private View noNetView;
    private Button btn_request_net;
    private TextView home_page_title_search_tv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.AUTO_REFRESH);
        receiver = new MyBroadcastReceiver();
        context.registerReceiver(receiver, intentFilter);
    }


    private void takeHomeData() {
        //检测网络状态
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            //广告位数据获取
            takeAdvData();
        } else {
            LoadDialog.dismiss(mContext);
            noNetView.setVisibility(View.VISIBLE);
            ToastUtil.showToast(mContext, mContext.getString(R.string.no_net));
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.home_fragment_layout;
    }


    @Override
    protected void initData() {
        CommonUtil.stateSetting(context, R.color.title_color);
        // 正式版 测试版数据切换时 退出上次登录并清除数据
        boolean isChange = (boolean) SPUtils.get(mContext.getApplicationContext(), "isChange", false);
        if (isChange) {
            WinguoAccountImpl.changeDebugLogout(mContext, true);
        }

    }

    /**
     * 从资源文件（comm_faction_array.xml）中读取存放的（常用功能--图片 文本）数据
     */
    private void shopClassifyData() {
        //从res 资源文件中读取 存放数据（图片.文本）
        String[] comm_iconNames = resources.getStringArray(R.array.home_page_shop_classify_name);
        TypedArray commTA = resources.obtainTypedArray(R.array.home_page_shop_classify_icons);
        int[] comm_icons = new int[commTA.length()];
        for (int i = 0; i < commTA.length(); i++) {
            comm_icons[i] = commTA.getResourceId(i, 0);
        }
        commTA.recycle();//释放资源
        for (int i = 0; i < comm_icons.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", comm_icons[i]);
            map.put("text", comm_iconNames[i]);
            comm_data.add(map);
        }
    }


    /**
     * 获取广告位数据
     */
    private void takeAdvData() {
        BannerRequestNet net = new BannerRequestNet();
        net.getBannerData(mHandler);
    }


    private BannerBean bannerBean;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                //广告位数据
                case RequestCodeConstant.BANNER_MAGESS:
                    bannerBean = (BannerBean) msg.obj;
                    //广告轮播数据
                    initBannerAdapter();
                    //获取 猜你喜欢数据
                    HomeDatahandle.takeShopData(mHandler);
                    break;
                case RequestCodeConstant.REQUEST_BANNER_CODE_FALSE:
                    LoadDialog.dismiss(mContext);
                    //请求失败 重新加载
                    mSwipeRefreshLayout.setRefreshing(false);
                    today_shop_rv.setVisibility(View.GONE);
                    noNetView.setVisibility(View.VISIBLE);
                    ToastUtil.showToast(mContext, mContext.getString(R.string.no_net_or_service_no));
                    break;

                case HomeDatahandle.TODAY_SHOPP_FLAG_SUCCESS:
                    LoadDialog.dismiss(mContext);
                    if (todayShops.isEmpty()) {
                        todayShops.addAll((List<TodayShop>) msg.obj);
                    } else {
                        todayShops.clear();
                        todayShops.addAll((List<TodayShop>) msg.obj);
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                    total_size = todayShops.size();
                    if (pullToRefreshAdapter == null) {
                        initAdapter();
                    } else {
                        pullToRefreshAdapter.setEnableLoadMore(true);
                        pullToRefreshAdapter.setNewData(todayShops);
                        // pullToRefreshAdapter.notifyDataSetChanged();
                    }
                    break;
                case HomeDatahandle.TODAY_SHOPP_FLAG_FALSE:
                    //请求失败 重新加载
                    LoadDialog.dismiss(mContext);
                    noNetView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    today_shop_rv.setVisibility(View.GONE);
                    ToastUtil.showToast(mContext, mContext.getString(R.string.no_net_or_service_no));

                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 初始化广告位适配器
     */
    private void initBannerAdapter() {
        if (bannerBean.message != null) {
            if (bannerBean.message.items != null) {
                if (bannerBean.message.items.data.size() > 0) {
                    GBLogUtils.DEBUG_DISPLAY("图片shuju ", bannerBean.toString());
                    List<ItemsBean.DataBean> data = bannerBean.message.items.data;
                    home_page_banner_content.setAdapter(new BGABanner.Adapter<ImageView, ItemsBean.DataBean>() {
                        @Override
                        public void fillBannerItem(BGABanner banner, ImageView itemView, ItemsBean.DataBean model, int position) {
                            GlideUtil.getInstance().loadImage(context, model.image.content, R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, itemView);
                            final String url = model.url;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO: 2017/5/18  轮播点击
                                    if (!TextUtils.isEmpty(url)) {
                                        Intent it = new Intent(mContext, BrowserActivity.class);
                                        it.putExtra("TargetUrl", url);
                                        startActivity(it);
                                    }
                                }
                            });
                        }
                    });
                    home_page_banner_content.setData(data, null);
                    home_page_banner_content.setAutoPlayInterval(5000);
                }
            }
        }
    }


    /**
     * 通过资源名字获取id
     *
     * @param name
     * @return
     */
    private int getImageResourceId(String name) {
        R.drawable drawables = new R.drawable();
        //默认的id
        int resId = 0x7f02000b;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            Field field = R.drawable.class.getField(name);
            //取值
            resId = (Integer) field.get(drawables);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;
    }


    @Override
    protected void initView(View view) {
        container = (FrameLayout) view.findViewById(R.id.home_content_container);
        //无网络
        //(给容器添加布局)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        noNetView = View.inflate(mContext, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        noNetView.setVisibility(View.GONE);
        container.addView(noNetView, params);

        today_shop_rv = (RecyclerView) view.findViewById(R.id.today_shop_rv);

        ViewGroup parent = (ViewGroup) today_shop_rv.getParent();
        headView = context.getLayoutInflater().inflate(R.layout.home_page_fragment_head, parent, false);
        home_page_title_search_tv = (TextView) headView.findViewById(R.id.home_page_title_search_tv);

        //广告位
        home_page_banner_content = (BGABanner) headView.findViewById(R.id.home_page_banner_content);

        //零食年货
        initSnack();
        //美妆护肤
        initBeauty();
        //商品分类
        shopClassify = (IGridView) headView.findViewById(R.id.home_comm_faction_gv);
        //商品分类适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.gv_item_ic, R.id.gv_item_name};
        commAdapter = new SimpleAdapter(context, comm_data, R.layout.comm_faction_gv_item, from, to);
        shopClassify.setAdapter(commAdapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        //设置样式刷新显示的位置
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);

        //猜你喜欢的商品推荐

        today_shop_rv.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        today_shop_rv.setItemAnimator(new DefaultItemAnimator());
        today_shop_rv.addItemDecoration(new SpacesItemDecoration(ScreenUtil.dipToPx(mContext,3)));

        //获取分类数据 本地资源
        shopClassifyData();

        if (SPUtils.contains(context, "accountName")) {  //登录状态
            if (NetWorkUtil.isNetworkAvailable(mContext)) {
                //自动登录
                String pizza = WinguoAccountDataMgr.getPizza(context);//微信登录 未保存密码为null  否则为正常自动登录
                if (pizza == null) {
                    weiXinOrSmsAutoLogin();
                } else {
                    autoLogin();
                }
            } else {
                noNetView.setVisibility(View.VISIBLE);
            }
        } else {  //未登录状态
            LoadDialog.show(mContext, true);
            takeHomeData();
        }

    }

    private void initSnack() {

        List<ThemeProductBean> snackData = new ArrayList<>();
        snackData.add(new ThemeProductBean("蟹黄蚕豆","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEiqAL2X6AAMxFyc9Aj467..png"));
        snackData.add(new ThemeProductBean("蟹黄瓜子仁","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEiqAVn_PAAMsZTYFiPk05..png"));
        snackData.add(new ThemeProductBean("综合豆果","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEiqAAa4rAALQE9zQwS073..png"));
        snackData.add(new ThemeProductBean("综合果仁","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEiuAGQ6IAAK17LEAy-k59..png"));

        home_page_snack_shop = (ImageView) headView.findViewById(R.id.home_page_snack_shop);
        GlideUtil.getInstance().loadImage(context, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYF3SALMzEAAKviHkPqSI35..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, home_page_snack_shop);

        home_page_snack_rv = (RecyclerView) headView.findViewById(R.id.home_page_snack_rv);
        home_page_snack_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        home_page_snack_rv.setItemAnimator(new DefaultItemAnimator());
       // home_page_snack_rv.addItemDecoration(new SpacesItemDecoration(ScreenUtil.dipToPx(mContext, 2)));
       RecyclerCommonAdapter<ThemeProductBean> adapter =  new RecyclerCommonAdapter<ThemeProductBean>(mContext, R.layout.home_page_snack_beauty_item, snackData) {
            @Override
            protected void convert(RecylcerViewHolder holder, ThemeProductBean s, int position) {
                holder.setText(R.id.home_page_classify_item_name, s.getName());
                ImageView itemView = holder.getView(R.id.home_page_classify_item_ic);
                GlideUtil.getInstance().loadImage(context, s.getIcURL(), R.drawable.little_theme_loading_bg, R.drawable.little_theme_error_bg, itemView);
            }
        };
        home_page_snack_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent productActivity = new Intent(mContext, ProductActivity.class);
                switch (position) {
                    case 0:
                        productActivity.putExtra("gid", 89842);
                        break;
                    case 1:
                        productActivity.putExtra("gid", 89843);
                        break;
                    case 2:
                        productActivity.putExtra("gid", 89844);
                        break;
                    case 3:
                        productActivity.putExtra("gid", 89845);
                        break;
                }
                startActivity(productActivity);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initBeauty() {
        //零食年货
        List<ThemeProductBean> beautyData = new ArrayList<>();
        beautyData.add(new ThemeProductBean("角蛋白补充洗发乳","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEimAVCYAAADH8S9VmZ889..png"));
        beautyData.add(new ThemeProductBean("玻尿酸补水面膜","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEimAB21PAAFSYKT0ofs39..png"));
        beautyData.add(new ThemeProductBean("补水舒缓冰膜","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEimAAuA6AAE4u6TxZCw00..png"));
        beautyData.add(new ThemeProductBean("雨洛芦荟胶","http://g1.img.winguo.com/group1/M00/06/77/wKgAUVpYEiqALMbKAABmfS5ALC802..png"));

        home_page_beauty_shop = (ImageView) headView.findViewById(R.id.home_page_beauty_shop);
        GlideUtil.getInstance().loadImage(context, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYF3SAA2pzAAIwBwSKvtY81..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, home_page_beauty_shop);

        home_page_beauty_rv = (RecyclerView) headView.findViewById(R.id.home_page_beauty_rv);
        home_page_beauty_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        home_page_beauty_rv.setItemAnimator(new DefaultItemAnimator());
       // home_page_beauty_rv.addItemDecoration(new SpacesItemDecoration(ScreenUtil.dipToPx(mContext, 2)));
       RecyclerCommonAdapter<ThemeProductBean> adapter =  new RecyclerCommonAdapter<ThemeProductBean>(mContext, R.layout.home_page_snack_beauty_item, beautyData) {
            @Override
            protected void convert(RecylcerViewHolder holder, ThemeProductBean s, int position) {
                holder.setText(R.id.home_page_classify_item_name, s.getName());
                ImageView itemView = holder.getView(R.id.home_page_classify_item_ic);
                GlideUtil.getInstance().loadImage(context, s.getIcURL(), R.drawable.little_theme_loading_bg, R.drawable.little_theme_error_bg, itemView);
            }
        };
        home_page_beauty_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent productActivity = new Intent(mContext, ProductActivity.class);
                switch (position) {
                    case 0:
                        productActivity.putExtra("gid", 89778);
                        break;
                    case 1:
                        productActivity.putExtra("gid", 89830);
                        break;
                    case 2:
                        productActivity.putExtra("gid", 89828);
                        break;
                    case 3:
                        productActivity.putExtra("gid", 89783);
                        break;
                }
                startActivity(productActivity);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        LoadDialog.show(mContext, true);
        WinguoAcccountManagerUtils.getKey(mContext, new WinguoAcccountManagerUtils.IPublicKey() {
            @Override
            public void publicKey(WinguoAccountKey key) {
                WinguoAccountDataMgr.setUUID(mContext, key.getUUID());
                WinguoAccountDataMgr.setTOKEN(mContext, key.getToken());
                WinguoAccountDataMgr.setKEY(mContext, key.getKey());
                String hashCommon = "id=" + WinguoAccountDataMgr.getUserName(mContext) + "&token=" + key.getToken() + "&uuid=" + key.getUUID();
                hashCommon = WinguoEncryption.commonEncryption(hashCommon, key);
                WinguoAccountDataMgr.setHashCommon(mContext, hashCommon);
                // 自动登录
                WinguoAcccountManagerUtils.autoLogin(context, new WinguoAcccountManagerUtils.IRequestAutoLogin() {
                    @Override
                    public void requestAutoLogin(String statu) {
                        if (!"error".equals(statu)) {
                            //自动登录成功 获取个人数据
                            WinguoAcccountManagerUtils.takeWinguoAccountGeneral(mContext, new WinguoAcccountManagerUtils.IGeneralData() {
                                @Override
                                public void getGeneral(WinguoAccountGeneral winguoAccountGeneral) {
                                    if (winguoAccountGeneral != null) {
                                        GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = winguoAccountGeneral;
                                        GBAccountMgr.getInstance().mAccountInfo.status = GBAccountStatus.usr_logged;
                                        //登陆成功后发出一条广播
                                        Intent intent = new Intent(Constants.LOGIN_SUCCESS);
                                        intent.putExtra("info", winguoAccountGeneral);
                                        mContext.sendBroadcast(intent);
                                        //获取首页数据
                                        takeHomeData();
                                    }
                                }

                                @Override
                                public void generalDataErrorMsg(int message) {
                                    // LoadDialog.dismiss(mContext);
                                    //  noNetView.setVisibility(View.VISIBLE);
                                    loginFail();
                                }
                            });

                        } else {
                            //LoadDialog.dismiss(mContext);
                            //noNetView.setVisibility(View.VISIBLE);
                            //自动登录失败 直接获取首页数据
                            loginFail();
                        }
                    }

                    @Override
                    public void requestAutoLoginErrorMsg(int message) {
                        // LoadDialog.dismiss(mContext);
                        //ToastUtil.showToast(mContext, GBAccountError.getErrorMsg(mContext, message));
                        //noNetView.setVisibility(View.VISIBLE);
                        loginFail();
                    }
                });

            }

            @Override
            public void publicKeyErrorMsg(int error) {
                //请求key 超时
                //LoadDialog.dismiss(mContext);
                //ToastUtil.showToast(mContext, mContext.getString(R.string.no_net_or_service_no));
                loginFail();
            }
        });


    }

    /**
     * 自动登录（微信、短信）失败（获取公钥、获取个人信息失败）
     * 获取首页信息 并清除登录信息
     */
    private void loginFail() {
        takeHomeData();
        //清除本地数据  自动登录失败
        Intent filter = new Intent();
        filter.setAction(Constants.QUIT_SUCCESS);
        mContext.sendBroadcast(filter);
    }

    /**
     * 微信自动登录
     * 短信验证码登录
     */
    private void weiXinOrSmsAutoLogin() {
        LoadDialog.show(mContext, true);
        //自动登录成功 获取个人数据
        WinguoAcccountManagerUtils.requestWeixinInfo(mContext, new WinguoAcccountManagerUtils.IGeneralData() {
            @Override
            public void getGeneral(WinguoAccountGeneral winguoAccountGeneral) {
                if (winguoAccountGeneral != null) {
                    GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = winguoAccountGeneral;
                    GBAccountMgr.getInstance().mAccountInfo.status = GBAccountStatus.usr_logged;
                    //登陆成功后发出一条广播
                    Intent intent = new Intent(Constants.LOGIN_SUCCESS);
                    intent.putExtra("info", winguoAccountGeneral);
                    mContext.sendBroadcast(intent);
                    //获取首页数据
                    takeHomeData();
                }
            }

            @Override
            public void generalDataErrorMsg(int message) {
                // LoadDialog.dismiss(mContext);
                //  noNetView.setVisibility(View.VISIBLE);
                //takeHomeData();
                //ToastUtil.showToast(context,GBAccountError.getErrorMsg(context,message));
                loginFail();
                switch (message) {
                    case GBAccountError.SESSION_TIMEOUT:
                        //微信登陆过期  直接跳转授权页面
                        wxLogin();
                        break;
                }
            }
        });


    }

    /**
     * 调用第三方微信登录
     */
    public void wxLogin() {
        if (!StartApp.mWxApi.isWXAppInstalled()) {
            ToastUtil.showToast(context, "您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        StartApp.mWxApi.sendReq(req);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {

        pullToRefreshAdapter = new PullToRefreshAdapter(R.layout.guess_you_like_item, todayShops);
        pullToRefreshAdapter.setOnLoadMoreListener(this, today_shop_rv);
        pullToRefreshAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);
        loadMoreView = new CustomLoadMoreView();
        pullToRefreshAdapter.setLoadMoreView(loadMoreView);
        today_shop_rv.setAdapter(pullToRefreshAdapter);
        mCurrentCounter = pullToRefreshAdapter.getData().size();

        today_shop_rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                // Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_LONG).show();
                TodayShop todayShop = todayShops.get(position);
                //String detailUrl = "http://m.winguo.com/goods/goodsinfo?gid=" + todayShop.getT_goods_m_item_id();
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("gid", todayShop.getT_goods_m_item_id());
                startActivity(intent);
            }
        });

        pullToRefreshAdapter.addHeaderView(headView);
    }


    @Override
    protected void setListener() {
        btn_request_net.setOnClickListener(this);
        home_page_snack_shop.setOnClickListener(this);
        home_page_beauty_shop.setOnClickListener(this);
        home_page_title_search_tv.setOnClickListener(this);

        //一个生活服务activity  viewpager 8个页面
        shopClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        // TODO: 2018/1/9  服饰鞋包
                        Intent clothes = new Intent(mContext, HomeClassify1Activity.class);
                        clothes.putExtra("cid", 100007684);
                        clothes.putExtra("type", Constants.TYPE_CLOTHES);
                        startActivity(clothes);

                        break;
                    case 1:
                        // TODO: 2018/1/9  中外名酒

                        Intent wine = new Intent(mContext, HomeClassify1Activity.class);
                        wine.putExtra("cid", 12259);
                        wine.putExtra("type", Constants.TYPE_WINE);
                        startActivity(wine);


                        break;
                    case 2:
                        // TODO: 2018/1/9  家居日用
                        Intent cases = new Intent(mContext, HomeClassify1Activity.class);
                        cases.putExtra("cid", 100002860);
                        cases.putExtra("type", Constants.TYPE_DAY_USE);
                        startActivity(cases);

                        break;
                    case 3:
                        // TODO: 2018/1/9  家用电器
                        ArrayList<Integer> cids = new ArrayList<>();
                        cids.add(752);  // 厨房电器
                        cids.add(100007898); //小家电
                        cids.add(794); // 大家电
                        Intent elec = new Intent(mContext, HomeClassify2Activity.class);
                        elec.putIntegerArrayListExtra("cids", cids);
                        elec.putExtra("type", Constants.TYPE_HOUSE_ELE);
                        startActivity(elec);

                        break;
                }
            }
        });
    }

    /**
     * @param v
     */
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.home_page_title_search_tv:
                Intent intent = new Intent(mContext, StoreSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.home_page_snack_shop:
                // TODO: 2018/1/11   零食年货
                ArrayList<Integer> cids = new ArrayList<>();
                cids.add(1320);  // 零食坚果
                cids.add(100003939); // 饮品类
                cids.add(100003941);  // 粮油调料
                Intent snack = new Intent(mContext, HomeClassify2Activity.class);
                snack.putIntegerArrayListExtra("cids", cids);
                snack.putExtra("type", Constants.TYPE_SNACK);
                startActivity(snack);
                break;
            case R.id.home_page_beauty_shop:
                // TODO: 2018/1/11  美妆护肤
                ArrayList<Integer> beautyCids = new ArrayList<>();
                beautyCids.add(1381);  // 面部护理
                beautyCids.add(100007694); // 个人护理
                beautyCids.add(1386);  // 洗护用品
                Intent beauty = new Intent(mContext, HomeClassify2Activity.class);
                beauty.putIntegerArrayListExtra("cids", beautyCids);
                beauty.putExtra("type", Constants.TYPE_BEAUTY);
                startActivity(beauty);
                break;
            case R.id.btn_request_net:
                // 网络异常
                noNetView.setVisibility(View.GONE);
                if (SPUtils.contains(context, "accountName")) {
                    //登录状态
                    if (NetWorkUtil.isNetworkAvailable(context)) {
                        //自动登录
                        String pizza = WinguoAccountDataMgr.getPizza(context);//微信登录 未保存密码为null  否则为正常自动登录
                        if (pizza == null) {
                            weiXinOrSmsAutoLogin();
                        } else {
                            autoLogin();
                        }
                    } else {
                        noNetView.setVisibility(View.VISIBLE);
                    }
                } else {
                    //未登录
                    LoadDialog.show(mContext, true);
                    takeHomeData();
                }
                break;

        }
    }

    /**
     * 主题活动的跳转
     *
     * @param oneTheme 主标题
     * @param twoTheme 副标题
     * @param cate_id  主题活动id
     */
    private void startThemeActivity(String oneTheme, String twoTheme, String cate_id) {
        Intent themeLeft = new Intent(mContext, ThemeActivity.class);
        themeLeft.putExtra("oneTheme", oneTheme);//主标题
        themeLeft.putExtra("twoTheme", twoTheme);//副标题
        themeLeft.putExtra("cate_id", cate_id);//主题分类id
        startActivity(themeLeft);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == VOICE_RESULT) {
            String voiceRes = data.getStringExtra("rawinput");//语音解释出的结果

            Intent it = new Intent(context, Search2Activity.class);
            it.putExtra("key", voiceRes);
            it.putExtra("type", "voice");
            startActivity(it);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean mLoadMoreEndGone = false;

    @Override
    public void onRefresh() {
        // TODO: 2017/5/11  去刷新
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            if (pullToRefreshAdapter != null)
                pullToRefreshAdapter.setEnableLoadMore(false);  //下拉刷新 屏蔽上拉加载更多
            takeAdvData();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            ToastUtil.showToast(context, getString(R.string.feedback_submit_error));
        }
    }


    @Override
    public void onLoadMoreRequested() {
        // TODO: 2017/5/11  加载更多
        mSwipeRefreshLayout.setEnabled(false);
        if (pullToRefreshAdapter.getData().size() < PAGE_SIZE) {
            //如果数据少于规定分页数据 显示没有更多
            pullToRefreshAdapter.loadMoreEnd(true);
        } else {
            if (mCurrentCounter >= total_size) {
                //如果当前数据量展示完毕 显示没有更多
                pullToRefreshAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visibl
            }
        }
        mSwipeRefreshLayout.setEnabled(true);

    }


    /**
     * 接受广播 登录后用户信息
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {

            String action = intent.getAction();

            //网络连接上后 自动刷新
            if (action.equals(Constants.AUTO_REFRESH)) {
                if (noNetView.getVisibility() == View.VISIBLE) {
                    noNetView.setVisibility(View.GONE);
                    if (SPUtils.contains(context, "accountName")) {
                        //自动登录
                        String pizza = WinguoAccountDataMgr.getPizza(context);//微信登录 未保存密码为null  否则为正常自动登录
                        if (pizza == null) {
                            weiXinOrSmsAutoLogin();
                        } else {
                            autoLogin();
                        }
                    } else {
                        takeHomeData();
                    }
                }
            }
        }
    }


}
