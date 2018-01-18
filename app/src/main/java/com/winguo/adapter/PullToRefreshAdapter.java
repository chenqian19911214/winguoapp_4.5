package com.winguo.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.bean.TodayShop;
import com.winguo.net.GlideUtil;
import com.winguo.utils.ScreenUtil;

import java.util.List;

/**
 * Created by admin on 2017/5/11.
 */

public class PullToRefreshAdapter extends BaseQuickAdapter<TodayShop, BaseViewHolder> {

    public PullToRefreshAdapter(@LayoutRes int layoutResId, @Nullable List<TodayShop> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayShop item) {

        ImageView icon = helper.getView(R.id.guess_like_good_uri);
        ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
        int w = (ScreenUtil.getScreenWidth((Activity) mContext) - 4) / 2;
        layoutParams.width = w;
        layoutParams.height = w;
        icon.setLayoutParams(layoutParams);

        GlideUtil.getInstance().loadImage(mContext, item.getT_goods_image_uri(), R.drawable.like_loading_bg, R.drawable.like_error_bg, icon);
        helper.setText(R.id.guess_like_good_uri_title, item.getT_goods_name());

        helper.setText(R.id.guess_like_good_price, item.getMin_price());
       // helper.setText(R.id.guess_like_good_pay_number, item.getM_item_counts()+"人付款");

    }


}
