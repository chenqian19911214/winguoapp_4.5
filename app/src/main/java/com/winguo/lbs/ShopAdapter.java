package com.winguo.lbs;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.lbs.bean.NearByRootBean;
import com.winguo.net.GlideUtil;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class ShopAdapter extends BaseQuickAdapter<NearByRootBean.ResultBean.StoreListsBean, BaseViewHolder> {
    public ShopAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearByRootBean.ResultBean.StoreListsBean item) {
        helper.setText(R.id.shop_name_tv, item.getShop_name());
        String per_consumption = item.getPer_consumption();
        if (per_consumption.equals("/")|| TextUtils.isEmpty(per_consumption)) {
            helper.setText(R.id.shop_per_consumption_tv, "");
        } else {
            helper.setText(R.id.shop_per_consumption_tv, String.format(mContext.getString(R.string.shop_per_consumption), item.getPer_consumption()));
        }
        ImageView shop_icon_iv = helper.getView(R.id.shop_icon_iv);
        GlideUtil.getInstance().loadImage(mContext, item.getShop_thumb(), R.drawable.column_theme_loading_bg, R.drawable.electric_theme_error_bg, shop_icon_iv);
    }
}
