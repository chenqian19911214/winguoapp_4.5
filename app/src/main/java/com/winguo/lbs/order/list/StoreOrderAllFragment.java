package com.winguo.lbs.order.list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.activity.ConfirmOrderActivity;
import com.winguo.base.BaseFragment2;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.lbs.order.StoreOrderControl;
import com.winguo.lbs.order.detail.StoreOrderDetailActivity;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc 实体店全部订单
 */

public class StoreOrderAllFragment extends BaseFragment2 implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, StoreOrderAdapter.IBaseViewHolderListener, BaseQuickAdapter.OnItemClickListener {
    private View emptyView;
    private View errorView;
    private View successView;
    private RecyclerView mRv;
    private String mId;
    private int mPage = 1;
    private List<StoreOrderBean.ContentBean> mData;
    private StoreOrderAdapter mAdapter;
    private boolean isLoadFail = false;
    private FrameLayout mStore_wait_pay_container;

    /**
     * 布局资源
     *
     * @return
     */
    @Override
    protected int getLayout() {
        return R.layout.fragment_store_order_wait_pay;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    @Override
    protected void initView(View view) {
        mStore_wait_pay_container = (FrameLayout) view.findViewById(R.id.store_wait_pay_container);
        //给容器添加不同布局
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //1、返回数据为空
        emptyView = View.inflate(mContext, R.layout.loading_empty, null);
        TextView textView = (TextView) emptyView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.store_order_no_data));
        mStore_wait_pay_container.addView(emptyView, params);

        //2、没有网络或请求失败
        errorView = View.inflate(mContext, R.layout.no_net, null);
        TextView no_net_tv = (TextView) errorView.findViewById(R.id.no_net_tv);
        errorView.findViewById(R.id.btn_request_net).setOnClickListener(this);
        Drawable drawableTop1 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop1, null, null);
        mStore_wait_pay_container.addView(errorView, params);

        //3.有数据
        successView = View.inflate(mContext, R.layout.nearby_rv, null);
        mRv = (RecyclerView) successView.findViewById(R.id.nearby_rv);
        mRv.setLayoutManager(new LinearLayoutManager(mContext));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mStore_wait_pay_container.addView(successView, params);

        mData = new ArrayList<>();
        //初始化适配器
        mAdapter = new StoreOrderAdapter(R.layout.store_order_item);
        mAdapter.setOnLoadMoreListener(this, mRv);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());


        //全部状态gone掉
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);
    }

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        WinguoAccountGeneral winguoGeneral = GBAccountMgr.getInstance().getAccountInfo().winguoGeneral;
        if (winguoGeneral != null) {
            mId = winguoGeneral.id;
            //mId = "39361";
            mPage = 1;
            requestData();
        } else {
            ToastUtil.showToast(mContext,"登录过期，请重新登录！");
        }
    }

    private void requestData() {
        if (NetworkUtils.isConnectNet(mContext)) {
            LoadDialog.show(mContext);
            StoreOrderControl.requestStoreOrder(mContext,mId, 0, 1, new StoreOrderControl.IStoreOrder() {
                @Override
                public void callback(StoreOrderBean storeOrderBean) {
                    LoadDialog.dismiss(mContext);
                    if (storeOrderBean != null) {
                        handlerStore(storeOrderBean);
                    } else {
                        errorView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void handlerStore(StoreOrderBean storeOrderBean) {
        successView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        if (storeOrderBean.getSize() != 0) {
            mData.addAll(storeOrderBean.getContent());
            mAdapter.setData(mData);
            mRv.setAdapter(mAdapter);
            //有更多
            if (storeOrderBean.getHas_more() == 1) {
                mAdapter.loadMoreComplete();
                //没有更多
            } else {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            emptyView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.GONE);
        }
    }

    private void requestDataAgain() {
        if (NetworkUtils.isConnectNet(mContext)) {
            StoreOrderControl.requestStoreOrder(mPage, new StoreOrderControl.IStoreOrder() {
                @Override
                public void callback(StoreOrderBean storeOrderBean) {
                    LoadDialog.dismiss(mContext);
                    if (storeOrderBean != null) {
                        handlerStoreAgain(storeOrderBean);
                    } else {
                        isLoadFail = true;
                        mAdapter.loadMoreFail();
                    }
                }
            });
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void handlerStoreAgain(StoreOrderBean storeOrderBean) {
        isLoadFail = false;
        mData.addAll(storeOrderBean.getContent());
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
        //有更多
        if (storeOrderBean.getHas_more() == 1) {
            mAdapter.loadMoreComplete();
            //没有更多
        } else {
            mAdapter.loadMoreEnd(false);
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        mAdapter.setListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_net_tv:
                requestData();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (isLoadFail) {
            --mPage;
        } else {
            ++mPage;
        }
        requestDataAgain();
    }

    //按钮的点击事件
    @Override
    public void callback(StoreOrderBean.ContentBean item) {
        StoreOrderBean.ContentBean.MakerBean maker = item.getMaker();
        String status = maker.getT_juchu_payment_status();
        final String code = maker.getT_juchu_pick_code();
        //待付款
        if ("1".equals(status)) {
            //跳转到支付
            Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("commonBeen", setData(item));
            intent.putExtras(bundle);
            intent.putExtra("isphysical", 1);
            intent.putExtra("name", maker.getT_juchu_delivery_received_name());
            intent.putExtra("phone", maker.getM_customer_tel_mobi());
            intent.putExtra("price", maker.getT_juchu_price());
            intent.putExtra("orderId", maker.getM_merge_order_no());
            startActivity(intent);
            //待备货
        } else if ("2".equals(status)) {
            ToastUtil.showToast(mContext, "提醒商家备货成功");
            //待提货
        } else if ("3".equals(status)) {
            new StoreOrderCodePopupWindow(mContext, new StoreOrderCodePopupWindow.ICallback() {
                @Override
                public String getCode() {
                    return code;
                }
            }).showAtLocation(mStore_wait_pay_container, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 处理 跳转到支付所需的javaBean
     *
     * @param item
     * @return
     */
    private ArrayList<CommonBean> setData(StoreOrderBean.ContentBean item) {
        ArrayList<CommonBean> commonBeans = new ArrayList<>();
        List<StoreOrderBean.ContentBean.ItemBean> item1 = item.getItem();
        for (int i = 0; i < item1.size(); i++) {
            StoreOrderBean.ContentBean.ItemBean itemBean = item1.get(i);
            CommonBean commonBean = new CommonBean();
            commonBean.name = itemBean.getName();
            commonBean.num = Integer.valueOf(itemBean.getNum().split("\\.")[0]);
            commonBean.content = itemBean.getImg_url();
            commonBeans.add(commonBean);
        }
        return commonBeans;
    }

    //条目点击事件
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StoreOrderBean.ContentBean.MakerBean maker = mData.get(position).getMaker();
        if ("4".equals(maker.getT_juchu_payment_status())) {
            ToastUtil.showToast(mContext, "该订单已关闭!");
        } else if ("5".equals(maker.getT_juchu_payment_status())) {
            ToastUtil.showToast(mContext, "该订单已完成!");
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, StoreOrderDetailActivity.class);
            intent.putExtra("store_order_id", maker.getId());
            intent.putExtra("order_status", maker.getT_juchu_payment_status());
            startActivity(intent);
        }
    }

}
