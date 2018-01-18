package com.winguo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iflytek.cloud.thirdparty.T;
import com.winguo.R;
import com.winguo.bean.TodayShop;
import com.winguo.net.GlideUtil;
import com.winguo.productList.modle.ItemEntitys;
import com.winguo.utils.ScreenUtil;

import java.util.List;

/**
 * Created by Admin on 2017/5/13.
 */

public class RecyclerClassifyAdapter extends BaseQuickAdapter<ItemEntitys, BaseViewHolder> {


    public RecyclerClassifyAdapter(@LayoutRes int layoutResId, @Nullable List<ItemEntitys> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntitys item) {
        ImageView iv_classify_item=helper.getView(R.id.iv_classify_item_ic);
        ViewGroup.LayoutParams layoutParams = iv_classify_item.getLayoutParams();
        int w = (ScreenUtil.getScreenWidth((Activity) mContext) - 4) / 2;
        layoutParams.width = w;
        layoutParams.height = w;
        iv_classify_item.setLayoutParams(layoutParams);
        GlideUtil.getInstance().loadImage(mContext,item.icon.content,R.drawable.electric_theme_loading_bg,R.drawable.electric_theme_error_bg,iv_classify_item);
        helper.setText(R.id.iv_classify_item_title,item.name);
        helper.setText(R.id.iv_classify_item_price,"¥ "+item.price.regular);
    }
}
