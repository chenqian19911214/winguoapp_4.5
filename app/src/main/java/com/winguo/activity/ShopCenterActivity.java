package com.winguo.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;
import com.guobi.gfc.gbmiscutils.thread.RunnableBus;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;
import com.winguo.utils.MobSharedUtils;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WebViewQRUtils;
import com.winguo.view.CustomWebView;
import com.winguo.view.SharedPopWindow;
import com.winguo.view.ShopMorePopWindow;
import com.winguo.view.WaveProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by admin on 2017/1/22.
 * webview  我的商品店铺（空间站）
 */
@Deprecated
public class ShopCenterActivity extends BaseActivity implements CustomWebView.LongClickCallBack {
    private final static String TAG = ShopCenterActivity.class.getSimpleName();
    private static final int WAIT_TIME_OUT_MS = 8 * 1000;
    private Timer mTimer = null;
    private Timer timer = null;
    private final RunnableBus mRBus = RunnableBus.getInstance();
    private CustomWebView webView;
    private ImageView topBack;
    private TextView webTitle;
    private ImageView topMore;
    private ProgressBar mProgress;
    private ShopMorePopWindow pop;
    private ImageView backHome;
    private SharedPopWindow sharedPopWindow;
    private String userShopURL= "http://m.winguo.com/";
    private Button noNetReload;
    private View noNetLayout;
    private int screenWidth,screenHeight;
    private FrameLayout fl;
    private NetChangeBroadReceiver receiver;
    private WaveProgressView waveProgressView_0;
    @Override
    protected int getLayout() {
        return R.layout.main_shop;
    }

    @Override
    protected void initData() {
        CommonUtil.stateSetting(this,R.color.white_top_color);
        screenWidth = ScreenUtil.getScreenWidth(this);
        screenHeight = ScreenUtil.getScreenHeight(this);
        waveProgressView_0 = new WaveProgressView(this);


        //监听网络变化
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NO_NET_STATU);
        filter.addAction(Constants.NET_STATU_WIFI);
        filter.addAction(Constants.NET_STATU_MOBILE);
        receiver = new NetChangeBroadReceiver();
        registerReceiver(receiver,filter);

        //更多功能
        pop = new ShopMorePopWindow(ShopCenterActivity.this, this);

        //分享菜单初始化
        sharedPopWindow = new SharedPopWindow(mContext,ShopCenterActivity.this);

        //无网络布局
        noNetLayout = mInflater.inflate(R.layout.no_network_layout, null, false);
        noNetLayout.setVisibility(View.GONE);
        //无网络重加载获取
       // noNetLayout = findViewById(R.id.no_network);
       // noNetReload = (Button) findViewById(R.id.no_network_reload);
        noNetReload = (Button) noNetLayout.findViewById(R.id.no_network_reload);

        //判断是否登录
        boolean isLogin = SPUtils.contains(mContext, "accountName");
        if (isLogin) {
            userShopURL = (String) SPUtils.get(mContext, "userShopURL", "http://m.winguo.com");
            Log.i("userShopURL  IS",":"+userShopURL);
        }
    }

    @Override
    protected void initViews() {
        fl = (FrameLayout) findViewById(R.id.main_shop_fl);
        // webView = (WebView) findViewById(R.id.shop_webView);
        FrameLayout.LayoutParams params = null;
        if (ScreenUtil.getScreenWidth(this) == 1080) {
             params = new FrameLayout.LayoutParams(350, 350, Gravity.CENTER);
        } else {
             params = new FrameLayout.LayoutParams(250,250,Gravity.CENTER);
        }
        webView = new CustomWebView(this,this);
        webView.setScrollbarFadingEnabled(true);
        fl.addView(webView);
        fl.addView(noNetLayout);
        fl.addView(waveProgressView_0,params);

        topBack = (ImageView) findViewById(R.id.shop_top_back);
        webTitle = (TextView) findViewById(R.id.shop_url_title);
        topMore = (ImageView) findViewById(R.id.shop_share_refresh);
        mProgress = (ProgressBar) findViewById(R.id.shop_web_progress);
        backHome = (ImageView) findViewById(R.id.shop_top_home);
        //初始化加载页面
        initWebView();
    }


    private void initWebView() {
        //打开网页上的链接 继续使用webview打开网页
        webView.setWebViewClient(new IWebViewClient());
        webView.setWebChromeClient(new IWebChromeClient());
        webView.setClickable(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setPersistentDrawingCache(0);
        webView.setDownloadListener(new IWebViewDownLoadListener());

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
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("TargetUrl");
        if (!TextUtils.isEmpty(url)) {
            Log.i(TAG,"isEmpty"+url);
            webLoadURL(url);
        } else {
            Log.i(TAG," else isEmpty"+userShopURL);
            webLoadURL(userShopURL);//默认
        }
    }

    private void webLoadURL(String url) {
        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            noNetLayout.setVisibility(View.GONE);
            webView.stopLoading();
            webView.goBack();
        } else {
            super.onBackPressed();
        }
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
        if (NetWorkUtil.isNetworkAvailable(this)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 先查找缓存，没有的情况下从网络获取。
        }
        // 可以读取文件缓存(manifest生效)
        settings.setAllowFileAccess(true);

        settings.setSavePassword(false);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath("/data/data/"+mContext.getPackageName()+"/databases/");
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        //settings.setSupportZoom(true); // 支持缩放
        ScreenUtil.setWebView(this,webView);//页面适配屏幕

        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

    }



    @Override
    protected void setListener() {
        topBack.setOnClickListener(this);
        topMore.setOnClickListener(this);
        backHome.setOnClickListener(this);
        noNetReload.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {

        switch (v.getId()) {
            //无网络重新加载
            case R.id.no_network_reload:
                noNetLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(currURL);
                break;
            case R.id.shop_top_back:

                if (webView.canGoBack()) {
                    noNetLayout.setVisibility(View.GONE);
                    webView.stopLoading();
                    webView.goBack();
                } else {
                    ToastUtil.showToast(mContext,"亲，已经返回发现首页！");
                }
                break;
            case R.id.shop_user_feedBack:
                pop.dismiss();
                Intents.feedBack(this,true);
                break;
            //跟多功能 分享 刷新
            case R.id.shop_share_refresh:
                //弹出pop 对话框
                showMenuPop();
                break;
            case R.id.shop_more_refresh:
                webTitle.setVisibility(View.GONE);
                pop.dismiss();
                webView.stopLoading();
                webView.reload();
                break;
            case R.id.shop_more_shared:
                //分享菜单
                sharedShow();
                pop.dismiss();
                break;

            case R.id.shop_top_home:
                finish();
                break;
            //分享
            case R.id.share_dao1_1:
                sharedPopWindow.dismiss();
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                shareShow(QQ.NAME);
                break;
            case R.id.share_dao1_2:
                sharedPopWindow.dismiss();
                shareShow(SinaWeibo.NAME);
                break;
            case R.id.share_dao1_3:
                sharedPopWindow.dismiss();
                shareShow(Wechat.NAME);
                break;
            case R.id.share_dao1_4:
                sharedPopWindow.dismiss();
                shareShow(WechatMoments.NAME);
                break;
        }
    }

    /**
     * 分享集成平台
     * @param platform
     */
    private void shareShow(String platform) {
        Platform plat = ShareSDK.getPlatform(platform);
        if (currURL.equals("")) {
            currURL = userShopURL;
        }
        GBLogUtils.DEBUG_DISPLAY("++++++++++++=shared",""+currURL+":"+currTitle);
        MobSharedUtils.showShopShare(plat.getName(),mContext,currURL,currTitle);
    }

    /**
     * 分享菜单
     */
    private void sharedShow() {
        sharedPopWindow.showAtLocation(webView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

    /**
     *更多菜单
     */
    private void showMenuPop() {
        int[] location = new int[2];
        topMore.getLocationOnScreen(location);
        GBLogUtils.DEBUG_DISPLAY("location++++++++++++++++++++++++++++","location[0]"+location[0]+":"+location[1]+":"+topMore.getWidth());
        pop.showAtLocation(topMore, Gravity.BOTTOM,location[0],screenHeight-location[1]);

    }


    /**
     * 初始化进度条
     * @param visibility
     */
    private void setmProgressBarState(int visibility){
        waveProgressView_0.setVisibility(View.GONE);
        waveProgressView_0.setProgress(0);
        mProgress.setProgress(0);
        mProgress.setVisibility(visibility);
    }

    /**
     * 设置当前进度
     * @param progress
     */
    private void setProgressBarProgress(int progress){
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setProgress(progress);
    }



    /**
     * webView下载监听
     */
    class IWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart","url="+url);
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart","userAgent="+userAgent);
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart","contentDisposition="+contentDisposition);
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart","mimetype="+mimetype);
            }

            if (mimetype.equals("application/vnd.android.package-archive")) {
                String fileName, title;
                int fileNameStart, fileNameEnd;
                fileNameStart = contentDisposition.indexOf("\"") + 1;
                fileNameEnd = contentDisposition.lastIndexOf("\"");
                if (fileNameEnd != -1 && fileNameStart != -1 && fileNameEnd > fileNameStart) {
                    title = fileName = contentDisposition.substring(fileNameStart, fileNameEnd);
                } else {
                    fileNameStart = url.lastIndexOf("/") + 1;
                    title = fileName = url.substring(fileNameStart);
                }
                //开始下载 工具有待加入


            } else {
                Uri uri = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }

        }
    }

    /**
     * 更新进度条 获取网页标题
     */
    class IWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG,"newProgress :"+newProgress);
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
            webTitle.setText(title);
            super.onReceivedTitle(view, title);
        }




    }

    private String currURL = "";
    private String currTitle = "";
    /**
     * WebViewClient
     */
    class IWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          /*  String qq="mqqwpa://im/chat?chat_type=wpa&uin=2567820876";
            if (url.equals(qq)) {
                //检测存在qq app
                if (isAvilible(mContext, "com.tencent.mqq")||isAvilible(mContext, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    ToastUtil.showToast(mContext,"请先安装QQ");
                }
                return true;
            }
            view.loadUrl(url);//点击网页内部链接 继续使用webview加载*/

            Log.i(TAG,"shouldOverrideUrlLoading"+url);
            currURL = url;
            Log.i(TAG,"currURL  shouldOverrideUrlLoading "+currURL);
            // 判断重定向url是不是一个网址 还是一个启动服务
            if (Patterns.WEB_URL.matcher(url).matches()) {
                //符合标准
                view.loadUrl(url);
                return true;
            } else{
                //不符合标准
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

                    ToastUtil.showToast(ShopCenterActivity.this,"找不到"+uri);
                    e.printStackTrace();
                }
                return true;
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           // objectAnimator0.start();
            Log.i(TAG,"onPageStarted"+url);
            if (isFirst) {
                startTimeTask();
            }
            //开始加载页面时调用  -->设定loading页面 告诉用户等待网络响应
            startTimer();
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(TAG,"onPageFinished"+url);
            stopTimeTask();
            cancelTimer();
            //waveProgressView_0.setVisibility(View.GONE);
            //objectAnimator0.cancel();

            webTitle.setText(view.getTitle());
            currTitle = view.getTitle();
            setmProgressBarState(View.GONE);
            GBLogUtils.DEBUG_DISPLAY(mContext,"onPageFinished"+url);
            view.getSettings().setBlockNetworkImage(false);
            //页面加载完成后调用 --> 处理关闭loading条 切换程序动作  加载 完成 可以改变控件 图片资源
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i(TAG,"onReceivedError");
            waveProgressView_0.setVisibility(View.GONE);
            //  objectAnimator0.cancel();
            //报告错误信息  可以处理无网络事件
            //view.loadUrl("html无网络事件");
            noNetLayout.setVisibility(View.VISIBLE);
           // showErrorPage();
            String errorHtml = "<html><head><title>please check net</title></head><body></body></html>";
            webView.loadData(errorHtml, "text/html", "UTF-8");
            Log.i(TAG," currURL onReceivedError  "+currURL);
            super.onReceivedError(view, errorCode, description, errorHtml);
        }
    }
    boolean mIsErrorPage;
    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    protected void showErrorPage() {
        FrameLayout webParentView = (FrameLayout)webView.getParent();

        initErrorPage();
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
        webParentView.addView(noNetLayout, 0, lp);
        mIsErrorPage = true;
    }
    protected void hideErrorPage() {
        FrameLayout webParentView = (FrameLayout)webView.getParent();

        mIsErrorPage = false;
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
    }


    protected void initErrorPage() {
        if (noNetLayout == null) {
            noNetLayout = View.inflate(this, R.layout.no_network_layout, null);
            Button button = (Button)noNetLayout.findViewById(R.id.no_network_reload);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    hideErrorPage();
                    webView.reload();
                }
            });
            noNetLayout.setOnClickListener(null);
        }
    }


    public static boolean isAvilible(Context context, String packageName) {

        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }


    /**
     * 启动定时器  匀速进度条
     */
    private int currentProgress = 0;
    private void startTimeTask() {
        stopTimeTask();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (currentProgress < 99) {
                            currentProgress += 1;

                            waveProgressView_0.setProgress(currentProgress);

                        } else {
                            stopTimeTask();
                        }
                    }
                });
            }
        }, 0, 50);
    }
    /**
     * 关闭定时器
     */
    private void stopTimeTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;

            isFirst = false;
        }
    }

    /**
     * 加载超时响应 计时器
     */
    private boolean mIsDestroy = false;
    private class TimeoutTask extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            mRBus.postMain(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mIsDestroy) {
                        return;
                    }

                    if (GBLogUtils.DEBUG) {
                        GBLogUtils.DEBUG_DISPLAY(this, "@@@@@@@ FUCK timeout!!");
                    }

                    //webvv.stopLoading();
                    //webvv.loadUrl("about:blank");
                    mProgress.setVisibility(View.GONE);
                    waveProgressView_0.setVisibility(View.GONE);

                    webTitle.setVisibility(View.VISIBLE);
                    final String msg = GBResourceUtils.getString(ShopCenterActivity.this, "launcher_support_webshell_timeout");
                    webTitle.setText(GBResourceUtils.getString(ShopCenterActivity.this, "launcher_support_webshell_timeout"));
                    Toast.makeText(ShopCenterActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private boolean isFirst = true;
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
    public void onDestroy() {
        cancelTimer();
        unregisterReceiver(receiver);
        if (webView != null) {
            fl.removeView(webView);
            webView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            webView.clearHistory();
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        mIsDestroy = true;
        super.onDestroy();
    }


    class NetChangeBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //无网络 ,打开Dialog
            if (action.equals(Constants.NO_NET_STATU)) {
                webView.setVisibility(View.INVISIBLE);
                noNetLayout.setVisibility(View.VISIBLE);
            }
            //网络（手机、wifi）打开 ,关闭Dialog
            if (action.equals(Constants.NET_STATU_MOBILE) || action.equals(Constants.NET_STATU_WIFI)) {
                noNetLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                Log.i(TAG,"curr broadcast "+currURL);
                webView.loadUrl(currURL);
            }
        }
    }


    @Override
    public void onLongClickCallBack(final String imgUrl) {
        currURL=imgUrl;
        new WebViewQRUtils(this).startOnLongCallBack(imgUrl);
    }

}
