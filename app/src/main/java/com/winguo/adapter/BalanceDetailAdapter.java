package com.winguo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.personalcenter.wallet.bean.BalanceDetail;

import java.util.List;

/**
 * Created by admin on 2018/1/15.
 * 可提现交易明细
 */

public class BalanceDetailAdapter extends BaseQuickAdapter<BalanceDetail.RootBean.ItemsBean.DataBean, BaseViewHolder> {

    public BalanceDetailAdapter(int layoutResId, @Nullable List<BalanceDetail.RootBean.ItemsBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceDetail.RootBean.ItemsBean.DataBean dataBean) {
        helper.setText(R.id.cash_detail,dataBean.getPrice()+"");
        helper.setText(R.id.cash_detail_title,dataBean.getTitle());
        helper.setText(R.id.cash_detail_time,dataBean.getCreatedate());
    }
}
