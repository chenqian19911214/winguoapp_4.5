package com.winguo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.bean.StoreShop;
import com.winguo.net.GlideUtil;

import java.util.List;

/**
 * Created by admin on 2017/6/30.
 */

public class MySpaceStoreAdapter extends BaseQuickAdapter<StoreShop.ResultBean, BaseViewHolder> {

    public MySpaceStoreAdapter(@LayoutRes int layoutResId, @Nullable List<StoreShop.ResultBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreShop.ResultBean item) {
        ImageView shop_item_ic = helper.getView(R.id.shop_item_ic);
        GlideUtil.getInstance().loadImage(mContext, item.getItemthumb(), R.drawable.little_theme_loading_bg, R.drawable.little_theme_error_bg, shop_item_ic);
        helper.setText(R.id.shop_item_title, item.getM_item_name());
        if (!TextUtils.isEmpty(item.getM_item_min_price())) {
            helper.setText(R.id.shop_item_price, "¥" + item.getM_item_max_price());
        } else if (!TextUtils.isEmpty(item.getM_item_max_price())) {
            helper.setText(R.id.shop_item_price, "¥" + item.getM_item_min_price());
        } else {
            helper.setText(R.id.shop_item_price, "¥ /");
        }
    }
}
