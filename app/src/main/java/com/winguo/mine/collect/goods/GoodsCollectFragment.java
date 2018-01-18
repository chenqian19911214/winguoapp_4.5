package com.winguo.mine.collect.goods;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.bumptech.glide.Glide;
import com.winguo.R;
import com.winguo.activity.ProductActivity;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragment2;
import com.winguo.mine.collect.CollectHandler;
import com.winguo.mine.collect.bean.DeleteCollectBean;
import com.winguo.net.GlideUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hcpai
 * @desc 商品收藏
 */
public class GoodsCollectFragment extends BaseFragment2 implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * 无网络
     */
    private View noNetView;
    private Button btn_request_net;
    private View mNoDataView;
    private View mSuccessView;
    private ListView mListView;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    //是否第一次加载
    public static boolean isFirst = true;
    private List<GoodsCollectItemBean> mDatas = new ArrayList<>();
    private MyAdapter mAdapter;
    public int page = 1;
    /**
     * 是否到底
     */
    private boolean mIsEnd = false;
    /**
     * 是否有更多
     */
    private boolean mHasMore = false;
    private int mPosition;
    /**
     * 是否超时
     */
    private boolean isTimeout = false;
    private View mMoreView;
    //private SwipeRefreshLayout mSwipeLayout;

    /**
     * 布局资源
     *
     * @return
     */
    @Override

    protected int getLayout() {
        return R.layout.fragment_collect;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    @Override
    protected void initView(View view) {
        FrameLayout shop_collect_container = (FrameLayout) view.findViewById(R.id.collect_container);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //没有数据
        mNoDataView = View.inflate(mActivity, R.layout.loading_empty, null);
        TextView textView = (TextView) mNoDataView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.no_goods_collect));
        shop_collect_container.addView(mNoDataView, params);
        //无网络
        noNetView = View.inflate(mActivity, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        shop_collect_container.addView(noNetView);
        //有数据
        mSuccessView = View.inflate(mActivity, R.layout.order_listview, null);
        mListView = (ListView) mSuccessView.findViewById(R.id.order_lv);
        //mSwipeLayout = (SwipeRefreshLayout) mSuccessView.findViewById(R.id.swipe_container);
        //给listView添加脚布局
        mMoreView = View.inflate(mActivity, R.layout.loading_more_view, null);
        ll_has_more = (LinearLayout) mMoreView.findViewById(R.id.ll_has_more);
        ImageView progressBar = (ImageView) mMoreView.findViewById(R.id.progressBar);
        Glide.with(this).load(R.drawable.loading_more_bg).into(progressBar);
        no_has_more = (TextView) mMoreView.findViewById(R.id.no_has_more);
        mListView.addFooterView(mMoreView);
        shop_collect_container.addView(mSuccessView, params);
        mAdapter = new MyAdapter(mActivity);
        //默认状态为隐藏
        mSuccessView.setVisibility(View.GONE);
        noNetView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
        isFirst = true;
        LoadDialog.show(mContext);
        getDataByStatus();
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        //mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        //mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
        //       android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        //mSwipeLayout.setProgressBackgroundColor(R.color.red); // 设定下拉圆圈的背景
        //mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        btn_request_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirst = true;
                page = 1;
                LoadDialog.show(mActivity);
                getDataByStatus();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirst = true;
        page = 1;
        LoadDialog.show(mActivity);
        getDataByStatus();
    }

    /**
     * 根据网络状态加载数据
     */
    private void getDataByStatus() {
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            CollectHandler.getGoodsCollect(getActivity(), handle, StartApp.limit, page, false);
        } else {
            if (isFirst) {
                changeStatus(1);
            } else {
                no_has_more.setVisibility(View.VISIBLE);
                ll_has_more.setVisibility(View.GONE);
                changeStatus(3);
            }
            //停止加载更多
            //mSwipeLayout.setRefreshing(false);
            ToastUtil.show(mActivity, getString(R.string.offline));
        }
    }

    /**
     * @param status 1.表示没有网络 2.没有数据 3.有数据
     */
    public void changeStatus(int status) {
        //没有网络
        if (status == 1) {
            noNetView.setVisibility(View.VISIBLE);
            mNoDataView.setVisibility(View.GONE);
            mSuccessView.setVisibility(View.GONE);
            //没有数据
        } else if (status == 2) {
            mNoDataView.setVisibility(View.VISIBLE);
            noNetView.setVisibility(View.GONE);
            mSuccessView.setVisibility(View.GONE);
        } else if (status == 3) {
            mSuccessView.setVisibility(View.VISIBLE);
            noNetView.setVisibility(View.GONE);
            mNoDataView.setVisibility(View.GONE);
        }
    }

    /**
     * 加载数据
     */
    public void setData(GoodsCollectAllBean goodsCollectAllBean) {
        LoadDialog.dismiss(mActivity);
        changeStatus(3);
        //第一次加载数据时要清空数据集
        if (isFirst) {
            mDatas.clear();
            page = 1;
        }
        if (goodsCollectAllBean.isEmpty()) {
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }
        if (goodsCollectAllBean.getIsObject()) {
            GoodsCollectObjBean.RootBean root = goodsCollectAllBean.getGoodsCollectObjBean().getRoot();
            //单个收藏商品
            mDatas.add(root.getItems().getItem());
            if (isFirst) {
                mAdapter.setDatas(mDatas);
                mListView.setAdapter(mAdapter);
                mListView.removeFooterView(mMoreView);
                //mMoreView.setVisibility(View.GONE);
            } else {
                ll_has_more.setVisibility(View.GONE);
                no_has_more.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
            mHasMore = false;
        } else {
            //多个收藏商品
            GoodsCollectArryBean.RootBean root = goodsCollectAllBean.getGoodsCollectArryBean().getRoot();
            if (root.getHas_more() == 1) {
                ll_has_more.setVisibility(View.VISIBLE);
                no_has_more.setVisibility(View.GONE);
                mHasMore = true;
                mDatas.addAll(root.getItems().getItem());
                if (isFirst) {
                    mAdapter.setDatas(mDatas);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                //没有更多数据--->判断是否是加载更多还是第一次加载,进而判断"没有更多"是否显示
                //第一次加载
                mHasMore = false;
                mDatas.addAll(root.getItems().getItem());
                if (isFirst) {
                    mListView.removeFooterView(mMoreView);
                    //mMoreView.setVisibility(View.GONE);
                    mAdapter.setDatas(mDatas);
                    mListView.setAdapter(mAdapter);
                } else {
                    //加载更多
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setText(R.string.no_has_more_text);
                    no_has_more.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        //mSwipeLayout.setRefreshing(false);
        //添加滑动监听
        mListView.setOnScrollListener(new OrderOnScrollListener());
        //添加item的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mDatas == null) {
                    return;
                }
                if (position > mDatas.size() - 1) {
                    if (NetWorkUtil.isNetworkAvailable(mActivity)) {
                        page += 1;
                        if (isTimeout) {
                            page -= 1;
                        }
                        getDataAgain();
                    } else {
                        //停止加载更多
                        // mSwipeLayout.setRefreshing(false);
                        ToastUtil.show(mActivity, getString(R.string.offline));
                    }
                    return;
                }
                LoadDialog.show(mActivity);
                //跳转到商品详情
                //ToastUtil.show(mActivity, "跳转到商品详情");
                Intent intent = new Intent();
                intent.putExtra("gid", mDatas.get(position).getGid());
                intent.setClass(mActivity, ProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataAgain() {
        if (no_has_more.getText().equals(getString(R.string.request_net2))) {
            isFirst = false;
            LoadDialog.show(mContext);
            getDataByStatus();
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_GOODS_COLLECT:
                GoodsCollectAllBean goodsCollectAllBeans = (GoodsCollectAllBean) msg.obj;
                if (goodsCollectAllBeans == null) {
                    isTimeout = true;
                    //停止加载更多
                    // mSwipeLayout.setRefreshing(false);
                    ToastUtil.show(mActivity, getString(R.string.timeout));
                    //第一次加载超时
                    LoadDialog.dismiss(mActivity);
                    if (isFirst) {
                        changeStatus(1);
                        //加载更多时超时
                    } else {
                        ll_has_more.setVisibility(View.GONE);
                        no_has_more.setVisibility(View.VISIBLE);
                        no_has_more.setText(R.string.request_net2);
                    }
                    return;
                }
                isTimeout = false;
                setData(goodsCollectAllBeans);
                break;
            case RequestCodeConstant.REQUEST_DELETE_GOODS_COLLECT:
                DeleteCollectBean deleteCollectBean = (DeleteCollectBean) msg.obj;
                if (deleteCollectBean == null) {
                    //停止加载更多
                    // mSwipeLayout.setRefreshing(false);
                    ToastUtil.show(mActivity, getString(R.string.timeout));
                    return;
                }
                if (deleteCollectBean.getMessage().getCode() == 1) {
                    LoadDialog.dismiss(mActivity);
                    ToastUtil.show(getActivity(), "删除收藏商品成功!");
                    //更新adapter
                    mDatas.remove(mPosition);
                    if (mDatas.size() < 6 && mDatas.size() != 0) {
                        ll_has_more.setVisibility(View.GONE);
                        no_has_more.setVisibility(View.GONE);
                    }
                    if (mDatas.size() == 0) {
                        mSuccessView.setVisibility(View.GONE);
                        mNoDataView.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(getActivity(), deleteCollectBean.getMessage().getText());
                }
                break;
        }
    }

    /**
     * 下拉刷新的回调接口
     */
    @Override
    public void onRefresh() {
        page = 1;
        isFirst = true;
        getDataByStatus();
    }

    /*----------------------Listview监听部分-----------------------*/
    private class OrderOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mIsEnd && mHasMore) {
                if (NetWorkUtil.isNetworkAvailable(getContext())) {
                    page += 1;
                    if (isTimeout) {
                        page -= 1;
                    }
                    isFirst = false;
                    CollectHandler.getGoodsCollect(getActivity(), handle, StartApp.limit, page, false);
                } else {
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.request_net2));
                    //停止加载更多
                    // mSwipeLayout.setRefreshing(false);
                    ToastUtil.show(getActivity(), getString(R.string.offline));
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mAdapter != null) {
                int lastVisiblePosition = absListView.getLastVisiblePosition();
                mIsEnd = lastVisiblePosition == mAdapter.getCount();
            }
        }
    }

    /*----------------------Adapter部分-----------------------*/
    private class MyAdapter extends BaseAdapter {

        private List<GoodsCollectItemBean> datas;
        private Activity mContext;

        public MyAdapter(Activity context) {
            this.mContext = context;
        }

        public void setDatas(List<GoodsCollectItemBean> datas) {
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
                convertView = View.inflate(mContext, R.layout.goods_collect_list_item, null);
                viewHolder.goods_collect_picture_iv = (ImageView) convertView.findViewById(R.id.goods_collect_picture_iv);
                viewHolder.goods_collect_name_tv = (TextView) convertView.findViewById(R.id.goods_collect_name_tv);
                viewHolder.goods_collect_price_tv = (TextView) convertView.findViewById(R.id.goods_collect_price_tv);
                //viewHolder.goods_collect_attention_people_tv = (TextView) convertView.findViewById(R.id.goods_collect_attention_people_tv);
                //viewHolder.goods_collect_repertory_tv = (TextView) convertView.findViewById(R.id.goods_collect_repertory_tv);
                viewHolder.goods_collect_delete_btn = (Button) convertView.findViewById(R.id.goods_collect_delete_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            initData(viewHolder, datas.get(position), position);
            return convertView;
        }

        /**
         * 初始化数据
         */
        private void initData(ViewHolder viewHolder, final GoodsCollectItemBean goodsCollectItemBean, final int position) {
            GlideUtil.getInstance().loadImage(mContext, goodsCollectItemBean.getPic().getContent(), R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, viewHolder.goods_collect_picture_iv);
            viewHolder.goods_collect_name_tv.setText(goodsCollectItemBean.getName());
            viewHolder.goods_collect_price_tv.setText("¥ " + goodsCollectItemBean.getPrice());
            //TODO 关注人数暂时设置为成交记录
            //viewHolder.goods_collect_attention_people_tv.setText(getString(R.string.mycollect_attention_people, goodsCollectItemBean.getSale_qty()));
            //viewHolder.goods_collect_repertory_tv.setText(getString(R.string.mycollect_repertory, goodsCollectItemBean.getStock()));
            viewHolder.goods_collect_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialog2 dialog = new CustomDialog2(mContext);
                    dialog.setDialogTitle(getResources().getString(R.string.mycollect_dialog_title));
                    dialog.setNegativeButton(getResources().getString(R.string.negative_text), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton(getResources().getString(R.string.positive_text), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (NetWorkUtil.isNetworkAvailable(getContext())) {
                                LoadDialog.show(mActivity);
                                CollectHandler.deleteGoodsCollect(mContext, handle, goodsCollectItemBean.getGid());
                                mPosition = position;
                                dialog.dismiss();
                            } else {
                                //停止加载更多
                                // mSwipeLayout.setRefreshing(false);
                                ToastUtil.show(getActivity(), getString(R.string.offline));
                            }
                        }
                    });
                }
            });
        }
    }

    class ViewHolder {
        private TextView goods_collect_name_tv, goods_collect_price_tv;
        //goods_collect_attention_people_tv, goods_collect_repertory_tv;
        private ImageView goods_collect_picture_iv;
        private Button goods_collect_delete_btn;
    }
}
