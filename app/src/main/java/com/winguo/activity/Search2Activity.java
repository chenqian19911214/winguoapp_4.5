package com.winguo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localsearch.LocalAppFinder;
import com.example.localsearch.LocalSearch;
import com.example.localsearch.LocalSmsFinder;
import com.example.localsearch.WGContactResult;
import com.example.localsearch.WGResultText;
import com.guobi.common.wordpy.Wordpy;
import com.guobi.gblocation.GBDLocation;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;
import com.guobi.gfc.gbmiscutils.thread.RunnableBus;
import com.winguo.R;
import com.winguo.adapter.CommonAdapter;
import com.winguo.adapter.SearchAdapterFactory;
import com.winguo.base.BaseActivity;
import com.winguo.bean.SearchHistory;
import com.winguo.bean.SearchKeyWord;
import com.winguo.flowlayout.FlowLayout;
import com.winguo.flowlayout.TagAdapter;
import com.winguo.flowlayout.TagFlowLayout;
import com.winguo.search.ISearchCallBack;
import com.winguo.search.history.SQLOperateImpl;
import com.winguo.search.modle.SearchChars;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.FileUtil;
import com.winguo.utils.Intents;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.view.IListView;
import com.winguo.view.SearchKeyWordLayout;
import com.winguo.view.SearchTypePopWindow;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/5/8.
 */

public class Search2Activity extends BaseActivity {

    private ImageView back;
    private LinearLayout searchContainer;

    @Override
    protected int getLayout() {
        return R.layout.activity_search2;
    }

    @Override
    protected void initData() {
        CommonUtil.stateSetting(this, R.color.white_top_color);
        Intent it = getIntent();
        type = it.getStringExtra("type");
        keyWord = it.getStringExtra("key");
        //历史记录查询
        sqlOperate = new SQLOperateImpl(this);
        searchHistories = sqlOperate.queryDesc();

        //关联 商品 数据
        searchChars = new SearchChars();

    }

    @Override
    protected void initViews() {

        long l1 = System.currentTimeMillis();
        initView();
        long l3 = System.currentTimeMillis();
        Log.i("Search  time1  ", "" + (l3 - l1));
        Log.i("tag++++++", "" + type);
        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission,Constants.REQUEST_CODE_PERMISSIONS_SEARCH);
        }
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {

                long l1 = System.currentTimeMillis();

                if (Intents.checkPermission(Search2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && Intents.checkPermission(Search2Activity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && Intents.checkPermission(Search2Activity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    File dbFile = new File(Constants.dbSDPath);
                    if (!FileUtil.isSdcardAvailable()) {
                        db = SQLiteDatabase.openDatabase(getFilesDir() + "/telocation.db", null, SQLiteDatabase.OPEN_READONLY);
                    } else if (dbFile.exists()) {
                        db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/telocation.db", null, SQLiteDatabase.OPEN_READONLY);
                    }
                    localSearch = new LocalSearch(Search2Activity.this, type);
                    long l2 = System.currentTimeMillis();
                    Log.i("Search  time2  ", "" + (l2 - l1));
                }
            }
        });

        long l2 = System.currentTimeMillis();
        Log.i("Search  time3  ", "" + (l2 - l3));
        //无数据显示
        noResult = (ImageView) findViewById(R.id.search_local_no_result);
        back = (ImageView) findViewById(R.id.top_back);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        search.setOnKeyListener(onKeyListener);
        searchType.setOnClickListener(this);
        clearHistory.setOnClickListener(this);
        search.addTextChangedListener(watcher);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.winguo_web:
                startSearch(keyWord);
                break;

            case R.id.search_history_clear:
                //点击 全部清除 历史记录
                clearRefreshHistoryLabel();
                break;
            //无网络重新加载
            case R.id.no_network_reload:
                noNetLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(getWebUrl(keyWord));
                break;
        }
    }

    /**
     * 初始化 服务搜索
     */
    private void initServiceView() {
        if (webContainer == null)
            webContainer = (FrameLayout) findViewById(R.id.search_ser_fl);
        if (mProgress == null)
            mProgress = (ProgressBar) findViewById(R.id.search_ser_web_progress);
        if (webView == null)
            webView = new WebView(this);
        //无网络布局
        if (noNetLayout == null)
            noNetLayout = mInflater.inflate(R.layout.no_network_layout, null);
        noNetLayout.setVisibility(View.GONE);
        //无网络重加载获取
        noNetReload = (Button) noNetLayout.findViewById(R.id.no_network_reload);
        noNetReload.setOnClickListener(this);

        setWebView();
        serviceView.setVisibility(View.GONE);
    }

    /**
     * 初始化本地搜索
     */
    private void initLocalView() {
        //生成适配器 并处理点击事件
        if (appAdapter == null)
            appAdapter = SearchAdapterFactory.getDataAdapter(this,this, apps, SearchKeyWord.TYPE_APP);
        //生成适配器 并处理点击事件
        if (conntactAdapter == null)
            conntactAdapter = SearchAdapterFactory.getDataAdapter(this,this, contacts, SearchKeyWord.TYPE_CONTACT);
        //生成适配器 并处理点击事件
        if (smsAdapter == null)
            smsAdapter = SearchAdapterFactory.getDataAdapter(this,this, sms, SearchKeyWord.TYPE_MESSAGE);

        //本地搜索
        if (contactLayout == null)
            contactLayout = new SearchKeyWordLayout(this, new SearchKeyWord(R.drawable.winguo_search_contact, "联系人"), conntactAdapter);
        if (appLayout == null)
            appLayout = new SearchKeyWordLayout(this, new SearchKeyWord(R.drawable.winguo_search_app, "应用"), appAdapter);
        if (smsLayout == null)
            smsLayout = new SearchKeyWordLayout(this, new SearchKeyWord(R.drawable.winguo_search_message, "信息"), smsAdapter);

        searchContainer.addView(contactLayout);
        searchContainer.addView(appLayout);
        searchContainer.addView(smsLayout);
        contactLayout.setVisibility(View.GONE);
        appLayout.setVisibility(View.GONE);
        smsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Constants.REQUEST_CODE_PERMISSIONS_SEARCH == requestCode) {
            int length = grantResults.length;
            StringBuffer name = new StringBuffer();
            for (int i = 0; i < length; i++) {

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //ToastUtil.showToast(getApplicationContext(),permissions[i]+"open");

                } else {

                    switch (permissions[i]) {

                        case Manifest.permission.READ_SMS:
                            name.append("短信、");
                            break;
                        case Manifest.permission.READ_CONTACTS:
                            name.append("通讯录、");
                            break;
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            name.append("存储、");
                            break;
                    }
                }
            }
            int nameLen = name.length();
            if (nameLen > 2) {
                name.replace(nameLen - 1, nameLen, "权限需要打开");
                Intents.noPermissionStatus(this, name.toString());
            }

        }

    }

    private final static String TAG = Search2Activity.class.getSimpleName();
    private int flagType = SearchHistory.SHOP_TYPE;//标记当前类型（商品 本地 服务） 默认商品
    private EditText search;
    private String keyWord;
    private String currkeyWord;
    private String type;
    private double longitude, latitude = 0.0f;
    private CommonAdapter conntactAdapter, smsAdapter, appAdapter;
    List<WGContactResult> contacts = new ArrayList<>();
    List<LocalAppFinder.AppResult> apps = new ArrayList<>();
    List<LocalSmsFinder.WGSmsResult> sms = new ArrayList<>();

    private LocalSearch localSearch;
    private SQLiteDatabase db;
    private ImageView noResult;
    private SearchKeyWordLayout contactLayout;
    private SearchKeyWordLayout appLayout;
    private SearchKeyWordLayout smsLayout;

    private Button searchType;


    //搜索历史
    private TagFlowLayout flowContainer;
    private ImageView clearHistory;
    private View historyView;
    private TagAdapter<SearchHistory> hisLabel;
    private SQLOperateImpl sqlOperate;
    //服务相关  关键字后台数据
    private View serviceView;
    private View noNetLayout;
    private Button noNetReload;
    private FrameLayout webContainer;
    private ProgressBar mProgress;
    private WebView webView;
    //商品 搜索关键字关联列表
    private IListView keyList;
    private SearchChars searchChars;
    private ArrayAdapter<String> adapter;
    private List<SearchHistory> searchHistories;
    private List<String> searchCharsItems = new ArrayList<>();

    /**
     * 首次使用提示
     */
    private void setTipToast() {
        // 自定义土司显示位置
        // 创建土司
        Toast toast = new Toast(this);
        // 找到toast布局的位置
        View layout = View.inflate(this, R.layout.search_tip_toast, null);
        // 设置toast文本，把设置好的布局传进来
        toast.setView(layout);
        // 设置土司显示在屏幕的位置
        int screenWidth = ScreenUtil.getScreenWidth(this);
        switch (screenWidth) {
            case 480:
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 32, 38);
                break;
            case 720:
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 32, 60);
                break;
            case 1080:
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 56, 60);
                break;
        }

        toast.setDuration(Toast.LENGTH_SHORT);
        // 显示土司
        toast.show();
        SPUtils.put(this, "isShowTip", "ok");
    }

    private void initView() {
        long local0 = System.currentTimeMillis();
        //历史记录
        historyView = findViewById(R.id.search_history);
        flowContainer = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        clearHistory = (ImageView) findViewById(R.id.search_history_clear);

        //服务
        serviceView = findViewById(R.id.search_ser);

        //商品关键字 列表
        keyList = (IListView) findViewById(R.id.search_shop_chars_lv);
        keyList.setVisibility(View.GONE);
        //头部右侧 搜索点击按钮
        TextView title = (TextView) findViewById(R.id.winguo_web);
        title.setOnClickListener(this);

        //获取容器 searchContainer 本地搜索结果
        searchContainer = (LinearLayout) findViewById(R.id.search_edit_resLL);

        search = (EditText) findViewById(R.id.main_search_edit);

        longitude = GBDLocation.getInstance().getLongitude();
        latitude = GBDLocation.getInstance().getLatitude();
        GBLogUtils.DEBUG_DISPLAY("searchedit", longitude + ":::::" + latitude);
        long local = System.currentTimeMillis();
        Log.i("Search time5", " local0 : " + (local - local0));

        //搜索类型 切换
        searchType = (Button) findViewById(R.id.search_type);


        fillData();
        setEditView();
        long local3 = System.currentTimeMillis();
        Log.i("Search time5", " local2 : " + (local3 - local));

    }

    /**
     * 服务 webview初始化设置
     */
    private void setWebView() {
        webView.setScrollbarFadingEnabled(true);
        webContainer.addView(webView);
        webContainer.addView(noNetLayout);
        //打开网页上的链接 继续使用webview打开网页
        webView.setWebViewClient(new IWebViewClient());
        webView.setWebChromeClient(new IWebChromeClient());
        webView.setClickable(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setPersistentDrawingCache(0);

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
        if (NetWorkUtil.isNetworkAvailable(this)) {
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
        settings.setGeolocationDatabasePath("/data/data/" + this.getPackageName() + "/databases/");
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        //settings.setSupportZoom(true); // 支持缩放
        ScreenUtil.setWebView((Activity) this, webView);//页面适配屏幕


    }

    private void fillData() {
        //历史记录 设置 展示
        searchHisSet();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchCharsItems);
        keyList.setAdapter(adapter);
        keyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startSearch(searchCharsItems.get(position));
            }
        });
    }

    /**
     * 历史记录 设置 展示
     */
    private void searchHisSet() {

        hisLabel = new TagAdapter<SearchHistory>(searchHistories) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHistory searchHistory) {

                TextView tv = null;
                switch (searchHistory.type) {
                    case SearchHistory.SHOP_TYPE:
                        tv = (TextView) mInflater.inflate(R.layout.search_his_shop_item, flowContainer, false);
                        tv.setTextColor(Color.BLUE);
                        break;
                    case SearchHistory.LOCAL_TYPE:
                        tv = (TextView) mInflater.inflate(R.layout.search_his_local_item, flowContainer, false);
                        tv.setTextColor(Color.RED);
                        break;
                    case SearchHistory.SERVER_TYPE:
                        tv = (TextView) mInflater.inflate(R.layout.search_his_service_item, flowContainer, false);
                        tv.setTextColor(Color.GREEN);
                        break;
                }
                tv.setText(searchHistory.name);
                return tv;
            }
        };
        flowContainer.setAdapter(hisLabel);
        flowContainer.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //历史记录可点击
                SearchHistory searchHistory = searchHistories.get(position);
                switch (searchHistory.type) {
                    case SearchHistory.SHOP_TYPE:
                        searchHisChoice(SearchHistory.SHOP_TYPE);
                        break;
                    case SearchHistory.LOCAL_TYPE:
                        search.setText(searchHistory.name);
                        keyList.setVisibility(View.GONE);
                        searchHisChoice(SearchHistory.LOCAL_TYPE);
                        break;
                    case SearchHistory.SERVER_TYPE:
                        search.setText(searchHistory.name);
                        keyList.setVisibility(View.GONE);
                        searchHisChoice(SearchHistory.SERVER_TYPE);
                        break;
                }
                startSearch(searchHistory.name);
                return true;
            }

        });
    }

    /**
     * 设置EditView
     */
    private void setEditView() {
        search.setFocusable(true);
        search.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);//InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL|
        search.setFocusableInTouchMode(true);
        search.requestFocus();
    }


    private int count = 0;
    /**
     * 搜索 键盘监听
     */
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (input != null) {
                    input.hideSoftInputFromWindow(search.getWindowToken(), 0);
                }
                count++; //因为在监听中，有执行两次
                if (count == 2) {  //第二次进入 在执行搜索
                    startSearch(keyWord);
                    count = 0; //计数重置
                }

                return true;
            }
            return false;
        }
    };

    /**
     * 开始搜索
     *
     * @param key
     */
    private void startSearch(String key) {

        switch (flagType) {
            case SearchHistory.SHOP_TYPE:
                // 跳转
                Intent searchIntent = new Intent(this, ProductListActivity.class);
                searchIntent.putExtra("text", key);
                startActivity(searchIntent);

                break;

        }
        if (!TextUtils.isEmpty(key))
            refreshHistoryLabel(key);//添加历史记录

    }




    /**
     * 开始商品 关键词
     */

    private synchronized void searchShopKeyWord(String keyWord) {
        keyList.setVisibility(View.VISIBLE);
        searchChars.getData(keyWord, new ISearchCallBack() {
            @Override
            public void onBackwordsList(List<String> items) {
                if (items != null && searchCharsItems != null) {
                    if (searchCharsItems.isEmpty()) {
                        searchCharsItems.addAll(items);
                        adapter.notifyDataSetChanged();
                    } else {
                        searchCharsItems.clear();
                        searchCharsItems.addAll(items);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }


    /**
     * 输入文本改变 监听
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            keyWord = search.getText().toString().trim();

            int len = keyWord.length();
            if (len == 0) {
                showHistoy();
                switch (flagType) {
                    case SearchHistory.SHOP_TYPE:
                        keyList.setVisibility(View.GONE);
                        break;
                }

            } else {
                hideHstory();
                switch (flagType) {
                    case SearchHistory.SHOP_TYPE:
                        searchShopKeyWord(keyWord);
                        break;
                }
            }

        }
    };

    /**
     * 清空当前集合数据 和 本地数据库
     */
    private void clearRefreshHistoryLabel() {
        searchHistories.clear();
        sqlOperate.delete();
        hisLabel.notifyDataChanged();
    }

    /**
     * 添加 搜索历史
     */
    private void refreshHistoryLabel(String key) {// TODO: 2017/6/20
        SearchHistory searchHis = null;
        switch (flagType) {
            case SearchHistory.SHOP_TYPE:
                searchHis = new SearchHistory(key, SearchHistory.SHOP_TYPE);
                break;
            case SearchHistory.LOCAL_TYPE:
                searchHis = new SearchHistory(key, SearchHistory.LOCAL_TYPE);
                break;
            case SearchHistory.SERVER_TYPE:
                searchHis = new SearchHistory(key, SearchHistory.SERVER_TYPE);
                break;
        }

        if (!searchHistories.contains(searchHis)) {
            searchHistories.add(0, searchHis);
            int size = searchHistories.size();
            if (size == 11) {
                searchHistories.remove(10);
            }
        }
        sqlOperate.add(searchHis);
        hisLabel.notifyDataChanged();
    }

    private void hideHstory() {
        historyView.setVisibility(View.GONE);
    }

    private void showHistoy() {
        historyView.setVisibility(View.VISIBLE);
    }


    /**
     *
     */
    private void searchHisChoice(int type) {
        switch (type) {
            case 1:
                searchType.setText(this.getString(R.string.search_type_pop_shop));
                flagType = SearchHistory.SHOP_TYPE;
                serviceView.setVisibility(View.GONE);
                watcher.afterTextChanged(search.getText());
                break;

        }
    }


    /**
     * 组装 拼接关键字搜索url
     *
     * @param key
     * @return
     */
    public String getWebUrl(String key) {

        if (TextUtils.isEmpty(key)) {
            return "http://www.winguo.com";
        }
        StringBuffer url = new StringBuffer("http://k.winguo.com/apikw?a=Search");
        if (key != null && key.length() > 0) {
            url.append("&kw=").append(URLEncoder.encode(key));
        }

        //在url上添加统计信息
        String location = latitude + "," + longitude;
        String locale = Locale.getDefault().toString();
        url.append("&locale=").append(locale);
        url.append("&location=").append(location);

        GBLogUtils.DEBUG_DISPLAY("searchedit url", "+++++++++++++++++++++++++++++" + url.toString());
        return url.toString();
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
     * 更新进度条 获取网页标题
     */
    class IWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
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
            super.onReceivedTitle(view, title);
        }


    }

    /**
     * WebViewClient
     */
    class IWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // 判断重定向url是不是一个网址 还是一个启动服务
            if (Patterns.WEB_URL.matcher(url).matches()) {
                //符合标准
                view.loadUrl(url);
                return true;
            } else {
                //不符合标准
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    /*"mqqwpa://im/chat?chat_type=wpa&uin=2567820876";
                       baidumap://map/place/search?*/
                    String uri = null;
                    if (url.contains("mqqwpa")) {
                        uri = "QQ";
                    } else if (url.contains("baidumap")) {
                        uri = "百度地图";
                    }

                    ToastUtil.showToast(Search2Activity.this, "找不到" + uri);
                    e.printStackTrace();
                }
                return true;
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // objectAnimator0.start();
            //开始加载页面时调用  -->设定loading页面 告诉用户等待网络响应
            startTimer();
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            cancelTimer();
            //waveProgressView_0.setVisibility(View.View.GONE);
            //objectAnimator0.cancel();
            setmProgressBarState(View.GONE);
            view.getSettings().setBlockNetworkImage(false);
            //页面加载完成后调用 --> 处理关闭loading条 切换程序动作  加载 完成 可以改变控件 图片资源
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //  objectAnimator0.cancel();
            //报告错误信息  可以处理无网络事件
            //view.loadUrl("html无网络事件");
            noNetLayout.setVisibility(View.VISIBLE);
            String errorHtml = "<html><head><title>please check net</title></head><body></body></html>";
            webView.loadData(errorHtml, "text/html", "UTF-8");
            super.onReceivedError(view, errorCode, description, errorHtml);
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

                    if (GBLogUtils.DEBUG) {
                        GBLogUtils.DEBUG_DISPLAY(this, "@@@@@@@ FUCK timeout!!");
                    }

                    //webvv.stopLoading();
                    //webvv.loadUrl("about:blank");
                    mProgress.setVisibility(View.GONE);

                    final String msg = GBResourceUtils.getString(Search2Activity.this, "launcher_support_webshell_timeout");
                    Toast.makeText(Search2Activity.this, msg, Toast.LENGTH_SHORT).show();
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
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
        if (webView != null) {
            webContainer.removeView(webView);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        mIsDestroy = true;
        //当点击回退键时，Activity onDestroy后才调用onDetachedFromWindow，这时我们就在这个方法做一些收尾工作，如：取消广播注册、停止动画等等
        if (db != null) {
            db.close();
            db = null;
        }
    }


}
