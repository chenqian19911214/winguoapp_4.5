package com.guobi.webshell;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;
import com.guobi.gfc.gbmiscutils.thread.RunnableBus;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WebViewQRUtils;
import com.winguo.view.CustomWebView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * 内置浏览器
 *
 * @author Administrator
 */
public final class BrowserActivity extends BaseTitleActivity
        implements OnClickListener, CustomWebView.LongClickCallBack {


    //private String _WebUrl = null;
    private CustomWebView mWebView;
    private boolean mIsSaveStated = false;


    private ImageView mLastLinkView;
    private ImageView mNextLinkView;
    private static String TAG = BrowserActivity.class.getSimpleName();

    private ProgressBar mProgressBar;
    private TextView mWebTitle;

    // 文件上传
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private FrameLayout fl;
    private NetChangeBroadReceiver receiver;
    private View noNetLayout;
    private Button noNetReload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setContentView(R.layout.launcher_support_webshell_main);
        initUI();
        final Intent intent = this.getIntent();
        final String url = intent.getStringExtra("TargetUrl");
        mWebView.loadUrl(url);
    }

    private void setProgressBarState(int visibility) {
        mProgressBar.setProgress(0);
        mProgressBar.setVisibility(visibility);
    }

    private void setProgressBarProgress(int progress) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(progress);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsSaveStated = true;
    }

    private void initUI() {
        //监听网络变化
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NO_NET_STATU);
        filter.addAction(Constants.NET_STATU_WIFI);
        filter.addAction(Constants.NET_STATU_MOBILE);
        receiver = new NetChangeBroadReceiver();
        registerReceiver(receiver,filter);

        //无网络布局
        noNetLayout = getLayoutInflater().inflate(R.layout.no_network_layout, null, false);
        noNetLayout.setVisibility(View.GONE);
        //无网络重加载获取
        noNetReload = (Button) noNetLayout.findViewById(R.id.no_network_reload);

        fl = (FrameLayout) findViewById(R.id.webshell_webview_fl);
        mWebView = new CustomWebView(this,this);
        mWebView.setScrollbarFadingEnabled(true);
        fl.addView(mWebView);
        fl.addView(noNetLayout);


        ImageView mBackView = (ImageView) findViewById(R.id.WebShell_MASTER_back);
        RelativeLayout mBackLayout = (RelativeLayout) findViewById(R.id.WebShell_MASTER_back_layout);
        mWebTitle = (TextView) findViewById(R.id.WebShell_MASTER_title);

        RelativeLayout mLastLinkLayout = (RelativeLayout) findViewById(R.id.WebShell_MASTER_last_link_layout);
        RelativeLayout mNextLayout = (RelativeLayout) findViewById(R.id.WebShell_MASTER_next_link_layout);

        mLastLinkView = (ImageView) findViewById(R.id.WebShell_MASTER_last_link);
        mNextLinkView = (ImageView) findViewById(R.id.WebShell_MASTER_next_link);
        ImageView mRefreshView = (ImageView) findViewById(R.id.WebShell_MASTER_refresh);

        mProgressBar = (ProgressBar) findViewById(R.id.WebShell_MASTER_loading_progressBar);

        noNetReload.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mBackLayout.setOnClickListener(this);
        mLastLinkLayout.setOnClickListener(this);
        mNextLayout.setOnClickListener(this);
        mRefreshView.setOnClickListener(this);

        setWebView();

        final String fileChooserTitle = GBResourceUtils.getString(this, "launcher_support_webshell_file_chooser_title");

        // 加载进度
        mWebView.setWebChromeClient(new WebChromeClient() {
            public final void onProgressChanged(WebView view, int param) {
                if (GBLogUtils.DEBUG) {
                    GBLogUtils.DEBUG_DISPLAY(this, "###### onProgressChanged:" + param);
                }

                if (param <= 10) {
                    startTimer();
                } else {
                    cancelTimer();
                }

                mWebTitle.setText(view.getTitle());

                BrowserActivity.this.setProgress(param * 100);
                setProgressBarProgress(param);


                if (param == 100) {
                    setProgressBarState(View.GONE);
                }

            }

            @Override
            public final void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                mWebTitle.setText(title);
            }

            //@Override
            public final void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(i, fileChooserTitle),
                        FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, fileChooserTitle),
                        FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(i, fileChooserTitle),
                        FILECHOOSER_RESULTCODE);

            }


        });

        mWebView.setDownloadListener(new MyWebViewDownLoadListener());// 支持下载功能

        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.setOnTouchListener(new OnTouchListener() {
            float start_Y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start_Y = event.getY();

                    if (!v.hasFocus())
                        v.requestFocus();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (!v.hasFocus())
                        v.requestFocus();
                }

                return false;
            }
        });


    }

    private void setWebView() {
        mWebView.setClickable(true);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setPersistentDrawingCache(0);
        // 点击网页上的链接继续用WebView打开网页
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setOnTouchListener(new View.OnTouchListener() {
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
        WebSettings s = mWebView.getSettings();
        s.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级
        s.setBlockNetworkImage(true);//使把图片加载放在最后来加载渲染
        s.setAppCacheEnabled(true);
        // webView数据缓存分为两种：AppCache和DOM Storage（Web Storage）。
        // 开启 DOM storage 功能
        s.setDomStorageEnabled(true);
        // 应用可以有数据库
        s.setDatabaseEnabled(true);
        // 根据网络连接情况，设置缓存模式，
        if (NetWorkUtil.isNetworkAvailable(this)) {
            s.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        } else {
            s.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 先查找缓存，没有的情况下从网络获取。
        }
        // 可以读取文件缓存(manifest生效)
        s.setAllowFileAccess(true);

        //s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应屏幕大小 缩放内容
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setUseWideViewPort(true);//设置webview推荐使用的窗口
        s.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        s.setDisplayZoomControls(false);//隐藏webview缩放按钮
        s.setJavaScriptEnabled(true); // 设置支持javascript脚本
        s.setBuiltInZoomControls(true); // 设置显示缩放按钮
        //s.setSupportZoom(true); // 支持缩放
        //s.setPluginsEnabled(true);
        //webvv.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);// 改成使用普通的缓存模式
        ScreenUtil.setWebView(this, mWebView);

        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/" + this.getPackageName() + "/databases/");

        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            s.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            s.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            s.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            s.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            s.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            s.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
    }




    @Override
    public void onLongClickCallBack(String imgUrl) {
        new WebViewQRUtils(this).startOnLongCallBack(imgUrl);
    }


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
                    mProgressBar.setVisibility(View.GONE);

                    final String msg = GBResourceUtils.getString(BrowserActivity.this, "launcher_support_webshell_timeout");
                    mWebTitle.setText(GBResourceUtils.getString(BrowserActivity.this, "launcher_support_webshell_timeout"));
                    Toast.makeText(BrowserActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    // 支持屏幕旋转
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mIsSaveStated)
            return false;

        return super.dispatchKeyEvent(event);
    }

    // 按下物理键返回的处理
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {

            if (GBLogUtils.DEBUG)
                GBLogUtils.DEBUG_DISPLAY(this,
                        "childCount = " + mWebView.getChildCount());

            // webvv.stopLoading();
            mWebView.goBack();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


    // 点击网页上的链接
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            GBLogUtils.DEBUG_DISPLAY(TAG+"shouldOverrideUrlLoading",url);

            if (url.contains("$&")) {
                url = url.replace("$","");
                GBLogUtils.DEBUG_DISPLAY(TAG+"shouldOverrideUrlLoading replace",url);
            }
            currURL = url;
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

                    ToastUtil.showToast(BrowserActivity.this,"找不到"+uri);
                    e.printStackTrace();
                }
                return true;
            }



        }


        public void onPageStarted(WebView paramWebView, String paramString,
                                   Bitmap paramBitmap) {
            super.onPageStarted(paramWebView, paramString, paramBitmap);

            mWebTitle.setText(paramWebView.getTitle());

            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(this, "###### onPageStarted");
            }

            startTimer();
        }

        public void onPageFinished(WebView paramWebView, String paramString) {
            super.onPageFinished(paramWebView, paramString);

            cancelTimer();
            paramWebView.getSettings().setBlockNetworkImage(false);//使把图片加载放在最后来加载渲染
            mWebTitle.setText(paramWebView.getTitle());
            if (mWebView!=null) {
                if (!mWebView.canGoBack()) {
                    mLastLinkView.setImageResource(R.drawable.launcher_support_webshell_last_link_no_click);
                } else {
                    mLastLinkView.setImageResource(R.drawable.launcher_support_webshell_last_link_selector);
                }

                if (!mWebView.canGoForward()) {
                    mNextLinkView.setImageResource(R.drawable.launcher_support_webshell_next_link_no_click);
                } else {
                    mNextLinkView.setImageResource(R.drawable.launcher_support_webshell_next_link_selector);
                }
            }
        }

        public void onReceivedError(WebView paramWebView, int paramInt,
                                    String paramString1, String paramString2) {
            Toast.makeText(BrowserActivity.this, paramString1, Toast.LENGTH_SHORT).show();
            noNetLayout.setVisibility(View.VISIBLE);
            // showErrorPage();
            String errorHtml = "<html><head><title>please check net</title></head><body></body></html>";
            mWebView.loadData(errorHtml, "text/html", "UTF-8");
            Log.i(TAG," currURL onReceivedError  "+currURL);
            super.onReceivedError(paramWebView, paramInt, paramString1, errorHtml);

        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
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

    // 下载
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {

            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart", "url:" + url);
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart", "userAgent:" + userAgent);
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart", "contentDisposition:" + contentDisposition);
                GBLogUtils.DEBUG_DISPLAY("onDownloadStart", "mimetype:" + mimetype);
            }

            try {

                if (mimetype.equals("application/vnd.android.package-archive")) {

                    String fileName, title;
                    int fileNameBegin, fileNameEnd;
                    fileNameBegin = contentDisposition.indexOf("\"") + 1;
                    fileNameEnd = contentDisposition.lastIndexOf("\"");
                    if (fileNameBegin != -1 &&
                            fileNameEnd != -1 &&
                            fileNameEnd > fileNameBegin) {
                        title = fileName = contentDisposition.substring(fileNameBegin, fileNameEnd);
                    } else {
                        fileNameBegin = url.lastIndexOf("/") + 1;
                        title = fileName = url.substring(fileNameBegin);
                    }


                    final Context context = BrowserActivity.this;
                    //DownloadHelper.startAppDownload(context, title, url, fileName, null);
                    //下载文件工具 上部工具底层使用HttpClient网络请求已经被谷歌废弃了

                } else {
                    GBLogUtils.DEBUG_DISPLAY("--url", url);
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 删除保存在手机上 的缓存
    private int clearCacheFolder(File dir, long numDays) {

        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {

            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onRestart() {
        super.onRestart();

    }

    protected void onStop() {
        super.onStop();

    }

    protected void onPause() {// 继承自Activity
        super.onPause();
        // titleBarRL.setVisibility(View.GONE); //
        // 控件显示比较占CPU，如果不退出WebView便去启动其它app就隐藏一下
        //MobclickAgent.onPause(this);


    }

    protected void onResume() {// 继承自Activity
        super.onResume();
        mIsSaveStated = false;
        //MobclickAgent.onResume(this);


    }

    private String currURL = "";
    class NetChangeBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //无网络 ,打开Dialog
            if (action.equals(Constants.NO_NET_STATU)) {
                mWebView.setVisibility(View.INVISIBLE);
                noNetLayout.setVisibility(View.VISIBLE);
            }
            //网络（手机、wifi）打开 ,关闭Dialog
            if (action.equals(Constants.NET_STATU_MOBILE) || action.equals(Constants.NET_STATU_WIFI)) {
                noNetLayout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                Log.i(TAG,"curr broadcast "+currURL);
                mWebView.loadUrl(currURL);
            }
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
    protected void onDestroy() {
        cancelTimer();
        unregisterReceiver(receiver);
        if (mWebView != null) {
            fl.removeView(mWebView);
            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            mWebView.clearHistory();
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        mIsDestroy = true;
        super.onDestroy();


        ////////////////////////////////////////////////////////
        // 关于清除缓存

/*		
        File file = CacheManager.getCacheFileBaseDir();
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}
		  
		clearCacheFolder(BrowserActivity.this.getCacheDir(),System.currentTimeMillis());
		webvv.clearCache(true);
		webvv.clearHistory();
		webvv.clearFormData();
		BrowserActivity.this.deleteDatabase("webview.db");
		BrowserActivity.this.deleteDatabase("webviewCache.db");
*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.no_network_reload:
                //无网络 点击重新加载
                noNetLayout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(currURL);
                break;
            case R.id.WebShell_MASTER_back:
            case R.id.WebShell_MASTER_back_layout:
                finish();
                break;

            case R.id.WebShell_MASTER_refresh:
                mWebView.reload();
                break;
            case R.id.WebShell_MASTER_last_link_layout:
                if (mWebView.canGoBack()) {
                    mWebView.stopLoading();
                    mWebView.goBack();
                }
                break;
            case R.id.WebShell_MASTER_next_link_layout:
                if (mWebView.canGoForward()) {
                    mWebView.stopLoading();
                    mWebView.goForward();
                }
                break;
        }

    }

    protected static final Pattern ACCEPTED_URI_SCHEMA = Pattern.compile("(?i)"
            + // switch on case insensitive matching
            "("
            + // begin group for schema
            "(?:http|https|file):\\/\\/" + "|(?:inline|data|about|javascript):"
            + ")" + "(.*)");

}
