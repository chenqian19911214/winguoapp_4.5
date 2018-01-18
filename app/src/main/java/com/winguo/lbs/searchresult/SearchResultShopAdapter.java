package com.winguo.lbs.searchresult;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.lbs.bean.SearchResultShopBean;
import com.winguo.net.GlideUtil;

import java.text.DecimalFormat;

/**
 * @author hcpai
 * @time 2017/6/28  19:36
 * @desc ${TODD}
 */

public class SearchResultShopAdapter extends BaseQuickAdapter<SearchResultShopBean.ResultBean, BaseViewHolder> {

    public SearchResultShopAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultShopBean.ResultBean item) {
        helper.setText(R.id.shop_name_tv, item.getShop_name());

        double distance = item.getDistance();
        setDistance(helper, distance + "");

        String per_consumption = item.getM_entity_maker_average_consumption();
        helper.setText(R.id.shop_per_consumption_tv, String.format(mContext.getString(R.string.shop_per_consumption), per_consumption));

        ImageView shop_icon_iv = helper.getView(R.id.shop_icon_iv);
        String m_maker_logo = item.getLogo();
        GlideUtil.getInstance().loadImage(mContext, m_maker_logo, R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, shop_icon_iv);

        String tradeName = item.getTradeName();
        helper.setText(R.id.shop_type_tradename_tv, tradeName);
    }

    private void setDistance(BaseViewHolder helper, String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            double dis = Double.valueOf(distance);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format(dis / 1000);
                helper.setText(R.id.shop_distance_tv, String.format(mContext.getString(R.string.shop_distance), format + "km"));
            } else {
                helper.setText(R.id.shop_distance_tv, String.format(mContext.getString(R.string.shop_distance), dis + "m"));
            }
        }
    }
}
