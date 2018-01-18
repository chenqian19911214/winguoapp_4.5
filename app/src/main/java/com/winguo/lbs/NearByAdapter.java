package com.winguo.lbs;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.lbs.bean.NearbyStoreBean;
import com.winguo.net.GlideUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class NearByAdapter extends BaseQuickAdapter<NearbyStoreBean.StoreListsBean, BaseViewHolder> {
    public NearByAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyStoreBean.StoreListsBean item) {
        String distance = item.getDistance();
        setDistance(helper, distance);
        ImageView shop_icon_iv = helper.getView(R.id.shop_icon_iv);
        List<NearbyStoreBean.StoreListsBean.StoreDetailBean> storeDetail = item.getStoreDetail();
        if (!storeDetail.isEmpty()) {
            //真实数据
            NearbyStoreBean.StoreListsBean.StoreDetailBean storeDetailBean = storeDetail.get(0);
            helper.setText(R.id.shop_name_tv,storeDetailBean.getM_maker_name_ch());
            String per_consumption = storeDetail.get(0).getM_entity_maker_average_consumption();
            helper.setText(R.id.shop_per_consumption_tv, String.format(mContext.getString(R.string.shop_per_consumption), per_consumption));
            String m_maker_logo = storeDetail.get(0).getM_maker_logo();
            if (TextUtils.isEmpty(m_maker_logo)) {
                shop_icon_iv.setImageResource(R.mipmap.ic_launcher);
            } else {
                GlideUtil.getInstance().loadImage(mContext, m_maker_logo, R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, shop_icon_iv);
            }
            String tradeName = storeDetail.get(0).getTradeName();
            helper.setText(R.id.shop_type_tradename_tv, tradeName);

        } else {
            //爬虫数据
            helper.setText(R.id.shop_name_tv,item.getShop_name());
            if (!"0".equals(item.getConsumption())) {
                helper.setText(R.id.shop_per_consumption_tv, String.format(mContext.getString(R.string.shop_per_consumption), item.getConsumption()));
            } else {
                helper.setText(R.id.shop_per_consumption_tv, String.format(mContext.getString(R.string.shop_per_consumption), "/"));
            }
            helper.setText(R.id.shop_type_tradename_tv, "未知类别");
            GlideUtil.getInstance().loadImage(mContext,item.getLogo(), R.drawable.column_theme_loading_bg, R.drawable.column_theme_error_bg, shop_icon_iv);

        }




    }

    private void setDistance(BaseViewHolder helper, String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            String substring = distance.substring(0, distance.length() - 2);
            double dis = Double.valueOf(substring);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format( dis / 1000);
                helper.setText(R.id.shop_distance_tv, String.format(mContext.getString(R.string.shop_distance),  format + "km"));
            } else {
                helper.setText(R.id.shop_distance_tv, String.format(mContext.getString(R.string.shop_distance), dis + "m"));
            }
        }
    }


}
