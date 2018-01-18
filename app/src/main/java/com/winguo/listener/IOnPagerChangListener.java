package com.winguo.listener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.winguo.utils.Constants;
import com.winguo.utils.SPUtils;

import java.util.List;

/**
 * 启动 （首次安装）引导页
 * Created by admin on 2016/12/1.
 */

public class IOnPagerChangListener implements ViewPager.OnPageChangeListener {

    List<ImageView> points ;
    Context context ;
    int current = 0;
    ImageView nowUse;
    public IOnPagerChangListener(Context context, List<ImageView> points,ImageView nowUse) {
        this.context = context;
        this.points = points;
        this.nowUse = nowUse;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        nowUse.setVisibility(View.GONE);
        if (current == position) {
            ImageView imageView = points.get(position);
            imageView.setImageResource(android.R.drawable.presence_online);
        } else {
            ImageView currPoint = points.get(position);
            ImageView imageView = points.get(current);
            currPoint.setImageResource(android.R.drawable.presence_online);
            imageView.setImageResource(android.R.drawable.presence_offline);
            current = position;
        }

        if(position ==  points.size()-1){
            nowUse.setVisibility(View.VISIBLE);
            SPUtils.put(context, Constants.isGuide,true);
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
