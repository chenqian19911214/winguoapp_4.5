package com.winguo.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseTitleActivity;
import com.winguo.product.controller.ProductHandle;
import com.winguo.product.modle.bean.EvaluateArrBean;
import com.winguo.product.modle.EvaluateItemBean;
import com.winguo.product.modle.ProductEvaluateAllBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hcpai
 * @desc 商品评论
 */
public class ProductEvaluateActivity extends BaseTitleActivity implements View.OnClickListener {

    private View mSuccessView;
    private View mNoDataView;
    private ListView mListView;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    private int mGid;
    private ArrayList<EvaluateItemBean> mData;
    private EvaluateAdapter mEvaluateAdapter;
    private int mPage = 1;
    /**
     * 是否到底
     */
    private boolean mIsEnd = false;
    /**
     * 请求回来的数据是否有更多判断
     */
    private boolean mHasMore;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadingMore = false;
    /**
     * 是否是第一次加载,判断是否有更多不用后台返回的"has_more_items"字段,因为不准
     */
    private boolean isFirst = true;
    private int mEvaluateCurrentCount;
    /**
     * 无网络
     */
    private View noNetView;
    private Button btn_request_net;
    /**
     * 是否超时
     */
    private boolean isTimeout = false;
   // private SwipeRefreshLayout swipe_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_evaluate);
        setBackBtn();
        initViews();
        initDatas();
        initListener();
    }

    /**
     * 初始化视图
     */

    private void initViews() {
        //默认加载中
        LoadDialog.show(this);

        FrameLayout fl_container = (FrameLayout) findViewById(R.id.fl_container);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //有数据
        if (mSuccessView == null) {
            mSuccessView = View.inflate(this, R.layout.order_listview, null);
             //swipe_container = (SwipeRefreshLayout) mSuccessView.findViewById(R.id.swipe_container);
            mListView = (ListView) mSuccessView.findViewById(R.id.order_lv);
            //给listView添加脚布局
            View moreView = View.inflate(this, R.layout.loading_more_view, null);
            ImageView progressBar = (ImageView) moreView.findViewById(R.id.progressBar);
            Glide.with(this).load(R.drawable.loading_more_bg).into(progressBar);
            ll_has_more = (LinearLayout) moreView.findViewById(R.id.ll_has_more);
            no_has_more = (TextView) moreView.findViewById(R.id.no_has_more);
            mListView.addFooterView(moreView);
            fl_container.addView(mSuccessView, params);
        }
        //没有数据
        if (mNoDataView == null) {
            mNoDataView = View.inflate(this, R.layout.loading_empty, null);
            TextView textView = (TextView) mNoDataView.findViewById(R.id.empty_data_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            textView.setText(getString(R.string.no_evaluate));
            fl_container.addView(mNoDataView, params);
        }
        //无网络
        if (noNetView == null) {
            noNetView = View.inflate(this, R.layout.no_net, null);
            btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
            TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
            no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            fl_container.addView(noNetView);
        }

        ll_has_more.setVisibility(View.GONE);
        no_has_more.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);
        noNetView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mGid = (int) getIntent().getExtras().get(ActionUtil.ACTION_PRODUCT_EVALUATE);
        ProductEvaluateAllBean productEvaluateAllBean = (ProductEvaluateAllBean) getIntent().getExtras().getSerializable("productEvaluateAllBean");
        if (productEvaluateAllBean == null) {
            LoadDialog.dismiss(this);
            noNetView.setVisibility(View.VISIBLE);
            isTimeout = true;
            return;
        }
        mData = new ArrayList<>();
        mEvaluateAdapter = new EvaluateAdapter(this);
        handlerEvaluate(productEvaluateAllBean);
        isFirst = false;
        //getDataByNetStatus();
    }

    /**
     * 根据网络状态
     */
    private void getDataByNetStatus() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            noNetView.setVisibility(View.GONE);
            ProductHandle.getEvaluate(handler, mGid, StartApp.limit, mPage);
        } else {
            LoadDialog.dismiss(this);
            if (mSuccessView.getVisibility() != View.VISIBLE) {
                noNetView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置监听器
     */
    private void initListener() {

        mListView.setOnScrollListener(new EvaluateOnScrollListener());
        btn_request_net.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData == null) {
                    return;
                }
                //脚布局点击
                if (position > mData.size() - 1) {
                    if (NetWorkUtil.isNetworkAvailable(ProductEvaluateActivity.this)) {
                        mPage += 1;
                        if (isTimeout) {
                            mPage -= 1;
                        }
                        getDataAgain();
                    } else {
                        Toast.makeText(ProductEvaluateActivity.this, getString(R.string.offline), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!swipe_container.isRefreshing())
            getDataByNetStatus();
            swipe_container.setRefreshing(false);
            ToastUtil.showToast(ProductEvaluateActivity.this,"刷新完成");
        }
        });*/
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
private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_PRODUCT_COMMENT:
                ProductEvaluateAllBean productEvaluateAllBean = (ProductEvaluateAllBean) msg.obj;
                if (productEvaluateAllBean == null) {
                    LoadDialog.dismiss(ProductEvaluateActivity.this);
                    noNetView.setVisibility(View.VISIBLE);
                    return;
                }
                isLoadingMore = false;
                handlerEvaluate(productEvaluateAllBean);
                isFirst = false;
                break;
        }
    }
};

    /**
     * 处理评论列表
     *
     * @param productEvaluateAllBean
     */
    private void handlerEvaluate(ProductEvaluateAllBean productEvaluateAllBean) {
        LoadDialog.dismiss(this);
        isTimeout = false;
        if (!productEvaluateAllBean.isHasData()) {
            //没数据
            mNoDataView.setVisibility(View.VISIBLE);
            ll_has_more.setVisibility(View.GONE);
            no_has_more.setVisibility(View.GONE);
        } else {
            mSuccessView.setVisibility(View.VISIBLE);
            //有数据
            if (productEvaluateAllBean.isObject()) {
                //单个数据
                EvaluateItemBean item = productEvaluateAllBean.getEvaluateObjectBean().getReviewList().getItems().getItem();
                mData.add(item);
                mEvaluateAdapter.setDatas(mData);
                if (!isFirst) {
                    //加载更多的话就刷新数据
                    mEvaluateAdapter.notifyDataSetChanged();
                    no_has_more.setVisibility(View.VISIBLE);
                    ll_has_more.setVisibility(View.GONE);
                    mHasMore = false;
                } else {
                    //不然就是第一次加载,直接设置adapter
                    mListView.setAdapter(mEvaluateAdapter);
                    no_has_more.setVisibility(View.GONE);
                    ll_has_more.setVisibility(View.GONE);
                }
            } else {
                //多条数据
                EvaluateArrBean.ReviewListBean reviewList = productEvaluateAllBean.getEvaluateArrBean().getReviewList();
                List<EvaluateItemBean> items = reviewList.getItems().getItem();
                if (isFirst) {
                    mEvaluateCurrentCount = reviewList.getCount();
                }
                mEvaluateCurrentCount = mEvaluateCurrentCount - items.size();
                mData.addAll(items);
                mEvaluateAdapter.setDatas(mData);
                if (!isFirst) {
                    mEvaluateAdapter.notifyDataSetChanged();
                    ToastUtil.show(this, "加载成功!");
                } else {
                    mListView.setAdapter(mEvaluateAdapter);
                }
                if (mEvaluateCurrentCount > 0) {
                    //有更多
                    mHasMore = true;
                    ll_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setVisibility(View.GONE);
                } else {
                    //没有更多
                    mHasMore = false;
                    no_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.no_has_more_text));
                    ll_has_more.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_net:
                LoadDialog.show(this);
                mPage = 1;
                isFirst = true;
                getDataByNetStatus();
                break;
        }
    }



    /*----------------------滑动监听-----------------------*/

    private class EvaluateOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mIsEnd && mHasMore && !isLoadingMore) {
                if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
                    mPage += 1;
                    if (isTimeout) {
                        mPage -= 1;
                    }
                    ProductHandle.getEvaluate(handler, mGid, mPage);
                } else {
                    LoadDialog.dismiss(getApplicationContext());
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.request_net2));
                    ToastUtil.show(ProductEvaluateActivity.this, getString(R.string.offline));
                }
                isLoadingMore = true;
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mEvaluateAdapter == null) {
                return;
            }
            int lastVisiblePosition = absListView.getLastVisiblePosition();
            mIsEnd = lastVisiblePosition == mEvaluateAdapter.getCount();
        }
    }
    /*----------------------adapter部分-----------------------*/

    private class EvaluateAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<EvaluateItemBean> datas = new ArrayList<>();

        public EvaluateAdapter(Context context) {
            mContext = context;
        }

        public void setDatas(ArrayList<EvaluateItemBean> data) {
            datas = data;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.evaluate_item, null);
                viewHolder.evaluate_name_tv = (TextView) convertView.findViewById(R.id.evaluate_name_tv);
                viewHolder.evaluate_content_tv = (TextView) convertView.findViewById(R.id.evaluate_content_tv);
                viewHolder.evaluate_color_tv = (TextView) convertView.findViewById(R.id.evaluate_color_tv);
                viewHolder.evaluate_size_tv = (TextView) convertView.findViewById(R.id.evaluate_size_tv);
                viewHolder.evaluate_time_tv = (TextView) convertView.findViewById(R.id.evaluate_time_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            initDatas(datas.get(position), viewHolder);
            return convertView;
        }

        private void initDatas(EvaluateItemBean evaluateItemBean, ViewHolder viewHolder) {
            if (TextUtils.isEmpty(evaluateItemBean.getNickname())) {
                viewHolder.evaluate_name_tv.setText("匿名");
            } else {
                viewHolder.evaluate_name_tv.setText(evaluateItemBean.getNickname());
            }
            if (TextUtils.isEmpty(evaluateItemBean.getDetail())) {
                //买家评论内容为空时,后台默认默认为"很好";
                viewHolder.evaluate_content_tv.setText("很好");
            } else {
                viewHolder.evaluate_content_tv.setText(evaluateItemBean.getDetail());
            }
            if (TextUtils.isEmpty(evaluateItemBean.getColor_name())) {
                viewHolder.evaluate_color_tv.setText(String.format(getString(R.string.evaluate_color), "无"));
            } else {
                viewHolder.evaluate_color_tv.setText(String.format(getString(R.string.evaluate_color), evaluateItemBean.getColor_name()));
            }
            if (TextUtils.isEmpty(evaluateItemBean.getSize())) {
                viewHolder.evaluate_size_tv.setText(String.format(getString(R.string.evaluate_size), "无"));
            } else {
                viewHolder.evaluate_size_tv.setText(String.format(getString(R.string.evaluate_size), evaluateItemBean.getSize()));
            }
            viewHolder.evaluate_time_tv.setText(evaluateItemBean.getCreated_at());
        }
    }

    class ViewHolder {
        private TextView evaluate_name_tv, evaluate_content_tv,
                evaluate_color_tv, evaluate_size_tv, evaluate_time_tv;
    }
}
