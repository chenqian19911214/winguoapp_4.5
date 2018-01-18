package com.winguo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.SearchHistory;
import com.winguo.flowlayout.FlowLayout;
import com.winguo.flowlayout.TagAdapter;
import com.winguo.flowlayout.TagFlowLayout;
import com.winguo.search.history.SQLOperateImpl;
import com.winguo.search.modle.SearchDBService;
import com.winguo.search.modle.SearchOpenHelper;
import com.winguo.search.presenter.WordsListPresenter;
import com.winguo.search.view.ISearchView;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/12/5.
 */

public class StoreSearchActivity extends BaseTitleActivity implements ISearchView, View.OnClickListener {

    private EditText seach_edit_text;
    private TextView seach_btn;
    private LinearLayout ll_no_net;
    private Button requestNetAgain;
    private WordsListPresenter presenter;
    private List<String> wordsList = new ArrayList<>();
    private WordsListAdapter wordsListAdapter;
    private ListView lv_search;
    private FrameLayout btn_clear;
    private LinearLayout ll_search_data;
    private SearchDBService searchDB;
    private List<String>  historyTexts=new ArrayList<>();
    private TextView tv_history_search;
    private TagFlowLayout lv_history_search;
    private TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);
        setBackBtn();
        initViews();
        initListener();
    }


    private void initViews() {
        seach_edit_text = (EditText) findViewById(R.id.seach_edit_text);
        seach_btn = (TextView) findViewById(R.id.seach_btn);

        ll_search_data = (LinearLayout) findViewById(R.id.ll_search_data);
        lv_history_search = (TagFlowLayout) findViewById(R.id.lv_search_history_list);
        tv_history_search = (TextView) findViewById(R.id.tv_hidtory_search);
        btn_clear = (FrameLayout) findViewById(R.id.btn_clear);

        lv_search = (ListView) findViewById(R.id.lv_search_words_list);
        //无网络
        ll_no_net = (LinearLayout) findViewById(R.id.ll_request);
        requestNetAgain = (Button) findViewById(R.id.btn_request_net);
        //判断网络状态
        showNetState();
    }

    /**
     * 根据网络状态显示界面
     */
    private void showNetState() {
        //切换显示状态
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            ll_search_data.setVisibility(View.VISIBLE);
            ll_no_net.setVisibility(View.GONE);
            //初始化回调数据
            if (presenter == null) {
                presenter = new WordsListPresenter(StoreSearchActivity.this);
            }
            //显示搜索历史
            setData();

        } else {
            //如果网络和WiFi都没有，就加载无网络
            ll_no_net.setVisibility(View.VISIBLE);
            ll_search_data.setVisibility(View.GONE);
        }

    }
    /**
     * 设置适配器
     */
    private void setData() {
        tagAdapter = new TagAdapter<String>(historyTexts) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(StoreSearchActivity.this).inflate(R.layout.nearby_search_item, lv_history_search, false);
                textView.setText(s);
                return textView;
            }
        };
        lv_history_search.setAdapter(tagAdapter);
        lv_history_search.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String text = historyTexts.get(position);
                //保存数据到数据库
                saveSearchText(text);
                //跳转
                jumpToProductActivity(text);

                return true;
            }
        });
        setHistoryAdapter();
        wordsListAdapter = new WordsListAdapter(StoreSearchActivity.this);
        lv_search.setAdapter(wordsListAdapter);

    }

    /**
     * 显示数据库的历史记录显示到控件上
     */
    private void setHistoryAdapter() {
        //取出数据库中的数据
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                //初始化查询数据库
                searchDB =SearchDBService.getInstance(StoreSearchActivity.this);
                historyTexts.addAll(searchDB.queryDesc());
                CommonUtil.printI("q请求数据库：=", historyTexts.toString());

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (historyTexts == null || historyTexts.size() == 0) {
                            ll_search_data.setVisibility(View.GONE);
                        } else {
                            ll_search_data.setVisibility(View.VISIBLE);
                                    CommonUtil.printI("q请求数据库后wordsList：=", historyTexts.toString());
                            tagAdapter.notifyDataChanged();

                        }
                    }
                });

            }
        });

    }

    private void initListener() {

        //搜索代码
        seach_btn.setOnClickListener(this);
        //没有网络重新加载
        requestNetAgain.setOnClickListener(this);
        //搜索历史记录清除
        btn_clear.setOnClickListener(this);
        //editText的内容变化监听
        seach_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // ToastUtil.showToast(CommonUtil.getAppContext(), String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (TextUtils.isEmpty(str)) {
                    //搜索字符为空
                    ll_search_data.setVisibility(View.VISIBLE);
                    lv_search.setVisibility(View.GONE);
                    //转换成数据库的
                    setHistoryAdapter();
                } else {
                    //搜索框中有字符，请求网络
                    ll_search_data.setVisibility(View.GONE);
                    lv_search.setVisibility(View.VISIBLE);
                    if (presenter != null) {
                        presenter.getWordsList(String.valueOf(editable));
                    }
                }
            }
        });
        //关键字列表的条目点击事件
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取到点击条目的数据
                if (wordsList != null) {
                    String text = wordsList.get(i);
                    //保存数据到数据库
                    saveSearchText(text);
                    //跳转
                    jumpToProductActivity(text);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seach_btn:
                //把查询的字符存入数据库
                final String texts = seach_edit_text.getText().toString().trim();
                saveSearchText(texts);
                //跳转页面
                jumpToProductActivity(texts);
                break;
            case R.id.btn_request_net:
                showNetState();
                break;
            case R.id.btn_clear:
                //删除历史数据时弹出对话框
                showDialog();
//                searchDB.deleteAll();
                break;
        }
    }

    /**
     * 对话框
     */
    private void showDialog() {

        final CustomDialog2 dialog=new CustomDialog2(StoreSearchActivity.this);
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
                searchDB.deleteAll();
                historyTexts.clear();
                tagAdapter.notifyDataChanged();
                ll_search_data.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

    }

    /**
     * 保存搜索内容到数据库
     *
     * @param text
     */
    private void saveSearchText(final String text) {
        if (!TextUtils.isEmpty(text)) {
            ThreadUtils.runOnBackThread(new Runnable() {
                @Override
                public void run() {
                    //点击搜索按钮，保存字符到数据库
                    searchDB.insert(text);
                }
            });
        }
    }

    /**
     * 跳转页面
     *
     * @param text 传递的数据
     */
    private void jumpToProductActivity(String text) {
        Intent productActivity = new Intent(StoreSearchActivity.this, ProductListActivity.class);
        productActivity.putExtra("text", text);
        startActivity(productActivity);
        finish();

    }

    @Override
    public void showWordsList(List<String> items) {

        //显示数据到控件上
        setWordsListAdapter(items);


    }

    /**
     * 显示网络数据到控件上
     *
     * @param items
     */
    private void setWordsListAdapter(List<String> items) {
        wordsList.clear();
//        wordsListAdapter.notifyDataSetChanged();
        if (items != null) {
            //返回的关键字数据
            for (String text : items) {
                wordsList.add(text);
            }
            CommonUtil.printI("请求网络的中的数据", wordsList.toString());
        }
        wordsListAdapter.notifyDataSetChanged();
    }


    /**
     * wordsList的适配器
     */
    private class WordsListAdapter extends BaseAdapter {
        private Context context;

        public WordsListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (wordsList != null) {
                count = wordsList.size();
            }

            return count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            View view = View.inflate(context, R.layout.words_list_item, null);
            ViewHolder holder;
            if (convertView == null) {
                convertView = view;
                holder = new ViewHolder();
                holder.tv = (TextView) view.findViewById(R.id.tv_words_list_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (wordsList != null) {
                holder.tv.setText(wordsList.get(position));
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tv;
    }


}
