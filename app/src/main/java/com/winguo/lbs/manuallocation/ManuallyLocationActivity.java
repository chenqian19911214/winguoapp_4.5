package com.winguo.lbs.manuallocation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseTitleActivity;
import com.winguo.lbs.bean.LbsLocationBean;
import com.winguo.search.nearby.NearbySearchActivity;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class ManuallyLocationActivity extends BaseTitleActivity {

    private CityListAdapter mCityAdapter;
    private ListView mListview_all_city;
    private LocationService locationService;
    private String currentCity = "";
    private LinearLayout mEdit_search_poi_ll;
    private ListView mEdit_search_poi_list;
    private TextView mLbs_search_et;
    private SuggestAddressAdapter suggestAdapter;
    /**
     * 在线建议查询结果集
     */
    private List<SuggestionResult.SuggestionInfo> keyWordPoiData;
    private TextView lbs_city_tv;
    private InputMethodManager mImm;
    /**
     * 用于判断EditText是否获取了焦点
     */
    private boolean isFocus;

    private LbsLocationBean mLbsLocationBean;
    private MyBDLocationListener mListener = new MyBDLocationListener();
    /**
     * 是否重新定位
     */
    private boolean isRefresh = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_location);
        initData();
        initView();
    }

    private void initView() {
        setBackBtn();
        //城市列表
        mListview_all_city = (ListView) findViewById(R.id.listview_all_city);
        mListview_all_city.setAdapter(mCityAdapter);

        //字母
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        SideLetterBar letterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        letterBar.setOverlay(overlay);
        letterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListview_all_city.setSelection(position);
            }
        });

        //搜索
        mLbs_search_et = (TextView) findViewById(R.id.lbs_search_et);
        lbs_city_tv = (TextView) findViewById(R.id.lbs_city_tv);
        mEdit_search_poi_ll = (LinearLayout) findViewById(R.id.edit_search_poi_ll);
        mEdit_search_poi_list = (ListView) findViewById(R.id.edit_search_poi_list);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mLbs_search_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEdit_search_poi_ll.setVisibility(View.VISIBLE);
                } else {
                    mEdit_search_poi_ll.setVisibility(View.GONE);
                }
                isFocus = hasFocus;
            }
        });
        //百度查询的列表监听
        mEdit_search_poi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuggestionResult.SuggestionInfo suggestionInfo = keyWordPoiData.get(position);
                mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mEdit_search_poi_ll.setVisibility(View.GONE);
                mLbs_search_et.clearFocus();
                mLbs_search_et.setText("");
                //保存定位信息
                mLbsLocationBean.setCity(suggestionInfo.city);
                mLbsLocationBean.setDescribe(suggestionInfo.key);
                LatLng pt = suggestionInfo.pt;
                if (pt != null) {
                    mLbsLocationBean.setLongitude(pt.longitude);
                    mLbsLocationBean.setLatitude(pt.latitude);
                }
                keyWordPoiData.clear();
                Intent intent = new Intent();
                intent.putExtra(NearbySearchActivity.LOCATION, mLbsLocationBean);
                setResult(0, intent);
                finish();
            }
        });

        //初始化定位textView
        if (mLbsLocationBean.getCity() != null) {
            currentCity = mLbsLocationBean.getCity();
            lbs_city_tv.setText(mLbsLocationBean.getCity());
            updateLocateStateByMainThread(LocateState.SUCCESS, mLbsLocationBean.getDescribe());
        } else {
            lbs_city_tv.setText("未知");
            updateLocateStateByMainThread(LocateState.FAILED, null);
        }
        mLbs_search_et.addTextChangedListener(watcher);
    }

    /**
     * 监听onKeyDown事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFocus) {
                mEdit_search_poi_ll.setVisibility(View.GONE);
                mLbs_search_et.setText("");
                mLbs_search_et.clearFocus();
                keyWordPoiData.clear();
                return true;
            }
            Intent intent = new Intent();
            if (isRefresh) {
                intent.putExtra(NearbySearchActivity.LOCATION, mLbsLocationBean);
            } else {
                intent.putExtra(NearbySearchActivity.LOCATION, (Parcelable[]) null);
            }
            setResult(0, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {
        mLbsLocationBean = (LbsLocationBean) getIntent().getExtras().get(NearbySearchActivity.LOCATION);
        keyWordPoiData = new ArrayList<>();
        DBManager dbManager = new DBManager(this);
        dbManager.copyDBFile();
        List<City> allCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, allCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                lbs_city_tv.setText(name);
                currentCity = name;
            }

            @Override
            public void onLocateClick() {
                isRefresh = true;
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                locationService.start();
                Toast.makeText(ManuallyLocationActivity.this, "重新定位...", Toast.LENGTH_SHORT).show();
            }
        });
        //初始化百度定位
        locationService = ((StartApp) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.registerListener(mListener);
    }

    /**
     * 更新当前位置
     *
     * @param state
     * @param city
     */
    private void updateLocateStateByMainThread(final int state, final String city) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCityAdapter.updateLocateState(state, city);
            }
        });
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if ("".equals(mLbs_search_et.getText().toString())) {
                return;
            }
            if ("".equals(currentCity)) {
                ToastUtil.showToast(ManuallyLocationActivity.this, "当前城市未知,请选择后重试");
                return;
            }
            suggestAdapter = null;
            /**
             * 在线建议查询对象实例化+设置监听
             * @在线建议查询： 根据城市和关键字搜索出相应的位置信息(模糊查询)
             * */
            SuggestionSearch keyWordsPoiSearch = SuggestionSearch.newInstance();
            keyWordsPoiSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
                @Override
                public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                    keyWordPoiData.clear();
                    if (suggestionResult.getAllSuggestions() == null) {
                        Toast.makeText(getApplicationContext(), "暂无数据信息", Toast.LENGTH_LONG).show();
                    } else {
                        keyWordPoiData = suggestionResult.getAllSuggestions();
                        //设置Adapter结束
                        suggestAdapter = new SuggestAddressAdapter(getApplicationContext(), keyWordPoiData);
                        mEdit_search_poi_list.setAdapter(suggestAdapter);
                    }
                }
            });
            keyWordsPoiSearch.requestSuggestion((new SuggestionSearchOption()).keyword(mLbs_search_et.getText().toString()).city(currentCity));
        }
    };

    /**
     * 定位结果回调
     */
    private class MyBDLocationListener extends IBaseLocationListener {

        /**
         * 定位成功
         *
         * @param lbsLocationBean
         */
        @Override
        protected void onResponse(LbsLocationBean lbsLocationBean) {
            //保存数据
            mLbsLocationBean.setLatitude(lbsLocationBean.getLatitude());
            mLbsLocationBean.setLongitude(lbsLocationBean.getLongitude());
            mLbsLocationBean.setCity(lbsLocationBean.getCity());
            mLbsLocationBean.setDescribe(lbsLocationBean.getDescribe());

            currentCity = lbsLocationBean.getCity();
            lbs_city_tv.setText(currentCity);
            updateLocateStateByMainThread(LocateState.SUCCESS, lbsLocationBean.getDescribe());
        }

        /**
         * 定位失败
         */
        @Override
        protected void onError() {
            updateLocateStateByMainThread(LocateState.FAILED, null);
        }
    }
}
