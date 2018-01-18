package com.winguo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winguo.R;

/**
 * 搜索框切换 弹窗
 * Created by admin on 2017/4/18.
 */

public class SearchTypePopWindow extends PopupWindow {

    private Context context;
    private View.OnClickListener onClickListener;
    private final TextView sharedTV;
    private final TextView voiceHelpTV;
    private final TextView moreSearchTV;

    public SearchTypePopWindow(Context context, View.OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.onClickListener = onClickListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View moreView = inflater.inflate(R.layout.search_type_pop_select, null,false);
        this.setContentView(moreView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.AnimationPreview);

        LinearLayout shared = (LinearLayout) moreView.findViewById(R.id.search_type_pop_shop);
        LinearLayout voiceHelp = (LinearLayout) moreView.findViewById(R.id.search_type_pop_local);
        LinearLayout moreSearch = (LinearLayout) moreView.findViewById(R.id.search_type_pop_service);

        sharedTV = (TextView) moreView.findViewById(R.id.search_type_pop_shop_tv);
        voiceHelpTV = (TextView) moreView.findViewById(R.id.search_type_pop_local_tv);
        moreSearchTV = (TextView) moreView.findViewById(R.id.search_type_pop_service_tv);


        shared.setOnClickListener(onClickListener);
        voiceHelp.setOnClickListener(onClickListener);
        moreSearch.setOnClickListener(onClickListener);

    }

    public void showCurruentType(int curType){
        switch (curType) {
            case 1:
                sharedTV.setTextColor(Color.RED);
                voiceHelpTV.setTextColor(Color.WHITE);
                moreSearchTV.setTextColor(Color.WHITE);
                break;
            case 2:
                sharedTV.setTextColor(Color.WHITE);
                voiceHelpTV.setTextColor(Color.RED);
                moreSearchTV.setTextColor(Color.WHITE);
                break;
            case 3:
                sharedTV.setTextColor(Color.WHITE);
                voiceHelpTV.setTextColor(Color.WHITE);
                moreSearchTV.setTextColor(Color.RED);
                break;
        }
    }




}
