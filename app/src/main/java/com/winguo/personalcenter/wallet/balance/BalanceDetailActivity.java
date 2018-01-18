package com.winguo.personalcenter.wallet.balance;

import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.guobi.account.GBAccountError;
import com.winguo.R;
import com.winguo.activity.ProductActivity;
import com.winguo.adapter.BalanceDetailAdapter;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseActivity;
import com.winguo.bean.TodayShop;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.personalcenter.wallet.bean.BalanceDetail;
import com.winguo.personalcenter.wallet.bean.CashCouponDetail;
import com.winguo.personalcenter.wallet.control.MyWalletControl;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceDetailCallback;
import com.winguo.personalcenter.wallet.moudle.RequestCashCouponDetailCallback;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 余额交易明细
 */

public class BalanceDetailActivity extends BaseActivity implements RequestBalanceDetailCallback, BaseQuickAdapter.RequestLoadMoreListener {

    private FrameLayout top_back;
    private TextView layout_title;
    private TextView top_title_right;
    private TextView mywallet_cash_coupon, detail_type_title;
    private LinearLayout mywallet_center_coupon_rl;
    private RecyclerView cash_coupon_rv;
    private BalanceDetailAdapter adapter;
    private MyWalletControl control;

    @Override
    protected int getLayout() {
        return R.layout.activity_cash_coupon;
    }

    @Override
    protected void initData() {
        control = new MyWalletControl(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this, true);
            control.getBalanceDetail(this, 1, 0, 0, 10);
        } else {
            ToastUtil.showToast(this, getString(R.string.timeout));
        }
    }

    private int mCurrentCounter = 0;
    private boolean mLoadMoreEndGone = false;

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        top_title_right = (TextView) findViewById(R.id.top_title_right);
        mywallet_cash_coupon = (TextView) findViewById(R.id.mywallet_cash_coupon);
        detail_type_title = (TextView) findViewById(R.id.detail_type_title);
        cash_coupon_rv = (RecyclerView) findViewById(R.id.cash_coupon_rv);
        mywallet_center_coupon_rl = (LinearLayout) findViewById(R.id.mywallet_center_coupon_rl);

        cash_coupon_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cash_coupon_rv.setItemAnimator(new DefaultItemAnimator());
        cash_coupon_rv.addItemDecoration(new SpacesItemDecoration(1));

        adapter = new BalanceDetailAdapter(R.layout.cash_coupon_detail_item, data);
        adapter.setOnLoadMoreListener(BalanceDetailActivity.this, cash_coupon_rv);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        CustomLoadMoreView loadMoreView = new CustomLoadMoreView();
        adapter.setLoadMoreView(loadMoreView);
        cash_coupon_rv.setAdapter(adapter);

        layout_title.setText(getString(R.string.wallet_balance_detail));
        detail_type_title.setText(getString(R.string.wallet_balance_detail));
        Intent cash = getIntent();
        if (cash != null) {
            double balance = cash.getDoubleExtra(Constants.BALANCE_DETAIL, 0.00);
            mywallet_cash_coupon.setText(String.format(getString(R.string.mywallet_cash_coupon), balance + ""));
        }

    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
        }
    }

    private List<BalanceDetail.RootBean.ItemsBean.DataBean> data = new ArrayList<>();
    private int TOTAL = 0;

    @Override
    public void requestBalanceDetailResult(BalanceDetail balanceDetail) {
        LoadDialog.dismiss(this);
        if (balanceDetail != null) {
            //获取成功
            BalanceDetail.RootBean.ItemsBean items = balanceDetail.getRoot().getItems();
            TOTAL += items.getData().size();
            mCurrentCounter += adapter.getData().size();
            adapter.addData(items.getData());
        } else {
            if (mCurrentCounter >= TOTAL) {
                //如果当前数据量展示完毕 显示没有更多
                adapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visibl
            }
        }
    }

    @Override
    public void requestBalanceDetailErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this, errorcode));
    }

    private int currPage = 1;

    @Override
    public void onLoadMoreRequested() {
        // TODO: 2017/5/15  加载更多
        currPage++;
        adapter.loadMoreLoading();
        toLoadMorePage(currPage);

    }

    private void toLoadMorePage(int currPage) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            control.getBalanceDetail(this, currPage, 0, 0, 10);
        } else {
            adapter.loadMoreFail();
            ToastUtil.showToast(this, getString(R.string.no_net));
        }
    }
}
