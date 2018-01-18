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

import com.guobi.account.GBAccountError;
import com.winguo.R;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseActivity;
import com.winguo.personalcenter.wallet.bean.CashCouponDetail;
import com.winguo.personalcenter.wallet.bean.PrestoreDetail;
import com.winguo.personalcenter.wallet.control.MyWalletControl;
import com.winguo.personalcenter.wallet.moudle.RequestCashCouponDetailCallback;
import com.winguo.personalcenter.wallet.moudle.RequestPrestoreDetailCallback;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 预存交易明细
 */

public class PrestoreDetailActivity extends BaseActivity implements RequestPrestoreDetailCallback {

    private FrameLayout top_back;
    private TextView layout_title;
    private TextView top_title_right,detail_type_title;
    private TextView mywallet_cash_coupon;
    private LinearLayout mywallet_center_coupon_rl;
    private RecyclerView cash_coupon_rv;
    private RecyclerCommonAdapter<PrestoreDetail.ItemBean> adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_cash_coupon;
    }

    @Override
    protected void initData() {
        MyWalletControl control = new MyWalletControl(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this, true);
            control.getPrestoreDetail(this);
        } else {
            ToastUtil.showToast(this,getString(R.string.timeout));
        }
    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        top_title_right = (TextView) findViewById(R.id.top_title_right);
        mywallet_cash_coupon = (TextView) findViewById(R.id.mywallet_cash_coupon);
        detail_type_title = (TextView) findViewById(R.id.detail_type_title);
        cash_coupon_rv = (RecyclerView) findViewById(R.id.cash_coupon_rv);
        mywallet_center_coupon_rl = (LinearLayout) findViewById(R.id.mywallet_center_coupon_rl);

        cash_coupon_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        cash_coupon_rv.setItemAnimator(new DefaultItemAnimator());
        cash_coupon_rv.addItemDecoration(new SpacesItemDecoration(1));

        adapter = new RecyclerCommonAdapter<PrestoreDetail.ItemBean>(this, R.layout.cash_coupon_detail_item, data) {
            @Override
            protected void convert(RecylcerViewHolder holder, PrestoreDetail.ItemBean itemBean, int position) {
                holder.setText(R.id.cash_detail,itemBean.getCash());
                holder.setText(R.id.cash_detail_title,itemBean.getTitle());
                holder.setText(R.id.cash_detail_time,itemBean.getTime());
            }

        };
        cash_coupon_rv.setAdapter(adapter);
        layout_title.setText(getString(R.string.wallet_prestore_detail));
        detail_type_title.setText(getString(R.string.wallet_prestore_detail));
        Intent cash = getIntent();
        if (cash != null) {
            double prestore = cash.getDoubleExtra(Constants.PRESTORE_DETAIL, 0.00);
            mywallet_cash_coupon.setText(String.format(getString(R.string.mywallet_cash_coupon),prestore+""));
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

    private List<PrestoreDetail.ItemBean> data = new ArrayList<>();

    @Override
    public void requestPrestoreDetailResult(PrestoreDetail prestoreDetail) {
        LoadDialog.dismiss(this);
        if (prestoreDetail.getCode() == 0) {
            //获取成功
            data.addAll(prestoreDetail.getItem());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestPrestoreDetailErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }
}
