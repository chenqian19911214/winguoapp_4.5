package com.winguo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.net.GlideUtil;
import com.winguo.productList.modle.ItemEntitys;

import java.util.List;

/**
 * Created by admin on 2017/5/17.
 */

public class HomeClassify2Adapter extends BaseQuickAdapter<ItemEntitys, BaseViewHolder> {

    public HomeClassify2Adapter(@LayoutRes int layoutResId, @Nullable List<ItemEntitys> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntitys item) {
        ImageView icon = helper.getView(R.id.home_classify2_item_img);
        GlideUtil.getInstance().loadImage(mContext, item.icon.content, R.drawable.beauty_theme_loading_bg, R.drawable.beauty_theme_error_bg, icon);
        helper.setText(R.id.home_classify2_item_title, item.name);
        helper.setText(R.id.home_classify2_item_price, item.price.regular);
    }
}
