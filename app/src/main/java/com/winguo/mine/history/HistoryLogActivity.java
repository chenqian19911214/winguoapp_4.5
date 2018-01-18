package com.winguo.mine.history;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.activity.ProductActivity;
import com.winguo.app.StartApp;
import com.winguo.mine.address.bean.HistoryBean;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;


/**
 * @author hcpai
 * @desc 浏览历史
 */
public class HistoryLogActivity extends BaseActivity2 implements HistoryListViewAdapter.IHistoryOnItemClickListener {
    private RelativeLayout back2;
    private View mNoDataView;
    private View mSuccessView;
    private View noNetView;
    private Button btn_request_net;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    /**
     * 是否到底
     */
    private boolean mIsEnd = false;
    /**
     * 是否第一次加载数据
     */
    private boolean isFirst = true;
    /**
     * 请求回来的数据是否有更多判断
     */
    private boolean mHasMore;
    /**
     * 第几页
     */
    private int page = 1;
    private ArrayList<HistoryCategoryItemBean> mDateList;
    private ListView mListView;
    private HistoryListViewAdapter mAdapter;
    private List<String> mTitles;
    /**
     * 是否超时
     */
    private boolean isTimeout = false;
    private View mMoreView;
    private int mPosition;
    private String mDateTitle;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_historylog;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        back2 = (RelativeLayout) findViewById(R.id.back2);
        FrameLayout history_container = (FrameLayout) findViewById(R.id.history_list_container);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //没有数据
        if (mNoDataView == null) {
            mNoDataView = inflate(this, R.layout.loading_empty, null);
            TextView textView = (TextView) mNoDataView.findViewById(R.id.empty_data_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            textView.setText(getString(R.string.no_history));
            history_container.addView(mNoDataView, params);
        }
        //有数据
        if (mSuccessView == null) {
            mSuccessView = View.inflate(this, R.layout.history_listview, null);
            mListView = (ListView) mSuccessView.findViewById(R.id.order_lv);
            //给listView添加脚布局
            mMoreView = View.inflate(this, R.layout.loading_more_view, null);
            ll_has_more = (LinearLayout) mMoreView.findViewById(R.id.ll_has_more);
            ImageView progressBar = (ImageView) mMoreView.findViewById(R.id.progressBar);
            Glide.with(this).load(R.drawable.loading_more_bg).into(progressBar);
            no_has_more = (TextView) mMoreView.findViewById(R.id.no_has_more);
            mListView.addFooterView(mMoreView);
            history_container.addView(mSuccessView, params);
        }
        //无网络
        if (noNetView == null) {
            noNetView = inflate(this, R.layout.no_net, null);
            btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
            TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
            no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            history_container.addView(noNetView);
        }
        //默认为加载中...
        mNoDataView.setVisibility(View.GONE);
        LoadDialog.show(this);
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        LoadDialog.show(this);
        getDataByNetStatus();
    }

    /**
     * 根据网络状态加载数据
     */
    private void getDataByNetStatus() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            HistoryHandle.getHistory(handler, this, StartApp.limit, page);
        } else {
            //无网络
            if (mSuccessView.getVisibility() == View.VISIBLE) {
                noNetView.setVisibility(View.GONE);
                ll_has_more.setVisibility(View.GONE);
                mNoDataView.setVisibility(View.GONE);
                no_has_more.setVisibility(View.VISIBLE);
                no_has_more.setText(getString(R.string.request_net));
            } else {
                noNetView.setVisibility(View.VISIBLE);
                mNoDataView.setVisibility(View.GONE);
            }
            LoadDialog.dismiss(this);
            ToastUtil.show(this, getString(R.string.offline));
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back2.setOnClickListener(this);
        btn_request_net.setOnClickListener(this);
        mListView.setOnScrollListener(new HistoryOnScrollListener());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mDateList == null) {
                    return;
                }
                int tempSize = 0;
                for (int i = 0; i < mDateList.size(); i++) {
                    tempSize += mDateList.get(i).getLists().size();
                }
                //脚布局点击
                if (position > tempSize - 1) {
                    page += 1;
                    if (isTimeout) {
                        page -= 1;
                    }
                    getDataAgain();
                }
            }
        });
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_HISTORYLOG_HAS:
                LoadDialog.dismiss(this);
                mSuccessView.setVisibility(View.VISIBLE);
                noNetView.setVisibility(View.GONE);
                mNoDataView.setVisibility(View.GONE);
                HistoryBean historyBean = (HistoryBean) msg.obj;
                //处理数据
                handlerData(historyBean);
                break;
            case RequestCodeConstant.REQUEST_DELHISTORYLOG_HAS:
                int code = (int) msg.obj;
                if (code == 0) {
                    int size = mDateList.size();
                    int totalCount = 0;
                    for (int i = 0; i < size; i++) {
                        HistoryCategoryItemBean historyCategoryItemBean = mDateList.get(i);
                        if (historyCategoryItemBean.getDate().equals(mDateTitle)) {
                            int resultCount = mPosition - totalCount;
                            //某一天的历史记录只有一条时,直接删除一整天
                            if (historyCategoryItemBean.getItemCount() == 2) {
                                mDateList.remove(i);
                                break;
                            } else {
                                //多条时,删除一天的某一条
                                historyCategoryItemBean.getLists().remove(resultCount - 2);
                            }
                        } else {
                            totalCount += historyCategoryItemBean.getItemCount();
                        }
                    }
                    //mDateList.clear();
                    //mTitles.clear();
                    mAdapter.notifyDataSetChanged();
                    ToastUtil.show(this, "删除浏览历史成功!");
                    //LoadDialog.show(this);
                    //getDataByNetStatus();
                } else if (code == 1) {
                    ToastUtil.show(this, "删除浏览历史失败!");
                } else if (code == -1) {
                    ToastUtil.show(this, getString(R.string.timeout));
                }
                LoadDialog.dismiss(this);
                break;
        }
    }

    /**
     * 处理网络返回的数据
     *
     * @param historyBean
     */
    private void handlerData(HistoryBean historyBean) {
        if (historyBean != null) {
            //没有浏览记录
            if (historyBean.getCount() == 0) {
                mNoDataView.setVisibility(View.VISIBLE);
                mSuccessView.setVisibility(View.GONE);
                noNetView.setVisibility(View.GONE);
                //有浏览记录
            } else {
                //有更多
                List<HistoryItemBean> data = historyBean.getData();
                setTitle(data);
                if (historyBean.isHas_more()) {
                    //第一次加载用setAdapter();
                    if (isFirst) {
                        mAdapter = new HistoryListViewAdapter(this, mDateList, this);
                        mListView.setAdapter(mAdapter);
                        //第二次加载用adapter的setData()方法;
                    } else {
                        mAdapter.setData(mDateList);
                        mAdapter.notifyDataSetChanged();
                    }
                    mHasMore = true;
                    ll_has_more.setVisibility(View.VISIBLE);
                    //没有更多
                } else {
                    ll_has_more.setVisibility(View.GONE);
                    if (isFirst) {
                        mAdapter = new HistoryListViewAdapter(this, mDateList, this);
                        mListView.setAdapter(mAdapter);
                        mListView.removeFooterView(mMoreView);
                        //mMoreView.setVisibility(View.GONE);
                    } else {
                        //第二次加载
                        mAdapter.setData(mDateList);
                        mAdapter.notifyDataSetChanged();
                        no_has_more.setText(getString(R.string.no_has_more_text));
                        no_has_more.setVisibility(View.VISIBLE);
                    }
                    mHasMore = false;
                }
            }
            isFirst = false;
            //连接超时
        } else {
            if (isFirst) {
                noNetView.setVisibility(View.VISIBLE);
                isFirst = false;
            } else {
                noNetView.setVisibility(View.GONE);
                ll_has_more.setVisibility(View.GONE);
                no_has_more.setVisibility(View.VISIBLE);
                no_has_more.setText(getString(R.string.request_net2));
            }
            isTimeout = true;
            return;
        }
        isTimeout = false;
    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.back2:
                finish();
                break;
            case R.id.btn_request_net:
                //重新加载网络
                page = 1;
                isFirst = true;
                LoadDialog.show(this);
                getDataByNetStatus();
                break;
        }
    }

    /**
     * 添加分组标题
     *
     * @param historyBeanList
     */
    private void setTitle(List<HistoryItemBean> historyBeanList) {
        if (isFirst && mDateList == null) {
            mDateList = new ArrayList<>();
            mTitles = new ArrayList<>();
        }
        //给item的date赋值
        for (int i = 0; i < historyBeanList.size(); i++) {
            String dateTime = historyBeanList.get(i).getM_item_access_rdatetime();
            String subDateTime = dateTime.substring(5, 10);
            String[] split = subDateTime.split("-");
            String date = split[0] + " 月 " + split[1] + " 日";
            historyBeanList.get(i).setDate(date);
        }
        for (int i = 0; i < historyBeanList.size(); i++) {
            HistoryItemBean historyItemBean = historyBeanList.get(i);
            String date = historyItemBean.getDate();
            HistoryCategoryItemBean historyCategoryItemBean;
            //分组不存在
            if (!mTitles.contains(date)) {
                mTitles.add(date);
                historyCategoryItemBean = new HistoryCategoryItemBean();
                historyCategoryItemBean.setDate(date);
                List<HistoryItemBean> historyItemBeans;
                if (historyCategoryItemBean.getLists() == null) {
                    historyItemBeans = new ArrayList<>();
                } else {
                    historyItemBeans = historyCategoryItemBean.getLists();
                }
                historyItemBeans.add(historyItemBean);
                historyCategoryItemBean.setLists(historyItemBeans);
                mDateList.add(historyCategoryItemBean);
            } else {
                //分组存在
                HistoryCategoryItemBean itemBean;
                for (int j = 0; j < mDateList.size(); j++) {
                    itemBean = mDateList.get(j);
                    String date2 = itemBean.getDate();
                    if (date.equals(date2)) {
                        itemBean.getLists().add(historyItemBean);
                        break;
                    }
                }
            }
        }
    }

    private class HistoryOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mIsEnd && mHasMore) {
                if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
                    page += 1;
                    if (isTimeout) {
                        page -= 1;
                    }
                    HistoryHandle.getHistory(page);
                } else {
                    LoadDialog.dismiss(getApplicationContext());
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.request_net2));
                    ToastUtil.show(HistoryLogActivity.this, getString(R.string.offline));
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mAdapter == null) {
                return;
            }
            int lastVisiblePosition = absListView.getLastVisiblePosition();
            mIsEnd = lastVisiblePosition == mAdapter.getCount();
        }

    }

    /**
     * item的点击回调(自定义接口)
     *
     * @param historyBean
     * @param position
     */
    @Override
    public void onItemClick(HistoryItemBean historyBean, int position) {
        LoadDialog.show(this);
        Intent intent = new Intent();
        intent.putExtra("gid", Integer.valueOf(historyBean.getM_item_id()));
        intent.setClass(this, ProductActivity.class);
        startActivity(intent);
    }


    @Override
    public void delHistoryLog(String gid, int position, String dateTitle) {
        mPosition = position + 1;
        mDateTitle = dateTitle;
        LoadDialog.show(this);
        HistoryHandle.delHistory(handler, this, gid);
    }

    /**
     * 点击脚布局重新加载数据
     */
    private void getDataAgain() {
        if (no_has_more.getText().equals(getString(R.string.request_net2))) {
            no_has_more.setVisibility(View.GONE);
            ll_has_more.setVisibility(View.VISIBLE);
            isFirst = false;
            getDataByNetStatus();
        }
    }
}
