package com.winguo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.net.GlideUtil;
import com.winguo.productList.modle.ItemEntitys;

import java.util.List;

/**
 * Created by admin on 2017/7/3.
 */

public class MySpaceShopAdapter extends BaseQuickAdapter<ItemEntitys, BaseViewHolder> {


    public MySpaceShopAdapter(@LayoutRes int layoutResId, @Nullable List<ItemEntitys> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntitys item) {

        String iconUrl = item.icon.content;
        GlideUtil.getInstance().loadImage(mContext, iconUrl, R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg,  (ImageView) helper.getView(R.id.iv_product_list_icon));
        helper.setText(R.id.tv_product_list_name,item.name);

        if (TextUtils.isEmpty(item.price.special)) {
            helper.setText(R.id.tv_product_list_price,String.format(mContext.getResources().getString(R.string.product_list_price), item.price.regular));
        } else {
            helper.setText(R.id.tv_product_list_price,String.format(mContext.getResources().getString(R.string.product_list_price), item.price.special));
        }

       helper.setText(R.id.tv_product_list_sales_volume,String.format(mContext.getResources().getString(R.string.product_list_sales_volume), item.sale_qty));
       helper.setText(R.id.tv_product_list_location,String.format(mContext.getResources().getString(R.string.product_list_product_location), item.cityname));

    }

}
