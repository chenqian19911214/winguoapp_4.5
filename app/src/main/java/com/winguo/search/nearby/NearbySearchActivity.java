package com.winguo.search.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.SearchHistory;
import com.winguo.flowlayout.FlowLayout;
import com.winguo.flowlayout.TagAdapter;
import com.winguo.flowlayout.TagFlowLayout;
import com.winguo.lbs.bean.LbsLocationBean;
import com.winguo.lbs.searchresult.SearchResultActivity;
import com.winguo.search.history.SQLOperateImpl;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.util.List;


/**
 * @author hcpai
 * @desc ${TODD}
 */

public class NearbySearchActivity extends BaseTitleActivity implements View.OnClickListener, NearbySearchChangePopupWindow.INearbySearchPopupWindow {
    private SQLOperateImpl sqlOperate;
    private List<SearchHistory> searchHistories;
    private TagAdapter<SearchHistory> hisLabelAdapter;
    private LayoutInflater inflater;
    private TagFlowLayout flowlayout;
    private EditText mNearby_search_et;
    private NearbySearchChangePopupWindow popupWindow;
    /**
     * 默认为实体店商品搜索:0表示店铺搜索,1表示商品搜索
     */
    private int flagType = 1;
    private TextView mNearby_change_tv;
    private LinearLayout nearby_search_history_ll;
    private ListView nearby_search_history_lv;
    private LinearLayout nearby_search_seek_ll;
    private boolean isFocus;
    private ArrayAdapter<String> seekAdapter;
    private LbsLocationBean mLbsLocationBean = new LbsLocationBean();

    public static final String SEARCHRESULT = "searchResult";
    public static final String LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setContentView(R.layout.activity_nearby_search);
        inflater = LayoutInflater.from(this);
        initDatas();
        initView();
        searchHisSet();
        setListener();
    }

    private void setListener() {
        mNearby_search_et.addTextChangedListener(watcher);
        mNearby_search_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nearby_search_seek_ll.setVisibility(View.VISIBLE);
                    nearby_search_history_ll.setVisibility(View.GONE);
                } else {
                    nearby_search_history_ll.setVisibility(View.VISIBLE);
                    nearby_search_seek_ll.setVisibility(View.GONE);
                }
                isFocus = hasFocus;
            }
        });
        mNearby_change_tv.setOnClickListener(this);
        popupWindow.setListener(this);
    }

    private void initView() {
        flowlayout = (TagFlowLayout) findViewById(R.id.flowlayout);
        findViewById(R.id.nearby_delete_tv).setOnClickListener(this);
        mNearby_change_tv = (TextView) findViewById(R.id.nearby_change_tv);
        mNearby_search_et = (EditText) findViewById(R.id.nearby_search_et);
        findViewById(R.id.nearby_search_tv).setOnClickListener(this);
        nearby_search_history_ll = (LinearLayout) findViewById(R.id.nearby_search_history_ll);
        nearby_search_seek_ll = (LinearLayout) findViewById(R.id.nearby_search_seek_ll);
        nearby_search_history_lv = (ListView) findViewById(R.id.nearby_search_history_lv);

        popupWindow = new NearbySearchChangePopupWindow(this);
    }

    private void initDatas() {
        //历史记录查询
        sqlOperate = new SQLOperateImpl(this);
        searchHistories = sqlOperate.queryDesc2();
        mLbsLocationBean = (LbsLocationBean) getIntent().getExtras().get(NearbySearchActivity.LOCATION);
    }


    /**
     * 历史记录展示
     */
    private void searchHisSet() {
        hisLabelAdapter = new TagAdapter<SearchHistory>(searchHistories) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHistory searchHistory) {

                TextView tv = null;
                switch (searchHistory.type) {
                    //实体店商家
                    case 0:
                        tv = (TextView) inflater.inflate(R.layout.nearby_search_item, flowlayout, false);
                        tv.setTextColor(getResources().getColor(R.color.black));
                        break;
                    //实体店商品
                    case 1:
                        tv = (TextView) inflater.inflate(R.layout.nearby_search_item, flowlayout, false);
                        break;
                }
                tv.setText(searchHistory.name);
                return tv;
            }
        };
        flowlayout.setAdapter(hisLabelAdapter);
        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                jumpToSearchResult(searchHistories.get(position));
                return true;
            }
        });
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby_change_tv:
                popupWindow.showAsDropDown(mNearby_change_tv, -20, 10);
                break;
            case R.id.nearby_search_tv:
                String keyWord = mNearby_search_et.getText().toString().trim();
                if (!TextUtils.isEmpty(keyWord)) {
                    SearchHistory searchHistory = new SearchHistory(keyWord, flagType);
                    sqlOperate.add2(searchHistory);
                    jumpToSearchResult(searchHistory);
                } else {
                    ToastUtil.showToast(this, "搜索内容为空");
                }
                break;
            case R.id.nearby_delete_tv:
                if (searchHistories.size() == 0) {
                    ToastUtil.showToast(this, "没有搜索记录可删除");
                } else {
                 showDialog();
                }
                break;
        }
    }
    /**
     * 对话框
     */
    private void showDialog() {

        final CustomDialog2 dialog=new CustomDialog2(NearbySearchActivity.this);
        dialog.setDialogTitle(getResources().getString(R.string.dialog_title));
        dialog.setNegativeButton(getResources().getString(R.string.negative_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getResources().getString(R.string.positive_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlOperate.delete2();
                searchHistories.clear();
                hisLabelAdapter.notifyDataChanged();
                dialog.dismiss();
            }
        });

    }

    /**
     * 跳转到搜索结果
     *
     * @param searchHistory
     */
    private void jumpToSearchResult(SearchHistory searchHistory) {
        Intent intent = new Intent();
        intent.setClass(NearbySearchActivity.this, SearchResultActivity.class);
        intent.putExtra(SEARCHRESULT, searchHistory);
        intent.putExtra(LOCATION, mLbsLocationBean);
        startActivity(intent);
        finish();
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
            String keyWord = mNearby_search_et.getText().toString().trim();
            int len = keyWord.length();
            if (len <= 0) {
                if (seekAdapter != null) {
                    seekAdapter.clear();
                    seekAdapter.notifyDataSetChanged();
                }
                return;
            }
            NearbySearchControl.requestSeek(keyWord, new NearbySearchControl.INearbySeekResult() {
                @Override
                public void onBackWordsList(final List<String> results) {
                    if (results != null) {
                        seekAdapter = new ArrayAdapter(NearbySearchActivity.this, android.R.layout.simple_list_item_1, results);
                        nearby_search_history_lv.setAdapter(seekAdapter);
                        nearby_search_history_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SearchHistory searchHistory = new SearchHistory(results.get(position), flagType);
                                sqlOperate.add2(searchHistory);
                                seekAdapter.clear();
                                jumpToSearchResult(searchHistory);
                            }
                        });
                    }
                }
            });
        }
    };

    /**
     * 切换店铺/商品的监听
     *
     * @param type
     */
    @Override
    public void onItemClickListener(int type) {
        flagType = type;
        if (type == 0) {
            mNearby_change_tv.setText("店铺");
        } else if (type == 1) {
            mNearby_change_tv.setText("商品");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFocus) {
                nearby_search_seek_ll.setVisibility(View.GONE);
                nearby_search_history_ll.setVisibility(View.VISIBLE);
                if (seekAdapter != null) {
                    seekAdapter.clear();
                }
                mNearby_search_et.setText("");
                mNearby_search_et.clearFocus();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
