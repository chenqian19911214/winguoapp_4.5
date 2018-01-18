package com.winguo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localsearch.LocalAppFinder;
import com.example.localsearch.LocalSearch;
import com.example.localsearch.WGContactResult;
import com.guobi.gblocation.GBDLocation;
import com.guobi.winguoapp.BrowserUtils;
import com.winguo.R;
import com.winguo.adapter.CommonAdapter;
import com.winguo.adapter.SearchAdapterFactory;
import com.winguo.base.BaseActivity;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.SearchKeyWord;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;
import com.winguo.utils.ThreadUtils;
import com.winguo.view.IListView;
import com.winguo.view.SearchKeyWordLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索主页面
 * Created by admin on 2016/12/5.
 */

public class SearchActivity extends BaseTitleActivity implements View.OnClickListener {

    public static List<LocalAppFinder.AppResult> apps = new ArrayList<>();
    public static List<WGContactResult> contacts = new ArrayList<>();
    private CommonAdapter conntactAdapter, appAdapter;
    private String keyWord,currkeyWord;
    private String type;
    private IListView iListView;
    private EditText searchEditView;
    private TextView searchTextView;
    private SQLiteDatabase db;
    private LocalSearch localSearch;
    private Context context;
    private double longitude, latitude = 0.0f;
    private SearchKeyWordLayout contactLayout;
    private SearchKeyWordLayout appLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setContentView(R.layout.search_activity);
        initData();
        initViews();
        setListener();
    }


    private void initViews() {

        LinearLayout searchContainer = (LinearLayout) findViewById(R.id.search_edit_resLL);
        iListView = (IListView) findViewById(R.id.search_shop_chars_lv);
        iListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        searchEditView = (EditText) findViewById(R.id.nearby_search_et);
        searchTextView = (TextView) findViewById(R.id.nearby_search_tv);
        contactLayout = new SearchKeyWordLayout(context, new SearchKeyWord(R.drawable.winguo_search_contact, "联系人"), conntactAdapter);
        appLayout = new SearchKeyWordLayout(context, new SearchKeyWord(R.drawable.winguo_search_app, "应用"), appAdapter);
        searchContainer.addView(contactLayout);
        searchContainer.addView(appLayout);
        contactLayout.setVisibility(View.GONE);
        appLayout.setVisibility(View.GONE);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

    }
    private void initData() {
     //   CommonUtil.stateSetting(this, R.color.white_top_color);
        context = getApplicationContext();
        longitude = GBDLocation.getInstance().getLongitude();
        latitude = GBDLocation.getInstance().getLatitude();
        appAdapter = SearchAdapterFactory.getDataAdapter(context,this, apps, SearchKeyWord.TYPE_APP);

        conntactAdapter = SearchAdapterFactory.getDataAdapter(context, this,contacts, SearchKeyWord.TYPE_CONTACT);

    }


    private void setListener() {
        searchEditView.addTextChangedListener(watcher);
        searchTextView.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (1 == requestCode) {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                searchLocalKeyWord(keyWord);
            }else{
                //申请权限失败
                Toast.makeText(context, "请打开联系人权限", Toast.LENGTH_SHORT).show();
            }
        }

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
            // curr = 0;
        }

        @Override
        public void afterTextChanged(Editable editable) {

            keyWord = searchEditView.getText().toString().trim();
            CommonUtil.printI("chenqina", "afterTextChanged:" + keyWord);
            int len = keyWord.length();
            if (len != 0) {

                if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SearchActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }else {
                    searchLocalKeyWord(keyWord);
                }
            }else{
                contactLayout.setVisibility(View.GONE);
                appLayout.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 开始搜索关键字
     */

    private synchronized void searchLocalKeyWord(String keyWord) {

        CommonUtil.printI("走了一次", keyWord + "+++++++++++++++searchKeyWord");
        if (localSearch == null) {

            localSearch = new LocalSearch(context, type);
        } else {
            localSearch.clearList();
        }
        localSearch.setKey(keyWord);
        currkeyWord = keyWord;
        contactResult();
        appResult();
    }
    /**
     * 联系人 搜索结果
     *
     * @param
     */
    private synchronized void contactResult() {

        ThreadUtils.runOnBackCacheThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.printI("---local contact", "contactResult===========");

                final List<WGContactResult> temp = localSearch.getContactResult();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp != null && currkeyWord.equals(keyWord)) {
                           // noResult.setVisibility(GONE);
                            contacts.clear();
                            contacts.addAll(temp);
                            conntactAdapter.notifyDataSetChanged();
                            contactLayout.setVisibility(View.VISIBLE);
                            CommonUtil.printI("---local contact", "if===========" + contacts.size());

                        } else {
                            //没有找到相关联系人 清除上次结果
                            CommonUtil.printI("---local contact", "else===========" + contacts.size());
                         //   showNoResult();
                            contacts.clear();
                            conntactAdapter.notifyDataSetChanged();
                            contactLayout.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });


    }
    /**
     * app 搜索结果
     *
     * @param
     */

    private synchronized void appResult() {
        ThreadUtils.runOnBackCacheThread(new Runnable() {

            @Override
            public void run() {
                CommonUtil.printI("---local app", "appResult===========");
                final List<LocalAppFinder.AppResult> tempApp = localSearch.getAppResult();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tempApp != null && currkeyWord.equals(keyWord)) {
                          //  noResult.setVisibility(GONE);
                            apps.clear();
                            apps.addAll(tempApp);
                            appAdapter.notifyDataSetChanged();
                            appLayout.setVisibility(View.VISIBLE);
                            CommonUtil.printI("---local app", "if===========" + apps.size());
                        } else {
                            //没有找到相关app 清除上次结果
                            CommonUtil.printI("---local app", "else===========" + apps.size());
                           // showNoResult();
                            apps.clear();
                            appAdapter.notifyDataSetChanged();
                            appLayout.setVisibility(View.GONE);

                        }
                    }
                });
            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.nearby_search_tv:
                // BrowserUtils.startBrowser(context,getWebUrl(keyWord));
                BrowserUtils.startWinguoWebSearch(context,keyWord,latitude,longitude,"");
                break;
            default:
        }
    }
}
