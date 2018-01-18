package com.winguo.lbs.searchresult;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.lbs.bean.SearchResultGoodsBean;
import com.winguo.net.GlideUtil;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class SearchResultGoodsAdapter extends BaseQuickAdapter<SearchResultGoodsBean.ContentBean, BaseViewHolder> {

    public SearchResultGoodsAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, SearchResultGoodsBean.ContentBean item) {
        helper.setText(R.id.search_result_goods_name_tv, item.getName());
        helper.setText(R.id.search_result_goods_price_tv, String.format(mContext.getString(R.string.search_result_goods_price), item.getPrice()));
        helper.setText(R.id.search_result_goods_number_tv, String.format(mContext.getString(R.string.search_result_goods_number), item.getSales()));
        ImageView search_result_goods_iv = helper.getView(R.id.search_result_goods_iv);
        GlideUtil.getInstance().loadImage(mContext, item.getImg_url(), R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, search_result_goods_iv);
        String m = item.getM();
        helper.setText(R.id.search_result_goods_distance_tv, String.format(mContext.getString(R.string.search_result_goods_distance), m));
    }
}
