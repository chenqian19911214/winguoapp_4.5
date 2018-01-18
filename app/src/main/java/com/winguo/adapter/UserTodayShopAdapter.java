package com.winguo.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.winguo.R;
import com.winguo.bean.UserTodayShop;

import java.util.List;

/**
 * Created by admin on 2017/4/27.
 */

public class UserTodayShopAdapter extends RecyclerCommonAdapter<UserTodayShop> {

    public UserTodayShopAdapter(Context context, int layoutId, List<UserTodayShop> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(RecylcerViewHolder holder, UserTodayShop userTodayShop, int position) {

        holder.setImageByUrl(R.id.t_goods_image_uri,userTodayShop.getImg2());
        holder.setText(R.id.t_goods_name,userTodayShop.getName());
        holder.setText(R.id.t_goods_m_category_name,"降价："+userTodayShop.getSales());//降价
        holder.setText(R.id.t_goods_m_sku_price,userTodayShop.getPrice());
        if (!TextUtils.isEmpty(userTodayShop.getArea())) {
            holder.setText(R.id.today_shop_city_name,userTodayShop.getArea());
        }
    }

}
