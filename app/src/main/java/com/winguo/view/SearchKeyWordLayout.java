package com.winguo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.adapter.CommonAdapter;
import com.winguo.bean.SearchKeyWord;

/**
 * Created by admin on 2016/12/29.
 */

public class SearchKeyWordLayout extends LinearLayout {

    private Context mContext;
    private  SearchKeyWord item;
    private CommonAdapter adapter;

    public SearchKeyWordLayout(Context context, SearchKeyWord item,CommonAdapter adapter) {
        super(context);
        this.mContext = context;
        this.item = item;
        this.adapter = adapter;
        initView();
    }

    public SearchKeyWordLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchKeyWordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        LayoutInflater.from(mContext).inflate(R.layout.search_keyword_layout,this,true);
        ImageView iconType = (ImageView) findViewById(R.id.search_layout_img);
        TextView title = (TextView) findViewById(R.id.search_layout_title);
        IListView iListView = (IListView) findViewById(R.id.search_layout_lv);

        iconType.setImageResource(item.typeIcon);
        title.setText(item.typeText);
        iListView.setAdapter(adapter);
    }

}
