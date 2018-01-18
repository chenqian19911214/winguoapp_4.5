package com.winguo.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.activity.Search2Activity;
import com.winguo.app.StartApp;
import com.winguo.mine.history.HistoryHandle;
import com.winguo.net.GlideUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;


/**
 * @author hcpai
 * @desc 好友动态
 */
public class FriendsDynamicActivity extends BaseActivity2 {
    private RelativeLayout back2;
    private TextView classify_et;
    private View mNoDataView;
    private View mSuccessView;
    private View noNetView;
    private Button btn_request_net;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    private View mMoreView;
    private ListView mListView;
    private int page;
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
     * 是否超时
     */
    private boolean isTimeout = false;
    private List<FriendDynamicBean.ResultBean> adapterDatas;
    private MyAdapter mAdapter;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_friends_dynamic;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        CommonUtil.stateSetting(this, R.color.white_top_color);
        back2 = (RelativeLayout) findViewById(R.id.back2);
        classify_et = (TextView) findViewById(R.id.classify_et);
        FrameLayout friend_dynamic_container = (FrameLayout) findViewById(R.id.friend_dynamic_container);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //没有数据
        if (mNoDataView == null) {
            mNoDataView = inflate(this, R.layout.loading_empty, null);
            TextView textView = (TextView) mNoDataView.findViewById(R.id.empty_data_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            textView.setText(getString(R.string.no_dynamic));
            friend_dynamic_container.addView(mNoDataView, params);
        }
        //有数据
        if (mSuccessView == null) {
            mSuccessView = inflate(this, R.layout.dynamic_listview, null);
            mListView = (ListView) mSuccessView.findViewById(R.id.order_lv);
            //给listView添加脚布局
            mMoreView = inflate(this, R.layout.loading_more_view, null);
            ll_has_more = (LinearLayout) mMoreView.findViewById(R.id.ll_has_more);
            ImageView progressBar = (ImageView) mMoreView.findViewById(R.id.progressBar);
            Glide.with(this).load(R.drawable.loading_more_bg).into(progressBar);
            no_has_more = (TextView) mMoreView.findViewById(R.id.no_has_more);
            mListView.addFooterView(mMoreView);
            friend_dynamic_container.addView(mSuccessView, params);
        }
        //无网络
        if (noNetView == null) {
            noNetView = inflate(this, R.layout.no_net, null);
            btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
            TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
            no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            friend_dynamic_container.addView(noNetView);
        }
        mAdapter = new MyAdapter(this);
        adapterDatas = new ArrayList<>();
        //默认为加载中...
        mNoDataView.setVisibility(View.GONE);
//        LoadDialog.show(this);
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        getDataByStatus();
    }

    /**
     * 根据网络状态加载数据
     */
    private void getDataByStatus() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            noNetView.setVisibility(View.GONE);
            LoadDialog.show(this);
            FriendsDynamicHandler.getFriendsDynamic(this, handler, StartApp.limit, page);
        } else {
//                LoadDialog.dismiss(this);
            if (mSuccessView.getVisibility() == View.GONE) {
                noNetView.setVisibility(View.VISIBLE);
                mSuccessView.setVisibility(View.GONE);
                mNoDataView.setVisibility(View.GONE);
                ll_has_more.setVisibility(View.GONE);
            } else {
                ll_has_more.setVisibility(View.GONE);
                mNoDataView.setVisibility(View.GONE);
                no_has_more.setVisibility(View.VISIBLE);
                no_has_more.setText(getString(R.string.request_net));
            }
            //停止加载更多
            //mSwipeLayout.setRefreshing(false);
            Toast.makeText(this, getString(R.string.offline), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        back2.setOnClickListener(this);
        btn_request_net.setOnClickListener(this);
        classify_et.setOnClickListener(this);
        mListView.setOnScrollListener(new HistoryOnScrollListener());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (adapterDatas == null) {
                    return;
                }
                //脚布局点击
                if (position > adapterDatas.size() - 1) {
                    if (NetWorkUtil.isNetworkAvailable(FriendsDynamicActivity.this)) {
                        page += 1;
                        if (isTimeout) {
                            page -= 1;
                        }
                        getDataAgain();
                    } else {
                        Toast.makeText(FriendsDynamicActivity.this, getString(R.string.offline), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 重新加载数据
     */
    private void getDataAgain() {
        if (no_has_more.getText().equals(getString(R.string.request_net2))) {
            isFirst = false;
//            LoadDialog.show(this);
            getDataByStatus();
        }
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_FRIEND_DYNAMIC:
                LoadDialog.dismiss(this);
                FriendDynamicBean friendDynamicBean = (FriendDynamicBean) msg.obj;
                if (friendDynamicBean == null) {
                    ToastUtil.show(this, getString(R.string.timeout));
                    return;
                }
                handlerData(friendDynamicBean);
                break;
        }
    }

    /**
     * 处理数据
     */
    private void handlerData(FriendDynamicBean friendDynamicBean) {
        //有数据
        if (friendDynamicBean.getCode().equals("0")) {
            mSuccessView.setVisibility(View.VISIBLE);
            noNetView.setVisibility(View.GONE);
            if (friendDynamicBean.getHasmore().equals("1")) {
                ll_has_more.setVisibility(View.VISIBLE);
                no_has_more.setVisibility(View.GONE);
                mHasMore = true;
                adapterDatas.addAll(friendDynamicBean.getResult());
                if (isFirst) {
                    mAdapter.setDatas(adapterDatas);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                mHasMore = false;
                adapterDatas.addAll(friendDynamicBean.getResult());
                if (isFirst) {
                    mAdapter.setDatas(adapterDatas);
                    mListView.setAdapter(mAdapter);
                    mListView.removeFooterView(mMoreView);
                } else {
                    //加载更多
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setText(R.string.no_has_more_text);
                    no_has_more.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else {
            mNoDataView.setVisibility(View.VISIBLE);
            mSuccessView.setVisibility(View.GONE);
        }
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
                getDataByStatus();
                break;
            case R.id.classify_et:
                startActivity(new Intent(this, Search2Activity.class));
                //ToastUtil.show(this, "跳转到搜索界面");
                break;
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
                    ToastUtil.show(FriendsDynamicActivity.this, getString(R.string.offline));
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

    /*----------------------Adapter部分-----------------------*/
    private class MyAdapter extends BaseAdapter {

        private List<FriendDynamicBean.ResultBean> datas;
        private Activity mContext;

        public MyAdapter(Activity context) {
            this.mContext = context;
        }

        public void setDatas(List<FriendDynamicBean.ResultBean> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (datas == null) {
                return 0;
            }
            return datas.size() == 0 ? 0 : datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
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
                convertView = View.inflate(mContext, R.layout.dynamic_list_item, null);
                viewHolder.dynamic_icon_iv = (ImageView) convertView.findViewById(R.id.dynamic_icon_iv);
                viewHolder.dynamic_name_tv = (TextView) convertView.findViewById(R.id.dynamic_name_tv);
                viewHolder.dynamic_account_tv = (TextView) convertView.findViewById(R.id.dynamic_account_tv);
                viewHolder.dynamic_time_tv = (TextView) convertView.findViewById(R.id.dynamic_time_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            initData(viewHolder, datas.get(position));
            return convertView;
        }

        /**
         * 初始化数据
         */
        private void initData(ViewHolder viewHolder, final FriendDynamicBean.ResultBean resultBean) {
            GlideUtil.getInstance().loadImage(mContext, resultBean.getHeadportrait(), R.drawable.icon, R.drawable.error_img, viewHolder.dynamic_icon_iv);
            String name = resultBean.getM_customer_shop_name();
            if (name.length() > 0) {
                viewHolder.dynamic_name_tv.setText(resultBean.getM_customer_shop_name() + " 空间站");
            } else {
                viewHolder.dynamic_name_tv.setText("**** 空间站");
            }
            viewHolder.dynamic_account_tv.setText(resultBean.getM_customer_account());
            viewHolder.dynamic_time_tv.setText(resultBean.getM_customer_open_time());
        }
    }

    class ViewHolder {
        private TextView dynamic_name_tv, dynamic_account_tv, dynamic_time_tv;
        private ImageView dynamic_icon_iv;
    }
}
