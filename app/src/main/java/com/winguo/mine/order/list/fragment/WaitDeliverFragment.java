package com.winguo.mine.order.list.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragment2;
import com.winguo.mine.order.OrderHandler;
import com.winguo.mine.order.detail.OrderDetailActivity;
import com.winguo.mine.order.detail.OrderDetailAllBean;
import com.winguo.mine.order.bean.OrderDetailArryBean;
import com.winguo.mine.order.detail.OrderDetailItemBean;
import com.winguo.mine.order.bean.OrderDetailObjBean;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.mine.order.bean.ConfirmReceiveBean;
import com.winguo.mine.order.list.OrderBeanList;
import com.winguo.mine.order.bean.OrderDataBean;
import com.winguo.mine.order.viewpager.ViewPagerBean;
import com.winguo.net.GlideUtil;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc 待发货
 */
public class WaitDeliverFragment extends BaseFragment2 {
    /**
     * 订单列表数据
     */
    private OrderBeanList mOrderBeanList;
    /**
     * 第几页
     */
    private int mPage = 1;
    private ListView mListView;
    private View mSuccessView;
    private View mNoDataView;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    private OrderAdapter mAdapter;
    private ArrayList<OrderDataBean> adapterDatas = new ArrayList<>();
    private static final String ARG_POSITION = "position";
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
     * 记录是否第一次加载订单,方便记录订单总数
     */
    private boolean isFirst = true;
    /**
     * 是否超时
     */
    private boolean isTimeout = false;
    /**
     * 当前订单商品种类数
     */
    private int mOrderCurrentCount;
    /**
     * 当前被点击的item
     */
    private View noNetView;
    private Button btn_request_net;
    private View mMoreView;
    private List<OrderDetailAllBean> mOrderDetailAllBeanList = new ArrayList<>();
    private ViewPagerBean viewPagerBean;
    private MyBroadcastReceiver receiver;
    private boolean isRefresh = false;

    public static WaitDeliverFragment newInstance(int position) {
        WaitDeliverFragment f = new WaitDeliverFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    /**
     * 布局资源
     *
     * @return
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_myorder_all;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    @Override
    protected void initView(View view) {
        FrameLayout order_list_container = (FrameLayout) view.findViewById(R.id.order_list_container);
         /*---------------------------------------------*/
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //有数据
        mSuccessView = View.inflate(mContext, R.layout.order_listview, null);
        mListView = (ListView) mSuccessView.findViewById(R.id.order_lv);
        //mSwipeLayout = (SwipeRefreshLayout) mSuccessView.findViewById(R.id.swipe_container);
        //给listView添加脚布局
        mMoreView = View.inflate(mContext, R.layout.loading_more_view, null);
        ll_has_more = (LinearLayout) mMoreView.findViewById(R.id.ll_has_more);
        ImageView progressBar = (ImageView) mMoreView.findViewById(R.id.progressBar);
        Glide.with(this).load(R.drawable.loading_more_bg).into(progressBar);
        no_has_more = (TextView) mMoreView.findViewById(R.id.no_has_more);
        mListView.addFooterView(mMoreView);
        order_list_container.addView(mSuccessView, params);
        //没有数据
        mNoDataView = View.inflate(mContext, R.layout.loading_empty, null);
        TextView textView = (TextView) mNoDataView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.no_order));
        order_list_container.addView(mNoDataView, params);
        //无网络
        noNetView = View.inflate(mContext, R.layout.no_net, null);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        order_list_container.addView(noNetView);
        /**
         * 默认加载中...
         */
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
        ll_has_more.setVisibility(View.GONE);
        no_has_more.setVisibility(View.GONE);


        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("reselectedListener");
        receiver = new MyBroadcastReceiver();
        mActivity.registerReceiver(receiver, intentFilter);
    }


    @Override
    protected void loadData() {
        if (viewPagerBean == null) {
            viewPagerBean = new ViewPagerBean();
            LoadDialog.show(mContext);
            getDataByNetStatus();
        } else {
            if (viewPagerBean.isEmpty()) {
                mNoDataView.setVisibility(View.VISIBLE);
            } else {
                isFirst = viewPagerBean.isFirst();
                mPage = viewPagerBean.getPage();
                adapterDatas = viewPagerBean.getAdapterDatas();
                mOrderDetailAllBeanList = viewPagerBean.getOrderDetailAllBeanList();
                mAdapter.setData(adapterDatas);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(receiver);
    }

    /**
     * 重置数据
     */
    private void resetData() {
        isFirst = true;
        mOrderDetailAllBeanList.clear();
        adapterDatas.clear();
        mPage = 1;
    }

    /**
     * 根据网络状态加载数据
     */
    private void getDataByNetStatus() {
        //TODO
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            OrderHandler.searchOrder(mContext, handle, WinguoAccountDataMgr.getUserName(mContext), mPage, StartApp.limit, "2", 0);
        } else {
            if (LoadDialog.isNowShowing()) {
                LoadDialog.dismiss(mContext);
            }
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
            Toast.makeText(mContext, getString(R.string.offline), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        mListView.setOnScrollListener(new OrderOnScrollListener());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (adapterDatas == null) {
                    return;
                }
                //脚布局点击
                if (position > adapterDatas.size() - 1) {
                    if (NetWorkUtil.isNetworkAvailable(mContext)) {
                        mPage += 1;
                        if (isTimeout) {
                            mPage -= 1;
                        }
                        getDataAgain();
                    } else {
                        Toast.makeText(mContext, getString(R.string.offline), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                OrderDataBean orderDataBean = adapterDatas.get(position);
                long number = orderDataBean.getNumber();
                int oid = orderDataBean.getOid();
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(ActionUtil.ACTION_ORDER_ID, oid);
                int size = mOrderDetailAllBeanList.size();
                for (int i = 0; i < size; i++) {
                    OrderDetailAllBean orderDetailAllBean = mOrderDetailAllBeanList.get(i);
                    if (orderDetailAllBean.getIsObject()) {
                        long no = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData().getNo();
                        if (no == number) {
                            intent.putExtra(ActionUtil.ACTION_ORDER_DETAIL, orderDetailAllBean);
                            break;
                        }
                    } else {
                        long no = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getNo();
                        if (no == number) {
                            intent.putExtra(ActionUtil.ACTION_ORDER_DETAIL, orderDetailAllBean);
                            break;
                        }
                    }
                }
                //TODO 点击事件
                startActivity(intent);
            }
        });
        //连接网络时,重新加载数据
        btn_request_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDialog.show(mContext);
                mPage = 1;
                isFirst = true;
                getDataByNetStatus();
            }
        });
        /*mSwipeLayout.setRefreshing(false);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!mSwipeLayout.isRefreshing()) {
                    resetData();
                    getDataByNetStatus();
                    mSwipeLayout.setRefreshing(false);
                }
            }
        });*/
        //设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        //mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
        //      android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        //        //mSwipeLayout.setProgressBackgroundColor(R.color.red); // 设定下拉圆圈的背景
        //mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
    }

    /**
     * 查询的返回的订单数
     */
    private int orderCount = 0;

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_SEARCH_ORDER:
                //每次请求订单详情时,都把orderCount置为0,防止与首次加载和加载更多重复累计
                orderCount = 0;
                mOrderBeanList = (OrderBeanList) msg.obj;
                no_has_more.setText(getString(R.string.no_has_more_text));
                if (mOrderBeanList == null) {
                    LoadDialog.dismiss(mContext);
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.request_net2));
                    //停止加载更多
                    //mSwipeLayout.setRefreshing(false);
                    Toast.makeText(mContext, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                    isTimeout = true;
                    return;
                }
                isTimeout = false;
                if (isFirst) {
                    mOrderCurrentCount = mOrderBeanList.getCount();
                }
                setHasMore(mOrderBeanList.getCount());
                break;
            case RequestCodeConstant.REQUEST_SEARCHORDER_DETAIL:
                OrderDetailAllBean orderDetailAllBean = (OrderDetailAllBean) msg.obj;
                if (orderDetailAllBean == null) {
                    LoadDialog.dismiss(mContext);
                    isTimeout = true;
                    //停止加载更多
                    //mSwipeLayout.setRefreshing(false);
                    Toast.makeText(mContext, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                    return;
                }
                isTimeout = false;
                int size = mOrderBeanList.getOrderDataBeanList().size();
                if (orderCount < size) {
                    mOrderDetailAllBeanList.add(orderDetailAllBean);
                    orderCount++;
                    if (orderCount == size) {
                        setGoodsValue(mOrderBeanList, mOrderDetailAllBeanList);
                    }
                }
                break;
            case RequestCodeConstant.REQUEST_CONFIRM_RECEIVE:
                ConfirmReceiveBean confirmReceiveBean = (ConfirmReceiveBean) msg.obj;
                LoadDialog.dismiss(mContext);
                if (confirmReceiveBean == null) {
                    ToastUtil.show(mActivity, getString(R.string.timeout_confirm_receive_fail));
                } else {
                    ConfirmReceiveBean.MessageBean message = confirmReceiveBean.getMessage();
                    if (message.getCode() == 1) {
                        ToastUtil.show(mActivity, "确认收货成功!");
                        resetData();
                        getDataByNetStatus();
                    } else {
                        ToastUtil.show(mActivity, message.getText());
                    }
                }
                break;
        }
    }

    /**
     * 设置是否有更多数据
     *
     * @param orderAll 订单总数
     */
    private void setHasMore(int orderAll) {
        //有数据
        if (orderAll != 0) {
            getGoodsDetail(mOrderBeanList);
            //没数据
        } else {
            mSuccessView.setVisibility(View.INVISIBLE);
            mNoDataView.setVisibility(View.VISIBLE);
            LoadDialog.dismiss(mContext);
            if (isRefresh) {
                Toast.makeText(mContext, "重新加载数据成功!", Toast.LENGTH_SHORT).show();
            }
            //保存数据
            viewPagerBean.setEmpty(true);
            viewPagerBean.setPage(mPage);
        }
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDetail(OrderBeanList orderBeanList) {
        List<OrderDataBean> orderDataBeanList = orderBeanList.getOrderDataBeanList();
        for (int i = 0; i < orderDataBeanList.size(); i++) {
            int oid = orderDataBeanList.get(i).getOid();
            if (NetWorkUtil.isNetworkAvailable(mContext)) {
                OrderHandler.searchOrderDetail(mContext, handle, oid);
            } else {
                //停止加载更多
                // mSwipeLayout.setRefreshing(false);
                Toast.makeText(mContext, getString(R.string.offline), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 设置商品属性,结合两个接口(订单列表,订单详情)的数据
     *
     * @param orderBeanList          订单列表
     * @param orderDetailAllBeanList 订单详情
     */
    private void setGoodsValue(OrderBeanList orderBeanList, List<OrderDetailAllBean> orderDetailAllBeanList) {
        List<OrderDataBean> orderDataBeanList = orderBeanList.getOrderDataBeanList();
        for (int i = 0; i < orderDataBeanList.size(); i++) {
            //订单列表
            OrderDataBean orderDataBean = orderDataBeanList.get(i);
            long number = orderDataBean.getNumber();

            for (int j = 0; j < orderDetailAllBeanList.size(); j++) {
                OrderDetailAllBean orderDetailAllBean = orderDetailAllBeanList.get(j);
                boolean isObject = orderDetailAllBean.getIsObject();
                //对象
                if (isObject) {
                    //订单详情
                    OrderDetailObjBean.RootBean.DataBean data = orderDetailAllBean.getOrderDetailObjBean().getRoot().getData();
                    long no = data.getNo();
                    //订单号一样
                    if (number == no) {
                        mOrderCurrentCount--;
                        OrderDetailItemBean item = data.getData().getItems();
                        //设置商品id
                        orderDataBean.setGid(item.getItem_id());
                        orderDataBean.setIsMore(false);
                        orderDataBean.setName(item.getName());
                        orderDataBean.setSingleCount(item.getQuantity());
                        orderDataBean.setSinglePrice(item.getUnit_price());
                        //给收件人地址赋值
                        ItemBean address = new ItemBean();
                        address.dinfo_received_name = data.getReceived_name();
                        address.dinfo_mobile = data.getTel();
                        address.provinceName = data.getProvinceName();
                        address.cityName = data.getCityName();
                        address.areaName = data.getAreaName();
                        address.townName = data.getTownName();
                        orderDataBean.setAddress(address);
                        //给支付所需的bean赋值
                        ArrayList<CommonBean> CommonBeans = new ArrayList<>();
                        CommonBean commonBean = new CommonBean();
                        commonBean.name = item.getName();
                        commonBean.num = item.getQuantity();
                        commonBean.content = item.getIcon().getContent();
                        CommonBeans.add(commonBean);
                        orderDataBean.setCommonBeans(CommonBeans);
                        //添加物流信息
                        orderDataBean.setDeliveryRootBean(data.getDeliveryRootBean());

                        //规格和颜色
                        OrderDetailItemBean items = data.getData().getItems();
                        orderDataBean.setColor_name(items.getColor_name());
                        orderDataBean.setColor_alias(items.getColor_alias());
                        orderDataBean.setSku_size(items.getSku_size());
                        orderDataBean.setSize_alias(items.getSize_alias());
                        break;
                    }
                } else {
                    //集合,取第一个对象
                    OrderDetailArryBean.RootBean.DataBean data = orderDetailAllBean.getOrderDetailArryBean().getRoot().getData();
                    long no = data.getNo();
                    if (number == no) {
                        orderDataBean.setIsMore(true);
                        mOrderCurrentCount = mOrderCurrentCount - data.getData().getItems().size();
                        OrderDetailItemBean orderDetailItemBean = data.getData().getItems().get(0);
                        orderDataBean.setGid(orderDetailItemBean.getItem_id());
                        orderDataBean.setName(orderDetailItemBean.getName());
                        orderDataBean.setSingleCount(orderDetailItemBean.getQuantity());
                        orderDataBean.setSinglePrice(orderDetailItemBean.getUnit_price());
                        orderDataBean.getImage().setContent(orderDetailItemBean.getIcon().getContent());
                        //给收件人地址赋值
                        ItemBean address = new ItemBean();
                        address.dinfo_received_name = data.getReceived_name();
                        address.dinfo_mobile = data.getTel();
                        address.provinceName = data.getProvinceName();
                        address.cityName = data.getCityName();
                        address.areaName = data.getAreaName();
                        address.townName = data.getTownName();
                        orderDataBean.setAddress(address);
                        //给支付所需的bean赋值
                        ArrayList<CommonBean> CommonBeans = new ArrayList<>();
                        List<OrderDetailItemBean> items = data.getData().getItems();
                        for (int k = 0; k < items.size(); k++) {
                            CommonBean commonBean = new CommonBean();
                            OrderDetailItemBean orderDetailItemBeans = items.get(k);
                            commonBean.name = orderDetailItemBeans.getName();
                            commonBean.num = orderDetailItemBeans.getQuantity();
                            commonBean.content = orderDetailItemBeans.getIcon().getContent();
                            CommonBeans.add(commonBean);
                        }
                        orderDataBean.setCommonBeans(CommonBeans);
                        //添加物流信息
                        orderDataBean.setDeliveryRootBean(data.getDeliveryRootBean());

                        //规格和颜色
                        OrderDetailItemBean items2 = data.getData().getItems().get(0);
                        orderDataBean.setColor_name(items2.getColor_name());
                        orderDataBean.setColor_alias(items2.getColor_alias());
                        orderDataBean.setSku_size(items2.getSku_size());
                        orderDataBean.setSize_alias(items2.getSize_alias());
                        break;
                    }
                }
            }
        }
        //有数据
        mSuccessView.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);
        noNetView.setVisibility(View.GONE);
        //判断是否还有更多数据
        if (mOrderBeanList.getHas_more_items() == 1) {
            //有"更多"数据
            if (mOrderCurrentCount > 0) {
                mHasMore = true;
                ll_has_more.setVisibility(View.VISIBLE);
                no_has_more.setVisibility(View.GONE);
            } else {
                mHasMore = false;
                ll_has_more.setVisibility(View.GONE);
                if (isFirst) {
                    no_has_more.setVisibility(View.VISIBLE);
                } else {
                    no_has_more.setVisibility(View.GONE);
                }
            }
        } else {
            //没有"更多"数据
            mHasMore = false;
            ll_has_more.setVisibility(View.GONE);
            if (!isFirst) {
                no_has_more.setVisibility(View.VISIBLE);
            } else {
                mListView.removeFooterView(mMoreView);
                //mMoreView.setVisibility(View.GONE);
            }
        }
        //加载数据
        if (!isFirst) {
            adapterDatas.addAll(orderDataBeanList);
            mAdapter.setData(adapterDatas);
            mAdapter.notifyDataSetChanged();
            //isLoadingMore = false;
            Toast.makeText(mContext, "加载成功!", Toast.LENGTH_SHORT).show();
        } else {
            adapterDatas.addAll(orderDataBeanList);
            mAdapter = new OrderAdapter(adapterDatas, mContext);
            mListView.setAdapter(mAdapter);
            isFirst = false;
            if (isRefresh) {
                Toast.makeText(mContext, "重新加载数据成功!", Toast.LENGTH_SHORT).show();
            }
        }
        //保存数据
        viewPagerBean.setFirst(isFirst);
        viewPagerBean.setEmpty(false);
        viewPagerBean.setPage(mPage);
        viewPagerBean.setAdapterDatas(adapterDatas);
        viewPagerBean.setOrderDetailAllBeanList(mOrderDetailAllBeanList);
        isLoadingMore = false;
        LoadDialog.dismiss(mContext);
        isRefresh = false;
        //mSwipeLayout.setRefreshing(false);
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

    //    /**
    //     * 下拉刷新的回调接口
    //     */
    //    @Override
    //    public void onRefresh() {
    //        if(!mSwipeLayout.isRefreshing()) {
    //
    //        }else{
    //            ToastUtil.showToast(mActivity,"正在刷新");
    //        }
    //        //isLoadingMore=false;
    //    }

       /*----------------------ListView监听部分-----------------------*/

    private class OrderOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
            //TODO
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mIsEnd && mHasMore && !isLoadingMore) {
                if (NetWorkUtil.isNetworkAvailable(mContext)) {
                    no_has_more.setVisibility(View.GONE);
                    ll_has_more.setVisibility(View.VISIBLE);
                    mPage += 1;
                    if (isTimeout) {
                        mPage -= 1;
                    }
                    isLoadingMore = true;
                    //TODO
                    //LoadDialog.show(mContext, false);
                    OrderHandler.searchOrder(handle, mPage, StartApp.limit, "2");
                } else {
                    ll_has_more.setVisibility(View.GONE);
                    no_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.request_net2));
                    Toast.makeText(mContext, getString(R.string.offline), Toast.LENGTH_SHORT).show();
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

    private class OrderAdapter extends BaseAdapter {
        private List<OrderDataBean> mList;
        private Context mContext;

        public OrderAdapter(ArrayList<OrderDataBean> list, Context context) {
            mList = list;
            mContext = context;
        }

        public void setData(ArrayList<OrderDataBean> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size() != 0 ? mList.size() : 0;
        }

        @Override
        public OrderDataBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            //初始化控件
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.order_listview_item, null);
                adapterInitView(viewHolder, convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //赋值
            OrderDataBean orderDataBean = mList.get(position);
            adapterInitData(viewHolder, orderDataBean);
            return convertView;
        }

        /**
         * 初始化adapter item的数据
         *
         * @param orderDataBean
         */
        private void adapterInitData(ViewHolder viewHolder, final OrderDataBean orderDataBean) {
            //店铺名
            viewHolder.order_shop_name_tv.setText(orderDataBean.getMname() + "");
            GlideUtil.getInstance().loadImage(mContext, orderDataBean.getImage().getContent(), R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, viewHolder.order_goods_picture_iv);
            //单价
            viewHolder.order_price_tv.setText("¥ " + CommonUtil.formatPoint(orderDataBean.getSinglePrice()));
            //单个数量
            viewHolder.order_goods_count_tv.setText("x " + orderDataBean.getSingleCount());
            //是否是同一家买多个商品
            viewHolder.order_list_more_iv.setVisibility(orderDataBean.getIsMore() ? View.VISIBLE : View.INVISIBLE);
            //商品名
            viewHolder.order_goods_name_tv.setText(orderDataBean.getName());
            //商品总数量
            viewHolder.order_all_count_tv.setText(String.format(getString(R.string.order_goods_count), orderDataBean.getQuantity()));
            //颜色规格
            viewHolder.order_goods_color_tv.setText(orderDataBean.getColor_alias() + ": ");
            viewHolder.order_goods_color2_tv.setText(orderDataBean.getColor_name());
            viewHolder.order_goods_standard_tv.setText(orderDataBean.getSize_alias() + ": ");
            viewHolder.order_goods_standard2_tv.setText(orderDataBean.getSku_size());

            double delivery_charges = orderDataBean.getDelivery_charges();
            String deliveryFee;
            if (delivery_charges == 0) {
                deliveryFee = "(包邮)";
            } else {
                deliveryFee = String.format(getString(R.string.order_delivery_charges), delivery_charges);
            }
            viewHolder.order_delivery_charges_tv.setText(deliveryFee);
            /**
             * 格式化运算
             */
            BigDecimal price2 = new BigDecimal(orderDataBean.getPrice());
            BigDecimal delivery_charges2 = new BigDecimal(orderDataBean.getDelivery_charges());
            double formatAllPrice = price2.add(delivery_charges2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String formatAllPrice2 = String.valueOf(formatAllPrice);
            //double all_price = orderDataBean.getPrice() + orderDataBean.getDelivery_charges();
            //double all_priceFormat = CommonUtil.formatPoint(all_price);
            //String formatAllPrice = String.valueOf(all_priceFormat);
            //商品总价
            viewHolder.order_all_price_tv.setText("¥ " + formatAllPrice2);
            //String[] split = formatAllPrice2.split("\\.");
            //viewHolder.order_all_price_tv.setText("¥ " + split[0]);
            //viewHolder.order_all_price_dot_tv.setText("." + split[1]);

            //规格和颜色
            viewHolder.order_goods_color_tv.setText(orderDataBean.getColor_alias() + ": ");
            viewHolder.order_goods_color2_tv.setText("".equals(orderDataBean.getColor_name()) ? "无" : orderDataBean.getColor_name());
            viewHolder.order_goods_standard_tv.setText(orderDataBean.getSize_alias() + ": ");
            viewHolder.order_goods_standard2_tv.setText("".equals(orderDataBean.getSku_size()) ? "无" : orderDataBean.getSku_size());
            //待发货
            viewHolder.order_left_btn.setVisibility(View.INVISIBLE);
            viewHolder.order_right_btn.setVisibility(View.VISIBLE);
            viewHolder.order_state_tv.setText(R.string.order_wait_deliver);
            viewHolder.order_right_btn.setText(R.string.order_remind_seller);
            viewHolder.order_right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "提醒卖家成功!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * 初始化adapter item的ui
         *
         * @param viewHolder
         * @param convertView
         */
        private void adapterInitView(ViewHolder viewHolder, View convertView) {
            //文字
            viewHolder.order_shop_name_tv = (TextView) convertView.findViewById(R.id.order_shop_name_tv);
            viewHolder.order_state_tv = (TextView) convertView.findViewById(R.id.order_state_tv);
            viewHolder.order_goods_name_tv = (TextView) convertView.findViewById(R.id.order_goods_name_tv);
            viewHolder.order_price_tv = (TextView) convertView.findViewById(R.id.order_price_tv);
            viewHolder.order_goods_count_tv = (TextView) convertView.findViewById(R.id.order_goods_count_tv);
            viewHolder.order_all_count_tv = (TextView) convertView.findViewById(R.id.order_all_count_tv);
            //viewHolder.order_all_count_tv = (TextView) convertView.findViewById(R.id.order_all_count_tv);
            viewHolder.order_all_price_tv = (TextView) convertView.findViewById(R.id.order_all_price_tv);
            //viewHolder.order_all_price_dot_tv = (TextView) convertView.findViewById(R.id.order_all_price_dot_tv);
            viewHolder.order_delivery_charges_tv = (TextView) convertView.findViewById(R.id.order_delivery_charges_tv);
            //图片
            viewHolder.order_goods_picture_iv = (ImageView) convertView.findViewById(R.id.order_goods_picture_iv);
            viewHolder.order_list_more_iv = (ImageView) convertView.findViewById(R.id.order_list_more_iv);
            //按钮
            viewHolder.order_right_btn = (Button) convertView.findViewById(R.id.order_right_btn);
            viewHolder.order_left_btn = (Button) convertView.findViewById(R.id.order_left_btn);
            //颜色 规格
            viewHolder.order_goods_color_tv = (TextView) convertView.findViewById(R.id.order_goods_color_tv);
            viewHolder.order_goods_color2_tv = (TextView) convertView.findViewById(R.id.order_goods_color2_tv);
            viewHolder.order_goods_standard_tv = (TextView) convertView.findViewById(R.id.order_goods_standard_tv);
            viewHolder.order_goods_standard2_tv = (TextView) convertView.findViewById(R.id.order_goods_standard2_tv);
        }

        class ViewHolder {
            private ImageView order_goods_picture_iv, order_list_more_iv;
            private TextView order_shop_name_tv, order_state_tv, order_goods_name_tv, order_delivery_charges_tv,
                    order_price_tv, order_goods_count_tv, order_all_count_tv,
                    order_all_price_tv, order_all_price_dot_tv,
                    order_goods_color_tv, order_goods_color2_tv, order_goods_standard_tv, order_goods_standard2_tv;
            private Button order_right_btn, order_left_btn;
        }
    }

    /**
     * 重复点击某种订单广播监听
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("reselectedListener")) {
                if (intent.getIntExtra(ARG_POSITION, -1) == 2) {
                    if (NetWorkUtil.isNetworkAvailable(mContext)) {
                        isRefresh = true;
                        LoadDialog.show(mContext);
                        resetData();
                        getDataByNetStatus();
                    } else {
                        Toast.makeText(mContext, getString(R.string.offline), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
