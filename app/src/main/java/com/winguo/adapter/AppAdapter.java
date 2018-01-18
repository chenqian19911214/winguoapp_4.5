package com.winguo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.example.localsearch.LocalAppFinder;
import com.example.localsearch.WGResultText;
import com.winguo.R;
import com.winguo.interfaces.OnCommentClickListener;

import java.util.List;

/**
 * App适配器
 * Created by admin on 2016/12/7.
 */

public class AppAdapter extends CommonAdapter<LocalAppFinder.AppResult> {



    private OnCommentClickListener onCommentClickListener;

    public AppAdapter(Context context, List<LocalAppFinder.AppResult> mDatas, int itemLayoutId, OnCommentClickListener onCommentClickListener) {
        super(context, mDatas, itemLayoutId);
        this.onCommentClickListener = onCommentClickListener;
    }

    @Override
    public void convert(ViewHolder helper, LocalAppFinder.AppResult item, int position) {
         String packageName = item.getPackageName();


        helper.setImageDrawable(R.id.app_item_ic,item.getIcon());
        helper.getView(R.id.app_item_ic).setOnClickListener(onCommentClickListener);
        helper.getView(R.id.app_item_ic).setTag(position);

        WGResultText name = item.getName();
         String content = name.getContent();
        int hightLightLen = name.getHightLightLen();
        int hightLightPos = name.getHightLightPos();
        SpannableString sp = new SpannableString(content);
        if (hightLightPos > -1 && hightLightLen > 0 && hightLightPos+hightLightLen< content.length()) {
            sp.setSpan(new ForegroundColorSpan(Color.RED),
                    hightLightPos, hightLightPos + hightLightLen, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.app_item_name,sp);
        helper.getView(R.id.app_item_fl).setOnClickListener(onCommentClickListener);
        helper.getView(R.id.app_item_fl).setTag(position);


    }


}
