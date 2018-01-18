package com.winguo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.winguo.activity.ShopCenterActivity;
import com.winguo.home.bean.BannerBean;
import com.winguo.home.bean.ItemsBean;
import com.winguo.net.GlideUtil;
import com.winguo.utils.CommonUtil;

/**
 * 新闻列表适配器
 * Created by admin on 2017/1/11.
 */

public class AdvertisementAdapter extends PagerAdapter{

    Context context;
    private BannerBean bannerBean;


    public AdvertisementAdapter(Context context, BannerBean bannerBean) {
        this.context = context;
        this.bannerBean = bannerBean;
    }

    @Override
    public int getCount() {
        int count=0;
        if (bannerBean!=null) {
            if (bannerBean.message != null) {
                if (bannerBean.message.items!=null) {
                    if (bannerBean.message.items.data.size() > 0) {
                        count = bannerBean.message.items.data.size();
                    }
                }
            }
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        CommonUtil.printI("图片地址","---------------------------");
        if (bannerBean.message!=null){
            if (bannerBean.message.items!=null) {
                if (bannerBean.message.items.data.size() > 0) {
                    ItemsBean.DataBean dataBean = bannerBean.message.items.data.get(position);
                    CommonUtil.printI("图片地址", dataBean.image.content);
                    GlideUtil.getInstance().loadImage(context, dataBean.image.content, imageView);
                }
            }
        }
        container.addView(imageView);
        onCkickItem(imageView,position);
        return imageView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    private void onCkickItem(ImageView imageView, final int position) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerBean.message!=null){
                    if (bannerBean.message.items!=null) {
                        if (bannerBean.message.items.data.size() > 0) {
                            ItemsBean.DataBean dataBean = bannerBean.message.items.data.get(position);

                            if (!TextUtils.isEmpty(dataBean.url)) {
                                Intent it = new Intent(context, ShopCenterActivity.class);
                                it.putExtra("TargetUrl", dataBean.url);
                                context.startActivity(it);
                            }

                        }
                    }
                }
            }
        });
    }

}
