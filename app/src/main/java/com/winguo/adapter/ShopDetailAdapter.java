package com.winguo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.net.GlideUtil;

import java.util.List;

/**
 * Created by admin on 2017/6/14.
 */

public class ShopDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShopDetailAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ic =  helper.getView(R.id.store_shop_detail_ic);
        GlideUtil.getInstance().loadImage(mContext,item, R.drawable.big_banner_bg,R.drawable.big_banner_error_bg,ic);
       // helper.setText(R.id.store_shop_detail_title,item);
    }
}
